/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TabsObject } from './tabsObject';
import { browser } from 'protractor';

describe('Tabs', () => {
    beforeEach(async () => {
        await TabsObject.Actions.navigateToTestPage();
        await browser.waitUntilNoModal();
    });

    describe('AngularJS Tabs', () => {
        const TABS_ONE = 'tabs-one';

        it('will display all the visible tabs', async () => {
            await TabsObject.Assertions.tabIsPresent(TABS_ONE, 'Tab 1');
            await TabsObject.Assertions.tabIsPresent(TABS_ONE, 'Tab 2');
            await TabsObject.Assertions.hasVisibleTabsCount(TABS_ONE, 2);
        });

        it('will display not-visible tabs in a drop-down', async () => {
            await TabsObject.Actions.toggleDropdown(TABS_ONE);

            await TabsObject.Assertions.hasDropdownTabsCount(TABS_ONE, 2);
            await TabsObject.Assertions.dropdownTabIsPresent(TABS_ONE, 'Tab 3');
            await TabsObject.Assertions.dropdownTabIsPresent(TABS_ONE, 'Tab 4');
        });

        it('will change content when clicking other tab', async () => {
            await TabsObject.Assertions.contentIsPresent(TABS_ONE, 'Tab 1 Content');
            await TabsObject.Actions.switchTab(TABS_ONE, 'Tab 2');
            await TabsObject.Assertions.contentIsPresent(TABS_ONE, 'Tab 2 Content');
        });

        it('will change content when clicked in tab in the drop-down', async () => {
            await TabsObject.Assertions.contentIsPresent(TABS_ONE, 'Tab 1 Content');
            await TabsObject.Actions.toggleDropdown(TABS_ONE);
            await TabsObject.Actions.switchDropdownTab(TABS_ONE, 'Tab 3');
            await TabsObject.Assertions.contentIsPresent(TABS_ONE, 'Tab 3 Content');
        });

        it('will show errors in tab', async () => {
            await TabsObject.Actions.addError(0);
            await TabsObject.Assertions.visibleTabHasError(TABS_ONE, 'Tab 1');
        });

        it('will show error on drop-down and MORE header', async () => {
            await TabsObject.Actions.addError(2);
            await TabsObject.Actions.toggleDropdown(TABS_ONE);

            await TabsObject.Assertions.dropdownTabHasError(TABS_ONE, 'Tab 3');
            await TabsObject.Assertions.dropdownToggleHasError(TABS_ONE);
        });

        it('can reset errors in tab and in MORE header', async () => {
            await TabsObject.Actions.addError(0);

            await TabsObject.Assertions.visibleTabHasError(TABS_ONE, 'Tab 1');
            await TabsObject.Actions.resetErrors();
            await TabsObject.Assertions.visibleTabDoesntHaveError(TABS_ONE, 'Tab 1');

            await TabsObject.Actions.addError(2);
            await TabsObject.Actions.toggleDropdown(TABS_ONE);
            await TabsObject.Assertions.dropdownTabHasError(TABS_ONE, 'Tab 3');
            await TabsObject.Assertions.dropdownToggleHasError(TABS_ONE);

            await TabsObject.Actions.resetErrors();

            await TabsObject.Assertions.dropdownTabDoesntHaveError(TABS_ONE, 'Tab 3');
            await TabsObject.Assertions.dropdownToggleDoesntHaveError(TABS_ONE);
        });
    });

    describe('Angular Tabs', () => {
        const TABS_TWO = 'tabs-two';

        it('will display all the visible tabs', async () => {
            await TabsObject.Assertions.tabIsPresent(TABS_TWO, 'Tab 1');
            await TabsObject.Assertions.tabIsPresent(TABS_TWO, 'Tab 2');
            await TabsObject.Assertions.hasVisibleTabsCount(TABS_TWO, 2);
        });

        it('will display not-visible tabs in a drop-down', async () => {
            await TabsObject.Actions.toggleDropdown(TABS_TWO);

            await TabsObject.Assertions.hasDropdownTabsCount(TABS_TWO, 2);
            await TabsObject.Assertions.dropdownTabIsPresent(TABS_TWO, 'Tab 3');
            await TabsObject.Assertions.dropdownTabIsPresent(TABS_TWO, 'Tab 4');
        });

        it('will change content when clicking other tab', async () => {
            await TabsObject.Assertions.contentIsPresent(TABS_TWO, 'Tab 1 Content');
            await TabsObject.Actions.switchTab(TABS_TWO, 'Tab 2');
            await TabsObject.Assertions.contentIsPresent(TABS_TWO, 'Tab 2 Content');
        });

        it('will change content when clicked in tab in the drop-down', async () => {
            await TabsObject.Assertions.contentIsPresent(TABS_TWO, 'Tab 1 Content');
            await TabsObject.Actions.toggleDropdown(TABS_TWO);
            await TabsObject.Actions.switchDropdownTab(TABS_TWO, 'Tab 3');
            await TabsObject.Assertions.contentIsPresent(TABS_TWO, 'Tab 3 Content');
        });
    });
});
