/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { Perspectives } from '../utils/components/Perspectives';
import { Page } from '../utils/components/Page';
import { Storefront } from '../utils/components/Storefront';
import { SfBuilderComponentObject } from '../utils/components/SfBuilderComponentObject';
import { browser, by, element, ElementFinder } from 'protractor';

describe('E2E Test for decorator service module', () => {
    beforeEach(async () => {
        await Page.Actions.getAndWaitForWholeApp('test/e2e/decorator/index.html');

        await browser.waitForWholeAppToBeReady();
        await Perspectives.Actions.selectPerspective(
            Perspectives.Constants.DEFAULT_PERSPECTIVES.ALL
        );

        await browser.switchToIFrame();
    });

    it('WHEN a component is wired with a single decorator THEN I expect only that decorator to be present with the transcluded content', async () => {
        expect(
            await Perspectives.Elements.deprecated_getElementInOverlay(
                Storefront.Constants.COMPONENT_1_ID,
                Storefront.Constants.COMPONENT_1_TYPE
            ).getText()
        ).toContain('Text_is_been_displayed_TextDisplayDecorator');
        expect(
            await Perspectives.Elements.deprecated_getElementInOverlay(
                Storefront.Constants.COMPONENT_1_ID,
                Storefront.Constants.COMPONENT_1_TYPE
            ).getText()
        ).not.toContain('Button_is_been_Displayed');
    });

    it('WHEN a component is wired with some other decorator THEN I expect only that decorator to be present with the transcluded content', async () => {
        expect(
            await Perspectives.Elements.deprecated_getElementInOverlay(
                Storefront.Constants.COMPONENT_2_ID,
                Storefront.Constants.COMPONENT_2_TYPE
            ).getText()
        ).not.toContain('Text_is_been_displayed_TextDisplayDecorator');
        expect(
            await Perspectives.Elements.deprecated_getElementInOverlay(
                Storefront.Constants.COMPONENT_2_ID,
                Storefront.Constants.COMPONENT_2_TYPE
            ).getText()
        ).toContain('Button_is_been_Displayed');
    });

    it('WHEN a component is wired with multiple decorators THEN I expect those decorators to be present with the transcluded content', async () => {
        expect(
            await Perspectives.Elements.deprecated_getElementInOverlay(
                Storefront.Constants.COMPONENT_3_ID,
                Storefront.Constants.COMPONENT_3_TYPE
            ).getText()
        ).toContain('Text_is_been_displayed_TextDisplayDecorator');
        expect(
            await Perspectives.Elements.deprecated_getElementInOverlay(
                Storefront.Constants.COMPONENT_3_ID,
                Storefront.Constants.COMPONENT_3_TYPE
            ).getText()
        ).toContain('Button_is_been_Displayed');
    });

    it('WHEN decorators are wired for both a component and its slot THEN I expect to see both slot and component decorators appear', async () => {
        expect(
            await Perspectives.Elements.deprecated_getElementInOverlay(
                Storefront.Constants.TOP_HEADER_SLOT_ID,
                'ContentSlot'
            ).getText()
        ).toContain('slot_text_is_been_displayed_SlotTextDisplayDecorator');
        expect(
            await Perspectives.Elements.deprecated_getElementInOverlay(
                Storefront.Constants.TOP_HEADER_SLOT_ID,
                'ContentSlot'
            ).getText()
        ).toContain('Slot_button_is_been_Displayed');
    });

    it('WHEN switching back to preview mode, THEN add to cart button still works', async () => {
        await Perspectives.Actions.selectPerspective(
            Perspectives.Constants.DEFAULT_PERSPECTIVES.NONE
        );
        await browser.switchToIFrame();
        await browser.click(Storefront.Elements.addToCartButton());
        expect(await Storefront.Elements.addToCartFeedback().getText()).toBe('1');
    });

    it('WHEN soft deep link to another page, decorators are recompiled', async () => {
        await waitToContainText(
            Storefront.Constants.COMPONENT_1_ID,
            Storefront.Constants.COMPONENT_1_TYPE,
            'homepage'
        );

        await SfBuilderComponentObject.Actions.changePageIdWithoutInteration('secondpage');

        await waitNotToContainText(
            Storefront.Constants.COMPONENT_1_ID,
            Storefront.Constants.COMPONENT_1_TYPE,
            'homepage'
        );
        await waitToContainText(
            Storefront.Constants.COMPONENT_1_ID,
            Storefront.Constants.COMPONENT_1_TYPE,
            'secondpage'
        );
    });

    it('GIVEN component update requires refresh WHEN component is updated THEN the decorators are replayed', async () => {
        // GIVEN
        await browser
            .actions()
            .mouseMove(element(by.id(Storefront.Constants.COMPONENT_4_ID)))
            .perform();
        const component = Perspectives.Elements.deprecated_getElementInOverlay(
            Storefront.Constants.COMPONENT_4_ID,
            Storefront.Constants.COMPONENT_4_TYPE
        );

        await changeTextInComponent(component);
        expect(await component.getText()).toContain('Button_has_been_Clicked');

        // WHEN
        await replayComponentDecorators(component);

        // THEN
        expect(await component.getText()).toContain('Button_is_been_Displayed');
    });

    async function waitNotToContainText(
        id: string,
        type: string,
        expectedText: string
    ): Promise<void> {
        await checkContainsText(id, type, expectedText, false);
    }

    async function waitToContainText(
        id: string,
        type: string,
        expectedText: string
    ): Promise<void> {
        await checkContainsText(id, type, expectedText, true);
    }

    async function checkContainsText(
        id: string,
        type: string,
        expectedText: string,
        operator: boolean
    ): Promise<void> {
        await browser.waitUntil(async () => {
            try {
                const actualText = await Perspectives.Elements.deprecated_getElementInOverlay(
                    id,
                    type
                ).getText();

                const contains = actualText.indexOf(expectedText) > -1;
                return operator ? contains : !contains;
            } catch (e) {
                return false;
            }
        }, 'failed to see text ' + expectedText + ' in component of id ' + id + ' and type' + type);
    }

    async function changeTextInComponent(component: ElementFinder): Promise<void> {
        const buttonSelector = 'button.main-button';
        await clickOnButtonInComponent(component, buttonSelector);
    }

    async function replayComponentDecorators(parentComponent: ElementFinder): Promise<void> {
        const refreshButtonSelector = 'button.refresh-button';
        await clickOnButtonInComponent(parentComponent, refreshButtonSelector);
    }

    async function clickOnButtonInComponent(
        parentComponent: ElementFinder,
        buttonSelector: string
    ): Promise<void> {
        await browser.click(parentComponent.element(by.css(buttonSelector)));
    }
});
