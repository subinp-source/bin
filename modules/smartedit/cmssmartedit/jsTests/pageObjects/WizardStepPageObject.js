/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
module.exports = {
    // Elements
    listItemByText: function(text) {
        browser.waitUntil(
            EC.presenceOf(element(by.cssContainingText('.se-add-page__item', text))),
            'List item with text ' + text + ' was not found after 5000ms'
        );
        return element(by.cssContainingText('.se-add-page__item', text));
    },
    field: function(fieldId) {
        return element(by.id(fieldId));
    },
    localizedField: function(fieldId, language) {
        return element(by.css('[tab-id=' + language + '] #' + fieldId));
    },

    // Actions
    selectItem: function(itemTitle) {
        return browser.click(this.listItem(itemTitle));
    },
    selectItemByIndex: function(itemIndex) {
        return browser.click(this.listItemByIndex(itemIndex));
    },
    selectItemByText: function(itemText) {
        return browser.click(this.listItemByText(itemText));
    },
    enterFieldData: function(fieldId, value) {
        return browser.sendKeys(this.field(fieldId), value);
    },
    enterLocalizedFieldData: function(fieldId, language, value) {
        return browser.sendKeys(this.localizedField(fieldId, language), value);
    }
};
