/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    AngularJSBootstrapIndicatorService,
    GatewayFactory,
    IToolbarService,
    IToolbarServiceFactory,
    SeCustomComponent,
    SeEntryModule,
    ToolbarItemType
} from 'smarteditcommons';
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

/* tslint:disable:max-classes-per-file */

@SeCustomComponent()
@Component({
    selector: 'inner-app-root',
    template: `
        <div class="inner-app-root">
            <label>Smart edit (this is an iFrame !)</label> <br />
            <button id="sendActionsInner" class="btg btg-default" (click)="sendActions()">
                Send Actions to Outer
            </button>
            <label id="message">{{ message }}</label>

            <br />
            <button id="removeAction" class="btg btg-default" (click)="removeAction()">
                Remove Action Outer
            </button>
        </div>
    `
})
export class AppRootComponent {
    public message: string;
    private toolbarService: IToolbarService;

    constructor(
        private toolbarServiceFactory: IToolbarServiceFactory,
        private angularJSBootstrapIndicatorService: AngularJSBootstrapIndicatorService,
        private gatewayFactory: GatewayFactory
    ) {
        this.gatewayFactory.initListener();

        this.angularJSBootstrapIndicatorService.onSmarteditReady().subscribe(() => {
            this.toolbarService = this.toolbarServiceFactory.getToolbarService(
                'smartEditHeaderToolbar'
            );
        });
    }

    removeAction() {
        this.toolbarService.removeItemByKey('toolbar.action.action4');
    }

    sendActions() {
        this.toolbarService.addItems([
            {
                key: 'toolbar.action.action3',
                type: ToolbarItemType.ACTION,
                nameI18nKey: 'toolbar.action.action3',
                callback: () => {
                    this.message = 'Action 3 called';
                },
                icons: ['icon3.png']
            },
            {
                key: 'toolbar.action.action4',
                type: ToolbarItemType.ACTION,
                nameI18nKey: 'toolbar.action.action4',
                callback: () => {
                    this.message = 'Action 4 called';
                },
                icons: ['icon4.png']
            }
        ]);
    }
}
@SeEntryModule('Innerapp')
@NgModule({
    imports: [CommonModule],
    declarations: [AppRootComponent],
    entryComponents: [AppRootComponent]
})
export class Innerapp {}
