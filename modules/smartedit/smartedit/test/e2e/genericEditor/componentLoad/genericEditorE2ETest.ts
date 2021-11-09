/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element } from 'protractor';

describe('GenericEditor - component load', () => {
    beforeEach(async () => {
        await browser.get('test/e2e/genericEditor/componentLoad/index.html');
    });

    it("will load 'Component ID' as default content for ID attribute", async () => {
        const val = await element(by.name('id')).getAttribute('value');

        expect(val).toEqual('Component ID');
    });

    it("will set 'The Headline' as default content for Headline attribute", async () => {
        const val = await element(by.name('headline')).getAttribute('value');

        expect(val).toEqual('The Headline');
    });

    it("will set 'vertical' as default content for Orientation attribute", async () => {
        await browser.waitForPresence(
            element(by.cssContainingText('span#enum-orientation', 'Vertical'))
        );
    });

    it("will set 'Option 1' as default content for SimpleDropdown attribute", async () => {
        await browser.waitForPresence(
            element(by.cssContainingText('#simpleDropdown-selector .se-item-printer', 'Option 1'))
        );
    });

    it('will set active checkbox to selected', async () => {
        expect(await element(by.name('active')).isSelected()).toBeTruthy();
    });

    it('will set enabled checkbox not un-selected', async () => {
        expect(await element(by.name('enabled')).isSelected()).toBeFalsy();
    });
});
