/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Type } from '@angular/core';

/**
 * @ngdoc interface
 * @name smarteditCommonsModule.interface:IDropdownMenuItem
 * @description
 * Interface of dropdownItem Object for {@link smarteditCommonsModule.component:DropdownMenuComponent DropdownMenuComponent} component.
 */
export interface IDropdownMenuItem {
    key?: string;
    icon?: string;
    /**
     * @description
     * HTML string that will be rendered.
     *
     * Either one of callback, template, templateUrl or component must be present.
     * @deprecated since 2005, use `component`
     */
    template?: string;
    /**
     * @description
     * Same as template but instead of passing a string, pass an URL to an HTML file.
     *
     * Either one of callback, template, templateUrl or component must be present.
     * @deprecated since 2005, use `component`
     */
    templateUrl?: string;
    /**
     * @description
     * Component that will be rendered
     *
     * Either one of callback, template, templateUrl or component must be present.
     */
    component?: Type<any>;
    customCss?: string;
    /**
     * @description
     *
     * When provided, dropdownItem will be rendered by default {@link smarteditCommonsModule.component:DropdownMenuItemDefaultComponent Component} component.
     * This function will be called with `DropdownMenuComponent.selectedItem` as an argument when dropdownItem has been clicked.
     *
     * Either one of callback, template, templateUrl or component must be present.
     */
    callback?(...args: any[]): void;
    /**
     * @description
     *
     * Function that returns condition for rendering the dropdownItem
     */
    condition?(...args: any[]): boolean;
}
