/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace NotificationPanelComponentObject {
    const NOTIFICATION_ID_PREFIX = 'se-notification-';

    export const Elements = {
        async notificationPanel(): Promise<ElementFinder> {
            await browser.switchToParent();

            return element(by.css('div.se-notification-panel'));
        },
        allNotifications(): ElementArrayFinder {
            return element.all(
                by.xpath('//*[contains(@id, "' + NOTIFICATION_ID_PREFIX + '")]')
            ) as any;
        },
        async notificationById(notificationId: string): Promise<ElementFinder> {
            await browser.switchToParent();
            return element(
                by.css(
                    'div.se-notification-panel se-notification div[id*="' + notificationId + '"]'
                )
            );
        },
        notificationByIndex(index: number): ElementFinder {
            return Elements.allNotifications().get(index);
        }
    };

    export const Actions = {
        async moveMousePointerOverNotificationPanel(): Promise<void> {
            await browser.switchToParent();
            const notificationPanel = await Elements.notificationPanel();
            await browser
                .actions()
                .mouseMove(notificationPanel)
                .perform();
        },
        async moveMousePointerOutOfNotificationPanel(): Promise<void> {
            const offset = {
                x: 10,
                y: 10
            };

            await browser.switchToParent();
            await Elements.notificationPanel();
            await browser
                .actions()
                .mouseMove(offset)
                .perform();
        }
    };

    export const Assertions = {
        async assertNotificationPanelDisplayed(): Promise<void> {
            const notificationPanel = await Elements.notificationPanel();
            await browser.waitToBeDisplayed(
                notificationPanel,
                'could not assert that the notification panel is displayed'
            );
        },
        async assertNotificationPanelNotDisplayed(): Promise<void> {
            const notificationPanel = await Elements.notificationPanel();
            await browser.waitNotToBeDisplayed(
                notificationPanel,
                'could not assert that the notification panel is not displayed'
            );
        },
        async assertNotificationPresenceById(notificationId: string): Promise<void> {
            const notification = await Elements.notificationById(notificationId);
            await browser.waitForPresence(
                notification,
                'could not assert presence of notification with ID "' + notificationId + '"'
            );
        },
        async assertNotificationAbsenceById(notificationId: string): Promise<void> {
            const notification = await Elements.notificationById(notificationId);
            await browser.waitForAbsence(
                notification,
                'could not assert absence of notification with ID "' + notificationId + '"'
            );
        },
        async assertNotificationTemplateById(
            notificationId: string,
            template: string
        ): Promise<void> {
            const notification = await Elements.notificationById(notificationId);
            expect(await browser.getInnerHTML(notification)).toContain(template);
        },
        async assertNotificationIdByIndex(index: number, notificationId: string): Promise<void> {
            expect(await Elements.notificationByIndex(index).getAttribute('id')).toEqual(
                NOTIFICATION_ID_PREFIX + notificationId
            );
        },
        async assertNumberOfNotifications(numberOfNotifications: number): Promise<void> {
            expect(await Elements.allNotifications().count()).toBe(numberOfNotifications);
        }
    };
}
