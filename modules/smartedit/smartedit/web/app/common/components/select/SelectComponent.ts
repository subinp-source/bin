/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ChangeDetectionStrategy,
    ChangeDetectorRef,
    Component,
    EventEmitter,
    HostBinding,
    HostListener,
    Input,
    OnChanges,
    OnInit,
    Output,
    SimpleChanges,
    Type,
    ViewChild
} from '@angular/core';
import { moveItemInArray, CdkDragDrop } from '@angular/cdk/drag-drop';
import { isEqual } from 'lodash';
import { LogService } from '@smart/utils';
import { Observable, Subject, Subscription } from 'rxjs';
import { debounceTime, filter, switchMap, tap } from 'rxjs/operators';

import { SeDowngradeComponent } from '../../di';
import { getLocalizedFilterFn, L10nPipeFilterFn } from '../../pipes';
import { stringUtils, VALIDATION_MESSAGE_TYPES } from '../../utils';
import { L10nService } from '../../services';

import {
    FetchStrategy,
    SelectApi,
    SelectDisableChoice,
    SelectItem,
    SelectKeyup,
    SelectOnRemove,
    SelectOnSelect,
    SelectReset
} from './interfaces';
import { DefaultItemPrinterComponent } from './defaultItemPrinter';
import { ActionableSearchItem } from './actionableSearchItem';
import { SearchInputComponent } from './searchInput';

/**
 * Represents dropdown menu with filtering capabilities which is customizable with an item component.
 *
 * There are two modes `Single Select` and `Multi Select`.
 *
 * By default, a `Single Select` dropdown will be displayed, which allows to select only one option.
 * A selected option is displayed in dropdown as highlighted.
 *
 * `Multi Select` dropdown allows to select multiple options whose sequence can be changed with Drag and Drop.
 * To switch to Multi Select set `multiSelect` to true.
 * Selected options are not displayed in dropdown.
 *
 * Component can work in both paged and non paged mode: providing either fetchAll or fetchPage function in the fetchStrategy will determine the flavour of the dropdown.
 * Works exclusively with Infinite Scrolling.
 *
 * Note:
 * For custom Results Header template, only one of `resultsHeaderComponent`, `resultsHeaderLabel` must be passed.
 *
 * @example
 * Single Select
 *
 * <se-select
 *   [id]="1"
 *   [(model)]="model"
 *   [fetch-strategy]="fetchStrategy">
 * </se-select>
 *
 * Multi Select
 *
 * <se-select
 *   [id]="2"
 *   [multiSelect]="true"
 *   [(model)]="model"
 *   [fetch-strategy]="fetchStrategy">
 * </se-select>
 */
@SeDowngradeComponent()
@Component({
    selector: 'se-select',
    changeDetection: ChangeDetectionStrategy.OnPush,
    host: {
        '[class.se-select]': 'true'
    },
    templateUrl: './SelectComponent.html'
})
export class SelectComponent<T extends SelectItem> implements OnInit, OnChanges {
    @HostBinding('class.se-select--single') get isSingleCss() {
        return !this.multiSelect;
    }

    @HostBinding('class.se-select--multi') get isMultiCss() {
        return this.multiSelect;
    }

    /** Used to identify elements like container, selected, dropdown list */
    @Input()
    @HostBinding('attr.id')
    id: string;

    /** Model whose value is selected item id */
    @Input() model: string | string[];

    /**
     * Notifies that model has changed.
     *
     * Values when there is no option selected:
     * Single Select - undefined
     *
     * Multi Select - []
     */
    @Output() modelChange = new EventEmitter<string | string[]>();

