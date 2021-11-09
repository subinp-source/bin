/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    browser,
    by,
    element,
    ElementArrayFinder,
    ElementFinder,
    Key,
    WebElement
} from 'protractor';

export namespace SelectComponentObject {
    export const Constants = {
        VALIDATION_MESSAGE_TYPE: {
            VALIDATION_ERROR: 'has-error',
            WARNING: 'has-warning'
        }
    };

    export const Elements = {
        getComponent: (hostId: string): ElementFinder => {
            return element(by.id(hostId));
        },
        getDropdown: (hostId: string): ElementFinder => {
            return Elements.getComponent(hostId).element(by.css('.fd-menu'));
        },
        getSelectorContainer: (hostId: string): ElementFinder => {
            return Elements.getComponent(hostId).element(by.id(hostId + '-selector'));
        },
        getSelectorContainerByClassName: (hostId: string, className: string): ElementFinder => {
            return Elements.getComponent(hostId).element(
                by.css(`#${hostId}-selector.${className}`)
            );
        },
        getSingleDropdownToggle: (hostId: string): ElementFinder => {
            return Elements.getComponent(hostId).element(by.css('.toggle-button'));
        },
        getMultiDropdownToggle: (hostId: string): ElementFinder => {
            return Elements.getComponent(hostId).element(by.css('.search__input'));
        },
        getOptionByText: (hostId: string, text: string): ElementFinder => {
            return Elements.getComponent(hostId).element(
                by.cssContainingText('.menu-option', text)
            );
        },
        getSelectedOptionByText: (hostId: string, text: string): ElementFinder => {
            return Elements.getComponent(hostId).element(
                by.cssContainingText('.selected-item', text)
            );
        },
        getAllOptions: (hostId: string): ElementArrayFinder => {
            return Elements.getComponent(hostId).all(by.css('.se-select-list__item'));
        },
        getAllSelectedOptions: (hostId: string): ElementArrayFinder => {
            return Elements.getComponent(hostId).all(
                by.css('.selected-container .se-item-printer')
            );
        },
        getOptionByIndex: (hostId: string, index: number): ElementFinder => {
            return Elements.getAllOptions(hostId).get(index);
        },
        getSelectedOptionByIndex: (hostId: string, index: number): ElementFinder => {
            return Elements.getAllSelectedOptions(hostId).get(index);
        },
        getRemoveSelectedOptionButtonByText: (hostId: string, text: string): ElementFinder => {
            return Elements.getSelectedOptionByText(hostId, text).element(
                by.css('.selected-item__remove-button')
            );
        },
        getSelectedPlaceholder: (hostId: string): ElementFinder => {
            return Elements.getComponent(hostId).element(by.css('.selected-placeholder'));
        },
        getSearchInput: (hostId: string): ElementFinder => {
            return Elements.getComponent(hostId).element(by.css('.search__input'));
        },
        getSearchInputPlaceholderByText: (hostId: string, text: string): ElementFinder => {
            return Elements.getComponent(hostId).element(
                by.css(`.search__input[placeholder="${text}"]`)
            );
        },
        getResultsHeader: (hostId: string): ElementFinder => {
            return Elements.getComponent(hostId).element(by.css('.results-header'));
        },
        getActionableSearchItemActionButton: (hostId: string): ElementFinder => {
            return Elements.getComponent(hostId).element(
                by.css('.se-actionable-search-item__action-btn')
            );
        },
        getScrollContainer: (hostId: string): ElementFinder => {
            return Elements.getComponent(hostId).element(
                by.css('.se-infinite-scrolling__container')
            );
        }
    };

