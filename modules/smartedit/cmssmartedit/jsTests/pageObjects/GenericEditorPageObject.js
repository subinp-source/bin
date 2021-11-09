/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
module.exports = (function() {
    var genericEditorPage = {};

    genericEditorPage.elements = {
        getOpenGenericEditorButton: function() {
            return element(by.id('openEditorBtn'));
        }
    };

    genericEditorPage.actions = {
        openGenericEditor: function() {
            return browser.waitUntilNoModal().then(function() {
                return browser.click(genericEditorPage.elements.getOpenGenericEditorButton());
            });
        }
    };

    genericEditorPage.assertions = {};

    return genericEditorPage;
})();
