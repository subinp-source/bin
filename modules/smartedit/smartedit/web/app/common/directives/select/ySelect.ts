/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import * as angular from 'angular';
import { LogService } from '@smart/utils';

import { SeComponent } from '../../di';
import { stringUtils, VALIDATION_MESSAGE_TYPES } from '../../utils';
import { UiSelect } from '../../services/interfaces';
import { FetchStrategy, SelectApi } from '../../components/select';

/**
 * @ngdoc object
 * @name ySelectModule.object:ySelectApi
 * @deprecated since 2005, use `SelectApi`
 */
export type YSelectApi = SelectApi;

/**
 * @ngdoc directive
 * @name ySelectModule.directive:ySelect
 * @scope
 * @restrict E
 * @element y-select
 * @deprecated since 2005, use `SelectComponent`
 *
 * @description
 * This component is a wrapper around ui-select directive and provides filtering capabilities for the dropdown menu that is customizable with an item template.
 * <br/>ySelect can work in both paged and non paged mode: providing either fetchAll or fetchPage function in the fetchStrategy will determine the flavour of the dropdown.
 *
 *
 * @param {@String=} id will be used to identify internal elements of ySelect for styling (and testing) purposes.
 * @param {<boolean=} controls Adds controls such as the magnifier and the remove button. Default is set to false.
 * @param {<Object} fetchStrategy strategy object containing the necessary functions for ySelect to populate the dropdown:
 * <b>Only one of either fetchAll or fetchPage must be defined.</b>
 * @param {<Function} fetchStrategy.fetchAll Function required to fetch all for a given optional mask.
 * fetchAll will be called without arguments upon initialization and with a mask every time the search section receives an input.
 * It must return a promise resolving to a list of items.
 * Every item must have a property "id" used for identification. If no itemTemplate is provided, these items will need to display a "label" property.
 * @param {<Function} fetchStrategy.fetchPage Function required to fetch a page for a given optional mask.
 * fetchPage must fulfill the contract of fetchPage from {@link yInfiniteScrollingModule.directive:yInfiniteScrolling yInfiniteScrolling}
 * It must return a promise resolving to a page of items as per {@link Page.object:Page Page}.
 * Every item must have a property "id" used for identification. If no itemTemplate is provided, these items will need to display a "label" property.
 * @param {<Function} fetchStrategy.fetchEntity Function to fetch an option by its identifier when we are in paged mode (fetchPage is defined) and the dropdown is initialized with a value.
 * @param {<Function=} disableChoiceFn A function to disable results in the drop-down. It is invoked for each item in the drop-down, with a single parameter, the item itself.
 * @param {<String=} placeholder the placeholder label or i18nKey that will be printed in the search section.
 * @param {<String=} itemTemplate the path to the template that will be used to display items in both the dropdown menu and the selection.
 * ItemTemplate has access to item, selected and the ySelect controller.
 * item is the item to print, selected is a boolean that is true when the template is used in the selection as opposed to the dropdown menu.
 * Default template will be:
 * <pre>
 * <span data-ng-bind-html="item.label | translate"></span>
 * </pre>
 * @param {<boolean=} keepModelOnReset. a non-paged dropdown: if the value is set to false, the widget will remove the selected entities in the model that no longer match the values available on the server.
 * For a paged dropdown: After a standard reset, even if keepModelOnReset is set to false,  the widget will not be able to remove the selected entities in the model
 * that no longer match the values available on the server. to force the widget to remove any selected entities, you must call reset(true).
 * @param {<boolean=} multiSelect The property specifies whether ySelect is multi-selectable.
 * @param {=Function=} reset A function that will be called when ySelect is reset.
 * @param {<boolean=} isReadOnly renders ySelect as disabled field.
 * @param {<String=} resultsHeaderTemplate the template that will be used on top of the result list.
 * @param {<String=} resultsHeaderTemplateUrl the path to the template what will be used on top of the result list.
 * @param {<String=} resultsHeaderLabel the label that will be displayed on top of the result list.
 * Only one of resultsHeaderTemplate, resultsHeaderTemplateUtl, and resultsHeaderLabel shall be passed.
 * @param {<boolean=} resetSearchInput Clears the search box after selecting an option.
 * @param {=Function=} onRemove A function that will be called when item was removed from selection, function is called with two arguments $item and $model
 * @param {=Function=} onSelect A function that will be called when item was selected, function is called with two arguments $item and $model
 * @param {=Function=} init A function that will be called when component is initialized, function is called with one argument $select
 * @param {=Function=} keyup A function that will be called on keyup event in search input, function is called with two arguments $event and $select.search
 * @param {&Function=} getApi Exposes the ySelect's api object. See {@link ySelectModule.object:ySelectApi ySelectApi} for more information.
 * @param {boolean} showRemoveButton Adds remove button
 */

