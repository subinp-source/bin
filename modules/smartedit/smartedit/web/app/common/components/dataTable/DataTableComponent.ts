/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { Component, EventEmitter, InjectionToken, Input, OnInit, Output } from '@angular/core';
import { TypedMap } from '@smart/utils';

import { SeDowngradeComponent } from '../../di';
import { SortDirections } from '../../utils';
import { DynamicPagedListColumnKey } from '../dynamicPagedList/interfaces';

export interface DataTableConfig {
    sortBy: string;
    reversed: boolean;
    injectedContext: any;
}

export interface DataTableComponentData {
    item: TypedMap<any>;
    column: DynamicPagedListColumnKey;
}

/**
 * @ngdoc object
 * @name smarteditCommonsModule.object:DATA_TABLE_COMPONENT_DATA
 *
 * @description
 * An injection token used to retrieve information about data table column and item from within rendered component.
 */

export const DATA_TABLE_COMPONENT_DATA = new InjectionToken('DATA_TABLE_COMPONENT_DATA');
/**
 * @ngdoc component
 * @name smarteditCommonsModule.component:DataTableComponent
 * @element se-data-table
 *
 * @description
 * Component used to print the provided data in the form of table and also enable sorting by column.
 *
 * @param {<Array} columns An array containing the properties of the column.
 * @param {<String} columns.key A unique identification key for the column
 * @param {<String} columns.i18n A printable column name.
 * @param {<Function} columns.renderer Deprecated, use component. A renderer function that returns a template for the column value.
 * @param {Type<any>} columns.component A component to be rendered within the column. Can be injected with {@link smarteditCommonsModule.object:DATA_TABLE_COMPONENT_DATA DATA_TABLE_COMPONENT_DATA}
 * token which provides data about column and item
 * @param {<Boolean} columns.sortable Boolean that determines if the column is sortable or not. [Default = false]
 * @param {<Object} config The config object that contains reversed, sortBy params.
 * @param {<Number} items The items to be displayed
 * @param {&Function} onSortColumn The function that is called with the column key and the sort direction every time sorting is done. The invoker
 * can bind this to a custom function to act and fetch results based on new data.
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-data-table',
    templateUrl: './DataTableComponent.html'
})
export class DataTableComponent implements OnInit {
    @Input() public columns: DynamicPagedListColumnKey[];
    @Input() public config: DataTableConfig;
    @Input() public items: TypedMap<any>[];
    @Output() public onSortColumn: EventEmitter<{
        $columnKey: DynamicPagedListColumnKey;
        $columnSortMode: SortDirections;
    }> = new EventEmitter();

    public internalSortBy: string;
    public currentPage: number;
    public columnWidth: number;
    public columnToggleReversed: boolean;
    public columnSortMode: SortDirections;
    public headersSortingState: TypedMap<boolean> = {};
    public visibleSortingHeader: string;

    ngOnInit() {
        this._validateInput();
        this._configure();
    }

    public sortColumn(columnKey: DynamicPagedListColumnKey): void {
        if (columnKey.sortable) {
            this.columnToggleReversed = !this.columnToggleReversed;
            this.headersSortingState[columnKey.property] = this.columnToggleReversed;
            this.visibleSortingHeader = columnKey.property;

            this.currentPage = 1;
            this.internalSortBy = columnKey.property;
            this.columnSortMode = this.columnToggleReversed
                ? SortDirections.Descending
                : SortDirections.Ascending;

            this.onSortColumn.emit({
                $columnKey: columnKey,
                $columnSortMode: this.columnSortMode
            });
        }
    }

    private _configure(): void {
        this.columnWidth = 100 / this.columns.length;
        this.columnToggleReversed = this.config.reversed;
        this.columnSortMode = this.config.reversed
            ? SortDirections.Descending
            : SortDirections.Ascending;

        this.headersSortingState[this.config.sortBy] = this.config.reversed;
        this.visibleSortingHeader = this.config.sortBy;
    }

    private _validateInput(): void {
        if (!Array.isArray(this.columns)) {
            throw new Error('Columns must be an array');
        }

        if (
            this.columns.some(
                (column: DynamicPagedListColumnKey) => !!column.renderer && !!column.component
            )
        ) {
            throw new Error('Columns must have either renderer or a component');
        }

        if (!(this.config instanceof Object)) {
            throw new Error('Config must be an object');
        }
    }
}
