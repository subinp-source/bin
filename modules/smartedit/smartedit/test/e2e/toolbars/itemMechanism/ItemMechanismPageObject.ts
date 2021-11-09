/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace ItemMechanismPageObject {
    export const Elements = {
        getMainToolbar(): ElementFinder {
            return element(by.css('.se-toolbar--shell'));
        },

        getToolbarItems(): ElementArrayFinder {
            return Elements.getMainToolbar().all(by.css('.se-template-toolbar'));
        },

        getSendActionOuterButton(): ElementFinder {
            return element(by.id('sendActionsOuter'));
        },

        getSendActionInnerButton(): ElementFinder {
            return element(by.id('sendActionsInner'));
        },

        getClearAllButton(): ElementFinder {
            return element(by.id('clear-toolbar'));
        },

        getHybridActionButton(): ElementFinder {
            return element(by.css('.toolbar-action--hybrid button'));
        },

        getHybridActionTemplate(): ElementFinder {
            return element(by.id('hybridActiontemplate'));
        },

        getToolbarActionItem(id: string): ElementFinder {
            return element(by.id(`smartEditHeaderToolbar_option_toolbar.action.${id}`));
        },

        getMessage(): ElementFinder {
            return element(by.id('message'));
        },

        getClickOutsideTarget(): ElementFinder {
            return element(by.css('.click-outside-target'));
        }
    };

    export const Actions = {
        async navigate(): Promise<void> {
            await browser.get('test/e2e/toolbars/itemMechanism/index.html');
            await browser.waitForWholeAppToBeReady();
            await browser.click(Elements.getClearAllButton());
        },

        async clickSendActionsOuterButton(): Promise<void> {
            await browser.click(Elements.getSendActionOuterButton());
        },

        async clickSendActionsInnerButton(): Promise<void> {
            await browser.click(Elements.getSendActionInnerButton());
        },

        async clickHybridActionButton(): Promise<void> {
            await browser.click(Elements.getHybridActionButton());
        },

        async clickAction(id: string): Promise<void> {
            await browser.click(Elements.getToolbarActionItem(id));
        },

        async clickOutside(): Promise<void> {
            await browser.click(Elements.getClickOutsideTarget());
        }
    };

    export const Assertions = {
        async assertTotalItemsCount(count: number) {
            expect(await Elements.getToolbarItems().count()).toBe(count);
        },

        async assertHybridActionTemplateHasText(text: string) {
            expect(await Elements.getHybridActionTemplate().getText()).toBe(text);
        },

        async assertHybricActionTemplateIsAbsent() {
            await browser.waitForAbsence(Elements.getHybridActionTemplate());
        },

        async assertActionHasCorrectAttribute(
            id: string,
            attr: string,
            value: string
        ): Promise<void> {
            expect(await Elements.getToolbarActionItem(id).getAttribute(attr)).toContain(value);
        },

        async messageHasText(text: string): Promise<void> {
            expect(await Elements.getMessage().getText()).toContain(text);
        }
    };
}
