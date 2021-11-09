/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';
import * as lo from 'lodash';

import {
    functionsUtils,
    nodeUtils,
    AggregatedNode,
    ComponentEntry,
    ComponentObject,
    CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS,
    CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS,
    ID_ATTRIBUTE,
    IPageInfoService,
    IPositionRegistry,
    IResizeListener,
    ISmartEditContractChangeListener,
    JQueryUtilsService,
    LogService,
    MUTATION_CHILD_TYPES,
    MUTATION_TYPES,
    PolyfillService,
    RemoveComponentObject,
    SeDowngradeService,
    SystemEventService,
    SMARTEDIT_COMPONENT_PROCESS_STATUS,
    TargetedNode,
    TestModeService,
    TypedMap,
    TYPE_ATTRIBUTE,
    UUID_ATTRIBUTE,
    YJQUERY_TOKEN
} from 'smarteditcommons';

import { ComponentHandlerService } from './ComponentHandlerService';

enum COMPONENT_STATE {
    ADDED = 'added',
    DESTROYED = 'destroyed'
}

/*
 * interval at which manual listening/checks are executed
 * So far it is only by repositionListener
 * (resizeListener delegates to a self-contained third-party library and DOM mutations observation is done in native MutationObserver)
 */
/* @internal */
export const DEFAULT_REPROCESS_TIMEOUT = 100;

/* @internal */
export const DEFAULT_PROCESS_QUEUE_POLYFILL_INTERVAL = 250;

/* @internal */
export const DEFAULT_CONTRACT_CHANGE_LISTENER_INTERSECTION_OBSERVER_OPTIONS = {
    // The root to use for intersection.
    // If not provided, use the top-level document’s viewport.
    root: null as HTMLElement,

    // Same as margin, can be 1, 2, 3 or 4 components, possibly negative lengths.
    // If an explicit root element is specified, components may be percentages of the
    // root element size. If no explicit root element is specified, using a percentage
    // is an error.
    rootMargin: '1000px',

    // Threshold(s) at which to trigger callback, specified as a ratio, or list of
    // ratios, of (visible area / total area) of the observed element (hence all
    // entries must be in the range [0, 1]). Callback will be invoked when the visible
    // ratio of the observed element crosses a threshold in the list.
    threshold: 0
};

/* @internal */
export const DEFAULT_CONTRACT_CHANGE_LISTENER_PROCESS_QUEUE_THROTTLE = 500;

/* @internal */
@SeDowngradeService(ISmartEditContractChangeListener)
export class SmartEditContractChangeListener implements ISmartEditContractChangeListener {
    /*
     * list of smartEdit component attributes the change of which we observe to trigger an onComponentChanged event
     */
    private smartEditAttributeNames: string[];

    /*
     * This is the configuration passed to the MutationObserver instance
     */
    private MUTATION_OBSERVER_OPTIONS: MutationObserverInit = {
        /*
         * enables observation of attribute mutations
         */
        attributes: true,
        /*
         * instruct the observer to keep in store the former values of the mutated attributes
         */
        attributeOldValue: true,
        /*
         * enables observation of addition and removal of nodes
         */
        childList: true,
        characterData: false,
        /*
         * enables recursive lookup without which only addition and removal of DIRECT children of the observed DOM root would be collected
         */
        subtree: true
    };

    /*
     * unique instance of a MutationObserver on the body (enough since subtree:true)
     */
    private mutationObserver: MutationObserver;

    /*
     * unique instance of a IntersectionObserver
     */
    private intersectionObserver: IntersectionObserver;

    /*
     * unique instance of a custom listener for repositioning invoking the positionRegistry
     */
    private repositionListener: number;

    /*
     * holder of the current value of the page since previous onPageChanged event
     */
    private currentPage: string;

    /*
     * Component state values
     * 'added' when _componentsAddedCallback was called
     * 'destroyed' when _componentsRemovedCallback was called
     */
    private enableExtendedView = false;

