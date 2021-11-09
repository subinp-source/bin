/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { TabData, TAB_DATA } from '../../tabs';
import { FormField, FormGrouping } from '@smart/utils';

@Component({
    selector: 'se-generic-editor-field-wrapper',
    template: `
        <ng-template [formRenderer]="form"></ng-template>
    `
})
export class GenericEditorFieldWrapperComponent implements OnDestroy {
    public form: FormField;
    private _subscription: Subscription;

    constructor(@Inject(TAB_DATA)
    {
        model: form,
        tabId,
        tab
    }: TabData<FormGrouping>) {
        this.form = form.forms[tabId] as FormField;

        this._subscription = this.form.control.statusChanges.subscribe((status) => {
            tab.hasErrors = status === 'INVALID';
        });
    }

    ngOnDestroy() {
        this._subscription.unsubscribe();
    }
}
