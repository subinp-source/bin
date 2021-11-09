/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */

var genericEditorCommons;
if (typeof require !== 'undefined') {
    genericEditorCommons = require('./commonGenericEditorComponentObject');
}

module.exports = (function() {
    var componentObject = {};

    componentObject.constants = {};

    componentObject.elements = {
        getTextField: function(fieldQualifier) {
            var field = genericEditorCommons.elements.getFieldByQualifier(fieldQualifier);
            browser.waitForPresence(field);
            var input = field.element(by.css('input[type=text]'));
            browser.waitForPresence(input);
            return input;
        },
        getBooleanField: function(fieldQualifier) {
            var field = genericEditorCommons.elements.getFieldByQualifier(fieldQualifier);
            browser.waitForPresence(field);
            var input = field.element(by.css('input'));
            browser.waitForPresence(input);
            return input;
        }
    };

    componentObject.actions = {
        // -- Text Widget --
        setTextFieldValue: function(fieldQualifier, value) {
            return componentObject.elements
                .getTextField(fieldQualifier)
                .clear()
                .sendKeys(value);
        }
    };

    componentObject.assertions = {
        // -- Text Widget --
        textFieldHasRightValue: function(fieldQualifier, expectedText) {
            expect(
                componentObject.elements.getTextField(fieldQualifier).getAttribute('value')
            ).toBe(
                expectedText,
                'Expected field ' + fieldQualifier + ' to have the following text: ' + expectedText
            );
        },

        // -- Boolean Widget --
        booleanFieldHasRightValue: function(fieldQualifier, expectedValue) {
            expect(componentObject.elements.getBooleanField(fieldQualifier).isSelected()).toBe(
                expectedValue,
                'Expected field ' + fieldQualifier + ' to be ' + expectedValue
            );
        }
    };

    componentObject.utils = {};

    return componentObject;
})();