    /*
     * nullable callbacks provided to smartEditContractChangeListener for all the observed events
     */
    private _componentsAddedCallback: (
        components: HTMLElement[],
        isEconomyMode: boolean
    ) => void = null;
    private _componentsRemovedCallback: (
        components: { component: HTMLElement; parent: HTMLElement }[],
        isEconomyMode: boolean
    ) => void = null;
    private _componentResizedCallback: (component: HTMLElement) => void = null;
    private _componentRepositionedCallback: (component: HTMLElement) => void = null;
    private _onComponentChangedCallback: (
        component: HTMLElement,
        oldAttributes: TypedMap<string>
    ) => void = null;
    private _pageChangedCallback: (pageUUID: string) => void = null;

    private _throttledProcessQueue = lo.throttle(
        () => this._rawProcessQueue(),
        DEFAULT_CONTRACT_CHANGE_LISTENER_PROCESS_QUEUE_THROTTLE
    );

    /*
     * Queue used to process components when intersecting the viewport
     * {Array.<{isIntersecting: Boolean, parent: DOMElement, processed: SmartEditContractChangeListener.COMPONENT_STATE}>}
     */
    private componentsQueue = new Map<Element, ComponentEntry>();

    private economyMode: boolean;

    constructor(
        private yjQueryUtilsService: JQueryUtilsService,
        private componentHandlerService: ComponentHandlerService,
        private pageInfoService: IPageInfoService,
        private resizeListener: IResizeListener,
        private positionRegistry: IPositionRegistry,
        private logService: LogService,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private systemEventService: SystemEventService,
        private polyfillService: PolyfillService,
        private testModeService: TestModeService
    ) {
        this.smartEditAttributeNames = [TYPE_ATTRIBUTE, ID_ATTRIBUTE, UUID_ATTRIBUTE];
    }

    /*
     * wrapping for test purposes
     */
    _newMutationObserver(callback: MutationCallback): MutationObserver {
        return new MutationObserver(callback);
    }

    /*
     * wrapping for test purposes
     */
    _newIntersectionObserver(callback: IntersectionObserverCallback): IntersectionObserver {
        return new IntersectionObserver(
            callback,
            DEFAULT_CONTRACT_CHANGE_LISTENER_INTERSECTION_OBSERVER_OPTIONS
        );
    }

    /*
     * Add the given entry to the componentsQueue
     * The components in the queue are sorted according to their position in the DOM
     * so that the adding of components is done to have parents before children
     */
    _addToComponentQueue(entry: IntersectionObserverEntry): void {
        const component = this.componentsQueue.get(entry.target);
        if (component) {
            component.isIntersecting = entry.isIntersecting;
        } else if (this.yjQueryUtilsService.isInDOM(entry.target as HTMLElement)) {
            this.componentsQueue.set(entry.target, {
                component: entry.target as HTMLElement,
                isIntersecting: entry.isIntersecting,
                processed: null,
                oldProcessedValue: null,
                parent: this.componentHandlerService.getParent(entry.target as HTMLElement)[0]
            });
        }
    }

    /*
     * for e2e test purposes
     */
    _componentsQueueLength(): number {
        return this.componentsQueue.size;
    }

    isExtendedViewEnabled(): boolean {
        return this.enableExtendedView;
    }

    /**
     * Set the 'economyMode' to true for better performance.
     * In economyMode, resize/position listeners are not present, and the current economyMode value is passed to the add /remove callbacks.
     */
    setEconomyMode(_mode: boolean): void {
        this.economyMode = _mode;

        if (!this.economyMode) {
            // reactivate
            Array.from(
                this.componentHandlerService.getFirstSmartEditComponentChildren(
                    this.yjQuery('body')
                ) as any
            ).forEach((firstLevelComponent: HTMLElement) => {
                this.applyToSelfAndAllChildren(firstLevelComponent, (node: HTMLElement) => {
                    this._registerSizeAndPositionListeners(node);
                });
            });
        }
    }

