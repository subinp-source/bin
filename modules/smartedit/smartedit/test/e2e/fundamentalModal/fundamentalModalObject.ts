/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder, ExpectedConditions } from 'protractor';

export namespace FundamentalModalObject {
    export const Elements = {
        buttonOpenModal(): ElementFinder {
            return element(by.id('openModal'));
        },

        buttonOpenModalWithPredefinedConfig(): ElementFinder {
            return element(by.id('openModalWithPredefinedConfig'));
        },

        fundamentalModal(): ElementFinder {
            return element(by.id('fundamentalModal'));
        },

        buttonAddModalButton(): ElementFinder {
            return element(by.id('addButton'));
        },

        buttonRemoveAllButtons(): ElementFinder {
            return element(by.id('removeAllButtons'));
        },

        buttonSetModalTitle(): ElementFinder {
            return element(by.id('setTitle'));
        },

        buttonHideDismissButton(): ElementFinder {
            return element(by.id('hideDismissButton'));
        },

        modalButton(label: string): ElementFinder {
            return element(by.cssContainingText('.fd-button', label));
        },

        modalTitle(title: string): ElementFinder {
            return element(by.cssContainingText('.fd-modal__title', title));
        },

        modalDismissButton(): ElementFinder {
            return element(by.css('.fd-modal__close'));
        },

        modalReturnedDataContainer(text: string): ElementFinder {
            return element(by.cssContainingText('#returnedData', text));
        }
    };

    export const Actions = {
        async navigateToTestPage(): Promise<void> {
            await browser.get('test/e2e/fundamentalModal/index.html');
        },

        async openModal(): Promise<void> {
            await browser.click(Elements.buttonOpenModal());
        },

        async openModalWithPredefinedConfig(): Promise<void> {
            await browser.click(Elements.buttonOpenModalWithPredefinedConfig());
        },

        async addButtonToModal(): Promise<void> {
            await browser.click(Elements.buttonAddModalButton());
        },

        async removeAllButtonsFromModal(): Promise<void> {
            await browser.click(Elements.buttonRemoveAllButtons());
        },

        async clickModalButton(label: string): Promise<void> {
            await browser.click(Elements.modalButton(label));
        },

        async clickModalDismissButton(): Promise<void> {
            await browser.click(Elements.modalDismissButton());
        },

        async hideDismissButton(): Promise<void> {
            await browser.click(Elements.buttonHideDismissButton());
        },

        async setTitle(): Promise<void> {
            await browser.click(Elements.buttonSetModalTitle());
        },

        async waitForModalToDisappear(): Promise<void> {
            return await browser.waitUntil(
                ExpectedConditions.invisibilityOf(Elements.fundamentalModal())
            );
        }
    };

    export const Assertions = {
        async modalIsPresent(): Promise<void> {
            await browser.waitForPresence(Elements.fundamentalModal());
        },

        async modalIsAbsent(): Promise<void> {
            await browser.waitForAbsence(Elements.fundamentalModal());
        },

        async modalButtonIsPresent(label: string): Promise<void> {
            await browser.waitForPresence(Elements.modalButton(label));
        },

        async modalButtonIsAbsent(label: string): Promise<void> {
            await browser.waitForAbsence(Elements.modalButton(label));
        },

        async modalTitleIsPresent(title: string): Promise<void> {
            await browser.waitForPresence(Elements.modalTitle(title));
        },

        async modalDismissButtonIsPresent(): Promise<void> {
            await browser.waitForPresence(Elements.modalDismissButton());
        },

        async modalDismissButtonIsAbsent(): Promise<void> {
            await browser.waitForAbsence(Elements.modalDismissButton());
        },

        async modalReturnedDataContainerIsPresent(text: string): Promise<void> {
            await browser.waitForPresence(Elements.modalReturnedDataContainer(text));
        }
    };
}
