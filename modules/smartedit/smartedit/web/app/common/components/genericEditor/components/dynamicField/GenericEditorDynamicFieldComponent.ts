/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { DynamicFormControl, DynamicInput, Payload } from '@smart/utils';

import { GenericEditorField } from '../../types';

/**
 * TODO: Some parts of the generic editor field can be moved up to this component.
 * and then we could dynamic inject which form we should put the control in.
 */
@Component({
    selector: 'se-ge-dynamic-field',
    styles: [
        `
            :host {
                display: block;
            }
        `
    ],
    template: `
        <se-generic-editor-field
            [formControl]="control"
            [field]="field"
            [model]="component"
            [qualifier]="qualifier"
            [id]="id"
        ></se-generic-editor-field>
    `
})
export class GenericEditorDynamicFieldComponent {
    @DynamicFormControl()
    control: FormControl;

    @DynamicInput()
    component: Payload;

    @DynamicInput()
    field: GenericEditorField;

    @DynamicInput()
    qualifier: string;

    @DynamicInput()
    id: string;
}
