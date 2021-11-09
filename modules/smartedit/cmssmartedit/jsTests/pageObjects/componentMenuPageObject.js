/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
module.exports = (function() {
    var pageObject = {};

    pageObject.constants = {};

    pageObject.actions = {
        navigateToPage: function(dirName) {
            return browser.bootstrap(dirName).then(function() {
                return browser.waitForWholeAppToBeReady();
            });
        }
    };

    return pageObject;
})();
