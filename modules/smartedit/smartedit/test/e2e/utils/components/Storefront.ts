/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder } from 'protractor';

export namespace Storefront {
    export const Actions = {
        async deepLink(): Promise<void> {
            await browser.switchToIFrame();
            await Actions.moveToComponent(Constants.DEEP_LINKS_SLOT_ID, 'ContentSlot');
            await browser.click(Elements.secondPageLink());
            await browser.switchToParent();
            await browser.waitForWholeAppToBeReady();
        },

        async goToThirdPage(): Promise<void> {
            await browser.switchToIFrame();
            await Actions.moveToComponent(Constants.DEEP_LINKS_SLOT_ID, 'ContentSlot');
            await browser.click(Elements.thirdPageLink());
            await browser.switchToParent();
            await browser.waitForWholeAppToBeReady();
        },

        async goToRequireJsPage(): Promise<void> {
            await browser.switchToIFrame();
            await Actions.moveToComponent(Constants.DEEP_LINKS_SLOT_ID, 'ContentSlot');
            await browser.click(Elements.requireJsLink());
            await browser.switchToParent();
            await browser.waitForWholeAppToBeReady();
        },

        async clickRequireJsSuccessButton(): Promise<void> {
            await browser.switchToIFrame();
            await browser.click(Elements.requireJsSuccessButton());
        },

        async moveToComponent(componentId: string, componentType: string): Promise<void> {
            await browser.switchToIFrame();
            await browser
                .actions()
                .mouseMove(await Elements.getOriginalComponentById(componentId, componentType))
                .perform();
        },

        async waitForNonPresenceOfSmartEditOverlay(): Promise<void> {
            await browser.waitForAbsence('#smarteditoverlay');
        }
    };

    const _assertElementContains = async function(e: ElementFinder, content: string) {
        expect(await e.getText()).toContain(content);
    };

    const _assertElementNotContains = async function(e: ElementFinder, content: string) {
        expect(await e.getText()).not.toContain(content);
    };

    const _assertElementPresent = async function(e: ElementFinder, isPresent: boolean) {
        expect(await e.isPresent()).toBe(isPresent);
    };

    const _assertElementDisplayed = async function(e: ElementFinder, isDisplayed: boolean) {
        expect(await e.isDisplayed()).toBe(isDisplayed);
    };

    export const Assertions = {
        async assertStoreFrontIsDisplayed(): Promise<void> {
            expect(await Elements.getBrowserUrl()).toContain('/storefront');
        },

        async assertComponentContains(componentModel: string, content: string): Promise<void> {
            await _assertElementContains(
                await Elements.getComponentByModel(componentModel),
                content
            );
        },

        async assertComponentHtmlContains(componentHtmlId: string, content: string): Promise<void> {
            expect(await _getComponentInnerHtml(componentHtmlId)).toContain(content);
        },

        async assertComponentInOverlayPresent(
            componentId: string,
            componentType: string,
            isPresent: boolean
        ): Promise<void> {
            if (isPresent) {
                await Actions.moveToComponent(componentId, componentType);
            }
            await _assertElementPresent(
                await Elements.getComponentInOverlayById(componentId, componentType),
                isPresent
            );
        },

        async assertComponentInOverlayContains(
            componentId: string,
            componentType: string,
            content: string
        ): Promise<void> {
            await Actions.moveToComponent(componentId, componentType);
            await _assertElementContains(
                await Elements.getComponentInOverlayById(componentId, componentType),
                content
            );
        },

        async assertComponentInOverlayNotContains(
            componentId: string,
            componentType: string,
            content: string
        ): Promise<void> {
            await Actions.moveToComponent(componentId, componentType);
            await _assertElementNotContains(
                await Elements.getComponentInOverlayById(componentId, componentType),
                content
            );
        },

        async assertSmartEditOverlayDisplayed(isDisplayed: boolean): Promise<void> {
            await _assertElementDisplayed(await Elements.getSmartEditOverlay(), isDisplayed);
        },

        async assertSmartEditOverlayPresent(isPresent: boolean): Promise<void> {
            await _assertElementPresent(await Elements.getSmartEditOverlay(), isPresent);
        },

        async assertDecoratorShowsOnComponent(
            componentId: string,
            componentType: string,
            decoratorClasses: string | string[]
        ): Promise<void> {
            decoratorClasses =
                typeof decoratorClasses === 'string' ? [decoratorClasses] : decoratorClasses;
            await Actions.moveToComponent(componentId, componentType);

            for (const decoratorClass of decoratorClasses) {
                await browser.waitToBeDisplayed(
                    by.css(_buildDecoratorSelector(componentId, componentType, decoratorClass)),
                    'could not find decorator ' + decoratorClass
                );
            }
        },
        async assertDecoratorDoesntShowOnComponent(
            componentId: string,
            componentType: string,
            decoratorClasses: string | string[]
        ): Promise<void> {
            decoratorClasses =
                typeof decoratorClasses === 'string' ? [decoratorClasses] : decoratorClasses;

            for (const decoratorClass of decoratorClasses) {
                await browser.waitForAbsence(
                    by.css(_buildDecoratorSelector(componentId, componentType, decoratorClass)),
                    'should not have found decorator ' + decoratorClass
                );
            }
        }
    };

