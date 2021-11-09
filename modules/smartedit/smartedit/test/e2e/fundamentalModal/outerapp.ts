/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable */

import { Component, NgModule } from '@angular/core';
import {
    SeEntryModule,
    SharedComponentsModule,
    SmarteditCommonsModule,
    SeDowngradeComponent,
    FundamentalModalManagerService,
    FundamentalModalButtonAction,
    FundamentalModalButtonStyle,
    ModalService
} from 'smarteditcommons';
import { ModalRef } from '@fundamental-ngx/core';
import { of } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-modal',
    template: `
        <div id="fundamentalModal">
            <div>
                <h1>Buttons</h1>
                <button id="addButton" (click)="addButton()">Add button</button>
                <button id="removeButton" (click)="removeButton()">Remove button</button>
                <button id="removeAllButtons" (click)="removeAllButtons()">
                    Remove all buttons
                </button>
            </div>

            <div>
                <h1>Title</h1>
                <button id="setTitle" (click)="setTitle()">Add title</button>
            </div>

            <div>
                <h1>Dismiss Button</h1>
                <button id="showDismissButton" (click)="showDismissButton()">
                    Show dismiss button
                </button>
                <button id="hideDismissButton" (click)="hideDismissButton()">
                    Hide dismiss button
                </button>
            </div>
        </div>
    `
})
export class AppModalComponent {
    constructor(private modalManager: FundamentalModalManagerService) {}

    addButton(): void {
        this.modalManager.addButton({
            id: 'id',
            label: 'my_label',
            action: FundamentalModalButtonAction.Dismiss,
            style: FundamentalModalButtonStyle.Default,
            compact: false,
            disabled: false
        });
    }

    removeButton(): void {
        this.modalManager.removeButton('id');
    }

    removeAllButtons(): void {
        this.modalManager.removeAllButtons();
    }

    setTitle(): void {
        this.modalManager.setTitle('New Title');
    }

    showDismissButton(): void {
        this.modalManager.setDismissButtonVisibility(true);
    }

    hideDismissButton(): void {
        this.modalManager.setDismissButtonVisibility(false);
    }
}

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <button id="openModal" (click)="openModal()">Open Modal</button>
            <button id="openModalWithPredefinedConfig" (click)="openModalWithPredefinedConfig()">
                Open Modal With Predefined Config
            </button>

            <div id="returnedData" *ngIf="returnedData">{{ returnedData }}</div>
        </div>
    `
})
export class AppRootComponent {
    public returnedData: string;

    constructor(private modalService: ModalService) {}

    openModal() {
        this.modalService.open({ component: AppModalComponent, templateConfig: {} });
    }

    openModalWithPredefinedConfig() {
        const ref: ModalRef = this.modalService.open({
            component: AppModalComponent,
            templateConfig: {
                title: 'My Title',
                isDismissButtonVisible: true,
                buttons: [
                    {
                        id: 'id_0',
                        label: 'my_label_0',
                        action: FundamentalModalButtonAction.Close,
                        style: FundamentalModalButtonStyle.Default,
                        callback: () => of('My Returned Data'),
                        compact: false,
                        disabled: false
                    },
                    {
                        id: 'id_1',
                        label: 'my_label_1',
                        action: FundamentalModalButtonAction.Dismiss,
                        style: FundamentalModalButtonStyle.Primary,
                        compact: false,
                        disabled: true
                    }
                ]
            }
        });

        ref.afterClosed.subscribe(
            (returnedData: string) => {
                this.returnedData = returnedData;
            },
            (error) => (this.returnedData = 'Modal dismissed')
        );
    }
}

@SeEntryModule('ModalApp')
@NgModule({
    imports: [SharedComponentsModule, SmarteditCommonsModule, CommonModule],
    declarations: [AppRootComponent, AppModalComponent],
    entryComponents: [AppRootComponent, AppModalComponent]
})
export class ModalApp {}