@SeComponent({
    template: "<div data-compile-html='$ctrl.result'></div>",
    transclude: true,
    require: {
        exposedModel: 'ngModel'
    },
    inputs: [
        'id:@',
        'fetchStrategy',
        'onChange',
        'controls',
        'multiSelect',
        'keepModelOnReset',
        'reset:=?',
        'isReadOnly',
        'resultsHeaderTemplate',
        'resultsHeaderTemplateUrl',
        'resultsHeaderLabel',
        'disableChoiceFn',
        'placeholder',
        'itemTemplate',
        'searchEnabled',
        'resetSearchInput',
        'onRemove',
        'onSelect',
        'init',
        'keyup',
        'showRemoveButton',
        'getApi:&'
    ]
})
export class YSelectComponent<T extends { id: string }> {
    public id: string;
    public controls: boolean;
    public fetchStrategy: FetchStrategy<T>;
    public disableChoiceFn: (item: T) => boolean;
    public placeholder: string;
    public itemTemplate: string;
    public keepModelOnReset: boolean;
    public multiSelect: boolean;
    public reset: (isForceReset: boolean) => void;
    public isReadOnly: boolean;
    public result: string;
    public resultsHeaderTemplateUrl: string;
    public resultsHeaderTemplate: string;
    public resultsHeaderLabel: string = 'se.yselect.options.inactiveoption.label';
    public resetSearchInput: boolean;
    public onRemove: (item: T, model: string[] | string) => void;
    public onSelect: (item: T, model: string[] | string) => void;
    public onChange: () => void;
    public init: ($select: UiSelect<T>) => void;
    public keyup: (event: Event, search: string) => void;
    public validationState: string;
    public theme: string;
    public actionableSearchItemTemplateConfig: { templateUrl: string };
    public api: YSelectApi = {
        setValidationState: (validationState: string) => {
            this.validationState = validationState;
        },
        resetValidationState: () => {
            this.validationState = undefined;
        }
    };
    public getApi: (api: { $api: YSelectApi }) => void;
    public items: T[];
    public searchEnabled: boolean;
    public exposedModel: angular.INgModelController;
    public model: string[] | string;
    public showRemoveButton: boolean;

    constructor(
        private logService: LogService,
        private $templateCache: angular.ITemplateCacheService
    ) {}

    // Initialization
    $onInit() {
        // this.items represent the options available in the control to choose from.
        // this.model represents the item(s) currently selected in the control. If the control is using the multiSelect
        // flag then the model is an array; otherwise it's a single object.
        this.items = [];
        this.searchEnabled = this.searchEnabled !== false;
        this.resetSearchInput = this.resetSearchInput !== false;

        // in order to propagate down changes to ngModel from the parent controller
        this.exposedModel.$viewChangeListeners.push(() => this.syncModels());
        this.exposedModel.$render = () => this.syncModels();

        this.reset = (forceReset: boolean) => {
            this.items.length = 0;

            if (forceReset) {
                this.resetModel();
            }

            return this.$onChanges();
        };

        if (typeof this.getApi === 'function') {
            this.getApi({
                $api: this.api
            });
        }
    }

