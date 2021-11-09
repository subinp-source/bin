/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeInjectable } from '../../di';
import * as angular from 'angular';
import {
    YPopoverConfig,
    YPopoverOnClickOutside,
    YPopoverTrigger
} from '../../components/popover/yPopoverDirective';
import * as lo from 'lodash';
import Popper from 'popper.js';
import ResizeObserver from 'resize-observer-polyfill';

export interface YPopupOverlayEvent {
    event: string;
    handle: (event: Event) => void;
}

export interface YPopupOverlayScope extends angular.IScope {
    closePopupOverlay: () => void;
}

export type YPopupOverlayTrigger = YPopoverTrigger | string | boolean;

export interface YPopupEngineConfig {
    onCreate: (conf: Popper.Data) => void;
    onUpdate: (conf: Popper.Data) => void;
    placement: Popper.Placement;
    modifiers: Popper.Modifiers;
    trigger: YPopupOverlayTrigger;
    onClickOutside: YPopoverOnClickOutside;
}

export interface YPopupOverlayEvent {
    event: string;
    handle: (event: Event) => void;
}

export interface IYPopupEngine {
    show(): void;
    configure(conf: YPopoverConfig): void;
    hide(): void;
    setTrigger(newTrigger: YPopupOverlayTrigger): void;
    dispose(): void;
}
// tslint:disable:max-classes-per-file

/**
 * @ngdoc service
 * @name yPopupOverlayModule.service:yPopupEngine
 * @description
 * Service that positions a template relative to an anchor element.
 */

