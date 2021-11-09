/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';
import { AlertsComponentObject } from '../utils/components/AlertsComponentObject';
import { Perspectives } from '../utils/components/Perspectives';

describe('Storefront FrontEnd <-> SmartEdit FrontEnd connectivity E2E', () => {
    it('shows info popup when storefront is not sending a heartbeat and provides a link to switch to preview mode', async () => {
        await browser.waitForAngularEnabled(false);
        await browser.get('test/e2e/heartBeat/noHeartBeatMocks/smartedit.html');
        await browser.waitUntilNoModal();

        await AlertsComponentObject.Assertions.assertAlertMessageByIndex(0, 'Heart beat failed');
        await AlertsComponentObject.Assertions.assertAlertMessageLinkByIndex(0, 'Preview mode');

        await AlertsComponentObject.Actions.clickOnLinkInAlertByIndex(0);
        await Perspectives.Assertions.assertPerspectiveSelectorButtonIsDisabled();

        await Perspectives.Assertions.assertPerspectiveSelectorToolTipIsPresent();
    });

    it('shows info popup when storefront is responding after unresponsive period', async () => {
        await browser.waitForAngularEnabled(false);
        await browser.get('test/e2e/heartBeat/reconnectingHeartBeatMocks/smartedit.html');
        await browser.waitUntilNoModal();

        const expected = new RegExp('Heart beat reconnected', 'i');

        await browser.waitUntil(async () => {
            const text = await browser
                .findElement(AlertsComponentObject.Elements.alertByIndex(0))
                .getText();

            return expected.test(text);
        }, "was expecting to see an alert with text: 'Heart beat reconnected'");
    });

    it('shows info popup to switch to preview mode when there is no web application injector in storefront', async () => {
        await browser.waitForAngularEnabled(false);
        await browser.get('test/e2e/heartBeat/noWebAppInjectorHeartBeatMocks/smartedit.html');

        await AlertsComponentObject.Assertions.assertAlertMessageByIndex(0, 'Heart beat failed');
        await AlertsComponentObject.Assertions.assertAlertMessageLinkByIndex(0, 'Preview mode');

        await AlertsComponentObject.Actions.clickOnLinkInAlertByIndex(0);
        await Perspectives.Assertions.assertPerspectiveSelectorButtonIsDisabled();

        await Perspectives.Assertions.assertPerspectiveSelectorToolTipIsPresent();
    });
});
