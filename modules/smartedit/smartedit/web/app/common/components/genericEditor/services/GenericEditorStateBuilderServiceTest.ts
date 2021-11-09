/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TranslateService } from '@ngx-translate/core';
import { ILanguage, SchemaCompilerService } from '@smart/utils';
import { Payload } from '../../../dtos';
import { CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, CONTEXT_SITE_ID } from '../../../utils';
import { EditorFieldMappingService } from './EditorFieldMappingService';
import { GenericEditorTabService } from './GenericEditorTabService';
import { GenericEditorStateBuilderService } from './GenericEditorStateBuilderService';
import {
    GenericEditorField,
    GenericEditorFieldsMap,
    GenericEditorSchema,
    GenericEditorStructure,
    GenericEditorTab
} from '../types';
import { IUriContext } from '../../../services';
import { RootSchemaDataMapper } from '../mappers';

describe('SchemaBuilderService', () => {
    let editorFieldMappingService: jasmine.SpyObj<EditorFieldMappingService>;
    let genericEditorTabService: jasmine.SpyObj<GenericEditorTabService>;
    let translateService: jasmine.SpyObj<TranslateService>;
    let schameComplierService: jasmine.SpyObj<SchemaCompilerService>;
    let mapper: jasmine.SpyObj<RootSchemaDataMapper>;

    let schemaBuilderService: GenericEditorStateBuilderService;
    let component: Payload;

    const uriContext: IUriContext = {
        [CONTEXT_SITE_ID]: 'theSiteId',
        [CONTEXT_CATALOG]: 'theCatalog',
        [CONTEXT_CATALOG_VERSION]: 'theCatalogVersion'
    };

    const languages: ILanguage[] = [
        {
            isocode: 'fr',
            name: 'fr',
            nativeName: 'fr',
            active: true,
            required: true
        }
    ];

    beforeEach(() => {
        component = { a: '1', b: 2 };

        editorFieldMappingService = jasmine.createSpyObj<EditorFieldMappingService>(
            'editorFieldMappingService',
            ['getEditorFieldMapping', 'getFieldTabMapping']
        );
        genericEditorTabService = jasmine.createSpyObj<GenericEditorTabService>(
            'genericEditorTabService',
            ['getComponentTypeDefaultTab', 'sortTabs']
        );
        genericEditorTabService.getComponentTypeDefaultTab.and.returnValue('default');

        translateService = jasmine.createSpyObj<TranslateService>('translateService', ['instant']);
        schameComplierService = jasmine.createSpyObj('schameComplierService', ['compileGroup']);

        schemaBuilderService = new GenericEditorStateBuilderService(
            editorFieldMappingService,
            genericEditorTabService,
            translateService,
            schameComplierService
        );

        mapper = jasmine.createSpyObj('mapper', ['toValue', 'toSchema']);
    });

    it('will assign postfix text when a field qualifier defines a property', () => {
        const schema: GenericEditorSchema = {
            id: 'id',
            languages,
            structure: {
                attributes: [
                    {
                        qualifier: 'media',
                        cmsStructureType: 'MediaContainer'
                    }
                ],
                category: 'CATEGORY',
                type: 'theType'
            },
            smarteditComponentType: 'theSmarteditComponentType',
            uriContext
        };

        const result = 'field can not be editable';
        translateService.instant.and.callFake((key: string) => {
            if (key === 'thetype.media.postfix.text') {
                return result;
            }
            throw new Error(`unexpected key ${key} passed to translateService.instant`);
        });

        const form = schemaBuilderService.buildState(component, schema);

        expect(form.fields[0].postfixText).toBe(result);
        expect(form.fields[0].smarteditComponentType).toBe('theSmarteditComponentType');
    });

    it("won't assign postfix text when a field qualifier does not define a property  ", () => {
        const schema: GenericEditorSchema = {
            id: 'id',
            languages,
            structure: {
                attributes: [
                    {
                        qualifier: 'media',
                        cmsStructureType: 'MediaContainer'
                    }
                ],
                category: 'CATEGORY',
                type: 'theType'
            },
            smarteditComponentType: 'theSmarteditComponentType',
            uriContext
        };

        translateService.instant.and.callFake((key: string) => {
            if (key === 'thetype.media.postfix.text') {
                return key;
            }
            throw new Error(`unexpected key ${key} passed to translateService.instant`);
        });

        spyOn(schemaBuilderService as any, '_createMapper').and.callFake(() => {
            return mapper;
        });
        const form = schemaBuilderService.buildState(component, schema);

        expect(form.fields[0].postfixText).toBe('');
        expect(form.fields[0].smarteditComponentType).toBe('theSmarteditComponentType');
    });

    it('compileGroup will distribute the fields in fieldsMap according to their tab mapping and will sort the tabs', () => {
        const schema: GenericEditorSchema = {
            id: 'id',
            languages,
            structure: {
                attributes: [
                    {
                        qualifier: 'a',
                        cmsStructureType: 'aType'
                    },
                    {
                        qualifier: 'b',
                        cmsStructureType: 'bType'
                    },
                    {
                        qualifier: 'c',
                        cmsStructureType: 'cType'
                    },
                    {
                        qualifier: 'd',
                        cmsStructureType: 'dType'
                    }
                ],
                category: 'CATEGORY',
                type: 'theType'
            },
            smarteditComponentType: 'theSmarteditComponentType',
            uriContext
        };

        editorFieldMappingService.getEditorFieldMapping.and.callFake(
            (field: GenericEditorField, structure: GenericEditorStructure) => {
                switch (field.qualifier) {
                    case 'a':
                        return null;
                    case 'b':
                        return { hidePrefixLabel: false };
                    case 'c':
                        return { hidePrefixLabel: true };
                    case 'd':
                        return undefined;
                    default:
                        throw new Error(
                            `unexpected qualifier ${
                                field.qualifier
                            } passed to editorFieldMappingService.getEditorFieldMapping`
                        );
                }
            }
        );

        editorFieldMappingService.getFieldTabMapping.and.callFake(
            (field: GenericEditorField, structure: GenericEditorStructure) => {
                switch (field.qualifier) {
                    case 'a':
                        return 'default';
                    case 'b':
                        return 'tab1';
                    case 'c':
                        return undefined;
                    case 'd':
                        return 'tab2';
                    default:
                        throw new Error(
                            `unexpected qualifier ${
                                field.qualifier
                            } passed to editorFieldMappingService.getFieldTabMapping`
                        );
                }
            }
        );

        // sort descending
        genericEditorTabService.sortTabs.and.callFake((tabs: GenericEditorTab[]) => {
            tabs.sort((tab1: GenericEditorTab, tab2: GenericEditorTab) => {
                return tab2.id < tab1.id ? -1 : 1;
            });
        });

        spyOn(schemaBuilderService as any, '_createMapper').and.returnValue(mapper);
        const form = schemaBuilderService.buildState(component, schema);

        expect(form.languages).toEqual(languages);
        expect(form.component).toEqual(component);
        expect(form.parameters).toEqual({
            siteId: 'theSiteId',
            catalogId: 'theCatalog',
            catalogVersion: 'theCatalogVersion'
        });
        expect(form.fields).toEqual([
            {
                qualifier: 'a',
                cmsStructureType: 'aType',
                editable: true,
                postfixText: undefined,
                smarteditComponentType: 'theSmarteditComponentType'
            },
            {
                qualifier: 'b',
                cmsStructureType: 'bType',
                hidePrefixLabel: false,
                editable: true,
                postfixText: undefined,
                smarteditComponentType: 'theSmarteditComponentType'
            },
            {
                qualifier: 'c',
                cmsStructureType: 'cType',
                hidePrefixLabel: true,
                editable: true,
                postfixText: undefined,
                smarteditComponentType: 'theSmarteditComponentType'
            },
            {
                qualifier: 'd',
                cmsStructureType: 'dType',
                editable: true,
                postfixText: undefined,
                smarteditComponentType: 'theSmarteditComponentType'
            }
        ] as GenericEditorField[]);

        expect((schemaBuilderService as any)._createMapper.calls.argsFor(0)[0]).toEqual({
            default: [
                {
                    qualifier: 'a',
                    cmsStructureType: 'aType',
                    editable: true,
                    postfixText: undefined,
                    smarteditComponentType: 'theSmarteditComponentType'
                },
                {
                    qualifier: 'c',
                    cmsStructureType: 'cType',
                    hidePrefixLabel: true,
                    editable: true,
                    postfixText: undefined,
                    smarteditComponentType: 'theSmarteditComponentType'
                }
            ],
            tab1: [
                {
                    qualifier: 'b',
                    cmsStructureType: 'bType',
                    hidePrefixLabel: false,
                    editable: true,
                    postfixText: undefined,
                    smarteditComponentType: 'theSmarteditComponentType'
                }
            ],
            tab2: [
                {
                    qualifier: 'd',
                    cmsStructureType: 'dType',
                    editable: true,
                    postfixText: undefined,
                    smarteditComponentType: 'theSmarteditComponentType'
                }
            ]
        } as GenericEditorFieldsMap);
    });
});
