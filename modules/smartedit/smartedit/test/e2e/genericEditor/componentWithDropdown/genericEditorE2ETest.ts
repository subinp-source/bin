/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DropdownObject as Dropdown } from './dropdownObject';

describe('GenericEditor Dropdown - ', () => {
    beforeEach(async () => {
        await Dropdown.Actions.openAndBeReady();
    });

    it('GIVEN a structure API and a content API THEN all dropdowns will be populated with respective values (if found in their list) else will reset', async () => {
        // has data in content API and match from list
        const valuesAB = await Dropdown.Elements.getDropdownValues(['dropdownA', 'dropdownB']);
        expect(valuesAB).toEqual(['OptionA2', 'OptionB7-A1-A2']);

        const valuesC = await Dropdown.Elements.getMultiDropdownValue('dropdownC');
        expect(valuesC).toEqual(['OptionC3-A2', 'OptionC4-A2']);

        const valuesD = await Dropdown.Elements.getMultiDropdownValue('dropdownD');
        expect(valuesD).toEqual(['OptionD2-sample-element']);

        // has data in content API but does not values match in the list
        const valuesE = await Dropdown.Elements.getDropdownValues(['dropdownE']);
        expect(valuesE).toEqual(['Select an Option']);
    });

    it('GIVEN a set of cascading dropdowns WHEN I change value of a dropdown THEN all the children dropdowns are reset while the independent ones are untouched', async () => {
        await Dropdown.Actions.clickDropdown('dropdownA');
        await Dropdown.Actions.selectOption('dropdownA', 'OptionA1');

        const valuesABE = await Dropdown.Elements.getDropdownValues([
            'dropdownA',
            'dropdownB',
            'dropdownE'
        ]);
        expect(valuesABE).toEqual(['OptionA1', 'OptionB7-A1-A2', 'Select an Option']);

        const valuesC = await Dropdown.Elements.getMultiDropdownValue('dropdownC');
        expect(valuesC).toEqual([]);

        const valuesD = await Dropdown.Elements.getMultiDropdownValue('dropdownD');
        expect(valuesD).toEqual(['OptionD2-sample-element']);
    });

    it('GIVEN a set of cascading dropdowns WHEN I update value of the parent dropdown THEN all the children dropdowns should update their options', async () => {
        // change dropdown A
        await Dropdown.Actions.clickDropdown('dropdownA');
        await Dropdown.Actions.selectOption('dropdownA', 'OptionA1');
        await Dropdown.Actions.clickDropdown('dropdownB');
        await Dropdown.Assertions.assertListOfOptions('dropdownB', [
            'OptionB1-A1',
            'OptionB2-A1',
            'OptionB7-A1-A2'
        ]);

        await Dropdown.Actions.clickDropdown('dropdownC');
        await Dropdown.Assertions.assertListOfOptions('dropdownC', ['OptionC1-A1', 'OptionC2-A1']);

        await Dropdown.Actions.clickDropdown('dropdownE');
        await Dropdown.Assertions.assertListOfOptions('dropdownE', ['OptionE7-B7']);

        // change dropdown B
        await Dropdown.Actions.clickDropdown('dropdownB');
        await Dropdown.Actions.selectOption('dropdownB', 'OptionB1-A1');
        await Dropdown.Actions.clickDropdown('dropdownE');
        await Dropdown.Assertions.assertListOfOptions('dropdownE', ['OptionE1-B1']);
    });

    it('GIVEN a dropdown WHEN I start typing in the dropdown search THEN the options should be filtered to match the searched key', async () => {
        await Dropdown.Actions.clickMultiSelectDropdown('dropdownD');
        await Dropdown.Assertions.assertListOfOptions('dropdownD', [
            'OptionD1-sample',
            'OptionD3-element'
        ]);
        await Dropdown.Assertions.searchAndAssertInDropdown('dropdownD', 'sample', [
            'OptionD1-sample'
        ]);
        await Dropdown.Assertions.searchAndAssertInDropdown('dropdownD', '', [
            'OptionD1-sample',
            'OptionD3-element'
        ]);
    });
});
