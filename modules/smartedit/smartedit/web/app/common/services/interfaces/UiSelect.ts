/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

export interface UiSelect<T> {
    $animate: angular.animate.IAnimateService;
    $element: JQuery;
    $filter: angular.IFilterService;
    activate: () => void;
    activeIndex: number;
    allowClear: boolean;
    baseTitle: string;
    choiceGrouped: (event: Event) => void;
    clear: (event: Event) => void;
    close: () => void;
    search: string;
    disableChoiceExpression: string;
    dropdownPosition: string;
    findGroupByName: () => string;
    focus: boolean;
    focusInput: JQuery;
    focusSearchInput: () => void;
    focusser: JQuery;
    focusserId: string;
    focusserTitle: string;
    generatedId: number;
    isActive: (event: Event) => boolean;
    isDisabled: (event: Event) => boolean;
    isEmpty: () => boolean;
    isGrouped: boolean;
    isLocked: () => boolean;
    itemProperty: string;
    items: T[];
    placeholder: string;
}
