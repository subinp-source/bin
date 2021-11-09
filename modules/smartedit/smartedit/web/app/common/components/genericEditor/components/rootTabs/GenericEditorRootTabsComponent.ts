/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component } from '@angular/core';

import { DynamicForm, DynamicInput, FormGrouping } from '@smart/utils';
import { GenericEditorTab } from '../../types';

@Component({
    selector: 'se-ge-root-tabs',
    styles: [
        `
            :host {
                display: block;
            }
        `
    ],
    template: `
        <se-tabs
            class="se-generic-editor__tabs"
            [model]="form"
            [tabsList]="tabs"
            [numTabsDisplayed]="3"
        ></se-tabs>
    `
})
export class GenericEditorRootTabsComponent {
    @DynamicForm()
    form: FormGrouping;

    @DynamicInput()
    tabs: GenericEditorTab[];
}
