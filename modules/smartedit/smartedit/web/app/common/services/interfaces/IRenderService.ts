/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc service
 * @name smarteditServicesModule.service:IRenderService
 *
 * @description
 * The renderService is responsible for rendering and resizing component overlays, and re-rendering components and slots
 * from the storefront.
 */
import {
    CrossFrameEventService,
    INotificationService,
    IPageInfoService,
    IPerspectiveService,
    ModalService,
    SystemEventService
} from '../';
import { WindowUtils } from 'smarteditcommons/utils/WindowUtils';
import { EVENT_OUTER_FRAME_CLICKED } from '../../utils';
import { INotificationConfiguration } from './INotificationService';

export abstract class IRenderService {
    private readonly HOTKEY_NOTIFICATION_CONFIGURATION: INotificationConfiguration;

    private readonly KEY_CODES = {
        ESC: 27
    };

    constructor(
        protected yjQuery: JQueryStatic,
        protected systemEventService: SystemEventService,
        private notificationService: INotificationService,
        private pageInfoService: IPageInfoService,
        private perspectiveService: IPerspectiveService,
        protected crossFrameEventService: CrossFrameEventService,
        private windowUtils: WindowUtils,
        private modalService: ModalService
    ) {
        this.HOTKEY_NOTIFICATION_CONFIGURATION = {
            id: 'HOTKEY_NOTIFICATION_ID',
            componentName: 'PerspectiveSelectorHotkeyNotificationComponent'
        };

        this._bindEvents();
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IRenderService#renderSlots
     * @methodOf smarteditServicesModule.service:IRenderService
     *
     * @description
     * Re-renders a slot in the page
     *
     * @param {Promise} promise a promise
     */
    renderSlots(_slotIds: string[] | string): Promise<any> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IRenderService#renderComponent
     * @methodOf smarteditServicesModule.service:IRenderService
     *
     * @description
     * Re-renders a component in the page.
     *
     * @param {String} componentId The ID of the component.
     * @param {String} componentType The type of the component.
     * @param {String} customContent The custom content to replace the component content with. If specified, the
     * component content will be rendered with it, instead of the accelerator's. Optional.
     *
     * @returns {Promise} Promise that will resolve on render success or reject if there's an error. When rejected,
     * the promise returns an Object{message, stack}.
     */
    renderComponent(componentId: string, componentType: string): Promise<string | boolean> {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IRenderService#renderRemoval
     * @methodOf smarteditServicesModule.service:IRenderService
     *
     * @description
     * This method removes a component from a slot in the current page. Note that the component is only removed
     * on the frontend; the operation does not propagate to the backend.
     *
     * @param {String} componentId The ID of the component to remove.
     * @param {String} componentType The type of the component.
     *
     * @returns {Object} Object wrapping the removed component.
     */
    renderRemoval(componentId: string, componentType: string, slotId: string): JQuery {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IRenderService#renderPage
     * @methodOf smarteditServicesModule.service:IRenderService
     *
     * @description
     * Re-renders all components in the page.
     * this method first resets the HTML content all of components to the values saved by {@link smarteditServicesModule.service:decoratorService#methods_storePrecompiledComponent} at the last $compile time
     * then requires a new compilation.
     */
    renderPage(isRerender: boolean): void {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IRenderService#toggleOverlay
     * @methodOf smarteditServicesModule.service:IRenderService
     *
     * @description
     * Toggles on/off the visibility of the page overlay (containing the decorators).
     *
     * @param {Boolean} showOverlay Flag that indicates if the overlay must be displayed.
     */
    toggleOverlay(isVisible: boolean): void {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IRenderService#refreshOverlayDimensions
     * @methodOf smarteditServicesModule.service:IRenderService
     *
     * @description
     * This method updates the position of the decorators in the overlay. Normally, this method must be executed every
     * time the original storefront content is updated to keep the decorators correctly positioned.
     *
     */
    refreshOverlayDimensions(): void {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IRenderService#blockRendering
     * @methodOf smarteditServicesModule.service:IRenderService
     *
     * @description
     * Toggles the rendering to be blocked or not which determines whether the overlay should be rendered or not.
     *
     * @param {Boolean} isBlocked Flag that indicates if the rendering should be blocked or not.
     */
    blockRendering(isBlocked: boolean): void {
        'proxyFunction';
        return null;
    }

    /**
     * @ngdoc method
     * @name smarteditServicesModule.service:IRenderService#isRenderingBlocked
     * @methodOf smarteditServicesModule.service:IRenderService
     *
     * @description
     * This method returns a boolean that determines whether the rendering is blocked or not.
     *
     * @param {Boolean} An indicator if the rendering is blocked or not.
     */
    isRenderingBlocked(): Promise<boolean> {
        'proxyFunction';
        return null;
    }

    protected _getDocument(): Document {
        return document;
    }

    private _bindEvents(): void {
        this._getDocument().addEventListener('keyup', (event: KeyboardEvent) =>
            this._keyUpEventHandler(event)
        );
        this._getDocument().addEventListener('click', () => this._clickEvent());
    }

    private _keyUpEventHandler(event: KeyboardEvent): Promise<void> {
        if (!this._areAllModalWindowsClosed()) {
            return Promise.resolve();
        }

        return this._shouldEnableKeyPressEvent(event).then((enableKeyPressEvent: boolean) => {
            if (enableKeyPressEvent) {
                this._keyPressEvent();
            }
        });
    }

    private _shouldEnableKeyPressEvent(event: KeyboardEvent): Promise<boolean> {
        return new Promise((resolve) => {
            try {
                return this.pageInfoService.getPageUUID().then((pageUUID: string) => {
                    if (pageUUID) {
                        return this.perspectiveService
                            .isHotkeyEnabledForActivePerspective()
                            .then((isHotkeyEnabled: boolean) => {
                                return resolve(
                                    event.which === this.KEY_CODES.ESC && isHotkeyEnabled
                                );
                            });
                    }
                    return resolve(false);
                });
            } catch (e) {
                return resolve(false);
            }
        });
    }

    private _keyPressEvent(): void {
        this.isRenderingBlocked().then((isBlocked: boolean) => {
            if (!isBlocked) {
                this.blockRendering(true);
                this.renderPage(false);
                this.notificationService.pushNotification(this.HOTKEY_NOTIFICATION_CONFIGURATION);
                this.systemEventService.publishAsync('OVERLAY_DISABLED');
            } else {
                this.blockRendering(false);
                this.renderPage(true);
                this.notificationService.removeNotification(
                    this.HOTKEY_NOTIFICATION_CONFIGURATION.id
                );
            }
        });
    }

    private _clickEvent(): Promise<void> {
        if (!this.windowUtils.isIframe()) {
            this.crossFrameEventService.publish(EVENT_OUTER_FRAME_CLICKED);
        }

        return this.isRenderingBlocked().then((isBlocked: boolean) => {
            if (isBlocked && !this.windowUtils.isIframe()) {
                this.blockRendering(false);
                this.renderPage(true);
                return this.notificationService.removeNotification(
                    this.HOTKEY_NOTIFICATION_CONFIGURATION.id
                );
            }
            return null;
        });
    }

    private _areAllModalWindowsClosed(): boolean {
        return !this.modalService.hasOpenModals();
    }
}
