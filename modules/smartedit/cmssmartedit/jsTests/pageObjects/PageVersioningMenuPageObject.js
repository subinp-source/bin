/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var pageVersioningMenu = {};

    pageVersioningMenu.constants = {};

    pageVersioningMenu.elements = {};

    pageVersioningMenu.actions = {
        navigateToPage: function(pageHasEmptyVersionList, dirname, done) {
            return browser.bootstrap(dirname).then(function() {
                browser.executeScript(
                    'window.sessionStorage.setItem("emptyVersionList", arguments[0])',
                    !!pageHasEmptyVersionList
                );
                browser.waitForWholeAppToBeReady().then(function() {
                    done();
                });
            });
        }
    };

    pageVersioningMenu.assertions = {};

    return pageVersioningMenu;
})();
