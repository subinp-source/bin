/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace ReloadPageObject {
    export const Elements = {
        getNameInput(): ElementFinder {
            return element(by.css("[id='name-shortstring']"));
        },
        getHeadline(): ElementFinder {
            return element(by.css("[id='headline-shortstring']"));
        },
        getGenericEditorStatus(): ElementFinder {
            return element(by.css('.generic-editor-status'));
        },
        getNewStructureButton(): ElementFinder {
            return element(by.id('set-new-structure-button'));
        },
        getNewStructureApiButton(): ElementFinder {
            return element(by.id('set-new-structure-api-button'));
        },
        getPostModeButton(): ElementFinder {
            return element(by.id('post-mode-button'));
        },
        getReloadButton(): ElementFinder {
            return element(by.id('load-button'));
        },
        getSubmitButton(): ElementFinder {
            return element(by.id('submit'));
        },
        getRichText(): ElementFinder {
            return element(by.tagName('se-rich-text-field'));
        },
        getActiveCheckbox(): ElementFinder {
            return element(by.css('.se-boolean__input'));
        },
        getTextArea(): ElementFinder {
            return element(by.tagName('textarea'));
        },
        getCustomFieldInput(): ElementFinder {
            return element(by.css("[id='componentCustomField-shortstring']"));
        },
        getControlLabel(): ElementFinder {
            return element(by.css('.se-control-label'));
        },
        getGenericEditor(): ElementFinder {
            return element(by.css('.se-generic-editor__body'));
        }
    };
    export const Actions = {
        async openAndBeReady(): Promise<void> {
            await browser.get('test/e2e/genericEditor/reload/index.html');
            await browser.waitForPresence(by.css('.se-generic-editor__form'));
            await browser.waitUntilNoModal();
        },
        async setNewStructure(): Promise<void> {
            await browser.click(Elements.getNewStructureButton());
        },
        async setNewStructureApi(): Promise<void> {
            await browser.click(Elements.getNewStructureApiButton());
        },
        async setPOSTMode(): Promise<void> {
            await browser.click(Elements.getPostModeButton());
        },
        async clickReloadButton(): Promise<void> {
            await browser.click(Elements.getReloadButton());
        },
        async clickLabel(): Promise<void> {
            await browser.click(Elements.getControlLabel());
        },
        async clickGenericEditor(): Promise<void> {
            await browser.click(Elements.getGenericEditor());
        },
        async submit(): Promise<void> {
            await browser.click(Elements.getSubmitButton());
            await browser.waitUntilNoModal();
        }
    };
    export const Assertions = {
        async assertNameInputIsDisplayed(): Promise<void> {
            expect(await Elements.getNameInput().isPresent()).toBe(true);
        },
        async assertNameInputIsNotDisplayed(): Promise<void> {
            await browser.waitForAbsence(Elements.getNameInput());
        },
        async assertRichTextEditorIsDisplayed(): Promise<void> {
            await browser.waitForPresence(Elements.getRichText());
        },
        async assertHeadlineIsDisplayed(): Promise<void> {
            expect(await Elements.getHeadline().isPresent()).toBe(true);
        },
        async assertActiveCheckboxIsDisplayed(): Promise<void> {
            expect(await Elements.getActiveCheckbox().isPresent()).toBe(true);
        },
        async assertTextAreaIsDisplayed(): Promise<void> {
            expect(await Elements.getTextArea().isPresent()).toBe(true);
        }
    };
}
