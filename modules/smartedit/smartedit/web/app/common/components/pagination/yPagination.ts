/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { SeComponent } from '../../di';

/**
 * @ngdoc directive
 * @name smarteditCommonsModule.component:yPagination
 * @scope
 * @deprecated since 2005
 * @restrict E
 * @element y-pagination
 *
 * @description
 * Deprecated, use {@link smarteditCommonsModule.component:PaginationComponent PaginationComponent}
 * The SmartEdit component that provides pagination by providing a visual pagination bar and buttons/numbers to navigate between pages.
 *
 * You need to bind the current page value to the ng-model property of the component.
 *
 * @param {<Number} totalItems The total number of items.
 * @param {<Number} itemsPerPage The total number of items per page.
 * @param {<String=} boundaryLinks Whether to display First / Last buttons. Defaults to false.
 * @param {<Number} ngModel The current page number.
 * @param {&Function} onChange The function that is called with the current page number when either a button or page number is clicked. The invoker
 * can bind this to a custom function to act and fetch results based on new page number.
 */

@SeComponent({
    selector: 'y-pagination',
    templateUrl: 'yPaginationTemplate.html',
    require: {
        exposedModel: 'ngModel'
    },
    inputs: ['totalItems', 'itemsPerPage', 'boundaryLinks', 'onChange:&']
})
export class YPaginationComponent {
    public totalItems: number;
    public itemsPerPage: number;
    public onChange: (data: { $currentPage: number }) => void;
    public currentPage: number;
    public boundaryLinks: string;

    private exposedModel: angular.INgModelController;

    $onInit() {
        // in order to propagate down changes to ngModel from the parent controller
        this.exposedModel.$viewChangeListeners.push(() => this.onPageChange());
        this.exposedModel.$render = () => this.onPageChange();
    }

    public onPageChange(): void {
        this.currentPage = this.exposedModel.$modelValue;
    }

    public pageChanged() {
        this.exposedModel.$setViewValue(this.currentPage);
        this.onChange({
            $currentPage: this.currentPage
        });
    }
}
