/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false */
function UiSelectPageObject(element) {
    this.getSelectToggle = function() {
        return element.find('.ui-select-toggle');
    };

    this.clickSelectToggle = function() {
        this.getSelectToggle().click();
    };

    this.getSelectedElement = function() {
        return element.find('.select2-chosen');
    };

    this.getSelectElement = function(index) {
        return window.smarteditJQuery(element.find('.ui-select-choices-row')[index]);
    };

    this.clickSelectElement = function(index) {
        this.getSelectElement(index).click();
    };

    // ---assertions

    this.assertNumberElements = function(expectedElementCount) {
        expect(element.find('.ui-select-choices-row').length).toBe(expectedElementCount);
    };

    this.assertElementTextEquals = function(index, text) {
        expect(
            this.getSelectElement(index)
                .text()
                .trim()
        ).toBe(text);
    };

    this.assertElementInList = function(itemText) {
        expect(element.find('.ui-select-choices-row:contains("' + itemText + '")').length).toBe(1);
    };

    this.assertElementNotInList = function(itemText) {
        expect(element.find('.ui-select-choices-row:contains("' + itemText + '")').length).toBe(0);
    };
}
