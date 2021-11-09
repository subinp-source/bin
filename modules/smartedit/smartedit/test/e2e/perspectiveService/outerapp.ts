/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    moduleUtils,
    IFeatureService,
    IPermissionService,
    IPerspectiveService,
    SeEntryModule
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file

@SeEntryModule('Outerapp')
@NgModule({
    imports: [CommonModule],

    providers: [
        moduleUtils.bootstrap(
            (
                perspectiveService: IPerspectiveService,
                featureService: IFeatureService,
                permissionService: IPermissionService
            ) => {
                permissionService.registerRule({
                    names: ['se.some.rule'],
                    verify() {
                        return Promise.resolve(
                            window.sessionStorage.getItem('PERSPECTIVE_SERVICE_RESULT') === 'true'
                        );
                    }
                });

                permissionService.registerPermission({
                    aliases: ['se.some.permission'],
                    rules: ['se.some.rule']
                });

                featureService.addToolbarItem({
                    toolbarId: 'smartEditPerspectiveToolbar',
                    key: 'se.some.item.key',
                    type: 'ACTION',
                    nameI18nKey: 'se.some.label',
                    iconClassName: 'some class',
                    priority: 4,
                    section: 'left',
                    permissions: ['se.some.permission']
                });

                perspectiveService.register({
                    key: 'permissionsKey',
                    nameI18nKey: 'permissionsI18nKey',
                    descriptionI18nKey: 'permissionsDescriptionI18nKey',
                    features: ['se.some.item.key'],
                    perspectives: []
                });
            },
            [IPerspectiveService, IFeatureService, IPermissionService]
        )
    ]
})
export class Outerapp {}
