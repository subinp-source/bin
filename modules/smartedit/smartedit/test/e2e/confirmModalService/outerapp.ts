/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

/* forbiddenNameSpaces angular.module:false */
/* tslint:disable:max-classes-per-file */

import * as angular from 'angular';
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    ConfirmationModalConfig,
    IConfirmationModalService,
    LegacyConfirmationModalConfig,
    SeDowngradeComponent,
    SeEntryModule
} from 'smarteditcommons';

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <button id="confirmation-modal-trigger-0" (click)="openConfirmationModal(0)">
                Open confirmation modal with description</button
            ><br />
            <button id="confirmation-modal-trigger-1" (click)="openConfirmationModal(1)">
                Open confirmation modal with scope</button
            ><br />
            <button id="confirmation-modal-trigger-2" (click)="openConfirmationModal(2)">
                Open confirmation modal with templateUrl and scope
            </button>
        </div>
    `
})
export class AppRootComponent {
    private modalConfigs: (ConfirmationModalConfig | LegacyConfirmationModalConfig)[] = [
        {
            title: 'my.confirmation.title',
            description: 'my.confirmation.message'
        },
        {
            title: 'my.confirmation.title',
            template: '<div>scopeParam: {{modalController.scopeParam}}</div>',
            scope: ({
                scopeParam: 'Scope Param Rendered'
            } as unknown) as angular.IScope
        },
        {
            title: 'my.confirmation.title',
            templateUrl: 'confirmModalWithTemplateUrl.html',
            scope: ({
                scopeParam: 'Scope Param Rendered'
            } as unknown) as angular.IScope
        }
    ];

    constructor(private confirmationModalService: IConfirmationModalService) {}

    public openConfirmationModal(idx: number) {
        this.confirmationModalService.confirm(this.modalConfigs[idx]);
    }
}

angular.module('ConfirmationModalApp', []).run(($templateCache: angular.ITemplateCacheService) => {
    $templateCache.put(
        'confirmModalWithTemplateUrl.html',
        '<div>scopeParam: {{modalController.scopeParam}}</div>'
    );
});

@SeEntryModule('ConfirmationModalApp')
@NgModule({
    imports: [CommonModule],
    declarations: [AppRootComponent],
    entryComponents: [AppRootComponent]
})
export class ConfirmationModalApp {}
