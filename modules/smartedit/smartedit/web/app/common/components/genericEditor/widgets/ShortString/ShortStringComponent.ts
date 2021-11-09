/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject } from '@angular/core';
import { GENERIC_EDITOR_WIDGET_DATA } from '../../components/GenericEditorFieldComponent';
import { GenericEditorWidgetData } from '../../types';

@Component({
    template: `
        <input
            type="text"
            id="{{ data.field.qualifier }}-shortstring"
            [ngClass]="{
                'is-invalid': data.field.hasErrors,
                'is-warning': data.field.hasWarnings
            }"
            class="fd-form-control"
            [placeholder]="data.field.tooltip || '' | translate"
            [attr.name]="data.field.qualifier"
            [disabled]="data.isFieldDisabled()"
            [(ngModel)]="data.model[data.qualifier]"
        />
    `,
    selector: 'se-short-string'
})
export class ShortStringComponent {
    constructor(@Inject(GENERIC_EDITOR_WIDGET_DATA) public data: GenericEditorWidgetData<any>) {}
}
