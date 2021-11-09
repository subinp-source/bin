/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GenericEditorComponentObject } from '../../componentObjects/GenericEditorComponentObject';
import { GenericEditorPageObject } from '../../componentObjects/GenericEditorPageObject';

describe('GenericEditor MultiTabs -', () => {
    const DEFAULT_TAB_KEY = 'GENERICEDITOR.TAB.DEFAULT.TITLE'.toLowerCase();
    const ADMIN_TAB_KEY = 'GENERICEDITOR.TAB.ADMINISTRATION.TITLE'.toLowerCase();

    // Fields
    const HEADLINE_FIELD_NAME = GenericEditorPageObject.Constants.HEADLINE_FIELD;
    const DESCRIPTION_FIELD_NAME = GenericEditorPageObject.Constants.DESCRIPTION_FIELD;
    const ID_FIELD_NAME = GenericEditorPageObject.Constants.ID_FIELD;

    // Constants

    const TEXT_FIELD_INVALID_VALUE = GenericEditorPageObject.Constants.HEADLINE_INVALID_TEXT;

    const NOT_LOCALIZED: string = null;

    beforeEach(async () => {
        await GenericEditorPageObject.Actions.configureTest({
            multipleTabs: true
        });
        await GenericEditorPageObject.Actions.bootstrap(__dirname);
    });

    describe('form save', async () => {
        it('WHEN the editor is opened THEN it will display cancel button AND not display submit button', async () => {
            // THEN
            await GenericEditorComponentObject.Assertions.cancelButtonIsNotDisplayed();
            await GenericEditorComponentObject.Assertions.submitButtonIsNotDisplayed();
        });

        it('GIVEN field has invalid information in another tab WHEN the form is saved THEN it will display validation errors in the correct tab', async () => {
            // GIVEN
            await GenericEditorComponentObject.Actions.selectTab(DEFAULT_TAB_KEY);
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                HEADLINE_FIELD_NAME,
                NOT_LOCALIZED,
                TEXT_FIELD_INVALID_VALUE
            );

            // WHEN
            await GenericEditorComponentObject.Actions.selectTab(ADMIN_TAB_KEY);
            await GenericEditorComponentObject.Actions.submitForm();

            // THEN
            await GenericEditorComponentObject.Assertions.tabIsInError(DEFAULT_TAB_KEY);
            await GenericEditorComponentObject.Actions.selectTab(DEFAULT_TAB_KEY);
            await GenericEditorComponentObject.Assertions.fieldHasValidationErrors(
                HEADLINE_FIELD_NAME,
                NOT_LOCALIZED,
                2
            );
        });

        it('GIVEN form has validation errors in multiple tabs WHEN reset is clicked THEN validation errors are removed in all tabs', async () => {
            // GIVEN
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                HEADLINE_FIELD_NAME,
                NOT_LOCALIZED,
                TEXT_FIELD_INVALID_VALUE
            );
            await GenericEditorComponentObject.Actions.submitForm();
            await GenericEditorComponentObject.Assertions.fieldHasValidationErrors(
                HEADLINE_FIELD_NAME,
                NOT_LOCALIZED,
                2
            );

            // WHEN
            await GenericEditorComponentObject.Actions.cancelForm();

            // THEN
            await GenericEditorComponentObject.Assertions.fieldHasNoValidationErrors(
                HEADLINE_FIELD_NAME
            );
        });

        it('when errors is about a field on another tab than the current one, the target tab lights up and the field is in error', async () => {
            // GIVEN

            await GenericEditorComponentObject.Actions.selectTab(ADMIN_TAB_KEY);

            await GenericEditorComponentObject.Actions.setTextFieldValue(
                ID_FIELD_NAME,
                NOT_LOCALIZED,
                'some wrong content X'
            );

            await GenericEditorComponentObject.Actions.selectTab(DEFAULT_TAB_KEY);

            // WHEN
            await GenericEditorComponentObject.Actions.submitForm();

            // THEN
            await GenericEditorComponentObject.Assertions.tabIsInError(ADMIN_TAB_KEY);
            await GenericEditorComponentObject.Actions.selectTab(ADMIN_TAB_KEY);
            const elements = GenericEditorComponentObject.Elements.getValidationErrors('id');

            expect(await elements.count()).toBe(1);
            expect(await elements.get(0).getText()).toBe('This field cannot contain an X');
        });
    });

    describe('field and tab validation', async () => {
        it('GIVEN non empty required pristine field AND no validation errors AND tab is not highlighted WHEN value is removed (field is empty and not pristine now) THEN field has validation error AND tab is highlighted', async () => {
            // GIVEN
            await GenericEditorComponentObject.Assertions.fieldHasNoValidationErrors(
                DESCRIPTION_FIELD_NAME
            );
            await GenericEditorComponentObject.Assertions.tabIsNotInError(DEFAULT_TAB_KEY);

            // WHEN
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                ''
            );

            // THEN
            await GenericEditorComponentObject.Assertions.fieldHasValidationErrors(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                1
            );
            await GenericEditorComponentObject.Assertions.tabIsInError(DEFAULT_TAB_KEY);
        });

        it('GIVEN required non pristine field is empty AND tab is highlighted AND validation error is displayed WHEN submit is clicked AND field error returned from backend AND required non pristine field is populated THEN tab is still highlighted', async () => {
            // GIVEN

            await GenericEditorComponentObject.Actions.setTextFieldValue(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                ''
            );

            await GenericEditorComponentObject.Assertions.fieldHasValidationErrors(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                1
            );

            await GenericEditorComponentObject.Assertions.tabIsInError(DEFAULT_TAB_KEY);

            // WHEN
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                'error description'
            );

            await GenericEditorComponentObject.Actions.submitForm();

            await GenericEditorComponentObject.Actions.setTextFieldValue(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                'some data'
            );

            // THEN
            await GenericEditorComponentObject.Assertions.tabIsInError(DEFAULT_TAB_KEY);
        });

        it('GIVEN required non pristine fields are empty on default tab and admin tab THEN both tabs are highlighted AND both fields have validation errors', async () => {
            // GIVEN

            await GenericEditorComponentObject.Actions.selectTab(ADMIN_TAB_KEY);

            await GenericEditorComponentObject.Actions.setTextFieldValue(
                ID_FIELD_NAME,
                NOT_LOCALIZED,
                ''
            );

            await GenericEditorComponentObject.Actions.selectTab(DEFAULT_TAB_KEY);

            await GenericEditorComponentObject.Actions.setTextFieldValue(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                ''
            );

            // THEN

            await GenericEditorComponentObject.Assertions.tabIsInError(DEFAULT_TAB_KEY);

            await GenericEditorComponentObject.Assertions.tabIsInError(ADMIN_TAB_KEY);

            await GenericEditorComponentObject.Actions.selectTab(DEFAULT_TAB_KEY);

            await GenericEditorComponentObject.Assertions.fieldHasValidationErrors(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                1
            );

            await GenericEditorComponentObject.Actions.selectTab(ADMIN_TAB_KEY);

            await GenericEditorComponentObject.Assertions.fieldHasValidationErrors(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                1
            );
        });

        it('GIVEN required non pristine field is empty AND validation error is displayed AND tab is highlighted WHEN field is populated THEN validation error is not displayed AND tab is not highlighted', async () => {
            // GIVEN

            await GenericEditorComponentObject.Actions.selectTab(DEFAULT_TAB_KEY);
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                ''
            );
            await GenericEditorComponentObject.Assertions.tabIsInError(DEFAULT_TAB_KEY);
            await GenericEditorComponentObject.Assertions.fieldHasValidationErrors(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                1
            );

            // WHEN
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                DESCRIPTION_FIELD_NAME,
                NOT_LOCALIZED,
                'some data'
            );

            // THEN
            await GenericEditorComponentObject.Assertions.tabIsNotInError(DEFAULT_TAB_KEY);
            await GenericEditorComponentObject.Assertions.fieldHasNoValidationErrors(
                DESCRIPTION_FIELD_NAME
            );
        });
    });
});
