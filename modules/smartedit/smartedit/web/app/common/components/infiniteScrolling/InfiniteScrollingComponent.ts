/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    AfterViewInit,
    ChangeDetectorRef,
    Component,
    ElementRef,
    EventEmitter,
    Input,
    OnChanges,
    OnInit,
    Output,
    ViewChild
} from '@angular/core';
import { Page } from '@smart/utils';
import { differenceBy } from 'lodash';
import ResizeObserver from 'resize-observer-polyfill';

import { SeDowngradeComponent } from '../../di';
import { stringUtils, DiscardablePromiseUtils } from '../../utils';

/** @internal */
export interface TechnicalUniqueIdAware {
    technicalUniqueId: string;
}

/**
 * @ngdoc component
 * @name smarteditCommonsModule.component:InfiniteScrollingComponent
 * @element se-dynamic-paged-list
 *
 * @description
 * A component that you can use to implement infinite scrolling for an expanding content
 * (typically with a ng-repeat for AngularJS / *ngFor for Angular) nested in it.
 * It is meant to handle paginated requests from a backend when data is expected to be large.
 *
 * Since the expanding content is a projected element, we may specify the context
 * to which the items will be attached:
 * If context is myContext, each pagination will push its new items to myContext.items.
 *
 * @param {Number} pageSize The maximum size of each page requested from the backend.
 * @param {String} mask The string value used to filter the result.
 * @param {String?} dropDownContainerClass An optional CSS class to be added to the container of the dropdown.
 * @param {String?} dropDownClass  An optional CSS class to be added to the dropdown.
 * @param {String?} distance  A optional number representing how close the bottom of the element must be to the bottom of the container before the expression specified by fetchPage function is triggered. Defaults to 0.
 * @param {Array} context The container object to which the items of the fetched `Page` will be added.
 * @param {Object?} fetchPage Function to fetch the next page when the bottom of the element approaches the bottom of the container.
 *
 *
 */
