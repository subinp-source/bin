/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import { HttpClient } from '@angular/common/http';
import { Inject } from '@angular/core';

import {
    stringUtils,
    CrossFrameEventService,
    CATALOG_VERSION_UUID_ATTRIBUTE,
    COMPONENT_CLASS,
    CONTAINER_ID_ATTRIBUTE,
    CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS,
    CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS,
    ELEMENT_UUID_ATTRIBUTE,
    EVENT_PERSPECTIVE_CHANGED,
    EVENT_PERSPECTIVE_REFRESHED,
    GatewayProxied,
    IAlertService,
    ID_ATTRIBUTE,
    IExperienceService,
    INotificationService,
    IPageInfoService,
    IPerspectiveService,
    IRenderService,
    JQueryUtilsService,
    LogService,
    ModalService,
    OVERLAY_COMPONENT_CLASS,
    OVERLAY_ID,
    OVERLAY_RERENDERED_EVENT,
    SeDowngradeService,
    SmarteditBootstrapGateway,
    SystemEventService,
    SMARTEDIT_ATTRIBUTE_PREFIX,
    SMARTEDIT_COMPONENT_PROCESS_STATUS,
    TypedMap,
    TYPE_ATTRIBUTE,
    UUID_ATTRIBUTE,
    WindowUtils,
    YJQUERY_TOKEN
} from 'smarteditcommons';
import { ComponentHandlerService } from 'smartedit/services/ComponentHandlerService';
import { SeNamespaceService } from 'smartedit/services/SeNamespaceService';

/** @internal */
@GatewayProxied(
    'blockRendering',
    'isRenderingBlocked',
    'renderSlots',
    'renderComponent',
    'renderRemoval',
    'toggleOverlay',
    'refreshOverlayDimensions',
    'renderPage'
)
@SeDowngradeService(IRenderService)
export class RenderService extends IRenderService {
    resizeSlots = lodash.debounce(this._resizeSlots.bind(this), 50);

    private _slotOriginalHeights: TypedMap<number>;

    constructor(
        private smarteditBootstrapGateway: SmarteditBootstrapGateway,
        private httpClient: HttpClient,
        private logService: LogService,
        @Inject(YJQUERY_TOKEN) protected yjQuery: JQueryStatic,
        private alertService: IAlertService,
        private componentHandlerService: ComponentHandlerService,
        protected crossFrameEventService: CrossFrameEventService,
        private jQueryUtilsService: JQueryUtilsService,
        private experienceService: IExperienceService,
        private seNamespaceService: SeNamespaceService,
        protected systemEventService: SystemEventService,
        notificationService: INotificationService,
        pageInfoService: IPageInfoService,
        perspectiveService: IPerspectiveService,
        windowUtils: WindowUtils,
        modalService: ModalService
    ) {
        super(
            yjQuery,
            systemEventService,
            notificationService,
            pageInfoService,
            perspectiveService,
            crossFrameEventService,
            windowUtils,
            modalService
        );

        this._slotOriginalHeights = {};

        this.crossFrameEventService.subscribe(
            EVENT_PERSPECTIVE_CHANGED,
            (eventId: string, isNonEmptyPerspective: boolean) => {
                this.renderPage(isNonEmptyPerspective);
            }
        );

        this.crossFrameEventService.subscribe(
            EVENT_PERSPECTIVE_REFRESHED,
            (eventId: string, isNonEmptyPerspective: boolean) => {
                this.renderPage(isNonEmptyPerspective);
            }
        );

        this.systemEventService.subscribe(
            CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS.PROCESS_COMPONENTS,
            (eventId: string, componentsList: any[]) => {
                const components = lodash.map(componentsList, (component: HTMLElement) => {
                    if (
                        component.dataset[SMARTEDIT_COMPONENT_PROCESS_STATUS] !==
                        CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS.KEEP_VISIBLE
                    ) {
                        component.dataset[
                            SMARTEDIT_COMPONENT_PROCESS_STATUS
                        ] = this.componentHandlerService.isOverlayOn()
                            ? CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS.PROCESS
                            : CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS.REMOVE;
                    }
                    return component;
                });
                return Promise.resolve(components);
            }
        );
    }

