/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element } from 'protractor';

export namespace InViewElementObserverPageObject {
    export const Actions = {
        async removeFirstComponent(): Promise<void> {
            await browser.click(by.id('removeFirstComponent'));
        },
        async addComponentAsFirst(): Promise<void> {
            await browser.click(by.id('addComponentAsFirst'));
        }
    };

    export const Elements = {
        async getReallyEligibleElements(): Promise<string> {
            return await element(by.id('total-eligible-components')).getText();
        },
        async getEligibleElements(): Promise<string> {
            return await element(by.id('total-eligible-components-from-observer')).getText();
        },
        async getInViewElements(): Promise<string> {
            return await element(
                by.id('total-visible-eligible-components-from-observer')
            ).getText();
        }
    };

    export const Assertions = {
        async inSync(expectedInViewElementsCount: number): Promise<void> {
            await browser.switchToIFrame();
            await browser.waitUntil(async () => {
                const t0 = await Elements.getReallyEligibleElements();
                const t1 = await Elements.getEligibleElements();

                return Number(t0) === Number(t1);
            }, 'inViewElementObserver failed to observe all eligible elements');

            await browser.waitUntil(async () => {
                const text = await Elements.getInViewElements();

                return Number(text) === Number(expectedInViewElementsCount);
            }, 'Expected ' + expectedInViewElementsCount + ' elements to be visible');
        }
    };
}
