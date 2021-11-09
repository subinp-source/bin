/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder } from 'protractor';

export namespace HttpErrorInterceptorPageObject {
    export const Elements = {
        getTriggerError404JsonButton(): ElementFinder {
            return element(by.id('trigger-error-404-json'));
        },
        getTriggerError400JsonButton(): ElementFinder {
            return element(by.id('trigger-error-400-json'));
        },
        getTriggerError404HtmlButton(): ElementFinder {
            return element(by.id('trigger-error-404-html'));
        },
        getTriggerError501JsonButton(): ElementFinder {
            return element(by.id('trigger-error-501-json'));
        },
        getTriggerError503(): ElementFinder {
            return element(by.id('trigger-error-503'));
        },
        getTriggerError502(): ElementFinder {
            return element(by.id('trigger-error-502'));
        },
        getGraceFulDegradationStatus(): ElementFinder {
            return element(by.id('gd-status'));
        }
    };

    export const Actions = {
        async navigate(): Promise<void> {
            await browser.get('test/e2e/a_httpErrorInterceptorService/index.html');
        },
        async triggerError404Json(): Promise<void> {
            await browser.waitUntilNoModal();
            await browser.click(Elements.getTriggerError404JsonButton());
        },
        async triggerError400Json(): Promise<void> {
            await browser.waitUntilNoModal();
            await browser.click(Elements.getTriggerError400JsonButton());
        },
        async triggerError404Html(): Promise<void> {
            await browser.waitUntilNoModal();
            await browser.click(Elements.getTriggerError404HtmlButton());
        },
        async triggerError501Json(): Promise<void> {
            await browser.waitUntilNoModal();
            await browser.click(Elements.getTriggerError501JsonButton());
        },
        async triggerError503(): Promise<void> {
            await browser.waitUntilNoModal();
            await browser.click(Elements.getTriggerError503());
        },
        async triggerError502(): Promise<void> {
            await browser.waitUntilNoModal();
            await browser.click(Elements.getTriggerError502());
        }
    };
}
