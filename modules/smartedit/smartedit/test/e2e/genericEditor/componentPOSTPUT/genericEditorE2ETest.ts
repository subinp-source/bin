/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element } from 'protractor';

describe('GenericEditor POST then PUT (standard case)', () => {
    beforeEach(async () => {
        await browser.get('test/e2e/genericEditor/componentPOSTPUT/index.html');
    });

    it('will hit server with POST then PUT and keep same id', async () => {
        expect(await element(by.id('componentId')).getText()).toBe('');

        await element(by.name('description')).clear();
        await element(by.name('description')).sendKeys('some description');
        await browser.click(by.id('submit'));

        const initialValue = await element(by.id('componentId')).getText();

        expect(initialValue).toBeDefined();
        expect(initialValue).not.toBe('');

        await element(by.name('description')).clear();
        await element(by.name('description')).sendKeys('some other description');

        await browser.click(by.id('submit'));
        expect(await element(by.id('componentId')).getText()).toBe(initialValue);
    });
});
