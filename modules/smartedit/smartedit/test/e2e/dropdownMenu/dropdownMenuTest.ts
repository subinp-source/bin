/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DropdownMenuPageObject as PageObject } from './dropdownMenuPageObject';

describe('DropdownMenu', () => {
    beforeEach(async () => {
        await PageObject.Actions.navigateToTestPage();
    });

    describe('Rendering Components - ', () => {
        it(`GIVEN Dropdown Menu has an Item rendered as a component WHEN Dropdown Menu is opened
      THEN Item should be rendered by this component`, async () => {
            // GIVEN & WHEN
            await PageObject.Actions.openWithComponentBasedDropdownMenuItem();

            // THEN
            await PageObject.Assertions.itemContainsGivenTagByIndex(0, 'dropdown-menu-item-mock');
            await PageObject.Assertions.itemHasTextByIndex(0, 'Mock Item Key');
        });

        it(`GIVEN Dropdown Menu has an Item with a "callback" property WHEN Dropdown Menu is opened
          THEN Item should be rendered by the Default Component`, async () => {
            // GIVEN & WHEN
            await PageObject.Actions.openWithDefaultComponentBasedDropdownMenuItem();

            // THEN
            await PageObject.Assertions.itemContainsGivenTagByIndex(
                0,
                'se-dropdown-menu-item-default'
            );
            await PageObject.Assertions.itemHasTextByIndex(0, 'Default Component Item Key');
        });

        // Legacy
        describe('Legacy - ', () => {
            it(`GIVEN Dropdown Menu has an Item with a "template" property WHEN Dropdown Menu is opened
            THEN Item should be rendered`, async () => {
                // GIVEN & WHEN
                await PageObject.Actions.openWithTemplateBasedDropdownMenuItem();

                // THEN
                await PageObject.Assertions.itemHasTextByIndex(0, 'Default template Item Key');
            });

            it(`GIVEN Dropdown Menu has an Item with a "templateUrl" property WHEN Dropdown Menu is opened
            THEN Item should be rendered`, async () => {
                // GIVEN, WHEN
                await PageObject.Actions.openWithTemplateUrlBasedDropdownMenuItem();

                // THEN
                await PageObject.Assertions.itemHasTextByIndex(0, 'Default templateUrl Item Key');
            });
        });
    });

    describe('Show and hide', () => {
        it('GIVEN Dropdown Menu is opened WHEN clicked outside THEN it should close', async () => {
            // GIVEN
            await PageObject.Actions.openWithDefaultComponentBasedDropdownMenuItem();

            // WHEN
            await PageObject.Actions.clickOutsideDropdownMenuComponent();

            // THEN
            await PageObject.Assertions.dropdownMenuListIsAbsent();
        });

        it('GIVEN Dropdown Menu is opened WHEN clicked on Item THEN it should close', async () => {
            // GIVEN
            await PageObject.Actions.openWithDefaultComponentBasedDropdownMenuItem();

            // WHEN
            await PageObject.Actions.clickOnListItemByIndex(0);

            // THEN
            await PageObject.Assertions.dropdownMenuListIsAbsent();
        });
    });
});
