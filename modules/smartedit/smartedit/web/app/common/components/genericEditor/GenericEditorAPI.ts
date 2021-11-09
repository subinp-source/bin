/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from '@smart/utils';

import { ILanguage } from '../../services';
import { objectUtils } from '../../utils';
import { GenericEditorAPI } from './types';

export const createApi = (editor: any): GenericEditorAPI => {
    /**
     * @ngdoc object
     * @name GenericEditorModule.object:genericEditorApi
     * @description
     * The generic editor's api object exposing public functionality
     */
    return {
        /**
         * @ngdoc method
         * @name setSubmitButtonText
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Overrides the i18n key used bfor the submit button
         */
        setSubmitButtonText: (_submitButtonText: string): void => {
            editor.submitButtonText = _submitButtonText;
        },

        /**
         * @ngdoc method
         * @name setCancelButtonText
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Overrides the i18n key used bfor the submit button
         */
        setCancelButtonText: (_cancelButtonText: string): void => {
            editor.cancelButtonText = _cancelButtonText;
        },

        /**
         * @ngdoc method
         * @name setAlwaysShowSubmit
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * If set to true, will always show the submit button
         */
        setAlwaysShowSubmit: (_alwaysShowSubmit: boolean): void => {
            editor.alwaysShowSubmit = _alwaysShowSubmit;
        },

        /**
         * @ngdoc method
         * @name setAlwaysShowReset
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * If set to true, will always show the reset button
         */
        setAlwaysShowReset: (_alwaysShowReset: boolean): void => {
            editor.alwaysShowReset = _alwaysShowReset;
        },

        /**
         * @ngdoc method
         * @name onReset
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * To be executed after reset
         */
        setOnReset: (_onReset: () => void): void => {
            editor.onReset = _onReset;
        },

        /**
         * @ngdoc method
         * @name setPreparePayload
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Function that passes a preparePayload function to the editor in order to transform the payload prior to submitting (see {@link GenericEditorModule.service:GenericEditorFactoryService#preparePayload})
         *
         * @param {Object} preparePayload The function that takes the original payload as argument
         */
        setPreparePayload: (_preparePayload: (payload: Payload) => Promise<Payload>) => {
            editor.preparePayload = _preparePayload;
        },

        /**
         * @ngdoc method
         * @name setUpdateCallback
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Function that passes an updateCallback function to the editor in order to perform an action upon successful submit. It is invoked with two arguments: the pristine object and the response from the server.
         * @param {Object} updateCallback the callback invoked upon successful submit
         */
        setUpdateCallback: (_updateCallback: (pristine: Payload, results: Payload) => void) => {
            editor.updateCallback = _updateCallback;
        },

        /**
         * @ngdoc method
         * @name updateComponent
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Function that updates the content of the generic editor without having to reinitialize
         *
         * @param {Object} component The component to replace the current model for the generic editor
         */
        updateContent: (component: Payload) => {
            editor.form && editor.form.patchComponent(component);
        },

        /**
         * @ngdoc method
         * @name getContent
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * copies of the current model
         * @return {object} a copy
         */
        getContent: (): Payload => {
            return editor.form ? objectUtils.copy<Payload>(editor.form.component) : undefined;
        },

        /**
         * @deprecated since 1905 - use {@link GenericEditorModule.object:genericEditorApi addContentChangeEvent} instead.
         *
         * @ngdoc method
         * @name onContentChange
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Function triggered everytime the current model changes
         */
        onContentChange(): void {
            return;
        },

        /**
         * @ngdoc method
         * @name addContentChangeEvent
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Method adds a new function to the list of functions triggered everytime the current model changes
         *
         * @param {Function} The function triggered everytime the current model changes
         *
         * @return {Function} The function to unregister the event;
         */
        addContentChangeEvent: (event: () => void) => {
            editor.onChangeEvents.push(event);
            return () => {
                const index = editor.onChangeEvents.findIndex((e: any) => {
                    return e === event;
                });

                if (index > -1) {
                    editor.onChangeEvents.splice(index, 1);
                }
            };
        },

        /**
         * @ngdoc method
         * @name triggerContentChangeEvents
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Triggers all functions that were added with addContentChangeEvent api method. It provides current content as parameter to every function call.
         */
        triggerContentChangeEvents: () => {
            editor.onChangeEvents.forEach((event: any) => {
                event(objectUtils.copy<Payload>(editor.form.component));
            });
        },

        /**
         * @ngdoc method
         * @name clearMessages
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Function that clears all validation messages in the editor
         */
        clearMessages: () => {
            editor.form.removeValidationMessages();
        },

        /**
         * @ngdoc method
         * @name switchToTabContainingQualifier
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * causes the genericEditor to switch to the tab containing a qualifier of the given name
         * @param {String} qualifier the qualifier contained in the tab we want to switch to
         */
        switchToTabContainingQualifier: (qualifier: string) => {
            editor.targetedQualifier = qualifier;
        },

        // currently used by clone components to open editor in dirty mode
        considerFormDirty: () => {
            editor.initialDirty = true;
        },

        /**
         * @ngdoc method
         * @name isSubmitDisabled
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * returns true to inform that the submit button delegated to the invoker should be disabled
         * @return {boolean} true if submit is disabled
         */
        isSubmitDisabled: () => {
            return editor.isSubmitDisabled();
        },

        /**
         * @ngdoc method
         * @name getLanguages
         * @methodOf GenericEditorModule.object:genericEditorApi
         * @description
         * Function that returns a promise resolving to language descriptors. If defined, will be resolved
         * when the generic editor is initialized to override what languages are used for localized elements
         * within the editor.
         * @return {Promise<ILanguage[]>} a promise resolving to language descriptors. Each descriptor provides the following
         * language properties: isocode, nativeName, name, active, and required.
         */
        getLanguages: (): Promise<ILanguage[]> => {
            return null;
        }
    };
};
