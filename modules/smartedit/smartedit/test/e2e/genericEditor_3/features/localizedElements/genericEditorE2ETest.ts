/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GenericEditorComponentObject } from '../../componentObjects/GenericEditorComponentObject';
import { GenericEditorPageObject } from '../../componentObjects/GenericEditorPageObject';

describe('GenericEditor - Localized Elements ', () => {
    // Constants
    const RICH_TEXT_FIELD = GenericEditorPageObject.Constants.CONTENT_FIELD;

    const ENGLISH_TAB = 'en';
    const ITALIAN_TAB = 'it';
    const FRENCH_TAB = 'fr';
    const POLISH_TAB = 'pl';
    const HINDI_TAB = 'hi';

    describe('GIVEN backend returns localized content', () => {
        beforeEach(async () => {
            await GenericEditorPageObject.Actions.configureTest({
                readableLanguages: ['en', 'it'],
                multipleTabs: false
            });
            await GenericEditorPageObject.Actions.bootstrap(__dirname);
        });

        it("will set different content for each language as default content for 'Content' attribute", async () => {
            await GenericEditorComponentObject.Assertions.assertValueInRichTextField(
                RICH_TEXT_FIELD,
                ENGLISH_TAB,
                'the content to edit'
            );
            await GenericEditorComponentObject.Assertions.assertValueInRichTextField(
                RICH_TEXT_FIELD,
                ITALIAN_TAB,
                'il contenuto da modificare'
            );
        });
    });

    describe('GIVEN user has read access only to some languages', () => {
        beforeEach(async () => {
            await GenericEditorPageObject.Actions.configureTest({
                readableLanguages: ['en', 'it'],
                multipleTabs: false
            });
            await GenericEditorPageObject.Actions.bootstrap(__dirname);
        });

        it('WHEN a localized component is displayed THEN it only displays the languages the user has access to', async () => {
            // WHEN / THEN
            await GenericEditorComponentObject.Assertions.tabLanguageIsDisplayed(
                RICH_TEXT_FIELD,
                ENGLISH_TAB
            );
            await GenericEditorComponentObject.Assertions.tabLanguageIsDisplayed(
                RICH_TEXT_FIELD,
                ITALIAN_TAB
            );

            await GenericEditorComponentObject.Assertions.tabLanguageIsNotDisplayed(
                RICH_TEXT_FIELD,
                FRENCH_TAB
            );
            await GenericEditorComponentObject.Assertions.tabLanguageIsNotDisplayed(
                RICH_TEXT_FIELD,
                POLISH_TAB
            );
            await GenericEditorComponentObject.Assertions.tabLanguageIsNotDisplayed(
                RICH_TEXT_FIELD,
                HINDI_TAB
            );

            await GenericEditorComponentObject.Assertions.richTextFieldIsEnabled(
                RICH_TEXT_FIELD,
                ENGLISH_TAB
            );

            await GenericEditorComponentObject.Actions.selectLocalizedFieldLanguage(
                RICH_TEXT_FIELD,
                ITALIAN_TAB
            );

            await GenericEditorComponentObject.Assertions.richTextFieldIsEnabled(
                RICH_TEXT_FIELD,
                ITALIAN_TAB
            );
        });
    });

    describe('GIVEN user has write access only to some languages', () => {
        beforeEach(async () => {
            await GenericEditorPageObject.Actions.configureTest({
                writeableLanguages: ['en'],
                multipleTabs: false
            });
            await GenericEditorPageObject.Actions.bootstrap(__dirname);
        });

        it('WHEN a localized component is displayed THEN it displays all languages AND only the ones the user can write are enabled', async () => {
            // WHEN / THEN
            await GenericEditorComponentObject.Assertions.tabLanguageIsDisplayed(
                RICH_TEXT_FIELD,
                ENGLISH_TAB
            );
            await GenericEditorComponentObject.Assertions.tabLanguageIsDisplayed(
                RICH_TEXT_FIELD,
                ITALIAN_TAB
            );
            await GenericEditorComponentObject.Assertions.tabLanguageIsDisplayed(
                RICH_TEXT_FIELD,
                FRENCH_TAB
            );
            await GenericEditorComponentObject.Assertions.tabLanguageIsDisplayed(
                RICH_TEXT_FIELD,
                POLISH_TAB
            );
            await GenericEditorComponentObject.Assertions.tabLanguageIsDisplayed(
                RICH_TEXT_FIELD,
                HINDI_TAB
            );

            await GenericEditorComponentObject.Assertions.richTextFieldIsEnabled(
                RICH_TEXT_FIELD,
                ENGLISH_TAB
            );
            await GenericEditorComponentObject.Assertions.richTextFieldIsDisabled(
                RICH_TEXT_FIELD,
                ITALIAN_TAB
            );
            await GenericEditorComponentObject.Assertions.richTextFieldIsDisabled(
                RICH_TEXT_FIELD,
                ITALIAN_TAB
            );
            await GenericEditorComponentObject.Assertions.richTextFieldIsDisabled(
                RICH_TEXT_FIELD,
                FRENCH_TAB
            );
            await GenericEditorComponentObject.Assertions.richTextFieldIsDisabled(
                RICH_TEXT_FIELD,
                POLISH_TAB
            );
            await GenericEditorComponentObject.Assertions.richTextFieldIsDisabled(
                RICH_TEXT_FIELD,
                HINDI_TAB
            );
        });
    });
});