    $onChanges(changes?: angular.IOnChangesObject) {
        let result: Promise<any> = Promise.resolve(null);

        this.isValidConfiguration();
        this.updateControlTemplate();

        /* we must initialize the list to contain at least the selected item
         * if a fetchEntity has been provided, it will be used
         * if no fetchEntity was provided, we resort to finding a match in the result from fetchAll
         * if we fail to find a match, the directive throws an error to notify that a fetchEntity is required
         */

        if (!this.items || this.items.length === 0) {
            if (!this.isPagedDropdown()) {
                result = this.internalFetchAll();
            } else if (this.fetchStrategy.fetchEntity || this.fetchStrategy.fetchEntities) {
                if (!this.isModelEmpty()) {
                    result = this.internalFetchEntities();
                }
            } else {
                throw new Error(
                    'could not initialize dropdown of ySelect, neither fetchEntity, fetchEntities, nor fetchAll were specified'
                );
            }
        }

        if (changes && changes.fetchStrategy) {
            this._updateChild();
        }

        return result;
    }

    // Event Listeners

    /*
     * This function is called whenever the value in the ui-select changes from an external source (e.g., like
     * the user making a selection).
     * NOTE: This is not triggered if the model is changed programatically.
     */

    public syncModels(): void {
        this.model = this.exposedModel.$modelValue;
        this.$onChanges();
        this.internalOnChange();
    }

    public clear($select: UiSelect<T>, $event: Event): void {
        $event.preventDefault();
        $event.stopPropagation();
        delete this.model;
        this.internalOnChange();
    }

    public showResultHeader(): boolean {
        return this.searchEnabled && this.items && this.items.length > 0;
    }

    public getActionableTemplateUrl(): string {
        return this.actionableSearchItemTemplateConfig
            ? this.actionableSearchItemTemplateConfig.templateUrl
            : '';
    }

    // in case of paged dropdown, the triggering of refresh is handled by yInfiniteScrolling component part of the pagedSelect2/choices.tpl.html template
    public refreshOptions(mask: string): void {
        if (this.fetchStrategy.fetchAll) {
            (this.fetchStrategy.fetchAll(mask) as Promise<T[]>).then((items: T[]) => {
                this.items = items;
            });
        }
    }

    public internalOnRemove(item: T, model: string[] | string): void {
        if (this.onRemove) {
            this.onRemove(item, model);
        }
    }

    public internalOnSelect(item: T, model: string[] | string): void {
        if (this.onSelect) {
            this.onSelect(item, model);
        }
    }

    public internalInit(select: UiSelect<T>): void {
        if (this.init) {
            this.init(select);
        }

        (select as any).searchInput.on('keydown', (event: KeyboardEvent) => {
            if (event.key === 'Backspace' || event.key === 'Delete') {
                event.stopImmediatePropagation();
            }
        });
    }

    public internalKeyup(event: Event, selectSearch: string): void {
        if (this.keyup) {
            this.keyup(event, selectSearch);
        }
    }

    /*
     * This method is used to propagate to the parent controller the changes made to the model programatically inside
     * this component.
     */

    public internalOnChange() {
        // in order to propagate up changes to ngModel into parent controller

        this.exposedModel.$setViewValue(this.model);

        if (this.onChange) {
            this.onChange();
        }
    }

    public internalFetchAll() {
        return (this.fetchStrategy.fetchAll() as Promise<T[]>).then((items: T[]) => {
            this.items = items;

            if (this.model) {
                const result = this.multiSelect
                    ? (this.model as string[]).every(
                          (key: string) => !!this.items.find((item: T) => item.id === key)
                      )
                    : items.find((item: T) => item.id === (this.model as string));

                if (!result) {
                    this.logService.debug(
                        '[ySelect - ' +
                            this.id +
                            '] fetchAll was used to fetch the option identified by ' +
                            this.model +
                            ' but failed to find a match'
                    );
                }

                this.updateModelIfNecessary();
            }

            this.internalOnChange();
        });
    }

    internalFetchEntities(): Promise<any> {
        let promise: Promise<T[]>;

        if (!this.multiSelect) {
            promise = this.fetchEntity(this.model as string).then((item: T) => [item]);
        } else {
            if (this.fetchStrategy.fetchEntities) {
                promise = (this.fetchStrategy.fetchEntities(this.model as string[]) as Promise<
                    T[]
                >).then((items: T[]) => {
                    if (items.length !== this.model.length) {
                        this.logService.debug(
                            '!fetchEntities was used to fetch the options identified by ' +
                                this.model +
                                ' but failed to find all matches'
                        );
                    }

                    return items;
                });
            } else {
                const promiseArray = (this.model as string[]).map((entryId: string) =>
                    this.fetchEntity(entryId)
                );

                promise = Promise.all(promiseArray);
            }
        }

        return promise.then((result: T[]) => {
            this.items = result
                .filter((item: T) => item !== null)
                .map((item: any) => {
                    delete item.$promise;
                    delete item.$resolved;
                    item.technicalUniqueId = stringUtils.encode(item);
                    return item;
                });

            this.updateModelIfNecessary();

            this.internalOnChange();
        });
    }

