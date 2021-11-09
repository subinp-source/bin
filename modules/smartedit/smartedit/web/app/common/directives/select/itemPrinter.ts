/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { SeComponent } from '../../di';
import { YSelectComponent } from './ySelect';

/** @internal */
export interface ItemPrinterScope<T extends { id: string }> extends angular.IScope {
    selected: boolean;
    item: string | string[];
    ySelect: YSelectComponent<T>;
}

/** @internal */
@SeComponent({
    selector: 'item-printer',
    template: `<div class="se-item-printer" ng-include="$ctrl.templateUrl"></div>`,
    replace: false,
    transclude: false,
    inputs: ['templateUrl', 'model'],
    require: {
        ySelect: '^ySelect'
    }
})
export class ItemPrinterComponent<T extends { id: string }> {
    public templateUrl: string;

    private ySelect: YSelectComponent<T>;
    private model: string | string[];

    constructor(private $scope: ItemPrinterScope<T>) {
        this.$scope.selected = true;
    }

    $onChanges = () => {
        /* needs to bind it scope and not controller in order for the templates required by API
         * to be agnostic of whether they are invoked within ui-select-coices or ui-select-match of ui-select
         */

        this.$scope.item = this.model;
        this.$scope.ySelect = this.ySelect;
    };
}
