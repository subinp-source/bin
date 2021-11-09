/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace TabsObject {
    export const Elements = {
        getTabsWrapper(id: string): ElementFinder {
            return element(by.css(`#${id}`));
        },

        getVisibleTab(id: string, text: string): ElementFinder {
            return Elements.getTabsWrapper(id).element(
                by.cssContainingText('.se-tabset__tab a', text)
            );
        },

        getVisibleTabWithError(id: string, text: string): ElementFinder {
            return Elements.getTabsWrapper(id).element(
                by.cssContainingText('.se-tabset__tab a.sm-tab-error', text)
            );
        },

        getDropdownTabWithError(id: string, text: string): ElementFinder {
            return Elements.getTabsWrapper(id).element(
                by.cssContainingText('.se-tabset__select .fd-menu__item.sm-tab-error', text)
            );
        },

        getDropdownTab(id: string, text: string): ElementFinder {
            return Elements.getTabsWrapper(id).element(
                by.cssContainingText('.se-tabset__select .fd-menu__item', text)
            );
        },

        getAllVisibleTabs(id: string): ElementArrayFinder {
            return Elements.getTabsWrapper(id).all(by.css('.se-tabset__tab a'));
        },

        getAllDropdownTabs(id: string): ElementArrayFinder {
            return Elements.getTabsWrapper(id).all(by.css('.se-tabset__select .fd-menu__item'));
        },

        getVisibleTabHeader(id: string, num: number): ElementFinder {
            return Elements.getTabsWrapper(id).element(
                by.css(`ul.nav.nav-tabs li:nth-child(${num}) a`)
            );
        },

        getTabContentData(): ElementFinder {
            return element(by.css('tab-content-data'));
        },

        getDropdownToggle(id: string): ElementFinder {
            return Elements.getTabsWrapper(id).element(by.css('li a.dropdown-toggle'));
        },

        getDropdownToggleWithError(id: string): ElementFinder {
            return Elements.getTabsWrapper(id).element(by.css('li a.dropdown-toggle.sm-tab-error'));
        },

        getTabContent(id: string): ElementFinder {
            return Elements.getTabsWrapper(id).element(by.css('se-tab:not([hidden])'));
        },

        getErrorButton(id: number): ElementFinder {
            return element(by.css(`#add-error-${id}`));
        },

        getResetErrorsButton(): ElementFinder {
            return element(by.css(`#reset-errors`));
        }
    };

    export const Actions = {
        async navigateToTestPage(): Promise<void> {
            await browser.get('test/e2e/tabs/index.html');
        },

        async toggleDropdown(id: string): Promise<void> {
            await browser.click(Elements.getDropdownToggle(id));
        },

        async switchTab(id: string, text: string) {
            await browser.click(Elements.getVisibleTab(id, text));
        },

        async switchDropdownTab(id: string, text: string) {
            await browser.click(Elements.getDropdownTab(id, text));
        },

        async addError(id: number) {
            await browser.click(Elements.getErrorButton(id));
        },

        async resetErrors() {
            await browser.click(Elements.getResetErrorsButton());
        }
    };

    export const Assertions = {
        async tabIsPresent(id: string, text: string) {
            await browser.waitForPresence(Elements.getVisibleTab(id, text));
        },

        async dropdownTabIsPresent(id: string, text: string) {
            await browser.waitForPresence(Elements.getDropdownTab(id, text));
        },

        async hasVisibleTabsCount(id: string, quantity: number) {
            const count = await Elements.getAllVisibleTabs(id).count();
            expect(count).toBe(quantity);
        },

        async hasDropdownTabsCount(id: string, quantity: number) {
            const count = await Elements.getAllDropdownTabs(id).count();
            expect(count).toBe(quantity);
        },

        async contentIsPresent(id: string, text: string) {
            const innerText = await Elements.getTabContent(id).getAttribute('innerText');
            expect(innerText).toBe(text);
        },

        async visibleTabHasError(id: string, text: string) {
            await browser.waitForPresence(Elements.getVisibleTabWithError(id, text));
        },

        async visibleTabDoesntHaveError(id: string, text: string) {
            await browser.waitForAbsence(Elements.getVisibleTabWithError(id, text));
        },

        async dropdownTabHasError(id: string, text: string) {
            await browser.waitForPresence(Elements.getDropdownTabWithError(id, text));
        },

        async dropdownTabDoesntHaveError(id: string, text: string) {
            await browser.waitForAbsence(Elements.getDropdownTabWithError(id, text));
        },

        async dropdownToggleHasError(id: string) {
            await browser.waitForPresence(Elements.getDropdownToggleWithError(id));
        },

        async dropdownToggleDoesntHaveError(id: string) {
            await browser.waitForAbsence(Elements.getDropdownToggleWithError(id));
        }
    };
}
