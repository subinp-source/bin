/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SelectObject } from './selectObject';

describe('Select', () => {
    beforeEach(() => {
        SelectObject.Actions.navigateToTestPage();
    });

    describe('- Basic', () => {
        it('GIVEN select trigger is clicked THEN menu is present', () => {
            SelectObject.Actions.clickSelectDropdownTrigger('select-one');

            SelectObject.Assertions.selectDropdownMenuIsPresent('select-one');
        });

        it('GIVEN select trigger is clicked when dropdown is open THEN menu is absent', () => {
            SelectObject.Actions.clickSelectDropdownTrigger('select-one');
            SelectObject.Actions.clickSelectDropdownTrigger('select-one');

            SelectObject.Assertions.selectDropdownMenuIsAbsent('select-one');
        });

        it('Select placeholder exists', () => {
            SelectObject.Assertions.selectDropdownTriggerPlaceholderIsPresent(
                'select-one',
                'My placeholder'
            );
        });

        it('GIVEN select value is selected THEN the select menu is absent', () => {
            SelectObject.Actions.clickSelectDropdownTrigger('select-one');
            SelectObject.Actions.clickMenuItemByValue('select-one', 'label_0');

            SelectObject.Assertions.selectDropdownMenuIsAbsent('select-one');
        });

        it('GIVEN select value is selected THEN the select output displays value', () => {
            SelectObject.Actions.clickSelectDropdownTrigger('select-one');
            SelectObject.Actions.clickMenuItemByValue('select-one', 'label_0');

            SelectObject.Assertions.selectDropdownOutputIsPresent('select-one-output', 'value_0');
        });

        it('GIVEN select value is selected THEN the placeholder value changes', () => {
            SelectObject.Actions.clickSelectDropdownTrigger('select-one');
            SelectObject.Actions.clickMenuItemByValue('select-one', 'label_0');

            SelectObject.Assertions.selectDropdownTriggerPlaceholderIsPresent(
                'select-one',
                'label_0'
            );
        });
    });
    describe('- With Initial Value', () => {
        it('Select placeholder exists with initial value label', () => {
            SelectObject.Assertions.selectDropdownTriggerPlaceholderIsPresent(
                'select-two',
                'label_0'
            );
        });

        it('GIVEN select dropdown menu item is clicked THEN placeholder is no longer set to initial value', () => {
            SelectObject.Actions.clickSelectDropdownTrigger('select-two');
            SelectObject.Actions.clickMenuItemByValue('select-two', 'label_1');

            SelectObject.Assertions.selectDropdownTriggerPlaceholderIsPresent(
                'select-two',
                'label_1'
            );
        });
    });

    describe('- Intergration with Angular Forms', () => {
        it('GIVEN select value is selected THEN the select output displays value using reactive forms', () => {
            SelectObject.Actions.clickSelectDropdownTrigger('select-three');
            SelectObject.Actions.clickMenuItemByValue('select-three', 'label_1');

            SelectObject.Assertions.selectDropdownOutputIsPresent('select-three-output', 'value_1');
        });

        it('GIVEN angular form control is initialized with value THEN the select placeholder should display value', () => {
            SelectObject.Assertions.selectDropdownTriggerPlaceholderIsPresent(
                'select-three',
                'label_0'
            );
        });
    });
});
