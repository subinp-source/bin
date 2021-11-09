/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace AnnouncementPageObject {
    export enum Constants {
        NON_CLOSEABLE_ANNOUNCEMENT_CONTENT = 'This is a non closeable announcement',
        SIMPLE_ANNOUNCEMENT_CONTENT = 'This is a simple announcement',
        TEMPLATED_BASED_ANNOUNCEMENT_CONTENT = 'This is a template based announcement',
        TEMPLATED_URL_BASED_ANNOUNCEMENT_CONTENT = 'This is an announcement coming from a template url and static data',
        CUSTOM_CTRL_BASED_ANNOUNCEMENT_CONTENT = 'This is the data coming from a custom controller',
        COMPONENT_BASED_ANNOUNCEMENT_CONTENT = 'Component Based Announcement Message'
    }

    export const Elements = {
        nonCloseableAnnouncementButton(): ElementFinder {
            return element(by.id('test-announcement-non-closeable-button'));
        },

        simpleAnnouncementButton(): ElementFinder {
            return element(by.id('test-announcement-simple-button'));
        },

        templateBasedAnnouncementButton(): ElementFinder {
            return element(by.id('test-announcement-template-button'));
        },

        templateUrlBasedAnnouncementButton(): ElementFinder {
            return element(by.id('test-announcement-templateurl-button'));
        },

        customCtrlBasedAnnouncementButton(): ElementFinder {
            return element(by.id('test-announcement-templateurl-customctrl-button'));
        },

        componentBasedAnnouncementButton(): ElementFinder {
            return element(by.id('test-announcement-component-button'));
        }
    };

    export const Actions = {
        async navigateToTestPage(): Promise<void> {
            await browser.get('test/e2e/announcement/index.html');
        },

        async displayNonCloseableAnnouncement(): Promise<void> {
            await AnnouncementPageObject.Elements.nonCloseableAnnouncementButton().click();
        },

        async displaySimpleAnnouncement(): Promise<void> {
            await AnnouncementPageObject.Elements.simpleAnnouncementButton().click();
        },

        async displayTemplateBasedAnnouncement(): Promise<void> {
            await AnnouncementPageObject.Elements.templateBasedAnnouncementButton().click();
        },

        async displayTemplateUrlBasedAnnouncement(): Promise<void> {
            await AnnouncementPageObject.Elements.templateUrlBasedAnnouncementButton().click();
        },

        async displayCustomCtrlBasedAnnouncement(): Promise<void> {
            await AnnouncementPageObject.Elements.customCtrlBasedAnnouncementButton().click();
        },

        async displayComponentBasedAnnouncement(): Promise<void> {
            await AnnouncementPageObject.Elements.componentBasedAnnouncementButton().click();
        }
    };
}
