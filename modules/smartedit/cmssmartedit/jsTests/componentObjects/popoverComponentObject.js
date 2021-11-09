/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObjects = {
        assertions: {
            isDisplayedWithProvidedText: function(expectedText) {
                expect(componentObjects.elements.getPopover()).toBeDisplayed();
                expect(componentObjects.elements.getPopover().getText()).toBe(expectedText);
            }
        },

        elements: {
            getPopover: function() {
                return browser.findElement(by.css('.popover'));
            }
        }
    };

    return componentObjects;
})();