    /**
     * fetchStrategy strategy object containing the necessary functions to populate the dropdown:
     *
     * `fetchStrategy.fetchAll` - function required to fetch all items for a given optional mask.
     * fetchAll will be called without arguments upon initialization and with a mask every time the search section receives an input.
     * It must return a promise resolving to a list of items.
     * Every item must have a property "id" used for identification. If no itemTemplate is provided, these items will need to display a "label" property.
     *
     * `fetchStrategy.fetchPage` - function required to fetch a page for a given optional mask.
     * fetchPage must fulfill the contract of fetchPage from {@link InfiniteScrollingComponent#fetchPage}
     * It must return a promise resolving to a page of items as per Page.
     * Every item must have a property "id" used for identification. If no itemTemplate is provided, these items will need to display a "label" property.
     *
     * `fetchStrategy.fetchEntity` - function to fetch an option by its identifier when we are in paged mode (fetchPage is defined) and the dropdown is initialized with a value.
     */
    @Input() fetchStrategy: FetchStrategy<T>;

    /**
     * Adds controls such as the magnifier and the remove button.
     *
     * For Single Select it will display magnifier and remove icon on selected item.
     * Clicking on remove icon will remove selected item from the model.
     */
    @Input() controls: boolean = false;

    /** Whether the component should be displayed as Multi Select */
    @Input() multiSelect: boolean = false;

    /**
     * False (non-paged dropdown) => when next time entities are fetched which returns some entities without the entities I have previously selected, it will remove these entities.
     * False (paged dropdown) => - || - must call reset(true) to remove them
     * True => will not remove
     *
     * a non-paged dropdown: if the value is set to false, the widget will remove the selected entities in the model that no longer match the values available on the server.
     */
    @Input() keepModelOnReset: boolean = false;

    /** Whether component is disabled  */
    @Input() @HostBinding('class.is-disabled') isReadOnly: boolean = false;

    /**
     * Data for `ActionableSearchItemComponent` that will display the component if provided.
     */
    @Input() actionableSearchItem?: ActionableSearchItem;

    /** Component that will be displayed on top of the result list (above `resultsHeadersLabel`). */
    @Input() resultsHeaderComponent?: Type<any>;

    /**
     * The label that will be displayed on top of the result list
     * when there is at least one option (previously filtered by the Search Input) to select.
     * e.g. "Select Option".
     *
     * Can be either text or translation string.
     */
    @Input() resultsHeaderLabel?: string;

    /** A function to disable results in the dropdown. It is invoked for each item in the dropdown, with a single parameter, the item itself. */
    @Input() disableChoiceFn?: SelectDisableChoice<T>;
    /**
     * Placeholder text for Search Input / Toggle Button.
     * Can be either text or translation string.
     *
     * Single Select -
     * For Search Input, it is displayed when an item is selected.
     * For Toggle Button text, it is displayed when no item is selected.
     *
     * Multi Select - displayed all the time for Search Input
     */
    @Input() placeholder?: string;

    /**
     * The component that will be used to display items in both the dropdown and the selection.
     * "itemComponent" has access to item, selected and the Select Component controller.
     * Item is the item to print, selected is a boolean that is true when the template is used in the selection as opposed to the dropdown menu.
     *
     * Default template will be:
     * ```
     * <span>{{ ((data.item.label || data.item.name) | seL10n | async) | translate }}</span>
     * ```
     */
    @Input() itemComponent?: Type<any>;

    /**
     * Single Select - whether the Search Input is displayed.
     *
     * Multi Select - if set to false, search input will be displayed as readonly.
     */
    @Input() searchEnabled: boolean = true;

    /** Whether to clear the search input after selecting an option */
    @Input() resetSearchInput: boolean = true;

    /**
     * A function that will be called when:
     * - On initial items fetch
     * - Option has been selected. For Single Select it will not be called when selected option is clicked
     * - Selected option is removed
     * - Parent changes the model
     * - `Multi Select` - Drag & Drop of an option has changed its order
     */
    @Input() onChange?: () => void;

    /** A function that will be called when item was removed from selection */
    @Input() onRemove?: SelectOnRemove<T>;

    /** A function that will be called when item was selected */
    @Input() onSelect?: SelectOnSelect<T>;

    /** A function that will be called on keyup event in search input */
    @Input() keyup?: SelectKeyup;

    /**
     * Whether to display the remove icon for selected item.
     *
     * Note: Meant for Single Select only.
     */
    @Input() showRemoveButton: boolean = false;