@SeDowngradeComponent()
@Component({
    selector: 'se-infinite-scrolling',
    templateUrl: './InfiniteScrollingComponent.html'
})
export class InfiniteScrollingComponent<T extends TechnicalUniqueIdAware>
    implements OnInit, OnChanges, AfterViewInit {
    /** The maximum size of each page requested from the backend. */
    @Input() pageSize: number;
    /**
     * A string value sent to the server upon fetching a page to further restrict the search,
     * it is sent as query string `mask`.
     *
     * The directive listens for change to mask and will reset the scroll and re-fetch data.
     *
     * It is left to the implementers to decide what it filters on.
     */
    @Input() mask: string;
    /**
     * An optional CSS class to be added to the container of the dropdown.
     * It would typically be used to override the default height.
     *
     * Note: The resolved CSS must set a `height` (or `max-height`) and `overflow-y:scroll`.
     */
    @Input() dropDownContainerClass: string;
    /**
     * An optional CSS class to be added to the dropdown
     *
     * Note: Neither height nor overflow should be set on the dropdown,
     * it must be free to fill up the space and reach the container size.
     * Failure to do so will cause the directive to call `nextPage` as many times
     * as the number of available pages on the server.
     */
    @Input() dropDownClass: string;
    /**
     * A number representing how close the bottom of the element must be to the bottom of the container
     * before the expression specified by fetchPage function is triggered.
     * Measured in multiples of the container height;
     *
     * For example, if the container is 1000 pixels tall and distance is set to 2,
     * the infinite scroll expression will be evaluated when the bottom of the element is within 2000 pixels of the bottom of the container.
     * Defaults to 0 (e.g. the expression will be evaluated when the bottom of the element crosses the bottom of the container).
     */
    @Input() distance: number = 80;
    /**
     * The container object to which the items of the fetched `Page` will be added.
     * The value for "items" property of that object will be set.
     */
    @Input() context: { items: T[] };
    /**
     * Function to fetch the next page when the bottom of the element approaches the bottom of the container.
     * The `currentPage` is determined by the scrolling and starts with 0.
     */
    @Input() fetchPage: (mask: string, pageSize: number, currentPage: number) => Promise<Page<T>>;
    /**
     * Some components may want to do some action when the `items` value changes.
     */
    @Output() itemsChange = new EventEmitter<T[]>();

    /** @internal */
    @ViewChild('container', { static: false }) containerElement: ElementRef<HTMLDivElement>;
    /** @internal */
    @ViewChild('content', { static: false }) contentElement: ElementRef<HTMLDivElement>;

    /** @internal */
    public containerId: string = stringUtils.generateIdentifier();
    /** @internal */
    public items: T[];
    /** @internal */
    public initiated = false;
    /** @internal */
    public currentPage: number;
    /**
     * Set to true when a page is fetched or all pages has been fetched to prevent from fetching next page.
     * @internal
     */
    public pagingDisabled: boolean;
    /**
     * For displaying loading spinner.
     * @internal
     */
    public isLoading = false;
    /**
     * Observes content for `shouldLoadNextPage` to perform calculations based on actual heights of elements.
     * @internal
     */
    private contentResizeObserver: ResizeObserver;

    constructor(
        private discardablePromiseUtils: DiscardablePromiseUtils,
        private cdr: ChangeDetectorRef
    ) {}

    /** @internal */
    ngOnInit() {
        this.init();
    }

    /** @internal */
    ngOnChanges() {
        this.context = this.context || this;
        this.init();
    }

    /** @internal */
    ngAfterViewInit() {
        this.initContentResizeObserver();
    }

    /** @internal */
    ngOnDestroy() {
        this.discardablePromiseUtils.clear(this.containerId);
        this.contentResizeObserver.disconnect();
    }

    /**
     * Fetches next page when the scroll approaches the bottom of scroll container element.
     *
     * Note: For the initial load, when the content inside the directive's element is not tall enough to
     * fill up the entire container, causing the scroll not to appear, it will fetch the next page.
     * This action will only be performed once, so if there's a need to load more pages, you will have to call this function manually.
     */
    public nextPage(): void {
        if (this.pagingDisabled) {
            return;
        }

        this.pagingDisabled = true;
        this.currentPage = this.currentPage + 1;
        this.mask = this.mask || '';
        this.isLoading = true;

        this.discardablePromiseUtils.apply(
            this.containerId,
            this.fetchPage(this.mask, this.pageSize, this.currentPage),
            (page: Page<T>) => {
                page.results = page.results || [];
                page.results.forEach((element: T) => {
                    element.technicalUniqueId = stringUtils.encode(element);
                });

                // incremental load to get only new results when user scrolls down
                const uniqueResults = differenceBy(
                    page.results,
                    this.context.items,
                    'technicalUniqueId' as keyof TechnicalUniqueIdAware
                );

                if (uniqueResults.length > 0) {
                    this.context.items = [...this.context.items, ...(uniqueResults || [])];
                    this.itemsChange.emit(this.context.items);
                }

                this.pagingDisabled =
                    page.results.length === 0 ||
                    (page.pagination && this.context.items.length === page.pagination.totalCount);

                if (this.pagingDisabled) {
                    // no more results or all pages has been fetched
                    this.isLoading = false;
                    this.cdr.detectChanges();
                    return;
                }
            }
        );
    }

    public scrollToTop(): void {
        if (this.containerElement) {
            this.containerElement.nativeElement.scrollTop = 0;
        }
    }

    public scrollToBottom(): void {
        if (this.containerElement) {
            this.containerElement.nativeElement.scrollTop = this.containerElement.nativeElement.scrollHeight;
        }
    }

    /** @internal */
    private init(): void {
        const wasInitiated = this.initiated;

        this.context.items = [];
        this.currentPage = -1;
        this.pagingDisabled = false;
        this.initiated = true;

        if (wasInitiated) {
            this.scrollToTop();
            this.nextPage();
        }
    }

    /** @internal */
    private initContentResizeObserver(): void {
        this.contentResizeObserver = new ResizeObserver((entries) => {
            const { contentRect } = entries[0];

            const isInitialChange = contentRect.height === 0;
            if (!this.pagingDisabled && this.isLoading && !isInitialChange) {
                if (this.shouldLoadNextPage()) {
                    this.nextPage();
                } else {
                    this.isLoading = false;
                    this.cdr.detectChanges();
                }
                // immediate check performed
                this.contentResizeObserver.disconnect();
            }
        });
        this.contentResizeObserver.observe(this.contentElement.nativeElement);
    }

    /**
     * Determines whether to load next page when the content inside the directive's element is not tall enough to
     * fill up the entire container, causing the scroll not to appear.
     * It may happen when the parent component have some condition for displaying certain items of the fetched page.
     * In that case, a next page should be fetched to fill the container height so the scroll can be displayed to a user.
     * The elements height are used for comparison because the content height is dynamic (it depends on the content projected into <ng-content>).
     *
     * Scroll appears when element height is 1px greather than container height.
     *
     * In AngularJS it was handled by ngInfiniteScroll "infiniteScrollImmediateCheck" property.
     * See https://github.com/sroze/ngInfiniteScroll.
     * @internal
     */
    private shouldLoadNextPage(): boolean {
        const contentHeight = this.contentElement.nativeElement.offsetHeight;
        const containerHeight = this.containerElement.nativeElement.offsetHeight;

        const shouldLoad = contentHeight <= containerHeight;
        return shouldLoad;
    }
}
