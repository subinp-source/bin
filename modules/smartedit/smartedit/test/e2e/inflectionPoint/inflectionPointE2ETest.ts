/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

import { InflectionPointObject } from './inflectionPointObject';

describe('end-to-end Test for inflection point module', () => {
    beforeEach(async () => {
        await InflectionPointObject.Actions.navigate();

        await browser.waitForWholeAppToBeReady();
        await browser.waitUntilNoModal();
    });

    it('Upon loading SmartEdit, inflection-point-selector should be displayed and select the first option. On selection, width of the iframe should be changed', async () => {
        await browser.click(InflectionPointObject.Elements.inflectionMenu());
        await browser.click(InflectionPointObject.Elements.firstInflectionDevice());

        const iFrameWidth = await InflectionPointObject.Elements.iframeWidth();

        expect(iFrameWidth).toBe(InflectionPointObject.Constants.firstDeviceWidth);
    });
});
