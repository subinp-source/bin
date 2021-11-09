/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

import { SelectTestPageObject as PageObject } from './SelectTestPageObject';
import { SelectComponentObject as Select } from '../utils/components/SelectComponentObject';

describe('Multi Select -', () => {
    const multiSelect1 = 'exampleMulti1';
    const multiSelect1App = 'exampleMulti1';
    const multiSelect2 = 'exampleMulti2';

    beforeEach(async () => {
        await browser.get('test/e2e/select/index.html');
        await browser.waitForContainerToBeReady();
    });

    it('GIVEN a multi selector WHEN selecting a product in the dropdown list THEN the dropdown selection list should be updated', async () => {
        await Select.Actions.toggleMultiSelector(multiSelect1);
        await Select.Actions.selectOptionByText(multiSelect1, 'Test Product 1#');

        await Select.Assertions.multiSelectorHasSelectedOptionsEqualTo(multiSelect1, [
            'Test Product 2#',
            'Test Product 3#',
            'Test Product 1#'
        ]);
    });

    it(
        'GIVEN a selected option that does not exist in another set of options and "Force Reset" as checked ' +
            'WHEN switching to another set of options THEN the multi selected model should reset',
        async () => {
            await PageObject.Actions.clickChangeSourceButton(multiSelect1App);

            await PageObject.Assertions.selectorModelIsEqualTo(multiSelect1App, '[ "product3" ]');
        }
    );

    it(
        'GIVEN the initial options and "Force Reset" as unchecked WHEN selecting a product that does not exist in another set ' +
            'THEN the selected model should not reset',
        async () => {
            await PageObject.Actions.clickForceResetCheckbox(multiSelect1App);

            await PageObject.Actions.clickChangeSourceButton(multiSelect1App);

            await PageObject.Assertions.selectorModelIsEqualTo(
                multiSelect1App,
                '[ "product2", "product3" ]'
            );
        }
    );

    it('GIVEN an validation state THEN the selector should display ERROR, WARNING and NO STATE', async () => {
        await PageObject.Actions.clickShowErrorButton(multiSelect1App);
        await Select.Assertions.selectorHasValidationType(
            multiSelect1,
            Select.Constants.VALIDATION_MESSAGE_TYPE.VALIDATION_ERROR
        );

        await PageObject.Actions.clickShowWarningButton(multiSelect1App);
        await Select.Assertions.selectorHasValidationType(
            multiSelect1,
            Select.Constants.VALIDATION_MESSAGE_TYPE.WARNING
        );

        await PageObject.Actions.clickResetValidationButton(multiSelect1App);
        await Select.Assertions.selectorHasNoValidationType(multiSelect1);
    });

    // --- new specs
    it(
        'GIVEN a selected option that does exist in another set of options and "Force Reset" is checked ' +
            'WHEN switching to another set of options THEN the selected model should preserve that option',
        async () => {
            await PageObject.Actions.clickChangeSourceButton(multiSelect1App);

            await PageObject.Assertions.selectorModelIsEqualTo(multiSelect1App, '[ "product3" ]');
        }
    );

    it('GIVEN a custom item component THEN options and selected options should be displayed with that component', async () => {
        await Select.Assertions.selectedOptionContainsTextByIndex(
            multiSelect2,
            'Test Product 1#',
            0
        );
        await Select.Assertions.selectedOptionContainsTextByIndex(
            multiSelect2,
            'Test Product 2#',
            1
        );

        await Select.Actions.toggleMultiSelector(multiSelect2);

        await Select.Assertions.optionContainsTextByIndex(multiSelect2, 'Test Product 3#', 0);
    });

    it('GIVEN a Multi Selector WHEN selecting an option THEN that option should no longer be in dropdown list', async () => {
        await Select.Actions.toggleMultiSelector(multiSelect1);
        await Select.Actions.selectOptionByText(multiSelect1, 'Test Product 1#');
        await Select.Assertions.selectedOptionContainsTextByIndex(
            multiSelect1,
            'Test Product 1#',
            2
        );

        await Select.Actions.toggleMultiSelector(multiSelect1);
        await Select.Assertions.optionNotInDropdownByText(multiSelect1, 'Test Product 1#');
    });

    it('GIVEN selected options WHEN removing a selected option THEN it should not be displayed among selected options BUT displayed in dropdown list', async () => {
        await Select.Actions.removeSelectedOptionByText(multiSelect1, 'Test Product 2#');

        await Select.Actions.toggleMultiSelector(multiSelect1);

        await Select.Assertions.optionContainsTextByIndex(multiSelect1, 'Test Product 2#', 1);
    });

    it('GIVEN placeholder THEN the placeholder should be set for Search Input', async () => {
        await Select.Assertions.searchInputPlaceholderIsEqualToText(
            multiSelect1,
            'Select an Option'
        );
    });

    describe('Results Header', () => {
        it('GIVEN "resultsHeaderLabel" WHEN opening a dropdown THEN it should be displayed with given text', async () => {
            await PageObject.Actions.clickSetResultsHeaderLabelButton(multiSelect1App);

            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Assertions.resultsHeaderContainsText(multiSelect1, 'Results Header Label');
        });

        it('GIVEN "resultsHeaderComponent" WHEN opening a dropdown THEN it should be displayed', async () => {
            await PageObject.Actions.clickSetResultsHeaderComponentButton(multiSelect1App);

            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Assertions.resultsHeaderContainsText(
                multiSelect1,
                'Results Header Custom Component'
            );
        });
    });

    describe('Actionable Search Item', () => {
        beforeEach(async () => {
            // Because of mocked data, results are not filtered when the value is entered
            // so we clear the items to display Actionable Search Item.
            await PageObject.Actions.clickClearSourceButton(multiSelect1App);
            await PageObject.Actions.clickSetActionableSearchItemButton(multiSelect1App);
        });

        it(
            'GIVEN "actionableSearchItem" WHEN opening a dropdown AND providing search input value ' +
                'for which items cannot be found THEN it should be displayed',
            async () => {
                await Select.Actions.toggleMultiSelector(multiSelect1);
                await Select.Actions.setSearchInputValue(
                    multiSelect1,
                    'item label I would like to add'
                );

                await Select.Assertions.actionableSearchItemActionButtonIsDisplayed(multiSelect1);
            }
        );

        it(
            'GIVEN "actionableSearchItem" AND providing search input value WHEN clicking on action button ' +
                'THEN the dropdown should be closed',
            async () => {
                await Select.Actions.toggleMultiSelector(multiSelect1);
                await Select.Actions.setSearchInputValue(
                    multiSelect1,
                    'item label I would like to add'
                );
                await Select.Actions.clickActionableSearchItemActionButton(multiSelect1);

                await Select.Assertions.dropdownIsClosed(multiSelect1);
            }
        );
    });

    describe('Search Input', () => {
        it('GIVEN "searchEnabled" is false WHEN user attempts to provide a value for Search Input THEN it should be not allowed', async () => {
            await PageObject.Actions.clickSearchEnabledCheckbox(multiSelect1App);

            await Select.Actions.setSearchInputValue(multiSelect1, 'abc');

            await Select.Assertions.searchInputValueIsEqualTo(multiSelect1, '');
        });

        it('GIVEN "searchEnabled" is true WHEN user attemps to provide a value for Search Input THEN it should be allowed', async () => {
            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Assertions.searchInputIsPresent(multiSelect1);
        });

        it(
            'GIVEN "resetSearchInput" is true WHEN providing the search input AND closing the dropdown AND opening the dropdown again ' +
                'THEN the value should be empty',
            async () => {
                await PageObject.Actions.clickResetSearchInputCheckbox(multiSelect1App);

                await Select.Actions.toggleMultiSelector(multiSelect1);
                await Select.Actions.setSearchInputValue(multiSelect1, 'abc');

                await PageObject.Actions.clickOutsideSelector();

                await Select.Assertions.searchInputValueIsEqualTo(multiSelect1, '');
            }
        );

        it(
            'GIVEN "resetSearchInput" is false WHEN providing the search input AND closing the dropdown AND opening the dropdown again ' +
                'THEN the value should persist',
            async () => {
                await PageObject.Actions.clickResetSearchInputCheckbox(multiSelect1App);
                await PageObject.Actions.clickResetSearchInputCheckbox(multiSelect1App);

                await Select.Actions.toggleMultiSelector(multiSelect1);
                await Select.Actions.setSearchInputValue(multiSelect1, 'abc');
                await PageObject.Actions.clickOutsideSelector();

                await Select.Assertions.searchInputValueIsEqualTo(multiSelect1, 'abc');
            }
        );

        it(
            'GIVEN "resetSearchInput" is false WHEN providing the search input AND selecting an option AND opening the dropdown again ' +
                'THEN the value should persist',
            async () => {
                await PageObject.Actions.clickResetSearchInputCheckbox(multiSelect1App);
                await PageObject.Actions.clickResetSearchInputCheckbox(multiSelect1App);

                await Select.Actions.toggleMultiSelector(multiSelect1);
                await Select.Actions.setSearchInputValue(multiSelect1, 'Test');
                await Select.Actions.selectOptionByText(multiSelect1, 'Test Product 4#');

                await Select.Assertions.searchInputValueIsEqualTo(multiSelect1, 'Test');
            }
        );
    });

    describe('isReadOnly', () => {
        it(`GIVEN "isReadOnly" is true THEN search input should be clickable`, async () => {
            await PageObject.Actions.clickIsReadOnlyCheckbox(multiSelect1App);

            await Select.Assertions.searchInputNotClickable(multiSelect1);
        });

        it(`GIVEN "isReadOnly" is true AND selected item THEN selected item shouldn'e be clickable`, async () => {
            await PageObject.Actions.clickIsReadOnlyCheckbox(multiSelect1App);

            await Select.Assertions.selectedOptionNotClickableByIndex(multiSelect1, 0);
        });
    });

    it(`GIVEN "disableChoiceFn" is defined WHEN opening a dropdown AND clicking on disabled option THEN it should not close the dropdown`, async () => {
        await PageObject.Actions.clickDisableChoiceFnCheckbox(multiSelect1App);

        await Select.Actions.toggleMultiSelector(multiSelect1);
        await Select.Actions.selectOptionByText(multiSelect1, 'Test Product 1#');

        await Select.Assertions.dropdownIsOpened(multiSelect1);
    });

    describe('Keyboard navigation', () => {
        it('GIVEN default settings WHEN opening a dropdown THEN first option should be active', async () => {
            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Assertions.optionIsActiveByIndex(multiSelect1, 0);
        });

        it('GIVEN default settings WHEN opening a dropdown AND pressing arrow up / arrow down key THEN a particular option should be active', async () => {
            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Actions.arrowDown();
            await Select.Assertions.optionIsActiveByIndex(multiSelect1, 1);

            await Select.Actions.arrowDown();
            await Select.Assertions.optionIsActiveByIndex(multiSelect1, 2);

            await Select.Actions.arrowUp();
            await Select.Assertions.optionIsActiveByIndex(multiSelect1, 1);

            await Select.Actions.arrowUp();
            await Select.Assertions.optionIsActiveByIndex(multiSelect1, 0);
        });

        it(
            'GIVEN "disableChoiceFn" is defined WHEN opening a dropdown AND pressing arrow up / arrow down keys ' +
                'THEN disabled options should not be active',
            async () => {
                await PageObject.Actions.clickDisableChoiceFnCheckbox(multiSelect1App);
                await Select.Actions.toggleMultiSelector(multiSelect1);

                await Select.Assertions.optionIsActiveByIndex(multiSelect1, 1);

                await Select.Actions.arrowDown();
                await Select.Assertions.optionIsActiveByIndex(multiSelect1, 2);

                await Select.Actions.arrowUp();
                await Select.Assertions.optionIsActiveByIndex(multiSelect1, 1);

                await Select.Actions.arrowUp();
                await Select.Assertions.optionIsActiveByIndex(multiSelect1, 1);
            }
        );

        it('GIVEN default settings WHEN opening a dropdown AND pressing enter key THEN highlighted option should be selected', async () => {
            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Actions.arrowDown();
            await Select.Actions.enter();

            await Select.Assertions.selectedOptionContainsTextByIndex(
                multiSelect1,
                'Test Product 4#',
                2
            );
        });

        it('GIVEN default settings WHEN opening a dropdown AND pressing esc key THEN dropdown should be closed', async () => {
            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Actions.enter();

            await Select.Assertions.dropdownIsClosed(multiSelect1);
        });
    });

    describe('callbacks', () => {
        describe('onChange', () => {
            beforeEach(async () => {
                await PageObject.Actions.clickSetOnChangeButton(multiSelect1App);
            });

            it('GIVEN "onChange" WHEN displaying component THEN "onChange" function should be called', async () => {
                await PageObject.Assertions.onChangeHasBeenCalledTimes(multiSelect1App, 1);
            });

            it('GIVEN "onChange" WHEN removing a selected option THEN "onChange" function should be called', async () => {
                await Select.Actions.removeSelectedOptionByText(multiSelect1, 'Test Product 2#');

                await PageObject.Assertions.onChangeHasBeenCalledTimes(multiSelect1App, 2);
            });

            it('GIVEN "onChange" WHEN selecting an option THEN "onChange" function should be called', async () => {
                await Select.Actions.toggleMultiSelector(multiSelect1);

                await Select.Actions.selectOptionByText(multiSelect1, 'Test Product 1#');

                await PageObject.Assertions.onChangeHasBeenCalledTimes(multiSelect1App, 2);
            });

            it('GIVEN "onChange" WHEN Parent Component changes the model THEN "onChange" function should be called', async () => {
                await PageObject.Actions.clickChangeModelButton(multiSelect1App);

                await PageObject.Assertions.onChangeHasBeenCalledTimes(multiSelect1App, 2);
            });
        });

        it('GIVEN "onRemove" WHEN removing a selected option THEN the "onRemove" function should be called', async () => {
            await PageObject.Actions.clickSetOnRemoveButton(multiSelect1App);

            await Select.Actions.removeSelectedOptionByText(multiSelect1, 'Test Product 2#');

            await PageObject.Assertions.onRemoveModelIsEqualTo(multiSelect1App, 'product2');
        });

        it('GIVEN "onSelect" WHEN selecting an option THEN the "onSelect" function should be called', async () => {
            await PageObject.Actions.clickSetOnSelectButton(multiSelect1App);

            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Actions.selectOptionByText(multiSelect1, 'Test Product 1#');

            await PageObject.Assertions.onSelectModelIsEqualTo(multiSelect1App, 'product1');
        });

        it('GIVEN "keyup" WHEN providing search input AND releasing a key (keyup) THEN the "keyup" function should be called', async () => {
            await PageObject.Actions.clickSetKeyupButton(multiSelect1App);

            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Actions.setSearchInputValue(multiSelect1, 'a');

            await PageObject.Assertions.keyupSearchIsEqualTo(multiSelect1App, 'a');

            await Select.Actions.setSearchInputValue(multiSelect1, 'b');

            await PageObject.Assertions.keyupSearchIsEqualTo(multiSelect1App, 'ab');
        });
    });

    describe('fetchPage', () => {
        beforeEach(async () => {
            // Clear the "model" because it is not required for the following specs.
            // If "model" is provided, SelectComponent validation will throw an error because no "fetchEntities" is provided.
            await PageObject.Actions.clickClearModelButton(multiSelect1App);
        });

        it('GIVEN "fetchPage" WHEN opening a dropdown THEN items should be displayed', async () => {
            await PageObject.Actions.clickSetFetchPage(multiSelect1App);

            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Assertions.optionContainsTextByIndex(multiSelect1, 'Test Product 1#', 0);
            await Select.Assertions.optionContainsTextByIndex(multiSelect1, 'Test Product 2#', 1);
        });

        it('GIVEN "fetchPage" WHEN opening a dropdown AND scrolling to the bottom THEN next page items should be displayed', async () => {
            await PageObject.Actions.clickSetFetchPage(multiSelect1App);

            await Select.Actions.toggleMultiSelector(multiSelect1);

            await Select.Actions.scrollToBottom(multiSelect1);

            await Select.Assertions.optionContainsTextByIndex(multiSelect1, 'Test Product 11#', 10);
            await Select.Assertions.optionContainsTextByIndex(multiSelect1, 'Test Product 12#', 11);
        });
    });
});
