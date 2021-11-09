/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Input } from '@angular/core';
import { VALIDATION_MESSAGE_TYPES } from '../../../utils';
import { GenericEditorField, GenericEditorFieldMessage } from '../types';

/**
 * @ngdoc directive
 * @name GenericEditorModule.component:GenericEditorFieldMessages
 * @element se-generic-editor-field-messages
 *
 * @description
 * Component responsible for displaying validation messages like errors or warnings.
 *
 * @param {< Object} field The field object that contains array of messages.
 * @param {< String} qualifier For a non-localized field, it is the actual field.qualifier. For a localized field, it is the ISO code of the language.
 */

@Component({
    selector: 'se-generic-editor-field-messages',
    templateUrl: './GenericEditorFIeldMessagesComponent.html'
})
export class GenericEditorFieldMessagesComponent {
    @Input() field: GenericEditorField;
    @Input() qualifier: string;

    public errors: string[];
    public warnings: string[];

    private previousMessages: string = null;

    ngDoCheck(): void {
        if (this.field) {
            const currentMessages = JSON.stringify(this.field.messages);

            if (this.previousMessages !== currentMessages) {
                this.previousMessages = currentMessages;
                this.errors = this.getFilteredMessagesByType(
                    VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR
                );
                this.warnings = this.getFilteredMessagesByType(VALIDATION_MESSAGE_TYPES.WARNING);
            }
        }
    }

    getFilteredMessagesByType(messageType: string): string[] {
        return (this.field.messages || [])
            .filter((validationMessage: GenericEditorFieldMessage) => {
                return (
                    validationMessage.marker === this.qualifier &&
                    !validationMessage.format &&
                    validationMessage.type === messageType
                );
            })
            .map((validationMessage) => {
                return validationMessage.message;
            });
    }
}
