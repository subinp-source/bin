/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element } from 'protractor';

import { Page } from '../utils/components/Page';
import { Perspectives } from '../utils/components/Perspectives';
import { Storefront } from '../utils/components/Storefront';

describe('Slot Contextual Menu Decorator', () => {
    beforeEach(async () => {
        await Page.Actions.getAndWaitForWholeApp('test/e2e/slotContextualMenu/index.html');

        await browser.waitForWholeAppToBeReady();
        await Perspectives.Actions.selectPerspective(
            Perspectives.Constants.DEFAULT_PERSPECTIVES.ALL
        );
    });

    it('WHEN I hover over a content slot THEN the ID of the slot appears in the slot contextual menu decorator', async () => {
        expect(await slotNamePanels().count()).toBe(0);

        await hoverOverSlot(Storefront.Constants.BOTTOM_HEADER_SLOT_ID);
        expect(await slotNamePanels().count()).toBe(1);
        expect(await slotNamePanel().getText()).toContain(
            Storefront.Constants.BOTTOM_HEADER_SLOT_ID
        );

        await hoverOverSlot(Storefront.Constants.FOOTER_SLOT_ID);
        expect(await slotNamePanels().count()).toBe(1);
        expect(await slotNamePanel().getText()).toContain(Storefront.Constants.FOOTER_SLOT_ID);
    });

    it('WHEN I hover over a content slot THEN the slot contextual menu items appear in the slot contextual menu decorator', async () => {
        await hoverOverSlot(Storefront.Constants.BOTTOM_HEADER_SLOT_ID);
        await browser.click(
            by.css(
                '.smartEditComponentX[data-smartedit-component-id="' +
                    Storefront.Constants.BOTTOM_HEADER_SLOT_ID +
                    '"]'
            )
        );
        expect(
            await contextualMenuItemForSlot(
                'slot.context.menu.title.dummy1',
                Storefront.Constants.BOTTOM_HEADER_SLOT_ID
            ).isPresent()
        ).toBe(true);
    });

    it('WHEN I click one of the slot contextual menu items THEN I expect the callback to be triggered', async () => {
        await hoverOverSlot(Storefront.Constants.BOTTOM_HEADER_SLOT_ID);
        await browser.click(
            by.css(
                '.smartEditComponentX[data-smartedit-component-id="' +
                    Storefront.Constants.BOTTOM_HEADER_SLOT_ID +
                    '"]'
            )
        );
        expect(
            await contextualMenuItemForSlot(
                'slot.context.menu.title.dummy1',
                Storefront.Constants.BOTTOM_HEADER_SLOT_ID
            ).isPresent()
        ).toBe(true);
        expect(
            await contextualMenuItemForSlot(
                'slot.context.menu.title.dummy2',
                Storefront.Constants.BOTTOM_HEADER_SLOT_ID
            ).isPresent()
        ).toBe(true);
        await browser.click(
            await contextualMenuItemForSlot(
                'slot.context.menu.title.dummy1',
                Storefront.Constants.BOTTOM_HEADER_SLOT_ID
            )
        );
        expect(
            await contextualMenuItemForSlot(
                'slot.context.menu.title.dummy1',
                Storefront.Constants.BOTTOM_HEADER_SLOT_ID
            ).isPresent()
        ).toBe(true);
        expect(
            await contextualMenuItemForSlot(
                'slot.context.menu.title.dummy2',
                Storefront.Constants.BOTTOM_HEADER_SLOT_ID
            ).isPresent()
        ).toBe(true);
    });

    // Actions
    async function hoverOverSlot(slotId: string): Promise<void> {
        await Storefront.Actions.moveToComponent(slotId, 'ContentSlot');
    }

    // Elements
    function slotNamePanels() {
        return element.all(by.css('.se-decorative-panel-area .se-decorative-panel__title'));
    }

    function slotNamePanel() {
        return element(by.css('.se-decorative-panel-area .se-decorative-panel__title'));
    }

    function contextualMenuItemForSlot(key: string, slotID: string) {
        return element(by.id(key + '-' + slotID + '-ContentSlot'));
    }
});