    /** Exposes the Select API object */
    @Output() getApi = new EventEmitter<SelectApi>();

    /** A function that will be called when Select Component is reset */
    @Input() reset: SelectReset;
    @Output() resetChange = new EventEmitter<SelectReset>();

    /**
     * View query of `SearchInputComponent`
     *
     * Multi Select - used for toggling
     *
     * Single Select - used for focusing on the Search Input
     *
     * @internal
     */
    @ViewChild(SearchInputComponent, { static: false })
    searchInputCmp: SearchInputComponent;

    /**
     * Whether the dropdown is open
     *
     * @internal
     */
    public isOpen: boolean = false;

    /** Items displayed in dropdown */
    public items: T[] = [];

    /**
     * Selected item
     *
     * @internal
     */
    public selected: T | T[];

    /**
     * Search Input model
     *
     * @internal
     */
    public search: string = '';

    /**
     * Last fetched items.
     * Used for filtering dropdown results.
     *
     * @internal
     */
    private fetchAllItems: T[];

    /**
     * To determine if the items should be refetched in ngOnChanges
     *
     * @internal
     */
    private modelChangeOld: string | string[];

    /**
     * Object with methods for setting the validation state, exposed to Parent Component via @Output
     *
     * @internal
     */
    private api: SelectApi;

    /**
     * Stores the results of validation state methods, used for setting a validation state based CSS class on component like warning or error
     *
     * @internal
     */
    private validationState: string;

    /**
     * Language dependent filter function for filter items by "label" or "name".
     * "label" takes precedence over the "name".
     *
     * @internal
     */
    private filterFn: L10nPipeFilterFn;

    /** @internal */
    private languageSwitchSubscription: Subscription;

    /** @internal */
    private searchInputChange$: Observable<string>;

    /** @internal */
    private searchInputChangeSubject: Subject<string>;

    constructor(
        private l10nService: L10nService,
        private logService: LogService,
        private cdr: ChangeDetectorRef
    ) {
        this.api = {
            setValidationState: this.setValidationState.bind(this),
            resetValidationState: this.resetValidationState.bind(this)
        };
    }

    /**
     * Toggle Multi Select.
     *
     * Toggling cannot be handled by Popover "triggers" property
     * because the Search Input is inside <fd-popover-control>
     * which will close the dropdown when it has been clicked.
     *
     * @internal
     */
    @HostListener('document:click', ['$event'])
    clickHandler(event: MouseEvent): void {
        if (!this.multiSelect) {
            return;
        }

        if (event.target === this.searchInputCmp.inputElement) {
            event.stopPropagation();
            if (!this.isOpen) {
                this.onMultiSelectIsOpenChange(true);
            }
        } else if (this.isOpen) {
            this.onMultiSelectIsOpenChange(false);
        }
    }

    /** @internal */
    ngOnInit() {
        if (!this.placeholder) {
            this.placeholder = 'se.genericeditor.sedropdown.placeholder';
        }

        if (this.fetchStrategy.fetchAll) {
            this.initSearchInputFilter();
        }

        this.fetchData();

        setTimeout(() => {
            // To prevent "ExpressionChangeAfterItHasBeenChecked" error, because we change the "reset" value.
            // It's required for backward compatibility.
            this.resetChange.emit(this.internalReset.bind(this));
        });

        this.getApi.emit(this.api);
    }

    /** @internal */
    ngOnChanges(changes: SimpleChanges) {
        if (!this.itemComponent) {
            this.itemComponent = DefaultItemPrinterComponent;
        }

        this.isValidConfiguration();

        // Each time a value is emitted via modelChange it can trigger ngOnChanges (if parent uses two-way binding).
        // When parent changes the model value, data has to be refetched.
        // So we store the last emitted value to determine whether parent has changed the model value.
        const modelChange = changes.model;

        // Parent has changed model (not first and not through this component)
        const didModelChange =
            modelChange &&
            modelChange.currentValue !== this.modelChangeOld &&
            !modelChange.firstChange;
        if (didModelChange) {
            // For Mulit Select, set an empty array if a parent component passed empty model
            // so it's easier to operate on that variable
            if (this.multiSelect) {
                if (this.isModelEmpty()) {
                    this.model = [];
                }
            }
            this.fetchData();
        }
    }

