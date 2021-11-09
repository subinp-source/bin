/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

import { AlertsComponentObject } from '../utils/components/AlertsComponentObject';
import { HttpErrorInterceptorPageObject } from './HttpErrorInterceptorPageObject';

describe('HTTP Error Interceptor -', () => {
    beforeEach(async () => {
        await HttpErrorInterceptorPageObject.Actions.navigate();
        await browser.waitForWholeAppToBeReady();
    });

    afterEach(async () => {
        await browser.waitUntilNoModal();
    });

    // Default interceptors
    it('WHEN a resource not found error (404) is triggered for Content-type json THEN I expect to see an alert message', async () => {
        await browser.waitUntilNoModal();
        await HttpErrorInterceptorPageObject.Actions.triggerError404Json();
        await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(1);
        await AlertsComponentObject.Assertions.assertAlertMessageByIndex(
            0,
            'Your request could not be processed! Please try again later!'
        );
    });

    it('WHEN a resource not found error (404) is triggered for Content-type html THEN no alert message is displayed', async () => {
        await browser.waitUntilNoModal();
        await HttpErrorInterceptorPageObject.Actions.triggerError404Html();
        await AlertsComponentObject.Assertions.assertNoAlertsDisplayed();
    });

    it('WHEN a bad request (400) is triggered for Content-type json THEN I expect to see an alert message', async () => {
        await browser.waitUntilNoModal();
        await HttpErrorInterceptorPageObject.Actions.triggerError400Json();
        await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(2);
        await AlertsComponentObject.Assertions.assertAlertMessageByIndex(1, 'error: bad request');
    });

    // Custom interceptors
    it('WHEN I add a custom interceptor for 501 errors of Content-type json AND a 501 error of Content-type json is triggered THEN I expect to see an alert message', async () => {
        await browser.waitUntilNoModal();
        await HttpErrorInterceptorPageObject.Actions.triggerError501Json();
        await AlertsComponentObject.Assertions.assertTotalNumberOfAlerts(1);
        await AlertsComponentObject.Assertions.assertAlertMessageByIndex(
            0,
            'error: 501 bad request'
        );
    });

    // Graceful degradation
    it(
        'GIVEN a custom retry strategy is registered for 503 error WHEN a 503 error is triggered that ' +
            'correspond to a operation context THEN I expect to see a message when maximum of retry is reached',
        async () => {
            await browser.waitUntilNoModal();
            await HttpErrorInterceptorPageObject.Actions.triggerError503();

            const text = await HttpErrorInterceptorPageObject.Elements.getGraceFulDegradationStatus().getText();

            expect(text).toBe('FAILED');
        }
    );

    it(
        'GIVEN a custom retry strategy is registered for a request that fails twice before being successfull WHEN the request is made ' +
            'THEN I expect to see a retry in progress and THEN a success message',
        async () => {
            await browser.waitUntilNoModal();
            await HttpErrorInterceptorPageObject.Actions.triggerError502();

            const text = await HttpErrorInterceptorPageObject.Elements.getGraceFulDegradationStatus().getText();

            expect(text).toBe('PASSED');
        }
    );
});
