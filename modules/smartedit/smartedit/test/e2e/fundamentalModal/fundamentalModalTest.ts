/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { FundamentalModalObject } from './fundamentalModalObject';

describe('FundamentalModal', () => {
    beforeEach(async () => {
        await FundamentalModalObject.Actions.navigateToTestPage();
    });

    describe('Modal With Predefined Config', () => {
        it('WHEN button "Open Modal With Predefined Config" is clicked THEN the modal is visible', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();
            await FundamentalModalObject.Assertions.modalIsPresent();
        });

        it('WHEN button "Open Modal With Predefined Config" is clicked THEN the modal with 2 buttons, title and dismiss button is visible', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();

            await FundamentalModalObject.Assertions.modalButtonIsPresent('my_label_0');
            await FundamentalModalObject.Assertions.modalButtonIsPresent('my_label_1');
            await FundamentalModalObject.Assertions.modalTitleIsPresent('My Title');
            await FundamentalModalObject.Assertions.modalDismissButtonIsPresent();
        });

        it('WHEN modal is visible one button is disabled by default', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();

            expect(
                await FundamentalModalObject.Elements.modalButton('my_label_1').getAttribute(
                    'disabled'
                )
            ).toBeTruthy();
            expect(
                await FundamentalModalObject.Elements.modalButton('my_label_0').getAttribute(
                    'disabled'
                )
            ).toBeFalsy();
        });

        it('WHEN button "my_label_0" is clicked THEN the modal closes', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();
            await FundamentalModalObject.Actions.clickModalButton('my_label_0');
            await FundamentalModalObject.Actions.waitForModalToDisappear();

            await FundamentalModalObject.Assertions.modalIsAbsent();
        });

        it('WHEN button "Remove All Buttons" is clicked THEN no button is present', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();
            await FundamentalModalObject.Actions.removeAllButtonsFromModal();

            await FundamentalModalObject.Assertions.modalButtonIsAbsent('my_label_0');
            await FundamentalModalObject.Assertions.modalButtonIsAbsent('my_label_1');
        });

        it('WHEN button "Set Title" is clicked THEN the title is changed', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();

            await FundamentalModalObject.Assertions.modalTitleIsPresent('My Title');

            await FundamentalModalObject.Actions.setTitle();

            await FundamentalModalObject.Assertions.modalTitleIsPresent('New Title');
        });

        it('WHEN Hide Dismiss Button is clicked THEN the dismiss button is not present', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();
            await FundamentalModalObject.Actions.hideDismissButton();

            await FundamentalModalObject.Assertions.modalDismissButtonIsAbsent();
        });

        it('WHEN modal dismiss button is clicked THEN it is not present', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();
            await FundamentalModalObject.Actions.clickModalDismissButton();
            await FundamentalModalObject.Actions.waitForModalToDisappear();

            await FundamentalModalObject.Assertions.modalIsAbsent();
        });

        it('WHEN modal is dismissed THEN the returnedData container is shown with "Modal dismissed"', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();
            await FundamentalModalObject.Actions.clickModalDismissButton();

            await FundamentalModalObject.Assertions.modalReturnedDataContainerIsPresent(
                'Modal dismissed'
            );
        });

        it('WHEN modal is closed THEN the returnedData container is shown with "My Returned Data"', async () => {
            await FundamentalModalObject.Actions.openModalWithPredefinedConfig();
            await FundamentalModalObject.Actions.clickModalButton('my_label_0');

            await FundamentalModalObject.Assertions.modalReturnedDataContainerIsPresent(
                'My Returned Data'
            );
        });
    });

    describe('Modal With Manual Config', () => {
        it('WHEN button "Open Modal" is clicked THEN the modal is visible', async () => {
            await FundamentalModalObject.Actions.openModal();
            await FundamentalModalObject.Assertions.modalIsPresent();
        });

        it('WHEN button "Add Button" is clicked THEN modal button is visible', async () => {
            await FundamentalModalObject.Actions.openModal();
            await FundamentalModalObject.Actions.addButtonToModal();
            await FundamentalModalObject.Assertions.modalButtonIsPresent('my_label');
        });

        it('WHEN "Set Title" is clicked THEN the title is present', async () => {
            await FundamentalModalObject.Actions.openModal();
            await FundamentalModalObject.Actions.setTitle();

            await FundamentalModalObject.Assertions.modalTitleIsPresent('New Title');
        });
    });
});
