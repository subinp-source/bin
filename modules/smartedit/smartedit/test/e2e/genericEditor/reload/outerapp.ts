/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import * as lo from 'lodash';

import {
    moduleUtils,
    EditorFieldMappingService,
    GenericEditorWidgetData,
    GENERIC_EDITOR_WIDGET_DATA,
    Payload,
    SeDowngradeComponent,
    SeEntryModule,
    SeGenericEditorModule,
    SystemEventService,
    YJQUERY_TOKEN
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file

@SeDowngradeComponent()
@Component({
    selector: 'custom-reload-button',
    template: `
        '<input type="button" id="load-button" (click)="load()" value="Reload" />
        <pre>model:{{ widget.model | json }}</pre>
        '
    `
})
export class CustomReloadButtonComponent {
    constructor(
        @Inject(GENERIC_EDITOR_WIDGET_DATA) public widget: GenericEditorWidgetData<any>,
        private systemEventService: SystemEventService
    ) {}

    load() {
        this.systemEventService.publishAsync('load-structure', {
            structure: {
                attributes: [
                    {
                        cmsStructureType: 'ShortString',
                        qualifier: 'name',
                        i18nKey: 'type.anyComponentType.name.name'
                    },
                    {
                        cmsStructureType: 'RichText',
                        qualifier: 'richtext',
                        i18nKey: 'type.anyComponentType.richtext.name'
                    },
                    {
                        cmsStructureType: 'ShortString',
                        qualifier: 'componentCustomField',
                        i18nKey: 'type.anyComponentType.componentcustomfield.name'
                    }
                ],
                category: 'TEST'
            },
            content: this.widget.model
        });
    }
}

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <input
            type="button"
            id="set-new-structure-button"
            (click)="setNewStructure()"
            value="Reload structure"
        />

        <input
            type="button"
            id="set-new-structure-api-button"
            (click)="setNewStructureApi()"
            value="Reload structure api"
        />

        <input
            type="button"
            id="post-mode-button"
            (click)="setPOSTMode()"
            value="New component (POST mode)"
        />

        <div class="container">
            <se-generic-editor
                [smarteditComponentId]="componentId"
                [smarteditComponentType]="componentType"
                [structureApi]="structureApi"
                [structure]="structure"
                [contentApi]="contentApi"
                [content]="content"
                [updateCallback]="onUpdate"
            >
            </se-generic-editor>
            <div class="generic-editor-status"></div>
        </div>
    `
})
export class AppRootComponent {
    componentType = 'AnyComponent';
    componentId = 'anyComponentId';

    content: Payload = null;
    contentApi = '/cmswebservices/v1/catalogs/apparel-ukContentCatalog/versions/Staged/items';

    structure: Payload = null;
    structureApi = '/cmswebservices/v1/types/defaultComponent';

    NEW_STRUCTURE = {
        attributes: [
            {
                cmsStructureType: 'ShortString',
                qualifier: 'name',
                i18nKey: 'type.anyComponentType.name.name'
            },
            {
                cmsStructureType: 'RichText',
                qualifier: 'richtext',
                i18nKey: 'type.anyComponentType.richtext.name'
            }
        ],
        category: 'TEST'
    };
    NEW_CONTENT = {
        type: 'anyComponentData',
        name: 'Any new name',
        pk: '1234567890',
        typeCode: 'AnyComponent',
        uid: 'ApparelDEAnyComponent',
        visible: true,
        richtext: '<strong>Any rich text here...</strong>'
    };
    NEW_COMPONENT_STRUCTURE = {
        attributes: [
            {
                cmsStructureType: 'ShortString',
                qualifier: 'name',
                i18nKey: 'type.anyComponentType.name.name'
            },
            {
                cmsStructureType: 'CustomReloadButton',
                qualifier: 'customReloadButton',
                i18nKey: 'type.anyComponentType.customreloadbutton.name'
            }
        ],
        category: 'TEST'
    };

    constructor(
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic,
        private systemEventService: SystemEventService
    ) {}
    /**
     * e2e test: call to this.setNewStructure and modify the name value
     */

    ngOnInit() {
        this.systemEventService.subscribe('load-structure', (eid, payload) =>
            this.onChangeStructureEvent(eid, payload)
        );
    }
    onUpdate = (data: Payload) => {
        let expectedData;
        if (data.hasOwnProperty('componentCustomField')) {
            expectedData = {
                type: 'anyComponentData',
                name: 'new component name',
                pk: '1234567890',
                typeCode: 'AnyComponent',
                uid: 'ApparelDEAnyComponent',
                visible: true,
                richtext: '',
                componentCustomField: 'custom value'
            };
        } else {
            expectedData = { ...this.NEW_CONTENT };
            expectedData.name = 'some new name';
        }
        this.yjQuery('.generic-editor-status').html(
            lo.isEqual(expectedData, data) ? 'PASSED' : 'FAILED'
        );
    };

    /**
     * SCENARIO: New structure with:
     * - qualifier:name, cmsStructureType:'ShortString'
     * - qualifier:richtext, cmsStructureType:'RichText'
     */
    setNewStructure() {
        this.content = this.content ? { ...this.content } : { ...this.NEW_CONTENT };

        this.structureApi = null; // The Generic Editor can't have both structure and structureApi
        this.structure = { ...this.NEW_STRUCTURE };
    }

    /**
     * SCENARIO: New structure api with:
     * - qualifier:headline, cmsStructureType:'ShortString'
     * - qualifier:active, cmsStructureType:'Boolean'
     * - qualifier:comments, cmsStructureType:'LongString'
     */
    setNewStructureApi() {
        this.content = {
            type: 'anyComponentData',
            headline: 'Any headline',
            active: true,
            comments: 'Any comments...',
            pk: '1234567890',
            typeCode: 'AnyComponent',
            uid: 'ApparelDEAnyComponent',
            visible: true
        };

        this.structure = null;
        this.structureApi = '/cmswebservices/v1/types/anotherComponent';
    }

    /**
     * SCENARIO: A new structure is set for a new component
     */
    setPOSTMode() {
        this.componentId = null;
        this.structureApi = null;
        this.structure = { ...this.NEW_COMPONENT_STRUCTURE };
    }

    /**
     * Upon reception of custom event, use the new payload.structure and payload.content to load the generic editor through attribute binding
     */
    onChangeStructureEvent(eventId: string, payload: any) {
        this.structureApi = null;
        this.structure = { ...payload.structure };
        this.content = payload.content;
    }
}
@SeEntryModule('Outerapp')
@NgModule({
    imports: [CommonModule, SeGenericEditorModule],
    declarations: [AppRootComponent, CustomReloadButtonComponent],
    entryComponents: [AppRootComponent, CustomReloadButtonComponent],
    providers: [
        moduleUtils.bootstrap(
            (editorFieldMappingService: EditorFieldMappingService) => {
                editorFieldMappingService.addFieldMapping('CustomReloadButton', null, null, {
                    component: CustomReloadButtonComponent
                });
            },
            [EditorFieldMappingService]
        )
    ]
})
export class Outerapp {}
