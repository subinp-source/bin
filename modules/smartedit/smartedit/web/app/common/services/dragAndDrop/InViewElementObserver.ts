/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { LogService } from '@smart/utils';
import { Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import * as lodash from 'lodash';

import { nodeUtils, stringUtils, MUTATION_TYPES } from 'smarteditcommons/utils';
import { SeDowngradeService } from 'smarteditcommons/di';
import { IMousePosition } from './index';
import { YJQUERY_TOKEN } from '../vendors/YjqueryModule';

const IN_VIEW_ELEMENTS_INTERSECTION_OBSERVER_OPTIONS = {
    // The root to use for intersection.
    // If not provided, use the top-level document’s viewport.
    root: null as HTMLElement,

    // Threshold(s) at which to trigger callback, specified as a ratio, or list of
    // ratios, of (visible area / total area) of the observed element (hence all
    // entries must be in the range [0, 1]). Callback will be invoked when the visible
    // ratio of the observed element crosses a threshold in the list.
    threshold: 0
} as IntersectionObserverInit;

/*
 * This is the configuration passed to the MutationObserver instance
 */
const IN_VIEW_ELEMENTS_MUTATION_OBSERVER_OPTIONS = {
    /*
     * enables observation of attribute mutations precisely for class="smartEditComponent" that may be added dynamically
     * turned to true dynamically if at least of the selectors is class sensitive
     */
    attributes: true,
    /*
     * instruct the observer not to keep in store the former values of the mutated attributes
     */
    attributeOldValue: false,
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

export interface QueueElement {
    component: HTMLElement;
    isIntersecting: boolean;
}

interface Selector {
    selector: string;
    callback?: () => void;
}
/**
 * InViewElementObserver maintains a collection of eligible DOM elements considered "in view".
 * An element is considered eligible if matches at least one of the selectors passed to the service.
 * An eligible element is in view when and only when it intersects with the view port of the window frame.
 * This services provides as well convenience methods around "in view" components:
 */
@SeDowngradeService()
export class InViewElementObserver {
    /*
     * unique instance of a MutationObserver on the body (enough since subtree:true)
     */
    private mutationObserver: any;

    /*
     * unique instance of a IntersectionObserver
     */
    private intersectionObserver: any;

    /*
     * Queue used to process components when intersecting the viewport
     * {Array.<{isIntersecting: Boolean, parent: DOMElement, processed: COMPONENT_STATE}>}
     */
    private componentsQueue: QueueElement[] = [];

    private selectors: Selector[] = [];

    private hasClassBasedSelectors = false;

    constructor(
        private logService: LogService,
        @Inject(DOCUMENT) private document: Document,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic
    ) {}

    /**
     * Retrieves the element targeted by the given mousePosition.
     * On some browsers, the native Javascript API will not work when targeting
     * an element inside an iframe from the container if a container overlay blocks it.
     * In such case we resort to returning the targeted element amongst the list of "in view" elements
     */
    elementFromPoint(mousePosition: IMousePosition): Element {
        const elementFromPointThroughNativeAPI = this.document.elementFromPoint(
            mousePosition.x,
            mousePosition.y
        );
        // we might potentially have an issue here if a browser has an intersection observer and document.elementFromPoint returns null.
        // Chrome version 66 when running in isE2EMode has the issue. But this is not likely to happen in reality because Chrome has an intersectionObserver
        // and hence it should just use the result from document.elementFromPoint.
        return (
            elementFromPointThroughNativeAPI ||
            this.getInViewElements().find((component) => {
                return nodeUtils.isPointOverElement(mousePosition, component as HTMLElement);
            })
        );
    }

    /**
     * Declares a new yjQuery selector in order to observe more elements.
     */
    addSelector(selector: string, callback?: () => void): () => void {
        if (
            !stringUtils.isBlank(selector) &&
            this.selectors.map((el) => el.selector).indexOf(selector) === -1
        ) {
            if (/\.[\w]+/.test(selector)) {
                this.hasClassBasedSelectors = true;
            }
            this.selectors.push({ selector, callback });
            this.restart();
        }

        return () => {
            const index = this.selectors.map((el) => el.selector).indexOf(selector);
            this.selectors.splice(index, 1);
            this.restart();
        };
    }

    /**
     * Retrieves the full list of eligible DOM elements even if they are not "in view".
     */
    getAllElements(): Element[] {
        return this.componentsQueue.map((element) => element.component);
    }

    /**
     * Retrieves the list of currently "in view" DOM elements.
     */
    getInViewElements(): Element[] {
        return this.componentsQueue
            .filter((element) => element.isIntersecting)
            .map((element) => element.component);
    }

    private restart() {
        this.stopListener();
        this.initListener();
    }

    /*
     * stops and clean up all listeners
     */
    private stopListener() {
        // Stop listening for DOM mutations
        if (this.mutationObserver) {
            this.mutationObserver.disconnect();
            delete this.mutationObserver;
        }

        if (this.intersectionObserver) {
            this.intersectionObserver.disconnect();
            delete this.intersectionObserver;
        }

        this.componentsQueue = [];
    }

    /*
     * initializes and starts all Intersection/DOM listeners:
     * - Intersection of eligible components with the viewport
     * - DOM mutations on eligible components (by Means of native MutationObserver)
     */
    private initListener() {
        if (!this.mutationObserver) {
            this.mutationObserver = this._newMutationObserver(
                this._mutationObserverCallback.bind(this)
            );

            if (!this.intersectionObserver) {
                // Intersection Observer is used to observe intersection of components with the viewport.
                // each time the 'isIntersecting' property of an entry changes, the Intersection Callback is called.
                // we are using the componentsQueue to hold the components references and their isIntersecting value.
                this.intersectionObserver = this._newIntersectionObserver((entries) => {
                    const eligibleEntries = entries.filter((entry) =>
                        this._isEligibleComponent(entry.target)
                    );

                    eligibleEntries.forEach((entry) => {
                        this._updateQueue(entry);
                    });

                    /*
                     * for each added selector, if at least one of the entries is a match, we call the assocated callback
                     *
                     */
                    this.selectors
                        .filter((element) => !!element.callback)
                        .forEach((element) => {
                            if (
                                eligibleEntries.find((entry) =>
                                    this.yjQuery(entry.target).is(element.selector)
                                )
                            ) {
                                element.callback();
                            }
                        });
                });
            }

            // Observing all eligible components that are already in the page.
            // Note that when an element visible in the viewport is removed, the Intersection Callback is called so we don't need to use the Mutation Observe to oberser removal of Nodes.
            this._getEligibleElements().forEach((component: HTMLElement) => {
                this.intersectionObserver.observe(component);
            });
        }
    }

    /*
     * Method used in mutationObserverCallback that extracts from mutations the list of added and removed nodes
     */
    private _aggregateAddedOrRemovedNodes(mutations: MutationRecord[], addedOnes: boolean) {
        const entries = lodash.flatten(
            mutations
                .filter((mutation) => {
                    // only keep mutations of type childList and addedNodes
                    return (
                        mutation.type === MUTATION_TYPES.CHILD_LIST.NAME &&
                        ((!!addedOnes && mutation.addedNodes && mutation.addedNodes.length) ||
                            (!addedOnes && mutation.removedNodes && mutation.removedNodes.length))
                    );
                })
                .map((mutation: MutationRecord) => {
                    const children = lodash
                        .flatten(
                            Array.prototype.slice
                                .call(addedOnes ? mutation.addedNodes : mutation.removedNodes)
                                .filter((node: HTMLElement) => {
                                    return node.nodeType === Node.ELEMENT_NODE;
                                })
                                .map((child: HTMLElement) => {
                                    const eligibleChildren = this._getAllEligibleChildren(child);
                                    return this._isEligibleComponent(child)
                                        ? [child].concat(eligibleChildren)
                                        : eligibleChildren;
                                })
                        )
                        .sort(nodeUtils.compareHTMLElementsPosition())
                        // so that in case of nested eligible components the deeper element is picked
                        .reverse();

                    return children;
                })
        );

        /*
         * Despite MutationObserver specifications it so happens that sometimes,
         * depending on the very way a parent node is added with its children,
         * parent AND children will appear in a same mutation. We then must only keep the parent
         * Since the parent will appear first, the filtering lodash.uniqWith will always return the parent as opposed to the child which is what we need
         */

        return lodash.uniqWith(entries, (entry1: HTMLElement, entry2: HTMLElement) => {
            return entry1.contains(entry2) || entry2.contains(entry1);
        });
    }

    /*
     * Method used in mutationObserverCallback that extracts from mutations the list of nodes that have a mutation in the class attribute value
     */
    private _aggregateMutationsOnClass(mutations: MutationRecord[], addedOnes: boolean) {
        return lodash.compact(
            mutations
                .filter((mutation: MutationRecord) => {
                    return (
                        mutation.target &&
                        mutation.target.nodeType === Node.ELEMENT_NODE &&
                        mutation.type === MUTATION_TYPES.ATTRIBUTES.NAME &&
                        mutation.attributeName === 'class'
                    );
                })
                .map((mutation: MutationRecord) => {
                    if (addedOnes && this._isEligibleComponent(mutation.target as Element)) {
                        return mutation.target;
                    } else if (
                        !addedOnes &&
                        this._isEligibleComponent(mutation.target as Element)
                    ) {
                        return mutation.target;
                    }
                    return null;
                })
        );
    }
    /*
     * callback executed by the mutation observer every time mutations occur.
     * repositioning and resizing are not part of this except that every time a eligible component is added,
     * it is registered within the positionRegistry and the resizeListener
     */
    private _mutationObserverCallback(mutations: MutationRecord[]) {
        this.logService.debug(mutations);

        this._aggregateAddedOrRemovedNodes(mutations, true).forEach((node: HTMLElement) => {
            this.intersectionObserver.observe(node);
        });

        if (this.hasClassBasedSelectors) {
            this._aggregateMutationsOnClass(mutations, true).forEach((node: HTMLElement) => {
                this.intersectionObserver.observe(node);
            });
        }

        this._aggregateAddedOrRemovedNodes(mutations, false).forEach((node: HTMLElement) => {
            const componentIndex = this._getComponentIndexInQueue(node);
            if (componentIndex !== -1) {
                this.componentsQueue.splice(componentIndex, 1);
            }
        });

        if (this.hasClassBasedSelectors) {
            this._aggregateMutationsOnClass(mutations, false).forEach((node: HTMLElement) => {
                const componentIndex = this._getComponentIndexInQueue(node);
                if (componentIndex !== -1) {
                    this.componentsQueue.splice(componentIndex, 1);
                }
            });
        }
    }

    /*
     * Add the given entry to the componentsQueue
     * The components in the queue are sorted according to their position in the DOM
     * so that the adding of components is done to have parents before children
     */
    private _updateQueue(entry: IntersectionObserverEntry) {
        const componentIndex = this._getComponentIndexInQueue(entry.target);

        if (componentIndex !== -1) {
            if (!entry.intersectionRatio && !this._isInDOM(entry.target)) {
                this.componentsQueue.splice(componentIndex, 1);
            } else {
                this.componentsQueue[componentIndex].isIntersecting = !!entry.intersectionRatio;
            }
        } else if (this._isInDOM(entry.target)) {
            // may have been removed by competing MutationObserver hence showign here but not intersecting
            this.componentsQueue.push({
                component: entry.target,
                isIntersecting: !!entry.intersectionRatio
            } as QueueElement);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////// HELPER METHODS //////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    /*
     * wrapping for test purposes
     */
    private _newMutationObserver(callback: MutationCallback) {
        const mutationObserver = new MutationObserver(callback);
        mutationObserver.observe(
            this.document.body,
            lodash.merge(lodash.cloneDeep(IN_VIEW_ELEMENTS_MUTATION_OBSERVER_OPTIONS), {
                attributes: this.hasClassBasedSelectors
            })
        );
        return mutationObserver;
    }

    /*
     * wrapping for test purposes
     */
    private _newIntersectionObserver(callback: IntersectionObserverCallback) {
        return new IntersectionObserver(callback, IN_VIEW_ELEMENTS_INTERSECTION_OBSERVER_OPTIONS);
    }

    private _getJQuerySelector(): string {
        return this.selectors.map((el) => el.selector).join(',');
    }

    private _isEligibleComponent(component: Element) {
        return this.yjQuery(component).is(this._getJQuerySelector());
    }

    private _getEligibleElements() {
        return Array.prototype.slice.call(this.yjQuery(this._getJQuerySelector()));
    }

    private _getAllEligibleChildren(component: HTMLElement): HTMLElement[] {
        return Array.prototype.slice.call(component.querySelectorAll(this._getJQuerySelector()));
    }

    private _getComponentIndexInQueue(component: Element) {
        return this.componentsQueue.findIndex(function(obj) {
            return component === obj.component;
        });
    }

    private _isInDOM(component: Element) {
        return this.yjQuery.contains(this.document.body, component);
    }
}
