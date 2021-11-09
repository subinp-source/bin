/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ChangeDetectionStrategy,
    Component,
    EventEmitter,
    Input,
    Output,
    Type
} from '@angular/core';

import { ListItemKeyboardControlDirective } from '@smart/utils';
import { FetchPageStrategy, SelectDisableChoice, SelectItem } from '../interfaces';
import { SelectComponent } from '../SelectComponent';

/**
 * Represents Dropdown List for Select Component
 *
 * @internal
 */
@Component({
    selector: 'se-select-list',
    changeDetection: ChangeDetectionStrategy.OnPush,
    host: {
        '[class.se-select-list]': 'true'
    },
    templateUrl: './SelectListComponent.html'
})
export class SelectListComponent<T extends SelectItem> {
    @Input() id: string;
    @Input() isPagedDropdown = false;
    @Input() fetchPage: FetchPageStrategy<T>;
    @Input() search: string;
    @Input() items: T[];
    @Input() selected: T | T[];
    /** Whether to display selected item */
    @Input() excludeSelected = false;
    @Input() disableChoiceFn?: SelectDisableChoice<T>;
    @Input() itemComponent: Type<any>;
    @Input() selectComponentCtx: SelectComponent<T>;

    @Output() optionClick = new EventEmitter<T>();
    @Output() infiniteScrollItemsChange = new EventEmitter<T>();

    public readonly infiniteScrollingPageSize: number = 10;

    public keyboardControlDisabledPredicate(item: ListItemKeyboardControlDirective) {
        return item.getElement().classList.contains('is-disabled');
    }

    public itemTrackBy(_: any, item: SelectItem): string {
        return item.id;
    }

    public isItemSelected(item: T): boolean {
        const isMultiSelect = Array.isArray(this.selected);

        if (!this.selected || (isMultiSelect && (this.selected as T[]).length === 0)) {
            return false;
        }

        return isMultiSelect
            ? !!(this.selected as T[]).find((selectedItem) => selectedItem.id === item.id)
            : item.id === (this.selected as T).id;
    }

    public onOptionClick(event: MouseEvent, item: T): void {
        if (this.isItemDisabled(item)) {
            // to prevent triggering click handler of @HostListener on Select Component for Multi Select
            event.stopPropagation();
            return;
        }
        this.optionClick.emit(item);
    }

    public onEnterKeydown(itemIndex: number): void {
        const items = this.getItems();
        const item = items[itemIndex];
        this.optionClick.emit(item);
    }

    public isItemDisabled(item: T): boolean {
        return this.disableChoiceFn ? this.disableChoiceFn(item) : false;
    }

    public onInfiniteScrollItemsChange(): void {
        this.infiniteScrollItemsChange.emit();
    }

    /** Returns items that are displayed in the list */
    private getItems(): T[] {
        if (this.excludeSelected) {
            return this.items.filter((item) => !this.isItemSelected(item));
        } else {
            return this.items;
        }
    }
}
