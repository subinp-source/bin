/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    Input,
    OnChanges,
    SimpleChanges
} from '@angular/core';
import { SlicePipe } from '@angular/common';
import { TypedMap } from '@smart/utils';

import { FilterByFieldPipe, StartFromPipe } from '../../pipes';
import { IDropdownMenuItem } from '../dropdown/dropdownMenu';
import { SeDowngradeComponent } from '../../di';
import { CompileHtmlNgController } from '../../directives';
import { ClientPagedList, ClientPagedListColumnKey, ClientPagedListItem } from './interfaces';
import { objectUtils } from '../../utils';

/**
 * @ngdoc component
 * @name smarteditCommonsModule.module:ClientPagedListModule.component:ClientPagedListComponent
 * @description
 * Component responsible for displaying a client-side paginated list of items as a text or custom components.
 * It allows the user to search and sort the list.
 *
 * @param {Object[]} items {@link smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#properties_items items}
 * @param {Object[]} keys {@link smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#properties_keys keys}
 * @param {number} itemsPerPage {@link smarteditCommonsModule.interface:PagedList#properties_itemsPerPage itemsPerPage}
 * @param {string} [sortBy] Optional {@link smarteditCommonsModule.interface:PagedList#properties_sortBy sortBy}
 * @param {boolean} [reversed=false] {@link smarteditCommonsModule.interface:PagedList#properties_reversed reversed}
 * @param {string} [query] Optional {@link smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#properties_query query}
 * @param {boolean} [displayCount=false] {@link smarteditCommonsModule.interface:PagedList#properties_displayCount displayCount}
 * @param {Object[]} [dropdownItems] Optional {@link smarteditCommonsModule.interface:PagedList#properties_dropdownItems dropdownItems}
 * @param {string[]} [itemFilterKeys] Optional {@link smarteditCommonsModule.module:ClientPagedListModule.interface:ClientPagedList#properties_itemFilterKeys itemFilterKeys}
 *
 */
@SeDowngradeComponent()
@Component({
    selector: 'se-client-paged-list',
    changeDetection: ChangeDetectionStrategy.OnPush,
    providers: [FilterByFieldPipe, StartFromPipe, SlicePipe],
    templateUrl: './ClientPagedListComponent.html'
})
export class ClientPagedListComponent implements ClientPagedList, OnChanges {
    @Input() items: ClientPagedListItem[];
    @Input() itemFilterKeys: string[];
    @Input() keys: ClientPagedListColumnKey[];
    @Input() itemsPerPage: number;
    @Input() sortBy: string;
    @Input() reversed: boolean = false;
    @Input() query: string;
    @Input() displayCount: boolean = false;
    @Input() dropdownItems: IDropdownMenuItem[];

    public totalItems: number = 0;
    /**
     * Pagination page number
     */
    public currentPage: number = 1;
    public columnWidth: number;
    public columnToggleReversed: boolean;
    public headersSortingState: TypedMap<boolean> = {};
    public visibleSortingHeader: string;

    public compileHtmlNgController: CompileHtmlNgController;

    public filteredItems: ClientPagedListItem[];

    constructor(
        private cdr: ChangeDetectorRef,
        private filterByFieldPipe: FilterByFieldPipe,
        private startFromPipe: StartFromPipe,
        private slicePipe: SlicePipe
    ) {}

    ngOnChanges(changes: SimpleChanges) {
        if (changes.items || changes.query || changes.itemFilterKeys || changes.itemsPerPage) {
            if (changes.query) {
                this.currentPage = 1;
            }
            this.filteredItems = this.filterItems();
            this.totalItems =
                changes.query && changes.query.currentValue
                    ? this.filteredItems.length
                    : this.items.length;
        }

        if (changes.keys) {
            this.columnWidth = 100 / (this.keys.length || 1);
        }

        if (changes.reversed) {
            this.columnToggleReversed = this.reversed;
        }

        if (changes.sortBy) {
            this.headersSortingState = {
                ...this.headersSortingState,
                [this.sortBy]: this.columnToggleReversed
            };
            this.visibleSortingHeader = this.sortBy;

            this.items = objectUtils.sortBy(this.items, this.sortBy, this.columnToggleReversed);
            this.filteredItems = this.filterItems();
        }
    }

    public keysTrackBy(_index: number, key: ClientPagedListColumnKey): string {
        return key.property;
    }

    public onOrderByColumn(columnKeyProp: string): void {
        this.columnToggleReversed = !this.columnToggleReversed;
        this.headersSortingState[columnKeyProp] = this.columnToggleReversed;
        this.visibleSortingHeader = columnKeyProp;
        this.items = objectUtils.sortBy(this.items, columnKeyProp, this.columnToggleReversed);
        this.filteredItems = this.filterItems();
        this.cdr.detectChanges();
    }

    public onCurrentPageChange(page: number): void {
        this.currentPage = page;
        this.filteredItems = this.filterItems();
        this.cdr.detectChanges();
    }

    private filterItems(): ClientPagedListItem[] {
        const filterKeys = this.itemFilterKeys || [];
        const filteredItems = this.filterByFieldPipe.transform<ClientPagedListItem>(
            this.items,
            this.query,
            filterKeys
        );

        const startFromIndex = (this.currentPage - 1) * this.itemsPerPage;
        const startFromItems = this.startFromPipe.transform<ClientPagedListItem>(
            filteredItems,
            startFromIndex
        );

        const slicedItems = this.slicePipe.transform<ClientPagedListItem>(
            startFromItems,
            0,
            this.itemsPerPage
        );
        return slicedItems;
    }
}
