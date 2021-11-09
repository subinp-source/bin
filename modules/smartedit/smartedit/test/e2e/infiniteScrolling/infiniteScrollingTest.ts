/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

import { InfiniteScrollingPageObject } from './infiniteScrollingObject';

describe('Infinite Scrolling - ', () => {
    const infiniteScrollingDefault = 'default-container';
    const infiniteScrollingImmediate = 'immediate-check-container';

    beforeEach(async () => {
        await browser.get('test/e2e/infiniteScrolling/index.html');
        await browser.waitForContainerToBeReady();
    });

    describe('Initial Setup', () => {
        it('Infinite Scrolling Container has correct class', async () => {
            await InfiniteScrollingPageObject.Assertions.infiniteScrollContainerHasClass(
                infiniteScrollingDefault,
                'container-class'
            );
        });

        it('Infinite Scrolling Holder has correct class', async () => {
            await InfiniteScrollingPageObject.Assertions.infiniteScrollHolderHasClass(
                infiniteScrollingDefault,
                'holder-class'
            );
        });

        it('Infinite Scroll has correct children count', async () => {
            await InfiniteScrollingPageObject.Assertions.hasCorrectItemCount(
                infiniteScrollingDefault,
                10
            );
        });

        it('Infinite Scroll has one of initial items', async () => {
            await InfiniteScrollingPageObject.Assertions.itemIsPresent(
                infiniteScrollingDefault,
                '0'
            );
        });

        it('Infinite Scroll doesnt have item out of its initial scope', async () => {
            await InfiniteScrollingPageObject.Assertions.itemIsAbsent(
                infiniteScrollingDefault,
                '20'
            );
        });

        it('Infinite Scroll should fetch next page when consumer applied condition for displaying items so that the container height was not populated by the items from the first page', async () => {
            await InfiniteScrollingPageObject.Assertions.itemIsPresent(
                infiniteScrollingImmediate,
                '10'
            );
            await InfiniteScrollingPageObject.Assertions.itemIsPresent(
                infiniteScrollingImmediate,
                '19'
            );
        });
    });

    describe('After scroll - ', () => {
        beforeEach(async () => {
            await InfiniteScrollingPageObject.Actions.clickScrollButton(infiniteScrollingDefault);
        });

        it('Has correct children count', async () => {
            await InfiniteScrollingPageObject.Assertions.hasCorrectItemCount(
                infiniteScrollingDefault,
                20
            );
        });

        it('Infinite Scroll has item', async () => {
            await InfiniteScrollingPageObject.Assertions.itemIsPresent(
                infiniteScrollingDefault,
                '19'
            );
        });
    });

    describe('Mask - ', () => {
        beforeEach(async () => {
            await InfiniteScrollingPageObject.Actions.inputMask(infiniteScrollingDefault, '10');
        });

        it('Has correct children count', async () => {
            await InfiniteScrollingPageObject.Assertions.hasCorrectItemCount(
                infiniteScrollingDefault,
                1
            );
        });

        it('Infinite Scroll has item', async () => {
            await InfiniteScrollingPageObject.Assertions.itemIsPresent(
                infiniteScrollingDefault,
                '10'
            );
        });

        it('Infinite Scroll doesnt have item', async () => {
            await InfiniteScrollingPageObject.Assertions.itemIsAbsent(
                infiniteScrollingDefault,
                '1'
            );
        });
    });
});
