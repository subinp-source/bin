/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser } from 'protractor';

import { SelectTestPageObject as PageObject } from './SelectTestPageObject';
import { SelectComponentObject as Select } from '../utils/components/SelectComponentObject';

describe('Single Select - ', () => {
    const singleSelect1 = 'example1';
    const singleSelect1App = 'example1';
    const singleSelect2 = 'singleSelect2';

    beforeEach(async () => {
        await browser.get('test/e2e/select/index.html');
        await browser.waitForContainerToBeReady();
    });

    describe('Single Selector - ', () => {
        it('GIVEN a validation state THEN the selector should display ERROR, WARNING and NO STATE.', async () => {
            await PageObject.Actions.clickShowErrorButton(singleSelect1App);
            await Select.Assertions.selectorHasValidationType(
                singleSelect1,
                Select.Constants.VALIDATION_MESSAGE_TYPE.VALIDATION_ERROR
            );

            await PageObject.Actions.clickShowWarningButton(singleSelect1App);
            await Select.Assertions.selectorHasValidationType(
                singleSelect1,
                Select.Constants.VALIDATION_MESSAGE_TYPE.WARNING
            );

            await PageObject.Actions.clickResetValidationButton(singleSelect1App);
            await Select.Assertions.selectorHasNoValidationType(singleSelect1);
        });

        it('GIVEN a user selects the German option THEN the displayed value of the option should be "de"', async () => {
            await Select.Actions.toggleSingleSelector(singleSelect1);

            await Select.Actions.selectOptionByText(singleSelect1, 'German');

            await PageObject.Assertions.selectorModelIsEqualTo(singleSelect1App, 'de');
        });

        it(
            'GIVEN a selected option that does not exist in another set of options and "Force Reset" is checked ' +
                'WHEN switching to another set of options THEN the selected model should reset',
            async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);
                await Select.Actions.selectOptionByText(singleSelect1, 'Russian');

                await PageObject.Actions.clickChangeSourceButton(singleSelect1App);

                await PageObject.Assertions.selectorModelIsEqualTo(singleSelect1App, '');
            }
        );

        it(
            'GIVEN the initial options and "Force Reset" is unchecked WHEN selecting a language that does not exist in another set ' +
                'THEN the selected model should not reset',
            async () => {
                await PageObject.Actions.clickForceResetCheckbox(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);
                await Select.Actions.selectOptionByText(singleSelect1, 'Russian');

                await PageObject.Actions.clickChangeSourceButton(singleSelect1App);

                await PageObject.Assertions.selectorModelIsEqualTo(singleSelect1App, 'ru');
            }
        );

        // new specs
        it(
            'GIVEN a selected option that does exist in another set of options and "Force Reset" is checked WHEN switching to another set of options ' +
                'THEN the selected model should not reset',
            async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);
                await Select.Actions.selectOptionByText(singleSelect1, 'English');

                await PageObject.Actions.clickChangeSourceButton(singleSelect1App);

                await PageObject.Assertions.selectorModelIsEqualTo(singleSelect1App, 'en');
            }
        );

        it('GIVEN a custom item component THEN options and selected option should be displayed with that component', async () => {
            await Select.Assertions.selectedOptionContainsTextByIndex(
                singleSelect2,
                'Test Product 1#',
                0
            );

            await Select.Actions.toggleSingleSelector(singleSelect2);

            await Select.Assertions.optionContainsTextByIndex(singleSelect2, 'Test Product 1#', 0);
            await Select.Assertions.optionContainsTextByIndex(singleSelect2, 'Test Product 2#', 1);
        });

        it(
            'GIVEN initial settings WHEN selecting an option THEN that option should be selected AND ' +
                ' displayed in dropdown list at the same time',
            async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);
                await Select.Actions.selectOptionByText(singleSelect1, 'German');
                await Select.Assertions.selectedOptionContainsTextByIndex(
                    singleSelect1,
                    'German',
                    0
                );

                await Select.Actions.toggleSingleSelector(singleSelect1);
                await Select.Assertions.optionContainsTextByIndex(singleSelect1, 'German', 1);
            }
        );

        it('GIVEN selected item WHEN opening a dropdown THEN item should be highlighted', async () => {
            await Select.Actions.toggleSingleSelector(singleSelect1);

            await Select.Assertions.selectOptionIsSelectedByText(singleSelect1, 'English');

            await Select.Actions.selectOptionByText(singleSelect1, 'German');

            await Select.Actions.toggleSingleSelector(singleSelect1);

            await Select.Assertions.selectOptionIsSelectedByText(singleSelect1, 'German');
        });

        describe('Results Header', () => {
            it('GIVEN "resultsHeaderLabel" WHEN opening a dropdown THEN it should be displayed with given text', async () => {
                await PageObject.Actions.clickSetResultsHeaderLabelButton(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Assertions.resultsHeaderContainsText(
                    singleSelect1,
                    'Results Header Label'
                );
            });

            it('GIVEN "resultsHeaderComponent" WHEN opening a dropdown THEN it should be displayed', async () => {
                await PageObject.Actions.clickSetResultsHeaderComponentButton(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Assertions.resultsHeaderContainsText(
                    singleSelect1,
                    'Results Header Custom Component'
                );
            });
        });

        describe('Placeholder', () => {
            it('GIVEN settings for controls WHEN removing a selected option THEN the placeholder should be displayed in place of selected item', async () => {
                await Select.Actions.removeSelectedOptionByText(singleSelect1, 'English');

                await Select.Assertions.selectedPlaceholderIsDisplayed(singleSelect1);
            });

            it('GIVEN an option is not selected WHEN opening a dropdown THEN search field should not have a placeholder', async () => {
                await Select.Actions.removeSelectedOptionByText(singleSelect1, 'English');

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Assertions.searchInputPlaceholderIsEqualToText(singleSelect1, '');
            });

            it('GIVEN an option is selected WHEN opening a dropdown THEN search field should have a placeholder', async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Assertions.searchInputPlaceholderIsEqualToText(
                    singleSelect1,
                    'Select an Option'
                );
            });
        });

        describe('Actionable Search Item', () => {
            beforeEach(async () => {
                // Because of mocked data, results are not filtered when the value is entered
                // so we clear the items to display Actionable Search Item.
                await PageObject.Actions.clickClearSourceButton(singleSelect1App);
                await PageObject.Actions.clickSetActionableSearchItemButton(singleSelect1App);
            });
            it(
                'GIVEN "actionableSearchItem" WHEN opening a dropdown AND providing search input value ' +
                    'for which items cannot be found THEN it should be displayed',
                async () => {
                    await Select.Actions.toggleSingleSelector(singleSelect1);
                    await Select.Actions.setSearchInputValue(
                        singleSelect1,
                        'item label I would like to add'
                    );

                    await Select.Assertions.actionableSearchItemActionButtonIsDisplayed(
                        singleSelect1
                    );
                }
            );

            it('GIVEN "actionableSearchItem" WHEN clicking on action button THEN the dropdown should be closed', async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);
                await Select.Actions.setSearchInputValue(
                    singleSelect1,
                    'item label I would like to add'
                );
                await Select.Actions.clickActionableSearchItemActionButton(singleSelect1);

                await Select.Assertions.dropdownIsClosed(singleSelect1);
            });
        });

        describe('Search Input', () => {
            it('GIVEN "searchEnabled" is false WHEN opening a dropdown THEN the search input should not be displayed', async () => {
                await PageObject.Actions.clickSearchEnabledCheckbox(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Assertions.searchInputIsNotPresent(singleSelect1);
            });

            it('GIVEN "searchEnabled" is true WHEN opening a dropdown THEN the search input should be displayed', async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Assertions.searchInputIsPresent(singleSelect1);
            });

            it(
                'GIVEN "resetSearchInput" is true WHEN providing the search input AND closing the dropdown AND opening the dropdown again ' +
                    'THEN the value should be empty',
                async () => {
                    await PageObject.Actions.clickResetSearchInputCheckbox(singleSelect1App);

                    await Select.Actions.toggleSingleSelector(singleSelect1);
                    await Select.Actions.setSearchInputValue(singleSelect1, 'abc');
                    await PageObject.Actions.clickOutsideSelector();
                    await Select.Actions.toggleSingleSelector(singleSelect1);

                    await Select.Assertions.searchInputValueIsEqualTo(singleSelect1, '');
                }
            );

            it(
                'GIVEN "resetSearchInput" is false WHEN providing the search input AND closing the dropdown AND opening the dropdown again ' +
                    'THEN the value should persist',
                async () => {
                    await PageObject.Actions.clickResetSearchInputCheckbox(singleSelect1App);
                    await PageObject.Actions.clickResetSearchInputCheckbox(singleSelect1App);

                    await Select.Actions.toggleSingleSelector(singleSelect1);
                    await Select.Actions.setSearchInputValue(singleSelect1, 'abc');
                    await PageObject.Actions.clickOutsideSelector();
                    await Select.Actions.toggleSingleSelector(singleSelect1);

                    await Select.Assertions.searchInputValueIsEqualTo(singleSelect1, 'abc');
                }
            );

            it(
                'GIVEN "resetSearchInput" is false WHEN providing the search input AND selecting an option AND opening the dropdown again ' +
                    'THEN the value should persist',
                async () => {
                    await PageObject.Actions.clickResetSearchInputCheckbox(singleSelect1App);
                    await PageObject.Actions.clickResetSearchInputCheckbox(singleSelect1App);

                    await Select.Actions.toggleSingleSelector(singleSelect1);
                    await Select.Actions.setSearchInputValue(singleSelect1, 'ger');
                    await Select.Actions.selectOptionByText(singleSelect1, 'German');
                    await Select.Actions.toggleSingleSelector(singleSelect1);

                    await Select.Assertions.searchInputValueIsEqualTo(singleSelect1, 'ger');
                }
            );
        });

        describe('isReadOnly', () => {
            it('GIVEN "isReadOnly" is true THEN component should be disabled', async () => {
                await PageObject.Actions.clickIsReadOnlyCheckbox(singleSelect1App);

                await Select.Assertions.dropdownNotClickable(singleSelect1);
            });
        });

        describe('Disabled options', () => {
            it('GIVEN "disableChoiceFn" is defined WHEN opening a dropdown AND clicking on disabled option THEN it should not close the dropdown', async () => {
                await PageObject.Actions.clickDisableChoiceFnCheckbox(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Actions.selectOptionByText(singleSelect1, 'German');
                await Select.Assertions.dropdownIsOpened(singleSelect1);
            });

            it('GIVEN "disableChoiceFn" has condition to disable given model WHEN opening a dropdown THEN option should be selected but clicking on it should not close the dropdown', async () => {
                await PageObject.Actions.clickDisableChoiceFnCheckbox(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Actions.selectOptionByText(singleSelect1, 'English');

                await Select.Assertions.dropdownIsOpened(singleSelect1);
            });
        });

        describe('Keyboard navigation', () => {
            it('GIVEN default settings WHEN opening a dropdown THEN first option should be active', async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Assertions.optionIsActiveByIndex(singleSelect1, 0);
            });

            it('GIVEN default settings WHEN opening a dropdown AND pressing arrow up / arrow down key THEN a particular option should be active', async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Actions.arrowDown();
                await Select.Assertions.optionIsActiveByIndex(singleSelect1, 1);

                await Select.Actions.arrowDown();
                await Select.Assertions.optionIsActiveByIndex(singleSelect1, 2);

                await Select.Actions.arrowUp();
                await Select.Assertions.optionIsActiveByIndex(singleSelect1, 1);

                await Select.Actions.arrowUp();
                await Select.Assertions.optionIsActiveByIndex(singleSelect1, 0);
            });

            it(
                'GIVEN "disableChoiceFn" is defined WHEN opening a dropdown AND pressing arrow up / arrow down keys ' +
                    'THEN disabled options should not be active',
                async () => {
                    await PageObject.Actions.clickDisableChoiceFnCheckbox(singleSelect1App);
                    await Select.Actions.toggleSingleSelector(singleSelect1);

                    await Select.Assertions.optionIsActiveByIndex(singleSelect1, 2);

                    await Select.Actions.arrowDown();
                    await Select.Assertions.optionIsActiveByIndex(singleSelect1, 3);

                    await Select.Actions.arrowUp();
                    await Select.Assertions.optionIsActiveByIndex(singleSelect1, 2);

                    await Select.Actions.arrowUp();
                    await Select.Assertions.optionIsActiveByIndex(singleSelect1, 2);
                }
            );

            it('GIVEN default settings WHEN opening a dropdown AND pressing enter key THEN highlighted option should be selected', async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Actions.arrowDown();
                await Select.Actions.enter();

                await Select.Assertions.selectedOptionContainsTextByIndex(
                    singleSelect1,
                    'German',
                    0
                );
            });

            it('GIVEN default settings WHEN opening a dropdown AND pressing esc key THEN dropdown should be closed', async () => {
                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Actions.enter();

                await Select.Assertions.dropdownIsClosed(singleSelect1);
            });
        });

        describe('callbacks', () => {
            describe('onChange', () => {
                beforeEach(async () => {
                    await PageObject.Actions.clickSetOnChangeButton(singleSelect1App);
                });

                it('GIVEN "onChange" WHEN rendering a dropdown THEN "onChange" function should be called', async () => {
                    await PageObject.Assertions.onChangeHasBeenCalledTimes(singleSelect1App, 1);
                });

                it('GIVEN "onChange" WHEN removing a selected option THEN "onChange" function should be called', async () => {
                    await Select.Actions.removeSelectedOptionByText(singleSelect1, 'English');

                    await PageObject.Assertions.onChangeHasBeenCalledTimes(singleSelect1App, 2);
                });

                it('GIVEN "onChange" WHEN selecting an option whis is not selected THEN "onChange" function should be called', async () => {
                    await Select.Actions.toggleSingleSelector(singleSelect1);

                    await Select.Actions.selectOptionByText(singleSelect1, 'German');

                    await PageObject.Assertions.onChangeHasBeenCalledTimes(singleSelect1App, 2);
                });

                it('GIVEN "onChange" WHEN selecting an option whis is selected THEN "onChange" function should not be called', async () => {
                    await Select.Actions.toggleSingleSelector(singleSelect1);

                    await Select.Actions.selectOptionByText(singleSelect1, 'English');

                    await PageObject.Assertions.onChangeHasBeenCalledTimes(singleSelect1App, 1);
                });

                it('GIVEN "onChange" WHEN Parent Component changes the model THEN "onChange" function should be called', async () => {
                    await PageObject.Actions.clickChangeModelButton(singleSelect1App);

                    await PageObject.Assertions.onChangeHasBeenCalledTimes(singleSelect1App, 2);
                });
            });

            it('GIVEN "onRemove" WHEN removing a selected option THEN the "onRemove" function should be called', async () => {
                await PageObject.Actions.clickSetOnRemoveButton(singleSelect1App);

                await Select.Actions.removeSelectedOptionByText(singleSelect1, 'English');

                await PageObject.Assertions.onRemoveModelIsEqualTo(singleSelect1App, 'en');
            });

            it('GIVEN "onSelect" WHEN selecting an option THEN the "onSelect" function should be called', async () => {
                await PageObject.Actions.clickSetOnSelectButton(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Actions.selectOptionByText(singleSelect1, 'German');

                await PageObject.Assertions.onSelectModelIsEqualTo(singleSelect1App, 'de');
            });

            it('GIVEN "keyup" WHEN providing search input AND releasing a key (keyup) THEN the "keyup" function should be called', async () => {
                await PageObject.Actions.clickSetKeyupButton(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Actions.setSearchInputValue(singleSelect1, 'a');

                await PageObject.Assertions.keyupSearchIsEqualTo(singleSelect1App, 'a');

                await Select.Actions.setSearchInputValue(singleSelect1, 'b');

                await PageObject.Assertions.keyupSearchIsEqualTo(singleSelect1App, 'ab');
            });
        });

        describe('fetchPage', () => {
            beforeEach(async () => {
                // Clear the "model" because it is not required for the following specs.
                // If "model" is provided, SelectComponent validation will throw an error because no "fetchEntities" is provided.
                await PageObject.Actions.clickClearModelButton(singleSelect1App);
            });

            it('GIVEN "fetchPage" WHEN opening a dropdown THEN items should be displayed', async () => {
                await PageObject.Actions.clickSetFetchPage(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Assertions.optionContainsTextByIndex(singleSelect1, 'Site1', 0);
                await Select.Assertions.optionContainsTextByIndex(singleSelect1, 'Site2', 1);
            });

            it('GIVEN "fetchPage" WHEN opening a dropdown AND scrolling to the bottom THEN next page items should be displayed', async () => {
                await PageObject.Actions.clickSetFetchPage(singleSelect1App);

                await Select.Actions.toggleSingleSelector(singleSelect1);

                await Select.Actions.scrollToBottom(singleSelect1);

                await Select.Assertions.optionContainsTextByIndex(singleSelect1, 'Site10', 9);
                await Select.Assertions.optionContainsTextByIndex(singleSelect1, 'Site11', 10);
            });
        });
    });
});
