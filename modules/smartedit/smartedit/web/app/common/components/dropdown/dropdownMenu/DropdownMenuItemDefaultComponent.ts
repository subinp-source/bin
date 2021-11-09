/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ChangeDetectionStrategy, Component, Inject, InjectionToken } from '@angular/core';
import { IDropdownMenuItem } from './IDropdownMenuItem';

interface IDropdownMenuItemData {
    dropdownItem: IDropdownMenuItem;
    selectedItem: any;
}
export const DROPDOWN_MENU_ITEM_DATA = new InjectionToken<IDropdownMenuItemData>(
    'DROPDOWN_MENU_ITEM_DATA'
);

/**
 * @ngdoc component
 * @name smarteditCommonsModule.component:DropdownMenuItemDefaultComponent
 *
 * @description
 * Component that represents a default Dropdown Menu Item.
 * Rendered by DropdownMenuComponent when Dropdown Menu Item has `callback` function.
 */
@Component({
    selector: 'se-dropdown-menu-item-default',
    changeDetection: ChangeDetectionStrategy.OnPush,
    templateUrl: './DropdownMenuItemDefaultComponent.html'
})
export class DropdownMenuItemDefaultComponent {
    constructor(@Inject(DROPDOWN_MENU_ITEM_DATA) public data: IDropdownMenuItemData) {}
}
