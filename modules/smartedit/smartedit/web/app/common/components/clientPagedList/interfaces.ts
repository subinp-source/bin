/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { InjectionToken, Type } from '@angular/core';
import { TypedMap } from '@smart/utils';

import { PagedList } from '../interfaces/PagedList';

export interface ClientPagedListCellComponentData {
    key: ClientPagedListColumnKey;
    item: ClientPagedListItem;
}

/**
 * @ngdoc object
 * @name smarteditCommonsModule.module:ClientPagedListModule.object:CLIENT_PAGED_LIST_CELL_COMPONENT_DATA
 *
 * @description
 * An Injection Token used to retrieve information about "item" and "key" from within rendered component.
 */
export const CLIENT_PAGED_LIST_CELL_COMPONENT_DATA_TOKEN = new InjectionToken<
    ClientPagedListCellComponentData
>('CLIENT_PAGED_LIST_CELL_COMPONENT_DATA_TOKEN');

/**
 * @ngdoc interface
 * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey
 *
 * @description
 * Column descriptor.
 */
export interface ClientPagedListColumnKey {
    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey#property
     * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey
     *
     * @description
     * The property must match at least one of the descriptors keys and will be used as the columns of the table.
     * Used for mapping `ClientPagedListItem` object key to the cell text.
     */
    property: string;
    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey#i18n
     * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey
     *
     * @description
     * Used for the column headers title.
     */
    i18n: string;
    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey#component
     * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey
     *
     * @description
     * A component that will be rendered for each cell of this column.
     * "item" and "key" are accessible via {@link smarteditCommonsModule.module:ClientPagedListModule.object:CLIENT_PAGED_LIST_CELL_COMPONENT_DATA CLIENT_PAGED_LIST_CELL_COMPONENT_DATA} Injection Token.
     *
     * ### Example
     *
     * ```ts
     * @Component({
     *  selector: 'my-client-paged-list-cell',
     *  template: `
     *    <div>{{ data.item[data.key.property] }}</div>
     *  `
     * })
     * export class MyClientPagedListCellComponent {
     *  constructor(
     *    @Inject(CLIENT_PAGED_LIST_CELL_COMPONENT_DATA_TOKEN)
     *    public data: ClientPagedListCellComponentData
     *  ) {}
     * }
     * ```
     */
    component?: Type<any>;
}

/**
 * @ngdoc interface
 * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListItem
 *
 * @description
 * Row item descriptor.
 * The Key corresponds to the column name where value is the text displayed in a cell.
 */
export interface ClientPagedListItem extends TypedMap<any> {
    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListItem#icon
     * @propertyof smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListItem
     *
     * @description
     * If provided an Icon will be displayed with a tooltip below.
     * Tooltip shows the text how many restrictions are applied for the Page.
     */
    icon?: {
        /**
         * @ngdoc property
         * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListItem#src
         * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListItem
         *
         * @description URL of an icon
         */
        src: string;
        /**
         * @ngdoc property
         * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListItem#numberOfRestrictions
         * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListItem
         *
         * @description
         * Number of restrictions that are displayed in the tooltip text.
         */
        numberOfRestrictions: number;
    };
}

/**
 * @ngdoc interface
 * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList
 *
 * @description
 * Required by ClientPagedListComponent
 */
export interface ClientPagedList extends PagedList {
    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#items
     * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList
     *
     * @description
     * An array of items: {@link smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListItem ClientPagedListItem} descriptors
     */
    items: ClientPagedListItem[];

    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#keys
     * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList
     *
     * @description
     * An array of keys: {@link smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey ClientPagedListColumnKey}
     */
    keys: ClientPagedListColumnKey[];

    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#itemFilterKeys
     * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList
     *
     * @description
     * An array of `ClientPagedListItem` object keys that will determine which fields the "filterByFieldPipe"
     * will use to filter through the items.
     */
    itemFilterKeys?: string[];

    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#renderers
     * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList
     * @deprecated since 2005, use {@link smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey##component ClientPagedListColumnKey#component}
     *
     * @description
     * An object that contains HTML renderers for specific keys property. A renderer is a function that returns a HTML string. This function has access to the current "item", "key" and the injected context(as $ctrl.injectedContext).
     */
    renderers?: TypedMap<(item: ClientPagedListItem, key: ClientPagedListColumnKey) => string>;

    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#injectedContext
     * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList
     * @deprecated since 2005, use {@link smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedListColumnKey#component ClientPagedListColumnKey#component}
     *
     * @description
     * An object that exposes values or functions to the directive. It can be used by the custom HTML renderers to bind a function to a click event for example.
     */
    injectedContext?: TypedMap<(...args: any[]) => any>;

    /**
     * @ngdoc property
     * @name smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#query
     * @propertyOf smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList
     *
     * @description
     * The query used to filter the list.
     */
    query: string;
}