    /** @internal */
    ngOnDestroy() {
        if (this.languageSwitchSubscription) {
            this.languageSwitchSubscription.unsubscribe();
        }
        if (this.searchInputChangeSubject) {
            this.searchInputChangeSubject.unsubscribe();
        }
    }

    /**
     * @internal
     */
    public onSingleSelectIsOpenChange(isOpen: boolean): void {
        if (isOpen) {
            // Search Input is not yet rendered at this point so we delay this block with setTimeout to get the reference.
            setTimeout(() => {
                if (this.searchInputCmp) {
                    this.searchInputCmp.focus();
                }

                // Refetch because the data on the backed might have changed since last fetch.
                // "fetchPage" is handled by Inifnite Scrolling
                if (this.fetchStrategy.fetchAll) {
                    this.refreshOptions(this.search);
                }
            });
        } else {
            this.resetOnClose();
        }
    }

    /** @internal */
    public onSearchInputKeyup(event: KeyboardEvent, value: string): void {
        this.internalKeyup(event, value);
    }

    /** @internal */
    public onSearchInputChange(value: string) {
        this.onSearchChange(value);

        if (this.searchInputChangeSubject) {
            this.searchInputChangeSubject.next(value);
        }
    }

    /** @internal */
    public onOptionClick(item: T): void {
        let selectedHasChanged = false;
        if (this.multiSelect) {
            // add new item to selected items
            const selected = [...((this.selected || []) as T[]), item];
            this.setSelected(selected);

            selectedHasChanged = true;
        } else if (!this.isItemSelected(item)) {
            this.setSelected(item);

            selectedHasChanged = true;
        }

        this.internalOnSelect(item, item.id);

        if (selectedHasChanged) {
            this.internalOnChange();
        }

        this.closeAndReset();
    }

    /** @internal */
    public onSearchChange(value: string): void {
        this.search = value;
    }

    /**
     * Multi Select
     *
     * Sorts selected options when option is dropped.
     *
     * @internal
     */
    public onDrop(event: CdkDragDrop<string[]>): void {
        moveItemInArray(this.selected as T[], event.previousIndex, event.currentIndex);
        this.setSelected(this.selected);

        const orderHasChanged = event.previousIndex !== event.currentIndex;
        if (orderHasChanged) {
            this.internalOnChange();
        }
    }

    /**
     * Removes selected option.
     * Closes the dropdown if it's opened and clears the Search Input.
     *
     * @internal
     */
    public removeSelectedOption(_: Event, item: T): void {
        const selectedNew = this.multiSelect
            ? (this.selected as T[]).filter((selectedItem) => selectedItem !== item)
            : undefined;
        this.setSelected(selectedNew);

        this.internalOnRemove(item, item.id);

        if (this.isOpen) {
            this.closeAndReset();
        }
    }

    /**
     * Closes the dropdown and clears the search input
     *
     * @internal
     */
    public closeAndReset(): void {
        this.close();
        this.resetOnClose();
    }

    /**
     * Whether the Results Header Label is displayed.
     *
     * @internal
     */
    public showResultsHeaderLabel(): boolean {
        return this.items.length > 0 && !!this.resultsHeaderLabel;
    }

    /**
     * Because of OnPush strategy, it's required for triggering Change Detection
     * whenever Infinite Scroll loads items so that items the can be rendered.
     *
     * @internal
     */
    public onInfiniteScrollItemsChange(): void {
        //
    }

    /**
     * @internal
     */
    public showPlaceholder(): boolean {
        return this.multiSelect || (!this.multiSelect && !!this.selected);
    }

    /**
     * @internal
     */
    public itemTrackBy(_: any, item: SelectItem): string {
        return item.id;
    }

    public setValidationState(validationState: string): void {
        this.validationState = validationState;
        this.cdr.detectChanges();
    }

