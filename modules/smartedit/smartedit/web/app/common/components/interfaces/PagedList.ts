/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IDropdownMenuItem } from 'smarteditcommons/components/dropdown/dropdownMenu';

/**
 * @ngdoc interface
 * @name smarteditCommonsModule.interface:PagedList
 *
 * @description
 * Base PagedList interface for `ClientPagedList` and `DynamicPagedList`.
 */
export interface PagedList {
    /**
     * @ngdoc property
     * @name smarteditCommonsModule.interface:PagedList#displayCount
     * @propertyOf smarteditCommonsModule.interface:PagedList
     * @description
     * If set to true the size of the filtered collection will be displayed.
     */
    displayCount: boolean;

    /**
     * @ngdoc property
     * @name smarteditCommonsModule.interface:PagedList#dropdownItems
     * @propertyOf smarteditCommonsModule.interface:PagedList
     * @description
     * dropdownItems, {@link smarteditCommonsModule.interface:IDropdownMenuItem IDropdownMenuItem}
     * If provided the DropdownMenu will be displayed
     */
    dropdownItems?: IDropdownMenuItem[];

    /**
     * @ngdoc property
     * @name smarteditCommonsModule.interface:PagedList#itemsPerPage
     * @propertyOf smarteditCommonsModule.interface:PagedList
     * @description
     * The number of items to display per page.
     */
    itemsPerPage: number;

    /**
     * @ngdoc property
     * @name smarteditCommonsModule.interface:PagedList#reversed
     * @propertyOf smarteditCommonsModule.interface:PagedList
     * @description
     * If set to true, the list will be sorted descending.
     */
    reversed: boolean;

    /**
     * @ngdoc property
     * @name smarteditCommonsModule.interface:PagedList#sortBy
     * @propertyOf smarteditCommonsModule.interface:PagedList
     * @description
     * The column name to sort the results.
     */
    sortBy: string;
}
