/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder } from 'protractor';

export namespace PagedListPageObject {
    export const Elements = {
        totalPageCount(): ElementFinder {
            return element(by.css('.se-page-list__page-count'));
        },
        async displayedPageCount(): Promise<number> {
            const count = await element.all(by.css('.se-paged-list-table tbody tr')).count();

            return count;
        },
        async paginationCount(): Promise<number> {
            const count = await element.all(by.css('.fd-pagination__link')).count();

            return count;
        },
        searchInput(): ElementFinder {
            return element(by.css('.page-list-search > input'));
        },
        columnHeaderForKey(key: string): ElementFinder {
            return element(
                by.css('.se-paged-list-table thead tr:first-child .se-paged-list__header-' + key)
            );
        },
        firstRowForKey(key: string): ElementFinder {
            return element(
                by.css('.se-paged-list-table tbody tr:first-child .se-paged-list-item-' + key)
            );
        },
        lastRowForKey(key: string): ElementFinder {
            return element(
                by.css('.se-paged-list-table tbody tr:last-child .se-paged-list-item-' + key)
            );
        },
        elemForKeyAndRow(key: string, row: number, selector: string): ElementFinder {
            return element(
                by.css(
                    '.se-paged-list-table tbody tr:nth-child(' +
                        row +
                        ') .se-paged-list-item-' +
                        key +
                        ' ' +
                        selector
                )
            );
        },
        catalogName(): ElementFinder {
            return element(by.css('.se-page-list__catalog-name'));
        }
    };

    export const Actions = {
        async openAndBeReady(
            pageListType: string,
            _PERSPECTIVE_SERVICE_RESULT: boolean
        ): Promise<void> {
            const PERSPECTIVE_SERVICE_RESULT = _PERSPECTIVE_SERVICE_RESULT || false;

            await browser.get(
                pageListType === 'clientPagedList'
                    ? `test/e2e/clientPagedList/index.html?perspectiveServiceResult=${PERSPECTIVE_SERVICE_RESULT}`
                    : `test/e2e/dynamicPagedList/index.html?perspectiveServiceResult=${PERSPECTIVE_SERVICE_RESULT}`
            );
        },
        async openMoreMenuFirstElement(): Promise<ElementFinder> {
            const first = await element.all(by.css('.se-dropdown-more-menu')).first();
            await first.click();

            return first;
        },
        async searchForPage(query: string, expectedNumber: number): Promise<void> {
            await Elements.searchInput().clear();
            await Elements.searchInput().sendKeys(query);

            expect(await Elements.totalPageCount().getText()).toBe(
                '(' + expectedNumber.toString() + ' se.pagelist.countsearchresult)'
            );
            expect(await Elements.displayedPageCount()).toBe(expectedNumber);
        },
        async navigateToIndex(index: number): Promise<void> {
            await browser.executeScript('window.scrollTo(0,document.body.scrollHeight);');

            await browser.click(
                element(by.cssContainingText('.fd-pagination__link', index.toString()))
            );
        },
        async clickOnColumnHeader(key: string): Promise<void> {
            await browser.click(Elements.columnHeaderForKey(key));
        }
    };

    export const Assertions = {
        async searchAndAssertCount(query: string, displayedResults: number, totalResults: number) {
            await Elements.searchInput().clear();
            await Elements.searchInput().sendKeys(query);

            await browser.waitForPresence(Elements.totalPageCount());

            expect(await Elements.totalPageCount().getText()).toBe(
                '(' + totalResults.toString() + ')'
            );
            expect(await Elements.displayedPageCount()).toBe(displayedResults);
        }
    };
}
