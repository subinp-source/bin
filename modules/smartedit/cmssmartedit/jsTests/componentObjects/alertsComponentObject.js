/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
// ====================================================================
// ====================================================================
// This is a copy paste of the one from smartedit :(
// We have ticket in the backlog to make smartedit components available
// to cmsSmartedit tests, but until that time....
// ====================================================================
// ====================================================================

module.exports = (function() {
    var ID_PREFIX = 'fd-alert';
    function getSelectorByIndex(index) {
        return ID_PREFIX + ':nth-child' + '(' + (index + 1) + ')';
    }

    var locators = {
        allAlerts: function() {
            return by.css(ID_PREFIX);
        },

        alertByIndex: function(index) {
            return by.css(getSelectorByIndex(index));
        },

        closeButtonByIndex: function(index) {
            return by.css(getSelectorByIndex(index) + ' .fd-alert__close');
        }
    };

    var alerts = {};

    // ================ ACTIONS =================

    alerts.actions = {
        closeAlertByIndex: function(index) {
            browser.waitForPresence(locators.closeButtonByIndex(index));
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
            expect(element.all(locators.allAlerts()).count()).toBe(numExpectedAlerts);
        }
    };

    return alerts;
})();
