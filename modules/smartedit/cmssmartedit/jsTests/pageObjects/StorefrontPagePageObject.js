/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
module.exports = (function () {
    var storefrontPage = {};

    storefrontPage.elements = {};

    storefrontPage.actions = {
        setSelectedSiteId: function (siteId) {
            return browser.executeScript(
                'window.localStorage.setItem("seselectedsite", JSON.stringify(arguments[0]), false)',
                siteId
            );
        }
    };

    storefrontPage.assertions = {};

    return storefrontPage;
})();