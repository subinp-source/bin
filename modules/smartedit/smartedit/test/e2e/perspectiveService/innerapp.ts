/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    moduleUtils,
    AbstractDecorator,
    IDecoratorService,
    IFeatureService,
    IPermissionService,
    IPerspectiveService,
    SeDecorator,
    SeEntryModule
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file

@SeDecorator()
@Component({
    selector: 'text-display-decorator',
    template: `
        <div>
            <div class="row" *ngIf="!active"></div>

            {{ textDisplayContent }}

            <ng-content></ng-content>
        </div>
    `
})
export class TextDisplayDecorator extends AbstractDecorator {
    public textDisplayContent = 'Text_is_been_displayed_TextDisplayDecorator';
}

@SeDecorator()
@Component({
    selector: 'permission-display-decorator',
    template: `
        <div>Test permission component <ng-content></ng-content></div>
    `
})
export class PermissionDisplayDecorator extends AbstractDecorator {}

@SeDecorator()
@Component({
    selector: 'permission-registered-inner-decorator',
    template: `
        <div>Test permission decorator registered inner <ng-content></ng-content></div>
    `
})
export class PermissionRegisteredInnerDecorator extends AbstractDecorator {}

@SeDecorator()
@Component({
    selector: 'button-display-decorator',
    template: `
        <div>
            <div class="row" *ngIf="!active"></div>

            <button>{{ buttonDisplayContent }}</button>
            <ng-content></ng-content>
        </div>
    `
})
export class ButtonDisplayDecorator extends AbstractDecorator {
    buttonDisplayContent = 'Button_is_been_Displayed';
}

@SeEntryModule('Innerapp')
@NgModule({
    imports: [CommonModule],
    declarations: [
        TextDisplayDecorator,
        PermissionDisplayDecorator,
        PermissionRegisteredInnerDecorator,
        ButtonDisplayDecorator
    ],
    entryComponents: [
        TextDisplayDecorator,
        PermissionDisplayDecorator,
        PermissionRegisteredInnerDecorator,
        ButtonDisplayDecorator
    ],
    providers: [
        moduleUtils.bootstrap(
            (
                perspectiveService: IPerspectiveService,
                decoratorService: IDecoratorService,
                featureService: IFeatureService,
                permissionService: IPermissionService
            ) => {
                permissionService.registerRule({
                    names: ['se.some.rule.inner.app'],
                    verify() {
                        try {
                            return Promise.resolve(
                                window.sessionStorage.getItem('PERSPECTIVE_SERVICE_RESULT') ===
                                    'true'
                            );
                        } catch (e) {
                            return Promise.resolve(false);
                        }
                    }
                });

                permissionService.registerPermission({
                    aliases: ['se.some.permission.inner.app'],
                    rules: ['se.some.rule.inner.app']
                });

                decoratorService.addMappings({
                    componentType1: ['textDisplayDecorator'],
                    componentType2: ['buttonDisplayDecorator'],
                    SimpleResponsiveBannerComponent: [
                        'textDisplayDecorator',
                        'buttonDisplayDecorator',
                        'permissionDisplayDecorator'
                    ],
                    componentType4: ['permissionRegisteredInnerDecorator']
                });

                featureService.addDecorator({
                    key: 'textDisplayDecorator',
                    nameI18nKey: 'somenameI18nKey',
                    descriptionI18nKey: 'somedescriptionI18nKey'
                });

                featureService.addDecorator({
                    key: 'buttonDisplayDecorator',
                    nameI18nKey: 'somenameI18nKey',
                    descriptionI18nKey: 'somedescriptionI18nKey'
                });

                featureService.addDecorator({
                    key: 'permissionDisplayDecorator',
                    nameI18nKey: 'permissionDisplayI18nKey',
                    descriptionI18nKey: 'permissionDisplayDescriptionI18nKey',
                    permissions: ['se.some.permission']
                });

                featureService.addDecorator({
                    key: 'permissionRegisteredInnerDecorator',
                    nameI18nKey: 'permissionRegisteredInnerI18nKey',
                    descriptionI18nKey: 'permissionRegisteredInnerDescriptionI18nKey',
                    permissions: ['se.some.permission.inner.app']
                });

                perspectiveService.register({
                    key: 'somekey',
                    nameI18nKey: 'somenameI18nKey',
                    descriptionI18nKey: 'somedescriptionI18nKey',
                    features: ['textDisplayDecorator', 'buttonDisplayDecorator'],
                    perspectives: []
                });

                perspectiveService.register({
                    key: 'permissionsKey',
                    nameI18nKey: 'permissionsI18nKey',
                    descriptionI18nKey: 'permissionsDescriptionI18nKey',
                    features: ['permissionDisplayDecorator', 'permissionRegisteredInnerDecorator'],
                    perspectives: []
                });

                // User restricted perspective
                permissionService.registerRule({
                    names: ['se.user.restricted.perspective.rule.inner.app'],
                    verify() {
                        try {
                            return Promise.resolve(
                                window.sessionStorage.getItem('HAS_RESTRICTED_PERSPECTIVE') ===
                                    'true'
                            );
                        } catch (e) {
                            return Promise.resolve(false);
                        }
                    }
                });

                permissionService.registerPermission({
                    aliases: ['se.user.restricted.perspective.permission.inner.app'],
                    rules: ['se.user.restricted.perspective.rule.inner.app']
                });

                perspectiveService.register({
                    key: 'userRestrictedPerspectiveKey',
                    nameI18nKey: 'userRestrictedPerspectiveI18nKey',
                    descriptionI18nKey: 'userRestrictedPerspectiveDescriptionI18nKey',
                    features: ['textDisplayDecorator'],
                    perspectives: [],
                    permissions: ['se.user.restricted.perspective.permission.inner.app']
                });
            },
            [IPerspectiveService, IDecoratorService, IFeatureService, IPermissionService]
        )
    ]
})
export class Innerapp {}
