/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import {
    DragAndDropService,
    GatewayFactory,
    IAlertService,
    IBrowserService,
    IWaitDialogService,
    MessageGateway,
    Payload,
    SeInjectable,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';
import { AssetsService, ICMSComponent, IPageContentSlotsComponentsRestService } from 'cmscommons';

export interface CmsDragAndDropCachedComponentHint {
    original: JQuery<HTMLElement>;
    position: number;
    rect: TypedMap<number>;
}

export interface CmsDragAndDropCachedComponent {
    id: string;
    type: string;
    original: JQuery<HTMLElement>;
    position: number;
    rect: TypedMap<number>;
    hints: CmsDragAndDropCachedComponentHint[];
}

export interface CmsDragAndDropCachedSlot {
    id: string;
    uuid: string;
    original: JQuery<HTMLElement>;
    components: CmsDragAndDropCachedComponent[];
    hint: {
        original: JQuery<HTMLElement>;
        rect: TypedMap<number>;
    };
    rect: TypedMap<number>;
    isAllowed?: boolean;
    mayBeAllowed?: boolean;
}

export interface CmsDragAndDropDragInfo extends Payload {
    componentId: string;
    componentUuid: string;
    componentType: string;
    slotUuid: string;
    slotId: string;
    slotOperationRelatedId: string;
    slotOperationRelatedType: string;
    cloneOnDrop?: boolean;
}

/**
 * @ngdoc service
 * @name cmsDragAndDropServiceModule.service:cmsDragAndDropService
 *
 * @description
 * This service provides a rich drag and drop experience tailored for CMS operations.
 */
@SeInjectable()
export class CmsDragAndDropService {
    private static readonly CMS_DRAG_AND_DROP_ID = 'se.cms.dragAndDrop';

    private static readonly TARGET_SELECTOR =
        "#smarteditoverlay .smartEditComponentX[data-smartedit-component-type='ContentSlot']";
    private static readonly SOURCE_SELECTOR =
        "#smarteditoverlay .smartEditComponentX[data-smartedit-component-type!='ContentSlot'] .movebutton";
    private static readonly MORE_MENU_SOURCE_SELECTOR = '.movebutton';

    private static readonly SLOT_SELECTOR =
        ".smartEditComponentX[data-smartedit-component-type='ContentSlot']";
    private static readonly COMPONENT_SELECTOR =
        ".smartEditComponentX[data-smartedit-component-type!='ContentSlot']";
    private static readonly HINT_SELECTOR = '.overlayDropzone';

    private static readonly CSS_CLASSES = {
        UI_HELPER_OVERLAY: 'overlayDnd',
        DROPZONE: 'overlayDropzone',
        DROPZONE_FULL: 'overlayDropzone--full',
        DROPZONE_TOP: 'overlayDropzone--top',
        DROPZONE_BOTTOM: 'overlayDropzone--bottom',
        DROPZONE_LEFT: 'overlayDropzone--left',
        DROPZONE_RIGHT: 'overlayDropzone--right',
        DROPZONE_HOVERED: 'overlayDropzone--hovered',
        DROPZONE_MAY_BE_ALLOWED: 'overlayDropzone--mayBeAllowed',
        OVERLAY_IN_DRAG_DROP: 'smarteditoverlay_dndRendering',
        COMPONENT_DRAGGED: 'component_dragged',
        COMPONENT_DRAGGED_HOVERED: 'component_dragged_hovered',
        SLOTS_MARKED: 'slot-marked',
        SLOT_ALLOWED: 'over-slot-enabled',
        SLOT_NOT_ALLOWED: 'over-slot-disabled',
        SLOT_MAY_BE_ALLOWED: 'over-slot-maybeenabled'
    };

    private static readonly DEFAULT_DRAG_IMG = '/images/contextualmenu_move_on.png';

    private cachedSlots: TypedMap<CmsDragAndDropCachedSlot> = {};
    private highlightedSlot: CmsDragAndDropCachedSlot = null;
    private highlightedComponent: CmsDragAndDropCachedComponent = null;
    private highlightedHint: CmsDragAndDropCachedComponentHint = null;
    private dragInfo: CmsDragAndDropDragInfo = null;
    private overlayRenderedUnSubscribeFn: () => void = null;
    private componentRemovedUnSubscribeFn: () => void = null;
    private gateway: MessageGateway;

    constructor(
        private $q: angular.IQService,
        private $window: angular.IWindowService,
        private $translate: angular.translate.ITranslateService,
        private yjQuery: JQueryStatic,
        private dragAndDropService: DragAndDropService,
        private componentHandlerService: any,
        private systemEventService: SystemEventService,
        private gatewayFactory: GatewayFactory,
        private slotRestrictionsService: any,
        private alertService: IAlertService,
        private assetsService: AssetsService,
        private browserService: IBrowserService,
        private componentEditingFacade: any,
        private waitDialogService: IWaitDialogService,
        private pageContentSlotsComponentsRestService: IPageContentSlotsComponentsRestService,
        private DRAG_AND_DROP_EVENTS: TypedMap<string>,
        private COMPONENT_REMOVED_EVENT: string,
        private CONTENT_SLOT_TYPE: string,
        private OVERLAY_RERENDERED_EVENT: string,
        private CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS: TypedMap<string>,
        private SMARTEDIT_COMPONENT_PROCESS_STATUS: string,
        private CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS: TypedMap<string>,
        private COMPONENT_IN_SLOT_STATUS: TypedMap<string>,
        private domain: string
    ) {
        this.gateway = this.gatewayFactory.createGateway('cmsDragAndDrop');
    }

    /**
     * @ngdoc method
     * @name cmsDragAndDropServiceModule.service:cmsDragAndDropService#register
     * @methodOf cmsDragAndDropServiceModule.service:cmsDragAndDropService
     *
     * @description
     * This method registers this drag and drop instance in SmartEdit.
     */
    register(): void {
        this.dragAndDropService.register({
            id: CmsDragAndDropService.CMS_DRAG_AND_DROP_ID,
            sourceSelector: [
                CmsDragAndDropService.SOURCE_SELECTOR,
                CmsDragAndDropService.MORE_MENU_SOURCE_SELECTOR
            ], // the source selectors are DnD menus located both inside and outside the more options of the overlay
            targetSelector: CmsDragAndDropService.TARGET_SELECTOR,
            startCallback: this.onStart.bind(this),
            dragEnterCallback: this.onDragEnter.bind(this),
            dragOverCallback: this.onDragOver.bind(this),
            dropCallback: this.onDrop.bind(this),
            outCallback: this.onDragLeave.bind(this),
            stopCallback: this.onStop.bind(this),
            enableScrolling: true,
            helper: this.getDragImageSrc.bind(this)
        });
    }

    /**
     * @ngdoc method
     * @name cmsDragAndDropServiceModule.service:cmsDragAndDropService#unregister
     * @methodOf cmsDragAndDropServiceModule.service:cmsDragAndDropService
     *
     * @description
     * This method unregisters this drag and drop instance from SmartEdit.
     */
    unregister(): void {
        this.dragAndDropService.unregister([CmsDragAndDropService.CMS_DRAG_AND_DROP_ID]);

        if (this.overlayRenderedUnSubscribeFn) {
            this.overlayRenderedUnSubscribeFn();
        }
        if (this.componentRemovedUnSubscribeFn) {
            this.componentRemovedUnSubscribeFn();
        }
    }

    /**
     * @ngdoc method
     * @name cmsDragAndDropServiceModule.service:cmsDragAndDropService#apply
     * @methodOf cmsDragAndDropServiceModule.service:cmsDragAndDropService
     *
     * @description
     * This method applies this drag and drop instance in the current page. After this method is executed,
     * the user can start a drag and drop operation.
     */
    apply(): void {
        this.dragAndDropService.apply(CmsDragAndDropService.CMS_DRAG_AND_DROP_ID);
        this.addUIHelpers();

        // Register a listener for every time the overlay is updated.
        this.overlayRenderedUnSubscribeFn = this.systemEventService.subscribe(
            this.OVERLAY_RERENDERED_EVENT,
            this.onOverlayUpdate.bind(this)
        );
        this.componentRemovedUnSubscribeFn = this.systemEventService.subscribe(
            this.COMPONENT_REMOVED_EVENT,
            this.onOverlayUpdate.bind(this)
        );

        this.gateway.subscribe<CmsDragAndDropDragInfo>(
            this.DRAG_AND_DROP_EVENTS.DRAG_STARTED,
            (eventId: string, data: CmsDragAndDropDragInfo) => {
                this.dragAndDropService.markDragStarted();
                this.initializeDragOperation(data);
            }
        );

        this.gateway.subscribe(this.DRAG_AND_DROP_EVENTS.DRAG_STOPPED, () => {
            this.dragAndDropService.markDragStopped();
            this.cleanDragOperation();
        });
    }

    /**
     * @ngdoc method
     * @name cmsDragAndDropServiceModule.service:cmsDragAndDropService#update
     * @methodOf cmsDragAndDropServiceModule.service:cmsDragAndDropService
     *
     * @description
     * This method updates this drag and drop instance in the current page. It is important to execute
     * this method every time a draggable or droppable element is added or removed from the page DOM.
     */
    update() {
        this.dragAndDropService.update(CmsDragAndDropService.CMS_DRAG_AND_DROP_ID);

        // Add UI helpers -> They identify the places where you can drop components.
        this.addUIHelpers();

        // Update cache elements AFTER adding UI Helpers
        this.cacheElements();
    }

    // Other Event Handlers
    private onOverlayUpdate(): angular.IPromise<void> {
        this.update();
        return this.$q.resolve();
    }

    // Drag and Drop Event Handlers
    private onStart(event: DragEvent): void {
        // Find element
        let targetElm = this.getSelector(event.target);
        // when the DnD icon is in the more option dropdown, the targetElm is a span and has no data-component-id. Here we get the closest element (i.e. <contextual-menu-item>)
        if (!targetElm.attr('data-component-id')) {
            targetElm = this.yjQuery(targetElm).closest('[data-component-id]');
        }
        const component = targetElm.closest(CmsDragAndDropService.COMPONENT_SELECTOR);
        const slot = component.closest(CmsDragAndDropService.SLOT_SELECTOR);

        // Here if the component evaluated above exits that means the component has been located and we can fetch its attributes
        // else it is not located as the DnD option is hidden inside the more option of the contextual menu in which case
        // we find the component/slot info by accessing attributes of the DnD icon.
        const componentId =
            component.length > 0
                ? this.componentHandlerService.getId(component)
                : targetElm.attr('data-component-id');
        const componentUuid =
            component.length > 0
                ? this.componentHandlerService.getSlotOperationRelatedUuid(component)
                : targetElm.attr('data-component-uuid');
        const componentType =
            component.length > 0
                ? this.componentHandlerService.getType(component)
                : targetElm.attr('data-component-type');
        const slotOperationRelatedId =
            component.length > 0
                ? this.componentHandlerService.getSlotOperationRelatedId(component)
                : targetElm.attr('data-component-id');
        const slotOperationRelatedType =
            component.length > 0
                ? this.componentHandlerService.getSlotOperationRelatedType(component)
                : targetElm.attr('data-component-type');

        const slotId =
            component.length > 0
                ? this.componentHandlerService.getId(slot)
                : targetElm.attr('data-slot-id');
        const slotUuid =
            component.length > 0
                ? this.componentHandlerService.getId(slot)
                : targetElm.attr('data-slot-uuid');

        const dragInfo = {
            componentId,
            componentUuid,
            componentType,
            slotUuid,
            slotId,
            slotOperationRelatedId,
            slotOperationRelatedType
        };
        component.addClass(CmsDragAndDropService.CSS_CLASSES.COMPONENT_DRAGGED);
        this.initializeDragOperation(dragInfo);
        this.toggleKeepVisibleComponentAndSlot(true);
    }

    private onDragEnter(event: DragEvent): void {
        this.highlightSlot(event);
    }

    private onDragOver(event: DragEvent): void {
        this.highlightSlot(event).then(() => {
            if (!this.highlightedSlot || !this.highlightedSlot.isAllowed) {
                return;
            }

            const slotId = this.componentHandlerService.getId(this.highlightedSlot.original);

            // Check which component is highlighted
            if (this.highlightedHint && this.isMouseInRegion(event, this.highlightedHint)) {
                // If right hint is already highlighted don't do anything.
                return;
            } else if (this.highlightedHint) {
                // Hint is not longer hovered.
                this.clearHighlightedHint();
            }

            const cachedSlot = this.cachedSlots[slotId];
            if (cachedSlot.components.length > 0) {
                // Find the hovered component.
                if (
                    !this.highlightedComponent ||
                    !this.isMouseInRegion(event, this.highlightedComponent)
                ) {
                    this.clearHighlightedComponent();

                    cachedSlot.components.forEach((component) => {
                        if (this.isMouseInRegion(event, component)) {
                            this.highlightedComponent = component;
                            return false;
                        }
                        return true;
                    });
                }

                // Find the hint, if any, to highlight.
                if (this.highlightedComponent && this.highlightedComponent.hints) {
                    this.highlightedComponent.hints.forEach((hint) => {
                        if (this.isMouseInRegion(event, hint)) {
                            this.highlightedHint = hint;
                            return false;
                        }
                        return true;
                    });
                }
            }

            if (
                this.highlightedComponent &&
                this.highlightedComponent.id === this.dragInfo.slotOperationRelatedId
            ) {
                this.highlightedComponent.original.addClass(
                    CmsDragAndDropService.CSS_CLASSES.COMPONENT_DRAGGED_HOVERED
                );
            } else if (this.highlightedHint) {
                if (this.highlightedSlot.isAllowed) {
                    this.highlightedHint.original.addClass(
                        CmsDragAndDropService.CSS_CLASSES.DROPZONE_HOVERED
                    );
                    if (this.highlightedSlot.mayBeAllowed) {
                        this.highlightedHint.original.addClass(
                            CmsDragAndDropService.CSS_CLASSES.DROPZONE_MAY_BE_ALLOWED
                        );
                    }
                }
            }
        });
    }

    private onDrop(event: DragEvent): void {
        if (this.highlightedSlot) {
            const sourceSlotId = this.dragInfo.slotId;
            const targetSlotId = this.componentHandlerService.getId(this.highlightedSlot.original);
            const targetSlotUUId = this.componentHandlerService.getUuid(
                this.highlightedSlot.original
            );
            const sourceComponentId = this.dragInfo.componentId;
            // if component is dragged from component-menu, there is no slotOperationRelated(Id/Type) available.
            const sourceSlotOperationRelatedId =
                this.dragInfo.slotOperationRelatedId || this.dragInfo.componentId;
            const componentType =
                this.dragInfo.slotOperationRelatedType || this.dragInfo.componentType;

            if (!this.highlightedSlot.isAllowed) {
                const translation = this.$translate.instant(
                    'se.drag.and.drop.not.valid.component.type',
                    {
                        slotUID: targetSlotId,
                        componentUID: sourceSlotOperationRelatedId
                    }
                );
                this.alertService.showDanger({
                    message: translation
                });
                return;
            }
            if (this.highlightedHint || this.highlightedSlot.components.length === 0) {
                let position = this.highlightedHint ? this.highlightedHint.position : 0;
                let result;

                this.waitDialogService.showWaitModal();

                if (!sourceSlotId) {
                    if (!sourceComponentId) {
                        const slotInfo = {
                            targetSlotId,
                            targetSlotUUId
                        };
                        const catalogVersionUuid = this.componentHandlerService.getCatalogVersionUuid(
                            this.highlightedSlot.original
                        );
                        result = this.componentEditingFacade.addNewComponentToSlot(
                            slotInfo,
                            catalogVersionUuid,
                            componentType,
                            position
                        );
                    } else {
                        const dragInfo = {
                            componentId: sourceComponentId,
                            componentUuid: this.dragInfo.componentUuid,
                            componentType
                        };
                        const componentProperties = {
                            targetSlotId,
                            dragInfo,
                            position
                        };

                        result = this.dragInfo.cloneOnDrop
                            ? this.componentEditingFacade.cloneExistingComponentToSlot(
                                  componentProperties
                              )
                            : this.componentEditingFacade.addExistingComponentToSlot(
                                  targetSlotId,
                                  dragInfo,
                                  position
                              );
                    }
                } else {
                    if (sourceSlotId === targetSlotId) {
                        const currentComponentPos = this.getComponentPositionFromCachedSlot(
                            sourceSlotId,
                            sourceComponentId
                        );
                        if (currentComponentPos < position) {
                            // The current component will be removed from its current position, thus the target
                            // position needs to take this into account.
                            position--;
                        } else if (currentComponentPos === position) {
                            // Do not perform update if position and slot has not changed.

                            this.waitDialogService.hideWaitModal();
                            return;
                        }
                    }
                    result = this.componentEditingFacade.moveComponent(
                        sourceSlotId,
                        targetSlotId,
                        sourceSlotOperationRelatedId,
                        position
                    );
                }

                result
                    .then(
                        () => {
                            this.scrollToModifiedSlot(targetSlotId);
                        },
                        () => {
                            this.onStop(event);
                        }
                    )
                    .finally(() => {
                        this.waitDialogService.hideWaitModal();
                    });
            }
        }
    }

    private getComponentPositionFromCachedSlot(slotId: string, componentId: string): number {
        const cachedSlot = this.cachedSlots[slotId];
        const componentsInCachedSlot =
            cachedSlot && cachedSlot.components ? cachedSlot.components : [];
        const cachedComponent = componentsInCachedSlot.find(
            (component) => this.componentHandlerService.getId(component.original) === componentId
        );
        const currentComponentPos = cachedComponent
            ? cachedComponent.position
            : this.componentHandlerService.getComponentPositionInSlot(slotId, componentId);
        return currentComponentPos;
    }

    private onDragLeave(event: DragEvent): void {
        if (this.highlightedSlot) {
            const slotId = this.componentHandlerService.getId(this.highlightedSlot.original);
            const cachedSlot = this.cachedSlots[slotId];

            if (!this.isMouseInRegion(event, cachedSlot)) {
                this.clearHighlightedSlot();
            }
        }
    }

    private onStop(event: DragEvent): void {
        const component = this.getSelector(event.target).closest(
            CmsDragAndDropService.COMPONENT_SELECTOR
        ) as JQuery<HTMLElement>;
        this.toggleKeepVisibleComponentAndSlot(false);
        this.cleanDragOperation(component);
        this.systemEventService.publish(
            this.CONTRACT_CHANGE_LISTENER_PROCESS_EVENTS.RESTART_PROCESS
        );
    }

    // Helpers
    /**
     * This function returns the source of the image used as drag image. Currently, the
     * image is only returned for Safari; all the other browsers display default images
     * properly.
     */
    private getDragImageSrc(): string {
        let imagePath = '';
        if (this.browserService.isSafari()) {
            imagePath = this.assetsService.getAssetsRoot() + CmsDragAndDropService.DEFAULT_DRAG_IMG;
        }
        return this.domain + imagePath;
    }

    private initializeDragOperation(dragInfo: CmsDragAndDropDragInfo): void {
        this.dragInfo = dragInfo;
        this.cacheElements();

        // Prepare UI
        const overlay = this.componentHandlerService.getOverlay();
        overlay.addClass(CmsDragAndDropService.CSS_CLASSES.OVERLAY_IN_DRAG_DROP);

        // Send an event to signal that the drag operation is started. Other pieces of SE, like contextual menus
        // need to be aware.
        this.systemEventService.publishAsync(this.DRAG_AND_DROP_EVENTS.DRAG_STARTED);
    }

    private cleanDragOperation(draggedComponent?: JQuery<HTMLElement>): void {
        this.clearHighlightedSlot();
        if (draggedComponent) {
            draggedComponent.removeClass(CmsDragAndDropService.CSS_CLASSES.COMPONENT_DRAGGED);
        }

        const overlay = this.componentHandlerService.getOverlay();
        overlay.removeClass(CmsDragAndDropService.CSS_CLASSES.OVERLAY_IN_DRAG_DROP);
        this.systemEventService.publishAsync(this.DRAG_AND_DROP_EVENTS.DRAG_STOPPED);

        this.dragInfo = null;
        this.cachedSlots = {};
        this.highlightedSlot = null;
    }

    private highlightSlot(event: DragEvent): angular.IPromise<void> {
        const slot = this.yjQuery(event.target).closest(CmsDragAndDropService.SLOT_SELECTOR);
        const slotId = this.componentHandlerService.getId(slot);

        let oldSlotId: string;
        if (this.highlightedSlot) {
            oldSlotId = this.componentHandlerService.getId(this.highlightedSlot.original);

            if (oldSlotId !== slotId) {
                this.clearHighlightedSlot();
            }
        }

        if (!this.highlightedSlot || this.highlightedSlot.isAllowed === undefined) {
            this.highlightedSlot = this.cachedSlots[slotId];

            const dragInfo = { ...this.dragInfo };
            // if component is dragged from component-menu, there is no slotOperationRelated(Id/Type) available.
            dragInfo.componentId =
                this.dragInfo.slotOperationRelatedId || this.dragInfo.componentId;
            dragInfo.componentType =
                this.dragInfo.slotOperationRelatedType || this.dragInfo.componentType;
            if (dragInfo.cloneOnDrop) {
                delete dragInfo.componentId;
            }

            return this.slotRestrictionsService
                .isComponentAllowedInSlot(this.highlightedSlot, dragInfo)
                .then((isComponentAllowed: string) => {
                    this.slotRestrictionsService
                        .isSlotEditable(slotId)
                        .then((slotIsEditable: boolean) => {
                            // The highlighted slot might have changed while waiting for the promise to be resolved.
                            if (this.highlightedSlot && this.highlightedSlot.id === slotId) {
                                const isAllowed =
                                    isComponentAllowed === this.COMPONENT_IN_SLOT_STATUS.ALLOWED &&
                                    slotIsEditable;
                                const mayBeAllowed =
                                    isComponentAllowed ===
                                        this.COMPONENT_IN_SLOT_STATUS.MAYBEALLOWED &&
                                    slotIsEditable;

                                /* Basically the component could be allowed to drop in the slot if the isComponentAllowed status is either ALLOWED or MAYBEALLOWED.
                                 * But in order to differentiate between ALLOWED and MAYBEALLOWED, we store it in highlightedSlot.isAllowed and highlightedSlot.mayBeAllowed respectively.
                                 */
                                this.highlightedSlot.isAllowed = isAllowed || mayBeAllowed;
                                this.highlightedSlot.mayBeAllowed = mayBeAllowed;

                                if (this.highlightedSlot.isAllowed) {
                                    slot.addClass(CmsDragAndDropService.CSS_CLASSES.SLOT_ALLOWED);
                                    if (mayBeAllowed) {
                                        slot.addClass(
                                            CmsDragAndDropService.CSS_CLASSES.SLOT_MAY_BE_ALLOWED
                                        );
                                    }
                                } else {
                                    slot.addClass(
                                        CmsDragAndDropService.CSS_CLASSES.SLOT_NOT_ALLOWED
                                    );
                                }

                                if (
                                    event.type === 'dragenter' &&
                                    (!oldSlotId || oldSlotId !== slotId)
                                ) {
                                    if (
                                        this.highlightedSlot &&
                                        this.highlightedSlot.id === slotId
                                    ) {
                                        this.systemEventService.publish(slotId + '_SHOW_SLOT_MENU');
                                        this.systemEventService.publish(
                                            this.DRAG_AND_DROP_EVENTS.DRAG_OVER,
                                            slotId
                                        ); // can be used to perform any actions on encountering a drag over event.
                                    }
                                }
                            }
                        });
                });
        }

        return this.$q.resolve();
    }

    private addUIHelpers(): void {
        const overlay = this.componentHandlerService.getOverlay();

        // First remove all dropzones.
        overlay.find('.' + CmsDragAndDropService.CSS_CLASSES.UI_HELPER_OVERLAY).remove();

        const vm = this;
        overlay.find(CmsDragAndDropService.SLOT_SELECTOR).each(function() {
            const slot = vm.yjQuery(this);
            const slotHeight = slot[0].offsetHeight;
            const slotWidth = slot[0].offsetWidth;

            const components = slot.find(CmsDragAndDropService.COMPONENT_SELECTOR);

            if (components.length === 0) {
                const uiHelperOverlay = vm.yjQuery('<div></div>');
                uiHelperOverlay.addClass(CmsDragAndDropService.CSS_CLASSES.UI_HELPER_OVERLAY);

                const uiHelper = vm.yjQuery('<div></div>');
                uiHelper.addClass(CmsDragAndDropService.CSS_CLASSES.DROPZONE);
                uiHelper.addClass(CmsDragAndDropService.CSS_CLASSES.DROPZONE_FULL);

                uiHelperOverlay.height(slotHeight);
                uiHelperOverlay.width(slotWidth);

                uiHelperOverlay.append(uiHelper);
                slot.append(uiHelperOverlay);
            } else {
                components.each(function() {
                    const component = vm.yjQuery(this);
                    const componentHeight = component[0].offsetHeight;
                    const componentWidth = component[0].offsetWidth;

                    const uiHelperOverlay = vm.yjQuery('<div></div>');
                    uiHelperOverlay.addClass(CmsDragAndDropService.CSS_CLASSES.UI_HELPER_OVERLAY);

                    uiHelperOverlay.height(componentHeight);
                    uiHelperOverlay.width(componentWidth);

                    const firstHelper = vm.yjQuery('<div></div>');
                    const secondHelper = vm.yjQuery('<div></div>');

                    firstHelper.addClass(CmsDragAndDropService.CSS_CLASSES.DROPZONE);
                    secondHelper.addClass(CmsDragAndDropService.CSS_CLASSES.DROPZONE);

                    if (componentWidth === slotWidth) {
                        firstHelper.addClass(CmsDragAndDropService.CSS_CLASSES.DROPZONE_TOP);
                        secondHelper.addClass(CmsDragAndDropService.CSS_CLASSES.DROPZONE_BOTTOM);
                    } else {
                        firstHelper.addClass(CmsDragAndDropService.CSS_CLASSES.DROPZONE_LEFT);
                        secondHelper.addClass(CmsDragAndDropService.CSS_CLASSES.DROPZONE_RIGHT);
                    }

                    uiHelperOverlay.append(firstHelper);
                    uiHelperOverlay.append(secondHelper);

                    component.append(uiHelperOverlay);
                });
            }
        });
    }

    private cacheElements(): void {
        const overlay = this.componentHandlerService.getOverlay();
        if (!overlay) {
            return;
        }
        const scrollY = this.getWindowScrolling();

        const vm = this;
        overlay.find(CmsDragAndDropService.SLOT_SELECTOR).each(function() {
            const slot = vm.yjQuery(this);
            const slotId = vm.componentHandlerService.getId(slot);
            const slotUuid = vm.componentHandlerService.getUuid(slot);

            // Fetch all components (visible or not) in each slot to get proper position values.
            // The componentHandlerService.getComponentPositionInSlot method is not used here, because it's only based on visible components in the DOM.
            vm.pageContentSlotsComponentsRestService
                .getComponentsForSlot(slotId)
                .then((componentsForSlot: ICMSComponent[]) => {
                    const cachedSlot: CmsDragAndDropCachedSlot = {
                        id: slotId,
                        uuid: slotUuid,
                        original: slot,
                        components: [],
                        rect: vm.getElementRects(slot, scrollY),
                        hint: null
                    };

                    const components = slot.children(CmsDragAndDropService.COMPONENT_SELECTOR);
                    if (components.length === 0) {
                        const hint = slot.find(CmsDragAndDropService.HINT_SELECTOR);
                        cachedSlot.hint =
                            hint.length > 0
                                ? {
                                      original: hint,
                                      rect: vm.getElementRects(hint, scrollY)
                                  }
                                : null;
                    } else {
                        components.each(function(index: number) {
                            const component = vm.yjQuery(this);
                            let positionInSlot = componentsForSlot.findIndex(
                                (componentInSlot: ICMSComponent) => {
                                    return (
                                        componentInSlot.uuid ===
                                        vm.componentHandlerService.getUuid(component)
                                    );
                                }
                            );
                            if (positionInSlot === -1) {
                                positionInSlot = index;
                            }
                            const cachedComponent: CmsDragAndDropCachedComponent = {
                                id: vm.componentHandlerService.getSlotOperationRelatedId(component),
                                type: vm.componentHandlerService.getSlotOperationRelatedType(
                                    component
                                ),
                                original: component,
                                position: positionInSlot,
                                hints: [],
                                rect: vm.getElementRects(component, scrollY)
                            };

                            let positionInComponent = positionInSlot++;
                            component.find(CmsDragAndDropService.HINT_SELECTOR).each(function() {
                                const hint = vm.yjQuery(this);
                                const cachedHint = {
                                    original: hint,
                                    position: positionInComponent++,
                                    rect: vm.getElementRects(hint, scrollY)
                                };

                                cachedComponent.hints.push(cachedHint);
                            });

                            cachedSlot.components.push(cachedComponent);
                        });
                    }

                    vm.cachedSlots[cachedSlot.id] = cachedSlot;
                });
        });
    }

    private clearHighlightedHint(): void {
        if (this.highlightedHint) {
            this.highlightedHint.original.removeClass(
                CmsDragAndDropService.CSS_CLASSES.DROPZONE_HOVERED
            );
            this.highlightedHint.original.removeClass(
                CmsDragAndDropService.CSS_CLASSES.DROPZONE_MAY_BE_ALLOWED
            );
            this.highlightedHint = null;
        }
    }

    private clearHighlightedComponent(): void {
        this.clearHighlightedHint();
        if (this.highlightedComponent) {
            this.highlightedComponent.original.removeClass(
                CmsDragAndDropService.CSS_CLASSES.COMPONENT_DRAGGED_HOVERED
            );
            this.highlightedComponent = null;
        }
    }

    private clearHighlightedSlot(): void {
        this.clearHighlightedComponent();

        if (this.highlightedSlot) {
            this.highlightedSlot.original.removeClass(
                CmsDragAndDropService.CSS_CLASSES.SLOT_ALLOWED
            );
            this.highlightedSlot.original.removeClass(
                CmsDragAndDropService.CSS_CLASSES.SLOT_NOT_ALLOWED
            );
            this.highlightedSlot.original.removeClass(
                CmsDragAndDropService.CSS_CLASSES.SLOT_MAY_BE_ALLOWED
            );

            this.systemEventService.publish('HIDE_SLOT_MENU');
            this.systemEventService.publish(this.DRAG_AND_DROP_EVENTS.DRAG_LEAVE); // can be used to perform any actions on encountering a drag leave event.
        }

        this.highlightedSlot = null;
    }

    private isMouseInRegion(
        event: DragEvent,
        element:
            | CmsDragAndDropCachedComponent
            | CmsDragAndDropCachedSlot
            | CmsDragAndDropCachedComponentHint
    ): boolean {
        const boundingRect = element.rect;

        return (
            event.pageX >= boundingRect.left &&
            event.pageX <= boundingRect.right &&
            event.pageY >= boundingRect.top &&
            event.pageY <= boundingRect.bottom
        );
    }

    private getElementRects(element: JQuery<HTMLElement>, scrollY: number): TypedMap<number> {
        const baseRect = element[0].getBoundingClientRect();
        return {
            left: baseRect.left,
            right: baseRect.right,
            bottom: baseRect.bottom + scrollY,
            top: baseRect.top + scrollY
        };
    }

    private getWindowScrolling(): number {
        return this.$window.pageYOffset;
    }

    private scrollToModifiedSlot(componentId: string): void {
        const component = this.componentHandlerService.getComponentInOverlay(
            componentId,
            this.CONTENT_SLOT_TYPE
        );
        if (component && component.length > 0) {
            component[0].scrollIntoView();
        }
    }

    private getSelector(selector: HTMLElement | EventTarget): JQuery<HTMLElement | EventTarget> {
        return this.yjQuery(selector);
    }

    /**
     * when a PROCESS_COMPONENTS is occuring, it could remove the currently dragged component if this one is not in the viewport.
     * To avoid having the dragged component and it's slot removed we mark then as "KEEP_VISIBLE" when the drag and drop start.
     * On drag end, an event is sent to call a RESTART_PROCESS to add or remove the components according to their viewport visibility and the component and slot are marked as "PROCESS".
     * Using yjQuery.each() here because of MiniCart component (among other slots/compoents) that have multiple occurences in DOM.
     */
    private toggleKeepVisibleComponentAndSlot(keepVisible: boolean): void {
        if (this.dragInfo) {
            const status = keepVisible
                ? this.CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS.KEEP_VISIBLE
                : this.CONTRACT_CHANGE_LISTENER_COMPONENT_PROCESS_STATUS.PROCESS;
            this.yjQuery.each(
                this.componentHandlerService.getComponentUnderSlot(
                    this.dragInfo.componentId,
                    this.dragInfo.componentType,
                    this.dragInfo.slotId
                ),
                (idx: number, element: HTMLElement) => {
                    element.dataset[this.SMARTEDIT_COMPONENT_PROCESS_STATUS] = status;
                }
            );
            this.yjQuery.each(
                this.componentHandlerService.getComponent(
                    this.dragInfo.slotId,
                    this.CONTENT_SLOT_TYPE
                ),
                (idx: number, element: HTMLElement) => {
                    element.dataset[this.SMARTEDIT_COMPONENT_PROCESS_STATUS] = status;
                }
            );
        }
    }
}
