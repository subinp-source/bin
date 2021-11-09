/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element, ElementFinder, ExpectedConditions as EC } from 'protractor';

export namespace DropdownObject {
    export const Constants = {
        PAGE_URL: 'test/e2e/genericEditor/componentWithDropdown/index.html'
    };

    export const Elements = {
        getDropdownSelector: (dropdown: string): ElementFinder => {
            return element(by.id(dropdown + '-selector'));
        },

        getDropdowOptionsSelector: (dropdownId: string): string => {
            return '#' + dropdownId + ' .ui-select-choices-row-inner';
        },

        getDropdownValues: async (dropdowns: string[]): Promise<string[]> => {
            const whenValues = dropdowns.map(
                async (dropdown) => await Elements.getDropdownSelector(dropdown).getText()
            );
            const values = await Promise.all(whenValues);
            return values;
        },

        getMultiDropdownSelector: (dropdown: string): ElementFinder => {
            return element(
                by.css('#' + dropdown + '-selector .select2-search-field .ui-select-search')
            );
        },

        getMultiDropdownValue: async (dropdown: string): Promise<string[]> => {
            const whenItems = element.all(
                by.xpath("//div[@id='" + dropdown + "-selector']/ul/span/li")
            );
            const texts = await whenItems.map<string>(async (item) => await item.getText());
            return texts;
        },

        getListOfOptions: async (dropdownId: string): Promise<string[]> => {
            const options = await element
                .all(by.css(Elements.getDropdowOptionsSelector(dropdownId)))
                .map<string>(async (item) => {
                    await browser.waitForPresence(item);
                    const text = await item.getText();
                    return text;
                });
            return options;
        },

        getItemPrinter: (dropdown: string): ElementFinder => {
            return element(by.xpath("//item-printer[@id='" + dropdown + "-selected']/div/span"));
        }
    };

    export const Actions = {
        clickDropdown: async (dropdown: string): Promise<void> => {
            await browser.click(element(by.cssContainingText('label', 'Dropdown A')));
            await Actions.selectDropdown(dropdown);
        },

        selectOption: async (dropdownId: string, optionLabel: string): Promise<void> => {
            await browser.click(
                by.cssContainingText(Elements.getDropdowOptionsSelector(dropdownId), optionLabel)
            );
        },

        selectDropdown: async (dropdown: string): Promise<void> => {
            await browser.click(Elements.getDropdownSelector(dropdown));
        },

        openAndBeReady: async (): Promise<void> => {
            await browser.get(Constants.PAGE_URL);
            await Helper.isReady();
        },

        clickMultiSelectDropdown: async (dropdown: string): Promise<void> => {
            await browser.click(Elements.getMultiDropdownSelector(dropdown));
        }
    };

    export const Assertions = {
        assertListOfOptions: async (
            dropdownId: string,
            expectedOptions: string[]
        ): Promise<void> => {
            const dropdownOptionCssSelector = '#' + dropdownId + ' .ui-select-choices-row-inner';
            await browser.waitUntil(async () => {
                const actualOptions = await element
                    .all(by.css(dropdownOptionCssSelector))
                    .map<string>(async (item) => {
                        let text: string;
                        try {
                            text = await item.getText();
                        } catch (err) {
                            text = '';
                        }
                        return text;
                    });
                return actualOptions.join(',') === expectedOptions.join(',');
            }, 'Expected dropdown options for ' + dropdownId + ' to be ' + expectedOptions);
        },

        searchAndAssertInDropdown: async (
            dropdownId: string,
            searchTerm: string,
            expectedOptions: string[]
        ): Promise<void> => {
            const searchInput = element(by.css('#' + dropdownId + ' input'));
            await searchInput.clear();
            await searchInput.sendKeys(searchTerm);

            await Assertions.assertListOfOptions(dropdownId, expectedOptions);
        }
    };

    const Helper = {
        isReady: async (): Promise<void> => {
            await browser.waitUntil(
                EC.visibilityOf(Elements.getItemPrinter('dropdownA')),
                'cannot select dropdown'
            );
        }
    };
}