    export const Constants = {
        COMPONENT_1_ID: 'component1',
        COMPONENT_2_ID: 'component2',
        COMPONENT_3_ID: 'component3',
        COMPONENT_4_ID: 'component4',

        COMPONENT_1_TYPE: 'componentType1',
        COMPONENT_2_TYPE: 'componentType2',
        COMPONENT_3_TYPE: 'SimpleResponsiveBannerComponent',
        COMPONENT_4_TYPE: 'componentType4',

        TOP_HEADER_SLOT_ID: 'topHeaderSlot',
        BOTTOM_HEADER_SLOT_ID: 'bottomHeaderSlot',
        SEARCH_BOX_SLOT: 'searchBoxSlot',
        FOOTER_SLOT_ID: 'footerSlot',
        OTHER_SLOT_ID: 'otherSlot',
        COMPONENT1_NAME: 'component1',
        COMPONENT4_NAME: 'component4',
        STATIC_SLOT_ID: 'staticDummySlot',
        STATIC_COMPONENT_NAME: 'staticDummyComponent',
        DEEP_LINKS_SLOT_ID: 'deepLinksSlot'
    };

    const _buildComponentSelector = (
        componentId: string,
        componentType?: string,
        inOverlay?: boolean
    ) => {
        let selector = '.smartEditComponent';

        if (inOverlay) {
            selector = '#smarteditoverlay ' + selector + 'X';
        }

        selector += '[data-smartedit-component-id="' + componentId + '"]';

        if (componentType) {
            selector += '[data-smartedit-component-type="' + componentType + '"]';
        }

        return selector;
    };

    const _buildDecoratorSelector = (
        componentId: string,
        componentType: string,
        decoratorClass: string
    ) => {
        return _buildComponentSelector(componentId, componentType, true) + ' .' + decoratorClass;
    };

    const _getComponentInnerHtml = async (componentHtmlId: string) => {
        await browser.switchToIFrame();
        return element(by.css('#' + componentHtmlId + ' div')).getText();
    };

    export const Elements = {
        async getBrowserUrl(): Promise<string> {
            const url = await browser.getCurrentUrl();

            return url;
        },

        componentButton(): ElementFinder {
            return element(by.css('#submitButton'));
        },

        secondComponentButton(): ElementFinder {
            return element(by.id('secondaryButton'));
        },

        component1(): Promise<ElementFinder> {
            return Elements.getComponentById(Constants.COMPONENT_1_ID);
        },

        component2(): Promise<ElementFinder> {
            return Elements.getComponentById(Constants.COMPONENT_2_ID);
        },

        component3(): Promise<ElementFinder> {
            return Elements.getComponentById(Constants.COMPONENT_3_ID);
        },

        secondPageLink(): ElementFinder {
            return element(by.id('deepLink'));
        },

        thirdPageLink(): ElementFinder {
            return element(by.id('thirdPage'));
        },

        requireJsLink(): ElementFinder {
            return element(by.id('deepLinkRequireJs'));
        },

        requireJsSuccessButton(): ElementFinder {
            return element(by.id('requirejs-success-button'));
        },

        addToCartButton(): ElementFinder {
            return element(by.id('addToCart'));
        },

        addToCartFeedback(): ElementFinder {
            return element(by.id('feedback'));
        },

        secondPage: {
            component2(): ElementFinder {
                return element(by.css('#component2 div'));
            }
        },

        async getComponentById(componentId: string): Promise<ElementFinder> {
            await browser.switchToIFrame();
            return element(by.css(_buildComponentSelector(componentId)));
        },

        async getComponentByModel(componentModel: string): Promise<ElementFinder> {
            await browser.switchToIFrame();
            return element(by.model(componentModel));
        },

        async getComponentInOverlayById(
            componentId: string,
            componentType?: string
        ): Promise<ElementFinder> {
            await browser.switchToIFrame();
            return element(by.css(_buildComponentSelector(componentId, componentType, true)));
        },

        async getOriginalComponentById(
            componentId: string,
            componentType: string
        ): Promise<ElementFinder> {
            await browser.switchToIFrame();
            return element(by.css(_buildComponentSelector(componentId, componentType, false)));
        },

        async getSmartEditOverlay(): Promise<ElementFinder> {
            await browser.switchToIFrame();
            return element(by.id('smarteditoverlay'));
        }
    };
}
