/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';
import * as lodash from 'lodash';
import { from, BehaviorSubject, Observable } from 'rxjs';
import { LogService, Payload, RestServiceFactory, TypedMap } from '@smart/utils';

import { ILanguage, ISharedDataService, IUriContext } from '../../services';
import {
    BackendErrorResponse,
    BackendValidationErrors,
    GenericEditorField,
    GenericEditorOnSubmitResponse,
    GenericEditorSchema,
    GenericEditorStructure,
    IGenericEditor,
    IGenericEditorConstructor,
    IGenericEditorFactoryOptions
} from './types';
import { EditorFieldMappingService } from './services/EditorFieldMappingService';
import { FetchEnumDataHandler } from './services/FetchEnumDataHandler';
import { IFetchDataHandler } from './services/IFetchDataHandler';
import { GenericEditorStackService } from './services/GenericEditorStackService';
import { SeValidationMessageParser } from './services/SeValidationMessageParser';
import { SeDowngradeService } from '../../di';
import { LanguageService } from '../../services/language/LanguageService';
import { SystemEventService } from '../../services/SystemEventService';
import { YJQUERY_TOKEN } from '../../services/vendors/YjqueryModule';
import {
    objectUtils,
    stringUtils,
    CONTEXT_CATALOG,
    CONTEXT_CATALOG_VERSION,
    CONTEXT_SITE_ID,
    GENERIC_EDITOR_LOADED_EVENT,
    GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT
} from '../../utils';
import { GenericEditorState } from './models';
import { createApi } from './GenericEditorAPI';

// tslint:disable:max-classes-per-file

