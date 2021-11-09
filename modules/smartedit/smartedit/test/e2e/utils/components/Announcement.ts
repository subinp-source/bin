/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace AnnouncementComponentObject {
    export enum Constants {
        ANNOUNCEMENT_ID_PREFIX = 'se-announcement-'
    }

    export const Elements = {
        getAnnouncements(): ElementArrayFinder {
            return element.all(by.css('se-announcement-board se-announcement'));
        },

        getAnnouncementByIndex(index: number): ElementFinder {
            return element(
                by.css('#' + AnnouncementComponentObject.Constants.ANNOUNCEMENT_ID_PREFIX + index)
            );
        },

        getCloseButton(index: number): ElementFinder {
            return AnnouncementComponentObject.Elements.getAnnouncementByIndex(index).element(
                by.css('.sap-icon--decline')
            );
        }
    };

    export const Assertions = {
        async assertTotalNumberOfAnnouncements(count: number): Promise<void> {
            const announcementsCount = await AnnouncementComponentObject.Elements.getAnnouncements().count();
            expect(announcementsCount).toBe(count);
        },

        async assertCloseButtonIsNotPresent(index: number): Promise<void> {
            expect(
                await browser.isAbsent(AnnouncementComponentObject.Elements.getCloseButton(index))
            ).toBe(true);
        },

        async assertCloseButtonIsPresent(index: number): Promise<void> {
            await browser.waitToBeDisplayed(
                AnnouncementComponentObject.Elements.getCloseButton(index),
                'could not assert that the announcement is displayed'
            );
        },

        async assertAnnouncementHasTextByIndex(index: number, expectedText: string): Promise<void> {
            await browser.waitUntil(async () => {
                const innerHTML = await AnnouncementComponentObject.Elements.getAnnouncementByIndex(
                    index
                ).getAttribute('innerHTML');
                const innerText = await AnnouncementComponentObject.Elements.getAnnouncementByIndex(
                    index
                ).getText();

                return innerHTML.includes(expectedText) || innerText.includes(expectedText);
            });
        },

        async assertAnnouncementIsNotDisplayed(index: number): Promise<void> {
            await browser.waitForAbsence(
                AnnouncementComponentObject.Elements.getAnnouncementByIndex(index)
            );
        }
    };
}
