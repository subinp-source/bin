/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace AlertsComponentObject {
    const ID_PREFIX = 'fd-alert';

    function getSelectorStrByIndex(index: number): string {
        return ID_PREFIX + ':nth-child' + '(' + (index + 1) + ')';
    }

    export const Elements = {
        allAlerts(): ElementArrayFinder {
            return element.all(by.css(ID_PREFIX));
        },

        alertByIndex(index: number): ElementFinder {
            return element(by.css(getSelectorStrByIndex(index)));
        },

        closeButtonByIndex(index: number): ElementFinder {
            return element(by.css(getSelectorStrByIndex(index) + ' > .fd-alert__close'));
        },

        linkInAlertByIndex(index: number): ElementFinder {
            return element(by.css(getSelectorStrByIndex(index) + ' a'));
        }
    };

    export const Actions = {
        async closeAlertByIndex(index: number): Promise<void> {
            await browser.waitForPresence(Elements.closeButtonByIndex(index));
            await browser.click(Elements.closeButtonByIndex(index));
        },

        async flush(): Promise<void> {
            const count = await Elements.allAlerts().count();

            for (let i = 0; i < count; i++) {
                await Actions.closeAlertByIndex(0);
            }

            await Assertions.assertNoAlertsDisplayed();
        },

        async clickOnLinkInAlertByIndex(index: number): Promise<void> {
            await browser.waitForPresence(Elements.linkInAlertByIndex(index));
            await browser.click(Elements.linkInAlertByIndex(index));
        }
    };

    export const Assertions = {
        async assertAlertIsOfTypeByIndex(index: number, type: string): Promise<void> {
            await browser.waitForPresence(
                Elements.alertByIndex(index),
                'failed to find alert by index: ' + index
            );
            const className = await Elements.alertByIndex(index).getAttribute('class');

            expect(className).toContain('fd-alert--' + type);
        },

        async assertAlertCloseabilityByIndex(index: number, closeable: boolean): Promise<void> {
            if (closeable) {
                await browser.waitToBeDisplayed(Elements.closeButtonByIndex(index));
            } else {
                await browser.waitForAbsence(Elements.closeButtonByIndex(index));
            }
        },

        async assertAlertMessageLinkByIndex(index: number, expectedText: string): Promise<void> {
            await browser.waitForPresence(
                Elements.linkInAlertByIndex(index),
                'failed to find alert message link by index: ' + index
            );

            const text = await Elements.linkInAlertByIndex(index).getText();

            expect(text).toContain(expectedText);
        },

        async assertAlertMessageByIndex(index: number, expectedText: string): Promise<void> {
            await browser.waitForPresence(
                Elements.alertByIndex(index),
                'failed to find alert message by index: ' + index
            );

            const text = await Elements.alertByIndex(index).getText();

            expect(text).toContain(expectedText);
        },

        async assertNoAlertsDisplayed(): Promise<void> {
            await browser.waitForAbsence(Elements.alertByIndex(0));
        },

        async assertTotalNumberOfAlerts(numExpectedAlerts: number): Promise<void> {
            await browser.waitUntil(function() {
                return Elements.allAlerts()
                    .count()
                    .then(function(count: number) {
                        return count === numExpectedAlerts;
                    });
            }, 'was expecting to see ' + numExpectedAlerts + ' alerts');
        }
    };
}