    /*
     * initializes and starts all Intersection/DOM listeners:
     * - Intersection of smartEditComponents with the viewport
     * - DOM mutations on smartEditComponents and page identifier (by Means of native MutationObserver)
     * - smartEditComponents repositioning (by means of querying, with an interval, the list of repositioned components from the positionRegistry)
     * - smartEditComponents resizing (by delegating to the injected resizeListener)
     */
    initListener(): void {
        this.enableExtendedView = this.polyfillService.isEligibleForExtendedView();

        try {
            this.pageInfoService.getPageUUID().then((pageUUID: string) => {
                this.currentPage = pageUUID;
                if (this._pageChangedCallback) {
                    this.executeCallback(
                        this._pageChangedCallback.bind(undefined, this.currentPage)
                    );
                }
            });
        } catch (e) {
            // case when the page that has just loaded is an asynchronous one
        }

        this.systemEventService.subscribe(
            CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS.RESTART_PROCESS,
            () => {
                this._processQueue();
                return Promise.resolve();
            }
        );

        // Intersection Observer not able to re-evaluate components that are not intersecting but going in and out of the extended viewport.
        if (this.enableExtendedView) {
            setInterval(() => {
                this._processQueue();
            }, DEFAULT_PROCESS_QUEUE_POLYFILL_INTERVAL);
        }

        this.mutationObserver = this._newMutationObserver(this.mutationObserverCallback.bind(this));
        this.mutationObserver.observe(document.body, this.MUTATION_OBSERVER_OPTIONS);

        // Intersection Observer is used to observe intersection of components with the viewport.
        // each time the 'isIntersecting' property of an entry (SmartEdit component) changes, the Intersection Callback is called.
        // we are using the componentsQueue to hold the components references and their isIntersecting value.
        this.intersectionObserver = this._newIntersectionObserver(
            (entries: IntersectionObserverEntry[]) => {
                entries.forEach((entry: IntersectionObserverEntry) => {
                    this._addToComponentQueue(entry);
                });
                // A better approach would be to process each entry individually instead of processing the whole queue, but a bug Firefox v.55 prevent us to do so.
                this._processQueue();
            }
        );

        // Observing all SmartEdit components that are already in the page.
        // Note that when an element visible in the viewport is removed, the Intersection Callback is called so we don't need to use the Mutation Observe to observe the removal of Nodes.
        Array.from(
            this.componentHandlerService.getFirstSmartEditComponentChildren(
                this.yjQuery('body')
            ) as any
        ).forEach((firstLevelComponent: HTMLElement) => {
            this.applyToSelfAndAllChildren(
                firstLevelComponent,
                this.intersectionObserver.observe.bind(this.intersectionObserver)
            );
        });

        this._startExpendableListeners();
    }

    // Processing the queue with throttling in production to avoid scrolling lag when there is a lot of components in the page.
    // No throttling when e2e mode is active
    _processQueue(): void {
        if (this.testModeService.isE2EMode() || functionsUtils.isUnitTestMode()) {
            this._rawProcessQueue();
        } else {
            this._throttledProcessQueue();
        }
    }

    isIntersecting(obj: ComponentEntry): boolean {
        if (!this.yjQueryUtilsService.isInDOM(obj.component)) {
            return false;
        }
        return this.enableExtendedView
            ? this.yjQueryUtilsService.isInExtendedViewPort(obj.component)
            : obj.isIntersecting;
    }

