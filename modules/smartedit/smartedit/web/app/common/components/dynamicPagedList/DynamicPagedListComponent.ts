/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { cloneDeep } from 'lodash';
import {
    ChangeDetectorRef,
    Component,
    EventEmitter,
    Input,
    OnChanges,
    OnInit,
    Output,
    SimpleChanges
} from '@angular/core';
import { LogService, Page, Pageable, RestServiceFactory } from '@smart/utils';

import { SeDowngradeComponent } from '../../di';
import {
    DynamicPagedListApi,
    DynamicPagedListColumnKey,
    DynamicPagedListConfig
} from './interfaces';
import { SortDirections } from '../../utils';

/**
 * @ngdoc component
 * @name smarteditCommonsModule.component:DynamicPagedListComponent
 * @element se-dynamic-paged-list
 *
 * @description
 * Component responsible for displaying a server-side paginated list of items with custom components. It allows the user to search and sort the list.
 *
 * @param {DynamicPagedListConfig} config {@link smarteditCommonsModule.interface:DynamicPagedListConfig config}
 * @param {<String?} mask The string value used to filter the result.
 * @param {EventEmitter<DynamicPagedListApi>} getApi Exposes the dynamic paged list module's api object
 * @param {EventEmitter<Page<any>>} onItemsUpdate  Event that exposes the item list
 *
 * @example
 * <em>Example of a <strong>renderers</strong> object(AngularJS legacy support)</em>
 *
 * <pre>
 *          renderers = {
 *              name: function(item, key) {
 *                  return "<a data-ng-click=\"$ctrl.config.injectedContext.onLink( item.path )\">{{ item[key.property] }}</a>";
 *              }
 *          };
 * </pre>
 *
 * <em>Example of an <strong>injectedContext</strong> object</em>
 * <pre>
 *          injectedContext = {
 *              onLink: function(link) {
 *                  if (link) {
 *                      var experiencePath = this._buildExperiencePath();
 *                      iframeManagerService.setCurrentLocation(link);
 *                      $location.path(experiencePath);
 *                  }
 *              }.bind(this),
 *              dropdownItems: []
 *          };
 * </pre>
 *
 *
 *  For Angular component(to be used instead of renderer) support read: {@link smarteditCommonsModule.component:DataTableComponent DataTableComponent}
 */

@SeDowngradeComponent()
@Component({
    selector: 'se-dynamic-paged-list',
    templateUrl: './DynamicPagedListComponent.html'
})
export class DynamicPagedListComponent implements OnInit, OnChanges {
    @Input() config: DynamicPagedListConfig;
    @Input() mask: string;

    @Output() getApi: EventEmitter<DynamicPagedListApi> = new EventEmitter();
    @Output() onItemsUpdate: EventEmitter<Page<any>> = new EventEmitter<Page<any>>();

    public internalSortBy: string;
    public columnSortMode: string;
    public currentPage: number;
    public ready: boolean;
    public totalItems: number;
    public items: any[];
    public columns: DynamicPagedListColumnKey[];

    private oldMask: string;

    private api: DynamicPagedListApi = {
        reloadItems: () => this.loadItems()
    };

    constructor(
        private logService: LogService,
        private restServiceFactory: RestServiceFactory,
        private cdr: ChangeDetectorRef
    ) {}

    ngOnInit() {
        this._validateInput();

        this.ready = false;

        this.totalItems = 0;
        this.currentPage = 1;

        this.columnSortMode = this.config.reversed
            ? SortDirections.Descending
            : SortDirections.Ascending;
        this.internalSortBy = cloneDeep(this.config.sortBy);
        this.oldMask = this.mask;

        this.columns = [];
        this._buildColumnData();
        this.loadItems();

        this.getApi.emit(this.api);
    }

    ngOnChanges(changes: SimpleChanges) {
        const maskChange = changes.mask;
        if (!!maskChange && !maskChange.firstChange) {
            if (this.oldMask !== this.mask) {
                this.oldMask = this.mask;

                // set page to 1 and reload data
                this.currentPage = 1;
                this.loadItems();
            }
        }
    }

    public orderByColumn(event: {
        $columnKey: DynamicPagedListColumnKey;
        $columnSortMode: string;
    }): void {
        this.internalSortBy = event.$columnKey.property;
        this.columnSortMode = event.$columnSortMode;

        this.config.reversed = this.columnSortMode === SortDirections.Descending;

        if (event.$columnKey.sortable) {
            this.currentPage = 1;
            this.loadItems();
        }
    }

    public loadItems(): Promise<Page<any>> {
        this.ready = false;

        const params: Pageable = {
            ...(this.config.queryParams || {}),
            currentPage: this.currentPage - 1,
            mask: this.mask,
            pageSize: this.config.itemsPerPage,
            sort: `${this.internalSortBy}:${this.columnSortMode}`
        };

        return this.restServiceFactory
            .get(this.config.uri)
            .page(params)
            .then((result: Page<any>) => {
                this.items = result.results;

                if (this.items.length === 0) {
                    this.logService.warn('PagedList: No items returned to display');
                }

                this.totalItems = result.pagination.totalCount;
                this.currentPage = parseInt(String(result.pagination.page), 10) + 1;

                this.ready = true;
                // Trigger cdr to hide the spinner immediately (For some reason, Change Detection is not triggered)
                this.cdr.detectChanges();

                this.onItemsUpdate.emit(result);

                return result;
            });
    }

    public onCurrentPageChange(newCurrentPage: number): void {
        if (newCurrentPage === this.currentPage) {
            return;
        }
        this.currentPage = newCurrentPage;
        this.loadItems();
    }

    private _validateInput(): void {
        if (!this.config) {
            throw new Error('config object is required');
        }

        if (!(this.config.keys instanceof Array)) {
            throw new Error('keys must be an array');
        }

        if (this.config.keys.length < 1) {
            throw new Error('dynamic paged list requires at least one key');
        }

        if (!this.config.uri) {
            throw new Error('dynamic paged list requires a uri to fetch the list of items');
        }
    }

    private _buildColumnData(): void {
        this.columns = this.config.keys.map((key) => ({
            ...key,
            renderer: this.config.renderers[key.property]
        }));
    }
}
