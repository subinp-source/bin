/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';
import { INotificationConfiguration } from 'smarteditcommons';

export namespace NotificationPanelTestPageObject {
    const NOTIFICATION_ID = 'test.notification.id';
    const NOTIFICATION_TEMPLATE_CONTENT = 'This is a test notification template.';

    export const Constants = {
        NOTIFICATION_ID,
        NOTIFICATION_ID1: NOTIFICATION_ID + '1',
        NOTIFICATION_ID2: NOTIFICATION_ID + '2',
        NOTIFICATION_ID3: NOTIFICATION_ID + '3',
        NOTIFICATION_TEMPLATE_URL: 'testNotificationTemplate.html',
        NOTIFICATION_TEMPLATE_CONTENT,
        NOTIFICATION_TEMPLATE_HTML: `<div>${NOTIFICATION_TEMPLATE_CONTENT}</div>`,
        COMPONENT_BASED_NOTIFICATION_CONTENT: 'This is a test component based notification'
    };

    export const Elements = {
        notificationId(): ElementFinder {
            return element(by.id('test-notification-id'));
        },

        notificationTemplate(): ElementFinder {
            return element(by.id('test-notification-template'));
        },

        notificationTemplateUrl(): ElementFinder {
            return element(by.id('test-notification-template-url'));
        },

        pushButton(): ElementFinder {
            return element(by.id('test-notification-push-button'));
        },

        removeButton(): ElementFinder {
            return element(by.id('test-notification-remove-button'));
        },

        removeAllButton(): ElementFinder {
            return element(by.id('test-notification-remove-all-button'));
        },

        resetButton(): ElementFinder {
            return element(by.id('test-notification-reset-button'));
        },

        clickThroughCheckbox(): ElementFinder {
            return element(by.id('test-notification-clickthrough-checkbox'));
        },

        goToStorefrontButton(): ElementFinder {
            return element(by.id('test-notification-goto-storefront'));
        },

        componentBasedNotificationButton(): ElementFinder {
            return element(by.id('test-notification-component-button'));
        }
    };

    export const Actions = {
        async navigateToTestPage(): Promise<void> {
            await browser.get('test/e2e/notifications/index.html');
            await browser.waitForWholeAppToBeReady();
            await browser.waitUntilNoModal();
        },

        async navigateToStorefront(): Promise<void> {
            await Actions.navigateToTestPage();
            await browser.waitForWholeAppToBeReady();
            await browser.waitUntilNoModal();
        },

        async setNotificationId(notificationId: string): Promise<void> {
            await browser.sendKeys(Elements.notificationId(), notificationId);
        },

        async setNotificationTemplate(template: string): Promise<void> {
            await browser.sendKeys(Elements.notificationTemplate(), template);
        },

        async setNotificationTemplateUrl(templateUrl: string): Promise<void> {
            await browser.sendKeys(Elements.notificationTemplateUrl(), templateUrl);
        },

        async clickPushButton(): Promise<void> {
            await browser.click(Elements.pushButton());
        },

        async clickRemoveButton(): Promise<void> {
            await browser.click(Elements.removeButton());
        },

        async clickRemoveAllButton(): Promise<void> {
            await browser.click(Elements.removeAllButton());
        },

        async clickResetButton(): Promise<void> {
            await browser.click(Elements.resetButton());
        },

        async clickClickThroughCheckbox(): Promise<void> {
            await browser.click(
                Elements.clickThroughCheckbox(),
                'Could not click through checkbox.'
            );
        },

        async clickClickThroughCheckboxInIFrame(): Promise<void> {
            await browser.switchToIFrame(true);
            await Actions.clickClickThroughCheckbox();
        },

        async clickGoToStorefrontButton(): Promise<void> {
            await browser.click(Elements.goToStorefrontButton());
        },

        async clickComponentBasedNotificationButton(): Promise<void> {
            await browser.click(Elements.componentBasedNotificationButton());
        },

        async resetForm(): Promise<void> {
            await browser.waitForContainerToBeReady();
            await Actions.clickResetButton();
        },

        async pushNotification(notification: INotificationConfiguration): Promise<void> {
            await Actions.resetForm();
            const { id: notificationId, template, templateUrl } = notification;

            if (notificationId) {
                await Actions.setNotificationId(notificationId);
            }

            if (template) {
                await Actions.setNotificationTemplate(template);
            }

            if (templateUrl) {
                await Actions.setNotificationTemplateUrl(templateUrl);
            }

            await Actions.clickPushButton();
        },

        async removeNotification(notificationId: string): Promise<void> {
            await Actions.resetForm();

            if (notificationId) {
                await Actions.setNotificationId(notificationId);
            }

            await Actions.clickRemoveButton();
        },

        async removeAllNotifications(): Promise<void> {
            await browser.waitUntilNoModal();
            await Actions.clickRemoveAllButton();
        },

        async displayComponentBasedNotification(notificationId: string): Promise<void> {
            await Actions.setNotificationId(notificationId);
            await Actions.clickComponentBasedNotificationButton();
        }
    };

    export const Assertions = {
        async _assertClickThroughCheckboxSelected(isSelected: boolean): Promise<void> {
            const actual = await Elements.clickThroughCheckbox().isSelected();
            expect(actual).toBe(isSelected);
        },

        async _assertClickThroughCheckboxNotClickable(clickPromise: Promise<void>): Promise<void> {
            const onSuccess = () => true;
            const onError = () => false;

            const val = await clickPromise.then(onSuccess, onError);
            expect(val).toBe(false);
        },

        async assertClickThroughCheckboxSelected(): Promise<void> {
            await Assertions._assertClickThroughCheckboxSelected(true);
        },

        async assertClickThroughCheckboxNotSelected(): Promise<void> {
            await Assertions._assertClickThroughCheckboxSelected(false);
        },

        async assertClickThroughCheckboxNotClickable(): Promise<void> {
            await Assertions._assertClickThroughCheckboxNotClickable(
                Actions.clickClickThroughCheckbox()
            );
        },

        async assertClickThroughCheckboxInIFrameNotClickable(): Promise<void> {
            await Assertions._assertClickThroughCheckboxNotClickable(
                Actions.clickClickThroughCheckboxInIFrame()
            );
        }
    };
}