    // for each component in the componentsQueue, we use the 'isIntersecting' and 'processed' values to add or remove it.
    // An intersecting component that was not already added is added, and a non intersecting component that was added is removed (happens when scrolling, resizing the page, zooming, opening dev-tools)
    // the 'PROCESS_COMPONENTS' promise is RESOLVED when the component can be added or removed, and it is REJECTED when the component can't be added but could be removed.
    _rawProcessQueue(): void {
        const observedQueueArray = Array.from(this.componentsQueue.values());

        this.systemEventService
            .publish(
                CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS.PROCESS_COMPONENTS,
                observedQueueArray.map(({ component }) => component)
            )
            .then(
                function(observedQueue: ComponentEntry[], response: HTMLElement[]) {
                    const addedComponents: ComponentEntry[] = [];
                    const removedComponents: ComponentEntry[] = [];
                    const responseSet = new Set<HTMLElement>(response || []);

                    observedQueue.forEach((obj: ComponentEntry) => {
                        if (!responseSet.has(obj.component)) {
                            return;
                        }

                        const processStatus =
                            obj.component.dataset[SMARTEDIT_COMPONENT_PROCESS_STATUS];

                        if (
                            processStatus ===
                            CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS.PROCESS
                        ) {
                            if (
                                obj.processed !== COMPONENT_STATE.ADDED &&
                                this.isIntersecting(obj)
                            ) {
                                addedComponents.push(obj);
                            } else if (
                                obj.processed === COMPONENT_STATE.ADDED &&
                                !this.isIntersecting(obj)
                            ) {
                                removedComponents.push(obj);
                            }
                        } else if (
                            processStatus ===
                            CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS.REMOVE
                        ) {
                            if (obj.processed === COMPONENT_STATE.ADDED) {
                                removedComponents.push(obj);
                            }
                        }
                        obj.oldProcessedValue = obj.processed;
                    });

                    addedComponents.forEach((queueObj: ComponentEntry) => {
                        queueObj.processed = COMPONENT_STATE.ADDED;
                    });
                    removedComponents.forEach((queueObj: ComponentEntry) => {
                        queueObj.processed = COMPONENT_STATE.DESTROYED;
                    });

                    // If the intersection observer returns multiple time the same components in the callback (happen when doing a drag and drop or sfBuilder.actions.rerenderComponent)
                    // we will have these same components in BOTH addedComponents and removedComponents, hence we must first call _removeComponents and then _addComponents (in this order).
                    if (removedComponents.length) {
                        this._removeComponents(removedComponents);
                    }
                    if (addedComponents.length) {
                        addedComponents.sort(nodeUtils.compareHTMLElementsPosition('component'));
                        this._addComponents(addedComponents);
                    }
                    if (!this.economyMode) {
                        lo.chain(addedComponents.concat(removedComponents))
                            .filter((obj: ComponentEntry) => {
                                return (
                                    obj.oldProcessedValue === null ||
                                    !this.yjQueryUtilsService.isInDOM(obj.component)
                                );
                            })
                            .map('parent')
                            .compact()
                            .uniq()
                            .value()
                            .forEach((parent: HTMLElement) => {
                                this.repairParentResizeListener(parent);
                            });
                    }
                }.bind(this, observedQueueArray)
            );
    }

    _addComponents(componentsObj: ComponentEntry[]): void {
        if (this._componentsAddedCallback) {
            this.executeCallback(
                this._componentsAddedCallback.bind(
                    undefined,
                    lo.map(componentsObj, 'component'),
                    this.economyMode
                )
            );
        }
        if (!this.economyMode) {
            componentsObj
                .filter((queueObj: ComponentEntry) => {
                    return queueObj.oldProcessedValue === null;
                })
                .forEach((queueObj: ComponentObject) => {
                    this._registerSizeAndPositionListeners(queueObj.component);
                });
        }
    }

    _removeComponents(componentsObj: RemoveComponentObject[], forceRemoval: boolean = false): void {
        componentsObj
            .filter((queueObj: RemoveComponentObject) => {
                return !this.yjQueryUtilsService.isInDOM(queueObj.component) || forceRemoval;
            })
            .forEach((queueObj: RemoveComponentObject) => {
                if (!this.economyMode) {
                    this._unregisterSizeAndPositionListeners(queueObj.component);
                }
                this.componentsQueue.delete(queueObj.component);
            });
        if (this._componentsRemovedCallback) {
            const removedComponents = componentsObj.map((obj: RemoveComponentObject) => {
                return lo.pick(obj, ['component', 'parent', 'oldAttributes']);
            });
            this.executeCallback(
                this._componentsRemovedCallback.bind(undefined, removedComponents, this.economyMode)
            );
        }
    }

    _registerSizeAndPositionListeners(component: HTMLElement): void {
        if (this._componentRepositionedCallback) {
            this.positionRegistry.register(component);
        }
        if (this._componentResizedCallback) {
            this.resizeListener.register(
                component,
                this._componentResizedCallback.bind(undefined, component)
            );
        }
    }

    _unregisterSizeAndPositionListeners(component: HTMLElement): void {
        if (this._componentRepositionedCallback) {
            this.positionRegistry.unregister(component);
        }
        if (this._componentResizedCallback) {
            this.resizeListener.unregister(component);
        }
    }

    /*
     * stops and clean up all listeners
     */
    stopListener(): void {
        // Stop listening for DOM mutations
        if (this.mutationObserver) {
            this.mutationObserver.disconnect();
        }

        this.intersectionObserver.disconnect();

        this.mutationObserver = null;

        this._stopExpendableListeners();
    }

