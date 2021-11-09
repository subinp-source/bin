/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element } from 'protractor';
import { PagedListPageObject } from '../utils/pageObjects/PagedListPageObject';

describe('dynamicPagedList - ', () => {
    const NUMBER_OF_ARROWS_IN_PAGINATION_LIST = 2;

    describe('', () => {
        beforeEach(async () => {
            await PagedListPageObject.Actions.openAndBeReady('dynamicPagedList', false);
        });

        it('GIVEN I am on the page that calls the dynamic paged list directive WHEN the page is fully loaded THEN I expect to see a paginated list of 10 pages max', async () => {
            // Expect the page collection size to be 1000
            expect(await PagedListPageObject.Elements.totalPageCount().getText()).toBe('(1000)');

            // Expect the number of page displayed to be 10
            expect(await PagedListPageObject.Elements.displayedPageCount()).toBe(10);

            // Expect the list to be sorted by name, ascending
            expect(await PagedListPageObject.Elements.firstRowForKey('name').getText()).toBe(
                'item-1'
            );
            await PagedListPageObject.Actions.navigateToIndex(100);
            expect(await PagedListPageObject.Elements.lastRowForKey('name').getText()).toBe(
                'item-999'
            );

            // Expect the pagination navigation to have 100 entries excluding without counting the arrows
            const count = await PagedListPageObject.Elements.paginationCount();
            const pageEntries = count - NUMBER_OF_ARROWS_IN_PAGINATION_LIST;
            // count of ellipsed pagination items

            expect(pageEntries).toBe(5);
        });

        it('GIVEN I am on the page that calls the dynamic paged list directive WHEN I search for a page THEN I expect the list to show the pages that match the query for any header', async () => {
            // Perform a search
            await PagedListPageObject.Assertions.searchAndAssertCount('-99', 10, 11);
            const count = await PagedListPageObject.Elements.paginationCount();
            const pageEntries = count - NUMBER_OF_ARROWS_IN_PAGINATION_LIST;

            expect(pageEntries).toBe(2);
            // Perform a search for key that does not exist
            await PagedListPageObject.Assertions.searchAndAssertCount('uid1', 0, 0);
        });

        it('GIVEN I am on the page that calls the dynamic paged list directive WHEN I click on the name column header THEN I expect the list to be re-sorted by this key in the descending order', async () => {
            // Sorting by name
            const text = await PagedListPageObject.Elements.firstRowForKey('name').getText();

            expect(text.toLowerCase()).toBe('item-1');

            await PagedListPageObject.Actions.clickOnColumnHeader('name');
            const text2 = await PagedListPageObject.Elements.firstRowForKey('name').getText();

            expect(text2.toLowerCase()).toBe('item-999');

            await PagedListPageObject.Actions.navigateToIndex(100);
            expect(await PagedListPageObject.Elements.lastRowForKey('name').getText()).toBe(
                'item-1'
            );
        });

        it('GIVEN I am on the page that calls the dynamic paged list directive WHEN I click on the uid column header THEN I expect the list to not be re-sorted as it is not sortable', async () => {
            // Sorting by name
            const text = await PagedListPageObject.Elements.firstRowForKey('uid').getText();

            expect(text.toLowerCase()).toBe('item-1');

            await PagedListPageObject.Actions.clickOnColumnHeader('uid');

            const text1 = await PagedListPageObject.Elements.firstRowForKey('name').getText();

            expect(text1.toLowerCase()).toBe('item-1');
        });

        it('GIVEN I have registered a custom renderer for a key with an on click event and a callback function WHEN I click on this item THEN I expect the on click event to call the callback function', async () => {
            // Click on the first page name link
            const firstPageNameLink = PagedListPageObject.Elements.elemForKeyAndRow('name', 1, 'a');
            await browser.click(firstPageNameLink);

            // Expect the provided function in the on click to update the link style
            expect(await firstPageNameLink.getAttribute('class')).toMatch('visited');
        });

        it('GIVEN I have registered a custom renderer for a key with inline styling WHEN I am on the page list THEN I expect the element to be rendered with my custom styling', async () => {
            const firstPageNameLink = PagedListPageObject.Elements.elemForKeyAndRow(
                'uid',
                1,
                'span'
            );

            expect(await firstPageNameLink.getAttribute('class')).toMatch('custom');
        });

        it('GIVEN I am on the page list WHEN permission service returns false THEN I expect to a drop down is not present', async () => {
            const el = element.all(by.css('.se-dropdown-more-menu'));

            await browser.waitForAbsence(el.get(0));
        });
    });

    describe('', () => {
        beforeEach(async () => {
            await PagedListPageObject.Actions.openAndBeReady('dynamicPagedList', true);
        });

        it('GIVEN I am on the page list WHEN permission service returns true AND I click on more menu button THEN I expect to open a drop down with the list of actions', async () => {
            const el = await PagedListPageObject.Actions.openMoreMenuFirstElement();
            const dropDown = el.all(by.css('.se-dropdown-menu__list se-dropdown-menu-item'));

            expect(await dropDown.count()).toBe(1);
            expect(await dropDown.get(0).getText()).toBe('pagelist.dropdown.edit');
        });

        it('GIVEN I am on the page list WHEN permission service returns true AND I click on more menu button AND click on the first one THEN I expect to execute a callback function to be called which changes the style of the corresponding element', async () => {
            const el = await PagedListPageObject.Actions.openMoreMenuFirstElement();
            const dropDown = el.all(by.css('.se-dropdown-menu__list se-dropdown-menu-item'));

            await dropDown
                .get(0)
                .getText()
                .click();

            const firstPageNameLink = PagedListPageObject.Elements.elemForKeyAndRow('name', 1, 'a');
            expect(await firstPageNameLink.getAttribute('class')).toMatch('link-clicked');
        });
    });
});
