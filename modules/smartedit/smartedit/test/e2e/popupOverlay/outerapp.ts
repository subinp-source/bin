/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
import { Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    PopupOverlayConfig,
    PopupOverlayModule,
    POPUP_OVERLAY_DATA,
    SeDowngradeComponent,
    SeEntryModule,
    TypedMap
} from 'smarteditcommons';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'se-popup-content',
    template: `
        <div id="popup">{{ data.message }}</div>
    `
})
export class PopupOverlayContentComponent {
    constructor(@Inject(POPUP_OVERLAY_DATA) public data: TypedMap<string>) {}
}

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div>
            <form>
                <h2>Type</h2>
                <label for="type-legacy">Legacy</label>
                <input
                    type="radio"
                    name="type"
                    id="type-legacy"
                    [(ngModel)]="type"
                    [value]="'legacy'"
                />

                <label for="type-angular">Angular</label>
                <input
                    type="radio"
                    name="type"
                    id="type-angular"
                    [(ngModel)]="type"
                    [value]="'angular'"
                />
                <h2>Trigger</h2>

                <label for="trigger-click">Click</label>
                <input
                    type="radio"
                    name="trigger"
                    id="trigger-click"
                    [(ngModel)]="trigger[type]"
                    [value]="'click'"
                />

                <label for="trigger-hover">Click</label>
                <input
                    type="radio"
                    name="trigger"
                    id="trigger-hover"
                    [(ngModel)]="trigger[type]"
                    [value]="'hover'"
                />

                <label for="trigger-displayed">Always displayed</label>
                <input
                    type="radio"
                    name="trigger"
                    id="trigger-displayed"
                    [(ngModel)]="trigger[type]"
                    [value]="true"
                />

                <label for="trigger-hidden">Always hidden</label>
                <input
                    type="radio"
                    name="trigger"
                    id="trigger-hidden"
                    [(ngModel)]="trigger[type]"
                    [value]="false"
                />
            </form>
            <div id="legacyPopup">
                <se-popup-overlay
                    [popupOverlay]="legacyConfig"
                    [popupOverlayTrigger]="trigger.legacy"
                    (popupOverlayOnHide)="handleOnHide()"
                    (popupOverlayOnShow)="handleOnShow()"
                >
                    <div id="anchor">Angular JS</div>
                </se-popup-overlay>
            </div>

            <div id="angularPopup">
                <se-popup-overlay
                    [popupOverlay]="angularConfig"
                    [popupOverlayData]="{ message: 'Hello from component!' }"
                    [popupOverlayTrigger]="trigger.angular"
                    (popupOverlayOnHide)="handleOnHide()"
                    (popupOverlayOnShow)="handleOnShow()"
                >
                    <div id="anchor">Angular</div>
                </se-popup-overlay>
            </div>
            <div id="message">{{ message }}</div>
        </div>
    `
})
export class AppRootComponent {
    public type: 'legacy' | 'angular';
    public message: string;
    public trigger = {
        legacy: 'click',
        angular: 'click'
    };
    public legacyConfig: PopupOverlayConfig = {
        templateUrl: 'popupOverlayTemplateUrl.html',
        valign: 'bottom',
        halign: 'left',
        legacyController: {
            alias: '$ctrl',
            value() {
                this.message = 'Hello from controller!';
            }
        }
    };

    public angularConfig: PopupOverlayConfig = {
        component: PopupOverlayContentComponent,
        valign: 'bottom',
        halign: 'left'
    };

    handleOnHide() {
        this.message = 'On Hide';
    }

    handleOnShow() {
        this.message = 'On Show';
    }
}

@SeEntryModule('popupOverlayApp')
@NgModule({
    imports: [PopupOverlayModule, CommonModule, FormsModule],
    providers: [{ provide: POPUP_OVERLAY_DATA, useValue: null }],
    declarations: [AppRootComponent, PopupOverlayContentComponent],
    entryComponents: [AppRootComponent, PopupOverlayContentComponent]
})
export class MoreTextAppNg {}