    public resetValidationState(): void {
        this.validationState = undefined;
        this.cdr.detectChanges();
    }

    public hasError(): boolean {
        return VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR === this.validationState;
    }

    public hasWarning(): boolean {
        return VALIDATION_MESSAGE_TYPES.WARNING === this.validationState;
    }

    public fetchEntity(modelId: string): Promise<T> {
        return (this.fetchStrategy.fetchEntity(modelId) as Promise<T>).then((item: T) => {
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

    /**
     * To prevent the case when the model doesn't match any items returned from API.
     * Called on initialize or when parent changes "model" value.
     * It will update the model and set "selected" based on its value.
     *
     * A non-paged dropdown: if the value is set to false, the selected options
     * that no longer match the values available on the server will be removed.
     * For example: If the current model equals to ['1','2'] and among the items there is an item whose id equals to '1'
     * but there is no item whose id equals to '2', the current model & selected option must be updated to match '1'.
     *
     * For a paged dropdown: After a standard reset, even if `keepModelOnReset` is set to false,
     * the widget will not be able to remove the selected entities in the model
     * that no longer match the values available on the server.
     * It will only notifiy a parent component that the model has changed.
     * To force the widget to remove any selected entities, you must call reset(true).
     *
     * Note: It's legacy for UiSelect because it was not possible to set selected option based on model if the <ui-select-choices> was empty (model doesn't have reference to selected options).
     * Those are handled by Infinite Scroll when Dropdown is open on a user scroll through the list.
     *
     * @internal
     */
    public updateModelIfNotFoundInItems(items: T[]): void {
        if (!this.keepModelOnReset) {
            if (this.multiSelect) {
                if (this.isModelEmpty()) {
                    this.setSelected([], false);
                    return;
                }

                const modelFromNewItems = (this.model as string[]).filter((id) =>
                    this.getItemByModel(items, id)
                );
                const multiSelectMatch = isEqual(this.model, modelFromNewItems);
                if (!multiSelectMatch) {
                    this.updateModel(modelFromNewItems);
                    this.resolveAndSetSelected(items);
                } else {
                    this.resolveAndSetSelected(items);
                }
                return;
            }

            // Single Select
            if (this.isModelEmpty()) {
                this.setSelected(undefined, false);
                return;
            }

            const singleSelectMatch = this.getItemByModel(items);
            if (!singleSelectMatch) {
                this.setSelected(undefined);
            } else {
                this.resolveAndSetSelected(items);
            }
        }
    }

    /**
     * Refetch because the data on the backed might have changed since last fetch.
     *
     * Note: Meant for "FetchAll" strategy only.
     */
    public refreshOptions(mask: string): void {
        this.internalFetchAll(mask).then((items) => {
            this.items = items;
            this.cdr.detectChanges();
        });
    }

    public isValidConfiguration(): void {
        if (this.resultsHeaderComponent && this.resultsHeaderLabel) {
            throw new Error(
                `Only one of "resultsHeaderComponent" or "resultsHeaderLabel" must be specified`
            );
        }

        if (!this.fetchStrategy.fetchAll && !this.fetchStrategy.fetchPage) {
            throw new Error('Neither fetchAll nor fetchPage have been specified in fetchStrategy');
        }

        if (this.fetchStrategy.fetchAll && this.fetchStrategy.fetchPage) {
            throw new Error(
                'Only one of either fetchAll or fetchPage must be specified in fetchStrategy'
            );
        }

        if (
            this.fetchStrategy.fetchPage &&
            this.model &&
            !this.fetchStrategy.fetchEntity &&
            !this.fetchStrategy.fetchEntities
        ) {
            throw new Error(
                `fetchPage has been specified in fetchStrategy but neither fetchEntity nor fetchEntities are available to load item identified by ${this.model}`
            );
        }

        if (this.isPagedDropdown() && !this.keepModelOnReset) {
            this.logService.debug(
                'current Select is paged, so keepModelOnReset flag is ignored (it will always keep the model on reset).'
            );
        }
    }

    /** @internal */
    private internalKeyup(event: Event, search: string): void {
        if (this.keyup) {
            this.keyup(event, search);
        }
    }

    /** @internal */
    private internalOnRemove(item: T, model: string): void {
        if (this.onRemove) {
            this.onRemove(item, model);
        }

        this.internalOnChange();
    }

    /** @internal */
    private internalOnChange(): void {
        if (this.onChange) {
            this.onChange();
        }
    }

    /** @internal */
    private internalOnSelect(item: T, model: string): void {
        if (this.onSelect) {
            this.onSelect(item, model);
        }
    }

    /** @internal */
    private internalFetchAll(mask: string = ''): Promise<T[]> {
        return (this.fetchStrategy.fetchAll(mask) as Promise<T[]>).then((items) => {
            this.fetchAllItems = items;
            return [...items];
        }) as Promise<T[]>;
    }

    /**
     * Fetching the data for PagedDropdown is handled by InfiniteScrollComponent.
     * This method is used for fetching selected items in PageDropdown mode.
     *
     * Multi Select favors "fetchEntities" over "fetchEntity" - less requests.
     *
     * @internal
     */
    private internalFetchEntities(): Promise<void> {
        let promise: Promise<T[]>;

        if (!this.multiSelect) {
            promise = this.fetchEntity(this.model as string).then((item: T) => [item]);
        } else {
            if (this.fetchStrategy.fetchEntities) {
                // fetch entities with one request
                promise = (this.fetchStrategy.fetchEntities(this.model as string[]) as Promise<
                    T[]
                >).then((items: T[]) => {
                    if (items.length !== this.model.length) {
                        this.logService.debug(
                            `!fetchEntities was used to fetch the options identified by ${JSON.stringify(
                                this.model
                            )} but failed to find all matches`
                        );
                    }
                    return items;
                });
            } else {
                // fetch entities with several requests
                const promiseArray = (this.model as string[]).map((id) => this.fetchEntity(id));
                promise = Promise.all(promiseArray);
            }
        }

        return promise.then((result: T[]) => {
            const items = result
                .filter((item: T) => item !== null)
                .map((item: T) => {
                    delete (item as any).$promise;
                    delete (item as any).$resolved;
                    (item as any).technicalUniqueId = stringUtils.encode(item);
                    return item;
                });

            this.updateModelIfNotFoundInItems(items);

            this.internalOnChange();

            // because outer callback is invoked in async context
            this.cdr.detectChanges();
        });
    }

    /** @internal */
    private onMultiSelectIsOpenChange(isOpen: boolean): void {
        this.isOpen = isOpen;

        if (isOpen) {
            if (this.isPagedDropdown()) {
                // "fetchPage" is handled by Inifnite Scrolling
                this.cdr.detectChanges();
                return;
            }

            this.refreshOptions(this.search);
        } else {
            this.resetOnClose();
        }
    }

    /**
     * Initializes handlers for Search Input when its value changes.
     *
     * Filters items immediately when user inputs the data.
     * It will refetch data from the API after the debounce time.
     *
     * Note: Meant for "FetchAll" strategy only.
     *
     * @internal
     */
    private initSearchInputFilter(): void {
        this.searchInputChangeSubject = new Subject<string>();
        this.searchInputChange$ = this.searchInputChangeSubject.asObservable();

        this.languageSwitchSubscription = this.l10nService.languageSwitch$.subscribe((lang) => {
            this.filterFn = getLocalizedFilterFn(lang);
        });

        this.searchInputChange$
            .pipe(
                filter(() => typeof this.fetchStrategy.fetchAll !== 'undefined'),
                tap((value: string) => {
                    this.items = this.filterItemsBySearch(this.fetchAllItems, value);
                    this.cdr.detectChanges();
                }),
                debounceTime(500),
                switchMap((value) => this.internalFetchAll(value))
            )
            .subscribe((items: T[]) => {
                this.items = this.filterItemsBySearch(items, this.search);
                this.cdr.detectChanges();
            });
    }

    /** @internal */
    private filterItemsBySearch(fetchedItems: T[], search: string): T[] {
        return fetchedItems.filter((item) =>
            this.filterFn(item.label || item.name)
                .trim()
                .toUpperCase()
                .includes(search.trim().toUpperCase())
        );
    }

    /** @internal */
    private close(): void {
        this.isOpen = false;
    }

    /** @internal */
    private resetOnClose(): void {
        if (this.search && this.resetSearchInput) {
            this.search = '';
            this.cdr.detectChanges();

            // For "FetchAll" strategy refetch the data,
            // so when next time a user opens the dropdown, items will be displayed immediately.
            if (this.fetchStrategy.fetchAll) {
                this.refreshOptions(this.search);
            }
        }
    }

    /** @internal */
    private fetchData(): void {
        if (!this.isPagedDropdown()) {
            this.internalFetchAllAndCheckModel();
        } else if (!this.isModelEmpty()) {
            this.internalFetchEntities();
        }
    }

    /**
     * Fetch items and set selected value for a given model.
     *
     * @internal
     */
    private internalFetchAllAndCheckModel(): Promise<void> {
        return this.internalFetchAll().then((items) => {
            this.updateModelIfNotFoundInItems(items);

            // Since "items" property in SEDropdownServiceFactor is set after calling "fetchAll",
            // we notify parent component that model has changed even if it hasn't.
            this.internalOnChange();

            // Because outer callback is invoked in async context. "model" or "selected" may have changed.
            this.cdr.detectChanges();
        });
    }

    /** @internal */
    private getItemByModel(items: T[], model = this.model as string): T | undefined {
        return items.find((item) => item.id === model);
    }

    /** @internal */
    private getItemsByModel(items: T[], model = this.model as string[]): T[] {
        return model.map((id) => this.getItemByModel(items, id)).filter((item) => !!item);
    }

    /** @internal */
    private mapSelectedToModel(): string | string[] {
        return this.multiSelect
            ? (this.selected as T[]).map((item) => item.id)
            : (this.selected as T).id;
    }

    /**
     * Called each time an option is selected or when selected item is determined by model value (default).
     * @param updateModel whether to update the model value & notify a parent about model change.
     *
     * @internal
     */
    private setSelected(items: T | T[], updateModel: boolean = true): void {
        this.selected = items;

        if (updateModel) {
            let model: string | string[]; // will remain undefined for Single Select if a user remove option
            if (this.selected) {
                model = this.mapSelectedToModel();
            }

            this.updateModel(model);
        }
    }

    /** @internal */
    private isItemSelected(item: T): boolean {
        if (!this.selected) {
            return false;
        }

        return item.id === (this.selected as T).id;
    }

    /**
     * Clears items and update the selected items. Exposed to parent component by `reset` @Output.
     *
     * @param forceReset If set to true it will clear items, model and selected regardless "keepModelOnReset" flag
     * @internal
     */
    private internalReset(forceReset = false): void {
        this.items.length = 0;

        if (forceReset) {
            this.selected = undefined;
            this.resetModel();
            return;
        }

        if (!this.keepModelOnReset) {
            // refetch, check and update selected based on "model" and "keepModelOnReset" flag
            this.fetchData();
        }
    }

    /** @internal */
    private resetModel(): void {
        const model: string[] | undefined = this.multiSelect ? [] : undefined;
        this.updateModel(model);
    }

    /** @internal */
    private updateModel(model: string | string[]): void {
        this.model = model;
        this.modelChangeOld = this.model;
        this.modelChange.emit(model);
    }

    /** @internal */
    private isPagedDropdown(): boolean {
        return !!this.fetchStrategy.fetchPage;
    }

    /** @internal */
    private isModelEmpty(): boolean {
        if (this.multiSelect) {
            return !this.model || (this.model && this.model.length === 0);
        } else {
            return !this.model;
        }
    }

    /**
     * Sets selected items based on the current model.
     *
     * @internal
     */
    private resolveAndSetSelected(items: T[]): void {
        const selected = this.multiSelect
            ? this.getItemsByModel(items)
            : this.getItemByModel(items);
        this.setSelected(selected, false);
    }
}
