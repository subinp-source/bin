/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
function PageObject() {}

PageObject.prototype.get = function(url) {
    browser.driver.manage().deleteAllCookies();
    return browser.get(url);
};
PageObject.prototype.getAndWaitForWholeApp = function(url) {
    return this.get(url).then(function() {
        return browser.waitForWholeAppToBeReady();
    });
};
PageObject.prototype.getAndWaitForLogin = function(url) {
    return this.get(url).then(
        function() {
            return this.clearCookies().then(
                function() {
                    return this.waitForLoginModal();
                }.bind(this)
            );
        }.bind(this)
    );
};

PageObject.prototype.runWithExplicitWait = function(fn, millis) {
    browser.driver
        .manage()
        .timeouts()
        .implicitlyWait(millis);
    try {
        fn();
    } catch (err) {
        throw err;
    } finally {
        browser.driver
            .manage()
            .timeouts()
            .implicitlyWait(browser.params.implicitWait);
    }
};

PageObject.prototype.goToSecondStorefrontPage = function() {
    return browser
        .switchToIFrame()
        .then(function() {
            return browser.click(
                element(by.id('deepLink')),
                'Timed out waiting for deep link to be clickable'
            );
        })
        .then(function() {
            return browser.switchToParent();
        })
        .then(function() {
            return browser.waitForWholeAppToBeReady();
        });
};

PageObject.prototype.dismissAlert = function() {
    browser.waitUntil(protractor.ExpectedConditions.alertIsPresent());
    browser
        .switchTo()
        .alert()
        .dismiss();
};

PageObject.prototype.acceptAlert = function() {
    browser.waitUntil(protractor.ExpectedConditions.alertIsPresent());
    browser
        .switchTo()
        .alert()
        .accept();
};

PageObject.prototype.open = function() {
    browser.get(this.pageURI);
};

module.exports = PageObject;
