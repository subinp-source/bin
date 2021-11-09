/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var ID_PREFIX = 'fd-alert';
    function getSelectorStrByIndex(index) {
        return ID_PREFIX + ':nth-child' + '(' + (index + 1) + ')';
    }

    var locators = {
        allAlerts: function() {
            return by.css(ID_PREFIX);
        },

        alertByIndex: function(index) {
            return by.css(getSelectorStrByIndex(index));
        },

        closeButtonByIndex: function(index) {
            return by.css(getSelectorStrByIndex(index) + ' > .fd-alert__close');
        }
    };

    var alerts = {
        locators: locators
    };

    // ================ ACTIONS =================

    alerts.actions = {
        closeAlertByIndex: function(index) {
            browser.waitForPresence(element(locators.closeButtonByIndex(index)));
            return browser.click(element(locators.closeButtonByIndex(index)));
        },

        flush: function() {
            element
                .all(locators.allAlerts())
                .count()
                .then(function(count) {
                    for (var i = 0; i < count; i++) {
                        alerts.actions.closeAlertByIndex(0);
                    }
                    alerts.assertions.assertNoAlertsDisplayed();
                });
        }
    };

    // =============== ASSERTIONS ===============

    alerts.assertions = {
        assertAlertIsOfTypeByIndex: function(index, type) {
            browser.waitForPresence(
                locators.alertByIndex(index),
                'failed to find alert by index: ' + index
            );
            expect(locators.alertByIndex(index)).toContainClass('fd-alert--' + type);
        },

        assertAlertCloseabilityByIndex: function(index, closeable) {
            if (closeable) {
                browser.waitToBeDisplayed(locators.closeButtonByIndex(index));
            } else {
                browser.waitForAbsence(locators.closeButtonByIndex(index));
            }
        },

        assertAlertMessageByIndex: function(index, expectedText) {
            browser.waitForPresence(
                locators.alertByIndex(index),
                'failed to find alert message by index: ' + index
            );
            expect(element(locators.alertByIndex(index)).getText()).toContain(expectedText);
        },

        assertNoAlertsDisplayed: function() {
            browser.waitForAbsence(locators.alertByIndex(0));
        },

        assertTotalNumberOfAlerts: function(numExpectedAlerts) {
            browser.waitUntil(function() {
                return element
                    .all(locators.allAlerts())
                    .count()
                    .then(function(count) {
                        return count === numExpectedAlerts;
                    });
            }, 'was expecting to see ' + numExpectedAlerts + ' alerts');
        }
    };

    return alerts;
})();
