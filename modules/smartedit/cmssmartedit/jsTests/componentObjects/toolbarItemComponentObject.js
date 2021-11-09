/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.elements = {
        getToolbarItemByNameSelector: function(name) {
            return element(by.cssContainingText('.toolbar-action-button__txt', name));
        }
    };

    componentObject.assertions = {
        assertToolbarItemByNameIsNotPresent: function(name) {
            browser.waitForAbsence(componentObject.elements.getToolbarItemByNameSelector(name));
        }
    };

    return componentObject;
})();
