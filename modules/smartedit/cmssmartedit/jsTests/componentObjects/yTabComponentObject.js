/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.elements = {
        /**
         * Get tab by id
         * @param {String} id The id of the tab
         * @returns {ElementFinder|ElementArrayFinder|webdriver.WebElement}
         */
        getTabById: function(id) {
            return element(by.xpath('//se-tab[@tab-id="' + id + '"]'));
        }
    };

    return componentObject;
})();
