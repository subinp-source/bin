/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, Locator } from 'protractor';

export namespace ToolbarItemComponentObject {
    export const Locators = {
        getToolbarItemByNameSelector(name: string): Locator {
            return by.cssContainingText('.se-template-toolbar__action-template span', name);
        }
    };

    export const Assertions = {
        async hasToolbarItemByName(name: string): Promise<void> {
            await browser.waitToBeDisplayed(Locators.getToolbarItemByNameSelector(name));
        }
    };
}
