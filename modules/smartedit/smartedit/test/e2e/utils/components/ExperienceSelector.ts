/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementArrayFinder, ElementFinder } from 'protractor';

export namespace ExperienceSelectorObject {
    export const Actions = {
        async clickInIframe(): Promise<void> {
            await browser.switchToIFrame();
            await browser.click(element(by.css('.noOffset1')));
            await browser.switchToParent();
        },
        async clickInApplication(): Promise<void> {
            await browser.click(element(by.css('.se-app-logo')));
        },
        async selectExpectedDate(): Promise<void> {
            await browser.click(
                element(by.css("div[class*='datepicker-days'] th[class*='picker-switch']"))
            );
            await browser.click(
                element(by.css("div[class*='datepicker-months'] th[class*='picker-switch']"))
            );
            await browser.click(element(by.cssContainingText("span[class*='year']", '2016')));
            await browser.click(element(by.css("span[class*='month']:first-child")));
            await browser.click(
                element(
                    by.xpath(
                        ".//*[.=\"1\" and contains(@class,'day') and not(contains(@class, 'old')) and not(contains(@class, 'new'))]"
                    )
                )
            );
            await browser.click(element(by.css("span[class*='glyphicon-time']")));
            await browser.click(element(by.css("div[class='timepicker-picker'] .timepicker-hour")));
            await browser.click(element(by.cssContainingText("td[class*='hour']", '01')));
            await browser.click(
                element(by.css("div[class='timepicker-picker'] .timepicker-minute"))
            );
            await browser.click(element(by.cssContainingText("td[class*='minute']", '00')));

            const periodToggleElement = element(
                by.cssContainingText("div[class*='timepicker'] button[class*='btn']", 'AM')
            );
            const isPresent = await periodToggleElement.isPresent();

            if (isPresent) {
                await browser.click(periodToggleElement);
            }
        },

        catalog: {
            async selectDropdown(): Promise<void> {
                await browser.click(element(by.id('previewCatalog')));
            },
            async selectOption(option: string): Promise<void> {
                const optionElement = element(
                    by.cssContainingText(
                        "[id='previewCatalog-list'] li[role='option'] span",
                        option
                    )
                );
                await browser.waitUntil(async () => {
                    try {
                        await optionElement.click();
                        const catalogLabel = element(by.id('previewCatalog-label'));
                        await browser.click(catalogLabel);

                        return true;
                    } catch (e) {
                        return false;
                    }
                }, 'Option not clickable');
            }
        },

        calendar: {
            async setDate(date: string): Promise<void> {
                const timeField = element(by.css("input[name='time']"));
                await browser.waitForVisibility(timeField);
                await browser.click(timeField, 'Experience Selector Date and Time Field not found');
                await timeField.sendKeys(date);
                const timeLabel = element(by.id('time-label'));

                await browser.click(timeLabel);
            }
        },

        language: {
            async selectDropdown(): Promise<void> {
                await browser.click(element(by.id('language')));
            },
            async selectOption(option: string): Promise<void> {
                const optionElement = element(
                    by.cssContainingText("[id='language-list'] li[role='option'] span", option)
                );

                await browser.waitUntil(async () => {
                    try {
                        await optionElement.click();

                        const languageLabel = element(by.id('language-label'));
                        await browser.click(languageLabel);
                        return true;
                    } catch (e) {
                        return false;
                    }
                }, 'Option not clickable');
            }
        },

        widget: {
            async openExperienceSelector(): Promise<void> {
                await browser.waitUntilNoModal();
                await browser.click(
                    by.id('experience-selector-btn'),
                    'Experience Selector button not found'
                );

                await browser.waitForPresence(
                    element(by.xpath("//item-printer[@id='language-selected']/div/span")),
                    'cannot load catalog item'
                );
            },
            async submit(): Promise<void> {
                await browser.click(by.id('submit'), 'Experience Selector Apply Button not found');
            },
            async cancel(): Promise<void> {
                await browser.click(by.id('cancel'), 'Experience Selector Apply Button not found');
            }
        },

        productCatalogs: {
            async openMultiProductCatalogVersionsSelectorWidget(): Promise<void> {
                await browser.click(
                    element(by.css("[id='multi-product-catalog-versions-selector']"))
                );
                await browser.waitForVisibility(element(by.id('y-modal-dialog')));
            },

            async selectOptionFromMultiProductCatalogVersionsSelectorWidget(
                catalogId: string,
                catalogVersion: string
            ): Promise<void> {
                await browser.click(element(by.css(`[id='${catalogId}'] .toggle-button`)));

                await browser.click(
                    element(
                        by.cssContainingText(`[id='${catalogId}'] .menu-option`, catalogVersion)
                    )
                );
            },

            async clickModalWindowDone(): Promise<void> {
                await browser.click(by.id('done'));
            }
        },

        async switchToCatalogVersion(catalogVersion: string): Promise<void> {
            await ExperienceSelectorObject.Actions.widget.openExperienceSelector();

            await ExperienceSelectorObject.Actions.catalog.selectDropdown();
            await ExperienceSelectorObject.Actions.catalog.selectOption(catalogVersion);

            await ExperienceSelectorObject.Actions.widget.submit();
            await browser.waitForWholeAppToBeReady();

            await ExperienceSelectorObject.Actions.widget.openExperienceSelector();
        }
    };

