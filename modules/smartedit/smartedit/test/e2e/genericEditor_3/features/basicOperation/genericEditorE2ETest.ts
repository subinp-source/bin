/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { GenericEditorPageObject } from '../../componentObjects/GenericEditorPageObject';
import { GenericEditorComponentObject } from '../../componentObjects/GenericEditorComponentObject';

describe('GenericEditor -', () => {
    // Constants
    const TEXT_FIELD = GenericEditorPageObject.Constants.HEADLINE_FIELD;
    const TEXT_FIELD_INVALID_VALUE = GenericEditorPageObject.Constants.HEADLINE_INVALID_TEXT;
    const TEXT_FIELD_UNKNOWN_TYPE = GenericEditorPageObject.Constants.HEADLINE_UNKNOWN_TYPE;

    const RICH_TEXT_FIELD = GenericEditorPageObject.Constants.CONTENT_FIELD;
    const RICH_TEXT_FIELD_INVALID_VALUE =
        GenericEditorPageObject.Constants.CONTENT_FIELD_INVALID_TEXT;
    const RICH_TEXT_FIELD_INVALID_VALUE_IT =
        GenericEditorPageObject.Constants.CONTENT_FIELD_INVALID_TEXT_IT;
    const RICH_TEXT_FIELD_ERROR_MSG = GenericEditorPageObject.Constants.CONTENT_FIELD_ERROR_MSG;

    const NOT_LOCALIZED: string = null;
    const ENGLISH_TAB = 'en';
    const ITALIAN_TAB = 'it';
    const FRENCH_TAB = 'fr';
    const POLISH_TAB = 'pl';
    const HINDI_TAB = 'hi';

    beforeEach(async () => {
        await GenericEditorPageObject.Actions.configureTest({
            multipleTabs: false
        });
        await GenericEditorPageObject.Actions.bootstrap(__dirname);
    });

    describe('basic', () => {
        it('GIVEN no tabs have been configured for the editor WHEN the editor opens THEN all fields are in only one tab', async () => {
            // THEN
            await GenericEditorComponentObject.Assertions.editorTabsAreNotDisplayed();
        });
    });

    describe('form save', () => {
        it('WHEN the editor is opened THEN it will display cancel button AND not display submit button', async () => {
            // THEN
            await GenericEditorComponentObject.Assertions.cancelButtonIsNotDisplayed();
            await GenericEditorComponentObject.Assertions.submitButtonIsNotDisplayed();
        });

        it('WHEN the editor is opened THEN it will display cancel and submit buttons when an attribute is modified', async () => {
            // WHEN
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                TEXT_FIELD,
                NOT_LOCALIZED,
                'some value'
            );

            // THEN
            await GenericEditorComponentObject.Assertions.cancelButtonIsDisplayed();
            await GenericEditorComponentObject.Assertions.submitButtonIsDisplayed();
        });

        xit('WHEN the editor is opened THEN it cancel will reset data to its original state', async () => {
            // WHEN
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                TEXT_FIELD,
                NOT_LOCALIZED,
                'some value'
            );

            await GenericEditorComponentObject.Actions.cancelForm();

            // THEN
            expect(
                await GenericEditorComponentObject.Elements.getTextField(
                    TEXT_FIELD,
                    NOT_LOCALIZED
                ).getText()
            ).toContain('The Headline');
        });

        it('GIVEN field has invalid information WHEN the form is saved THEN it will display validation errors', async () => {
            // GIVEN

            // WHEN
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                TEXT_FIELD,
                NOT_LOCALIZED,
                TEXT_FIELD_INVALID_VALUE
            );
            await GenericEditorComponentObject.Actions.submitForm();

            // THEN
            await GenericEditorComponentObject.Assertions.fieldHasValidationErrors(
                TEXT_FIELD,
                NOT_LOCALIZED,
                2
            );
        });

        // should it be moved to localizedElement test ?
        it('GIVEN invalid information has been pushed to two languages in a localized field WHEN the form is saved THEN it will display validation errors only on those two languages', async () => {
            // GIVEN
            await GenericEditorComponentObject.Actions.selectLocalizedFieldLanguage(
                RICH_TEXT_FIELD,
                ENGLISH_TAB
            );
            await GenericEditorComponentObject.Actions.setValueInRichTextField(
                RICH_TEXT_FIELD,
                ENGLISH_TAB,
                RICH_TEXT_FIELD_INVALID_VALUE
            );

            await GenericEditorComponentObject.Actions.selectLocalizedFieldLanguage(
                RICH_TEXT_FIELD,
                ITALIAN_TAB
            );
            await GenericEditorComponentObject.Actions.setValueInRichTextField(
                RICH_TEXT_FIELD,
                ITALIAN_TAB,
                RICH_TEXT_FIELD_INVALID_VALUE_IT
            );

            // WHEN
            await GenericEditorComponentObject.Actions.submitForm();

            // THEN
            await GenericEditorComponentObject.Assertions.fieldHasValidationErrorInLanguage(
                RICH_TEXT_FIELD,
                ITALIAN_TAB,
                RICH_TEXT_FIELD_ERROR_MSG
            );
            await GenericEditorComponentObject.Actions.selectLocalizedFieldLanguage(
                RICH_TEXT_FIELD,
                ENGLISH_TAB
            );
            await GenericEditorComponentObject.Assertions.fieldHasValidationErrorInLanguage(
                RICH_TEXT_FIELD,
                ENGLISH_TAB,
                RICH_TEXT_FIELD_ERROR_MSG
            );
            await GenericEditorComponentObject.Assertions.fieldHasNoValidationErrorsInLanguage(
                RICH_TEXT_FIELD,
                FRENCH_TAB
            );
            await GenericEditorComponentObject.Assertions.fieldHasNoValidationErrorsInLanguage(
                RICH_TEXT_FIELD,
                POLISH_TAB
            );
            await GenericEditorComponentObject.Assertions.fieldHasNoValidationErrorsInLanguage(
                RICH_TEXT_FIELD,
                HINDI_TAB
            );
        });

        it('GIVEN form has validation errors WHEN reset is clicked THEN validation errors are removed', async () => {
            // GIVEN
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                TEXT_FIELD,
                NOT_LOCALIZED,
                TEXT_FIELD_INVALID_VALUE
            );
            await GenericEditorComponentObject.Actions.submitForm();
            await GenericEditorComponentObject.Assertions.fieldHasValidationErrors(
                TEXT_FIELD,
                NOT_LOCALIZED,
                2
            );

            // WHEN
            await GenericEditorComponentObject.Actions.cancelForm();

            // THEN
            await GenericEditorComponentObject.Assertions.fieldHasNoValidationErrors(TEXT_FIELD);
        });

        it('WHEN form is submitted AND API returns a field that does not exist THEN the editor will display no validation errors', async () => {
            // GIVEN
            await GenericEditorComponentObject.Actions.setTextFieldValue(
                TEXT_FIELD,
                NOT_LOCALIZED,
                TEXT_FIELD_UNKNOWN_TYPE
            );

            // WHEN
            await GenericEditorComponentObject.Actions.submitForm();

            // THEN
            await GenericEditorComponentObject.Assertions.formHasNoValidationErrors();
        });
    });
});