    _stopExpendableListeners(): void {
        // Stop listening for DOM resize
        this.resizeListener.dispose();
        // Stop listening for DOM repositioning
        if (this.repositionListener) {
            clearInterval(this.repositionListener);

            this.repositionListener = null;
        }
        this.positionRegistry.dispose();
    }

    _startExpendableListeners(): void {
        if (this._componentRepositionedCallback) {
            this.repositionListener = (setInterval(() => {
                this.positionRegistry
                    .getRepositionedComponents()
                    .forEach((component: HTMLElement) => {
                        this._componentRepositionedCallback(component);
                    });
            }, DEFAULT_REPROCESS_TIMEOUT) as unknown) as number;
        }
    }

    /*
     * registers a unique callback to be executed every time a smarteditComponent node is added to the DOM
     * it is executed only once per subtree of smarteditComponent nodes being added
     * the callback is invoked with the root node of a subtree
     */
    onComponentsAdded(callback: (components: HTMLElement[], isEconomyMode: boolean) => void): void {
        this._componentsAddedCallback = callback;
    }

    /*
     * registers a unique callback to be executed every time a smarteditComponent node is removed from the DOM
     * it is executed only once per subtree of smarteditComponent nodes being removed
     * the callback is invoked with the root node of a subtree and its parent
     */
    onComponentsRemoved(
        callback: (
            components: { component: HTMLElement; parent: HTMLElement }[],
            isEconomyMode: boolean
        ) => void
    ): void {
        this._componentsRemovedCallback = callback;
    }

    /*
     * registers a unique callback to be executed every time at least one of the smartEdit contract attributes of a smarteditComponent node is changed
     * the callback is invoked with the mutated node itself and the map of old attributes
     */
    onComponentChanged(
        callback: (component: HTMLElement, oldAttributes: TypedMap<string>) => void
    ): void {
        this._onComponentChangedCallback = callback;
    }

    /*
     * registers a unique callback to be executed every time a smarteditComponent node is resized in the DOM
     * the callback is invoked with the resized node itself
     */
    onComponentResized(callback: (component: HTMLElement) => void): void {
        this._componentResizedCallback = callback;
    }

    /*
     * registers a unique callback to be executed every time a smarteditComponent node is repositioned (as per Node.getBoundingClientRect()) in the DOM
     * the callback is invoked with the resized node itself
     */
    onComponentRepositioned(callback: (component: HTMLElement) => void) {
        this._componentRepositionedCallback = callback;
    }

    /*
     * registers a unique callback to be executed:
     * - upon bootstrapping smartEdit IF the page identifier is available
     * - every time the page identifier is changed in the DOM (see pageInfoService.getPageUUID())
     * the callback is invoked with the new page identifier read from pageInfoService.getPageUUID()
     */
    onPageChanged(callback: (pageUUID: string) => void): void {
        this._pageChangedCallback = callback;
    }

    /*
     * Method used in mutationObserverCallback that extracts from mutations the list of nodes added
     * The nodes are returned within a pair along with their nullable closest smartEditComponent parent
     */
    private aggregateAddedOrRemovedNodesAndTheirParents(
        mutations: MutationRecord[],
        type: MUTATION_CHILD_TYPES
    ): AggregatedNode[] {
        const entries = mutations
            .filter((mutation: MutationRecord) => {
                // only keep mutations of type childList and [added/removed]Nodes
                return (
                    mutation.type === MUTATION_TYPES.CHILD_LIST.NAME &&
                    mutation[type] &&
                    mutation[type].length
                );
            })
            .map((mutation: MutationRecord) => {
                // the mutated child may not be smartEditComponent, in such case we return their first level smartEditComponent children
                const children = lo
                    .flatten<HTMLElement>(
                        Array.from(mutation[type])
                            .filter((node: HTMLElement) => {
                                return node.nodeType === Node.ELEMENT_NODE;
                            })
                            .map((child: HTMLElement) => {
                                return this.componentHandlerService.isSmartEditComponent(child)
                                    ? child
                                    : Array.from(
                                          this.componentHandlerService.getFirstSmartEditComponentChildren(
                                              child
                                          ) as any
                                      );
                            })
                    )
                    .sort(nodeUtils.compareHTMLElementsPosition());

                // nodes are returned in pairs with their nullable parent
                const parent = this.componentHandlerService.getClosestSmartEditComponent(
                    mutation.target as HTMLElement
                );

                return children.map((node: HTMLElement) => {
                    return {
                        node,
                        parent: parent.length ? parent[0] : null
                    };
                });
            });

        /*
         * Despite MutationObserver specifications it so happens that sometimes,
         * depending on the very way a parent node is added with its children,
         * parent AND children will appear in a same mutation. We then must only keep the parent
         * Since the parent will appear first, the filtering lodash.uniqWith will always return the parent as opposed to the child which is what we need
         */

        return lo.uniqWith(lo.flatten(entries), (entry1, entry2) => {
            return entry1.node.contains(entry2.node) || entry2.node.contains(entry1.node);
        });
    }

