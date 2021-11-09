/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { SeComponent } from '../../di';
/**
 * @ngdoc overview
 * @name yDataTableModule
 * @description
 * # The yDataTableModule
 *
 * The yDataTableModule is used to print the input data in the form of a table and also allowing to sort by column.
 *
 */

/**
 * @ngdoc directive
 * @name yDataTableModule.directive:yDataTable
 * @scope
 * @restrict E
 * @deprecated since 2005
 * @element y-data-table
 *
 * @description
 * Deprecated, use {@link smarteditCommonsModule.component:DataTableComponent DataTableComponent}
 * Component used to print the provided data in the form of table and also enable sorting by column.
 *
 * @param {<Array} columns An array containing the properties of the column.
 * @param {<String} columns.key A unique identification key for the column
 * @param {<String} columns.i18n A printable column name.
 * @param {<Function} columns.renderer A renderer function that returns a template for the column value.
 * @param {<Boolean} columns.sortable Boolean that determines if the column is sortable or not. [Default = false]
 * @param {<Object} config The config object that contains reversed, sortBy params.
 * @param {<Number} items The items to be displayed
 * @param {&Function} onSortColumn The function that is called with the column key and the sort direction every time sorting is done. The invoker
 * can bind this to a custom function to act and fetch results based on new data.
 */

@SeComponent({
    selector: 'y-data-table',
    templateUrl: 'yDataTableTemplate.html',
    inputs: ['columns', 'config', 'items', 'onSortColumn:&']
})
export class YDataTableComponent {}
