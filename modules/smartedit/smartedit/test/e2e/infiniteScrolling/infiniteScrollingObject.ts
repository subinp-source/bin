/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace InfiniteScrollingPageObject {
    export const Elements = {
        getContainer: (hostId: string): ElementFinder => {
            return element(by.id(hostId));
        },
        getMaskInput: (hostId: string): ElementFinder => {
            return Elements.getContainer(hostId).element(by.css('#mask'));
        },
        getScrollToBottomButton: (hostId: string): ElementFinder => {
            return Elements.getContainer(hostId).element(by.css('#scrollToBottom'));
        },

        getInfiniteScrollContainer: (hostId: string): ElementFinder => {
            return Elements.getContainer(hostId).element(
                by.css('.se-infinite-scrolling__container')
            );
        },

        getInfiniteScrollHolder: (hostId: string): ElementFinder => {
            return Elements.getContainer(hostId).element(by.css('.se-infinite-scrolling__holder'));
        },

        getSingleItem: (hostId: string, itemId: string): ElementFinder => {
            return Elements.getContainer(hostId).element(by.css(`#item${itemId}`));
        },

        getAllItems: (hostId: string): ElementArrayFinder => {
            return Elements.getContainer(hostId).all(by.css('.item'));
        }
    };

    export const Actions = {
        inputMask: async (hostId: string, input: string): Promise<void> => {
            await browser.sendKeys(Elements.getMaskInput(hostId), input);
        },
        clickScrollButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getScrollToBottomButton(hostId));
        }
    };

    export const Assertions = {
        infiniteScrollContainerHasClass: async (
            hostId: string,
            className: string
        ): Promise<void> => {
            const actual = await Elements.getInfiniteScrollContainer(hostId).getAttribute('class');

            expect(actual).toContain(className);
        },

        infiniteScrollHolderHasClass: async (hostId: string, className: string): Promise<void> => {
            const actual = await Elements.getInfiniteScrollHolder(hostId).getAttribute('class');

            expect(actual).toContain(className);
        },

        itemIsPresent: async (hostId: string, itemId: string): Promise<void> => {
            await browser.waitForPresence(Elements.getSingleItem(hostId, itemId));
        },

        itemIsAbsent: async (hostId: string, itemId: string): Promise<void> => {
            await browser.waitForAbsence(Elements.getSingleItem(hostId, itemId));
        },

        hasCorrectItemCount: async (hostId: string, count: number): Promise<void> => {
            const actual = await Elements.getAllItems(hostId).count();
            expect(actual).toBe(count);
        }
    };
}