    toggleOverlay(isVisible: boolean): void {
        const overlay = this.componentHandlerService.getOverlay();
        overlay.css('visibility', isVisible ? 'visible' : 'hidden');
    }

    refreshOverlayDimensions(element: JQuery = null): void {
        element = element || this.yjQuery('body');
        const children: any = this.componentHandlerService.getFirstSmartEditComponentChildren(
            element
        );
        children.each((index: number, childElement: any) => {
            this.updateComponentSizeAndPosition(childElement);
            this.refreshOverlayDimensions(childElement);
        });
    }

    /**
     * Updates the dimensions of the overlay component element given the original component element and the overlay component itself.
     * If no overlay component is provided, it will be fetched through {@link componentHandlerService.getOverlayComponent}
     *
     * The overlay component is resized to be the same dimensions of the component for which it overlays, and positioned absolutely
     * on the page. Additionally, it is provided with a minimum height and width. The resizing takes into account both
     * the size of the component element, and the position based on iframe scrolling.
     *
     * @param {HTMLElement} element The original CMS component element from the storefront.
     * @param {HTMLElement =} componentOverlayElem The overlay component. If none is provided
     */
    updateComponentSizeAndPosition(element: HTMLElement, componentOverlayElem?: HTMLElement): void {
        const componentElem = this.yjQuery(element);
        componentOverlayElem =
            componentOverlayElem ||
            this.componentHandlerService.getComponentCloneInOverlay(componentElem).get(0);
        if (!componentOverlayElem) {
            return;
        }

        const parentPos = this._getParentInOverlay(componentElem)
            .get(0)
            .getBoundingClientRect();

        const innerWidth: number = componentElem.get(0).offsetWidth;
        const innerHeight: number = componentElem.get(0).offsetHeight;

        // Update the position based on the IFrame Scrolling
        const pos = componentElem.get(0).getBoundingClientRect();
        const elementTopPos: number = pos.top - parentPos.top;
        const elementLeftPos: number = pos.left - parentPos.left;

        // In SakExecutorService.ts, the 'position' and 'top' will decide the slot-contextual-menu show at top of slot or bottom of slot
        componentOverlayElem.style.position = 'absolute';
        componentOverlayElem.style.top = elementTopPos + 'px';
        componentOverlayElem.style.left = elementLeftPos + 'px';
        componentOverlayElem.style.width = innerWidth + 'px';
        componentOverlayElem.style.height = innerHeight + 'px';
        componentOverlayElem.style.minWidth = '51px';
        componentOverlayElem.style.minHeight = '48px';

        const cloneId: string = this._buildShallowCloneId(
            componentElem.attr(ID_ATTRIBUTE) as string,
            componentElem.attr(TYPE_ATTRIBUTE) as string,
            componentElem.attr(CONTAINER_ID_ATTRIBUTE) as string
        );
        const shallowCopy: JQuery = this.yjQuery(componentOverlayElem).find(
            '[id="' + cloneId + '"]'
        );
        shallowCopy.width(innerWidth);
        shallowCopy.height(innerHeight);
        shallowCopy.css('min-height', 49);
        shallowCopy.css('min-width', 51);
    }

    renderPage(isRerender: boolean): void {
        this.resizeSlots();
        this.componentHandlerService.getOverlay().hide();
        this.isRenderingBlocked().then((isRenderingBlocked: boolean) => {
            this._markSmartEditAsReady();
            const overlay = this._createOverlay();
            if (isRerender && !isRenderingBlocked) {
                overlay.show();
            }
            this.systemEventService.publish(
                CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS.RESTART_PROCESS
            );
            this.crossFrameEventService.publish(OVERLAY_RERENDERED_EVENT);
        });
    }

