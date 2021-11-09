/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { combineLatest } from 'rxjs';
import { distinctUntilChanged, map, startWith } from 'rxjs/operators';

import { stringUtils, VALIDATION_MESSAGE_TYPES } from '../../../utils';
import { AbstractForm, FormField, FormGrouping } from '@smart/utils';
import { ILanguage, IUriContext } from '../../../services';
import { Payload, TypedMap } from '../../../dtos';

import { GenericEditorField, GenericEditorFieldMessage, GenericEditorTab } from '../types';
import { parseValidationMessage } from '../services/SeValidationMessageParser';
import { GenericEditorDynamicFieldComponent } from '../components/dynamicField/GenericEditorDynamicFieldComponent';
import { proxifyDataObject } from './InternalProperty';

const VALIDATION_MESSAGE_TYPES_SET = new Set(lo.values(VALIDATION_MESSAGE_TYPES));

const CMS_STRUCTURE_TYPE = {
    SHORT_STRING: 'ShortString',
    LONG_STRING: 'LongString'
};

/**
 * @internal
 * Holds the entire state of the generic editor.
 * Provides method to query and mutate the generic editor state.
 * The GenericEditorState is created by the GenericEditorStateBuilderService.
 */
export class GenericEditorState {
    /**
     * @internal
     */
    private _bcPristine: any;

    /**
     * @internal
     */
    private _formFields: FormField[];

    /**
     * @internal
     */
    private _qualifierFieldMap: Map<string, GenericEditorField>;

    constructor(
        public readonly id: string,
        public readonly group: FormGrouping,
        public component: any,
        private proxiedComponent: any,
        public pristine: any,
        public readonly tabs: GenericEditorTab[],
        public readonly fields: GenericEditorField[],
        public readonly languages: ILanguage[],
        public readonly parameters: IUriContext
    ) {
        this._qualifierFieldMap = this.fields.reduce((acc, field) => {
            acc.set(field.qualifier, field);
            return acc;
        }, new Map());

        this._formFields = this._buildFormFieldsArray(this.group);
    }

    /**
     * Removes all validation (local, outside or server) errors from fieds and tabs.
     */
    removeValidationMessages(): void {
        this.fields.forEach((field: GenericEditorField) => {
            field.messages = undefined;
            field.hasErrors = false;
            field.hasWarnings = false;
        });
    }

    /**
     * Removes validation errors generated in frontend, not the ones sent by outside or server.
     * Removes errors only from fields, not tabs.
     */
    removeFrontEndValidationMessages(): void {
        this.fields.forEach((field: GenericEditorField) => {
            if (!Array.isArray(field.messages)) {
                return;
            }
            const messages = (field.messages || []).filter((message: GenericEditorFieldMessage) => {
                return message.fromSubmit === undefined ? true : message.fromSubmit;
            });
            field.messages = messages.length ? messages : undefined;
            field.hasErrors = this._containsValidationMessageType(
                field.messages,
                VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR
            );
            field.hasWarnings = this._containsValidationMessageType(
                field.messages,
                VALIDATION_MESSAGE_TYPES.WARNING
            );
        });
    }

    /**
     * Checks if this validation message belongs to the current generic editor by seeing if the generic editor
     * has the qualifier.
     *
     * TODO: It assumes that the qualifier is unique in every genericeditor.
     *
     * @param {GenericEditorFieldMessage} validationMessage
     * @return {boolean}
     */
    validationMessageBelongsToCurrentInstance(
        validationMessage: GenericEditorFieldMessage
    ): boolean {
        return this._qualifierFieldMap.has(validationMessage.subject);
    }

    /**
     * @param {GenericEditorFieldMessage[]} messages
     * @return {GenericEditorFieldMessage[]}
     */
    collectUnrelatedValidationMessages(
        messages: GenericEditorFieldMessage[]
    ): GenericEditorFieldMessage[] {
        return messages.filter((message: GenericEditorFieldMessage) => {
            return (
                this._isValidationMessageType(message.type) &&
                !this.validationMessageBelongsToCurrentInstance(message)
            );
        });
    }

    /**
     * Collects validation errors on all the form fields.
     * Returns the list of errors or empty list.
     * Each error contains the following properties:
     * type - VALIDATION_MESSAGE_TYPES
     * subject - the field qualifier.
     * message - error message.
     * fromSubmit - contains true if the error is related to submit operation, false otherwise.
     * isNonPristine - contains true if the field was modified (at least once) by the user, false otherwise.
     * language - optional language iso code.
     */

    watchErrors(formElement: HTMLFormElement): void {
        const formChangeStream = this._formFields.map((form) =>
            form.control.statusChanges.pipe(startWith(null))
        );

        combineLatest([...formChangeStream, this.group.control.statusChanges.pipe(startWith(null))])
            .pipe(
                distinctUntilChanged((prev, curr) => lo.isEqual(prev, curr)),
                map(() => this.collectFrontEndValidationErrors(false, formElement))
            )
            .subscribe((messages) => {
                this.removeFrontEndValidationMessages();
                this.displayValidationMessages(messages, false);
            });
    }