    export const Actions = {
        toggleSingleSelector: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getSingleDropdownToggle(hostId));
            await Assertions.dropdownIsOpened(hostId);
        },
        toggleMultiSelector: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getMultiDropdownToggle(hostId));
            await Assertions.dropdownIsOpened(hostId);
        },
        selectOptionByText: async (hostId: string, text: string): Promise<void> => {
            await browser.click(Elements.getOptionByText(hostId, text));
        },
        removeSelectedOptionByText: async (hostId: string, text: string): Promise<void> => {
            await browser.click(Elements.getRemoveSelectedOptionButtonByText(hostId, text));
        },
        clickActionableSearchItemActionButton: async (hostId: string): Promise<void> => {
            await browser.click(Elements.getActionableSearchItemActionButton(hostId));
        },
        setSearchInputValue: async (hostId: string, text: string): Promise<void> => {
            await browser.sendKeys(Elements.getSearchInput(hostId), text);
        },
        scrollToBottom: async (hostId: string): Promise<void> => {
            const dropdown = await Elements.getScrollContainer(hostId);
            await browser.scrollToBottom(dropdown);
        },
        arrowUp: async (): Promise<void> => {
            await browser
                .actions()
                .sendKeys(Key.ARROW_UP)
                .perform();
        },
        arrowDown: async (): Promise<void> => {
            await browser
                .actions()
                .sendKeys(Key.ARROW_DOWN)
                .perform();
        },
        enter: async (): Promise<void> => {
            await browser
                .actions()
                .sendKeys(Key.ENTER)
                .perform();
        },
        esc: async (): Promise<void> => {
            await browser
                .actions()
                .sendKeys(Key.ESCAPE)
                .perform();
        }
    };

    export const Assertions = {
        selectorHasValidationType: async (
            hostId: string,
            validationMessageType: string
        ): Promise<void> => {
            await browser.waitForPresence(
                Elements.getSelectorContainerByClassName(hostId, validationMessageType)
            );
        },
        selectorHasNoValidationType: async (hostId: string): Promise<void> => {
            const classNames = await Elements.getSelectorContainer(hostId).getAttribute('class');
            const classRegexp = new RegExp(
                '(' +
                    SelectComponentObject.Constants.VALIDATION_MESSAGE_TYPE.VALIDATION_ERROR +
                    '|' +
                    SelectComponentObject.Constants.VALIDATION_MESSAGE_TYPE.WARNING +
                    ')',
                'g'
            );

            expect(!classNames.match(classRegexp)).toBeTruthy();
        },
        multiSelectorHasSelectedOptionsEqualTo: async (
            hostId: string,
            expectedOptions: string[]
        ): Promise<void> => {
            const multiSelectors = await Elements.getAllSelectedOptions(hostId).getWebElements();

            await Assertions.webElementsHasExpectedOptions(multiSelectors, expectedOptions);
        },
        webElementsHasExpectedOptions: async (
            elements: WebElement[],
            expectedOptions: string[]
        ): Promise<void> => {
            const whenOptions = elements.map(async (ele) => await ele.getText());
            const options = await Promise.all(whenOptions);

            expect(options).toEqual(expectedOptions);
        },
        optionContainsTextByIndex: async (
            hostId: string,
            expectedText: string,
            index: number
        ): Promise<void> => {
            const whenOption = Elements.getOptionByIndex(hostId, index);
            await browser.waitForPresence(whenOption);

            const actual = await whenOption.getText();

            expect(actual).toContain(expectedText);
        },
        selectedOptionContainsTextByIndex: async (
            hostId: string,
            expectedText: string,
            index: number
        ): Promise<void> => {
            const whenOption = Elements.getSelectedOptionByIndex(hostId, index);
            await browser.waitForPresence(whenOption);

            const actual = await whenOption.getText();

            expect(actual).toContain(expectedText);
        },
        dropdownIsEmpty: async (hostId: string): Promise<void> => {
            const count = await Elements.getAllOptions(hostId).count();

            expect(count).toBe(0);
        },
        dropdownIsClosed: async (hostId: string): Promise<void> => {
            await browser.waitForAbsence(Elements.getDropdown(hostId));
        },
        dropdownIsOpened: async (hostId: string): Promise<void> => {
            await browser.waitForPresence(Elements.getDropdown(hostId));
        },
        selectedPlaceholderIsDisplayed: async (hostId: string): Promise<void> => {
            await browser.waitForPresence(Elements.getSelectedPlaceholder(hostId));
        },
        resultsHeaderContainsText: async (hostId: string, expectedText: string): Promise<void> => {
            await browser.waitForSelectorToContainText(
                Elements.getResultsHeader(hostId),
                expectedText
            );
        },
        searchInputPlaceholderIsEqualToText: async (
            hostId: string,
            expectedText: string
        ): Promise<void> => {
            await browser.waitForPresence(
                Elements.getSearchInputPlaceholderByText(hostId, expectedText)
            );
        },
        searchInputIsPresent: async (hostId: string): Promise<void> => {
            await browser.waitForPresence(Elements.getSearchInput(hostId));
        },
        searchInputIsNotPresent: async (hostId: string): Promise<void> => {
            await browser.waitForAbsence(Elements.getSearchInput(hostId));
        },
        actionableSearchItemActionButtonIsDisplayed: async (hostId: string): Promise<void> => {
            await browser.waitForPresence(Elements.getActionableSearchItemActionButton(hostId));
        },
        dropdownNotClickable: async (hostId: string): Promise<void> => {
            await browser.elementNotClickable(Elements.getSingleDropdownToggle(hostId));
        },
        searchInputNotClickable: async (hostId: string): Promise<void> => {
            await browser.elementNotClickable(Elements.getSearchInput(hostId));
        },
        selectOptionIsSelectedByText: async (
            hostId: string,
            expectedText: string
        ): Promise<void> => {
            const menuOption = Elements.getOptionByText(hostId, expectedText);
            const listItem = menuOption.element(by.xpath('..'));

            await browser.waitForPresence(menuOption);

            expect(await listItem.getAttribute('class')).toContain('is-selected');
        },
        searchInputValueIsEqualTo: async (hostId: string, expectedText: string): Promise<void> => {
            const whenInput = Elements.getSearchInput(hostId);
            await browser.waitForPresence(whenInput);

            const actual = await whenInput.getAttribute('value');
            expect(actual).toBe(expectedText);
        },
        selectedOptionNotClickableByIndex: async (hostId: string, index: number): Promise<void> => {
            await browser.elementNotClickable(Elements.getSelectedOptionByIndex(hostId, index));
        },
        optionNotInDropdownByText: async (hostId: string, text: string): Promise<void> => {
            await browser.waitForAbsence(Elements.getOptionByText(hostId, text));
        },
        optionIsActiveByIndex: async (hostId: string, index: number): Promise<void> => {
            const whenElement = Elements.getOptionByIndex(hostId, index);
            await browser.waitForPresence(whenElement, `failed to find option by index: ${index}`);

            const className = await whenElement.getAttribute('class');
            expect(className).toContain('is-active');
        }
    };
}
