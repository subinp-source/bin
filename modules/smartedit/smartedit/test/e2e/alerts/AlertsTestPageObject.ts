/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

import { CompileHtmlNgController } from 'smarteditcommons';

export namespace AlertsTestPageObject {
    export const Constants = {
        Mode: 'AngularJS'
    };
    export const Elements = {
        showAngularButton(): ElementFinder {
            return element(by.css('#test-alert-add-button-angular'));
        },
        showAngularJSButton(): ElementFinder {
            return element(by.css('#test-alert-add-button-angularjs'));
        },
        resetButton(): ElementFinder {
            return element(by.css('#test-alert-reset-button'));
        },
        alertMessage(): ElementFinder {
            return element(by.css('#test-alert-message'));
        },
        alertMessagePlaceholders(): ElementFinder {
            return element(by.css('#test-alert-message-placeholder'));
        },
        alertType(type: string): ElementFinder {
            return element(by.css(`#test-alert-type option[value="${type || 'information'}"]`));
        },
        alertCloseable(): ElementFinder {
            return element(by.css('#test-alert-closeable'));
        },
        alertTimeout(): ElementFinder {
            return element(by.css('#test-alert-timeout'));
        },
        alertTemplate(): ElementFinder {
            return element(by.css('#test-alert-template'));
        },
        alertTemplateUrl(): ElementFinder {
            return element(by.css('#test-alert-templateUrl'));
        },
        alertController(): ElementFinder {
            return element(by.css('#test-alert-controller'));
        }
    };

    export const Actions = {
        async navigate(): Promise<void> {
            await browser.get('test/e2e/alerts/index.html');
            await browser.waitForWholeAppToBeReady();

            /**
             * Don't wait below: container looks for toolbars, but this setup doesn't load them
             */
        },

        async setMessage(message: string): Promise<void> {
            await browser.sendKeys(Elements.alertMessage(), message);
        },

        async setMessagePlaceholders(messagePlaceholders: string): Promise<void> {
            await browser.sendKeys(Elements.alertMessagePlaceholders(), messagePlaceholders);
        },

        async setType(type: string): Promise<void> {
            await browser.click(Elements.alertType(type));
        },

        async setTimeout(millis: number): Promise<void> {
            await browser.sendKeys(Elements.alertTimeout(), millis);
        },

        async setCloseable(closeable: boolean): Promise<void> {
            const isSelected = await Elements.alertCloseable().isSelected();

            if (closeable !== isSelected) {
                await browser.click(Elements.alertCloseable());
            }
        },

        async setTemplate(template: string): Promise<void> {
            await browser.sendKeys(Elements.alertTemplate(), template);
        },

        async setTemplateUrl(templateUrl: string): Promise<void> {
            await browser.sendKeys(Elements.alertTemplateUrl(), templateUrl);
        },

        async setController(controller: CompileHtmlNgController['value']): Promise<void> {
            await browser.sendKeys(Elements.alertController(), controller.toString());
        },

        async clickShowAlert(): Promise<void> {
            if (Constants.Mode === 'angular') {
                await browser.click(Elements.showAngularButton());
            } else {
                await browser.click(Elements.showAngularJSButton());
            }
        },

        async resetForm(): Promise<void> {
            await browser.waitUntilNoModal();
            await browser.click(Elements.resetButton());
        },

        async showAlert(config: any) {
            await Actions.resetForm();
            if (config.message) {
                await Actions.setMessage(config.message);
            }
            if (config.messagePlaceholders) {
                await Actions.setMessagePlaceholders(config.messagePlaceholders);
            }
            if (config.template) {
                await Actions.setTemplate(config.template);
            }
            if (config.templateUrl) {
                await Actions.setTemplateUrl(config.templateUrl);
            }
            if (config.type) {
                await Actions.setType(config.type);
            }
            if (typeof config.closeable === 'boolean') {
                await Actions.setCloseable(config.closeable);
            }
            if (config.timeout) {
                await Actions.setTimeout(config.timeout);
            }
            if (config.controller) {
                await Actions.setController(config.controller);
            }

            await Actions.clickShowAlert();
            await browser.sleep(500);
        }
    };
}
