/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder } from 'protractor';

export namespace SelectTestPageObject {
    export const Elements = {
        getComponentHost: (hostId: string): ElementFinder => {
            return element(by.id(`${hostId}-host`));
        },
        getSelectorModel: async (hostId: string): Promise<string> => {
            const modelText = await Elements.getComponentHost(hostId)
                .element(by.id('model'))
                .getText();
            return modelText.trim();
        },
        getErrorButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('error'));
        },
        getWarningButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('warning'));
        },
        getResetButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('reset'));
        },
        getChangeSourceButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('source'));
        },
        getClearSourceButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('clear-source'));
        },
        getForceResetCheckbox: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('force-reset'));
        },
        getResetSearchInputCheckbox: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('reset-search-input'));
        },
        getSetResultsHeaderLabelButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('set-results-header-label'));
        },
        getSetResultsHeaderComponentButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('set-results-header-component'));
        },
        getSetActionableSearchItemButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('set-actionable-search-item'));
        },
        getSearchEnabledCheckbox: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('search-enabled'));
        },
        getIsReadOnlyCheckbox: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('isReadOnly'));
        },
        getDisableChoiceFnCheckbox: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('disableChoiceFn'));
        },
        getSetOnChangeButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('set-onChange'));
        },
        getSetOnRemoveButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('set-onRemove'));
        },
        getSetOnSelectButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('set-onSelect'));
        },
        getSetKeyupButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('set-keyup'));
        },
        getOnChangeCounter: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('onChange-counter'));
        },
        getOnRemoveModel: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('onRemove-model'));
        },
        getOnSelectModel: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('onSelect-model'));
        },
        getKeyupSearch: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('keyup-search'));
        },
        getSetFetchPageButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('set-fetchPage'));
        },
        getClearModelButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('clear-model'));
        },
        getChangeModelButton: (hostId: string): ElementFinder => {
            return Elements.getComponentHost(hostId).element(by.id('change-model'));
        }
    };

    export const Actions = {
        clickOutsideSelector: async (): Promise<void> => {
            await browser.click(by.css('body'));
        },
        clickShowErrorButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getErrorButton(hostId));
        },
        clickShowWarningButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getWarningButton(hostId));
        },
        clickResetValidationButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getResetButton(hostId));
        },
        clickChangeSourceButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getChangeSourceButton(hostId));
        },
        clickClearSourceButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getClearSourceButton(hostId));
        },
        clickForceResetCheckbox: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getForceResetCheckbox(hostId));
        },
        clickResetSearchInputCheckbox: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getResetSearchInputCheckbox(hostId));
        },
        clickSetResultsHeaderLabelButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSetResultsHeaderLabelButton(hostId));
        },
        clickSetResultsHeaderComponentButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSetResultsHeaderComponentButton(hostId));
        },
        clickSetActionableSearchItemButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSetActionableSearchItemButton(hostId));
        },
        clickSearchEnabledCheckbox: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSearchEnabledCheckbox(hostId));
        },
        clickIsReadOnlyCheckbox: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getIsReadOnlyCheckbox(hostId));
        },
        clickDisableChoiceFnCheckbox: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getDisableChoiceFnCheckbox(hostId));
        },
        clickSetOnChangeButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSetOnChangeButton(hostId));
        },
        clickSetOnRemoveButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSetOnRemoveButton(hostId));
        },
        clickSetOnSelectButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSetOnSelectButton(hostId));
        },
        clickSetKeyupButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSetKeyupButton(hostId));
        },
        clickSetFetchPage: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSetFetchPageButton(hostId));
        },
        clickClearModelButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getClearModelButton(hostId));
        },
        clickChangeModelButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getChangeModelButton(hostId));
        }
    };

    export const Assertions = {
        selectorModelIsEqualTo: async (hostId: string, expectedText: string): Promise<void> => {
            const actual = await Elements.getSelectorModel(hostId);

            expect(actual).toBe(expectedText);
        },
        onChangeHasBeenCalledTimes: async (
            hostId: string,
            expectedCounter: number
        ): Promise<void> => {
            const onChangeCounter = await Elements.getOnChangeCounter(hostId);
            await browser.waitForSelectorToContainText(onChangeCounter, String(expectedCounter));
        },
        onRemoveModelIsEqualTo: async (hostId: string, expectedText: string): Promise<void> => {
            const actual = await Elements.getOnRemoveModel(hostId).getText();

            expect(actual).toBe(expectedText);
        },
        onSelectModelIsEqualTo: async (hostId: string, expectedText: string): Promise<void> => {
            const actual = await Elements.getOnSelectModel(hostId).getText();

            expect(actual).toBe(expectedText);
        },
        keyupSearchIsEqualTo: async (hostId: string, expectedText: string): Promise<void> => {
            const actual = await Elements.getKeyupSearch(hostId).getText();

            expect(actual).toBe(expectedText);
        }
    };
}
