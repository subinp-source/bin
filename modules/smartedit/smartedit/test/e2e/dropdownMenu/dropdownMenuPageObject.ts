/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace DropdownMenuPageObject {
    export const Elements = {
        componentBasedDropdownMenuItemButton(): ElementFinder {
            return element(by.id('test-dropdown-menu-item-component-button'));
        },

        defaultComponentBasedDropdownMenuItemButton(): ElementFinder {
            return element(by.id('test-dropdown-menu-item-default-component-button'));
        },

        templateBasedDropdownMenuItemButton(): ElementFinder {
            return element(by.id('test-dropdown-menu-item-template-button'));
        },

        templateUrlBasedDropdownMenuItemButton(): ElementFinder {
            return element(by.id('test-dropdown-menu-item-templateurl-button'));
        },

        dropdownMenuComponent(): ElementFinder {
            return element(by.tagName('se-dropdown-menu'));
        },

        getDropdownMenuList(): ElementFinder {
            return element(by.css('.se-dropdown-menu__list'));
        },

        getItems(): ElementArrayFinder {
            return element.all(by.css('.se-dropdown-menu__item-wrapper'));
        },

        getItemByIndex(index: number): ElementFinder {
            return Elements.getItems().get(index);
        }
    };

    export const Actions = {
        async navigateToTestPage(): Promise<void> {
            await browser.get('test/e2e/dropdownMenu/index.html');
        },

        async openWithComponentBasedDropdownMenuItem(): Promise<void> {
            await browser.click(Elements.componentBasedDropdownMenuItemButton());
            await browser.click(Elements.dropdownMenuComponent());
        },

        async openWithDefaultComponentBasedDropdownMenuItem(): Promise<void> {
            await browser.click(Elements.defaultComponentBasedDropdownMenuItemButton());
            await browser.click(Elements.dropdownMenuComponent());
        },

        async openWithTemplateBasedDropdownMenuItem(): Promise<void> {
            await browser.click(Elements.templateBasedDropdownMenuItemButton());
            await browser.click(Elements.dropdownMenuComponent());
        },

        async openWithTemplateUrlBasedDropdownMenuItem(): Promise<void> {
            await browser.click(Elements.templateUrlBasedDropdownMenuItemButton());
            await browser.click(Elements.dropdownMenuComponent());
        },

        async clickOutsideDropdownMenuComponent(): Promise<void> {
            await browser
                .actions()
                .mouseMove(Elements.dropdownMenuComponent())
                .mouseMove({ x: -10, y: -10 })
                .click()
                .perform();
        },

        async clickOnListItemByIndex(index: number): Promise<void> {
            await browser.click(Elements.getItemByIndex(index));
        }
    };

    export const Assertions = {
        async itemContainsGivenTagByIndex(index: number, tagName: string): Promise<void> {
            const isDisplayed = await Elements.getItemByIndex(index)
                .element(by.tagName(tagName))
                .isDisplayed();

            expect(isDisplayed).toBe(true);
        },

        async itemHasTextByIndex(index: number, expectedText: string): Promise<void> {
            expect(await Elements.getItemByIndex(index).getText()).toContain(expectedText);
        },

        async dropdownMenuListIsAbsent(): Promise<void> {
            expect(await browser.isAbsent(Elements.getDropdownMenuList())).toBe(true);
        }
    };
}
