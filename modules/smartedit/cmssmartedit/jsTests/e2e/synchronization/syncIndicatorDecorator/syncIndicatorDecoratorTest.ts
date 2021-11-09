/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { browser } from 'protractor';
const e2e = (global as any).e2e;

describe('syncIndicatorDecorator', () => {
    const storefront = e2e.componentObjects.storefront;
    const syncIndicator = e2e.componentObjects.syncIndicatorDecorator;
    const modeSelector = e2e.componentObjects.modeSelector;
    const synchronizationPanel = e2e.componentObjects.synchronizationPanel;
    const syncMenu = e2e.componentObjects.syncMenu;

    beforeEach(() => {
        browser.bootstrap(__dirname);
    });

    beforeEach((done) => {
        browser.waitForWholeAppToBeReady().then(() => {
            modeSelector.selectAdvancedPerspective().then(() => {
                browser.switchToIFrame().then(() => {
                    synchronizationPanel.setupTest();
                    done();
                });
            });
        });
    });

    describe('Sync indicator for slots', () => {
        it('GIVEN I am in advanced edit mode WHEN the page is fully loaded and there are 1 out-of-sync slots THEN I expect the decorators to present a NOT_IN_SYNC state for 1 out-of-sync slots.', () => {
            storefront.actions.moveToComponent(storefront.constants.BOTTOM_HEADER_SLOT_ID);
            syncIndicator.assertions.slotIsOutOfSync(storefront.constants.BOTTOM_HEADER_SLOT_ID);
        });

        it('GIVEN I am in advanced edit mode WHEN the page is fully loaded and there are 1 in-sync slot THEN I expect the decorators to have an IN_SYNC status for one in-sync slot.', () => {
            storefront.actions.moveToComponent(storefront.constants.OTHER_SLOT_ID);
            syncIndicator.assertions.slotIsInSync(storefront.constants.OTHER_SLOT_ID);
        });

        it('GIVEN I open sync panel of topHeaderSlot then open sync panel of page WHEN I sync topHeaderSlot from the page panel THEN the status of the associated decorator should automatically be updated.', () => {
            storefront.actions.moveToComponent(storefront.constants.TOP_HEADER_SLOT_ID);
            syncIndicator.assertions.slotIsOutOfSync(storefront.constants.TOP_HEADER_SLOT_ID);

            syncMenu.actions.click();
            synchronizationPanel.checkItem('All Slots and Page Information');
            synchronizationPanel.clickSync().then(() => {
                synchronizationPanel.switchToIFrame().then(() => {
                    syncIndicator.assertions.slotIsInSync(storefront.constants.TOP_HEADER_SLOT_ID);
                });
            });
        });
    });
});
