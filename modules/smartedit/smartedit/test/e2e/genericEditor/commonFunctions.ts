/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    browser,
    by,
    element,
    ElementArrayFinder,
    ElementFinder,
    ExpectedConditions
} from 'protractor';

export namespace CommonFunctions {
    export const getLocalizedTabElement = (language: string, qualifier: string): ElementFinder => {
        return element(by.xpath('//*[@id="' + qualifier + '"]//li[@tab-id="' + language + '"]'));
    };

    export const selectLocalizedTab = async (
        language: string,
        qualifier: string,
        isHidden: boolean
    ): Promise<void> => {
        if (isHidden) {
            await browser.click(
                by.css(`#${qualifier} .caret`),
                'could not click on tab ' + language
            );
        }

        await browser.click(getLocalizedTabElement(language, qualifier));
    };

    export const switchToIframeForRichTextAndAddContent = async (
        iframeId: string,
        content: string
    ): Promise<void> => {
        await browser.waitUntil(ExpectedConditions.presenceOf(element(by.css(iframeId))));
        await browser.switchTo().frame(element(by.css(iframeId)).getWebElement());
        await browser.driver.findElement(by.tagName('body')).sendKeys(content);
    };

    export const switchToIframeForRichTextAndValidateContent = async (
        iframeId: string,
        content: string
    ): Promise<void> => {
        await browser.waitUntil(ExpectedConditions.presenceOf(element(by.css(iframeId))));
        await browser.switchTo().frame(element(by.css(iframeId)).getWebElement());

        expect(await browser.driver.findElement(by.tagName('body')).getText()).toEqual(content);
    };

    export const getValidationErrorElements = (qualifier: string): ElementArrayFinder => {
        return element.all(
            by.css(
                '[id="' +
                    qualifier +
                    '"] se-generic-editor-field-messages span.se-help-block--has-error'
            )
        );
    };

    export const getValidationErrorElementByLanguage = (
        qualifier: string,
        language: string
    ): ElementFinder => {
        return element(
            by.css(
                '[tab-id="' +
                    language +
                    '"] [validation-id="' +
                    qualifier +
                    '"] se-generic-editor-field-messages .se-help-block--has-error'
            )
        );
    };

    export const clickSubmit = async (): Promise<void> => {
        await browser.click(by.id('submit'));
    };
}
