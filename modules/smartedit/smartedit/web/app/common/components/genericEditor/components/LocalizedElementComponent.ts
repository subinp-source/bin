/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, DoCheck } from '@angular/core';
import {
    DynamicForm,
    DynamicInput,
    DynamicInputChange,
    FormGrouping,
    ISessionService,
    Payload,
    TypedMap
} from '@smart/utils';

import { VALIDATION_MESSAGE_TYPES } from '../../../utils';
import { GenericEditorFieldWrapperComponent } from './GenericEditorFieldWrapperComponent';
import { ILanguage } from '../../../services';
import { GenericEditorField, GenericEditorFieldMessage, GenericEditorTab } from '../types';

@Component({
    selector: 'se-localized-element',
    styles: [
        `
            :host {
                display: block;
            }
        `
    ],
    templateUrl: './LocalizedElementComponent.html'
})
export class LocalizedElementComponent implements DoCheck, DynamicInputChange {
    @DynamicForm()
    form: FormGrouping;

    @DynamicInput()
    field: GenericEditorField;

    @DynamicInput()
    component: Payload;

    @DynamicInput()
    languages: ILanguage[];

    tabs: GenericEditorTab[];
    private _previousMessages: GenericEditorFieldMessage[];

    constructor(private sessionService: ISessionService) {}

    onDynamicInputChange() {
        this._createLocalizedTabs();
    }

    /**
     * TODO: Could probably remove this method after replacing native HTML, validation with
     * Angular validation.
     */
    ngDoCheck() {
        if (this.tabs && this.field.messages !== this._previousMessages) {
            this._previousMessages = this.field.messages;

            const messageMap: TypedMap<boolean> = this.field.messages
                ? this.field.messages
                      .filter((message: GenericEditorFieldMessage) => {
                          return message.type === VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR;
                      })
                      .reduce((holder: TypedMap<boolean>, next) => {
                          holder[next.language] = true;
                          return holder;
                      }, {})
                : {};

            this.tabs.forEach(function(tab) {
                const message = messageMap[tab.id];

                tab.hasErrors = message !== undefined ? message : false;
            });
        }
    }

    /**
     * Map fields to localized tabs.
     *
     * @returns {Promise<void>}
     * @private
     */
    private async _createLocalizedTabs() {
        this.field.isLanguageEnabledMap = {};
        const {
            readableLanguages,
            writeableLanguages
        } = await this.sessionService.getCurrentUser();

        const readableSet = new Set(readableLanguages);
        const writeable = new Set(writeableLanguages);

        this.tabs = this.languages
            .filter((language: ILanguage) => readableSet.has(language.isocode))
            .map(({ isocode, required }: ILanguage) => {
                this.field.isLanguageEnabledMap[isocode] = writeable.has(isocode);

                const title = `${isocode.toUpperCase()}${
                    this.field.editable && required ? '*' : ''
                }`;
                return {
                    id: isocode,
                    title,
                    component: GenericEditorFieldWrapperComponent
                };
            });
    }
}
