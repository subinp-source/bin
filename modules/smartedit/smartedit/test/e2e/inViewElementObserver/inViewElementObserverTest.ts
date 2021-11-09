/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

import { Page } from '../utils/components/Page';
import { Storefront } from '../utils/components/Storefront';
import { InViewElementObserverPageObject } from './inViewElementObserverPageObject';
import { SfBuilderComponentObject } from '../utils/components/SfBuilderComponentObject';

describe('inViewElementObserver -', () => {
    const SLOT_ID = 'headerLinksSlot';
    const INITIAL_ELEMENTS_COUNT = 3;

    beforeEach(async () => {
        await Page.Actions.getAndWaitForWholeApp('test/e2e/inViewElementObserver/index.html');
        await browser.switchToIFrame();
    });

    it('WHEN initializing (no scroll), only 3 eligible elements are in view', async () => {
        await InViewElementObserverPageObject.Assertions.inSync(INITIAL_ELEMENTS_COUNT);
    });

    it('WHEN an out of view component is removed from the DOM, it is removed from the queue but not from the visible elements', async () => {
        await SfBuilderComponentObject.Actions.removeComponent(SLOT_ID);
        await InViewElementObserverPageObject.Assertions.inSync(INITIAL_ELEMENTS_COUNT);
    });

    it('WHEN an out of view component is addded to the DOM, it is add to the queue but not to the visible elements', async () => {
        await SfBuilderComponentObject.Actions.addComponent('blabla');

        await InViewElementObserverPageObject.Assertions.inSync(INITIAL_ELEMENTS_COUNT);
    });

    it('WHEN a component is added in view and removed in view, it is added to /removed from the queue and added to /removed from the visible elements', async () => {
        await InViewElementObserverPageObject.Assertions.inSync(INITIAL_ELEMENTS_COUNT);

        await InViewElementObserverPageObject.Actions.addComponentAsFirst();

        await InViewElementObserverPageObject.Assertions.inSync(4);

        await InViewElementObserverPageObject.Actions.removeFirstComponent();

        await InViewElementObserverPageObject.Assertions.inSync(INITIAL_ELEMENTS_COUNT);
    });

    it('WHEN scrolling down and back up, the number of visible components adjusts and then resets', async () => {
        await Storefront.Actions.moveToComponent('headerLinksSlot', 'ContentSlot');

        await InViewElementObserverPageObject.Assertions.inSync(7);

        await Storefront.Actions.moveToComponent('topHeaderSlot', 'ContentSlot');

        await InViewElementObserverPageObject.Assertions.inSync(3);
    });
});
