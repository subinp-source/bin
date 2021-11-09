/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// tslint:disable:max-classes-per-file
import {
    ChangeDetectorRef,
    Component,
    Inject,
    NgModule,
    Type,
    ViewEncapsulation
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { cloneDeep } from 'lodash';

import {
    FetchStrategy,
    ItemComponentData,
    ITEM_COMPONENT_DATA_TOKEN,
    SelectApi,
    SelectComponent,
    SelectDisableChoice,
    SelectKeyup,
    SelectModule,
    SelectOnChange,
    SelectOnRemove,
    SelectOnSelect,
    SelectReset,
    SeDowngradeComponent,
    SeEntryModule,
    VALIDATION_MESSAGE_TYPES
} from 'smarteditcommons';
import {
    mockLanguagesV1,
    mockLanguagesV2,
    mockProductsV1,
    mockProductsV2,
    mockSites
} from './outerMocks';

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div class="smartedit-testing-overlay">
            <mock-single-select-1></mock-single-select-1>
            <mock-single-select-2></mock-single-select-2>
            <mock-multi-select-1></mock-multi-select-1>
            <mock-multi-select-2></mock-multi-select-2>
        </div>
    `,
    encapsulation: ViewEncapsulation.None,
    styles: [
        `
            .select-container {
                width: 35rem;
                margin-left: 1rem;
            }
        `
    ]
})
export class AppRootComponent {}

/** Component with helper methods */
@Component({
    template: ''
})
export class BaseMockSelectComponent {
    public api: SelectApi;
    public itemComponent: Type<any>;
    public reset: SelectReset | any;
    public resultsHeaderLabel: string;
    public resultsHeaderComponent: Type<any>;
    public searchEnabled = true;
    public actionableSearchItem: any;
    public isReadOnly = false;
    public onChange: SelectOnChange;
    public onSelect: SelectOnSelect<any>;
    public onRemove: SelectOnRemove<any>;
    public keyup: SelectKeyup;
    /** for simulating rerendering */
    public isDisplayed = true;

    public onChangeCounter = 0;
    public onRemoveModel: string;
    public onSelectModel: string;
    public keyupSearch: string;
    public resetSearchInput: boolean;

    constructor(public _cdr: ChangeDetectorRef) {}

    public changeValidationState(state: string) {
        if (state === 'error') {
            this.api.setValidationState(VALIDATION_MESSAGE_TYPES.VALIDATION_ERROR);
        } else if (state === 'warning') {
            this.api.setValidationState(VALIDATION_MESSAGE_TYPES.WARNING);
        } else {
            this.api.resetValidationState();
        }
    }

    public changeResultsHeaderTemplate(type: 'label' | 'component') {
        if (type === 'label') {
            this.resultsHeaderLabel = 'Results Header Label';
            this.resultsHeaderComponent = undefined;
        } else if (type === 'component') {
            this.resultsHeaderLabel = undefined;
            this.resultsHeaderComponent = MockResultsHeaderComponent;
        }
    }

    public setActionableSearchItem() {
        this.actionableSearchItem = {};
    }

    public setOnSelect() {
        this.onSelect = (_: any, model: string) => {
            this.onSelectModel = model;
        };
    }

    public setOnChange() {
        this.isDisplayed = false;
        this._cdr.detectChanges();

        this.onChange = () => {
            this.onChangeCounter++;
        };
        this.isDisplayed = true;
    }

    public setOnRemove() {
        this.onRemove = (_: any, model: string) => {
            this.onRemoveModel = model;
        };
    }

    public setKeyup() {
        this.keyup = (_: Event, search: string) => {
            this.keyupSearch = search;
        };
    }

    public getFetchPageResults(
        sites: typeof mockSites | typeof mockProductsV1,
        pageSize: number,
        currentPage: number
    ): Promise<any> {
        const results = cloneDeep(sites)
            .slice(currentPage * pageSize)
            .slice(0, pageSize);
        return Promise.resolve({
            results,
            pagination: { totalCount: 20, count: null, page: null, totalPages: null }
        });
    }
}

@Component({
    selector: 'mock-results-header',
    template: `
        <span>Results Header Custom Component</span>
    `
})
export class MockResultsHeaderComponent {}

@Component({
    selector: 'mock-single-select-1',
    host: {
        '[id]': "id + '-host'"
    },
    template: `
        <h2>Single Select 1</h2>
        <div>
            <span
                >Selected value:<span id="model">{{ model }}</span></span
            >
            <button id="clear-model" (click)="model = undefined">Clear Model</button>
            <button id="change-model" (click)="changeModel()">Change Model</button>
        </div>

        <div>
            <span>Force Reset:{{ forceReset }}</span> Force Reset:
            <input type="checkbox" id="force-reset" [(ngModel)]="forceReset" />
        </div>

        <div>
            <span>resetSearchInput:{{ resetSearchInput }}</span> resetSearchInput:
            <input type="checkbox" id="reset-search-input" [(ngModel)]="resetSearchInput" />
        </div>

        <div>
            <button id="source" type="button" (click)="changeSource()">Change source</button>
            <button id="clear-source" type="button" (click)="clearSource()">Clear source</button>
        </div>

        <div>
            <button id="error" (click)="changeValidationState('error')">Show Error</button>
            <button id="warning" (click)="changeValidationState('warning')">Show Warning</button>
            <button id="reset" (click)="changeValidationState('reset')">Reset Validation</button>
        </div>

        <div>
            <button id="set-results-header-label" (click)="changeResultsHeaderTemplate('label')">
                Set Results Header Label
            </button>
            <button
                id="set-results-header-component"
                (click)="changeResultsHeaderTemplate('component')"
            >
                Set Results Header Component
            </button>
        </div>

        <div>
            <button id="disableChoiceFn" (click)="setDisableChoiceFn()">Set disableChoiceFn</button>
        </div>

        <button id="set-actionable-search-item" (click)="setActionableSearchItem()">
            Set Actionable Search Item
        </button>

        <label for="search-enabled">
            searchEnabled <input type="checkbox" id="search-enabled" [(ngModel)]="searchEnabled" />
        </label>

        <label for="isReadOnly">
            isReadOnly <input type="checkbox" id="isReadOnly" [(ngModel)]="isReadOnly" />
        </label>

        <div>
            <button id="set-onChange" (click)="setOnChange()">Set onChange</button> onChangeCounter:
            <span id="onChange-counter">{{ onChangeCounter }}</span>
        </div>

        <div>
            <button id="set-onRemove" (click)="setOnRemove()">Set onRemove</button> onRemoveModel:
            <span id="onRemove-model">{{ onRemoveModel }}</span>
        </div>

        <div>
            <button id="set-onSelect" (click)="setOnSelect()">Set onSelect</button> onSelectModel:
            <span id="onSelect-model">{{ onSelectModel }}</span>
        </div>

        <div>
            <button id="set-keyup" (click)="setKeyup()">Set keyup</button> keyupSearch:
            <span id="keyup-search">{{ keyupSearch }}</span>
        </div>

        <div><button id="set-fetchPage" (click)="setFetchPage()">Set fetchPage</button></div>

        <se-select
            *ngIf="isDisplayed"
            [id]="id"
            [(model)]="model"
            [(reset)]="reset"
            [keepModelOnReset]="!forceReset"
            [fetchStrategy]="fetchStrategy"
            (getApi)="api = $event"
            [controls]="true"
            [resultsHeaderLabel]="resultsHeaderLabel"
            [resultsHeaderComponent]="resultsHeaderComponent"
            [actionableSearchItem]="actionableSearchItem"
            [searchEnabled]="searchEnabled"
            [isReadOnly]="isReadOnly"
            [disableChoiceFn]="disableChoiceFn"
            [onChange]="onChange"
            [onRemove]="onRemove"
            [onSelect]="onSelect"
            [keyup]="keyup"
            [resetSearchInput]="resetSearchInput"
        >
        </se-select>
    `
})
export class MockSingleSelect1Component extends BaseMockSelectComponent {
    public id = 'example1';
    public model = 'en';
    public fetchStrategy: FetchStrategy<any> = {
        fetchAll: () => Promise.resolve([...this.source])
    };
    public forceReset = true;
    public resultsHeaderLabel = 'Results Header Label';
    public disableChoiceFn: SelectDisableChoice<any>;
    public onSelect: SelectOnSelect<any>;

    public onSelectModel: string;
    private source = mockLanguagesV1;

    constructor(private cdr: ChangeDetectorRef) {
        super(cdr);
    }

    public changeModel() {
        this.model = this.source.find((item) => item.id !== this.model).id;
    }

    public changeSource() {
        this.source = this.source === mockLanguagesV1 ? mockLanguagesV2 : mockLanguagesV1;
        this.reset();
    }

    public clearSource() {
        this.model = undefined;
        this.source = [];
    }

    public setDisableChoiceFn() {
        this.disableChoiceFn = (item: typeof mockLanguagesV1[0]) =>
            item.id === 'en' || item.id === 'de';
    }

    public setOnSelect() {
        this.onSelect = (_: any, model: string) => {
            this.onSelectModel = model;
        };
    }

    public setFetchPage() {
        this.isDisplayed = false;
        this.cdr.detectChanges();

        this.fetchStrategy = {
            fetchPage: (_: string, pageSize: number, currentPage: number) =>
                this.getFetchPageResults(mockSites, pageSize, currentPage)
        };
        this.isDisplayed = true;
    }
}

@Component({
    selector: 'mock-item-printer',
    template: `
        <div><b>Label:</b> {{ data.item.label }} <b>Price:</b> {{ data.item.price }}</div>
    `
})
export class MockItemPrinterComponent {
    constructor(
        @Inject(ITEM_COMPONENT_DATA_TOKEN) public data: ItemComponentData<typeof mockProductsV1[0]>
    ) {}
}

@Component({
    selector: 'mock-single-select-2',
    host: {
        '[id]': "id + '-host'"
    },
    template: `
        <h2>Single Select 2 - custom template</h2>
        <se-select
            [id]="id"
            [(model)]="model"
            [fetchStrategy]="fetchStrategy"
            [itemComponent]="itemComponent"
        >
        </se-select>
    `
})
export class MockSingleSelect2Component extends BaseMockSelectComponent {
    public id = 'singleSelect2';
    public model = 'product1';
    public fetchStrategy = {
        fetchAll: () => Promise.resolve([...mockProductsV1])
    };

    constructor(cdr: ChangeDetectorRef) {
        super(cdr);

        this.itemComponent = MockItemPrinterComponent;
    }
}

@Component({
    selector: 'mock-multi-select-1',
    host: {
        '[id]': "id + '-host'"
    },
    template: `
        <h2>Multi Select 1</h2>
        <div>
            <span>
                Selected value:<span id="model">{{ model | json }}</span>
            </span>
            <button id="clear-model" (click)="model = undefined">Clear Model</button>
            <button id="change-model" (click)="changeModel()">Change Model</button>
        </div>

        <div>
            <span>Force Reset:{{ forceReset }}</span> Force Reset:
            <input type="checkbox" id="force-reset" [(ngModel)]="forceReset" />
        </div>

        <div>
            <span>resetSearchInput:{{ resetSearchInput }}</span> resetSearchInput:
            <input type="checkbox" id="reset-search-input" [(ngModel)]="resetSearchInput" />
        </div>

        <div>
            <button type="button" id="source" (click)="changeSource()">Change source</button>
            <button id="clear-source" type="button" (click)="clearSource()">Clear source</button>
        </div>
        <button type="button" id="error" (click)="changeValidationState('error')">
            Show Error
        </button>
        <button type="button" id="warning" (click)="changeValidationState('warning')">
            Show Warning
        </button>
        <button type="button" id="reset" (click)="changeValidationState('reset')">
            Reset Validation
        </button>

        <div>
            <button id="set-results-header-label" (click)="changeResultsHeaderTemplate('label')">
                Set Results Header Label
            </button>
            <button
                id="set-results-header-component"
                (click)="changeResultsHeaderTemplate('component')"
            >
                Set Results Header Component
            </button>
        </div>

        <label for="search-enabled">
            searchEnabled <input type="checkbox" id="search-enabled" [(ngModel)]="searchEnabled" />
        </label>

        <button id="set-actionable-search-item" (click)="setActionableSearchItem()">
            Set Actionable Search Item
        </button>

        <label for="isReadOnly">
            isReadOnly <input type="checkbox" id="isReadOnly" [(ngModel)]="isReadOnly" />
        </label>

        <div>
            <button id="disableChoiceFn" (click)="setDisableChoiceFn()">Set disableChoiceFn</button>
        </div>

        <div>
            <button id="set-onChange" (click)="setOnChange()">Set onChange</button> onChangeCounter:
            <span id="onChange-counter">{{ onChangeCounter }}</span>
        </div>

        <div>
            <button id="set-onRemove" (click)="setOnRemove()">Set onRemove</button> onRemoveModel:
            <span id="onRemove-model">{{ onRemoveModel }}</span>
        </div>

        <div>
            <button id="set-onSelect" (click)="setOnSelect()">Set onSelect</button> onSelectModel:
            <span id="onSelect-model">{{ onSelectModel }}</span>
        </div>

        <div>
            <button id="set-keyup" (click)="setKeyup()">Set keyup</button> keyupSearch:
            <span id="keyup-search">{{ keyupSearch }}</span>
        </div>

        <div><button id="set-fetchPage" (click)="setFetchPage()">Set fetchPage</button></div>

        <se-select
            *ngIf="isDisplayed"
            [id]="id"
            [(model)]="model"
            [controls]="true"
            [multiSelect]="true"
            [(reset)]="reset"
            [keepModelOnReset]="!forceReset"
            [fetchStrategy]="fetchStrategy"
            (getApi)="api = $event"
            [resultsHeaderLabel]="resultsHeaderLabel"
            [resultsHeaderComponent]="resultsHeaderComponent"
            [actionableSearchItem]="actionableSearchItem"
            [searchEnabled]="searchEnabled"
            [isReadOnly]="isReadOnly"
            [disableChoiceFn]="disableChoiceFn"
            [onChange]="onChange"
            [onRemove]="onRemove"
            [onSelect]="onSelect"
            [keyup]="keyup"
            [resetSearchInput]="resetSearchInput"
        ></se-select>
    `
})
export class MockMultiSelect1Component extends BaseMockSelectComponent
    implements Partial<SelectComponent<any>> {
    public id = 'exampleMulti1';
    public model = ['product2', 'product3'];
    public fetchStrategy: FetchStrategy<any> = {
        fetchAll: () => Promise.resolve([...this.source])
    };
    public forceReset = true;
    public disableChoiceFn: SelectDisableChoice<any>;
    public onRemove: SelectOnRemove<any>;
    public onSelect: SelectOnSelect<any>;

    public onRemoveModel: string;
    public onSelectModel: string;
    private source = mockProductsV1;

    constructor(private cdr: ChangeDetectorRef) {
        super(cdr);
    }

    public changeModel() {
        this.model = ['product2', 'product3'];
    }

    public changeSource() {
        this.source = this.source === mockProductsV1 ? mockProductsV2 : mockProductsV1;
        this.reset();
    }

    public clearSource() {
        this.model = undefined;
        this.source = [];
    }

    public setDisableChoiceFn() {
        this.disableChoiceFn = (item: typeof mockProductsV1[0]) => item.id === 'product1';
    }

    public setFetchPage() {
        this.isDisplayed = false;
        this.cdr.detectChanges();

        this.fetchStrategy = {
            fetchPage: (_: string, pageSize: number, currentPage: number) =>
                this.getFetchPageResults(mockProductsV1, pageSize, currentPage)
        };
        this.isDisplayed = true;
    }
}

@Component({
    selector: 'mock-multi-select-2',
    host: {
        '[id]': "id + '-host'"
    },
    template: `
        <h2>Multi Select 2 - custom template</h2>
        <se-select
            [id]="id"
            [(model)]="model"
            [controls]="true"
            [multiSelect]="true"
            [fetchStrategy]="fetchStrategy"
            [itemComponent]="itemComponent"
        ></se-select>
    `
})
export class MockMultiSelect2Component extends BaseMockSelectComponent
    implements Partial<SelectComponent<any>> {
    public id = 'exampleMulti2';
    public model = ['product1', 'product2'];
    public fetchStrategy = {
        fetchAll: () => Promise.resolve([...mockProductsV1])
    };

    constructor(cdr: ChangeDetectorRef) {
        super(cdr);

        this.itemComponent = MockItemPrinterComponent;
    }
}

@SeEntryModule('OuterappModule')
@NgModule({
    imports: [CommonModule, FormsModule, SelectModule],
    declarations: [
        AppRootComponent,
        MockSingleSelect1Component,
        MockSingleSelect2Component,
        MockMultiSelect1Component,
        MockMultiSelect2Component,
        MockItemPrinterComponent,
        MockResultsHeaderComponent,
        BaseMockSelectComponent
    ],
    entryComponents: [
        AppRootComponent,
        MockSingleSelect1Component,
        MockSingleSelect2Component,
        MockMultiSelect1Component,
        MockMultiSelect2Component,
        MockItemPrinterComponent,
        MockResultsHeaderComponent,
        BaseMockSelectComponent
    ]
})
export class OuterappModule {}
