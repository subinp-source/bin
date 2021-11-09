/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

describe('GenericEditor With Number and Float', () => {
    let price: ElementFinder;
    let quantity: ElementFinder;

    beforeEach(async () => {
        await browser.get('test/e2e/genericEditor/componentWithNumberAndFloat/index.html');
    });

    beforeEach(async () => {
        quantity = element(by.css("[id='quantity-number']"));
        price = element(by.css("[id='price-float']"));
    });

    describe('GIVEN a quantity Number attribute, ', () => {
        beforeEach(async () => {
            quantity = element(by.css("[id='quantity-number']"));
            await quantity.clear();
            await browser.sendKeys(quantity, '10');
        });

        it('WHEN the component is rendered then quantity is an input (number)', async () => {
            expect(await quantity.isPresent()).toBe(true);
            expect(await quantity.getAttribute('value')).toEqual('10');
        });

        it('WHEN the quantity has an invalid attribute, THEN cancel and submit are not present', async () => {
            await browser.sendKeys(quantity, 'I have changed');
            await browser.waitForAbsence(by.id('submit'));
        });

        it('WHEN the quantity has an invalid input, THEN cancel and submit are not present', async () => {
            await quantity.clear();
            await browser.sendKeys(quantity, '123123123');
            await browser.waitForPresence(by.id('submit'));
        });
    });

    describe('GIVEN a price Float attribute, ', () => {
        it('WHEN the component is rendered then price is an input (float)', async () => {
            expect(await price.isPresent()).toBe(true);
            expect(await price.getAttribute('value')).toEqual('100.15');
        });

        it('WHEN the price has an invalid input, THEN submit is not present', async () => {
            await browser.sendKeys(price, '123.123213.123123');
            await browser.waitForAbsence(by.id('submit'));
        });

        it('WHEN the price has an invalid attribute, THEN cancel and submit are not present', async () => {
            await price.clear();
            await browser.sendKeys(price, '123.12');
            await browser.waitForPresence(by.id('submit'));
        });
    });
});
