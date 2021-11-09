/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Type } from '@angular/core';
import { Payload, TypedMap } from '@smart/utils';
import { ILanguage, IUriContext } from '../../services';
import { FormControl } from '@angular/forms';

import { GenericEditorState } from './models';

export interface GenericEditorOnSubmitResponse {
    payload: Payload;
    response: Payload;
}

export interface GenericEditorAttribute {
    cmsStructureType: string;
    cmsStructureEnumType?: string;
    qualifier: string;
    i18nKey?: string;
    localized?: boolean;
    editable?: boolean;
    required?: boolean;
    collection?: boolean;
    postfixText?: string;
}

/*
 * custom sanitization fucntion
 * first argument is a property value (not the full model) that may be comple or scalar
 */
export type GenericEditorCustomSanitize<T = any> = (
    payload: T,
    sanitize: (input: string) => string
) => T;

export interface GenericEditorField extends GenericEditorAttribute {
    hasErrors?: boolean;
    hasWarnings?: boolean;
    smarteditComponentType?: string;
    messages?: GenericEditorFieldMessage[];
    paged?: boolean;
    template?: string;
    component?: Type<any>;
    initiated?: string[];
    options?: TypedMap<string[]> | GenericEditorOption;
    hidePrefixLabel?: boolean;
    customSanitize?: GenericEditorCustomSanitize;
    requiresUserCheck?: TypedMap<boolean>;
    isUserChecked?: boolean;
    defaultValue?: string;
    params?: TypedMap<string>;
    dependsOn?: string;
    uri?: string;
    propertyType?: string;
    idAttribute?: string;
    labelAttributes?: string[];
    errors?: GenericEditorFieldMessage[];
    control?: FormControl;

    /**
     * This map is only used for localized fields. Each entry contains a boolean
     * that specifies whether the field should be enabled for a language or not.
     */
    isLanguageEnabledMap?: { [languageId: string]: boolean };
    tooltip?: string;
    labelText?: string;
}

export interface GenericEditorTab {
    id: string;
    title: string;
    templateUrl?: string;
    component?: Type<any>;
    hasErrors?: boolean;
    active?: boolean;
}

export type GenericEditorFieldsMap = TypedMap<GenericEditorField[]>;

/**
 * @ngdoc interface
 * @name GenericEditorModule.interface:GenericEditorSchema
 * @description
 * Descriptor schema necessary to build a GenericEditorState.
 */
export interface GenericEditorSchema {
    id: string;
    languages: ILanguage[];
    structure: GenericEditorStructure;
    uriContext: IUriContext;
    smarteditComponentType: string;
    targetedQualifier?: string;
}

export interface GenericEditorFieldMessage {
    fromSubmit?: boolean;
    isNonPristine?: boolean;
    message: string;
    subject?: string;
    type?: string;
    uniqId?: string;
    marker?: string;
    format?: string;
    language?: string;
    [index: string]: any;
}

export interface GenericEditorTabConfiguration {
    priority: number;
}

export interface GenericEditorStructure {
    attributes: GenericEditorAttribute[];
    category: string;
    type?: string;
}

export interface IGenericEditorFactoryOptions {
    content?: Payload;
    contentApi?: string;
    customOnSubmit?: (newContent: Payload) => Promise<GenericEditorOnSubmitResponse>;
    editorStackId?: string;
    id?: string;
    smarteditComponentId?: string;
    smarteditComponentType?: string;
    structure?: GenericEditorStructure;
    structureApi?: string;
    updateCallback?: (pristine: Payload, results: Payload) => void;
    uriContext?: Promise<IUriContext>;
    element: HTMLElement;
}

export type GenericEditorPredicate = (structure: GenericEditorStructure) => boolean | string;

export interface GenericEditorInfo {
    editorStackId?: string;
    editorId: string;
    component: Payload;
    componentType: string;
}

export interface GenericEditorMapping {
    structureTypeMatcher: string | ((...args: any[]) => boolean);
    componentTypeMatcher: string | ((...args: any[]) => boolean);
    discriminatorMatcher: string | ((...args: any[]) => boolean);
    value: any;
}

export interface GenericEditorMappingConfiguration {
    template?: string;
    component?: Type<any>;
    customSanitize?: GenericEditorCustomSanitize;
    precision?: string;
    i18nKey?: string;
    hidePrefixLabel?: boolean;
}

export interface BackendValidationErrors {
    errors: GenericEditorFieldMessage[];
}
export interface BackendErrorResponse<T> {
    error: T;
}
export interface GenericEditorAPI {
    setSubmitButtonText: (_submitButtonText: string) => void;
    setCancelButtonText: (_cancelButtonText: string) => void;
    setAlwaysShowSubmit: (_alwaysShowSubmit: boolean) => void;
    setAlwaysShowReset: (_alwaysShowReset: boolean) => void;
    setOnReset: (_onReset: () => void) => void;
    setPreparePayload: (_preparePayload: (payload: Payload) => Promise<Payload>) => void;
    setUpdateCallback: (_updateCallback: (pristine: Payload, results: Payload) => void) => void;
    updateContent: (component: Payload) => void;
    getContent: () => Payload;
    onContentChange: () => void;
    addContentChangeEvent: (event: () => void) => () => void;
    triggerContentChangeEvents: () => void;
    clearMessages: () => void;
    switchToTabContainingQualifier: (qualifier: string) => void;
    considerFormDirty: () => void;
    isSubmitDisabled: () => boolean;
    getLanguages: () => Promise<ILanguage[]>;
}

export interface IGenericEditor {
    readonly id: string;
    readonly form: GenericEditorState;
    readonly api: GenericEditorAPI;
    readonly onReset: () => void;
    readonly alwaysShowSubmit: boolean;
    readonly alwaysShowReset: boolean;
    readonly editorStackId: string;
    readonly hasFrontEndValidationErrors: boolean;
    readonly submitButtonText: string;
    readonly cancelButtonText: string;
    readonly parameters: IUriContext;
    readonly inProgress: boolean;
    readonly smarteditComponentType: string;
    readonly smarteditComponentId: string;
    readonly updateCallback: (pristine: Payload, results: Payload) => void;
    readonly structure: GenericEditorStructure;
    readonly uriContext: Promise<IUriContext>;
    readonly editorStructureService: any;
    readonly editorCRUDService: any;
    readonly initialContent: Payload;
    readonly pristine: Payload;
    readonly initialDirty: boolean;
    readonly targetedQualifier: string;

    _finalize(): void;
    init(): Promise<void>;
    isDirty(): boolean;
    reset(pristine?: Payload): Promise<any>;
    submit(newContent: Payload): Promise<any>;
    getComponent(): Payload;
    isValid(comesFromSubmit?: boolean): boolean;
    isSubmitDisabled(): boolean;
    watchFormErrors(form: HTMLFormElement): void;
    fetch(): Promise<any>;
    load(): Promise<any>;
    popEditorFromStack(): void;
    refreshOptions(field: GenericEditorField, qualifier: string, search: string): Promise<void>;
}

export type IGenericEditorConstructor = new (conf: IGenericEditorFactoryOptions) => IGenericEditor;

export interface GenericEditorOption {
    id?: string;
    label?: string | TypedMap<string>;
    [key: string]: any;
}

export interface GenericEditorWidgetData<T> {
    field: GenericEditorField;
    qualifier: string;
    model: T;
    editor?: IGenericEditor;
    isFieldDisabled: () => boolean;
}
