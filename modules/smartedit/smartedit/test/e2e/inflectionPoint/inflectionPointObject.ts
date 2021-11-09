/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace InflectionPointObject {
    export const Constants = {
        firstDeviceWidth: '480px'
    };

    export const Elements = {
        inflectionMenu(): ElementFinder {
            return element(by.id('inflectionPtDropdown'));
        },

        firstInflectionDevice(): ElementFinder {
            return element(by.id('se-device-phone'));
        },

        async iframeWidth(): Promise<string> {
            return await element(by.tagName('iframe')).getCssValue('width');
        }
    };

    export const Actions = {
        async navigate(): Promise<void> {
            await browser.get('test/e2e/inflectionPoint/index.html');
        }
    };
}
