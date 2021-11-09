/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { ModalWizardPageObject } from './ModalWizardPageObject';
import { ConfirmModalServiceObject } from '../confirmModalService/confirmModalServiceObject';

describe('Modal Wizard - ', () => {
    beforeEach(async () => {
        await ModalWizardPageObject.Actions.navigate();
    });

    describe('Legacy - ', () => {
        beforeEach(async () => {
            ModalWizardPageObject.Constants.Mode = 'legacy';
        });

        it(
            'WHEN the wizard is opened, ' +
                'THEN the wizard (underlying)template should be displayed',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Assertions.assertModalDisplayed();
            }
        );

        it(
            'WHEN it is first opened, ' + 'THEN I should be on the first step of the wizard',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getNameStep()
                );
            }
        );

        it(
            'WHEN I have a form validation, ' +
                'AND the validation is failing, ' +
                'THEN I should see the next button disabled',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Assertions.assertNextIsDisabled();
            }
        );

        it(
            'WHEN I have a form validation, ' +
                'AND the validation is passing, ' +
                'THEN I should see the next button enabled',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Actions.makeNameStepValid();
                await ModalWizardPageObject.Assertions.assertNextIsEnabled();
            }
        );

        it(
            'WHEN I click Next, ' + 'THEN I should navigate to the next step of the wizard',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Actions.makeNameStepValid();
                await ModalWizardPageObject.Actions.clickNext();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getGenderStep()
                );
            }
        );

        it(
            'WHEN the last step is displayed, ' +
                'THEN I should see a done button instead of a next button',
            async () => {
                await ModalWizardPageObject.Actions.openToSummaryStep();
                await ModalWizardPageObject.Assertions.assertNextIsNotPresent();
                await ModalWizardPageObject.Assertions.assertDoneIsPresent();
            }
        );

        it(
            'WHEN I click Back, ' + 'THEN I should navigate to the previous step of the wizard',
            async () => {
                await ModalWizardPageObject.Actions.openToAgeStep();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getAgeStep()
                );
                await ModalWizardPageObject.Actions.clickBack(); // to gender
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getGenderStep()
                );
            }
        );

        it(
            'WHEN I click cancel, ' +
                'AND I dismiss the cancel confirmation, ' +
                'THEN the wizard should still be displayed',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Actions.clickCancel();
                await ConfirmModalServiceObject.Actions.clickCancel(); // cancel the cancel...
                await ModalWizardPageObject.Assertions.assertModalDisplayed();
            }
        );

        it(
            'WHEN I click cancel, ' +
                'AND I accept the cancel confirmation, ' +
                'THEN the wizard should be closed',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Actions.clickCancel();
                await ConfirmModalServiceObject.Actions.clickConfirm();
                await ModalWizardPageObject.Assertions.assertWizardNotDisplayed();
            }
        );

        it(
            'WHEN I click Done, ' +
                'AND I dismiss the close confirmation, ' +
                'THEN the wizard should still be displayed',
            async () => {
                await ModalWizardPageObject.Actions.openToSummaryStep();
                await ModalWizardPageObject.Actions.clickDone();
                await ConfirmModalServiceObject.Actions.clickCancel();
                await ModalWizardPageObject.Assertions.assertModalDisplayed();
            }
        );

        it(
            'WHEN I click Done, ' +
                'AND I accept the close confirmation, ' +
                'THEN the wizard should be closed',
            async () => {
                await ModalWizardPageObject.Actions.openToSummaryStep();
                await ModalWizardPageObject.Actions.clickDone();
                await ConfirmModalServiceObject.Actions.clickConfirm();
                await ModalWizardPageObject.Assertions.assertWizardNotDisplayed();
            }
        );

        it(
            'WHEN I add a step at runtime, ' +
                'THEN I should see the step in the nav bar and be able to navigate to it',
            async () => {
                await ModalWizardPageObject.Actions.openToGenderStep();
                await ModalWizardPageObject.Actions.clickOffendedToggle();
                await ModalWizardPageObject.Actions.clickNext();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getOffendedStep()
                );
            }
        );

        it(
            'WHEN I remove a step at runtime, ' +
                'THEN I should see the step removed from the navbar and navigating should skip that step',
            async () => {
                await ModalWizardPageObject.Actions.openToGenderStep();
                await ModalWizardPageObject.Actions.clickOffendedToggle();
                await ModalWizardPageObject.Actions.clickNext();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getOffendedStep()
                );
                await ModalWizardPageObject.Actions.clickBack();
                await ModalWizardPageObject.Actions.clickOffendedToggle();
                await ModalWizardPageObject.Actions.clickNext();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getAgeStep()
                );
            }
        );

        it(
            'WHEN I select a wizard navbar step, ' +
                'AND that step is before the current step, ' +
                'THEN I should be navigated directly to that step',
            async () => {
                await ModalWizardPageObject.Actions.openToGenderStep();
                await ModalWizardPageObject.Actions.clickNavbar(
                    ModalWizardPageObject.Elements.getNameNavbar()
                );
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getNameStep()
                );
            }
        );

        it(
            'WHEN I select a wizard navbar step, ' +
                'AND that step is ahead of the current step, ' +
                'THEN I should stay on the current step',
            async () => {
                await ModalWizardPageObject.Actions.openWizard().then(async () => {
                    ModalWizardPageObject.Elements.getAgeNavbar()
                        .isEnabled()
                        .then((isEnabled) => {
                            expect(isEnabled).toBe(false);
                        });
                });
            }
        );
    });

    describe('Angular - ', () => {
        beforeEach(async () => {
            ModalWizardPageObject.Constants.Mode = 'angular';
        });

        it(
            'WHEN the wizard is opened, ' +
                'THEN the wizard (underlying)template should be displayed',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Assertions.assertModalDisplayed();
            }
        );

        it(
            'WHEN it is first opened, ' + 'THEN I should be on the first step of the wizard',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getNameStep()
                );
            }
        );

        it(
            'WHEN I have a form validation, ' +
                'AND the validation is failing, ' +
                'THEN I should see the next button disabled',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Assertions.assertNextIsDisabled();
            }
        );

        it(
            'WHEN I have a form validation, ' +
                'AND the validation is passing, ' +
                'THEN I should see the next button enabled',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Actions.makeNameStepValid();
                await ModalWizardPageObject.Assertions.assertNextIsEnabled();
            }
        );

        it(
            'WHEN I click Next, ' + 'THEN I should navigate to the next step of the wizard',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Actions.makeNameStepValid();
                await ModalWizardPageObject.Actions.clickNext();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getGenderStep()
                );
            }
        );

        it(
            'WHEN the last step is displayed, ' +
                'THEN I should see a done button instead of a next button',
            async () => {
                await ModalWizardPageObject.Actions.openToSummaryStep();
                await ModalWizardPageObject.Assertions.assertNextIsNotPresent();
                await ModalWizardPageObject.Assertions.assertDoneIsPresent();
            }
        );

        it(
            'WHEN I click Back, ' + 'THEN I should navigate to the previous step of the wizard',
            async () => {
                await ModalWizardPageObject.Actions.openToAgeStep();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getAgeStep()
                );
                await ModalWizardPageObject.Actions.clickBack(); // to gender
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getGenderStep()
                );
            }
        );

        it(
            'WHEN I click cancel, ' +
                'AND I dismiss the cancel confirmation, ' +
                'THEN the wizard should still be displayed',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Actions.clickCancel();
                await ConfirmModalServiceObject.Actions.clickCancel(); // cancel the cancel...
                await ModalWizardPageObject.Assertions.assertModalDisplayed();
            }
        );

        it(
            'WHEN I click cancel, ' +
                'AND I accept the cancel confirmation, ' +
                'THEN the wizard should be closed',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();
                await ModalWizardPageObject.Actions.clickCancel();
                await ConfirmModalServiceObject.Actions.clickConfirm();
                await ModalWizardPageObject.Assertions.assertWizardNotDisplayed();
            }
        );

        it(
            'WHEN I click Done, ' +
                'AND I dismiss the close confirmation, ' +
                'THEN the wizard should still be displayed',
            async () => {
                await ModalWizardPageObject.Actions.openToSummaryStep();
                await ModalWizardPageObject.Actions.clickDone();
                await ConfirmModalServiceObject.Actions.clickCancel();
                await ModalWizardPageObject.Assertions.assertModalDisplayed();
            }
        );

        it(
            'WHEN I click Done, ' +
                'AND I accept the close confirmation, ' +
                'THEN the wizard should be closed',
            async () => {
                await ModalWizardPageObject.Actions.openToSummaryStep();
                await ModalWizardPageObject.Actions.clickDone();
                await ConfirmModalServiceObject.Actions.clickConfirm();
                await ModalWizardPageObject.Assertions.assertWizardNotDisplayed();
            }
        );

        it(
            'WHEN I add a step at runtime, ' +
                'THEN I should see the step in the nav bar and be able to navigate to it',
            async () => {
                await ModalWizardPageObject.Actions.openToGenderStep();
                await ModalWizardPageObject.Actions.clickOffendedToggle();
                await ModalWizardPageObject.Actions.clickNext();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getOffendedStep()
                );
            }
        );

        it(
            'WHEN I remove a step at runtime, ' +
                'THEN I should see the step removed from the navbar and navigating should skip that step',
            async () => {
                await ModalWizardPageObject.Actions.openToGenderStep();
                await ModalWizardPageObject.Actions.clickOffendedToggle();
                await ModalWizardPageObject.Actions.clickNext();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getOffendedStep()
                );
                await ModalWizardPageObject.Actions.clickBack();
                await ModalWizardPageObject.Actions.clickOffendedToggle();
                await ModalWizardPageObject.Actions.clickNext();
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getAgeStep()
                );
            }
        );

        it(
            'WHEN I select a wizard navbar step, ' +
                'AND that step is before the current step, ' +
                'THEN I should be navigated directly to that step',
            async () => {
                await ModalWizardPageObject.Actions.openToGenderStep();
                await ModalWizardPageObject.Actions.clickNavbar(
                    ModalWizardPageObject.Elements.getNameNavbar()
                );
                await ModalWizardPageObject.Assertions.assertStepDisplayed(
                    ModalWizardPageObject.Elements.getNameStep()
                );
            }
        );

        it(
            'WHEN I select a wizard navbar step, ' +
                'AND that step is ahead of the current step, ' +
                'THEN I should stay on the current step',
            async () => {
                await ModalWizardPageObject.Actions.openWizard();

                const isEnabled = await ModalWizardPageObject.Elements.getAgeNavbar().isEnabled();
                expect(isEnabled).toBe(false);
            }
        );
    });
});
