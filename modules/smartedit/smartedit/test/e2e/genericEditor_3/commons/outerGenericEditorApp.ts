/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// tslint:disable:max-classes-per-file

import {
    EditorFieldMappingService,
    GenericEditorTabService,
    ISharedDataService,
    RestServiceFactory,
    SeDowngradeComponent,
    SeEntryModule,
    SeGenericEditorModule
} from 'smarteditcommons';

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <se-generic-editor
                [smarteditComponentId]="thesmarteditComponentId"
                [smarteditComponentType]="thesmarteditComponentType"
                [structureApi]="structureApi"
                [contentApi]="contentApi"
            >
            </se-generic-editor>
        </div>
    `
})
export class AppRoot {
    thesmarteditComponentType: string;
    thesmarteditComponentId: string;
    structureApi: string;
    CONTEXT_CATALOG: string;
    CONTEXT_CATALOG_VERSION: string;
    contentApi: string;
    constructor(
        private restServiceFactory: RestServiceFactory,
        private sharedDataService: ISharedDataService,
        private genericEditorTabService: GenericEditorTabService,
        private editorFieldMappingService: EditorFieldMappingService
    ) {}
    ngOnInit() {
        const rawConfig = window.sessionStorage.getItem('TEST_CONFIGS');
        const testConfig = rawConfig ? JSON.parse(rawConfig) : {};

        this.restServiceFactory.setDomain('thedomain');
        this.sharedDataService.set('experience', {
            siteDescriptor: {
                uid: 'someSiteUid'
            },
            catalogDescriptor: {
                catalogId: 'electronics',
                catalogVersion: 'staged'
            }
        });

        this.thesmarteditComponentType = 'thesmarteditComponentType';
        this.thesmarteditComponentId = 'thesmarteditComponentId';
        this.structureApi = 'cmswebservices/v1/types/:smarteditComponentType';
        this.CONTEXT_CATALOG = 'CURRENT_CONTEXT_CATALOG';
        this.CONTEXT_CATALOG_VERSION = 'CURRENT_CONTEXT_CATALOG_VERSION';

        this.contentApi =
            '/cmswebservices/v1/catalogs/' +
            this.CONTEXT_CATALOG +
            '/versions/' +
            this.CONTEXT_CATALOG_VERSION +
            '/items';

        if (testConfig.multipleTabs) {
            this.editorFieldMappingService.addFieldTabMapping(null, null, 'visible', 'visibility');
            this.editorFieldMappingService.addFieldTabMapping(null, null, 'id', 'administration');
            this.editorFieldMappingService.addFieldTabMapping(
                null,
                null,
                'modifiedtime',
                'administration'
            );
            this.editorFieldMappingService.addFieldTabMapping(
                'DateTime',
                null,
                'creationtime',
                'administration'
            );

            this.genericEditorTabService.configureTab('default', {
                priority: 5
            });
            this.genericEditorTabService.configureTab('administration', {
                priority: 4
            });
        }
        this.genericEditorTabService.configureTab('default', {
            priority: 5
        });
        this.genericEditorTabService.configureTab('administration', {
            priority: 4
        });
    }
}
@SeEntryModule('GenericEditorApp')
@NgModule({
    imports: [SeGenericEditorModule, CommonModule],
    declarations: [AppRoot],
    entryComponents: [AppRoot],
    providers: []
})
export class GenericEditorApp {}
