/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.assertions = {};

    componentObject.actions = {
        selectLabel: function(selectorOfTheDropDownparent, optionLabel) {
            return browser
                .click(by.css(selectorOfTheDropDownparent + ' [data-uib-dropdown-toggle]'))
                .then(function() {
                    return browser.click(
                        by.cssContainingText(
                            selectorOfTheDropDownparent + ' ul[data-uib-dropdown-menu] li a',
                            optionLabel
                        )
                    );
                });
        },
        selectDropdownItemByText: function(dropdownEl, text) {
            return browser.click(dropdownEl).then(function() {
                var dropdownItem = componentObject.elements.getDropdownItemByText(dropdownEl, text);
                return browser.click(dropdownItem);
            });
        },
        openDropdown: function(dropdownEl) {
            return browser.click(dropdownEl);
        }
    };

    componentObject.elements = {
        getDropdownItems: function(dropdownEl) {
            componentObject.actions.openDropdown(dropdownEl);
            return dropdownEl.all(by.css('li[role="option"]'));
        },
        getDropdownItemByText: function(dropdownEl, text) {
            return dropdownEl.element(by.cssContainingText('span', text));
        }
    };

    return componentObject;
})();
