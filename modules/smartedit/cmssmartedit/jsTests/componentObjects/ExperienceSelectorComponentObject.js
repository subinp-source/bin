/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var experienceSelectorObject = {};

    experienceSelectorObject.assertions = {
        contentCatalogDropdownIsEditabled: function() {
            expect(
                experienceSelectorObject.elements.contentCatalogDropdown().getAttribute('disabled')
            ).toBeFalsy();
        },
        contentCatalogDropdownIsNotEditabled: function() {
            expect(
                experienceSelectorObject.elements.contentCatalogDropdown().getAttribute('disabled')
            ).toBeTruthy();
        },
        experienceSelectorButtonHasText: function(text) {
            expect(experienceSelectorObject.elements.experienceSelectorButtonText()).toContain(
                text
            );
        }
    };

    experienceSelectorObject.actions = {
        openExperienceSelector: function() {
            return browser.waitUntilNoModal().then(function() {
                return browser
                    .click(experienceSelectorObject.elements.experienceSelectorButtonSelector())
                    .then(function() {
                        browser.waitUntil(
                            EC.presenceOf(
                                experienceSelectorObject.elements.contentCatalogDropdown()
                            ),
                            'Expected modal to be opened'
                        );
                    });
            });
        }
    };

    experienceSelectorObject.elements = {
        experienceSelectorButtonSelector: function() {
            return by.id('experience-selector-btn', 'Experience Selector button not found');
        },
        submitButtonSelector: function() {
            return by.id('submit', 'Experience Selector Submit Button not found');
        },
        cancelButtonSelector: function() {
            return by.id('cancel', 'Experience Selector Cancel Button not found');
        },
        contentCatalogDropdown: function() {
            return element(by.css('div[id="previewCatalog"] .se-generic-editor-dropdown'));
        },
        experienceSelectorButtonText: function() {
            return element(
                by.css("[class*='se-experience-selector__btn-text ']", 'Selector widget not found')
            ).getText();
        }
    };

    return experienceSelectorObject;
})();