/**
 * @ngdoc service
 * @name GenericEditorModule.service:GenericEditorFactoryService
 * @description
 * The Generic Editor is a class that makes it possible for SmartEdit users (CMS managers, editors, etc.) to edit components in the SmartEdit interface.
 * The Generic Editor class is used by the {@link GenericEditorModule.component:GenericEditorComponent GenericEditorComponent} component.
 * The genericEditor directive makes a call either to a Structure API or, if the Structure API is not available, it reads the data from a local structure to request the information that it needs to build an HTML form.
 * It then requests the component by its type and ID from the Content API. The genericEditor directive populates the form with the data that is has received.
 * The form can now be used to edit the component. The modified data is saved using the Content API if it is provided else it would return the form data itself.
 * <br/><br/>
 * <strong>The structure and the REST structure API</strong>.
 * <br/>
 * The constructor of the {@link GenericEditorModule.service:GenericEditorFactoryService GenericEditorFactoryService} must be provided with the pattern of a REST Structure API, which must contain the string  ":smarteditComponentType", or with a local data structure.
 * If the pattern, Structure API, or the local structure is not provided, the Generic Editor will fail. If the Structure API is used, it must return a JSON payload that holds an array within the attributes property.
 * If the actual structure is used, it must return an array. Each entry in the array provides details about a component property to be displayed and edited. The following details are provided for each property:
 *
 * <ul>
 * <li><strong>qualifier:</strong> Name of the property.
 * <li><strong>i18nKey:</strong> Key of the property label to be translated into the requested language.
 * <li><strong>editable:</strong> Boolean that indicates if a property is editable or not. The default value is true.
 * <li><strong>localized:</strong> Boolean that indicates if a property is localized or not. The default value is false.
 * <li><strong>required:</strong> Boolean that indicates if a property is mandatory or not. The default value is false.
 * <li><strong>cmsStructureType:</strong> Value that is used to determine which form widget (property editor) to display for a specified property.
 * The selection is based on an extensible strategy mechanism owned by {@link GenericEditorModule.service:EditorFieldMappingService EditorFieldMappingService}.
 * <li><strong>cmsStructureEnumType:</strong> The qualified name of the Enum class when cmsStructureType is "Enum"
 * </li>
 * <ul><br/>
 *
 * <b>Note:</b><br/>
 * The generic editor has a tabset within. This allows it to display complex types in an organized and clear way. By default, all fields are stored
 * in the default tab, and if there is only one tab the header is hidden. The selection and configuration of where each field resides is
 * controlled by the  {@link GenericEditorModule.service:EditorFieldMappingService EditorFieldMappingService}. Similarly, the rendering
 * of tabs can be customized with the {@link GenericEditorModule.service:GenericEditorTabService GenericEditorTabService}.
 * <br />
 * <br />
 *
 * There are two options when you use the Structure API. The first option is to use an API resource that returns the structure object.
 * The following is an example of the JSON payload that is returned by the Structure API in this case:
 * <pre>
 * {
 *     attributes: [{
 *         cmsStructureType: "ShortString",
 *         qualifier: "someQualifier1",
 *         i18nKey: 'i18nkeyForsomeQualifier1',
 *         localized: false
 *     }, {
 *         cmsStructureType: "LongString",
 *         qualifier: "someQualifier2",
 *         i18nKey: 'i18nkeyForsomeQualifier2',
 *         localized: false
 *    }, {
 *         cmsStructureType: "RichText",
 *         qualifier: "someQualifier3",
 *         i18nKey: 'i18nkeyForsomeQualifier3',
 *         localized: true,
 *         required: true
 *     }, {
 *         cmsStructureType: "Boolean",
 *         qualifier: "someQualifier4",
 *         i18nKey: 'i18nkeyForsomeQualifier4',
 *         localized: false
 *     }, {
 *         cmsStructureType: "DateTime",
 *         qualifier: "someQualifier5",
 *         i18nKey: 'i18nkeyForsomeQualifier5',
 *         localized: false
 *     }, {
 *         cmsStructureType: "Media",
 *         qualifier: "someQualifier6",
 *         i18nKey: 'i18nkeyForsomeQualifier6',
 *         localized: true,
 *         required: true
 *     }, {
 *         cmsStructureType: "Enum",
 *         cmsStructureEnumType:'de.mypackage.Orientation'
 *         qualifier: "someQualifier7",
 *         i18nKey: 'i18nkeyForsomeQualifier7',
 *         localized: true,
 *         required: true
 *     }]
 * }
 * </pre><br/>
 * The second option is to use an API resource that returns a list of structures. In this case, the generic editor will select the first element from the list and use it to display its attributes.
 * The generic editor expects the structures to be in one of the two fields below.
 * <pre>
 * {
 *     structures: [{}, {}]
 * }
 *
 * or
 *
 * {
 *     componentTypes: [{}, {}]
 * }
 * </pre>
 * If the list has more than one element, the Generic Editor will throw an exception, otherwise it will get the first element on the list.
 * The following is an example of the JSON payload that is returned by the Structure API in this case:
 * <pre>
 * {
 *     structures: [
 *         {
 *             attributes: [{
 *                 		cmsStructureType: "ShortString",
 *                 		qualifier: "someQualifier1",
 *                 		i18nKey: 'i18nkeyForsomeQualifier1',
 *                 		localized: false
 *             		}, {
 *                 		cmsStructureType: "LongString",
 *                 		qualifier: "someQualifier2",
 *                 		i18nKey: 'i18nkeyForsomeQualifier2',
 *                 		localized: false
 *         	   		}]
 *         }
 *     ]
 * }
 * </pre>
 * <pre>
 * {
 *     componentTypes: [
 *         {
 *             attributes: [{
 *                 		cmsStructureType: "ShortString",
 *                 		qualifier: "someQualifier1",
 *                 		i18nKey: 'i18nkeyForsomeQualifier1',
 *                 		localized: false
 *             		}, {
 *                 		cmsStructureType: "LongString",
 *                 		qualifier: "someQualifier2",
 *                 		i18nKey: 'i18nkeyForsomeQualifier2',
 *                 		localized: false
 *         	   		}]
 *         }
 *     ]
 * }
 * </pre>
 * The following is an example of the expected format of a structure:
 * <pre>
 *    [{
 *         cmsStructureType: "ShortString",
 *         qualifier: "someQualifier1",
 *         i18nKey: 'i18nkeyForsomeQualifier1',
 *         localized: false
 *     }, {
 *         cmsStructureType: "LongString",
 *         qualifier: "someQualifier2",
 *         i18nKey: 'i18nkeyForsomeQualifier2',
 *         editable: false,
 *         localized: false
 *    }, {
 *         cmsStructureType: "RichText",
 *         qualifier: "someQualifier3",
 *         i18nKey: 'i18nkeyForsomeQualifier3',
 *         localized: true,
 *         required: true
 *     }, {
 *         cmsStructureType: "Boolean",
 *         qualifier: "someQualifier4",
 *         i18nKey: 'i18nkeyForsomeQualifier4',
 *         localized: false
 *     }, {
 *         cmsStructureType: "DateTime",
 *         qualifier: "someQualifier5",
 *         i18nKey: 'i18nkeyForsomeQualifier5',
 *         editable: false,
 *         localized: false
 *     }, {
 *         cmsStructureType: "Media",
 *         qualifier: "someQualifier6",
 *         i18nKey: 'i18nkeyForsomeQualifier6',
 *         localized: true,
 *         required: true
 *     }, {
 *         cmsStructureType: "Enum",
 *         cmsStructureEnumType:'de.mypackage.Orientation'
 *         qualifier: "someQualifier7",
 *         i18nKey: 'i18nkeyForsomeQualifier7',
 *         localized: true,
 *         required: true
 *     }]
 * </pre>
 *
 * <strong>The REST CRUD API</strong>, is given to the constructor of {@link GenericEditorModule.service:GenericEditorFactoryService GenericEditorFactoryService}.
 * The CRUD API must support GET and PUT of JSON payloads.
 * The PUT method must return the updated payload in its response. Specific to the GET and PUT, the payload must fulfill the following requirements:
 * <ul>
 * 	<li>DateTime types: Must be serialized as long timestamps.</li>
 * 	<li>Media types: Must be serialized as identifier strings.</li>
 * 	<li>If a cmsStructureType is localized, then we expect that the CRUD API returns a map containing the type (string or map) and the map of values, where the key is the language and the value is the content that the type returns.</li>
 * </ul>
 *
 * The following is an example of a localized payload:
 * <pre>
 * {
 *    content: {
 * 		'en': 'content in english',
 * 		'fr': 'content in french',
 * 		'hi': 'content in hindi'
 * 	  }
 * }
 * </pre>
 *
 * <br/><br/>
 *
 * If a validation warning or error occurs, the PUT method of the REST CRUD API will return a validation warning/error object that contains an array of validation messages. The information returned for each validation message is as follows:
 * <ul>
 * 	<li><strong>subject:</strong> The qualifier that has the error</li>
 * 	<li><strong>message:</strong> The error message to be displayed</li>
 * 	<li><strong>type:</strong> The type of message returned. This is of the type ValidationError or Warning.</li>
 * 	<li><strong>language:</strong> The language the error needs to be associated with. If no language property is provided, a match with regular expression /(Language: \[)[a-z]{2}\]/g is attempted from the message property. As a fallback, it implies that the field is not localized.</li>
 * </ul>
 *
 * The following code is an example of an error response object:
 * <pre>
 * {
 *    errors: [{
 *        subject: 'qualifier1',
 *        message: 'error message for qualifier',
 *        type: 'ValidationError'
 *    }, {
 *        subject: 'qualifier2',
 *        message: 'error message for qualifier2 language: [fr]',
 *        type: 'ValidationError'
 *    }, {
 *        subject: 'qualifier3',
 *        message: 'error message for qualifier2',
 *        type: 'ValidationError'
 *    }, {
 *        subject: 'qualifier4',
 *        message: 'warning message for qualifier4',
 *        type: 'Warning'
 *    }]
 * }
 *
 * </pre>
 *
 * Whenever any sort of dropdown is used in one of the cmsStructureType widgets, it is advised using {@link GenericEditorModule.service:GenericEditorFactoryService#methods_refreshOptions refreshOptions method}. See this method documentation to learn more.
 *
 */

