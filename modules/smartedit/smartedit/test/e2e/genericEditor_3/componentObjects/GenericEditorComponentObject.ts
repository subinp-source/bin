/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementArrayFinder, ElementFinder, Key, Locator } from 'protractor';

export namespace GenericEditorComponentObject {
    export const Elements = {
        // Editor
        getEditorTabs(): ElementArrayFinder {
            // TODO: This selector is too dependent on the current layout.
            // It'd be convenient to parametrize yTabset and yTabs to include an id to be
            // easier to find.
            return element.all(by.css('form > se-tabset > div > ul > li'));
        },
        getSubmitButton(): Locator {
            return by.css('.se-generic-editor__footer #submit');
        },
        getCancelButton(): Locator {
            return by.css('.se-generic-editor__footer #cancel');
        },
        getAnyValidationError(): Locator {
            return by.css("[id^='validation-error']");
        },

        // Tabs
        getTabSelector(tabname: string, inError?: boolean): Locator {
            const selector = `.se-generic-editor__tabs .se-tabset li a${
                inError ? '.sm-tab-error' : ''
            }`;
            return by.cssContainingText(selector, tabname);
        },

        // Fields
        getFieldStructure(fieldName: string): ElementFinder {
            return element(
                by.css(
                    '.ySEGenericEditorFieldStructure[data-cms-field-qualifier="' + fieldName + '"]'
                )
            );
        },
        getField(fieldName: string, language: string): ElementFinder {
            const fieldStructure = this.getFieldStructure(fieldName);
            if (language) {
                return fieldStructure.element(
                    by.css(
                        'se-localized-element se-tab[tab-id="' +
                            language +
                            '"] se-generic-editor-field'
                    )
                );
            } else {
                return fieldStructure.element(by.css('se-generic-editor-field'));
            }
        },
        getLocalizedTabHeader(fieldName: string, language: string): ElementFinder {
            return this.getFieldStructure(fieldName).element(
                by.css('se-localized-element ul.nav-tabs li[tab-id="' + language + '"]')
            );
        },
        getValidationErrors(fieldName: string, language?: string): ElementArrayFinder {
            const fieldStructure = this.getFieldStructure(fieldName);

            if (language) {
                return fieldStructure.all(
                    by.css(
                        'se-tab[tab-id="' +
                            language +
                            '"] se-generic-editor-field-messages .se-help-block--has-error'
                    )
                );
            } else {
                return fieldStructure.all(
                    by.css('se-generic-editor-field-messages .se-help-block--has-error')
                );
            }
        },
        getValidationErrorInLanguage(fieldName: string, language: string): ElementFinder {
            return element(
                by.css(
                    '[tab-id="' +
                        language +
                        '"] [validation-id="' +
                        fieldName +
                        '"] se-generic-editor-field-messages'
                )
            );
        },

        // Short/Long String
        getTextField(fieldName: string, language: string): ElementFinder {
            return this.getField(fieldName, language).element(by.css('input, textarea'));
        },

        // Rich/Text Editor
        getRichTextField(fieldName: string, language: string): ElementFinder {
            const fieldStructure = this.getFieldStructure(fieldName);
            if (language) {
                return fieldStructure.element(
                    by.css(
                        'se-localized-element se-tab[tab-id="' +
                            language +
                            '"] .cke_contents iframe'
                    )
                );
            } else {
                return fieldStructure.element(by.css('.cke_contents iframe'));
            }
        },
        getRichTextBody() {
            return browser.driver.findElement(by.tagName('body'));
        }
    };

    export const Actions = {
        // Editor
        async submitForm(): Promise<void> {
            await browser.click(Elements.getSubmitButton());
        },
        async cancelForm(): Promise<void> {
            await browser.click(Elements.getCancelButton());
        },
        // Tabs
        async selectTab(tabName: string): Promise<void> {
            await browser.click(Elements.getTabSelector(tabName));
        },
        // Fields
        async selectLocalizedFieldLanguage(fieldName: string, language: string): Promise<void> {
            await browser.click(Elements.getLocalizedTabHeader(fieldName, language));
        },
        async setTextFieldValue(fieldName: string, language: string, value: string): Promise<void> {
            const elem = Elements.getTextField(fieldName, language);

            // Angular model does not update on .clear() method. We need to trigger it manually.

            await elem.clear();

            if (value && value !== '') {
                await elem.sendKeys(value);
            } else {
                await elem.sendKeys('e');
                await elem.sendKeys(Key.chord(Key.CONTROL, 'a'));
                await elem.sendKeys(Key.BACK_SPACE);
            }
        },
        async setValueInRichTextField(
            fieldName: string,
            language: string,
            value: string
        ): Promise<void> {
            const richTextField = Elements.getRichTextField(fieldName, language);

            await browser.switchTo().frame(await richTextField.getWebElement());

            await (await Elements.getRichTextBody()).sendKeys(value);

            await browser.switchToParent();
        }
    };

