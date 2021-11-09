/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element } from 'protractor';

describe('GenericEditor permanent POST (case of preview)', async () => {
    beforeEach(async () => {
        await browser.get('test/e2e/genericEditor/componentPermanentPOST/index.html');
    });

    it('will retrieve a different ticketId everytime one presses submit', async () => {
        expect(await element(by.id('ticketId')).getText()).toBe('');

        await element(by.name('description')).clear();
        await element(by.name('description')).sendKeys('some description');
        await browser.click(by.id('submit'));

        const initialValue = await element(by.id('ticketId')).getText();

        expect(initialValue).toBeDefined();
        expect(initialValue).not.toBe('');

        await element(by.name('description')).clear();
        await element(by.name('description')).sendKeys('some other description');
        await browser.click(by.id('submit'));

        expect(await element(by.id('ticketId')).getText()).toBeDefined();
        expect(await element(by.id('ticketId')).getText()).not.toBe('');
        expect(await element(by.id('ticketId')).getText()).not.toBe(initialValue);
    });
});
