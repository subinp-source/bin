/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, promise, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace ClientPagedListPageObject {
    const Constants = {
        NUMBER_OF_ARROWS_IN_PAGINATION_LIST: 2
    };

    export const Elements = {
        totalRowsCount(): ElementFinder {
            return element(by.css('.se-page-list__page-count span'));
        },

        displayedRows(): ElementArrayFinder {
            return element.all(by.css('.se-paged-list-table tbody tr'));
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

        catalogName(): ElementFinder {
            return element(by.css('.se-page-list__catalog-name'));
        },

        dropdownMenuItems(openedDropdownMenu: any): ElementArrayFinder {
            return openedDropdownMenu.all(
                by.css('.se-dropdown-menu__list se-dropdown-menu-item a')
            );
        },

        paginationLinks(): ElementArrayFinder {
            return element.all(by.css('.fd-pagination__link[role="button"]'));
        },

        iconForRow(row: number): ElementFinder {
            return element(
                by.css(`.se-paged-list-table tbody tr:nth-child(${row}) se-tooltip img`)
            );
        },

        tooltipContainer(): ElementFinder {
            return element(by.tagName(`fd-popover-container`));
        }
    };

    export const Actions = {
        async navigateToTestPage(PERSPECTIVE_SERVICE_RESULT = false): Promise<void> {
            const pageURI = `test/e2e/clientPagedList/index.html?perspectiveServiceResult=${PERSPECTIVE_SERVICE_RESULT}`;
            await browser.get(pageURI);
        },

        async searchForPage(
            query: string,
            columnHeader: string,
            expectedNumber: number
        ): Promise<void> {
            await Elements.searchInput().clear();
            await Elements.searchInput().sendKeys(query);

            await Assertions.totalRowsCountHasTextByNumber(expectedNumber);
            expect<any>(await Elements.displayedRows().count()).toBe(expectedNumber);

            const text = await Elements.firstRowForKey(columnHeader).getText();

            expect(text.toLowerCase().indexOf(query) >= 0).toBeTruthy();
        },

        async clickOnColumnHeader(key: string): promise.Promise<any> {
            await browser.click(Elements.columnHeaderForKey(key));
        },

        async getPaginationCount(): Promise<number> {
            const count = await Elements.paginationLinks().count();

            return count - Constants.NUMBER_OF_ARROWS_IN_PAGINATION_LIST;
        },

        async openMoreMenuFirstElement(): Promise<ElementFinder> {
            const elem = element.all(by.css('.se-dropdown-more-menu')).first();
            await elem.click();

            return elem;
        },

        async clickOnFirstDropdownItemOfFirstElement(): Promise<void> {
            const openedDropdownMenu = await Actions.openMoreMenuFirstElement();
            await browser.click(Elements.dropdownMenuItems(openedDropdownMenu).first());
        },

        async navigateToPage(pageNumber: number): Promise<void> {
            const index = pageNumber + 1;
            await browser.executeScript('window.scrollTo(0,document.body.scrollHeight);');

            await browser.click(
                element(
                    by.css(
                        `.pagination-container  .fd-pagination__link[role="button"]:nth-child(${index})`
                    )
                )
            );
        },

        async hoverOnIcon(row: number): Promise<void> {
            await browser
                .actions()
                .mouseMove(Elements.iconForRow(row))
                .perform();
        }
    };

    export const Assertions = {
        async totalRowsCountHasTextByNumber(expectedNumber: number): Promise<void> {
            expect<any>(await Elements.totalRowsCount().getText()).toBe(
                `(${String(expectedNumber)} se.pagelist.countsearchresult)`
            );
        }
    };
}
