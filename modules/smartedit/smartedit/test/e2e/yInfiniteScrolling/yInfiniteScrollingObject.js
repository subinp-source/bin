/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = {
    assertListOfItems: function(expectedOptions) {
        var itemsSelector = by.xpath(
            "//div[@class='infContainer']//div[contains(@class,'repeaterTemplate')]"
        );
        browser.waitUntil(function() {
            return element
                .all(itemsSelector)
                .map(function(element) {
                    return element.getText().then(
                        function(text) {
                            return text;
                        },
                        function() {
                            return '';
                        }
                    );
                })
                .then(function(actualOptions) {
                    return actualOptions.join(',') === expectedOptions.join(',');
                });
        }, 'cannot load items');
    },

    getItemsScrollElement: function() {
        return element(by.xpath("//div[@class='infContainer']//y-infinite-scrolling/div"));
    },

    searchItems: function(searchKey) {
        return element(by.xpath("//input[@name='srch-term']")).sendKeys(searchKey);
    }
};
