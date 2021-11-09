/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace InflectionPoint {
    export const Actions = {
        async openInflectionPointMenu(): Promise<void> {
            await browser.click(Elements.getInflectionPointMenuOpenButton());
        }
    };

    export const Assertions = {
        async inflectionPointSelectorIsNotPresent(): Promise<void> {
            await browser.waitForAbsence(
                Elements.getInflectionPointSelector(),
                'Expect inflection point button not to be displayed.'
            );
        }
    };

    export const Elements = {
        getInflectionPointSelector(): ElementFinder {
            return element(by.tagName('inflection-point-selector'));
        },
        getInflectionPointMenuOpenButton(): ElementFinder {
            return element(by.css('inflection-point-selector button'));
        },
        getInflectionPointMenu(): ElementFinder {
            return element(by.css('inflection-point-selector .fd-menu__list'));
        }
    };
}