    /*
     * Method used in mutationObserverCallback that extracts from mutations the list of nodes the attributes of which have changed
     * The nodes are returned within a pair along with their map of changed attributes
     */
    private aggregateMutationsOnChangedAttributes(mutations: MutationRecord[]): TargetedNode[] {
        const map = mutations.reduce((seed: Map<Node, TargetedNode>, mutation: MutationRecord) => {
            if (
                !(
                    mutation.target &&
                    mutation.target.nodeType === Node.ELEMENT_NODE &&
                    mutation.type === MUTATION_TYPES.ATTRIBUTES.NAME
                )
            ) {
                return seed;
            }

            let targetEntry = seed.get(mutation.target);

            if (!targetEntry) {
                targetEntry = {
                    node: mutation.target as HTMLElement,
                    oldAttributes: {}
                };
                seed.set(mutation.target, targetEntry);
            }

            targetEntry.oldAttributes[mutation.attributeName] = mutation.oldValue;
            return seed;
        }, new Map<Node, TargetedNode>());

        return Array.from(map.values());
    }

    /**
     * Verifies whether the entry is a smartedit complient element.
     */
    private isSmarteditNode(entry: TargetedNode): boolean {
        return (
            this.componentHandlerService.isSmartEditComponent(entry.node) &&
            this.smartEditAttributeNames.some((attributeName) => {
                return entry.node.hasAttribute(attributeName);
            })
        );
    }

    /**
     * Verifies whether at least one of the changed attributes is a smartedit attribute.
     */
    private isSmarteditAttributeChanged(entry: TargetedNode): boolean {
        return this.smartEditAttributeNames.some((attributeName) => {
            return entry.oldAttributes.hasOwnProperty(attributeName);
        });
    }

    /**
     * Verifies whether the entry is not a smartedit element anymore.
     * It checks that all smartedit related attributes were removed and the
     * entry.node is still in the componentsQueue.
     */
    private wasSmarteditNode(entry: TargetedNode): boolean {
        return (
            this.componentsQueue.has(entry.node) &&
            this.smartEditAttributeNames.every((attributeName) => {
                return !entry.node.hasAttribute(attributeName);
            })
        );
    }

    /*
     * Methods used in mutationObserverCallback that determines whether the smartEdit contract page identifier MAY have changed in the DOM
     */
    private mutationsHasPageChange(mutations: MutationRecord[]): MutationRecord {
        return mutations.find((mutation: MutationRecord) => {
            const element = mutation.target as Element;
            return (
                mutation.type === MUTATION_TYPES.ATTRIBUTES.NAME &&
                element.tagName === 'BODY' &&
                mutation.attributeName === 'class'
            );
        });
    }

    /*
     * convenience method to invoke a callback on a node and recursively on all its smartEditComponent children
     */
    private applyToSelfAndAllChildren(
        node: HTMLElement,
        callback: (target: HTMLElement) => void
    ): void {
        callback(node);
        Array.from(
            this.componentHandlerService.getFirstSmartEditComponentChildren(node) as any
        ).forEach((component: HTMLElement) => {
            this.applyToSelfAndAllChildren(component, callback);
        });
    }

