/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace CollapsiblePageObject {
    export const Elements = {
        getHeader(): ElementFinder {
            return element(by.css('.collapsible-container__header'));
        },

        getHeaderIconLeft(): ElementFinder {
            return element(by.css('.collapsible-container__header--icon-left'));
        },

        getHeaderIconRight(): ElementFinder {
            return element(by.css('.collapsible-container__header--icon-right'));
        },

        getHeaderTitle(): ElementFinder {
            return Elements.getHeader().element(by.css('.collapsible-container__header__title'));
        },

        getHeaderButton(): ElementFinder {
            return Elements.getHeader().element(by.css('.collapsible-container__header__button'));
        },

        getHeaderButtonExpanded(): ElementFinder {
            return Elements.getHeaderButton().element(
                by.css('.collapsible-container__header__button--expanded')
            );
        },

        getContent(): ElementFinder {
            return element(by.css('.collapsible-container__content'));
        },

        getHeaderWithText(): ElementFinder {
            return Elements.getHeader().element(by.css('#header-text'));
        },

        getHeaderWithHtml(): ElementFinder {
            return Elements.getHeader().element(by.css('#header-html'));
        },

        getIconAlignmentButtonLeft(): ElementFinder {
            return element(by.css('#iconAlignment-1'));
        },

        getIconAlignmentButtonRight(): ElementFinder {
            return element(by.css('#iconAlignment-2'));
        },

        getIconVisibleButtonTrue(): ElementFinder {
            return element(by.css('#iconVisible-1'));
        },

        getIconVisibleButtonFalse(): ElementFinder {
            return element(by.css('#iconVisible-2'));
        },

        getTitleTextButton(): ElementFinder {
            return element(by.css('#title-text'));
        },

        getTitleHtmlButton(): ElementFinder {
            return element(by.css('#title-html'));
        },

        getTitleNoneButton(): ElementFinder {
            return element(by.css('#title-none'));
        },

        getResetButton(): ElementFinder {
            return element(by.css('#button-reset'));
        }
    };

    export const Actions = {
        async clickTitleText(): Promise<void> {
            await browser.click(Elements.getTitleTextButton());
        },

        async clickTitleHtml(): Promise<void> {
            await browser.click(Elements.getTitleHtmlButton());
        },

        async clickTitleNone(): Promise<void> {
            await browser.click(Elements.getTitleNoneButton());
        },

        async clickResetButton(): Promise<void> {
            await browser.click(Elements.getResetButton());
        },

        async clickIconInvisible(): Promise<void> {
            await browser.click(Elements.getIconVisibleButtonFalse());
        },

        async clickIconVisible(): Promise<void> {
            await browser.click(Elements.getIconVisibleButtonTrue());
        },

        async clickIconAlignentLeft(): Promise<void> {
            await browser.click(Elements.getIconAlignmentButtonLeft());
        },

        async clickIconAlignentRight(): Promise<void> {
            await browser.click(Elements.getIconAlignmentButtonRight());
        },

        async clickHeaderButton(): Promise<void> {
            await browser.click(Elements.getHeaderButton());
        },

        async clickHeader(): Promise<void> {
            await browser.click(Elements.getHeader());
        },

        async navigate(): Promise<void> {
            await browser.get('test/e2e/collapsible/index.html');
        }
    };

    export const Assertions = {
        async iconAlignedRight(): Promise<void> {
            await browser.waitForPresence(Elements.getHeaderIconRight());
        },

        async iconAlignedLeft(): Promise<void> {
            await browser.waitForPresence(Elements.getHeaderIconLeft());
        },

        async iconIsVisible(): Promise<void> {
            await browser.waitForPresence(Elements.getHeaderButton());
        },

        async iconIsInvisible(): Promise<void> {
            await browser.waitForAbsence(Elements.getHeaderButton());
        },

        async headerTextVisible(): Promise<void> {
            await browser.waitForPresence(Elements.getHeaderWithText());
        },

        async headerHtmlVisible(): Promise<void> {
            await browser.waitForPresence(Elements.getHeaderWithHtml());
        },

        async headerNoneVisible(): Promise<void> {
            await browser.waitForAbsence(Elements.getHeaderWithHtml());
            await browser.waitForAbsence(Elements.getHeaderWithText());
        },

        async contentIsInvisible(): Promise<void> {
            const height = await Elements.getContent().getAttribute('clientHeight');

            expect(height).toBe('0');
        },

        async contentIsVisible(): Promise<void> {
            const height = await Elements.getContent().getAttribute('clientHeight');

            expect(height).not.toBe('0');
        }
    };
}
