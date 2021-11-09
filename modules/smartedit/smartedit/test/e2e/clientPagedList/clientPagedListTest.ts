/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element } from 'protractor';

import { ClientPagedListPageObject as PageObject } from './clientPagedListObject';
import { DropdownMenuPageObject } from '../dropdownMenu/dropdownMenuPageObject';

describe('clientPagedList - ', () => {
    describe('GIVEN I am on the page that renders the Client Paged List ', () => {
        describe('', () => {
            beforeEach(async () => {
                await PageObject.Actions.navigateToTestPage();
            });

            it('WHEN the page is fully loaded THEN I expect to see a paginated list of 10 pages max, sorted by name ascending', async () => {
                await PageObject.Assertions.totalRowsCountHasTextByNumber(12);

                expect<any>(await PageObject.Elements.displayedRows().count()).toBe(10);

                // sorted by name ascending
                expect<any>(await PageObject.Elements.firstRowForKey('name').getText()).toBe(
                    'Advertise'
                );
                await PageObject.Actions.navigateToPage(2);
                expect<any>(await PageObject.Elements.lastRowForKey('name').getText()).toBe(
                    'welcomePage'
                );

                expect<any>(await PageObject.Actions.getPaginationCount()).toBe(2);
            });

            it('WHEN I search for a page THEN I expect the list to show the pages that match the query for any header', async () => {
                await browser.waitForAngularEnabled(false);
                // Perform a search on a name
                await PageObject.Actions.searchForPage('welcomepage', 'name', 1);
                expect<any>(await PageObject.Actions.getPaginationCount()).toBe(1);

                // Perform a search on a page UID
                await PageObject.Actions.searchForPage('uid1', 'uid', 4);

                // Perform a search on a page type
                await PageObject.Actions.searchForPage('product', 'typeCode', 4);

                // Perform a search on a page template
                await PageObject.Actions.searchForPage('mycustompagetemplate', 'template', 1);
                expect<any>(await PageObject.Actions.getPaginationCount()).toBe(1);
            });

            it('WHEN I click on the name column header THEN I expect the list to be re-sorted by this key in the descending order', async () => {
                await PageObject.Actions.clickOnColumnHeader('name');
                expect<any>(await PageObject.Elements.firstRowForKey('name').getText()).toBe(
                    'welcomePage'
                );

                await PageObject.Actions.navigateToPage(2);
                expect<any>(await PageObject.Elements.lastRowForKey('name').getText()).toBe(
                    'Advertise'
                );
            });

            it('WHEN I click on the UID column header THEN I expect the list to be re-sorted by this key in the descending order', async () => {
                await PageObject.Actions.clickOnColumnHeader('uid');
                expect<any>(await PageObject.Elements.firstRowForKey('uid').getText()).toBe(
                    'zuid12'
                );
                await PageObject.Actions.navigateToPage(2);
                expect<any>(await PageObject.Elements.lastRowForKey('uid').getText()).toBe('auid1');
            });

            it('WHEN I click on the page type column header THEN I expect the list to be re-sorted by this key in the descending order', async () => {
                // Sorting by page type
                await PageObject.Actions.clickOnColumnHeader('typeCode');
                expect<any>(await PageObject.Elements.firstRowForKey('typeCode').getText()).toBe(
                    'mock-WallPage'
                );
                await PageObject.Actions.navigateToPage(2);
                expect<any>(await PageObject.Elements.lastRowForKey('typeCode').getText()).toBe(
                    'mock-ActionPage'
                );
            });

            it('WHEN I click on the name column header THEN I expect the list to be re-sorted by this key in the descending order', async () => {
                // Sorting by page template
                await PageObject.Actions.clickOnColumnHeader('template');
                expect<any>(await PageObject.Elements.firstRowForKey('template').getText()).toBe(
                    'ZTemplate'
                );
                await PageObject.Actions.navigateToPage(2);
                expect<any>(await PageObject.Elements.lastRowForKey('template').getText()).toBe(
                    'ActionTemplate'
                );
            });

            it('WHEN a column key has specified a component for rendering a cell THEN I expect the component to be rendered', async () => {
                expect<any>(await PageObject.Elements.firstRowForKey('typeCode').getText()).toBe(
                    'mock-MyCustomType'
                );
            });

            describe('Icon', async () => {
                it('WHEN the page is fully loaded THEN I expect to see an icon to be rendered for specific cell', async () => {
                    expect<any>(await PageObject.Elements.iconForRow(1).isDisplayed()).toBe(true);
                });

                it('WHEN I hover on an Icon THEN I expect a tooltip to be displayed with the translated text', async () => {
                    await PageObject.Actions.hoverOnIcon(1);
                    await browser.sleep(5000);
                    expect<any>(await PageObject.Elements.tooltipContainer().getText()).toContain(
                        '2 restrictions on this page'
                    );
                });
            });
        });

        describe('Dropdown Menu button - ', async () => {
            it('WHEN permission service returns true AND I click on more menu button THEN I expect to open a dropdown menu with the list of actions', async () => {
                await PageObject.Actions.navigateToTestPage(true);

                await PageObject.Actions.openMoreMenuFirstElement();
                const dropdownMenuItems = DropdownMenuPageObject.Elements.getItems();
                expect<any>(await dropdownMenuItems.count()).toBe(1);
                expect<any>(await dropdownMenuItems.get(0).getText()).toBe(
                    'pagelist.dropdown.edit'
                );
            });

            it('WHEN permission service returns true AND I click on more menu button AND click on the first one THEN I expect to execute a callback function to remove the items', async () => {
                await PageObject.Actions.navigateToTestPage(true);

                await PageObject.Actions.clickOnFirstDropdownItemOfFirstElement();
                expect<any>(await PageObject.Elements.totalRowsCount().getText()).toBe(
                    '(0 se.pagelist.countsearchresult)'
                );
            });

            it('WHEN permission service returns false THEN I expect that the dropdown menu is not present', async () => {
                await PageObject.Actions.navigateToTestPage(false);

                const el = element.all(by.tagName('se-dropdown-menu'));
                expect(await browser.isAbsent(el.get(0))).toBe(true);
            });
        });
    });
});
