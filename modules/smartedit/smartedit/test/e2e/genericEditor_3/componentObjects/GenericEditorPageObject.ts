/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

// tslint:disable:no-var-requires
import { browser } from 'protractor';

const path = require('path');

export namespace GenericEditorPageObject {
    export const Constants = {
        // Text Field
        HEADLINE_FIELD: 'headline',
        HEADLINE_INVALID_TEXT:
            'I have changed to an invalid headline with two validation errors, % and lots of text',
        HEADLINE_UNKNOWN_TYPE: 'Checking unknown type',
        DESCRIPTION_FIELD: 'description',
        ID_FIELD: 'id',

        // Rich Text Field
        CONTENT_FIELD: 'content',
        CONTENT_FIELD_INVALID_TEXT:
            'I have changed to an invalid content with one validation error',
        CONTENT_FIELD_INVALID_TEXT_IT:
            'Ho cambiato ad un contenuto non valido con un errore di validazione',
        CONTENT_FIELD_ERROR_MSG:
            'This field is required and must to be between 1 and 255 characters long.'
    };

    export const Actions = {
        async configureTest(testConfig: any): Promise<void> {
            await browser.executeScript(
                'window.sessionStorage.setItem("TEST_CONFIGS", arguments[0])',
                JSON.stringify(testConfig)
            );
        },
        async bootstrap(testFolder: string): Promise<void> {
            try {
                const index = path.resolve('').length;
                const pathToTest = path.resolve(testFolder, 'index.html').substring(index);

                await browser.get(pathToTest);
                await browser.waitForContainerToBeReady();
            } catch (exception) {
                // tslint:disable-next-line:no-console
                console.error(
                    'genericEditorPageObject - Cannot load the test with folder ',
                    testFolder
                );
            }
        }
    };
}
