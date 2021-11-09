/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace SelectObject {
    export const Elements = {
        selectContainer(id: string): ElementFinder {
            return element(by.css(`#${id}`));
        },
        selectDropdownTriggerWithText(id: string, text: string): ElementFinder {
            return Elements.selectContainer(id).element(
                by.cssContainingText('.fd-dropdown__control', text)
            );
        },

        selectDropdownTrigger(id: string): ElementFinder {
            return Elements.selectContainer(id).element(by.css('.fd-dropdown__control'));
        },

        selectDropdownMenu(id: string): ElementFinder {
            return Elements.selectContainer(id).element(by.css('.su-select__menu'));
        },

        selectDropdownMenuItemByValue(id: string, value: string): ElementFinder {
            return Elements.selectContainer(id).element(
                by.cssContainingText('.fd-menu__item', value)
            );
        },

        selectDropdownOutput(id: string, value: string): ElementFinder {
            return element(by.cssContainingText(`#${id}`, value));
        }
    };

    export const Actions = {
        navigateToTestPage(): void {
            browser.get('test/e2e/suSelect/index.html');
        },

        clickSelectDropdownTrigger(id: string): void {
            browser.click(Elements.selectDropdownTrigger(id));
        },

        clickMenuItemByValue(id: string, value: string): void {
            browser.click(Elements.selectDropdownMenuItemByValue(id, value));
        }
    };

    export const Assertions = {
        selectDropdownMenuIsPresent(id: string): void {
            browser.waitForPresence(Elements.selectDropdownMenu(id));
        },

        selectDropdownMenuIsAbsent(id: string): void {
            browser.waitForAbsence(Elements.selectDropdownMenu(id));
        },

        selectDropdownTriggerPlaceholderIsPresent(id: string, text: string): void {
            browser.waitForPresence(Elements.selectDropdownTriggerWithText(id, text));
        },

        selectDropdownOutputIsPresent(id: string, value: string): void {
            browser.waitForPresence(Elements.selectDropdownOutput(id, value));
        }
    };
}