/* @internal */
@SeDowngradeService()
export class GenericEditorFactoryService {
    genericEditorConstructor: IGenericEditorConstructor;

    constructor(
        @Inject(YJQUERY_TOKEN) yjQuery: JQueryStatic,
        restServiceFactory: RestServiceFactory,
        languageService: LanguageService,
        sharedDataService: ISharedDataService,
        systemEventService: SystemEventService,
        logService: LogService,
        upgrade: UpgradeModule,
        seValidationMessageParser: SeValidationMessageParser,
        editorFieldMappingService: EditorFieldMappingService
    ) {
        editorFieldMappingService._registerDefaultFieldMappings();

        this.genericEditorConstructor = class implements IGenericEditor {
            get pristine(): Payload {
                return this.form ? this.form.pristine : undefined;
            }

            private get nativeForm(): HTMLFormElement {
                return this.element.querySelector('form');
            }

            public form: GenericEditorState;
            public schema$ = new BehaviorSubject<GenericEditorSchema>(null);
            public data$ = new BehaviorSubject<Payload>(null);
            public hasFrontEndValidationErrors: boolean;
            public submitButtonText = 'se.componentform.actions.submit';
            public cancelButtonText = 'se.componentform.actions.cancel';

            public alwaysShowSubmit: boolean;
            public alwaysShowReset: boolean;
            public onReset: () => void;
            public parameters: IUriContext;
            public id: string;
            public inProgress: boolean;
            public smarteditComponentType: string;
            public smarteditComponentId: string;
            public editorStackId: string;
            public updateCallback: (pristine: Payload, results: Payload) => void;
            public structure: GenericEditorStructure;
            public uriContext: Promise<IUriContext>;
            public editorStructureService: any;
            public editorCRUDService: any;
            public initialContent: Payload;
            public initialDirty: boolean;
            public targetedQualifier: string;
            public onChangeEvents: any[];
            public element: HTMLElement;
            public api = createApi(this);
            private _unregisterUnrelatedMessagesEvent: () => void;

            constructor(conf: IGenericEditorFactoryOptions) {
                this._validate(conf);
                this.id = conf.id;
                this.inProgress = false;
                this.smarteditComponentType = conf.smarteditComponentType;
                this.smarteditComponentId = conf.smarteditComponentId;
                this.editorStackId = conf.editorStackId || this.id;

                this.updateCallback = conf.updateCallback;
                this.structure = conf.structure;
                this.onChangeEvents = [];
                if (conf.structureApi) {
                    this.editorStructureService = restServiceFactory.get(conf.structureApi);
                }
                this.uriContext = conf.uriContext as Promise<IUriContext>;
                if (conf.contentApi) {
                    this.editorCRUDService = restServiceFactory.get(conf.contentApi);
                }
                this.initialContent = lodash.cloneDeep(conf.content);
                this.initialDirty = false;

                if (conf.customOnSubmit) {
                    this.onSubmit = conf.customOnSubmit;
                }

                this._unregisterUnrelatedMessagesEvent = systemEventService.subscribe(
                    GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
                    this._handleUnrelatedValidationMessages.bind(this)
                );
                this.element = conf.element;
            }

            setForm(state: GenericEditorState) {
                this.form = state;
                this.form.group.control.valueChanges.subscribe(() => {
                    this.api.triggerContentChangeEvents();
                });
            }

            _finalize(): void {
                this._unregisterUnrelatedMessagesEvent();
                this.popEditorFromStack();
            }

            popEditorFromStack(): void {
                if (!this.editorStackId) {
                    return;
                }

                systemEventService.publishAsync(
                    GenericEditorStackService.EDITOR_POP_FROM_STACK_EVENT,
                    {
                        editorStackId: this.editorStackId
                    }
                );
            }

            /**
             * @ngdoc method
             * @name GenericEditorModule.service:GenericEditorFactoryService#reset
             * @methodOf GenericEditorModule.service:GenericEditorFactoryService
             *
             * @description
             * Sets the content within the editor to its original state.
             */
            reset(pristine?: Payload): Promise<void> {
                this.form && this.form.removeValidationMessages();

                const structurePromise = this.editorStructureService
                    ? this.editorStructureService.get({
                          smarteditComponentType: this.smarteditComponentType
                      })
                    : Promise.resolve(this.structure);

                return this._getUriContext()
                    .then((uriContext) => {
                        return Promise.all([
                            this.api.getLanguages() ||
                                languageService.getLanguagesForSite(uriContext[CONTEXT_SITE_ID]),
                            structurePromise.then((structure: any) =>
                                this._convertStructureArray(structure)
                            ),
                            Promise.resolve(uriContext)
                        ]);
                    })
                    .then(
                        ([languages, structure, uriContext]: [
                            ILanguage[],
                            GenericEditorStructure,
                            IUriContext
                        ]) => {
                            const schema = {
                                id: this.id,
                                languages,
                                structure,
                                uriContext,
                                smarteditComponentType: this.smarteditComponentType,
                                targetedQualifier: this.targetedQualifier
                            };
                            this.schema$.next(schema);
                            this.data$.next(pristine ? pristine : this.pristine);
                            return;
                        }
                    );
            }

            /**
             *  fetch will:
             *  - return data if initialContent is provided
             *  - make a call to the CRUD API to return the payload if initialContent is not provided
             *
             *  (In initialDirty is set to true, it is populated after loading and setting the content which will make the
             *   pristine and component states out of sync thus making the editor dirty)
             */
            fetch(): Promise<any> {
                if (!this.initialDirty) {
                    return this.initialContent
                        ? Promise.resolve(this.initialContent)
                        : this.smarteditComponentId
                        ? this.editorCRUDService
                              .getById(this.smarteditComponentId)
                              .then((response: any) => response || {})
                        : Promise.resolve({});
                }
                return Promise.resolve({});
            }

            load(): Promise<any> {
                return this.fetch().then(
                    (pristine: any) => {
                        return this.reset(pristine);
                    },
                    (failure: any) => {
                        logService.error('GenericEditor.load failed');
                        logService.error(failure);
                    }
                );
            }

            getComponent(): Payload {
                return (this.form && this.form.component) || null;
            }

            /**
             * @ngdoc method
             * @name GenericEditorModule.service:GenericEditorFactoryService#preparePayload
             * @methodOf GenericEditorModule.service:GenericEditorFactoryService
             *
             * @description
             * Transforms the payload before POST/PUT to server
             *
             * @param {Object} the transformed payload
             */
            preparePayload(originalPayload: Payload): Promise<Payload> {
                return Promise.resolve(this.form.sanitizedPayload(originalPayload));
            }

            onSubmit = (payload: Payload): Promise<GenericEditorOnSubmitResponse> => {
                if (this.smarteditComponentId) {
                    payload.identifier = this.smarteditComponentId;
                }

                // if POST mode
                if (this.editorCRUDService && !this.smarteditComponentId) {
                    // if we have a type field in the structure, use it for the type in the POST payload
                    if (this.structure && this.structure.type) {
                        // if the user already provided a type field, lets be nice
                        if (!payload.type) {
                            payload.type = this.structure.type;
                        }
                    }
                }

                return this.preparePayload(payload).then((preparedPayload: Payload) => {
                    const promise = this.editorCRUDService
                        ? this.smarteditComponentId
                            ? this.editorCRUDService.update(preparedPayload)
                            : this.editorCRUDService.save(preparedPayload)
                        : Promise.resolve(preparedPayload);
                    return promise.then(function(response: Payload) {
                        return {
                            payload,
                            response
                        };
                    });
                });
            };

            submit$ = (): Observable<any> => {
                return from(this.submit(this.form.component));
            };

            /**
             * @ngdoc method
             * @name GenericEditorModule.service:GenericEditorFactoryService#submit
             * @methodOf GenericEditorModule.service:GenericEditorFactoryService
             *
             * @description
             * Saves the content within the form for a specified component. If there are any validation errors returned by the CRUD API after saving the content, it will display the errors.
             */
            submit(newContent: Payload): Promise<GenericEditorOnSubmitResponse> {
                // It's necessary to remove validation errors even if the form is not dirty. This might be because of unrelated validation errors
                // triggered in other tab.
                this.form.removeValidationMessages();
                this.hasFrontEndValidationErrors = false;

                if (!this.form.fieldsAreUserChecked()) {
                    this.hasFrontEndValidationErrors = true;
                    // Mark this tab as "in error" due to front-end validation.
                    return Promise.reject(true);
                } else if (this.isValid(true)) {
                    this.inProgress = true;
                    /*
                     * upon submitting, server side may have been updated,
                     * since we PUT and not PATCH, we need to take latest of the fields not presented and send them back with the editable ones
                     */
                    return this.onSubmit(newContent);
                } else {
                    logService.warn(
                        'GenericEditor.submit() - unable to submit form. Form is unexpectedly invalid.'
                    );
                    return Promise.reject({});
                }
            }

            onSuccess = (submitResult: GenericEditorOnSubmitResponse): void => {
                // If we're doing a POST or PUT and the request returns non empty response, then this response is returned.
                // Otherwise the payload for the request is returned.
                let pristine: Payload;
                if (submitResult.response) {
                    pristine = objectUtils.copy<any>(submitResult.response);
                } else {
                    pristine = objectUtils.copy<any>(submitResult.payload);
                }

                delete pristine.identifier;

                if (!this.smarteditComponentId && submitResult.response) {
                    this.smarteditComponentId = (submitResult.response as any).uuid;
                }

                this.reset(pristine);
                this.inProgress = false;

                if (this.updateCallback) {
                    // should onSuccess = updateCallback ?
                    this.updateCallback(pristine, submitResult.response as Payload);
                }
            };

            onFailure = (failure: BackendErrorResponse<BackendValidationErrors>): void => {
                this.form.removeValidationMessages();
                const errors = failure.error ? failure.error.errors : [];
                this.form.displayValidationMessages(errors, true);
                // send unrelated validation messages to any other listening genericEditor when no other errors
                const unrelatedValidationMessages = this.form.collectUnrelatedValidationMessages(
                    errors
                );
                if (unrelatedValidationMessages.length > 0) {
                    // send tab id in errors for the legacy event.
                    systemEventService.publishAsync(
                        GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
                        {
                            messages: unrelatedValidationMessages,
                            sourceGenericEditorId: this.id
                        }
                    );
                }
                this.inProgress = false;
            };

            /**
             * @ngdoc method
             * @name GenericEditorModule.service:GenericEditorFactoryService#refreshOptions
             * @methodOf GenericEditorModule.service:GenericEditorFactoryService
             *
             * @description
             * Is invoked by HTML field templates that update and manage dropdowns.
             *  It updates the dropdown list upon initialization (creates a list of one option) and when performing a search (returns a filtered list).
             *  To do this, the GenericEditor fetches an implementation of the  {@link GenericEditorModule.FetchDataHandlerInterface FetchDataHandlerInterface} using the following naming convention:
             * <pre>"fetch" + cmsStructureType + "DataHandler"</pre>
             * @param {GenericEditorField} field The field in the structure that requires a dropdown to be built.
             * @param {string} qualifier For a non-localized field, it is the actual field.qualifier. For a localized field, it is the ISO code of the language.
             * @param {string} search The value of the mask to filter the dropdown entries on.
             */

            refreshOptions(
                field: GenericEditorField,
                qualifier: string,
                search: string
            ): Promise<void> {
                return new Promise((resolve) => {
                    const ctorOrToken = this._getRefreshHandleContructorOrToken(
                        field.cmsStructureType
                    );
                    const objHandler = this._getDataHandler(ctorOrToken);

                    let theIdentifier: string;
                    let optionsIdentifier: string;

                    if (field.localized) {
                        theIdentifier = (this.form.component[field.qualifier] as any)[
                            qualifier
                        ] as string;
                        optionsIdentifier = qualifier;
                    } else {
                        theIdentifier = this.form.component[field.qualifier] as string;
                        optionsIdentifier = field.qualifier;
                    }

                    field.initiated = field.initiated || [];
                    field.options = field.options || {};

                    if (field.cmsStructureType === 'Enum') {
                        field.initiated.push(optionsIdentifier);
                    }
                    if (field.initiated.indexOf(optionsIdentifier) > -1) {
                        if (search.length > 2 || field.cmsStructureType === 'Enum') {
                            return objHandler
                                .findByMask(field, search)
                                .then((entities: string[]) => {
                                    field.options[optionsIdentifier] = entities;
                                    resolve();
                                });
                        }
                    } else if (theIdentifier) {
                        return objHandler.getById(field, theIdentifier).then((entity: string) => {
                            field.options[optionsIdentifier] = [entity];
                            field.initiated.push(optionsIdentifier);
                            resolve();
                        });
                    } else {
                        field.initiated.push(optionsIdentifier);
                    }

                    return resolve();
                });
            }

            /**
             * @ngdoc method
             * @name GenericEditorModule.service:GenericEditorFactoryService#isDirty
             * @methodOf GenericEditorModule.service:GenericEditorFactoryService
             *
             * @description
             * A predicate function that returns true if the editor is in dirty state or false if it not.
             * The state of the editor is determined by comparing the current state of the component with the state of the component when it was pristine.
             *
             * @return {Boolean} An indicator if the editor is in dirty state or not.
             */
            isDirty(qualifier?: string, language?: string): boolean {
                return (
                    this.initialDirty ||
                    (this.form ? this.form.isDirty(qualifier, language) : false)
                );
            }

            /**
             * Check for html validation errors on all the form fields.
             * If so, assign an error to a field that is not pristine.
             * The seGenericEditorFieldError will render these errors, just like
             * errors we receive from the backend.
             * It also validates error states for tabs.
             */
            isValid(comesFromSubmit = false): boolean {
                if (comesFromSubmit) {
                    const validationErrors = this.form.collectFrontEndValidationErrors(
                        comesFromSubmit,
                        this.nativeForm
                    );
                    this.form.removeFrontEndValidationMessages();
                    this.form.displayValidationMessages(validationErrors, comesFromSubmit);
                }

                return !!this.form && this.form.group.control.valid;
            }

            isSubmitDisabled = (): boolean => {
                return this.inProgress || !this.isDirty() || !this.isValid();
            };

            watchFormErrors(el: HTMLFormElement): void {
                this.form.watchErrors(el);
            }

            init(): Promise<void> {
                return this.load().then(() => {
                    // If initialDirty is set to true and if any initial content is provided, it is populated here which will make the
                    // pristine and component states out of sync thus making the editor dirty

                    if (this.initialDirty) {
                        this.form.patchComponent(this.initialContent || {});
                        this.data$.next(this.initialContent || {});
                    }

                    this._pushEditorToStack();
                    systemEventService.publishAsync(GENERIC_EDITOR_LOADED_EVENT, this.id);
                });
            }

            /* @internal */
            private _validate(conf: IGenericEditorFactoryOptions): void {
                if (stringUtils.isBlank(conf.structureApi) && !conf.structure) {
                    throw new Error('genericEditor.configuration.error.no.structure');
                } else if (!stringUtils.isBlank(conf.structureApi) && conf.structure) {
                    throw new Error('genericEditor.configuration.error.2.structures');
                }
            }

            /* @internal */
            private _getUriContext(): Promise<IUriContext> {
                return Promise.resolve(
                    this.uriContext
                        ? this.uriContext
                        : sharedDataService.get('experience').then((experience: any) => {
                              return {
                                  [CONTEXT_SITE_ID]: experience.siteDescriptor.uid,
                                  [CONTEXT_CATALOG]: experience.catalogDescriptor.catalogId,
                                  [CONTEXT_CATALOG_VERSION]:
                                      experience.catalogDescriptor.catalogVersion
                              } as IUriContext;
                          })
                );
            }

            /**
             * @internal
             * Conversion function in case the first attribute of the response is an array of type structures.
             */
            private _convertStructureArray(
                structure:
                    | GenericEditorStructure
                    | { structures: GenericEditorStructure[] }
                    | { componentTypes: GenericEditorStructure[] }
            ): GenericEditorStructure {
                const structureArray =
                    (structure as any).structures || (structure as any).componentTypes;
                if (lodash.isArray(structureArray)) {
                    if (structureArray.length > 1) {
                        throw new Error('init: Invalid structure, multiple structures returned');
                    }
                    structure = structureArray[0];
                }
                return structure as GenericEditorStructure;
            }

            /* @internal */
            private _handleUnrelatedValidationMessages(
                key: string,
                validationData: TypedMap<any>
            ): void {
                if (
                    validationData.targetGenericEditorId &&
                    validationData.targetGenericEditorId !== this.id
                ) {
                    return;
                }

                if (
                    validationData.sourceGenericEditorId &&
                    validationData.sourceGenericEditorId === this.id
                ) {
                    return;
                }

                this.form.removeValidationMessages();
                this.form.displayValidationMessages(validationData.messages, true);
            }

            /* @internal */
            private _getDataHandler(ctorOrToken: IFetchDataHandler | string): IFetchDataHandler {
                if (upgrade.injector.get(ctorOrToken, null)) {
                    return upgrade.injector.get(ctorOrToken);
                }

                if (upgrade.$injector.has(ctorOrToken)) {
                    return upgrade.$injector.get(ctorOrToken);
                }

                return null;
            }

            /* @internal */
            private _getRefreshHandleContructorOrToken(type: string): IFetchDataHandler | string {
                switch (type) {
                    case 'Enum':
                        return (FetchEnumDataHandler as any) as IFetchDataHandler;
                    default:
                        return 'fetch' + type + 'DataHandler';
                }
            }

            /* @internal */
            private _pushEditorToStack(): void {
                systemEventService.publishAsync(
                    GenericEditorStackService.EDITOR_PUSH_TO_STACK_EVENT,
                    {
                        editorId: this.id,
                        editorStackId: this.editorStackId,
                        component: this.form.component,
                        componentType: this.smarteditComponentType
                    }
                );
            }
        };
    }

    getGenericEditorConstructor(): IGenericEditorConstructor {
        return this.genericEditorConstructor;
    }
}
