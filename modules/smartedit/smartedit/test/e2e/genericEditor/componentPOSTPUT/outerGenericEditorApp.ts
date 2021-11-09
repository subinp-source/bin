/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';

// tslint:disable:max-classes-per-file

import {
    GenericEditorComponent,
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
        <span>id:&nbsp;</span> <span id="componentId">{{ componentId }}</span>
        <se-generic-editor
            #genericEditor
            [smarteditComponentType]="thesmarteditComponentType"
            [structureApi]="structureApi"
            [contentApi]="contentApi"
            [updateCallback]="updateCallback"
        >
        </se-generic-editor>
    `
})
export class AppRoot {
    thesmarteditComponentType = 'thesmarteditComponentType';
    structureApi = 'cmswebservices/v1/types/:smarteditComponentType';
    displaySubmit = true;
    displayCancel = true;
    contentApi = 'previewApi';
    componentId = '';

    @ViewChild('genericEditor', { static: false }) genericEditor: GenericEditorComponent;

    constructor(
        public restServiceFactory: RestServiceFactory,
        sharedDataService: ISharedDataService
    ) {
        restServiceFactory.setDomain('thedomain');
        sharedDataService.set('experience', {
            siteDescriptor: {
                uid: 'someSiteUid'
            },
            catalogDescriptor: {
                catalogId: 'somecatalogId',
                catalogVersion: 'someCatalogVersion'
            }
        });
    }
    updateCallback = () => {
        this.componentId = this.genericEditor.editor.smarteditComponentId;
    };
}

@SeEntryModule('GenericEditorApp')
@NgModule({
    imports: [CommonModule, SeGenericEditorModule],
    declarations: [AppRoot],
    entryComponents: [AppRoot]
})
export class GenericEditorApp {}
