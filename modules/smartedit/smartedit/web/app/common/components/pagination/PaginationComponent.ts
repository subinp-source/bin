/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, EventEmitter, Input, Output } from '@angular/core';

import { SeDowngradeComponent } from '../../di';

/**
 * @ngdoc component
 * @name smarteditCommonsModule.component:PaginationComponent
 * @element se-pagination
 *
 * @description
 * The SmartEdit component that provides pagination by providing a visual pagination bar and buttons/numbers to navigate between pages.
 *
 *
 * @param {number} totalItems The total number of items.
 * @param {boolean=} displayTotalItems Whether to display the total number of items.
 * @param {number} itemsPerPage The total number of items per page.
 * @param {number} currentPage The current page number.
 * @param {function=} onChange The function that is called with the current page number when either a button or page number is clicked. The invoker
 * can bind this to a custom function to act and fetch results based on new page number.
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-pagination',
    templateUrl: './PaginationComponent.html'
})
export class PaginationComponent {
    @Input() totalItems: number;
    @Input() displayTotalItems: boolean = false;
    @Input() itemsPerPage: number;
    @Input() currentPage: number;

    @Output() onChange: EventEmitter<number> = new EventEmitter();

    public onPageChanged(page: number): void {
        this.currentPage = page;
        this.onChange.emit(page);
    }
}
