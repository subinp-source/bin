/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder } from 'protractor';

export namespace ModalWizardPageObject {
    export const Constants = {
        Mode: 'legacy'
    };
    export const Elements = {
        getWizard(): ElementFinder {
            return element(by.id('yModalWizard'));
        },
        getGenderStep(): ElementFinder {
            return element(by.id('genderStep'));
        },
        getNameStep(): ElementFinder {
            return element(by.id('nameStep'));
        },
        getOffendedStep(): ElementFinder {
            return element(by.id('offendedStep'));
        },
        getAgeStep(): ElementFinder {
            return element(by.id('ageStep'));
        },
        getNameNavbar(): ElementFinder {
            return element(by.id('NAV-name'));
        },
        getAgeNavbar(): ElementFinder {
            return element(by.id('NAV-age'));
        },
        getOpenButton(): ElementFinder {
            return element(by.id(`${Constants.Mode}-open`));
        },
        getNextButton(): ElementFinder {
            return element(by.id('ACTION_NEXT'));
        },
        getBackButton(): ElementFinder {
            return element(by.id('ACTION_BACK'));
        },
        getDoneButton(): ElementFinder {
            return element(by.id('ACTION_DONE'));
        },
        getNameInput(): ElementFinder {
            return element(by.id('nameInput'));
        },

        getCancelButton(): ElementFinder {
            return element(by.css('[fd-modal-close-btn]'));
        },

        getOffendedToggle(): ElementFinder {
            return element(by.id('offendedCheck'));
        }
    };

    export const Actions = {
        async navigate(): Promise<void> {
            await browser.get('/test/e2e/modalWizard/index.html');
        },
        async openWizard(): Promise<void> {
            await browser.click(Elements.getOpenButton());
        },

        async clickNext(): Promise<void> {
            await browser.click(Elements.getNextButton());
        },

        async clickBack(): Promise<void> {
            await browser.click(Elements.getBackButton());
        },

        async clickDone(): Promise<void> {
            await browser.waitForPresence(Elements.getDoneButton());
            await browser.click(Elements.getDoneButton());
        },

        async makeNameStepValid(): Promise<void> {
            await Elements.getNameInput().sendKeys('testing');
        },

        async openToGenderStep(): Promise<void> {
            await Actions.openWizard();
            await Actions.makeNameStepValid();
            await Actions.clickNext();
        },

        async openToAgeStep(): Promise<void> {
            await Actions.openToGenderStep();
            await Actions.clickNext();
        },

        async openToSummaryStep(): Promise<void> {
            await Actions.openToAgeStep();
            await Actions.clickNext();
        },

        async clickNavbar(elem: ElementFinder): Promise<void> {
            await browser.click(elem);
        },

        async clickCancel(): Promise<void> {
            await browser.click(Elements.getCancelButton());
        },

        async clickOffendedToggle(): Promise<void> {
            await browser.click(Elements.getOffendedToggle());
        }
    };

    export const Assertions = {
        async assertModalDisplayed(): Promise<void> {
            await browser.waitForPresence(Elements.getWizard());
        },

        async assertWizardNotDisplayed(): Promise<void> {
            await browser.waitForAbsence(Elements.getWizard());
        },

        async assertStepDisplayed(step: ElementFinder): Promise<void> {
            await browser.waitForPresence(step);
        },

        async assertNextIsEnabled(): Promise<void> {
            expect(await Elements.getNextButton().isEnabled()).toBeTruthy();
        },

        async assertNextIsDisabled(): Promise<void> {
            expect(await Elements.getNextButton().isEnabled()).toBeFalsy();
        },

        async assertNextIsPresent(): Promise<void> {
            await browser.waitForPresence(Elements.getNextButton());
        },

        async assertNextIsNotPresent(): Promise<void> {
            await browser.waitForAbsence(Elements.getNextButton());
        },

        async assertDoneIsPresent(): Promise<void> {
            await browser.waitForPresence(Elements.getDoneButton());
        },

        async assertDoneIsAbsent(): Promise<void> {
            await browser.waitForAbsence(Elements.getDoneButton());
        }
    };
}
