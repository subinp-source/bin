/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder } from 'protractor';
import { SliderPanelComponentObjects } from '../utils/components/SliderPanelComponentObjects';

export namespace SliderPanelPageObject {
    export const Elements = {
        getOpenModalButton(): ElementFinder {
            return element(by.css('#button_openModal'));
        },

        getShowSliderPanelButton(): ElementFinder {
            return element(by.css('#button_showSliderPanel'));
        },

        getShowSliderPanelTemplateButton(): ElementFinder {
            return element(by.css('#button_showSliderPanelTemplate'));
        },

        getShowSliderPanelTemplateUrlButton(): ElementFinder {
            return element(by.css('#button_showSliderPanelTemplateUrl'));
        },

        getIsDirtySwitch(): ElementFinder {
            return element(by.css("label[for='isDirtySwitch']"));
        }
    };

    export const Actions = {
        async navigate() {
            await browser.get('test/e2e/sliderPanel/index.html');
        },
        async clickOnIsDirtySwitch() {
            await browser.waitForPresence(Elements.getIsDirtySwitch());
            await browser.click(Elements.getIsDirtySwitch());
        },

        async showModal() {
            await Elements.getOpenModalButton().click();
        },

        async showSliderPanel() {
            await Actions.showModal();
            await browser.click(Elements.getShowSliderPanelButton(), 'Could not click');
        },

        async clickSliderPanelTemplateButton(): Promise<void> {
            await browser.click(Elements.getShowSliderPanelTemplateButton(), 'Could not click');
        },

        async clickSliderPanelTemplateUrlButton(): Promise<void> {
            await browser.click(Elements.getShowSliderPanelTemplateUrlButton(), 'Could not click');
        }
    };

    export const Assertions = {
        async modalSliderPanelIsOverflowing() {
            const scroll = await SliderPanelComponentObjects.Elements.getModalSliderPanelBody().getAttribute(
                'scrollHeight'
            );
            const client = await SliderPanelComponentObjects.Elements.getModalSliderPanelBody().getAttribute(
                'clientHeight'
            );
            expect(Number(scroll)).toBeGreaterThan(Number(client));
        }
    };
}