@SeInjectable()
export class YPopupEngineService {
    constructor(
        $document: angular.IDocumentService,
        $compile: angular.ICompileService,
        $timeout: angular.ITimeoutService
    ) {
        class YPopupEngine implements IYPopupEngine {
            private eventListeners: (() => void)[] = [];
            private isOpen: boolean = false;
            private oldTrigger: any = null;
            private container: string | HTMLElement;
            private disposing: boolean;
            private onChanges: (element: HTMLElement, data: Popper.Data) => void;
            private onHide: () => void;
            private onShow: () => void;
            private popupElement: HTMLElement;
            private popupScope: angular.IScope;
            private popupInstance: Popper;
            private config: YPopupEngineConfig;
            private resizeObserver = new ResizeObserver((entries) => {
                for (const entry of entries) {
                    const element = entry.target as HTMLElement;
                    if (element === this.popupElement) {
                        this.update();
                    }
                }
            });

            private popperConfig: Popper.PopperOptions = {
                modifiers: {
                    preventOverflow: {
                        padding: 0,
                        boundariesElement: 'viewport'
                    }
                }
            };

            constructor(
                private anchorElement: Element,
                private template: string,
                private scope: angular.IScope,
                config: YPopoverConfig
            ) {
                this.configure(config);
            }

            /**
             * @ngdoc method
             * @name yPopupOverlayModule.service:yPopupEngine#configure
             * @methodOf yPopupOverlayModule.service:yPopupEngine
             *
             * @description
             * Configures the popup engine.
             *
             * @param {Object=} config The configuration object.
             * @param {String=} [config.placement='bottom'] The placement of the popup, see {@link https://popper.js.org/popper-documentation.html#Popper.Defaults.placement options}.
             * @param {String=} [config.trigger='hover'] For the trigger, see {@link yPopupOverlayModule.service:yPopupEngine#setTrigger setTrigger} method for the available triggers.
             * @param {Function=} [config.onChanges] Called when a change occurs on the popup's position or creation.
             * @param {Function=} [config.onShow] Called when the popup is created.
             * @param {Function=} [config.onHide] Called when the popup is hidden.
             * @param {(String|HTMLElement)=} [config.container=document.body] The parent element that contains the popup. It can be a CSS selector or a HTMLElement.
             * @param {String=} [config.onClickOutside='close'] 'close'|'none'. setting to none will not affect the popup when the user clicks outside of the element.
             * @param {Object=} [config.modifiers] Modifiers provided by the popper library, see the {@link https://popper.js.org/popper-documentation.html#Popper.Defaults.modifiers popper} documentation.
             *
             */

            configure(conf: YPopoverConfig): void {
                const config = conf || ({} as YPopoverConfig);

                this.onChanges = config.onChanges;
                this.container = config.container || $document[0].body;
                this.onShow = config.onShow;
                this.onHide = config.onHide;
                this.disposing = false;

                this.config = {
                    placement: config.placement || 'bottom',
                    modifiers: config.modifiers,
                    trigger: config.trigger || YPopoverTrigger.Hover,
                    onClickOutside: config.onClickOutside || YPopoverOnClickOutside.Close,
                    onCreate: (object: Popper.Data) => this._onChanges(object),
                    onUpdate: (object: Popper.Data) => this._onChanges(object)
                };

                this.setTrigger(this.config.trigger);
            }

            /**
             * @ngdoc method
             * @name yPopupOverlayModule.service:yPopupEngine#show
             * @methodOf yPopupOverlayModule.service:yPopupEngine
             *
             * @description
             * Explicitly shows the popup.
             */
            show(): void {
                if (this.isOpen || this.disposing) {
                    return;
                }

                this.isOpen = true;

                this.popupScope = this.scope.$new(false);
                this.popupElement = $compile(this.template)(this.popupScope)[0];

                // FIXME: CMSX-6084
                this.popupInstance = new Popper(
                    this.anchorElement,
                    this.popupElement,
                    lo.merge(this.config, this.popperConfig)
                );

                if (typeof this.container === 'string') {
                    angular.element(this.container)[0].appendChild(this.popupElement);
                } else {
                    this.container.appendChild(this.popupElement);
                }

                this.resizeObserver.observe(this.popupElement);

                if (this.onShow) {
                    this.onShow();
                }

                this.update();
            }

            /**
             * @ngdoc method
             * @name yPopupOverlayModule.service:yPopupEngine#hide
             * @methodOf yPopupOverlayModule.service:yPopupEngine
             *
             * @description
             * Explicitly hides the popup by removing it from the DOM.
             */
            hide(): void {
                if (!this.isOpen) {
                    return;
                }

                this.resizeObserver.unobserve(this.popupElement);

                this.popupScope.$destroy();
                this.popupInstance.destroy();
                this.popupElement.parentNode.removeChild(this.popupElement);

                if (this.onHide) {
                    this.onHide();
                }

                this.isOpen = false;
            }

            /**
             * @ngdoc method
             * @name yPopupOverlayModule.service:yPopupEngine#update
             * @methodOf yPopupOverlayModule.service:yPopupEngine
             *
             * @description
             * Updates the position of the popup.
             */

            update(): void {
                if (this.isOpen) {
                    this.popupInstance.scheduleUpdate();
                }
            }

            /**
             * @ngdoc method
             * @name yPopupOverlayModule.service:yPopupEngine#setTrigger
             * @methodOf yPopupOverlayModule.service:yPopupEngine
             *
             * @description
             * Configures the anchor's trigger type.
             *
             * @param {String} newTrigger The trigger type: 'click', 'hover', or 'focus'.
             */
            setTrigger(newTrigger: YPopupOverlayTrigger): void {
                if (this.oldTrigger === newTrigger) {
                    return;
                }

                this.config.trigger = newTrigger;
                this.oldTrigger = newTrigger;
                this._removeTriggers();

                this._composeEvents(newTrigger).forEach((event: YPopupOverlayEvent) =>
                    this._setEventListener(event)
                );

                if (
                    this.config.onClickOutside === YPopoverOnClickOutside.Close &&
                    this.config.trigger === YPopoverTrigger.Click
                ) {
                    const bodyClick = ($event: Event) => {
                        return (
                            $event.target !== this.anchorElement &&
                            !this.anchorElement.contains($event.target as HTMLElement) &&
                            this.hide()
                        );
                    };

                    $document[0].body.addEventListener('click', ($event) => {
                        $timeout(() => {
                            bodyClick($event);
                        });
                    });

                    this.eventListeners.push(() => {
                        $document[0].body.removeEventListener('click', bodyClick);
                    });
                }
            }

            /**
             * @ngdoc method
             * @name yPopupOverlayModule.service:yPopupEngine#dispose
             * @methodOf yPopupOverlayModule.service:yPopupEngine
             *
             * @description
             * Removes the popup from the DOM and unregisters all events from the anchor.
             */

            dispose(): void {
                this.disposing = true;
                this.hide();
                this._removeTriggers();
            }

            /**
             * Removes event listeners from the anchor element.
             * @private
             */

            private _removeTriggers(): void {
                this.eventListeners.forEach((unRegisterEvent: () => void) => {
                    unRegisterEvent();
                });
                this.eventListeners = [];
            }

            private _onChanges(dataObject: Popper.Data): void {
                if (this.onChanges) {
                    this.onChanges(this.popupElement, dataObject);
                }
            }

            private _handleShow($event: Event): void {
                $event.stopPropagation();
                $event.preventDefault();
                this.show();
            }

            private _handleHide($event: Event): void {
                $event.stopPropagation();
                $event.preventDefault();
                this.hide();
            }

            private _setEventListener(event: YPopupOverlayEvent): void {
                this.anchorElement.addEventListener(event.event, ($event: Event) => {
                    $timeout(() => {
                        event.handle($event);
                    });
                });
                this.eventListeners.push(() => {
                    this.anchorElement.removeEventListener(event.event, event.handle);
                });
            }

            private _composeEvents(trigger: YPopupOverlayTrigger): YPopupOverlayEvent[] {
                switch (trigger) {
                    case YPopoverTrigger.Click:
                        return [
                            {
                                event: 'click',
                                handle: () => (this.isOpen ? this.hide() : this.show())
                            }
                        ];

                    case YPopoverTrigger.Hover:
                        return [
                            {
                                event: 'mouseenter',
                                handle: (event: Event) => this._handleShow(event)
                            },
                            {
                                event: 'mouseleave',
                                handle: (event: Event) => this._handleHide(event)
                            }
                        ];
                    case YPopoverTrigger.Focus:
                        return [
                            {
                                event: 'focus',
                                handle: (event: Event) => this._handleShow(event)
                            },
                            {
                                event: 'blur',
                                handle: (event: Event) => this._handleHide(event)
                            }
                        ];
                    case 'show':
                    case 'true':
                    case true:
                        this.show();
                        return [];
                    case 'hide':
                    case 'false':
                    case false:
                        this.hide();
                        return [];
                    default:
                        return [];
                }
            }
        }

        return YPopupEngine;
    }
}