    collectFrontEndValidationErrors(
        comesFromSubmit: boolean,
        formElement: HTMLFormElement
    ): GenericEditorFieldMessage[] {
        comesFromSubmit = comesFromSubmit || false;
        return this._formFields.reduce((acc, form) => {
            const field = form.getInput<GenericEditorDynamicFieldComponent>(
                'field'
            ) as GenericEditorField;

            if (
                (() => {
                    const fieldNativeElement = Array.from(formElement || []).find(
                        (elem: HTMLElement) => elem.getAttribute('name') === field.qualifier
                    ) as HTMLInputElement;

                    return !!fieldNativeElement && !fieldNativeElement.checkValidity();
                })()
            ) {
                acc.push({
                    type: VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR,
                    subject: field.qualifier,
                    message: 'se.editor.htmlo.validation.error',
                    fromSubmit: comesFromSubmit,
                    isNonPristine: this.isDirty(field.qualifier)
                });
            }

            // Could get more specific errors.
            if (form.control.getError('required') && form.control.touched) {
                if (field.localized) {
                    const id = form.getInput<GenericEditorDynamicFieldComponent>('id') as string;
                    acc.push({
                        type: VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR,
                        subject: field.qualifier,
                        message: 'se.componentform.required.field',
                        language: id, // Isocode
                        fromSubmit: comesFromSubmit,
                        isNonPristine: this.isDirty(
                            field.qualifier,
                            id // Isocode
                        )
                    });
                } else {
                    acc.push({
                        type: VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR,
                        subject: field.qualifier,
                        message: 'se.componentform.required.field',
                        fromSubmit: comesFromSubmit,
                        isNonPristine: this.isDirty(field.qualifier)
                    });
                }
            }
            return acc;
        }, []);
    }

    /**
     * Displays validation errors for fields and changes error states for all tabs.
     * TODO: move validation to fields.
     */
    displayValidationMessages(
        validationMessages: GenericEditorFieldMessage[],
        keepAllErrors: boolean
    ): Promise<void> {
        validationMessages
            .filter((message: GenericEditorFieldMessage) => {
                return (
                    this._isValidationMessageType(message.type) &&
                    (keepAllErrors || message.isNonPristine)
                );
            })
            .forEach((validation: GenericEditorFieldMessage) => {
                validation.type = validation.type || VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR;
                const field = this._qualifierFieldMap.get(validation.subject);

                if (!field) {
                    return;
                }

                if (!field.messages) {
                    field.messages = [];
                }

                const message = lo.merge(
                    validation,
                    this._getParseValidationMessage(validation.message)
                );

                message.marker = field.localized ? message.language : field.qualifier;
                message.type = validation.type;
                message.uniqId = stringUtils.encode(message);

                const existing = field.messages.find((msg: GenericEditorFieldMessage) => {
                    return msg.uniqId === message.uniqId;
                });

                if (!existing) {
                    field.messages.push(message);
                    if (message.type === VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR) {
                        field.hasErrors = true;
                    } else if (message.type === VALIDATION_MESSAGE_TYPES.WARNING) {
                        field.hasWarnings = true;
                    }
                } else {
                    // Update existing message.
                    lo.merge(existing, message);
                }
            });

        /**
         * Need to trigger onStatusChanges for each tab because these messages are added after
         * validation is triggered inside of AbstractFormControls. Messages will dictate for now
         * if a field is invalid.
         */
        lo.values(this.group.forms).forEach((tab) => {
            tab.control.updateValueAndValidity({ emitEvent: true });
        });
        return Promise.resolve();
    }

    isDirty(qualifier?: string, language?: string): boolean {
        this._bcPristine = this._buildComparable(this.fields, this.pristine);
        const bcComponent = this._buildComparable(this.fields, this.component);

        const subPristine = qualifier
            ? language
                ? this._bcPristine[qualifier][language]
                : this._bcPristine[qualifier]
            : this._bcPristine;
        const subComponent = qualifier
            ? language
                ? (bcComponent[qualifier] as any)[language]
                : bcComponent[qualifier]
            : bcComponent;

        return !lo.isEqual(subPristine, subComponent);
    }

    fieldsAreUserChecked(): boolean {
        return this.fields.every((field: GenericEditorField) => {
            let requiresUserCheck = false;
            for (const qualifier in field.requiresUserCheck) {
                if (field.requiresUserCheck.hasOwnProperty(qualifier)) {
                    requiresUserCheck = requiresUserCheck || field.requiresUserCheck[qualifier];
                }
            }
            return !requiresUserCheck || field.isUserChecked;
        });
    }

    /**
     * Updates the component with the patching component.
     *
     * @param value
     */
    patchComponent(value: Payload) {
        // Proxify the localized objects.
        this._qualifierFieldMap.forEach(({ localized, qualifier }) => {
            if (localized && value[qualifier]) {
                value[qualifier] = proxifyDataObject(value[qualifier]);
            }
        });

        Object.assign(this.proxiedComponent, value);
    }

