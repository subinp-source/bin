/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace ConfirmModalServiceObject {
    export const Elements = {
        getLegacyModal(): ElementFinder {
            return element(by.css('.yFrontModal'));
        },

        getModal(): ElementFinder {
            return element(by.css('.se-confirmation-dialog'));
        },

        getTriggerButton(id: number): ElementFinder {
            return element(by.css(`#confirmation-modal-trigger-${id}`));
        },

        getModalTitle(text: string): ElementFinder {
            return element(by.cssContainingText('.fd-modal__title', text));
        },

        getLegacyModalTitle(text: string): ElementFinder {
            return element(by.cssContainingText('.se-modal__header-title', text));
        },

        getModalBody(text: string): ElementFinder {
            return element(by.cssContainingText('.fd-modal__body', text));
        },

        getCancelButton(): ElementFinder {
            return element(by.id('confirmCancel'));
        },

        getConfirmButton(): ElementFinder {
            return element(by.id('confirmOk'));
        }
    };

    export const Actions = {
        async clickTriggerButton(id: number): Promise<void> {
            await browser.click(Elements.getTriggerButton(id));
        },

        async navigateToTestPage(): Promise<void> {
            await browser.get('test/e2e/confirmModalService/index.html');
        },

        async clickCancel(): Promise<void> {
            await browser.click(Elements.getCancelButton());
        },

        async clickConfirm(): Promise<void> {
            await browser.click(Elements.getConfirmButton());
        }
    };

    export const Assertions = {
        async modalIsPresent(isLegacy: boolean): Promise<void> {
            return isLegacy
                ? await browser.waitForPresence(Elements.getLegacyModal())
                : await browser.waitForPresence(Elements.getModal());
        },

        async modalHasCorrectTitle(text: string, isLegacy: boolean): Promise<void> {
            return isLegacy
                ? await browser.waitForPresence(Elements.getLegacyModalTitle(text))
                : await browser.waitForPresence(Elements.getModalTitle(text));
        },

        async modalHasCorrectBody(text: string): Promise<void> {
            await browser.waitForPresence(Elements.getModalBody(text));
        }
    };
}
