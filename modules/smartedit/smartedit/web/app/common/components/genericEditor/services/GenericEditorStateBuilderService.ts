/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ILanguage, Payload, SchemaCompilerService } from '@smart/utils';

import {
    objectUtils,
    CONTEXT_CATALOG,
    CONTEXT_CATALOG_VERSION,
    CONTEXT_SITE_ID
} from '../../../utils';

import {
    GenericEditorAttribute,
    GenericEditorField,
    GenericEditorFieldsMap,
    GenericEditorSchema,
    GenericEditorTab
} from '../types';

import { EditorFieldMappingService } from './EditorFieldMappingService';
import { GenericEditorTabService } from './GenericEditorTabService';
import { GenericEditorTabComponent } from '../components/GenericEditorTabComponent';

import { proxifyDataObject, GenericEditorState } from '../models';
import { createRootMapper, RootSchemaDataMapper } from '../mappers';

/**
 * @internal
 * GenericEditorStateBuilderService generates a GenericEditorState.
 */
@Injectable()
export class GenericEditorStateBuilderService {
    constructor(
        /** @internal */
        private editorFieldMappingService: EditorFieldMappingService,
        /** @internal */
        private genericEditorTabService: GenericEditorTabService,
        /** @internal */
        private translateService: TranslateService,
        /** @internal */
        private schemaCompiler: SchemaCompilerService
    ) {}

    /**
     * Compiles a GenericEditorState from schema and data. Whenever a new state
     * is provided the entire form is recompiled.
     */
    buildState(data: Payload, schema: GenericEditorSchema): GenericEditorState {
        const fields = this._fieldAdaptor(schema);

        const tabs: GenericEditorTab[] = [];
        const fieldsMap: GenericEditorFieldsMap = fields.reduce(
            (seed: GenericEditorFieldsMap, field: GenericEditorField) => {
                let tab = this.editorFieldMappingService.getFieldTabMapping(
                    field,
                    schema.structure
                );
                if (!tab) {
                    tab = this.genericEditorTabService.getComponentTypeDefaultTab(schema.structure);
                }

                if (!seed[tab]) {
                    seed[tab] = [];

                    tabs.push({
                        id: tab,
                        title: 'se.genericeditor.tab.' + tab + '.title',
                        component: GenericEditorTabComponent
                    });
                }

                seed[tab].push(field);
                return seed;
            },
            {}
        );

        this.genericEditorTabService.sortTabs(tabs);

        // for setting uri params into custom widgets
        const parameters = {
            siteId: schema.uriContext[CONTEXT_SITE_ID],
            catalogId: schema.uriContext[CONTEXT_CATALOG],
            catalogVersion: schema.uriContext[CONTEXT_CATALOG_VERSION]
        };

        const component: any = objectUtils.copy(data);
        const proxied = proxifyDataObject(component);

        const mapper = this._createMapper(fieldsMap, proxied, schema.languages, tabs);
        const form = this.schemaCompiler.compileGroup(mapper.toValue(), mapper.toSchema());

        // Create central state handler.
        const state = new GenericEditorState(
            schema.id,
            form,
            component,
            proxied,
            objectUtils.copy(data), // Pristine form.
            tabs,
            fields,
            schema.languages,
            parameters
        );

        state.switchToTabContainingQualifier(schema.targetedQualifier);
        return state;
    }

    /** @internal */
    private _createMapper(
        fieldsMap: GenericEditorFieldsMap,
        component: Payload,
        languages: ILanguage[],
        tabs: GenericEditorTab[]
    ): RootSchemaDataMapper {
        return createRootMapper(fieldsMap, component, languages, tabs);
    }

    /** @internal */
    private _fieldAdaptor(schema: GenericEditorSchema): GenericEditorField[] {
        const structure = schema.structure;

        return structure.attributes.map((field: GenericEditorAttribute) => {
            const fieldMapping = this.editorFieldMappingService.getEditorFieldMapping(
                field,
                structure
            );
            const genericField: GenericEditorField = Object.assign(field, fieldMapping);

            if (genericField.editable === undefined) {
                genericField.editable = true;
            }

            if (!genericField.postfixText) {
                const key =
                    (structure.type ? structure.type.toLowerCase() : '') +
                    '.' +
                    field.qualifier.toLowerCase() +
                    '.postfix.text';

                const translated = this.translateService.instant(key);
                genericField.postfixText = translated !== key ? translated : '';
            }

            genericField.smarteditComponentType = schema.smarteditComponentType;
            return genericField;
        });
    }
}