    export const Assertions = {
        catalog: {
            async assertOptionText(index: number, expectedText: string): Promise<void> {
                await browser.waitUntil(async () => {
                    let text;

                    try {
                        text = await ExperienceSelectorObject.Elements.catalog
                            .option(index)
                            .getText();
                    } catch (e) {
                        text = '';
                    }

                    return text === expectedText;
                }, 'Dropdown options missing');
            },

            async assertNumberOfOptions(length: number) {
                await browser.waitUntil(async () => {
                    const count = await ExperienceSelectorObject.Elements.catalog.options().count();

                    return count === length;
                }, 'dropdown failed to contain ' + length + ' elements');
            }
        },

        language: {
            async assertOptionText(index: number, expectedText: string) {
                await browser.waitUntil(async () => {
                    let text;

                    try {
                        text = await ExperienceSelectorObject.Elements.language
                            .option(index)
                            .getText();
                    } catch (e) {
                        text = '';
                    }

                    return text === expectedText;
                }, 'Dropdown options missing');
            },

            async assertNumberOfOptions(length: number): Promise<void> {
                await browser.waitUntil(async () => {
                    let count;

                    try {
                        count = await ExperienceSelectorObject.Elements.language.options().count();
                    } catch (e) {
                        count = 0;
                    }

                    return count === length;
                }, 'dropdown failed to contain ' + length + ' elements');
            }
        }
    };

    export const Elements = {
        widget: {
            button(): ElementFinder {
                return element(by.id('experience-selector-btn'));
            },
            async text(): Promise<string> {
                const text = await element(
                    by.css("[class*='se-experience-selector__btn-text ']")
                ).getText();

                return text;
            },
            getExperienceMenu(): ElementFinder {
                return element(
                    by.css('se-experience-selector-button .se-experience-selector__dropdown')
                );
            }
        },

        catalog: {
            label(): ElementFinder {
                return element(by.id('previewCatalog-label'));
            },
            selectedOption(): ElementFinder {
                return element(by.css("[id='previewCatalog-selected']"));
            },
            dropdown(): ElementFinder {
                return element(by.css("[id='previewCatalog'] [class*='ui-select-container'] > a"));
            },
            option(index: number): ElementFinder {
                return element(
                    by.css(
                        "[id='previewCatalog'] ul[role='listbox'] li[role='option']:nth-child(" +
                            index +
                            ') span'
                    )
                );
            },
            options(): ElementArrayFinder {
                return element.all(
                    by.css("[id='previewCatalog'] ul[role='listbox'] li[role='option'] span")
                );
            }
        },

        dateAndTime: {
            label(): ElementFinder {
                return element(by.id('time-label'));
            },
            async field(): Promise<ElementFinder> {
                const timeField = element(by.css("input[name='time']"));
                await browser.waitForVisibility(
                    timeField,
                    'Experience Selector Date and Time Field not found'
                );
                return timeField;
            },
            button(): ElementFinder {
                return element(
                    by.css("[id='time'] div[class*='date'] span[class*='input-group-addon']")
                );
            }
        },

        language: {
            async label(): Promise<ElementFinder> {
                const languageLabel = element(by.id('language-label'));
                await browser.waitForVisibility(
                    languageLabel,
                    'Experience Selector Language Field Label not found'
                );
                return languageLabel;
            },
            async selectedOption(): Promise<ElementFinder> {
                const languageField = element(
                    by.css("[id='language-selected'] span[class*='y-select-default-item']")
                );
                await browser.waitForVisibility(
                    languageField,
                    'Experience Selector Language Field not found'
                );
                return languageField;
            },
            dropdown(): ElementFinder {
                return element(by.css("[id='language'] [class*='ui-select-container'] > a"));
            },
            option(index: number): ElementFinder {
                return element(
                    by.css(
                        "[id='language'] ul[role='listbox'] li[role='option']:nth-child(" +
                            index +
                            ') span'
                    )
                );
            },
            options(): ElementArrayFinder {
                return element.all(
                    by.css("[id='language'] ul[role='listbox'] li[role='option'] span")
                );
            }
        },

        productCatalogs: {
            label(): ElementFinder {
                return element(by.id('productCatalogVersions-label'));
            }
        },

        singleProductCatalogVersionSelector: {
            selectedOption(): ElementFinder {
                return element(by.css("[id='productCatalogVersions-selected']"));
            },
            dropdown(): ElementFinder {
                return element(by.css("[id='productCatalogVersions'] .toggle-button"));
            },
            option(index: number): ElementFinder {
                return element.all(by.css("[id='productCatalogVersions'] .menu-option")).get(index);
            },
            options(): ElementArrayFinder {
                return element.all(by.css("[id='productCatalogVersions'] .menu-option"));
            }
        },

        multiProductCatalogVersionsSelector: {
            async selectedOptions(): Promise<string> {
                const value = await element(
                    by.css("input[name='productCatalogVersions']")
                ).getAttribute('value');

                return value;
            },

            getSelectedOptionFromMultiProductCatalogVersionsSelectorWidget(
                catalogId: string
            ): ElementFinder {
                return element(by.css('[id="' + catalogId + '-selected"]'));
            }
        },

        otherFields: {
            label(fieldName: string): ElementFinder {
                return element(by.id(fieldName + '-label'));
            },
            field(fieldName: string): ElementFinder {
                return element(by.css("input[name='" + fieldName + "']"));
            }
        },

        buttons: {
            ok(): ElementFinder {
                return element(by.id('submit'));
            },
            cancel(): ElementFinder {
                return element(by.id('cancel'));
            }
        },

        page: {
            iframe(): ElementFinder {
                return element(by.css('#js_iFrameWrapper iframe'));
            }
        }
    };
}
