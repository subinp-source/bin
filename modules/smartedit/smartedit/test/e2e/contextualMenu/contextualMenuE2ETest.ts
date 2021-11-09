/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { buildDecoratorName } from '../utils/outerCommon';
import { Storefront } from '../utils/components/Storefront';
import { Perspectives } from '../utils/components/Perspectives';
import { browser, by, element } from 'protractor';

describe('Contextual menu', () => {
    beforeEach(async () => {
        await browser.get('test/e2e/contextualMenu/index.html');

        await browser.waitForWholeAppToBeReady();
        await browser.waitForWholeAppToBeReady();
        await Perspectives.Actions.selectPerspective(
            Perspectives.Constants.DEFAULT_PERSPECTIVES.ALL
        );
        await browser.waitForWholeAppToBeReady();
    });

    it("Upon loading SmartEdit, contextualMenu named 'INFO' will be added to ComponentType1 and contextualMenu named 'DELETE' will be added to ComponentType2", async () => {
        await browser.switchToIFrame();

        // Assert on ComponentType1
        await browser.waitForPresence(element(by.id(Storefront.Constants.COMPONENT_1_ID)));

        await browser
            .actions()
            .mouseMove(element(by.id(Storefront.Constants.COMPONENT_1_ID)))
            .perform();

        expect(
            await element(
                by.id(
                    buildDecoratorName(
                        'INFO',
                        Storefront.Constants.COMPONENT_1_ID,
                        Storefront.Constants.COMPONENT_1_TYPE,
                        0
                    )
                )
            ).isPresent()
        ).toBe(true);
        expect(
            await element(
                by.id(
                    buildDecoratorName(
                        'DELETE',
                        Storefront.Constants.COMPONENT_1_ID,
                        Storefront.Constants.COMPONENT_1_TYPE,
                        0
                    )
                )
            ).isPresent()
        ).toBe(false);

        // Assert on ComponentType2
        browser
            .actions()
            .mouseMove(element(by.id(Storefront.Constants.COMPONENT_2_ID)))
            .perform();
        expect(
            await element(
                by.id(
                    buildDecoratorName(
                        'INFO',
                        Storefront.Constants.COMPONENT_2_ID,
                        Storefront.Constants.COMPONENT_2_TYPE,
                        0
                    )
                )
            ).isPresent()
        ).toBeFalsy();
        expect(
            await element(
                by.id(
                    buildDecoratorName(
                        'DELETE',
                        Storefront.Constants.COMPONENT_2_ID,
                        Storefront.Constants.COMPONENT_2_TYPE,
                        0
                    )
                )
            ).isPresent()
        ).toBe(true);
    });

    it('Display a string template popup', async () => {
        await browser.switchToIFrame();
        await browser
            .actions()
            .mouseMove(element(by.id(Storefront.Constants.COMPONENT_1_ID)))
            .perform();
        const e = Perspectives.Elements.deprecated_getElementInOverlay(
            Storefront.Constants.COMPONENT_1_ID,
            Storefront.Constants.COMPONENT_1_TYPE
        );
        await browser.click(e.element(by.css('.se-ctx-menu-element__btn--more')));
        await browser.click(
            by.id(
                buildDecoratorName(
                    'TEMPLATE',
                    Storefront.Constants.COMPONENT_1_ID,
                    Storefront.Constants.COMPONENT_1_TYPE,
                    2
                )
            )
        );
        expect(await element(by.css('#ctx-template')).isDisplayed()).toBe(true);
    });

    it('Display a templateUrl popup', async () => {
        await browser.switchToIFrame();
        await browser
            .actions()
            .mouseMove(element(by.id(Storefront.Constants.COMPONENT_2_ID)))
            .perform();
        await browser.click(
            by.id(
                buildDecoratorName(
                    'TEMPLATEURL',
                    Storefront.Constants.COMPONENT_2_ID,
                    Storefront.Constants.COMPONENT_2_TYPE,
                    1
                )
            )
        );
        expect(await element(by.css('#ctx-template-url')).isDisplayed()).toBe(true);
    });

    it('contextualMenu item WILL change the DOM element of ComponentType1 WHEN condition callback is called', async () => {
        await browser.switchToIFrame();
        // Assert on ComponentType1
        await browser.click(
            Perspectives.Elements.deprecated_getElementInOverlay(
                Storefront.Constants.COMPONENT_1_ID,
                Storefront.Constants.COMPONENT_1_TYPE
            )
        );
        expect(await element(by.className('conditionClass1')).isPresent()).toBe(true);
    });

    it('Can add and remove contextual menu items on the fly', async () => {
        // Arrange
        await browser.switchToIFrame();

        const component4DecoratorName = buildDecoratorName(
            'INFO',
            Storefront.Constants.COMPONENT_4_ID,
            Storefront.Constants.COMPONENT_4_TYPE,
            0
        );
        const component3DecoratorName = buildDecoratorName(
            'enable',
            Storefront.Constants.COMPONENT_3_ID,
            Storefront.Constants.COMPONENT_3_TYPE,
            0
        );

        expect(await element(by.id(component4DecoratorName)).isPresent()).toBe(false);

        // Act / Assert
        await Storefront.Assertions.assertComponentInOverlayPresent(
            Storefront.Constants.COMPONENT_3_ID,
            Storefront.Constants.COMPONENT_3_TYPE,
            true
        );
        await browser.click(by.id(component3DecoratorName));

        await Storefront.Assertions.assertComponentInOverlayPresent(
            Storefront.Constants.COMPONENT_4_ID,
            Storefront.Constants.COMPONENT_4_TYPE,
            true
        );
        expect(await element(by.id(component4DecoratorName)).isPresent()).toBe(
            true,
            'Expected new contextual menu item to be present'
        );

        await Storefront.Assertions.assertComponentInOverlayPresent(
            Storefront.Constants.COMPONENT_3_ID,
            Storefront.Constants.COMPONENT_3_TYPE,
            true
        );
        await browser
            .actions()
            .mouseMove(element(by.id(Storefront.Constants.COMPONENT_3_ID)))
            .perform();
        await browser.click(by.id(component3DecoratorName));

        await Storefront.Assertions.assertComponentInOverlayPresent(
            Storefront.Constants.COMPONENT_4_ID,
            Storefront.Constants.COMPONENT_4_TYPE,
            true
        );
        await browser
            .actions()
            .mouseMove(element(by.id(Storefront.Constants.COMPONENT_4_ID)))
            .perform();
        await browser.waitForAbsence(
            element(by.id(component4DecoratorName)),
            'Expected contextual menu item to be removed'
        );
    });
});
