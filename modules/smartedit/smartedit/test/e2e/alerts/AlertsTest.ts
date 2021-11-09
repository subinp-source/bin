/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser } from 'protractor';
import { AlertsComponentObject } from '../utils/components/AlertsComponentObject';
import { AlertsTestPageObject } from './AlertsTestPageObject';

describe('Alerts -', () => {
    describe('Hiding and showing - AngularJS', () => {
        beforeEach(async () => {
            await AlertsTestPageObject.Actions.navigate();
            await AlertsTestPageObject.Actions.resetForm();
            await browser.waitUntilNoModal();
        });

        it('A basic alert can be displayed', async () => {
            await AlertsTestPageObject.Actions.showAlert({
                message: 'test alert message',
                closeable: false,
                timeout: -1
            });
            await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(1);
        });

        it('2 alerts can both be displayed (stacked)', async () => {
            const s1 = 'test alert messages 1';
            const s2 = 'test alert messages 2';

            await AlertsTestPageObject.Actions.showAlert({
                message: s1,
                type: 'error',
                closeable: false,
                timeout: -1
            });
            await AlertsTestPageObject.Actions.showAlert({
                message: s2,
                type: 'warning',
                closeable: false,
                timeout: -1
            });

            await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(2);
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(0, s1);
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(1, s2);
        });

        it('Given 3 alerts displayed, if second one manually dismissed, only 1st and 3rd remain', async () => {
            const alertMessage0 = 'alert 0';
            const alertMessage1 = 'alert 1';
            const alertMessage2 = 'alert 2';

            await AlertsTestPageObject.Actions.showAlert({
                message: alertMessage0,
                closeable: false,
                timeout: -1
            });
            await AlertsTestPageObject.Actions.showAlert({
                message: alertMessage1,
                closeable: true,
                timeout: -1
            });
            await AlertsTestPageObject.Actions.showAlert({
                message: alertMessage2,
                closeable: false,
                timeout: -1
            });

            await AlertsComponentObject.Actions.closeAlertByIndex(1);

            await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(2);
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(0, alertMessage0);
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(1, alertMessage2);
        });

        it('Alert is automatically removed after timeout', async () => {
            // Is this potentially flaky? probably...
            // If system really slow, maybe 1000 is not enough to display
            // but if we make it longer... slow test suit
            await AlertsTestPageObject.Actions.showAlert({
                message: 'alert to timeout',
                timeout: 1000
            });

            await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(1);
            await AlertsComponentObject.Assertions.assertNoAlertsDisplayed();
        });

        it('Alert shows dismiss X button for closeable alerts', async () => {
            await AlertsTestPageObject.Actions.showAlert({
                message: 'some default alert',
                closeable: true,
                timeout: 1000
            });

            await AlertsComponentObject.Assertions.assertAlertCloseabilityByIndex(0, true);
        });

        it('Alert does not show dismiss X button for non-closeable alerts', async () => {
            await AlertsTestPageObject.Actions.showAlert({
                message: 'some default alert',
                closeable: false
            });
            await AlertsComponentObject.Assertions.assertAlertCloseabilityByIndex(0, false);
        });
    });

    describe('Hiding and showing - Angular -', () => {
        beforeEach(async () => {
            await AlertsTestPageObject.Actions.navigate();
            await AlertsTestPageObject.Actions.resetForm();
            await browser.waitUntilNoModal();

            AlertsTestPageObject.Constants.Mode = 'Angular';
        });

        it('A basic alert can be displayed', async () => {
            await AlertsTestPageObject.Actions.showAlert({
                message: 'test alert message',
                closeable: false,
                timeout: -1
            });

            await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(1);
        });

        it('2 alerts can both be displayed (stacked)', async () => {
            const s1 = 'test alert messages 1';
            const s2 = 'test alert messages 2';

            await AlertsTestPageObject.Actions.showAlert({
                message: s1,
                type: 'error',
                closeable: false,
                timeout: -1
            });
            await AlertsTestPageObject.Actions.showAlert({
                message: s2,
                type: 'warning',
                closeable: false,
                timeout: -1
            });

            await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(2);
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(0, s1);
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(1, s2);
        });

        it('Given 3 alerts displayed, if second one manually dismissed, only 1st and 3rd remain', async () => {
            const alertMessage0 = 'alert 0';
            const alertMessage1 = 'alert 1';
            const alertMessage2 = 'alert 2';

            await AlertsTestPageObject.Actions.showAlert({
                message: alertMessage0,
                closeable: false,
                timeout: -1
            });
            await AlertsTestPageObject.Actions.showAlert({
                message: alertMessage1,
                closeable: true,
                timeout: -1
            });
            await AlertsTestPageObject.Actions.showAlert({
                message: alertMessage2,
                closeable: false,
                timeout: -1
            });

            await AlertsComponentObject.Actions.closeAlertByIndex(1);

            await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(2);
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(0, alertMessage0);
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(1, alertMessage2);
        });

        it('Alert is automatically removed after timeout', async () => {
            // Is this potentially flaky? probably...
            // If system really slow, maybe 1000 is not enough to display
            // but if we make it longer... slow test suit
            await AlertsTestPageObject.Actions.showAlert({
                message: 'alert to timeout',
                timeout: 1000
            });

            await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(1);
            await AlertsComponentObject.Assertions.assertNoAlertsDisplayed();
        });

        it('Alert shows dismiss X button for closeable alerts', async () => {
            await AlertsTestPageObject.Actions.showAlert({
                message: 'some default alert',
                closeable: true,
                timeout: 1000
            });

            await AlertsComponentObject.Assertions.assertAlertCloseabilityByIndex(0, true);
        });

        it('Alert does not show dismiss X button for non-closeable alerts', async () => {
            await AlertsTestPageObject.Actions.showAlert({
                message: 'some default alert',
                closeable: false
            });
            await AlertsComponentObject.Assertions.assertAlertCloseabilityByIndex(0, false);
        });
    });

    describe('is rendering ', async () => {
        beforeEach(async () => {
            await AlertsTestPageObject.Actions.navigate();
            await AlertsTestPageObject.Actions.resetForm();
        });

        it('message', async () => {
            const expectedMessage = 'The number is 12,345.123.';

            await AlertsTestPageObject.Actions.showAlert({
                message: expectedMessage,
                closeable: false,
                timeout: -1
            });
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(0, expectedMessage);
        });

        it('message placeholder', async () => {
            const expectedMessage = 'this 12345is a test';

            await AlertsTestPageObject.Actions.showAlert({
                message: 'sync.confirm.msg',
                timeout: -1,
                messagePlaceholders: JSON.stringify({ catalogName: '12345' }) as any
            });
            await AlertsComponentObject.Assertions.assertAlertMessageByIndex(0, expectedMessage);
        });

        describe('templates', () => {
            it('template', async () => {
                const alertTemplate =
                    '<div class="message">The number is {{ "12345.12345" | number: 3 }}.</div>';
                const expectedMessage = 'The number is 12,345.123.';

                await AlertsTestPageObject.Actions.showAlert({
                    closeable: false,
                    timeout: -1,
                    template: alertTemplate
                });
                await AlertsComponentObject.Assertions.assertAlertMessageByIndex(
                    0,
                    expectedMessage
                );
            });

            it('template with custom controller', async () => {
                const template =
                    '<div class="message">The number is {{ $alertInjectedCtrl.theNumber | number: 3 }}.</div>';
                const controller = function controllerFn() {
                    this.theNumber = '12345.12345';
                };
                const expectedMessage = 'The number is 12,345.123.';

                await AlertsTestPageObject.Actions.showAlert({
                    closeable: false,
                    timeout: -1,
                    controller,
                    template
                });

                await AlertsComponentObject.Assertions.assertAlertMessageByIndex(
                    0,
                    expectedMessage
                );
            });

            it('templateUrl', async () => {
                const expectedMessage = 'The number is 12,345.12.';

                await AlertsTestPageObject.Actions.showAlert({
                    closeable: false,
                    timeout: -1,
                    templateUrl: 'AlertTestComponentTemplate.html'
                });
                await AlertsComponentObject.Assertions.assertAlertMessageByIndex(
                    0,
                    expectedMessage
                );
            });
        });
    });

    describe('Alert types -', () => {
        beforeEach(async () => {
            await AlertsTestPageObject.Actions.navigate();
            await AlertsTestPageObject.Actions.resetForm();
        });

        it('Will style the 4 alert types', async () => {
            await AlertsTestPageObject.Actions.showAlert({
                message: 'error alert',
                type: 'error',
                timeout: -1
            });
            await AlertsTestPageObject.Actions.showAlert({
                message: 'info alert',
                type: 'information',
                timeout: -1
            });
            await AlertsTestPageObject.Actions.showAlert({
                message: 'warning alert',
                type: 'warning',
                timeout: -1
            });
            await AlertsTestPageObject.Actions.showAlert({
                message: 'success alert',
                type: 'success',
                timeout: -1
            });

            await AlertsComponentObject.Assertions.assertAlertIsOfTypeByIndex(0, 'error');
            await AlertsComponentObject.Assertions.assertAlertIsOfTypeByIndex(1, 'information');
            await AlertsComponentObject.Assertions.assertAlertIsOfTypeByIndex(2, 'warning');
            await AlertsComponentObject.Assertions.assertAlertIsOfTypeByIndex(3, 'success');
        });
    });
});