    public fetchEntity(entryId: string): Promise<T> {
        return (this.fetchStrategy.fetchEntity(entryId) as Promise<T>).then((item: T) => {
            if (!item) {
                this.logService.debug(
                    'fetchEntity was used to fetch the option identified by ' +
                        item +
                        ' but failed to find a match'
                );
            }

            return item;
        });
    }

    public updateModelIfNecessary() {
        if (!this.keepModelOnReset) {
            if (this.multiSelect) {
                this.model = (this.model as string[]).filter((modelKey: string) =>
                    this.items.find((item: T) => item && item.id === modelKey)
                );
            } else {
                const result = this.items.filter((item: T) => item.id === (this.model as string));
                this.model = result.length > 0 ? this.model : null;
            }
        }
    }

    // Helper functions
    public isValidConfiguration(): void {
        if (!this.fetchStrategy.fetchAll && !this.fetchStrategy.fetchPage) {
            throw new Error('neither fetchAll nor fetchPage have been specified in fetchStrategy');
        }
        if (this.fetchStrategy.fetchAll && this.fetchStrategy.fetchPage) {
            throw new Error(
                'only one of either fetchAll or fetchPage must be specified in fetchStrategy'
            );
        }
        if (
            this.fetchStrategy.fetchPage &&
            this.model &&
            !this.fetchStrategy.fetchEntity &&
            !this.fetchStrategy.fetchEntities
        ) {
            throw new Error(
                `fetchPage has been specified in fetchStrategy but neither fetchEntity nor fetchEntities are available to load item identified by ${
                    this.model
                }`
            );
        }

        if (this.isPagedDropdown() && !this.keepModelOnReset) {
            this.logService.debug(
                'current ySelect is paged, so keepModelOnReset flag is ignored (it will always keep the model on reset).'
            );
        }
    }

    public requiresPaginatedStyling(): boolean {
        return this.isPagedDropdown() || this.hasControls();
    }

    public hasError(): boolean {
        return VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR === this.validationState;
    }

    public hasWarning(): boolean {
        return VALIDATION_MESSAGE_TYPES.WARNING === this.validationState;
    }

    public hasControls(): boolean {
        return (this.controls || false) === true;
    }

    public disableChoice(item: T): boolean {
        if (this.disableChoiceFn) {
            return this.disableChoiceFn(item);
        }

        return false;
    }
    private isPagedDropdown(): boolean {
        return !!this.fetchStrategy.fetchPage;
    }
    private resetModel(): void {
        if (this.multiSelect) {
            (this.model as string[]).length = 0;
        } else {
            delete this.model;
        }
    }

    private updateControlTemplate(): void {
        this.theme = this.isPagedDropdown() ? 'pagedSelect2' : 'select2';
        this.itemTemplate = this.itemTemplate || 'defaultItemTemplate.html';
    }

    private isModelEmpty(): boolean {
        if (this.multiSelect) {
            return !this.model || (this.model && this.model.length === 0);
        } else {
            return !this.model;
        }
    }

    private _updateChild(): void {
        const ySelectTemplate = 'ySelectTemplate.html';
        const yMultiSelectTemplate = 'yMultiSelectTemplate.html';

        const theme = !this.fetchStrategy.fetchPage ? 'select2' : 'pagedSelect2';
        const selectedFilters = !this.fetchStrategy.fetchPage
            ? 'repeat="item.id as item in $ctrl.items | filter: $select.search" refresh="$ctrl.refreshOptions($select.search)"'
            : 'repeat="item.id as item in $ctrl.items"';

        const rawTemplate = this.$templateCache.get(
            this.multiSelect ? yMultiSelectTemplate : ySelectTemplate
        ) as string;

        this.result = rawTemplate
            .replace('<%= theme %>', theme)
            .replace('{% filtering %}', selectedFilters);
    }
}
