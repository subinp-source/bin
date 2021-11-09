/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var PageObject = {
        actions: {
            getAndWaitForWholeApp: function(url) {
                return browser
                    .get(url)
                    .then(function() {
                        return browser.waitForWholeAppToBeReady();
                    })
                    .then(function() {
                        return browser.waitUntilNoModal();
                    });
            },
            getAndWaitForLogin: function(url) {
                return browser.get(url).then(
                    function() {
                        browser.clearLocalStorage();
                        browser.waitForAngularEnabled(false);
                        return this.waitForLoginModal();
                    }.bind(this)
                );
            },
            waitForLoginModal: function() {
                return browser
                    .waitUntil(
                        protractor.ExpectedConditions.elementToBeClickable(
                            element(by.css('input[id^="username_"]'))
                        ),
                        'Timed out waiting for username input'
                    )
                    .then(function() {
                        return browser.waitForAngular();
                    });
            },
            setWaitForPresence: function(implicitWait) {
                return browser.driver
                    .manage()
                    .timeouts()
                    .implicitlyWait(implicitWait);
            },
            clearCookies: function() {
                return browser.waitUntil(function() {
                    return browser.driver
                        .manage()
                        .deleteAllCookies()
                        .then(
                            function() {
                                return true;
                            },
                            function(err) {
                                throw err;
                            }
                        );
                }, 'Timed out waiting for cookies to clear');
            }
        },

        assertions: {},

        constants: {},

        elements: {}
    };

    return PageObject;
})();
