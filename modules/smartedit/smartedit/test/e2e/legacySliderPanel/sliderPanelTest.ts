/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser } from 'protractor';

import { SliderPanelPageObject } from '../sliderPanel/SliderPanelPageObject';
import { SliderPanelComponentObjects } from '../utils/components/SliderPanelComponentObjects';

describe('Slider Panel', () => {
    beforeEach(async () => {
        await browser.get('test/e2e/legacySliderPanel/index.html');
        SliderPanelComponentObjects.Constants.mode = 'legacy';
    });

    describe('Default Rendering:', () => {
        it(
            "GIVEN the page contains a 'modal' slider panel " +
                'WHEN the page gets loaded or when the modal is displayed ' +
                'THEN the slider panel is hidden by default',
            async () => {
                await SliderPanelComponentObjects.Assertions.assertForNonPresenceOfModalSliderPanel();

                await SliderPanelPageObject.Actions.showModal();
                const sliderPanel = SliderPanelComponentObjects.Elements.getModalSliderPanel();
                expect(await browser.isAbsent(sliderPanel)).toBe(true);
            }
        );
    });

    describe('Slider panel get properly displayed', () => {
        beforeEach(async () => {
            await SliderPanelPageObject.Actions.showSliderPanel();
        });

        it(
            "GIVEN the page contains a 'modal' slider panel " +
                'Then the slider panel is displayed after clicking on the toggle button',
            async () => {
                await SliderPanelComponentObjects.Assertions.assertModalSliderIsPresent();
            }
        );

        it(
            "GIVEN the page contains a 'modal' slider panel " +
                'WHEN the modal and slider panel get displayed ' +
                'THEN the save button is rendered as disabled by default',
            async () => {
                await browser.waitForVisibility(
                    SliderPanelComponentObjects.Elements.getModalSliderPanelTitle()
                );
                await SliderPanelComponentObjects.Assertions.saveButtonIsDisabledByDefaultInModalSlider();
            }
        );
    });

    describe('Overlaying content is not visible but can be scrolled', async () => {
        beforeEach(async () => {
            await SliderPanelPageObject.Actions.showSliderPanel();
        });

        it(
            "GIVEN the page contains a 'modal' slider panel " +
                'WHEN the slider panel is displayed' +
                'THEN any overlaying content is not visible',
            async () => {
                await SliderPanelPageObject.Assertions.modalSliderPanelIsOverflowing();
            }
        );
    });

    describe("Save is enabled when content is defined as 'isDirty'", () => {
        beforeEach(async () => {
            await SliderPanelPageObject.Actions.showSliderPanel();
            await browser.waitForPresence(
                SliderPanelComponentObjects.Elements.getModalSliderPanel()
            );
        });

        it(
            "GIVEN the page contains a 'modal' slider panel " +
                'WHEN the slider panel is displayed in dirty mode ' +
                'THEN the save button gets enabled and slider panel is hidden on click',
            async () => {
                await browser.waitForPresence(
                    SliderPanelComponentObjects.Elements.getModalSliderPanel()
                );
                await SliderPanelPageObject.Actions.clickOnIsDirtySwitch();
                await SliderPanelComponentObjects.Assertions.assertModalSliderSaveBtnIsDisplayed();
                await SliderPanelComponentObjects.Assertions.assertModalSliderSaveBtnIsEnabled();

                await SliderPanelComponentObjects.Actions.clickOnModalSliderPanelSaveButton();
                await browser.waitForAbsence(
                    SliderPanelComponentObjects.Elements.getModalSliderPanel()
                );
            }
        );
    });

    describe('Slider panel is hidden on cancel', async () => {
        beforeEach(async () => {
            await SliderPanelPageObject.Actions.showSliderPanel();
        });

        it(
            "GIVEN the page contains a 'modal' slider panel " +
                'WHEN the slider panel is displayed in dirty mode ' +
                'THEN the save button gets enabled',
            async () => {
                await browser.waitForPresence(
                    SliderPanelComponentObjects.Elements.getModalSliderPanel()
                );
                await SliderPanelComponentObjects.Actions.clickOnModalSliderPanelCancelButton();
                await browser.waitForAbsence(
                    SliderPanelComponentObjects.Elements.getModalSliderPanel()
                );
            }
        );
    });

    describe('Slider panel shows confirmation on dismiss (callback is called)', async () => {
        beforeEach(async () => {
            await SliderPanelPageObject.Actions.showSliderPanel();
        });

        it(
            "GIVEN the page contains a 'modal' slider panel " +
                'WHEN the dismiss icon is clicked on slider panel ' +
                'THEN a confirmation window opens',
            async () => {
                await browser.waitForVisibility(
                    SliderPanelComponentObjects.Elements.getModalSliderPanel()
                );
                await SliderPanelComponentObjects.Actions.clickOnModalSliderPanelDismissButton();
                await SliderPanelComponentObjects.Assertions.checkIfConfirmationModalIsPresent();
            }
        );
    });
});
