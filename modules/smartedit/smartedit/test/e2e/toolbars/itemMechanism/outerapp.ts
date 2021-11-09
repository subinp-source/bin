/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    moduleUtils,
    AngularJSBootstrapIndicatorService,
    AngularJSLazyDependenciesService,
    CrossFrameEventService,
    HttpBackendService,
    HIDE_TOOLBAR_ITEM_CONTEXT,
    IToolbarServiceFactory,
    SeDowngradeComponent,
    SeEntryModule,
    SHOW_TOOLBAR_ITEM_CONTEXT,
    ToolbarItemType,
    WindowUtils
} from 'smarteditcommons';

/* tslint:disable:max-classes-per-file */

@SeDowngradeComponent()
@Component({
    selector: 'action-context',
    template: `
        <div>Action 5 - Context</div>
    `
})
export class ActionContextComponent {}

@SeDowngradeComponent()
@Component({
    selector: 'standard',
    template: `
        <span id="standardTemplate">STANDARD TEMPLATE</span>
    `
})
export class StandardComponent {}

@SeDowngradeComponent()
@Component({
    selector: 'hybrid-action',
    template: `
        <span id="hybridActiontemplate">HYBRID ACTION TEMPLATE</span>
    `
})
export class HybridActionComponent {}
@SeDowngradeComponent()
@Component({
    selector: 'outer-app-root',
    template: `
        <div class="outer-app-root">
            <label>Smart edit container</label>
            <button class="click-outside-target">Click outside target</button>
            <button id="clear-toolbar" (click)="clearToolbar()">Clear toolbar</button>
            <button id="sendActionsOuter" class="btg btg-default" (click)="sendActions()">
                Send actions from outer to outer
            </button>
            <label id="message">{{ message }}</label>

            <br />
            <button id="removeActionsOuter" class="btg btg-default" (click)="removeActions()">
                Remove actions from outer
            </button>

            <button
                id="showActionToolbarContext5"
                class="btg btg-default"
                (click)="showActionToolbarContext5()"
            >
                Hide Hybrid Action Toolbar Context 5
            </button>

            <button
                id="showHybridActionToolbarContext"
                class="btg btg-default"
                (click)="showHybridActionToolbarContext()"
            >
                Show Hybrid Action Toolbar Context
            </button>

            <button
                id="hideHybridActionToolbarContext"
                class="btg btg-default"
                (click)="hideHybridActionToolbarContext()"
            >
                Hide Hybrid Action Toolbar Context
            </button>
        </div>
    `
})
export class AppRootComponent {
    public message: string;
    public toolbarService: any;
    public testRoot: string;
    constructor(
        public toolbarServiceFactory: IToolbarServiceFactory,
        public httpBackendService: HttpBackendService,
        public crossFrameEventService: CrossFrameEventService,
        public windowUtils: WindowUtils
    ) {
        this.toolbarService = toolbarServiceFactory.getToolbarService('smartEditHeaderToolbar');
        this.testRoot = '../../test/e2e/toolbars/itemMechanism/';
    }

    clearToolbar() {
        (this.toolbarService as any).aliases = [];
        this.toolbarService.removeItemByKey('headerToolbar.logoTemplate');
    }
    sendActions() {
        this.toolbarService.addItems([
            {
                key: 'toolbar.action.action5',
                type: ToolbarItemType.ACTION,
                nameI18nKey: 'toolbar.action.action5',
                callback: () => {
                    this.message = 'Action 5 called';
                },
                icons: [this.testRoot + 'icon5.png'],
                component: StandardComponent,
                contextComponent: ActionContextComponent
            },
            {
                key: 'toolbar.standardTemplate',
                type: ToolbarItemType.TEMPLATE,
                component: StandardComponent
            },
            {
                key: 'toolbar.action.action6',
                type: ToolbarItemType.HYBRID_ACTION,
                nameI18nKey: 'toolbar.action.action6',
                callback: () => {
                    this.message = 'Action 6 called';
                },
                icons: [this.testRoot + 'icon6.png'],
                component: HybridActionComponent,
                contextTemplate: '<div>Hybrid Action 6 - Context</div>'
            },
            {
                key: 'toolbar.action.action8',
                type: ToolbarItemType.ACTION,
                nameI18nKey: 'Icon Test',
                callback: () => {
                    this.message = 'Action 8 called';
                },
                iconClassName: 'hyicon hyicon-clone se-toolbar-menu-ddlb--button__icon'
            },

            // LEGACY SUPPORT

            {
                key: 'toolbar.action.action9',
                type: ToolbarItemType.TEMPLATE,
                include: 'standardTemplate9.html'
            },
            {
                key: 'toolbar.action.action10',
                type: ToolbarItemType.ACTION,
                nameI18nKey: 'toolbar.action.action10',
                callback: () => {
                    this.message = 'Action 10 called';
                },
                iconClassName: 'hyicon hyicon-clone se-toolbar-menu-ddlb--button__icon',
                include: 'actionTemplate10.html'
            }
        ]);
    }

    removeActions() {
        this.toolbarService.removeItemByKey('toolbar.standardTemplate');
        this.toolbarService.removeItemByKey('toolbar.action.action5');
    }

    showActionToolbarContext5() {
        this.crossFrameEventService.publish(SHOW_TOOLBAR_ITEM_CONTEXT, 'toolbar.action.action5');
    }

    showHybridActionToolbarContext() {
        this.crossFrameEventService.publish(SHOW_TOOLBAR_ITEM_CONTEXT, 'toolbar.action.action6');
    }

    hideHybridActionToolbarContext() {
        this.crossFrameEventService.publish(HIDE_TOOLBAR_ITEM_CONTEXT, 'toolbar.action.action6');
    }
}

@SeEntryModule('Outerapp')
@NgModule({
    imports: [CommonModule],
    declarations: [
        AppRootComponent,
        HybridActionComponent,
        StandardComponent,
        ActionContextComponent
    ],
    entryComponents: [
        AppRootComponent,
        HybridActionComponent,
        StandardComponent,
        ActionContextComponent
    ],
    providers: [
        moduleUtils.bootstrap(
            (
                lazy: AngularJSLazyDependenciesService,
                indicator: AngularJSBootstrapIndicatorService
            ) => {
                indicator.onSmarteditContainerReady().subscribe(() => {
                    lazy.$templateCache().put(
                        'standardTemplate9.html',
                        '<span id="standardTemplate9">STANDARD TEMPLATE 9</span>'
                    );
                    lazy.$templateCache().put(
                        'actionTemplate10.html',
                        '<span id="actionTemplate10">ACTION TEMPLATE 10</span>'
                    );
                });
            },
            [AngularJSLazyDependenciesService, AngularJSBootstrapIndicatorService]
        )
    ]
})
export class Outerapp {}
