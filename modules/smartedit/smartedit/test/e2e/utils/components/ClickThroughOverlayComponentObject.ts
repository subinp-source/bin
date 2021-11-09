/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace ClickThroughOverlayComponentObject {
    export const Elements = {
        allowClickThroughButton(): ElementFinder {
            return element(
                by.id('smartEditPerspectiveToolbar_option_se.CLICK_THROUGH_OVERLAY_btn')
            );
        },
        async preventClickThroughButton() {
            await browser.waitUntilNoModal();
            return browser.element(
                by.id('smartEditPerspectiveToolbar_option_se.PREVENT_OVERLAY_CLICKTHROUGH_btn')
            );
        }
    };

    export const Actions = {
        async allowClickThroughOverlay() {
            await browser.switchToParent();
            await browser.click(Elements.allowClickThroughButton());
        },

        async preventClickThroughOverlay() {
            await browser.switchToParent();
            await browser.click(await Elements.preventClickThroughButton());
        }
    };

    export const Utils = {
        async clickThroughOverlay(e: ElementFinder) {
            await Actions.allowClickThroughOverlay();
            await browser.switchToIFrame();
            await browser.click(e);
            await Actions.preventClickThroughOverlay();
        }
    };
}
