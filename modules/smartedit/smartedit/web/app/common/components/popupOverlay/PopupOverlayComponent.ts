/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    Component,
    EventEmitter,
    Injector,
    Input,
    Output,
    SimpleChanges,
    ViewChild
} from '@angular/core';
import { Placement } from 'popper.js';
import { TypedMap } from '@smart/utils';

import { SeDowngradeComponent } from '../../di';
import { windowUtils } from '../../utils';
import { PopupOverlayConfig, POPUP_OVERLAY_DATA } from './PopupOverlayConfig';
import { PopoverTrigger, PopupOverlayTrigger } from './PopupOverlayTrigger';

/* forbiddenNameSpaces useValue:false */

/**
 *  @ngdoc component
 *  @name smarteditCommonsModule.component:PopupOverlayComponent
 *  @element se-popup-overlay
 *
 *  @description
 *  The PopupOverlay is meant to be a component that allows popups/overlays to be displayed attached to any element.
 *  The element that wrapped with PopupOverlay is applied to is called the anchor element. Once the popup is displayed, it is
 *  positioned relative to the anchor, depending on the configuration provided.
 *
 *  @param {< Object} popupOverlay A popup overlay configuration object that must contain either a template or a templateUrl.
 *  See {@link }smarteditCommonsModule.object:PopupOverlayConfig PopupOverlayConfig}
 *  @param {string} popupOverlay.template|templateUrl Deprecated, use popupOverlay.component. An html string template or a url to an html file
 *  @param {string =} [popupOverlay.halign='right'] Aligns the popup horizontally
 *      relative to the anchor (element). Accepts values: 'left' or 'right'.
 *  @param {string =} [popupOverlay.valign='bottom'] Aligns the popup vertically
 *      relative to the anchor (element). Accepts values: 'top' or 'bottom'.
 *  @param {@ string =} popupOverlayTrigger 'true'|'false'|'click' Controls when the overlay is displayed.
 *      If popupOverlayTrigger is true, the overlay is displayed, if false (or something other then true or click)
 *      then the overlay is hidden.
 *      If popupOverlayTrigger is 'click' then the overlay is displayed when the anchor (element) is clicked on
 *   @param {Object} popupOverlayData, an object that is accesible within legacy AngularJS component scope or can be injected with POPUP_OVERLAY_DATA token in Angular component
 *  @param {& expression =} popupOverlayOnShow An angular expression executed whenever this overlay is displayed
 *  @param {& expression =} popupOverlayOnHide An angular expression executed whenever this overlay is hidden
 *
 *
 *  @example
 *
 *  <se-popup-overlay
 *      [popupOverlay]="config"
 *      [popupOverlayTrigger]="true"
 *      [popupOverlayData]={ item: item }
 *      (popupOverlayOnShow)="handlePopupOverlayDisplayed()"
 *  >
 *      <se-anchor></se-anchor>
 *  </se-popup-overlay>
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-popup-overlay',
    template: `
        <fd-popover
            [placement]="getPlacement()"
            [noArrow]="true"
            [appendTo]="appendTo"
            [triggers]="trigger.length && trigger"
            (isOpenChange)="handleOpenChange($event)"
            #popover
            class="popup-overlay popover-outer"
        >
            <fd-popover-control> <ng-content></ng-content> </fd-popover-control>

            <fd-popover-body #popoverBody class="popover-inner popup-overlay__inner">
                <ng-container *ngIf="isOpen">
                    <div
                        *ngIf="popupOverlay.template"
                        [seCompileHtml]="popupOverlay.template"
                        [scope]="popupOverlayData"
                        [compileHtmlNgController]="popupOverlay.legacyController"
                    ></div>
                    <div
                        *ngIf="popupOverlay.templateUrl"
                        [ngInclude]="popupOverlay.templateUrl"
                        [scope]="popupOverlayData"
                        [compileHtmlNgController]="popupOverlay.legacyController"
                    ></div>
                    <ng-container *ngIf="popupOverlay.component">
                        <ng-container
                            *ngComponentOutlet="
                                popupOverlay.component;
                                injector: popupOverlayInjector
                            "
                        ></ng-container>
                    </ng-container>
                </ng-container>
            </fd-popover-body>
        </fd-popover>
    `
})
export class PopupOverlayComponent {
    @Input() popupOverlay: PopupOverlayConfig;
    @Input() popupOverlayTrigger: PopupOverlayTrigger;
    @Input() popupOverlayData: TypedMap<any>;
    @Output() popupOverlayOnShow: EventEmitter<any> = new EventEmitter();
    @Output() popupOverlayOnHide: EventEmitter<any> = new EventEmitter();

    @ViewChild('popover', { static: false }) popover: any; // PopoverComponent;

    public isOpen: boolean;
    public trigger: string[] = [];
    public appendTo: HTMLElement;
    public popupOverlayInjector: Injector;

    constructor(private injector: Injector) {}

    ngOnInit() {
        this.setTrigger();
        this.createInjector();
        this.appendTo = windowUtils.isIframe()
            ? document.getElementById('smarteditoverlay')
            : document.getElementsByTagName('body')[0];
    }

    ngOnChanges(changes: SimpleChanges) {
        if (changes.popupOverlayTrigger) {
            this.setTrigger();
        }

        if (changes.popupOverlayData) {
            this.createInjector();
        }
    }

    public handleOpenChange(isOpen: boolean): void {
        return isOpen ? this.handleOpen() : this.handleClose();
    }

    public handleOpen(): void {
        this.isOpen = true;
        this.popupOverlayOnShow.emit();
    }

    public handleClose(): void {
        this.isOpen = false;
        this.popupOverlayOnHide.emit();
    }

    public getPlacement(): Placement {
        return `${this.popupOverlay.valign || 'bottom'}-${this.getHorizontalAlign()}` as Placement;
    }

    private setTrigger(): void {
        if (this.popupOverlayTrigger === PopoverTrigger.Click) {
            this.trigger = [this.popupOverlayTrigger];
        }

        if (this.popupOverlayTrigger === PopoverTrigger.Hover) {
            this.trigger = ['mouseenter', 'mouseleave'];
        }

        if (!this.popover) {
            return;
        }

        if (this.popupOverlayTrigger === 'true' || this.popupOverlayTrigger === true) {
            this.popover.open();
        } else {
            this.popover.close();
        }
    }

    private getHorizontalAlign(): 'start' | 'end' {
        if (!this.popupOverlay.halign) {
            return 'start';
        }

        return this.popupOverlay.halign === 'right' ? 'start' : 'end';
    }

    private createInjector(): void {
        this.popupOverlayInjector = Injector.create({
            providers: [
                {
                    provide: POPUP_OVERLAY_DATA,
                    useValue: this.popupOverlayData || {}
                }
            ],
            parent: this.injector
        });
    }
}
