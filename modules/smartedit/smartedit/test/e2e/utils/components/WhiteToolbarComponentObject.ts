/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder } from 'protractor';

export namespace WhiteToolbarComponentObject {
    export const Actions = {
        async clickShowActionToolbarContext5(): Promise<void> {
            await browser.click(element(by.id('showActionToolbarContext5')));
            await browser.waitForPresence(element(by.id('showActionToolbarContext5')));
        },
        async clickShowHybridActionToolbarContext(): Promise<void> {
            await browser.click(element(by.id('showHybridActionToolbarContext')));
            await browser.waitForPresence(element(by.id('showHybridActionToolbarContext')));
        },
        async clickHideHybridActionToolbarContext(): Promise<void> {
            await browser.click(element(by.id('hideHybridActionToolbarContext')));
            await browser.waitForAbsence(element(by.id('hideHybridActionToolbarContext')));
        }
    };

    export const Assertions = {
        async assertButtonPresent(title: string): Promise<void> {
            await browser.waitForPresence(await Elements.getButtonByTitle(title));
        },
        async assertButtonNotPresent(title: string): Promise<void> {
            await browser.waitForAbsence(await Elements.getButtonByTitle(title));
        }
    };

    export const Elements = {
        smartEditPerspectiveToolbar(): ElementFinder {
            return element(by.css('.se-toolbar--perspective'));
        },
        async getButtonByTitle(title: string): Promise<ElementFinder> {
            await browser.switchToParent();
            return Elements.smartEditPerspectiveToolbar().element(
                by.cssContainingText('button', title)
            );
        },
        renderButton(): Promise<ElementFinder> {
            return this.getButtonByTitle('Render Component');
        },
        renderSlotButton(): Promise<ElementFinder> {
            return this.getButtonByTitle('Render Slot');
        },
        getToolbarItemContextByKey(key: string): ElementFinder {
            return element(by.id('toolbar_item_context_' + key + '_btn'));
        },
        async getToolbarItemContextTextByKey(key: string): Promise<string> {
            const text = await element(by.id('toolbar_item_context_' + key + '_btn')).getText();

            return text;
        }
    };
}
