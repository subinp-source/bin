/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
/* forbiddenNameSpaces angular.module:false */
import { Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    DataTableComponentData,
    DataTableConfig,
    DynamicPagedListColumnKey,
    DATA_TABLE_COMPONENT_DATA,
    SeDowngradeComponent,
    SeEntryModule,
    SharedComponentsModule,
    TypedMap
} from 'smarteditcommons';

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <se-data-table [items]="items" [columns]="columns" [config]="config"> </se-data-table>
        </div>
    `
})
export class AppRootComponent {
    public config: DataTableConfig = {
        sortBy: 'item_1',
        reversed: false,
        injectedContext: { id: 'injectedId' }
    };
    public items: TypedMap<any> = [
        {
            col_1: 'col_1_row_1',
            col_2: 'col_2_row_1',
            col_3: 'col_3_row_1'
        },
        {
            col_1: 'col_1_row_2',
            col_2: 'col_2_row_2',
            col_3: 'col_3_row_2'
        },
        {
            col_1: 'col_1_row_3',
            col_2: 'col_2_row_3',
            col_3: 'col_3_row_3'
        }
    ];
    public columns: DynamicPagedListColumnKey[] = [
        {
            i18n: 'col_1',
            property: 'col_1',
            sortable: true,
            renderer: () => `
                <div id="renderer">
                    <span id="{{$ctrl.config.injectedContext.id}}">{{ item[column.property] }}</span>
                </div>
            `
        },
        {
            i18n: 'col_2',
            property: 'col_2',
            sortable: true,
            component: RenderedComponent
        },
        {
            i18n: 'col_3',
            property: 'col_3',
            sortable: true
        }
    ];
}

@Component({
    selector: 'my-selector',
    template: `
        <div>{{ data.item[data.column.property] }}</div>
    `
})
class RenderedComponent {
    constructor(@Inject(DATA_TABLE_COMPONENT_DATA) public data: DataTableComponentData) {}
}

@SeEntryModule('dataTableApp')
@NgModule({
    imports: [SharedComponentsModule, CommonModule],
    declarations: [AppRootComponent, RenderedComponent],
    entryComponents: [AppRootComponent, RenderedComponent]
})
export class DataTableAppNg {}