    /**
     * Get sanitized payload to be sent to the backend.
     *
     * @param {Payload} payload
     * @returns {Payload}
     */
    sanitizedPayload(payload: Payload = this.component): Payload {
        this.fields
            .filter((field: GenericEditorField) => {
                return (
                    field.cmsStructureType === CMS_STRUCTURE_TYPE.LONG_STRING ||
                    field.cmsStructureType === CMS_STRUCTURE_TYPE.SHORT_STRING ||
                    typeof field.customSanitize === 'function'
                );
            })
            .forEach(({ qualifier, localized, customSanitize }) => {
                if (payload[qualifier] !== undefined && qualifier in payload) {
                    if (customSanitize) {
                        payload[qualifier] = customSanitize(
                            payload[qualifier] as any,
                            stringUtils.sanitize
                        );
                    } else {
                        if (localized) {
                            const qualifierValueObject = payload[qualifier] as any;
                            Object.keys(qualifierValueObject).forEach((locale: string) => {
                                qualifierValueObject[locale] = stringUtils.sanitize(
                                    qualifierValueObject[locale]
                                );
                            });
                        } else {
                            payload[qualifier] = stringUtils.sanitize(
                                this.component[qualifier] as string
                            );
                        }
                    }
                }
            });

        return payload;
    }

    /*
     * Switches to tab with qualifier.
     * Causes the genericEditor to switch to the tab containing a qualifier of the given name.
     */
    switchToTabContainingQualifier(targetedQualifier?: string): void {
        if (!targetedQualifier) {
            return;
        }

        this.tabs.forEach((tab: GenericEditorTab) => {
            tab.active = !!this.group.getFormElement([tab.id, targetedQualifier]);
        });
    }

    private _getParseValidationMessage(message: string): GenericEditorFieldMessage {
        return parseValidationMessage(message);
    }

    /**
     * Sees if it contains validation message type.
     * @param {GenericEditorFieldMessage[]} validationMessages
     * @param {string} messageType
     * @return {boolean}
     * @private
     */
    private _containsValidationMessageType(
        validationMessages: GenericEditorFieldMessage[],
        messageType: string
    ): boolean {
        if (!Array.isArray(validationMessages)) {
            return false;
        }
        return validationMessages.some((message: GenericEditorFieldMessage) => {
            return (
                message.type === messageType &&
                this.validationMessageBelongsToCurrentInstance(message)
            );
        });
    }

    /**
     * @internal
     * Checks if validation message type is of type ValidationError or Warning.
     */
    private _isValidationMessageType(messageType: string): boolean {
        return VALIDATION_MESSAGE_TYPES_SET.has(messageType);
    }

    /**
     * @internal
     * Builds a comparable data object.
     */
    private _buildComparable(fields: GenericEditorField[], source: Payload): Payload {
        if (!source) {
            return source;
        }
        const comparable: Payload = {};

        fields.forEach((field: GenericEditorField) => {
            let fieldValue = source[field.qualifier];
            if (field.localized) {
                fieldValue = fieldValue as TypedMap<string>;

                const sub: Payload = {};
                lo.forEach(fieldValue, (langValue: string, lang: string) => {
                    if (!lo.isUndefined(langValue)) {
                        sub[lang] = this._buildFieldComparable(langValue, field);
                    }
                });
                comparable[field.qualifier] = sub;
            } else {
                fieldValue = source[field.qualifier] as string;
                comparable[field.qualifier] = this._buildFieldComparable(fieldValue, field);
            }
        });

        // sometimes, such as in navigationNodeEntryEditor, we update properties not part of the fields and still want the editor to turn dirty
        lo.forEach(source, (value: Payload, key: string) => {
            const notDisplayed = !fields.some((field: GenericEditorField) => {
                return field.qualifier === key;
            });
            if (notDisplayed) {
                comparable[key] = value;
            }
        });

        return lo.omitBy(comparable, lo.isUndefined);
    }

    /**
     * @internal
     */
    private _buildFieldComparable(fieldValue: string, field: GenericEditorField): string | boolean {
        switch (field.cmsStructureType) {
            case 'RichText':
                return fieldValue !== undefined ? stringUtils.sanitizeHTML(fieldValue) : null;
            case 'Boolean':
                return fieldValue !== undefined ? fieldValue : false;
            default:
                return fieldValue;
        }
    }

    /**
     * @internal
     * Get all leaf nodes of the form.
     */
    private _buildFormFieldsArray(form: AbstractForm, array: FormField[] = []): FormField[] {
        if (form instanceof FormField) {
            array.push(form);
            return array;
        }
        if (form instanceof FormGrouping) {
            Object.keys(form.forms).forEach((key) => {
                const field = form.forms[key];

                this._buildFormFieldsArray(field, array);
            });
            return array;
        }
        return array;
    }
}
