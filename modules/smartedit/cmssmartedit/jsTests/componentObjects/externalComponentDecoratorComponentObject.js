/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.elements = {
        getExternalComponentDecorators: function() {
            return element.all(by.css('.externalComponentDecorator'));
        },
        getExternalComponentDecoratorByComponentId: function(componentId) {
            return element(
                by.css(
                    '[data-smartedit-component-id="' +
                        componentId +
                        '"] .externalComponentDecorator'
                )
            );
        }
    };

    componentObject.assertions = {
        externalComponentDecoratorsCount: function(expectedCount) {
            browser.waitUntil(function() {
                return browser.switchToIFrame().then(function() {
                    return componentObject.elements
                        .getExternalComponentDecorators()
                        .count()
                        .then(function(actualCount) {
                            return actualCount === expectedCount;
                        });
                });
            }, 'was expecting ' + expectedCount + ' external component decorators');
        }
    };

    return componentObject;
})();
