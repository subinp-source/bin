/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace DataTableObject {
    export const Elements = {
        getHeaders(): ElementArrayFinder {
            return element.all(by.css('.se-paged-list__header'));
        },

        getRows(): ElementArrayFinder {
            return element.all(by.css('.se-paged-list-item'));
        },

        getNgComponents(): ElementArrayFinder {
            return element.all(by.css('my-selector'));
        },

        getLegacyComponents(): ElementArrayFinder {
            return element.all(by.css('#renderer'));
        },

        getCell(text: string): ElementFinder {
            return element(by.cssContainingText('td', text));
        }
    };

    export const Actions = {
        async navigate(): Promise<void> {
            await browser.get('test/e2e/dataTable/index.html');
        }
    };

    export const Assertions = {
        async hasCorrectHeadersCount(expected: number): Promise<void> {
            const count = await Elements.getHeaders().count();

            expect(count).toBe(expected);
        },

        async hasCorrectRowsCount(expected: number): Promise<void> {
            const count = await Elements.getRows().count();
            expect(count).toBe(expected);
        },

        async hasCorrectNgComponentsCount(expected: number): Promise<void> {
            const count = await Elements.getNgComponents().count();

            expect(count).toBe(expected);
        },

        async hasCorrectLegacyComponentsCount(expected: number): Promise<void> {
            const count = await Elements.getLegacyComponents().count();
            expect(count).toBe(expected);
        },

        async cellExists(text: string): Promise<void> {
            await browser.waitForPresence(Elements.getCell(text));
        }
    };
}
