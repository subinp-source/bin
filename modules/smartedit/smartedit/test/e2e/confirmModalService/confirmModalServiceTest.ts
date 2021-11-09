/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ConfirmModalServiceObject } from './confirmModalServiceObject';

describe('ConfirmationModalService', () => {
    beforeEach(async () => {
        await ConfirmModalServiceObject.Actions.navigateToTestPage();
    });

    describe('Modal with description - ', async () => {
        it('is present after button is triggered', async () => {
            await ConfirmModalServiceObject.Actions.clickTriggerButton(0);

            await ConfirmModalServiceObject.Assertions.modalIsPresent(false);
        });

        it('has valid texts', async () => {
            await ConfirmModalServiceObject.Actions.clickTriggerButton(0);

            await ConfirmModalServiceObject.Assertions.modalHasCorrectTitle(
                'my.confirmation.title',
                false
            );
            await ConfirmModalServiceObject.Assertions.modalHasCorrectBody(
                'my.confirmation.message'
            );
        });
    });

    describe('Modal with scope - ', () => {
        it('is present after button is triggered', async () => {
            await ConfirmModalServiceObject.Actions.clickTriggerButton(1);

            await ConfirmModalServiceObject.Assertions.modalIsPresent(true);
        });

        it('has valid texts', async () => {
            await ConfirmModalServiceObject.Actions.clickTriggerButton(1);

            await ConfirmModalServiceObject.Assertions.modalHasCorrectTitle(
                'my.confirmation.title',
                true
            );
            await ConfirmModalServiceObject.Assertions.modalHasCorrectBody(
                'scopeParam: Scope Param Rendered'
            );
        });
    });

    describe('Modal with scope and templateUrl - ', () => {
        it('is present after button is triggered', async () => {
            await ConfirmModalServiceObject.Actions.clickTriggerButton(2);

            await ConfirmModalServiceObject.Assertions.modalIsPresent(true);
        });

        it('has valid texts', async () => {
            await ConfirmModalServiceObject.Actions.clickTriggerButton(2);

            await ConfirmModalServiceObject.Assertions.modalHasCorrectTitle(
                'my.confirmation.title',
                true
            );
            await ConfirmModalServiceObject.Assertions.modalHasCorrectBody(
                'scopeParam: Scope Param Rendered'
            );
        });
    });
});
