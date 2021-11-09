/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
var select;
if (typeof require !== 'undefined') {
    select = require('./SelectComponentObject');
}

module.exports = (function() {
    var componentObject = {};

    componentObject.constants = {
        SELECT_ID: 'display-conditions-primary-association-selector'
    };

    componentObject.elements = {
        getAssociatedPrimaryPageNameByText: function(text) {
            return element(by.cssContainingText('.dc-associated-primary-page', text));
        },
        getAssociatedPrimaryPageLabelByText: function(text) {
            return element(by.cssContainingText('.se-control-label', text));
        },
        getDropdownToggle: function() {
            return select.elements.getDropdownToggle(componentObject.constants.SELECT_ID);
        },
        getOptionByText: function(text) {
            return select.elements.getOptionByText(componentObject.constants.SELECT_ID, text);
        },
        getSelectedOption: function() {
            return select.elements.getSelectedOption(componentObject.constants.SELECT_ID);
        },
        getAllOptions: function() {
            return select.elements.getAllOptions(componentObject.constants.SELECT_ID);
        }
    };

    componentObject.actions = {
        togglePrimaryPagesSelector: function() {
            return select.actions.toggleSelector(componentObject.constants.SELECT_ID);
        },
        selectPrimaryPageByText: function(text) {
            return select.actions.selectOptionByText(componentObject.constants.SELECT_ID, text);
        }
    };

    componentObject.assertions = {
        associatedPrimaryPageNameContainsText: function(text) {
            return browser.waitForPresence(
                componentObject.elements.getAssociatedPrimaryPageNameByText(text)
            );
        },
        associatedPrimaryPageLabelContainsText: function(text) {
            return browser.waitForPresence(
                componentObject.elements.getAssociatedPrimaryPageLabelByText(text)
            );
        },
        selectedOptionContainsText: function(text) {
            select.assertions.selectedOptionContainsText(componentObject.constants.SELECT_ID, text);
        },
        optionContainsTextByIndex: function(text, index) {
            select.assertions.optionContainsTextByIndex(
                componentObject.constants.SELECT_ID,
                text,
                index
            );
        }
    };

    return componentObject;
})();
