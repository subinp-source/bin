module.exports = (function() {
    var pageObject = {};

    pageObject.elements = {
        getReadOnlyCheckbox: function() {
            return element(by.id('test-readOnly'));
        },
        getOnPrimaryPageSelectedCounter: function() {
            return element(by.id('test-onPrimaryPageSelectedCounter'));
        }
    };

    pageObject.actions = {
        clickReadOnlyCheckbox: function() {
            return browser.click(pageObject.elements.getReadOnlyCheckbox());
        }
    };

    pageObject.assertions = {
        onPrimaryPageSelectedHaveBeenCalled: function() {
            return browser.waitForSelectorToContainText(
                pageObject.elements.getOnPrimaryPageSelectedCounter(),
                '1'
            );
        }
    };

    return pageObject;
})();
