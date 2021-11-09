/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

import { ClickThroughOverlayComponentObject } from '../utils/components/ClickThroughOverlayComponentObject';

export namespace SmartEditContractChangeListenerPageObject {
    function forceClickThroughOverlay(_element: ElementFinder) {
        return ClickThroughOverlayComponentObject.Utils.clickThroughOverlay(_element);
    }

    export const Elements = {
        enlargeComponentButton(): ElementFinder {
            return element(by.id('enlargeComponentButton'));
        },
        shrinkComponentButton(): ElementFinder {
            return element(by.id('shrinkComponentButton'));
        },
        moveComponentButton(): ElementFinder {
            return element(by.id('moveComponentButton'));
        },
        removeComponentButton(): ElementFinder {
            return element(by.id('removeComponentButton'));
        },
        toggleComponentTypeButton(): ElementFinder {
            return element(by.id('toggleComponentTypeButton'));
        },
        changePage(): ElementFinder {
            return element(by.id('changePage'));
        },
        removeSlotButton(): ElementFinder {
            return element(by.id('removeSlotButton'));
        },
        addSlotButton(): ElementFinder {
            return element(by.id('addSlotButton'));
        },
        animateComponentButton(): ElementFinder {
            return element(by.id('animateComponentButton'));
        },
        stopAnimateComponentButton(): ElementFinder {
            return element(by.id('stopAnimateComponentButton'));
        },
        pageChangeTest(): ElementFinder {
            return element(by.tagName('page-change-test'));
        },
        async getBoundingClientRect(_element: ElementFinder): Promise<ClientRect> {
            const rect = (await browser.executeScript(
                'return arguments[0].getBoundingClientRect();',
                _element
            )) as ClientRect;

            return rect;
        },
        async getTotalStoreFrontElements(): Promise<string> {
            return await element(by.id('total-store-front-components')).getText();
        },
        async getTotalVisibleStoreFrontElements(): Promise<string> {
            return await element(by.id('total-visible-store-front-components')).getText();
        },
        async getTotalDecorators(): Promise<string> {
            return await element(by.id('total-decorators')).getText();
        },
        async getTotalSakExecutorElements(): Promise<string> {
            return await element(by.id('total-sak-executor-elements')).getText();
        },
        async getTotalResizeListeners(): Promise<string> {
            return await element(by.id('total-resize-listeners')).getText();
        },
        async getTotalRepositionListeners(): Promise<string> {
            return await element(by.id('total-reposition-listeners')).getText();
        },
        createSimpleDivElementButton(): ElementFinder {
            return element(by.id('createSimpleDivElement'));
        },
        convertSimpleDivToComponentButton(): ElementFinder {
            return element(by.id('convertSimpleDivToComponent'));
        },
        convertComponentToSimpleDivButton(): ElementFinder {
            return element(by.id('convertComponentToSimpleDiv'));
        }
    };

    export const Actions = {
        async enlargeComponent(): Promise<void> {
            await forceClickThroughOverlay(Elements.enlargeComponentButton());
        },
        async shrinkComponent(): Promise<void> {
            await forceClickThroughOverlay(Elements.shrinkComponentButton());
        },
        async moveComponent(): Promise<void> {
            await forceClickThroughOverlay(Elements.moveComponentButton());
        },
        async removeComponent(): Promise<void> {
            await forceClickThroughOverlay(Elements.removeComponentButton());
        },
        async toggleComponentType(): Promise<void> {
            await forceClickThroughOverlay(Elements.toggleComponentTypeButton());
        },
        async changePage(): Promise<void> {
            await forceClickThroughOverlay(Elements.changePage());
        },
        async removeSlot(): Promise<void> {
            await forceClickThroughOverlay(Elements.removeSlotButton());
        },
        async addSlot(): Promise<void> {
            await forceClickThroughOverlay(Elements.addSlotButton());
        },
        async animateComponent(): Promise<void> {
            await forceClickThroughOverlay(Elements.animateComponentButton());
        },
        async stopAnimateComponent(): Promise<void> {
            await forceClickThroughOverlay(Elements.stopAnimateComponentButton());
        },
        async createSimpleDivElement(): Promise<void> {
            await forceClickThroughOverlay(Elements.createSimpleDivElementButton());
        },
        async convertSimpleDivToComponent(): Promise<void> {
            await forceClickThroughOverlay(Elements.convertSimpleDivToComponentButton());
        },
        async convertComponentToSimpleDiv(): Promise<void> {
            await forceClickThroughOverlay(Elements.convertComponentToSimpleDivButton());
        }
    };
    export const Assertions = {
        async pageHasChanged(newvalue: string): Promise<void> {
            expect(await Elements.pageChangeTest().getText()).toBe(newvalue);
        },

        async elementsHaveSameDimensions(
            component1: ElementFinder,
            component2: ElementFinder,
            marginOfError?: number
        ): Promise<void> {
            const component1Size = await component1.getSize();
            const component2Size = await component2.getSize();
            const expectedWidth = component1Size.width;
            const expectedHeight = component1Size.height;

            if (marginOfError) {
                expect(Math.abs(component2Size.width - expectedWidth)).toBeLessThan(marginOfError);
                expect(Math.abs(component2Size.height - expectedWidth)).toBeLessThan(marginOfError);
            } else {
                expect(component2Size.width).toBe(expectedWidth);
                expect(component2Size.height).toBe(expectedHeight);
            }
        },
        async elementsHaveSamePosition(
            component1: ElementFinder,
            component2: ElementFinder,
            marginOfError?: number
        ): Promise<void> {
            const component1Position = await Elements.getBoundingClientRect(component1);
            const component2Position = await Elements.getBoundingClientRect(component2);

            const expectedLeft = component1Position.left;
            const expectedTop = component1Position.top;

            if (marginOfError) {
                expect(Math.abs(component2Position.left - expectedLeft)).toBeLessThan(
                    marginOfError
                );
                expect(Math.abs(component2Position.top - expectedTop)).toBeLessThan(marginOfError);
            } else {
                expect(component2Position.left).toBe(expectedLeft);
                expect(component2Position.top).toBe(expectedTop);
            }
        },
        async overlayAndStoreFrontAreSynced(): Promise<void> {
            await browser.switchToIFrame();
            await browser.waitUntil(async () => {
                const item0 = await Elements.getTotalDecorators();
                const item1 = await Elements.getTotalSakExecutorElements();

                return Number(item0) >= Number(item1);
            }, 'Number of SAK executor instances higher than the number of decorators');

            browser.waitUntil(async () => {
                const item0 = await Elements.getTotalStoreFrontElements();
                const item1 = await Elements.getTotalRepositionListeners();

                return Number(item0) >= Number(item1);
            }, 'Number of reposition listeners higher than the number of visible storefront elements');

            browser.waitUntil(async () => {
                const item0 = await Elements.getTotalStoreFrontElements();
                const item1 = await Elements.getTotalResizeListeners();

                return Number(item0) >= Number(item1);
            }, 'Number of resize listeners higher than the number of visible storefront elements');

            await browser.waitForPresence(element(by.cssContainingText('#healthStatus', 'OK')));
        }
    };
}
