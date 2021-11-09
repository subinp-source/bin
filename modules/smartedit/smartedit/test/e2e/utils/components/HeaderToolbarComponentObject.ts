/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder, Locator } from 'protractor';
import { Configurations } from './Configurations';

export namespace HeaderToolbarComponentObject {
    export const Elements = {
        getUserAccountButton(): ElementFinder {
            return element(by.css('[data-item-key="headerToolbar.userAccountTemplate"] button'));
        },
        getLogoutButton(): ElementFinder {
            return element(by.css('a.se-sign-out__link'));
        },
        getLanguageSelector(css: string): ElementFinder {
            return element(by.css(css));
        }
    };

    export const Actions = {
        async clickOnLogout(): Promise<void> {
            await browser.waitUntilNoModal();
            await browser.click(Elements.getUserAccountButton());
            await browser.waitForPresence(Elements.getLogoutButton());
            await browser.click(Elements.getLogoutButton());
        }
    };

    export const Assertions = {
        async assertConfigurationCenterIsAbsent(): Promise<void> {
            await browser.waitForAbsence(Configurations.Elements.getConfigurationCenterButton());
        },
        async assertLanguageSelectorIsPresent(css: string): Promise<void> {
            await browser.waitForPresence(Elements.getLanguageSelector(css));
        },
        async waitForUrlToMatch(): Promise<void> {
            await browser.waitForUrlToMatch(/^(?!.*storefront)/);
        },
        async localizedFieldIsTranslated(_by: Locator, expectedText: string): Promise<void> {
            await browser.waitUntil(async () => {
                const actualText = await element(_by).getText();

                return actualText === expectedText;
            });
        }
    };
}
