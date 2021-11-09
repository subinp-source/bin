/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder } from 'protractor';

export namespace Perspectives {
    const OVERLAY_SELECTOR = '#smarteditoverlay';

    export const Constants = {
        DEFAULT_PERSPECTIVES: {
            ALL: 'All',
            NONE: 'Preview'
        }
    };

    export const Elements = {
        getTooltipIcon(): ElementFinder {
            return element(by.id('perspectiveTooltip'));
        },

        getPerspectiveDropdownToggle(): ElementFinder {
            return element(by.css('.se-perspective-selector__btn'));
        },

        getPerspectiveDropdownMenu(): ElementFinder {
            return element(by.css('.se-perspective__list'));
        },

        getPerspectiveDropdownOption(perspectiveName: string): ElementFinder {
            return element(
                by.cssContainingText('.se-perspective__list-item-text', perspectiveName)
            );
        },

        async getActivePerspectiveName(): Promise<string> {
            await browser.switchToParent();

            return element(by.css('.se-perspective-selector__btn')).getText();
        },

        deprecated_getElementInOverlay(componentID: string, componentType: string): ElementFinder {
            let selector =
                '#smarteditoverlay .smartEditComponentX[data-smartedit-component-id="' +
                componentID +
                '"]';

            if (componentType) {
                selector += '[data-smartedit-component-type="' + componentType + '"]';
            }

            return element(by.css(selector));
        }
    };

    export const Actions = {
        async openAndBeReady(): Promise<void> {
            await browser.get('test/e2e/perspectiveService/index.html');
            await browser.waitForContainerToBeReady();
            await browser.switchToIFrame();
        },

        async refreshAndWaitForAngularEnabled(): Promise<void> {
            await browser.get('test/e2e/perspectiveService/index.html');
            await browser.waitForAngularEnabled(false);
        },

        async openPerspectiveSelectorDropdown(): Promise<void> {
            await browser.switchToParent();
            await browser.click(Elements.getPerspectiveDropdownToggle());
        },

        async selectPerspective(perspectiveName: string): Promise<void | boolean> {
            await browser.switchToParent();
            const perspectiveSelected = await Elements.getActivePerspectiveName();

            if (perspectiveSelected.toUpperCase() !== perspectiveName.toUpperCase()) {
                await Actions.openPerspectiveSelectorDropdown();
                await browser.click(
                    Elements.getPerspectiveDropdownOption(perspectiveName),
                    'perspective ' + perspectiveName + ' is not clickable'
                );

                await browser.waitForContainerToBeReady();

                await browser.switchToIFrame();

                if (perspectiveName === Constants.DEFAULT_PERSPECTIVES.NONE) {
                    return true;
                } else {
                    await browser.waitForVisibility(OVERLAY_SELECTOR);
                }
            } else {
                await browser.waitForWholeAppToBeReady();
                await browser.switchToIFrame();

                if (perspectiveName === Constants.DEFAULT_PERSPECTIVES.NONE) {
                    return true;
                } else {
                    await browser.waitForVisibility(OVERLAY_SELECTOR);
                }
            }

            await browser.switchToParent();
        }
    };

    export const Assertions = {
        async assertPerspectiveActive(): Promise<void> {
            const name = await Elements.getActivePerspectiveName();

            expect(name).toBeDefined();
        },

        async assertPerspectiveSelectorDropdownDisplayed(isDisplayed: boolean): Promise<void> {
            const isPerspectiveDisplayed = await Elements.getPerspectiveDropdownMenu().isDisplayed();

            expect(isPerspectiveDisplayed).toBe(isDisplayed);
        },

        async assertPerspectiveSelectorDropdownIsAbsent(): Promise<void> {
            await browser.waitForAbsence(Elements.getPerspectiveDropdownMenu());
        },

        async assertSmarteditOverlayIsPresent(): Promise<void> {
            expect(await browser.waitToBeDisplayed(by.css(OVERLAY_SELECTOR))).toBe(true);
        },

        async assertSmarteditOverlayIsAbsent(): Promise<void> {
            expect(await browser.isAbsent(by.id('perspectiveTooltip'))).toBe(true);
        },

        async assertPerspectiveSelectorButtonIsDisabled(): Promise<void> {
            await browser.waitForPresence(Elements.getPerspectiveDropdownToggle());
            expect(Elements.getPerspectiveDropdownToggle().isEnabled()).toBeFalsy();
        },

        async assertPerspectiveSelectorToolTipIsPresent(): Promise<void> {
            await browser.waitForPresence(Elements.getTooltipIcon());
        }
    };
}