    // --------------------------------------------------------------------------------------------------
    // Assertions
    // --------------------------------------------------------------------------------------------------
    export const Assertions = {
        // Editor
        async editorTabsAreNotDisplayed(): Promise<void> {
            expect(await Elements.getEditorTabs().count()).toBe(
                0,
                'Expected editor not to have any displayed tabs.'
            );
        },
        async cancelButtonIsDisplayed(): Promise<void> {
            await browser.waitForPresence(element(Elements.getCancelButton()));
        },
        async submitButtonIsDisplayed(): Promise<void> {
            await browser.waitForPresence(element(Elements.getSubmitButton()));
        },
        async cancelButtonIsNotDisplayed(): Promise<void> {
            await browser.waitForAbsence(element(Elements.getCancelButton()));
        },
        async submitButtonIsNotDisplayed(): Promise<void> {
            await browser.waitForAbsence(element(Elements.getSubmitButton()));
        },
        async formHasNoValidationErrors(): Promise<void> {
            await browser.waitForAbsence(Elements.getAnyValidationError());
        },

        // Tabs
        async tabIsInError(tabname: string): Promise<void> {
            await browser.waitForVisibility(Elements.getTabSelector(tabname, true));
        },
        async tabIsNotInError(tabname: string): Promise<void> {
            await browser.waitForVisibility(Elements.getTabSelector(tabname, false));
        },

        // Localized Elements
        async tabLanguageIsDisplayed(qualifier: string, fieldLanguage: string): Promise<void> {
            await browser.waitForPresence(Elements.getLocalizedTabHeader(qualifier, fieldLanguage));
        },
        async tabLanguageIsNotDisplayed(qualifier: string, fieldLanguage: string): Promise<void> {
            await browser.waitForAbsence(Elements.getLocalizedTabHeader(qualifier, fieldLanguage));
        },

        // Fields
        async richTextFieldIsDisabled(fieldName: string, language: string): Promise<void> {
            // Note: Using the getTextField element instead of getRichTextField on purpose. If getRichTextField is used, it returns a
            // tag that doesn't contain any information about the field being enabled or not; it will always return true.
            // Instead, the rich text field has a text area that determines if it is enabled; this text area is retrieved with getTextField.
            expect(await Elements.getTextField(fieldName, language).isEnabled()).toBe(false);
        },
        async richTextFieldIsEnabled(fieldName: string, language: string): Promise<void> {
            // Note: Using the getTextField element instead of getRichTextField on purpose. If getRichTextField is used, it returns a
            // tag that doesn't contain any information about the field being enabled or not; it will always return true.
            // Instead, the rich text field has a text area that determines if it is enabled; this text area is retrieved with getTextField.
            expect(await Elements.getTextField(fieldName, language).isEnabled()).toBeTruthy();
        },
        async fieldHasValidationErrors(fieldName: string, language: string, expectedCount: number) {
            expect(await Elements.getValidationErrors(fieldName, language).count()).toBe(
                expectedCount,
                'Expected field ' + fieldName + ' to have ' + expectedCount + ' errors.'
            );
        },
        async fieldHasNoValidationErrors(fieldName: string): Promise<void> {
            expect(await Elements.getValidationErrors(fieldName, null).count()).toBe(
                0,
                'Expected field ' + fieldName + ' not to have errors.'
            );
        },
        async fieldHasValidationErrorInLanguage(
            fieldName: string,
            language: string,
            expectedErrorMsg: string
        ): Promise<void> {
            const validationErrors = Elements.getValidationErrors(fieldName, language);
            expect(await validationErrors.count()).toBe(
                1,
                'Expected ' + fieldName + ' in ' + language + ' to have only one error message.'
            );
            expect(await validationErrors.get(0).getAttribute('innerText')).toContain(
                expectedErrorMsg
            );
        },
        async fieldHasNoValidationErrorsInLanguage(
            fieldName: string,
            language: string
        ): Promise<void> {
            await browser.waitForAbsence(Elements.getValidationErrors(fieldName, language));
        },
        async assertValueInRichTextField(
            fieldName: string,
            language: string,
            value: string
        ): Promise<void> {
            const richTextField = Elements.getRichTextField(fieldName, language);
            await browser.switchTo().frame(richTextField.getWebElement());
            expect(await (await Elements.getRichTextBody()).getText()).toContain(value);
            await browser.switchToParent();
        }
    };
}