    async renderSlots(_slotIds: string[] | string = null): Promise<any> {
        if (stringUtils.isBlank(_slotIds) || (_slotIds instanceof Array && _slotIds.length === 0)) {
            return Promise.reject('renderService.renderSlots.slotIds.required');
        }
        if (typeof _slotIds === 'string') {
            _slotIds = [_slotIds];
        }

        // need to retrieve unique set of slotIds, happens when moving a component within a slot
        const slotIds = lodash.uniqBy(_slotIds, (slotId: string) => slotId);

        // see if storefront can handle the rerendering
        const slotsRemaining = slotIds.filter(
            (id: string) => !this.seNamespaceService.renderComponent(id, 'ContentSlot')
        );

        if (slotsRemaining.length <= 0) {
            // all were handled by storefront
            return true;
        } else {
            let storefrontUrl: string;
            let response: string;

            try {
                storefrontUrl = await this.experienceService.buildRefreshedPreviewUrl();
            } catch (e) {
                this.logService.error(
                    'renderService.renderSlots() - error with buildRefreshedPreviewUrl'
                );

                return Promise.reject(e);
            }

            try {
                response = await this.httpClient
                    .get(storefrontUrl, {
                        headers: {
                            Accept: 'text/html',
                            Pragma: 'no-cache'
                        },
                        responseType: 'text'
                    })
                    .toPromise();
            } catch (e) {
                this.alertService.showDanger({
                    message: e
                });

                return Promise.reject(e);
            }

            const root = this.jQueryUtilsService.unsafeParseHTML(response);
            slotsRemaining.forEach((slotId: string) => {
                const slotSelector =
                    '.' +
                    COMPONENT_CLASS +
                    '[' +
                    TYPE_ATTRIBUTE +
                    "='ContentSlot'][" +
                    ID_ATTRIBUTE +
                    "='" +
                    slotId +
                    "']";
                const slotToBeRerendered = this.jQueryUtilsService.extractFromElement(
                    root,
                    slotSelector
                );
                const originalSlot = this.yjQuery(slotSelector);
                originalSlot.html(slotToBeRerendered.html());
                if (originalSlot.data('smartedit-resized-slot')) {
                    // reset the slot height to auto because the originalSlot height could have been changed previously with a specific height.
                    originalSlot.css('height', 'auto');
                }
            });
            this._reprocessPage();
            return Promise.resolve();
        }
    }

    renderComponent(componentId: string, componentType: string): Promise<string | boolean> {
        const component = this.componentHandlerService.getComponent(componentId, componentType);
        const slotId = this.componentHandlerService
            .getParent(component)
            .attr(ID_ATTRIBUTE) as string;
        if (this.seNamespaceService.renderComponent(componentId, componentType, slotId)) {
            return Promise.resolve(true);
        } else {
            return this.renderSlots(slotId);
        }
    }

    renderRemoval(componentId: string, componentType: string, slotId: string): JQuery {
        const removedComponents = this.componentHandlerService
            .getComponentUnderSlot(componentId, componentType, slotId)
            .remove();
        this.refreshOverlayDimensions();
        return removedComponents;
    }

    /**
     * Given a smartEdit component in the storefront layer, its clone in the smartEdit overlay is removed and the pertaining decorators destroyed.
     *
     * @param {Element} element The original CMS component element from the storefront.
     * @param {Element=} parent the closest smartEditComponent parent, expected to be null for the highest elements
     * @param {Object=} oldAttributes The map of former attributes of the element. necessary when the element has mutated since the last creation
     */
    destroyComponent(_component: HTMLElement, _parent?: HTMLElement, oldAttributes?: any): void {
        const component = this.yjQuery(_component);
        const parent = this.yjQuery(_parent);

        const componentInOverlayId =
            oldAttributes && oldAttributes[ID_ATTRIBUTE]
                ? oldAttributes[ID_ATTRIBUTE]
                : component.attr(ID_ATTRIBUTE);
        const componentInOverlayType =
            oldAttributes && oldAttributes[TYPE_ATTRIBUTE]
                ? oldAttributes[TYPE_ATTRIBUTE]
                : component.attr(TYPE_ATTRIBUTE);

        // the node is no longer attached so can't find parent
        if (parent.attr(ID_ATTRIBUTE)) {
            this.componentHandlerService
                .getOverlayComponentWithinSlot(
                    componentInOverlayId,
                    componentInOverlayType,
                    parent.attr(ID_ATTRIBUTE)
                )
                .remove();
        } else {
            this.componentHandlerService
                .getComponentInOverlay(componentInOverlayId, componentInOverlayType)
                .remove();
        }
    }

