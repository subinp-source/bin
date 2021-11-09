/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
import { Component, Inject, Type } from '@angular/core';

import { GENERIC_EDITOR_WIDGET_DATA } from '../../components/GenericEditorFieldComponent';
import { GenericEditorWidgetData } from '../../types';
import {
    FetchAllStrategy,
    ItemComponentData,
    ITEM_COMPONENT_DATA_TOKEN,
    SelectItem
} from '../../../../components/select';

@Component({
    selector: 'se-dropdown-item-printer',
    template: `
        <span>{{ data.item.label }}</span>
    `
})
export class DropdownItemPrinterComponent {
    constructor(@Inject(ITEM_COMPONENT_DATA_TOKEN) public data: ItemComponentData) {}
}

@Component({
    selector: 'se-dropdown-wrapper',
    template: `
        <se-select
            [id]="data.field.qualifier"
            class="se-generic-editor-dropdown"
            [(model)]="data.model[data.qualifier]"
            [searchEnabled]="false"
            [fetchStrategy]="fetchStrategy"
            [itemComponent]="itemComponent"
            [placeholder]="'se.genericeditor.sedropdown.placeholder'"
        ></se-select>
    `
})
export class DropdownComponent {
    public itemComponent: Type<any> = DropdownItemPrinterComponent;
    public fetchStrategy: {
        fetchAll: FetchAllStrategy<SelectItem>;
    };

    constructor(@Inject(GENERIC_EDITOR_WIDGET_DATA) public data: GenericEditorWidgetData<any>) {
        const {
            field: { options }
        } = data;
        this.fetchStrategy = {
            fetchAll: () => {
                return Promise.resolve(options as SelectItem[]);
            }
        };
    }
}
