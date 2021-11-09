/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ExpectedConditions } from 'protractor';

export namespace Page {
    export const Actions = {
        async getAndWaitForWholeApp(url: string): Promise<void> {
            await browser.get(url);
            await browser.waitForWholeAppToBeReady();
        },
        async getAndWaitForLogin(url: string): Promise<void> {
            await browser.get(url);
            await browser.clearLocalStorage();
            await browser.waitForAngularEnabled(false);
            await Actions.waitForLoginModal();
        },
        async waitForLoginModal(): Promise<void> {
            await browser.waitUntil(
                ExpectedConditions.elementToBeClickable(element(by.css('input[id^="username_"]'))),
                'Timed out waiting for username input'
            );
            await browser.waitForAngular();
        },
        async setWaitForPresence(implicitWait: number): Promise<void> {
            await browser.driver
                .manage()
                .timeouts()
                .implicitlyWait(implicitWait);
        },
        async clearCookies(): Promise<void> {
            await browser.waitUntil(() => {
                return browser.driver
                    .manage()
                    .deleteAllCookies()
                    .then(
                        () => {
                            return true;
                        },
                        (err) => {
                            throw err;
                        }
                    );
            }, 'Timed out waiting for cookies to clear');
        }
    };
}
