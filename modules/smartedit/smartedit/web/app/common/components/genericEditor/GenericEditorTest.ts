/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { BehaviorSubject } from 'rxjs';
import * as angular from 'angular';
import { Injector } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';
import { TranslateService } from '@ngx-translate/core';
import { ILanguage, LogService, RestServiceFactory } from '@smart/utils';

import {
    BackendErrorResponse,
    BackendValidationErrors,
    EditorFieldMappingService,
    GenericEditorFactoryService,
    GenericEditorField,
    GenericEditorSchema,
    GenericEditorStackService,
    GenericEditorStructure,
    IFetchDataHandler,
    IGenericEditorConstructor,
    IRestService,
    ISharedDataService,
    IUriContext,
    LanguageService,
    Payload,
    SeValidationMessageParser,
    SystemEventService
} from 'smarteditcommons';
import { jQueryHelper } from 'testhelpers';
import {
    GENERIC_EDITOR_LOADED_EVENT,
    GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT
} from '../../utils';
import { GenericEditorState } from './models';
import { GenericEditorFieldMessage, IGenericEditor } from './types';

describe('test GenericEditor class', () => {
    const CONTEXT_SITE_ID = 'CURRENT_CONTEXT_SITE_ID';
    const CONTEXT_CATALOG = 'CURRENT_CONTEXT_CATALOG';
    const CONTEXT_CATALOG_VERSION = 'CURRENT_CONTEXT_CATALOG_VERSION';

    let smarteditComponentType: string;
    let smarteditComponentId: string;
    let updateCallback: (pristine: Payload, results: Payload) => void;
    let GenericEditor: IGenericEditorConstructor;
    let sharedDataService: jasmine.SpyObj<ISharedDataService>;
    let languageService: jasmine.SpyObj<LanguageService>;
    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;
    let editorStructureService: jasmine.SpyObj<IRestService<any>>;
    let editorCRUDService: jasmine.SpyObj<IRestService<any>>;
    let editorMediaService: jasmine.SpyObj<IRestService<any>>;
    let fetchEnumDataHandler: jasmine.SpyObj<IFetchDataHandler>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let editorFieldMappingService: jasmine.SpyObj<EditorFieldMappingService>;
    let injector: jasmine.SpyObj<Injector>;
    let seValidationMessageParser: jasmine.SpyObj<SeValidationMessageParser>;
    let logService: jasmine.SpyObj<LogService>;
    let translate: jasmine.SpyObj<TranslateService>;
    let $injector: jasmine.SpyObj<angular.auto.IInjectorService>;
    let element: jasmine.SpyObj<HTMLElement>;
    let formElement: jasmine.SpyObj<HTMLFormElement>;
    let schema$: jasmine.SpyObj<BehaviorSubject<GenericEditorSchema>>;
    let data$: jasmine.SpyObj<BehaviorSubject<Payload>>;
    let form: jasmine.SpyObj<GenericEditorState>;
    const upgrade: UpgradeModule = {} as UpgradeModule;

    const URICONTEXT = {
        [CONTEXT_SITE_ID]: 'someSiteUid',
        [CONTEXT_CATALOG]: 'somecatalogId',
        [CONTEXT_CATALOG_VERSION]: 'someCatalogVersion'
    } as IUriContext;

    const options = [
        {
            code: 'code1',
            label: 'label1'
        },
        {
            code: 'code2',
            label: 'label2'
        }
    ];

    const STOREFRONT_LANGUAGES = [
        {
            language: 'en',
            isocode: 'en',
            required: true,
            active: true,
            name: 'en',
            nativeName: 'en'
        },
        {
            language: 'pl',
            isocode: 'en',
            required: true,
            active: true,
            name: 'pl',
            nativeName: 'pl'
        },
        {
            language: 'it',
            isocode: 'en',
            active: true,
            name: 'it',
            nativeName: 'it'
        }
    ] as ILanguage[];

    const STRUCTURE = {
        attributes: [{ qualifier: 'qualifier', cmsStructureType: 'cmsStructureType' }],
        category: 'SOME_CATEGORY'
    } as GenericEditorStructure;

    const setForm = (
        editor: IGenericEditor,
        formState: GenericEditorState,
        properties: {
            languages?: ILanguage[];
            tabs?: ILanguage[];
            fields?: any;
            parameters?: any;
            pristine?: any;
            component?: any;
        } = {}
    ) => {
        (editor.form as any) = formState;
        Object.assign(editor.form as any, {
            languages: properties.languages || [],
            tabs: properties.tabs || [],
            fields: properties.fields || [],
            parameters: properties.parameters || {},
            pristine: properties.pristine || {},
            component: properties.component || {}
        });
    };

    beforeEach(() => {
        schema$ = jasmine.createSpyObj<BehaviorSubject<GenericEditorSchema>>('schema$', [
            'next',
            'complete'
        ]);
        data$ = jasmine.createSpyObj<BehaviorSubject<Payload>>('data$', ['next', 'complete']);

        element = jasmine.createSpyObj<HTMLElement>('element', ['querySelector']);
        formElement = jasmine.createSpyObj<HTMLFormElement>('form', ['elements']);
        (formElement as any).elements = [];

        element.querySelector.and.callFake((selector: string) => {
            if (selector === 'form') {
                return formElement;
            }
            throw new Error(`unexpected selector ${selector} passed to element.querySelector`);
        });

        translate = jasmine.createSpyObj('translate', ['instant']);
        translate.instant.and.callFake((key: string) => key);
        logService = jasmine.createSpyObj('logService', ['log', 'warn']);

        systemEventService = jasmine.createSpyObj('systemEventService', [
            'subscribe',
            'publishAsync'
        ]);

        languageService = jasmine.createSpyObj('languageService', [
            'getLanguagesForSite',
            'getBrowserLocale'
        ]);
        languageService.getLanguagesForSite.and.callFake(() => {
            return Promise.resolve(STOREFRONT_LANGUAGES);
        });
        languageService.getBrowserLocale.and.returnValue('en_US');

        sharedDataService = jasmine.createSpyObj('sharedDataService', ['get']);

        sharedDataService.get.and.callFake(() => {
            return Promise.resolve({
                siteDescriptor: {
                    uid: URICONTEXT[CONTEXT_SITE_ID]
                },
                catalogDescriptor: {
                    catalogId: URICONTEXT[CONTEXT_CATALOG],
                    catalogVersion: URICONTEXT[CONTEXT_CATALOG_VERSION]
                }
            });
        });

        fetchEnumDataHandler = jasmine.createSpyObj('fetchEnumDataHandler', [
            'findByMask',
            'getById'
        ]);

        fetchEnumDataHandler.findByMask.and.callFake(() => {
            return Promise.resolve(options);
        });

        $injector = jasmine.createSpyObj<angular.auto.IInjectorService>('$injector', [
            'has',
            'get'
        ]);
        injector = jasmine.createSpyObj<Injector>('injector', ['get']);

        injector.get.and.callFake(() => {
            return fetchEnumDataHandler;
        });

        upgrade.injector = injector;
        upgrade.$injector = $injector;

        editorFieldMappingService = jasmine.createSpyObj('editorFieldMappingService', [
            'getEditorFieldMapping',
            '_registerDefaultFieldMappings',
            'getFieldTabMapping'
        ]);
        editorFieldMappingService.getEditorFieldMapping.and.callFake(function(type: any) {
            return {
                template: type + 'Template'
            };
        });

        editorFieldMappingService.getEditorFieldMapping.and.callFake(
            (): null => {
                return null;
            }
        );
        editorFieldMappingService.getFieldTabMapping.and.returnValue('default');

        smarteditComponentType = 'smarteditComponentType';
        smarteditComponentId = 'smarteditComponentId';
        updateCallback = () => {
            return;
        };

        editorStructureService = jasmine.createSpyObj('restService', [
            'getById',
            'get',
            'query',
            'page',
            'save',
            'update',
            'remove'
        ]);
        editorCRUDService = jasmine.createSpyObj('restService', [
            'getById',
            'get',
            'query',
            'page',
            'save',
            'update',
            'remove'
        ]);
        editorMediaService = jasmine.createSpyObj('restService', [
            'getById',
            'get',
            'query',
            'page',
            'save',
            'update',
            'remove'
        ]);

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.and.callFake(function(uri: any) {
            if (uri === '/cmswebservices/types/:smarteditComponentType') {
                return editorStructureService;
            } else if (uri === '/cmswebservices/cmsxdata/contentcatalog/staged/Media') {
                return editorMediaService;
            } else if (
                uri ===
                '/cmswebservices/catalogs/' +
                    CONTEXT_CATALOG +
                    '/versions/' +
                    CONTEXT_CATALOG_VERSION +
                    '/items'
            ) {
                return editorCRUDService;
            }
            return null;
        });

        form = jasmine.createSpyObj<GenericEditorState>('form', [
            'removeValidationMessages',
            'removeFrontEndValidationMessages',
            'validationMessageBelongsToCurrentInstance',
            'collectUnrelatedValidationMessages',
            'collectFrontEndValidationErrors',
            'displayValidationMessages',
            'isDirty',
            'fieldsAreUserChecked',
            'patchComponent',
            'sanitizedPayload',
            'switchToTabContainingQualifier',
            'group'
        ]);

        (form.group as any).control = { valid: true };

        form.collectUnrelatedValidationMessages.and.callFake(
            (messages: GenericEditorFieldMessage[]) => {
                return messages;
            }
        );

        seValidationMessageParser = jasmine.createSpyObj('seValidationMessageParser', ['parse']);

        GenericEditor = new GenericEditorFactoryService(
            jQueryHelper.jQuery(),
            restServiceFactory,
            languageService,
            sharedDataService,
            systemEventService,
            logService,
            upgrade,
            seValidationMessageParser,
            editorFieldMappingService
        ).getGenericEditorConstructor();
    });

    it('GenericEditor fails to initialize if neither structureApi nor structure are provided', () => {
        expect(() => {
            return new GenericEditor({
                smarteditComponentType,
                smarteditComponentId,
                element
            });
        }).toThrow(new Error('genericEditor.configuration.error.no.structure'));
    });

    it('GenericEditor fails to initialize if both structureApi and structure are provided', () => {
        expect(() => {
            return new GenericEditor({
                smarteditComponentType,
                smarteditComponentId,
                structureApi: '/cmswebservices/types/:smarteditComponentType',
                structure: STRUCTURE,
                element
            } as any);
        }).toThrow(new Error('genericEditor.configuration.error.2.structures'));
    });

    it('GenericEditor initializes fine with structure API', () => {
        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            contentApi:
                '/cmswebservices/catalogs/' +
                CONTEXT_CATALOG +
                '/versions/' +
                CONTEXT_CATALOG_VERSION +
                '/items',
            updateCallback,
            element
        });

        expect(editor.smarteditComponentType).toBe(smarteditComponentType);
        expect(editor.smarteditComponentId).toBe(smarteditComponentId);
        expect(editor.updateCallback).toBe(updateCallback);
        expect(editor.editorStructureService).toBe(editorStructureService);
        expect(editor.editorCRUDService).toBe(editorCRUDService);
    });

    it('GenericEditor initializes fine with structure', () => {
        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            contentApi:
                '/cmswebservices/catalogs/' +
                CONTEXT_CATALOG +
                '/versions/' +
                CONTEXT_CATALOG_VERSION +
                '/items',
            updateCallback
        } as any);

        expect(editor.smarteditComponentType).toBe(smarteditComponentType);
        expect(editor.smarteditComponentId).toBe(smarteditComponentId);
        expect(editor.updateCallback).toBe(updateCallback);
        expect(editor.editorStructureService).toBeUndefined();
        expect((editor as any).structure).toBe(STRUCTURE);
        expect(editor.editorCRUDService).toBe(editorCRUDService);
    });

    it('GenericEditor fetch executes get with identifier if identifier is set', (done) => {
        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            contentApi:
                '/cmswebservices/catalogs/' +
                CONTEXT_CATALOG +
                '/versions/' +
                CONTEXT_CATALOG_VERSION +
                '/items'
        } as any);

        editorCRUDService.getById.and.returnValue(Promise.resolve('somedata'));

        editor.fetch().then(
            function(value: any) {
                expect(editorCRUDService.getById).toHaveBeenCalledWith(smarteditComponentId);
                expect(value).toBe('somedata');
                done();
            },
            () => {
                (expect() as any).fail();
            }
        );
    });

    it('GenericEditor fetch executes return empty object if identifier is not set', (done) => {
        const editor = new GenericEditor({
            smarteditComponentType,
            structure: STRUCTURE,
            contentApi:
                '/cmswebservices/catalogs/' +
                CONTEXT_CATALOG +
                '/versions/' +
                CONTEXT_CATALOG_VERSION +
                '/items'
        } as any);

        editor.fetch().then(
            function(value: any) {
                expect(value).toEqual({});
                expect(editorCRUDService.get).not.toHaveBeenCalled();
                done();
            },
            () => {
                (expect() as any).fail();
            }
        );
    });

    it('calling reset() sets component to prior pristine state with and emits both schema and data', async () => {
        const pristine: Payload = {
            a: '1',
            b: '2',
            c: '3'
        };

        const component: Payload = {
            a: '2',
            b: '3'
        };
        const fields: any = [
            {
                field: 'field1'
            },
            {
                field: 'field2'
            }
        ];

        const editor = new GenericEditor({
            id: 'id',
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            contentApi:
                '/cmswebservices/catalogs/' +
                CONTEXT_CATALOG +
                '/versions/' +
                CONTEXT_CATALOG_VERSION +
                '/items'
        } as any);

        (editor as any).schema$ = schema$;
        (editor as any).data$ = data$;
        setForm(editor, form, { fields, pristine, component });

        await editor.reset();

        expect(form.removeValidationMessages).toHaveBeenCalledTimes(1);
        expect(schema$.next).toHaveBeenCalledWith({
            id: 'id',
            languages: STOREFRONT_LANGUAGES,
            structure: STRUCTURE,
            uriContext: URICONTEXT,
            smarteditComponentType,
            targetedQualifier: undefined
        });
        expect(data$.next).toHaveBeenCalledWith(pristine);
    });

    it('successful load will set pristine state and call reset', async () => {
        const data: any = {
            a: '1',
            b: '2',
            c: null,
            d: {
                en: 'something'
            },
            e: null
        };

        editorCRUDService.getById.and.returnValue(Promise.resolve(data));
        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            contentApi:
                '/cmswebservices/catalogs/' +
                CONTEXT_CATALOG +
                '/versions/' +
                CONTEXT_CATALOG_VERSION +
                '/items',
            element
        });

        spyOn(editor, 'reset').and.returnValue(null);

        await editor.load();

        expect(editorCRUDService.getById).toHaveBeenCalledWith('smarteditComponentId');

        expect(editor.reset).toHaveBeenCalledWith(data);
    });

    it('submit will do nothing if componentForm is not valid', (done) => {
        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            content: {},
            element
        });

        (editor.form as any) = form;
        setForm(editor, form);
        spyOn(editor, 'reset').and.returnValue(null);
        spyOn(editor, 'isDirty').and.returnValue(true);
        spyOn(editor, 'isValid').and.returnValue(false);

        editor.submit({}).then(
            () => {
                //
            },
            () => {
                // The errors should have been removed. This is necessary in case there was an associated error in a different tab.
                expect(form.removeValidationMessages).toHaveBeenCalled();

                expect(editorCRUDService.update).not.toHaveBeenCalled();
                expect(editor.reset).not.toHaveBeenCalled();
                done();
            }
        );
    });

    it('GIVEN generic editor with modified component WHEN onSuccess is called and the backend returns empty response THEN it returns original payload and calls updateCallback', async () => {
        // GIVEN
        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            content: {},
            updateCallback,
            element
        });

        const component = {
            a: '1',
            b: '4',
            c: '3'
        };

        spyOn(editor, 'updateCallback').and.returnValue(null);
        spyOn(editor, 'reset').and.returnValue(null);

        // WHEN
        (editor as any).onSuccess({ payload: component });

        // THEN
        expect(editor.updateCallback).toHaveBeenCalledWith(component, undefined);
    });

    it('submit will call update, removeValidationMessages, reset if dirty and form valid', async () => {
        editorCRUDService.update.and.returnValue(Promise.resolve(null)); // not listening to response anymore

        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            contentApi:
                '/cmswebservices/catalogs/' +
                CONTEXT_CATALOG +
                '/versions/' +
                CONTEXT_CATALOG_VERSION +
                '/items',
            element
        });

        const component = {
            a: '1',
            b: '4',
            c: '3'
        };

        const fields = [
            {
                qualifier: 'a'
            },
            {
                qualifier: 'b'
            },
            {
                qualifier: 'c'
            }
        ] as any;

        setForm(editor, form, { fields, component });
        form.isDirty.and.returnValue(true);
        form.sanitizedPayload.and.callFake((p: Payload) => p);
        form.fieldsAreUserChecked.and.returnValue(true);

        await editor.submit(component);

        expect(editorCRUDService.update).toHaveBeenCalledWith({
            a: '1',
            b: '4',
            c: '3',
            identifier: 'smarteditComponentId'
        });

        expect(form.removeValidationMessages).toHaveBeenCalled();
        expect(form.displayValidationMessages).toHaveBeenCalled();
    });

    it('successful init will call load, pushEditorToStack with given editorStackId, and publish loaded event', async () => {
        const editor = new GenericEditor({
            id: 'theId',
            editorStackId: 'theEditorStackId',
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            content: {},
            element
        });
        const component: any = {
            a: '1'
        };

        setForm(editor, form, { component });
        spyOn(editor as any, 'load').and.returnValue(Promise.resolve());

        await editor.init();

        expect(editor.load).toHaveBeenCalled();

        expect(systemEventService.publishAsync).toHaveBeenCalledTimes(2);
        expect(systemEventService.publishAsync).toHaveBeenCalledWith(
            GENERIC_EDITOR_LOADED_EVENT,
            'theId'
        );
        expect(systemEventService.publishAsync).toHaveBeenCalledWith(
            GenericEditorStackService.EDITOR_PUSH_TO_STACK_EVENT,
            {
                editorId: 'theId',
                editorStackId: 'theEditorStackId',
                component,
                componentType: smarteditComponentType
            }
        );
    });

    it('GIVEN there are errors in one or more fields in the current editor detected externally WHEN GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT handler is called THEN the editor must display those validation errors', () => {
        // Arrange
        const failure: BackendErrorResponse<BackendValidationErrors> = {
            error: {
                errors: [
                    {
                        message: 'This field cannot contain special characters',
                        reason: 'missing',
                        subject: 'headline',
                        subjectType: 'parameter',
                        type: 'ValidationError'
                    },
                    {
                        message:
                            'This field is required and must to be between 1 and 255 characters long.',
                        reason: 'missing',
                        subject: 'content',
                        subjectType: 'parameter',
                        type: 'ValidationError'
                    }
                ]
            }
        };

        const fields = [
            {
                qualifier: 'headline'
            }
        ] as GenericEditorField[];

        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            updateCallback,
            structure: STRUCTURE,
            contentApi:
                '/cmswebservices/catalogs/' +
                CONTEXT_CATALOG +
                '/versions/' +
                CONTEXT_CATALOG_VERSION +
                '/items'
        } as any);
        (editor as any).id = 'some ID';

        setForm(editor, form, { fields });
        form.isDirty.and.returnValue(false);

        // Act
        editor.reset();
        (editor as any)._handleUnrelatedValidationMessages('some Key', {
            messages: failure.error.errors
        });

        // Assert
        expect(editor.form.removeValidationMessages).toHaveBeenCalled();
        expect(editor.form.displayValidationMessages).toHaveBeenCalledWith(
            failure.error.errors,
            true
        );
    });

    it('successful init will call load, pushEditorToStack with given editor id as editorStackId, and publish loaded event', async () => {
        const editor = new GenericEditor({
            id: 'theId',
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            content: {},
            element
        });
        // editor.api.considerFormDirty();

        const component: any = {
            a: '1'
        };

        setForm(editor, form, { component });
        spyOn(editor as any, 'load').and.returnValue(Promise.resolve());

        await editor.init();

        expect(editor.load).toHaveBeenCalled();

        expect(systemEventService.publishAsync).toHaveBeenCalledTimes(2);
        expect(systemEventService.publishAsync).toHaveBeenCalledWith(
            GENERIC_EDITOR_LOADED_EVENT,
            'theId'
        );
        expect(systemEventService.publishAsync).toHaveBeenCalledWith(
            GenericEditorStackService.EDITOR_PUSH_TO_STACK_EVENT,
            {
                editorId: 'theId',
                editorStackId: 'theId',
                component,
                componentType: smarteditComponentType
            }
        );
    });

    it('GIVEN that cmsStructureType is "Enum", refreshOptions  will call fetchEnumDataHandler to fetch fetch full list of enums', async () => {
        const field: GenericEditorField = {
            qualifier: 'property1',
            cmsStructureType: 'Enum'
        };

        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            content: {},
            element
        });

        const component: Payload = {};
        setForm(editor, form, { component });

        await editor.refreshOptions(field, 'qualifier', 's');

        expect(fetchEnumDataHandler.getById).not.toHaveBeenCalled();
        expect(fetchEnumDataHandler.findByMask).toHaveBeenCalledWith(field, 's');
        expect(field.options).toEqual({
            property1: [
                {
                    code: 'code1',
                    label: 'label1'
                },
                {
                    code: 'code2',
                    label: 'label2'
                }
            ]
        });
        expect(field.initiated).toEqual(['property1']);
    });

    it('onFailure will remove existing validation errors and call _displayValidationMessages', () => {
        const failure: BackendErrorResponse<BackendValidationErrors> = {
            error: {
                errors: [
                    {
                        message: 'This field cannot contain special characters',
                        reason: 'missing',
                        subject: 'headline',
                        subjectType: 'parameter',
                        type: 'ValidationError'
                    },
                    {
                        message:
                            'This field is required and must to be between 1 and 255 characters long.',
                        reason: 'missing',
                        subject: 'content',
                        subjectType: 'parameter',
                        type: 'ValidationError'
                    }
                ]
            }
        };

        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            updateCallback,
            structure: STRUCTURE,
            content: {},
            element
        });

        const fields = [
            {
                qualifier: 'a',
                cmsStructureType: 'someType'
            },
            {
                qualifier: 'b',
                cmsStructureType: 'someType'
            }
        ] as GenericEditorField[];

        setForm(editor, form, { fields });
        (editor as any).onFailure(failure);

        expect(form.removeValidationMessages).toHaveBeenCalled();
        expect(form.displayValidationMessages).toHaveBeenCalledWith(failure.error.errors, true);
    });

    it('GIVEN there are errors caused by an external editor WHEN onFailure is called THEN the editor must raise a GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT ', () => {
        // Arrange
        const failure: BackendErrorResponse<BackendValidationErrors> = {
            error: {
                errors: [
                    {
                        message: 'This field cannot contain special characters',
                        reason: 'missing',
                        subject: 'headline',
                        subjectType: 'parameter',
                        type: 'ValidationError'
                    },
                    {
                        message:
                            'This field is required and must to be between 1 and 255 characters long.',
                        reason: 'missing',
                        subject: 'content',
                        subjectType: 'parameter',
                        type: 'ValidationError'
                    }
                ]
            }
        };

        const refreshedData = {
            a: '1',
            b: '2',
            c: '5'
        };

        const editor = new GenericEditor({
            id: 'someId',
            smarteditComponentType,
            smarteditComponentId,
            updateCallback,
            structure: STRUCTURE,
            content: {},
            element
        });

        setForm(editor, form);
        spyOn(editor, 'fetch').and.returnValue(Promise.resolve(refreshedData));

        // Act
        (editor as any).onFailure(failure);

        // Assert
        expect(systemEventService.publishAsync).toHaveBeenCalledWith(
            GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
            {
                messages: failure.error.errors,
                sourceGenericEditorId: 'someId'
            }
        );
    });

    it('submit WILL fail validation WHEN submit a not checked required checkbox field', () => {
        const fields: GenericEditorField[] = [
            {
                qualifier: 'content',
                cmsStructureType: 'Paragraph',
                requiresUserCheck: {
                    content: true
                },
                isUserChecked: false
            }
        ];
        const componentType = 'simpleResponsiveBannerComponent';
        const editor = new GenericEditor({
            smarteditComponentType: componentType,
            smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            content: {},
            element
        });

        setForm(editor, form, { fields });
        form.fieldsAreUserChecked.and.returnValue(false);

        editor.submit({});

        expect(editor.hasFrontEndValidationErrors).toEqual(true);
    });

    it('submit WILL pass validation WHEN submit a checked required checkbox field', () => {
        const fields: GenericEditorField[] = [
            {
                qualifier: 'content',
                cmsStructureType: 'Paragraph',
                requiresUserCheck: {
                    content: true
                },
                isUserChecked: true
            }
        ];
        const componentType = 'simpleResponsiveBannerComponent';
        const editor = new GenericEditor({
            smarteditComponentType: componentType,
            smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            content: {},
            element
        });
        setForm(editor, form, { fields });

        form.fieldsAreUserChecked.and.returnValue(true);
        editor.submit({});
        expect(editor.hasFrontEndValidationErrors).toEqual(false);
    });

    it('_convertStructureArray will properly convert the structures to a format that the GE can understand', () => {
        const fields: GenericEditorField[] = [
            {
                qualifier: 'content',
                cmsStructureType: 'Paragraph',
                requiresUserCheck: {
                    content: true
                },
                isUserChecked: true
            }
        ];

        const structures: { structures: GenericEditorStructure[] } = {
            structures: [
                {
                    attributes: fields,
                    category: 'CATEGORY'
                }
            ]
        };

        const componentTypes: { componentTypes: GenericEditorStructure[] } = {
            componentTypes: [
                {
                    attributes: fields,
                    category: 'CATEGORY'
                }
            ]
        };

        const componentType = 'simpleResponsiveBannerComponent';
        const editor = new GenericEditor({
            smarteditComponentType: componentType,
            smarteditComponentId,
            structureApi: '/cmswebservices/types/:smarteditComponentType',
            content: {},
            element
        });

        let structure = (editor as any)._convertStructureArray(structures);
        expect(structure.attributes).toEqual(fields);

        structure = (editor as any)._convertStructureArray(componentTypes);
        expect(structure.attributes).toEqual(fields);
    });

    it('WHEN an editor is finalized THEN it properly cleans up', () => {
        // GIVEN
        const _unregisterUnrelatedMessagesEvent = jasmine.createSpy(
            '_unregisterUnrelatedMessagesEvent'
        );
        systemEventService.subscribe.and.callFake(function(eventId: any) {
            if (eventId === GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT) {
                return _unregisterUnrelatedMessagesEvent;
            }
            return null;
        });

        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            updateCallback,
            structure: STRUCTURE,
            content: {}
        } as any);

        spyOn(editor, 'popEditorFromStack').and.callThrough();

        // WHEN
        editor._finalize();

        // THEN
        expect((editor as any)._unregisterUnrelatedMessagesEvent).toHaveBeenCalled();
        expect(editor.popEditorFromStack).toHaveBeenCalled();
        expect(systemEventService.publishAsync).not.toHaveBeenCalled();
    });

    it('WHEN popEditorFromStack is called THEN it sends the right event', () => {
        // GIVEN
        const STACK_ID = 'some stack id';
        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            updateCallback,
            editorStackId: STACK_ID,
            structure: STRUCTURE,
            content: {},
            element
        });

        // WHEN
        editor.popEditorFromStack();

        // THEN
        expect(systemEventService.publishAsync).toHaveBeenCalledWith(
            GenericEditorStackService.EDITOR_POP_FROM_STACK_EVENT,
            {
                editorStackId: STACK_ID
            }
        );
    });

    // checked until here
    it('GIVEN generic editor WHEN addContentChangeEvent is called THEN new callback function is added to the list of callbacks', () => {
        // GIVEN
        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            content: {},
            element
        });
        setForm(editor, form);

        editor.init();

        // WHEN
        const fn = (): any => {
            return null;
        };
        editor.api.addContentChangeEvent(fn);

        // THEN
        const events = (editor as any).onChangeEvents;
        expect(events).toEqual([fn]);
    });

    it('GIVEN generic editor AND content change callback is added WHEN unregister is called THEN the callback is removed from the list of callbacks', () => {
        // GIVEN
        const editor = new GenericEditor({
            smarteditComponentType,
            smarteditComponentId,
            structure: STRUCTURE,
            content: {},
            element
        });

        setForm(editor, form);

        editor.init();
        const fn = (): any => {
            return null;
        };
        const events = (editor as any).onChangeEvents;
        const unregister = editor.api.addContentChangeEvent(fn);
        expect(events).toEqual([fn]);

        // WHEN
        unregister();

        // THEN
        expect(events).toEqual([]);
    });
});
