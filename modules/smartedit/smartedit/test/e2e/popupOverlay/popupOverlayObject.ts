/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace PopupOverlayObject {
    export const Constants = {
        type: 'legacy'
    };

    export const Elements = {
        getPopupOverlayAnchor(): ElementFinder {
            return element(by.css(`#${Constants.type}Popup #anchor`));
        },

        getPopup(text: string): ElementFinder {
            return element(by.cssContainingText('#popup', text));
        },

        getLegacyRadioButton(): ElementFinder {
            return element(by.css('#type-legacy'));
        },

        getAngularRadioButton(): ElementFinder {
            return element(by.css('#type-angular'));
        },

        getTriggerClickRadioButton(): ElementFinder {
            return element(by.css('#trigger-click'));
        },

        getTriggerAlwaysDisplayedButton(): ElementFinder {
            return element(by.css('#trigger-displayed'));
        },

        getTriggerHoverButton(): ElementFinder {
            return element(by.css('#trigger-hover'));
        },

        getTriggerAlwaysHiddenButton(): ElementFinder {
            return element(by.css('#trigger-hidden'));
        },
        getMessage(text: string): ElementFinder {
            return element(by.cssContainingText('#message', text));
        }
    };

    export const Actions = {
        async switchToLegacy(): Promise<void> {
            Constants.type = 'legacy';
            await browser.click(Elements.getLegacyRadioButton());
        },

        async switchToAngular(): Promise<void> {
            Constants.type = 'angular';
            await browser.click(Elements.getAngularRadioButton());
        },

        async navigate(): Promise<void> {
            await browser.get('test/e2e/popupOverlay/index.html');
            await browser.waitUntilNoModal();
        },

        async clickPopupOverlayAnchor(): Promise<void> {
            await browser.click(Elements.getPopupOverlayAnchor());
        },

        async setTriggerToAlwaysDisplayed(): Promise<void> {
            await browser.click(Elements.getTriggerAlwaysDisplayedButton());
        },

        async setTriggerToAlwaysHidden(): Promise<void> {
            await browser.click(Elements.getTriggerAlwaysHiddenButton());
        },

        async setTriggerToClick(): Promise<void> {
            await browser.click(Elements.getTriggerClickRadioButton());
        },

        async setTriggerToHover(): Promise<void> {
            await browser.click(Elements.getTriggerHoverButton());
        },

        async hoverOverAnchor(): Promise<void> {
            await browser
                .actions()
                .mouseMove(Elements.getPopupOverlayAnchor())
                .perform();
        },

        async hoverAwayAnchor(): Promise<void> {
            await browser
                .actions()
                .mouseMove(Elements.getPopupOverlayAnchor(), { x: -100, y: 0 })
                .perform();
        }
    };

    export const Assertions = {
        async popupIsPresent(text: string): Promise<void> {
            await browser.waitForPresence(Elements.getPopup(text));
        },

        async popupIsAbsent(text: string): Promise<void> {
            await browser.waitForAbsence(Elements.getPopup(text));
        },

        async messageIsDisplayed(text: string): Promise<void> {
            await browser.waitForPresence(Elements.getMessage(text));
        }
    };
}
