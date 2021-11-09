/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder } from 'protractor';

const LEGACY_SELECTOR = 'y-slider-panel';
const ANGULAR_SELECTOR = 'se-slider-panel';

const LEGACY_MODAL_SELECTOR = '#y-modal-dialog';
const ANGULAR_MODAL_SELECTOR = '.fd-modal__content';

export namespace SliderPanelComponentObjects {
    export const Constants = {
        mode: 'angular',
        selector: () => (Constants.mode === 'angular' ? ANGULAR_SELECTOR : LEGACY_SELECTOR),
        modalSelector: () =>
            Constants.mode === 'angular' ? ANGULAR_MODAL_SELECTOR : LEGACY_MODAL_SELECTOR
    };
    export const Elements = {
        getModalSliderPanel(): ElementFinder {
            return element(by.css(`${Constants.modalSelector()} .se-slider-panel-container`));
        },

        getModalSliderPanelSaveButton(): ElementFinder {
            return element(
                by.css(
                    `${Constants.modalSelector()} ${Constants.selector()} .se-slider-panel__footer-btn--save`
                )
            );
        },

        getModalSliderPanelCancelButton(): ElementFinder {
            return element(
                by.css(
                    `${Constants.modalSelector()} ${Constants.selector()} .se-slider-panel__footer-btn--cancel`
                )
            );
        },

        getModalSliderPanelDismissButton(): ElementFinder {
            return element(by.css(`.se-slider-panel__close-btn`));
        },

        getModalSliderPanelBody(): ElementFinder {
            return element(
                by.css(
                    `${Constants.modalSelector()} ${Constants.selector()} .se-slider-panel__body`
                )
            );
        },

        getSliderPanelBody(): ElementFinder {
            return element(by.css(`${Constants.selector()} .se-slider-panel__body`));
        },

        getModalSliderPanelTitle(): ElementFinder {
            return element(
                by.css(
                    `${Constants.modalSelector()} ${Constants.selector()} .se-slider-panel__header span:first-child`
                )
            );
        }
    };

    export const Actions = {
        async checkPresenceOfModalSliderPanel(): Promise<void> {
            await browser.waitForPresence(
                element(by.css(`${Constants.modalSelector()} ${Constants.selector()}`))
            );
        },

        async clickOnModalSliderPanelCancelButton(): Promise<void> {
            await browser.click(Elements.getModalSliderPanelCancelButton());
        },

        async clickOnModalSliderPanelSaveButton(): Promise<void> {
            await browser.click(Elements.getModalSliderPanelSaveButton());
        },

        async clickOnModalSliderPanelDismissButton(): Promise<void> {
            await browser.click(Elements.getModalSliderPanelDismissButton());
        }
    };

    export const Assertions = {
        async checkIfConfirmationModalIsPresent(): Promise<void> {
            await browser.waitForPresence(element(by.id('confirmationModalDescription')));
        },

        async assertForNonPresenceOfModalSliderPanel(): Promise<void> {
            await Util.waitForNonPresenceOfModalSliderPanel();
            await browser.waitForAbsence(
                element(by.css(`${Constants.modalSelector()} ${Constants.selector()}`))
            );
        },

        async assertModalSliderIsPresent(): Promise<void> {
            await browser.waitForPresence(Elements.getModalSliderPanel());
        },

        async saveButtonIsDisabledByDefaultInModalSlider() {
            await browser.waitForVisibility('.se-slider-panel-container');
            expect(await Elements.getModalSliderPanelTitle().getText()).toContain(
                'Slider Panel Title'
            );
            await Assertions.assertGetModalSliderPanelCancelButtonIsDisplayed();
            await Assertions.assertGetModalSliderPanelCancelButtonIsEnabled();
            await Assertions.assertModalSliderSaveBtnIsDisplayed();
            await Assertions.assertModalSliderSaveBtnIsEnabled(false);
        },

        async assertGetModalSliderPanelCancelButtonIsEnabled(isEnabled?: boolean) {
            if (isEnabled === undefined) {
                isEnabled = true;
            }
            await browser.waitUntil(async () => {
                const enabled = await Elements.getModalSliderPanelCancelButton().isEnabled();
                return enabled === isEnabled;
            }, 'Expected modal slider cancel button enablement to be: ' + isEnabled);
        },

        async assertGetModalSliderPanelCancelButtonIsDisplayed(isDisplayed?: boolean) {
            if (isDisplayed === undefined) {
                isDisplayed = true;
            }
            await browser.waitUntil(async () => {
                const shown = await Elements.getModalSliderPanelCancelButton().isDisplayed();
                return shown === isDisplayed;
            }, 'Expected modal slider cancel button displayed to be ' + isDisplayed);
        },

        async assertModalSliderSaveBtnIsEnabled(isEnabled?: boolean) {
            if (isEnabled === undefined) {
                isEnabled = true;
            }
            await browser.waitUntil(async () => {
                const enabled = await Elements.getModalSliderPanelSaveButton().isEnabled();

                return enabled === isEnabled;
            }, 'Expected modal slider save button enablement to be: ' + isEnabled);
        },

        async assertModalSliderSaveBtnIsDisplayed(isDisplayed?: boolean) {
            if (isDisplayed === undefined) {
                isDisplayed = true;
            }
            await browser.waitUntil(async () => {
                const shown = await Elements.getModalSliderPanelSaveButton().isDisplayed();

                return shown === isDisplayed;
            }, 'Expected modal slider save button displayed to be ' + isDisplayed);
        },

        async assertSliderBodyContainsText(text: string): Promise<void> {
            const whenBody = Elements.getSliderPanelBody();

            await browser.waitForSelectorToContainText(whenBody, text);
        }
    };

    export const Util = {
        async waitForNonPresenceOfModalSliderPanel(): Promise<void> {
            await browser.waitForAbsence(`${Constants.modalSelector()} ${Constants.selector()}`);
        }
    };
}
