/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
var displayConditionsPrimaryPage;
if (typeof require !== 'undefined') {
    displayConditionsPrimaryPage = require('../componentObjects/DisplayConditionsPrimaryPageComponentObject');
}
module.exports = (function() {
    var pageObject = {};

    function getTrimmedTextFromElement(element) {
        return element.getText().then(function(text) {
            return text.trim();
        });
    }

    pageObject.elements = {
        getSuccessfulEditPageButton: function() {
            return element(by.id('openPageEditorSuccess'));
        },
        getFailEditPageButton: function() {
            return element(by.id('openPageEditorFail'));
        },
        getPrimaryContentPageModalButton: function() {
            return element(by.cssContainingText('tr', 'Primary Content Page')).element(
                by.css('button')
            );
        },
        getVariationContentPageModalButton: function() {
            return element(by.cssContainingText('tr', 'Variation Content Page')).element(
                by.css('button')
            );
        },
        getPrimaryCategoryPageModalButton: function() {
            return element(by.cssContainingText('tr', 'Primary Category Page')).element(
                by.css('button')
            );
        },
        getVariationCategoryPageModalButton: function() {
            return element(by.cssContainingText('tr', 'Variation Category Page')).element(
                by.css('button')
            );
        },
        getPrimaryProductPageModalButton: function() {
            return element(by.cssContainingText('tr', 'Primary Product Page')).element(
                by.css('button')
            );
        },
        getVariationProductPageModalButton: function() {
            return element(by.cssContainingText('tr', 'Variation Product Page')).element(
                by.css('button')
            );
        },
        getLabelField: function() {
            return element(by.id('label-shortstring'));
        },
        getNameField: function() {
            return element(by.id('name-shortstring'));
        },
        getUidField: function() {
            return element(by.id('uid-shortstring'));
        },
        getSaveButton: function() {
            return element(by.id('save'));
        },
        getCancelButton: function() {
            return element(by.id('cancel'));
        },
        getDisplayConditionsTab: function() {
            return element(by.css('[tab-id="displaycondition"] > a'));
        },
        getModal: function() {
            return element(by.css('.fd-modal'));
        },
        getBasicInfoTabHeader: function() {
            return element(by.css('li[tab-id="information"] > a'));
        },
        getBasicInfoTabHeaderClass: function() {
            return this.getBasicInfoTabHeader().getAttribute('class');
        },
        getNameErrorsElement: function() {
            return element(
                by.css('#name se-generic-editor-field-messages .se-help-block--has-error')
            );
        },
        getNameErrorsText: function() {
            return getTrimmedTextFromElement(this.getNameErrorsElement());
        },
        getDisplayConditionsPageNameText: function() {
            return getTrimmedTextFromElement(element(by.css('.dc-page-name')));
        },
        getDisplayConditionsPageTypeText: function() {
            return getTrimmedTextFromElement(element(by.css('.dc-page-type')));
        },
        getPageDisplayConditionText: function() {
            return getTrimmedTextFromElement(element(by.css('.dc-page-display-condition')));
        },
        getPageVariationsFirstRow: function() {
            return element(by.css('display-conditions-page-variations tbody tr:nth-child(1)'));
        },
        getPageVariationsSecondRow: function() {
            return element(by.css('display-conditions-page-variations tbody tr:nth-child(2)'));
        },
        getPageNameOfFirstPageVariationRow: function() {
            return getTrimmedTextFromElement(
                pageObject.elements
                    .getPageVariationsFirstRow()
                    .element(by.css('.se-paged-list-item-pageName'))
            );
        },
        getCreationDateOfFirstPageVariationRow: function() {
            return getTrimmedTextFromElement(
                pageObject.elements
                    .getPageVariationsFirstRow()
                    .element(by.css('.se-paged-list-item-creationDate'))
            );
        },
        getRestrictionsOfFirstPageVariationRow: function() {
            return getTrimmedTextFromElement(
                pageObject.elements
                    .getPageVariationsFirstRow()
                    .element(by.css('.se-paged-list-item-restrictions'))
            );
        },
        getPageNameOfSecondPageVariationRow: function() {
            return getTrimmedTextFromElement(
                pageObject.elements
                    .getPageVariationsSecondRow()
                    .element(by.css('.se-paged-list-item-pageName'))
            );
        },
        getCreationDateOfSecondPageVariationRow: function() {
            return getTrimmedTextFromElement(
                pageObject.elements
                    .getPageVariationsSecondRow()
                    .element(by.css('.se-paged-list-item-creationDate'))
            );
        },
        getRestrictionsOfSecondPageVariationRow: function() {
            return getTrimmedTextFromElement(
                pageObject.elements
                    .getPageVariationsSecondRow()
                    .element(by.css('.se-paged-list-item-restrictions'))
            );
        },
        getSelectedOptionText: function() {
            return getTrimmedTextFromElement(
                displayConditionsPrimaryPage.elements.getSelectedOption()
            );
        },
        waitForPrimaryPageDropDownToHaveOptions: function(expectedCount) {
            return browser.waitUntil(function() {
                return element
                    .all(by.css('.ui-select-choices-row'))
                    .count()
                    .then(function(count) {
                        return count === expectedCount;
                    });
            }, 'expected primary page dropdown to have ' + expectedCount + ' options');
        },
        _getDropDownContent: function() {
            return displayConditionsPrimaryPage.elements.getAllOptions().then(function(options) {
                return protractor.promise.all(
                    options.map(function(e) {
                        return e.getText();
                    })
                );
            });
        },

        getFirstAvailableOptionText: function() {
            return getTrimmedTextFromElement(
                displayConditionsPrimaryPage.elements.getOptionByText('Primary Content Page')
            );
        },
        getSecondAvailableOptionText: function() {
            return getTrimmedTextFromElement(
                displayConditionsPrimaryPage.elements.getOptionByText(
                    'Some Other Primary Content Page'
                )
            );
        },
        getThirdAvailableOptionText: function() {
            return getTrimmedTextFromElement(
                displayConditionsPrimaryPage.elements.getOptionByText(
                    'Another Primary Content Page'
                )
            );
        },
        getNoAssociatedVariationPagesText: function() {
            return getTrimmedTextFromElement(element(by.css('.dc-no-variations')));
        },
        getAssociatedPrimaryPageText: function() {
            return getTrimmedTextFromElement(element(by.css('.dc-associated-primary-page')));
        },

        getElementContainingText: function(text) {
            return element(by.cssContainingText('se-message', text));
        }
    };

    pageObject.actions = {
        openSuccessfulEditPageModal: function() {
            return browser.click(pageObject.elements.getSuccessfulEditPageButton());
        },
        openFailEditPageModal: function() {
            return browser.click(pageObject.elements.getFailEditPageButton()).then(function() {
                return browser.waitUntilModalAppears();
            });
        },
        openPrimaryContentPageModal: function() {
            return browser.click(pageObject.elements.getPrimaryContentPageModalButton());
        },
        openVariationContentPageModal: function() {
            return browser.click(pageObject.elements.getVariationContentPageModalButton());
        },
        openPrimaryCategoryPageModal: function() {
            return browser.click(pageObject.elements.getPrimaryCategoryPageModalButton());
        },
        openVariationCategoryPageModal: function() {
            return browser.click(pageObject.elements.getVariationCategoryPageModalButton());
        },
        openPrimaryProductPageModal: function() {
            return browser.click(pageObject.elements.getPrimaryProductPageModalButton());
        },
        openVariationProductPageModal: function() {
            return browser.click(pageObject.elements.getVariationProductPageModalButton());
        },
        enterTextInNameField: function() {
            return browser.sendKeys(pageObject.elements.getNameField(), 'update label');
        },
        clearNameField: function() {
            var elem = pageObject.elements.getNameField();

            elem.clear();
            elem.sendKeys(
                'e',
                protractor.Key.CONTROL,
                'a',
                protractor.Key.NULL,
                protractor.Key.BACK_SPACE,
                protractor.Key.NULL
            );
        },

        clickSave: function() {
            return browser.click(pageObject.elements.getSaveButton());
        },
        clickCancel: function() {
            return browser.click(pageObject.elements.getCancelButton());
        },
        clickDisplayConditionsTab: function() {
            return browser.click(pageObject.elements.getDisplayConditionsTab());
        },
        openPrimaryPageSelectDropdown: function() {
            return displayConditionsPrimaryPage.actions.togglePrimaryPagesSelector();
        }
    };

    pageObject.assertions = {
        assertSaveButtonDisabled: function() {
            pageObject.utils.assertFieldIsDisabled(pageObject.elements.getSaveButton());
        },
        assertNameFieldDisabled: function() {
            pageObject.utils.assertFieldIsDisabled(pageObject.elements.getNameField());
        },
        assertReadOnlyMessageIsDisplayed: function(message) {
            expect(pageObject.elements.getElementContainingText(message).isPresent()).toBe(
                true,
                'Expected message "' + message + '" to be displayed'
            );
        },
        assertOnDropDownContent: function(contentArray) {
            var lastSeen;
            browser.waitUntil(
                function() {
                    return pageObject.elements._getDropDownContent().then(function(dropdownArray) {
                        return contentArray.every(function(element) {
                            return dropdownArray.indexOf(element) > -1;
                        });
                    });
                }.bind(this),
                'expected dropdown array to contain ' +
                    JSON.stringify(contentArray) +
                    'but got ' +
                    lastSeen
            );
        },

        assertLabelIsPresent: function(isPresent) {
            if (isPresent) {
                browser.waitForPresence(
                    pageObject.elements.getLabelField(),
                    'Expected label field to be present'
                );
            } else {
                browser.waitForAbsence(
                    pageObject.elements.getLabelField(),
                    'Expected label field not to be present'
                );
            }
        },

        assertUidFieldIsNotEnabled: function() {
            browser.waitForPresence(
                pageObject.elements.getUidField(),
                'Expected uid field to be present'
            );
            browser.waitUntil(function() {
                return pageObject.elements
                    .getUidField()
                    .isEnabled()
                    .then(function(isEnabled) {
                        return !isEnabled;
                    });
            }, 'Expected uid field not to be enabled');
        },

        assertOnSaveButtonEnabledStatus: function(expectedStatus) {
            browser.waitUntil(function() {
                return pageObject.elements
                    .getSaveButton()
                    .isEnabled()
                    .then(function(isEnabled) {
                        return expectedStatus === isEnabled;
                    });
            }, "'Expected save button to be in enabled status '" + expectedStatus + "'");
        }
    };

    pageObject.utils = {
        assertFieldIsDisabled: function(fieldElement) {
            fieldElement.getAttribute('disabled').then(function(isButtonDisabled) {
                expect(isButtonDisabled).toBe('true', 'Expected the button to be disabled.');
            });
        }
    };

    return pageObject;
})();
