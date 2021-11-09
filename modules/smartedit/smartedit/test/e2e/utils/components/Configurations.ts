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

export namespace Configurations {
    export const Elements = {
        getCancelButton(): ElementFinder {
            return element(by.cssContainingText('.fd-button', 'Cancel'));
        },

        getSaveButton(): ElementFinder {
            return element(by.cssContainingText('.fd-button', 'Save'));
        },

        getConfigurationTitle(): ElementFinder {
            return element(by.css('.fd-modal__title'));
        },

        getConfigurationsAsList(): ElementArrayFinder {
            return element.all(by.css('#editConfigurationsBody .se-config__entry'));
        },

        getConfigurationCenterButton(): ElementFinder {
            return element(
                by.id('smartEditHeaderToolbar_option_headerToolbar.configurationTemplate_btn')
            );
        },

        getErrorForKey(key: string): ElementFinder {
            return element(by.id(key + '_error_0'));
        },

        getAbsoluteUrlCheckbox(): ElementFinder {
            return element(by.id('previewTicketURI_absoluteUrl_check_0'));
        },

        getAbsoluteUrlText(): ElementFinder {
            return element(by.id('previewTicketURI_absoluteUrl_msg_0'));
        }
    };
    export const Actions = {
        async openConfigurationEditor(): Promise<void> {
            await browser.click(Elements.getConfigurationCenterButton());
        },
        async waitForModal(): Promise<void> {
            await browser.waitUntil(() => {
                return element(by.css('div.modal-content'));
            }, 'could not find modal content div');
        },

        async waitUntilConfigurationReady(): Promise<void> {
            await browser.waitUntil(() => {
                return element
                    .all(by.css('#editConfigurationsBody .se-config__entry'))
                    .count()
                    .then((count) => {
                        return count > 0;
                    });
            }, 'could not find config list');
        },

        async setConfigurationValue(rowIndex: number, value: string): Promise<void> {
            await Actions.waitUntilConfigurationReady();
            const elem = Elements.getConfigurationsAsList()
                .get(rowIndex)
                .element(by.css('.se-config__entry-value textarea'));

            await elem.clear();
            await elem.sendKeys(value);
            await browser.executeScript('arguments[0].blur()', elem);
        },

        async setConfigurationKeyAndValue(
            rowIndex: number,
            key: string,
            value: string
        ): Promise<void> {
            await Actions.waitUntilConfigurationReady();
            const elem = await Elements.getConfigurationsAsList()
                .get(rowIndex)
                .element(by.css('.se-config__entry-key input'));

            await elem.clear();
            await elem.sendKeys(key);
            await Actions.setConfigurationValue(rowIndex, value);
        },

        async clickSave(): Promise<void> {
            await browser.click(
                element(by.cssContainingText('.fd-button', 'Save')),
                'Could not click on Save Button'
            );
            await browser.waitUntilNoModal();
        },

        async clickCancel(): Promise<void> {
            await browser.click(element(by.cssContainingText('.fd-button', 'Cancel')));
            await browser.waitUntilNoModal();
        },

        async clickConfirmOk(): Promise<void> {
            await browser.click(
                by.css('.se-confirmation-dialog #confirmOk'),
                'could not find confirmOk button'
            );
            await browser.waitUntilNoModal();
        },

        async clickAdd(): Promise<void> {
            return browser.click(by.css('.se-config__add-entry-btn'), 'could not find add button');
        },

        async deleteConfiguration(rowIndex: number): Promise<void> {
            await Actions.waitUntilConfigurationReady();
            const elem = Elements.getConfigurationsAsList()
                .get(rowIndex)
                .element(by.css('button[id*=removeButton]'));

            await browser.waitUntil(
                ExpectedConditions.elementToBeClickable(elem),
                'could not find removeButton'
            );
            await Actions.waitUntilConfigurationReady();
            await Elements.getConfigurationsAsList()
                .get(rowIndex)
                .element(by.css('button[id*=removeButton]'))
                .click();
        },

        async getEntry(
            array: { key: string; value: string }[],
            rows: ElementArrayFinder,
            index: number
        ): Promise<{ key: string; value: string }[]> {
            const key = await rows
                .get(index)
                .element(by.css('.se-config__entry-key input'))
                .getAttribute('value');

            const value = await rows
                .get(index)
                .element(by.css('.se-config__entry-value textarea'))
                .getAttribute('value');

            array.push({
                key,
                value
            });

            const length = await rows.count();

            if (index < length - 1) {
                const arr = await Actions.getEntry(array, rows, index + 1);

                return arr;
            } else {
                return array;
            }
        },

        async getConfigurations(): Promise<{ key: string; value: string }[]> {
            const rows = element.all(by.css('#editConfigurationsBody .se-config__entry'));
            const array = await Actions.getEntry([], rows, 0);

            return array;
        },

        async waitForConfigurationModal(size: number, message?: string): Promise<void> {
            await Actions.waitForModal();
            await Actions.waitForConfigurationsToPopulate(size, message);
        },

        async waitForConfigurationsToPopulate(size: number, message?: string): Promise<void> {
            message = message || 'could not find initial list of entries';
            await browser.waitUntil(() => {
                return element
                    .all(by.css('#editConfigurationsBody .se-config__entry'))
                    .count()
                    .then((count) => {
                        return count === size;
                    });
            }, message);
        },

        async waitForErrorForKey(key: string): Promise<void> {
            await browser.waitUntil(
                ExpectedConditions.visibilityOf(element(by.css('#' + key + '_error_0')))
            );
        }
    };
}