    /**
     * Given a smartEdit component in the storefront layer. An empty clone of it will be created, sized and positioned in the smartEdit overlay
     * then compiled with all eligible decorators for the given perspective (see {@link smarteditServicesModule.interface:IPerspectiveService perspectiveService})
     * @param {Element} element The original CMS component element from the storefront.
     */
    createComponent(element: HTMLElement): void {
        if (this.componentHandlerService.isOverlayOn() && this._isComponentVisible(element)) {
            this._cloneComponent(element);
        }
    }

    /**
     * Resizes the height of all slots on the page based on the sizes of the components. The new height of the
     * slot is set to the minimum height encompassing its sub-components, calculated by comparing each of the
     * sub-components' top and bottom bounding rectangle values.
     *
     * Slots that do not have components inside still appear in the DOM. If the CMS manager is in a perspective in which
     * slot contextual menus are displayed, slots must have a height. Otherwise, overlays will overlap. Thus, empty slots
     * are given a minimum size so that overlays match.
     */
    private _resizeSlots(): void {
        Array.prototype.slice
            .call(this.componentHandlerService.getFirstSmartEditComponentChildren(document.body))
            .forEach((slotComponent: HTMLElement) => {
                const slotComponentID = this.yjQuery(slotComponent).attr(ID_ATTRIBUTE);
                const slotComponentType = this.yjQuery(slotComponent).attr(TYPE_ATTRIBUTE);

                let newSlotTop = -1;
                let newSlotBottom = -1;

                this.yjQuery(slotComponent)
                    .find('.' + COMPONENT_CLASS)
                    .filter((idx: number, componentInSlotElement: HTMLElement) => {
                        const componentInSlot: JQuery = this.yjQuery(componentInSlotElement);
                        return (
                            componentInSlot.attr(ID_ATTRIBUTE) !== slotComponentID &&
                            componentInSlot.attr(TYPE_ATTRIBUTE) !== slotComponentType &&
                            componentInSlot.is(':visible')
                        );
                    })
                    .each((compIndex: number, component: HTMLElement) => {
                        const componentDimensions = component.getBoundingClientRect();
                        newSlotTop =
                            newSlotTop === -1
                                ? componentDimensions.top
                                : Math.min(newSlotTop, componentDimensions.top);
                        newSlotBottom =
                            newSlotBottom === -1
                                ? componentDimensions.bottom
                                : Math.max(newSlotBottom, componentDimensions.bottom);
                    });

                const newSlotHeight = newSlotBottom - newSlotTop;
                const currentSlotHeight =
                    parseFloat(window.getComputedStyle(slotComponent).height) || 0;
                if (Math.abs(currentSlotHeight - newSlotHeight) > 0.001) {
                    const currentSlotVerticalPadding =
                        parseFloat(window.getComputedStyle(slotComponent).paddingTop) +
                        parseFloat(window.getComputedStyle(slotComponent).paddingBottom);
                    const slotUniqueKey = slotComponentID + '_' + slotComponentType;
                    let oldSlotHeight = this._slotOriginalHeights[slotUniqueKey];
                    if (!oldSlotHeight) {
                        oldSlotHeight = currentSlotHeight;
                        this._slotOriginalHeights[slotUniqueKey] = oldSlotHeight;
                    }
                    if (newSlotHeight + currentSlotVerticalPadding > oldSlotHeight) {
                        slotComponent.style.height =
                            newSlotHeight + currentSlotVerticalPadding + 'px';
                    } else {
                        slotComponent.style.height = oldSlotHeight + 'px';
                    }
                    this.yjQuery(slotComponent).data('smartedit-resized-slot', true);
                }
            });
    }

    private _getParentInOverlay(element: JQuery): JQuery {
        const parent: JQuery = this.componentHandlerService.getParent(element);
        if (parent.length) {
            return this.componentHandlerService.getOverlayComponent(parent);
        } else {
            return this.componentHandlerService.getOverlay();
        }
    }

    private _buildShallowCloneId(
        smarteditComponentId: string,
        smarteditComponentType: string,
        smarteditContainerId: string
    ): string {
        const containerSection = !stringUtils.isBlank(smarteditContainerId)
            ? '_' + smarteditContainerId
            : '';
        return smarteditComponentId + '_' + smarteditComponentType + containerSection + '_overlay';
    }

