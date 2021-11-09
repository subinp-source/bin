/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
function PageObject() {
    this.DEFAULT_ALERT_WAIT = 5000;
}

PageObject.prototype.dismissAlert = function() {
    var selector = '.se-confirmation-dialog #confirmCancel';

    browser.waitForPresence(element(by.css(selector)));
    element(by.css(selector)).click();
};

PageObject.prototype.acceptAlert = function() {
    var selector = '.se-confirmation-dialog #confirmOk';

    browser.waitForPresence(element(by.css(selector)));
    element(by.css(selector)).click();
};

module.exports = PageObject;