    private repairParentResizeListener(parent: HTMLElement): void {
        if (parent) {
            // the adding of a component is likely to destroy the DOM added by the resizeListener on the parent, it needs be restored
            /*
             * since the DOM hierarchy is processed in order, by the time we need repair the parent,
             * it has already been processed so we can rely on its process status to know whether it is eligible
             */
            const parentObj = this.componentsQueue.get(parent);
            if (
                parentObj &&
                parentObj.processed === COMPONENT_STATE.ADDED &&
                this.yjQueryUtilsService.isInDOM(parent)
            ) {
                this._componentResizedCallback(parent);
            }
        }
    }

    /*
     * when a callback is executed we make sure that angular is synchronized since it is occurring outside the life cycle
     */
    private executeCallback(callback: () => void): void {
        if (this.testModeService.isE2EMode() || functionsUtils.isUnitTestMode()) {
            callback();
        } else {
            setTimeout(() => callback(), 0);
        }
    }

    /*
     * callback executed by the mutation observer every time mutations occur.
     * repositioning and resizing are not part of this except that every time a smartEditComponent is added,
     * it is registered within the positionRegistry and the resizeListener
     */
    private mutationObserverCallback(mutations: MutationRecord[]): void {
        this.logService.debug(mutations);

        if (this._pageChangedCallback && this.mutationsHasPageChange(mutations)) {
            this.pageInfoService.getPageUUID().then((newPageUUID: string) => {
                if (this.currentPage !== newPageUUID) {
                    this.executeCallback(this._pageChangedCallback.bind(undefined, newPageUUID));
                }
                this.currentPage = newPageUUID;
            });
        }

        if (this._componentsAddedCallback) {
            this.aggregateAddedOrRemovedNodesAndTheirParents(
                mutations,
                MUTATION_TYPES.CHILD_LIST.ADD_OPERATION
            ).forEach((childAndParent: AggregatedNode) => {
                this.applyToSelfAndAllChildren(
                    childAndParent.node,
                    this.intersectionObserver.observe.bind(this.intersectionObserver)
                );
            });
        }

        this.aggregateAddedOrRemovedNodesAndTheirParents(
            mutations,
            MUTATION_TYPES.CHILD_LIST.REMOVE_OPERATION
        ).forEach((childAndParent: AggregatedNode) => {
            this.applyToSelfAndAllChildren(childAndParent.node, (node: HTMLElement) => {
                const component = this.componentsQueue.get(node);
                if (component) {
                    if (!this.economyMode) {
                        this.repairParentResizeListener(childAndParent.parent);
                    }
                    this._removeComponents([
                        {
                            isIntersecting: false,
                            component: node,
                            parent: childAndParent.parent
                        }
                    ]);
                }
            });
        });

        if (this._onComponentChangedCallback) {
            // TODO: are we missing tests here?
            this.aggregateMutationsOnChangedAttributes(mutations).forEach((entry: TargetedNode) => {
                if (this.isSmarteditAttributeChanged(entry)) {
                    if (this.isSmarteditNode(entry)) {
                        const component = this.componentsQueue.get(entry.node);
                        if (!component) {
                            // Newly created smartedit element should always be in the component queue. If the component was created from
                            // a simple div tag by adding smartedit attributes we need first to add the component to the queue even if the current
                            // operation is change. If the component is not added to the queue it won't be rendered cause during the change callback
                            // it sometimes won't be able to find a parent overlay (if it is outside of the viewport).
                            this.applyToSelfAndAllChildren(
                                entry.node,
                                this.intersectionObserver.observe.bind(this.intersectionObserver)
                            );
                        } else {
                            // the onComponentChanged is called with the mutated smartEditComponent subtree and the map of old attributes
                            this.executeCallback(
                                this._onComponentChangedCallback.bind(
                                    undefined,
                                    entry.node,
                                    entry.oldAttributes
                                )
                            );
                        }
                    } else if (this.wasSmarteditNode(entry)) {
                        this.applyToSelfAndAllChildren(
                            entry.node,
                            this.intersectionObserver.unobserve.bind(this.intersectionObserver)
                        );
                        const parents = this.componentHandlerService.getClosestSmartEditComponent(
                            entry.node as HTMLElement
                        );
                        this._removeComponents(
                            [
                                {
                                    isIntersecting: false,
                                    component: entry.node,
                                    parent: parents.length ? parents[0] : null,
                                    oldAttributes: entry.oldAttributes
                                }
                            ],
                            true
                        );
                    }
                }
            });
        }
    }
}
