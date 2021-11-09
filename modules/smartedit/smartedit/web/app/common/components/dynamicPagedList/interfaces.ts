/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TypedMap } from '@smart/utils';
import { Page } from '../../dtos';
import { PagedList } from '../interfaces/PagedList';
import { Type } from '@angular/core';
import { IDropdownMenuItem } from '../dropdown/dropdownMenu/IDropdownMenuItem';

/**
 * @ngdoc interface
 * @name smarteditCommonsModule.interface:DynamicPagedListApi
 * @description
 * The dynamic paged list api object exposing public functionality
 */

export interface DynamicPagedListApi {
    /**
     * @ngdoc method
     * @name reloadItems
     * @methodOf  smarteditCommonsModule.interface:DynamicPagedListApi
     * @description
     * Function that reloads the items of the paged list.
     * @returns {Promise<Page<any>>}
     */

    reloadItems: () => Promise<Page<any>>;
}

export interface DynamicPagedListDropdownItem {
    template: string;
}

export interface DynamicPagedListColumnKey {
    i18n: string;
    property: string;
    sortable: boolean;
    renderer?: (...args: any[]) => string;
    component?: Type<any>;
}

export interface DynamicPagedListInjectedContext {
    dropdownItems?: IDropdownMenuItem[];
    onLink?: (uid: string) => void;
    permissionForDropdownItems?: string;
    uriContext?: TypedMap<string>;
}

/**
 * @ngdoc interface
 * @name smarteditCommonsModule.interface:DynamicPagedListConfig
 * @description
 * Configuration object interface for {@link smarteditCommonsModule.component:DynamicPagedListComponent DynamicPagedListComponent}
 */
export interface DynamicPagedListConfig extends PagedList {
    /**
     * @ngdoc property
     * @name keys
     * @description
     * An array of object(s) that detemine the properties of each column.
     * It requires a property - unique id of the column, i18n - displayable column name and sortable - optional flag that enables column sorting.
     * The properties must match one at least one of the descriptors' keys and will be used as the columns of the table. The related i18n keys are used for the column headers' title.
     * In order for sorting to work, the REST endpoint must support for sorting of that field.
     */
    keys: DynamicPagedListColumnKey[];

    /**
     * @ngdoc property
     * @name queryParams
     * @propertyOf smarteditCommonsModule.interface:DynamicPagedListConfig
     * @description
     * An object containing list of query params that needed to be passed to the uri.
     */
    queryParams: TypedMap<string>;

    /**
     * @ngdoc property
     * @name uri
     * @propertyOf smarteditCommonsModule.interface:DynamicPagedListConfig
     * @description
     * The uri to fetch the list from. The REST end point represented by uri must support paging and accept parameters such as currentPage, pageSize, mask and sort for fetching and fitering paged data.
     *
     * for example: If the uri = '/someuri', then the dynamic-paged-list component will fetch paged information by making call to the API such as:
     * ```
     * /someuri?currentPage=0&pageSize=10
     * /someuri?currentPage=0&pageSize=10&mask=home
     * /someuri?currentPage=0&pageSize=10&sort=name:asc
     * ```
     */
    uri: string;

    /**
     * @ngdoc property
     * @name renderers
     * @description
     * An object that contains HTML renderers for specific keys property. A renderer is a function that returns a HTML string. This function has access to the current item and the injected context(as $ctrl.config.injectedContext).
     */
    renderers: TypedMap<() => string>;

    /**
     * @ngdoc property
     * @name injectedContext
     * @description
     * An object that exposes values or functions to the directive. It can be used by the custom HTML renderers to bind a function to a click event for example.
     */
    injectedContext: DynamicPagedListInjectedContext;
}

/**
 * @ngdoc interface
 * @name dynamicPagedListModule.interface:OnGetApiEvent
 * @description
 * @deprecated since 2005
 * Event emitted by {@link  dynamicPagedListModule.directive:DynamicPagedListComponent DynamicPagedListComponent}. Exposes the dynamic paged list module's api object
 */

export type OnGetApiEvent = (data: { $api: DynamicPagedListApi }) => void;

/**
 * @ngdoc interface
 * @name dynamicPagedListModule.interface:OnItemsUpdateEvent
 * @description
 * @deprecated since 2005
 * Event emitted by {@link  dynamicPagedListModule.directive:DynamicPagedListComponent DynamicPagedListComponent}. Exposes the item list
 */

export type OnItemsUpdateEvent = (data: Page<any>) => void;

/**
 * @ngdoc interface
 * @name dynamicPagedListModule.interface:OnGetApiEvent
 * @description
 * Event emitted by {@link  dynamicPagedListModule.directive:DynamicPagedListComponent DynamicPagedListComponent}. Exposes the dynamic paged list module's api object
 */
