/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { ReloadPageObject } from './ReloadPageObject';
import { browser } from 'protractor';

describe('GenericEditor reload', () => {
    beforeEach(async () => {
        await ReloadPageObject.Actions.openAndBeReady();
    });

    it('WHEN setting a new structure THEN the Generic Editor automatically reloads with the new structure', async () => {
        await ReloadPageObject.Actions.setNewStructure();
        await ReloadPageObject.Assertions.assertNameInputIsDisplayed();
        expect(await ReloadPageObject.Elements.getNameInput().getAttribute('value')).toBe(
            'Any new name'
        );
        await ReloadPageObject.Assertions.assertRichTextEditorIsDisplayed();
    });

    it('WHEN setting a new structure api THEN the Generic Editor automatically reloads the new structure api', async () => {
        await ReloadPageObject.Actions.setNewStructureApi();
        await ReloadPageObject.Assertions.assertNameInputIsNotDisplayed();
        await ReloadPageObject.Assertions.assertHeadlineIsDisplayed();
        await ReloadPageObject.Assertions.assertActiveCheckboxIsDisplayed();
        await ReloadPageObject.Assertions.assertTextAreaIsDisplayed();
        expect(await ReloadPageObject.Elements.getHeadline().getAttribute('value')).toBe(
            'Any headline'
        );
    });

    /**
     * sucess message displayed assert that the payload passed has expected keys (need to check value too?)
     */
    it('WHEN setting a new structure AND submitting a modified content THEN the Generic Editor a success message will be displayed', async () => {
        await browser.waitForAngularEnabled(false);

        await ReloadPageObject.Actions.setNewStructure();
        await ReloadPageObject.Elements.getNameInput().clear();
        await ReloadPageObject.Elements.getNameInput().sendKeys('some new name');
        expect(await ReloadPageObject.Elements.getNameInput().getAttribute('value')).toBe(
            'some new name'
        );

        await ReloadPageObject.Actions.submit();
        expect(await ReloadPageObject.Elements.getGenericEditorStatus().getText()).toBe('PASSED');
    });

    it('GIVEN a new structure is set for a new component AND the User modify the content WHEN the structure change and the modified content is passed THEN the modified content is up to date', async () => {
        await browser.waitForAngularEnabled(false);

        await ReloadPageObject.Actions.setPOSTMode();
        await ReloadPageObject.Elements.getNameInput().clear();
        await ReloadPageObject.Elements.getNameInput().sendKeys('new component name');

        await ReloadPageObject.Actions.clickReloadButton();
        await ReloadPageObject.Assertions.assertRichTextEditorIsDisplayed();

        expect(await ReloadPageObject.Elements.getNameInput().getAttribute('value')).toBe(
            'new component name'
        );

        await ReloadPageObject.Elements.getCustomFieldInput().sendKeys('custom value');

        await ReloadPageObject.Actions.submit();
        expect(await ReloadPageObject.Elements.getGenericEditorStatus().getText()).toBe('PASSED');
    });
});