    private _cloneComponent(el: HTMLElement): void {
        if (!this.yjQuery(el).is(':visible')) {
            return;
        }

        const element: JQuery = this.yjQuery(el);
        const parentOverlay = this._getParentInOverlay(element);

        if (!parentOverlay.length) {
            this.logService.error(
                'renderService: parentOverlay empty for component:',
                element.attr(ID_ATTRIBUTE)
            );
            return;
        }
        if (!this._validateComponentAttributesContract(element)) {
            return;
        }

        // FIXME: CMSX-6139: use dataset instead of attr(): ELEMENT_UUID_ATTRIBUTE value should not be exposed.
        const elementUUID: string = stringUtils.generateIdentifier();
        if (!element.attr(ELEMENT_UUID_ATTRIBUTE)) {
            element.attr(ELEMENT_UUID_ATTRIBUTE, elementUUID);
        }
        const smarteditComponentId: string = element.attr(ID_ATTRIBUTE) as string;
        const smarteditComponentType: string = element.attr(TYPE_ATTRIBUTE) as string;
        const smarteditContainerId: string = element.attr(CONTAINER_ID_ATTRIBUTE) as string;

        const shallowCopy: HTMLElement = this._getDocument().createElement('div');
        shallowCopy.id = this._buildShallowCloneId(
            smarteditComponentId,
            smarteditComponentType,
            smarteditContainerId
        );

        const smartEditWrapper = this._getDocument().createElement('smartedit-element');
        const componentDecorator = this.yjQuery(smartEditWrapper);
        componentDecorator.append(shallowCopy);
        this.updateComponentSizeAndPosition(element.get(0), smartEditWrapper);

        if (smarteditComponentType === 'NavigationBarCollectionComponent') {
            // Make sure the Navigation Bar is on top of the navigation items
            smartEditWrapper.style.zIndex = '7';
        }

        componentDecorator.addClass(OVERLAY_COMPONENT_CLASS);
        Array.prototype.slice.apply(element.get(0).attributes).forEach((node: HTMLElement) => {
            if (node.nodeName.indexOf(SMARTEDIT_ATTRIBUTE_PREFIX) === 0) {
                componentDecorator.attr(node.nodeName, node.nodeValue);
            }
        });

        parentOverlay.append(smartEditWrapper);
    }

    private _createOverlay(): JQuery {
        const overlayWrapper = this.componentHandlerService.getOverlay();
        if (overlayWrapper.length) {
            return overlayWrapper;
        }
        const overlay = this._getDocument().createElement('div');
        overlay.id = OVERLAY_ID;
        overlay.style.position = 'absolute';
        overlay.style.top = '0px';
        overlay.style.left = '0px';
        overlay.style.bottom = '0px';
        overlay.style.right = '0px';
        overlay.style.display = 'none';
        document.body.appendChild(overlay);
        return this.yjQuery(overlay);
    }

    private _validateComponentAttributesContract(element: JQuery): boolean {
        const requiredAttributes = [
            ID_ATTRIBUTE,
            UUID_ATTRIBUTE,
            TYPE_ATTRIBUTE,
            CATALOG_VERSION_UUID_ATTRIBUTE
        ];
        let valid = true;
        requiredAttributes.forEach((reqAttribute: string) => {
            if (!element || !element.attr(reqAttribute)) {
                valid = false;
                this.logService.warn(
                    'RenderService - smarteditComponent element discovered with missing contract attribute: ' +
                        reqAttribute
                );
            }
        });
        return valid;
    }

    private _markSmartEditAsReady(): void {
        this.smarteditBootstrapGateway.getInstance().publish('smartEditReady', {});
    }

    private _isComponentVisible(component: HTMLElement): boolean {
        // NOTE: This might not work as expected for fixed positioned items. For those cases a more expensive
        // check must be performed (get the component style and check if it's visible or not).
        return component.offsetParent !== null;
    }

    private _reprocessPage(): void {
        this.seNamespaceService.reprocessPage();
    }
}
