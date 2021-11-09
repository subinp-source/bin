/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder, ExpectedConditions } from 'protractor';

export namespace Login {
    export const Constants = {
        STORE_FRONT_HOME_PAGE: 'storefront.html'
    };
    export const Elements = {
        // Elements
        mainLoginUsernameInput(): ElementFinder {
            return element(by.id('username_L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4'));
        },
        mainLoginPasswordInput(): ElementFinder {
            return element(by.id('password_L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4'));
        },
        mainLoginSubmitButton(): ElementFinder {
            return element(by.id('submit_L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4'));
        },
        mainLoginSubmitSSOButton(): ElementFinder {
            return element(by.id('submitSSO_L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4'));
        },
        fake1LoginUsernameInput(): ElementFinder {
            return element(by.id('username_L2F1dGhFbnRyeVBvaW50MQ'));
        },
        fake1LoginPasswordInput(): ElementFinder {
            return element(by.id('password_L2F1dGhFbnRyeVBvaW50MQ'));
        },
        fake1LoginSubmitButton(): ElementFinder {
            return element(by.id('submit_L2F1dGhFbnRyeVBvaW50MQ'));
        },
        fake2LoginUsernameInput(): ElementFinder {
            return element(by.id('username_L2F1dGhFbnRyeVBvaW50Mg'));
        },
        fake2LoginPasswordInput(): ElementFinder {
            return element(by.id('password_L2F1dGhFbnRyeVBvaW50Mg'));
        },
        fake2LoginSubmitButton(): ElementFinder {
            return element(by.id('submit_L2F1dGhFbnRyeVBvaW50Mg'));
        },
        requiredError(): ElementFinder {
            return element(by.id('requiredError'));
        },
        authenticationError(): ElementFinder {
            return element(by.id('invalidError'));
        },
        userAccountButton(): ElementFinder {
            return element(by.css('[data-item-key="headerToolbar.userAccountTemplate"] button'));
        },
        logoutButton(): ElementFinder {
            return element(by.css('a.se-sign-out__link'));
        },
        languageSelectorDropdown(): ElementFinder {
            return element(by.css('.su-login-language'));
        },
        languageSelectorDropdownControl(): ElementFinder {
            return Elements.languageSelectorDropdown().element(
                by.css('button.fd-dropdown__control')
            );
        },
        languageSelectorOptionByLanguage(language: string): ElementFinder {
            return Elements.languageSelectorDropdown().element(
                by.cssContainingText('.fd-menu__item', language)
            );
        },
        languageSelectorFirstInList(): ElementFinder {
            return element(by.css('.fd-menu__item:first-child'));
        }
    };

    export const Actions = {
        async logoutUser(): Promise<any> {
            await browser.waitUntilNoModal();
            await browser.switchToParent();

            await browser.click(Elements.userAccountButton());
            await browser.waitUntil(
                ExpectedConditions.elementToBeClickable(Elements.logoutButton()),
                'Timed out waiting for logout button'
            );
            await browser.click(Elements.logoutButton());
        },

        async loginWithSSO(): Promise<any> {
            await browser.waitUntil(
                ExpectedConditions.elementToBeClickable(Elements.mainLoginSubmitSSOButton()),
                'Timed out waiting for SSO submit button'
            );
            await browser.click(
                Elements.mainLoginSubmitSSOButton(),
                'could no click on main login submit SSO button'
            );
        },

        async loginAsUser(username: string, password: string): Promise<any> {
            await browser.waitUntil(
                ExpectedConditions.elementToBeClickable(Elements.mainLoginUsernameInput()),
                'Timed out waiting for username input'
            );

            await Elements.mainLoginUsernameInput().sendKeys(username);

            await Elements.mainLoginPasswordInput().sendKeys(password);

            await browser.click(
                Elements.mainLoginSubmitButton(),
                'could no click on main login submit button'
            );
        },

        async loginAsInvalidUser(): Promise<any> {
            await Actions.loginAsUser('invalid', 'invalid');
        },

        async loginAsCmsManager(): Promise<any> {
            await Actions.loginAsUser('cmsmanager', '1234');
            await browser.waitForWholeAppToBeReady();
        },

        async loginAsAdmin(): Promise<any> {
            await Actions.loginAsUser('admin', '1234');
            await browser.waitForWholeAppToBeReady();
        },

        async loginAsCmsManagerToLandingPage(): Promise<any> {
            await Actions.loginAsUser('cmsmanager', '1234');
            await browser.waitForContainerToBeReady();
        },

        async loginToAuthForFake1(): Promise<any> {
            await browser.waitUntil(
                ExpectedConditions.elementToBeClickable(Elements.fake1LoginUsernameInput()),
                'Timed out waiting for fake 1 username input'
            );
            await Elements.fake1LoginUsernameInput().sendKeys('fake1');
            await Elements.fake1LoginPasswordInput().sendKeys('1234');
            await browser.click(Elements.fake1LoginSubmitButton());
        },

        async loginToAuthForFake2(): Promise<any> {
            await browser.waitUntil(
                ExpectedConditions.elementToBeClickable(Elements.fake2LoginUsernameInput()),
                'Timed out waiting for fake 2 username input'
            );
            await Elements.fake2LoginUsernameInput().sendKeys('fake2');
            await Elements.fake2LoginPasswordInput().sendKeys('1234');
            await browser.click(Elements.fake2LoginSubmitButton());
        },

        async toggleLanguageSelectorDropdown(): Promise<any> {
            await browser.click(Elements.languageSelectorDropdownControl());
        },

        async waitForLanguageSelectorToBePopulated(language: string): Promise<any> {
            await Actions.toggleLanguageSelectorDropdown();
            await browser.waitToBeDisplayed(Elements.languageSelectorOptionByLanguage(language));
            await Actions.toggleLanguageSelectorDropdown();
        }
    };

    export const Assertions = {
        async assertLanguageSelectorLanguage(language: string): Promise<any> {
            const text = await Elements.languageSelectorDropdownControl().getText();

            expect(text).toBe(language);
        },

        async assertLanguageSelectorFirstInList(language: string): Promise<any> {
            const text = await Elements.languageSelectorFirstInList().getText();
            expect(text).toBe(language);
        }
    };
}
