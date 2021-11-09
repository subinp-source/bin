/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
module.exports = {
    get: function(url) {
        return browser.get(url);
    },
    getAndWaitForWholeApp: function(url) {
        return this.get(url).then(function() {
            return browser.waitForWholeAppToBeReady();
        });
    },
    getAndWaitForLogin: function(url) {
        return this.get(url).then(
            function() {
                this.clearCookies().then(
                    function() {
                        this.waitForLoginModal();
                    }.bind(this)
                );
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
        browser.driver
            .manage()
            .timeouts()
            .implicitlyWait(implicitWait);
    },
    dumpAllLogsToConsole: function() {
        if (global.waitForSprintDemoLogTime !== null && global.waitForSprintDemoLogTime > 0) {
            // List logs
            var logs = browser.driver.manage().logs(),
                logType = 'browser'; // browser
            logs.getAvailableLogTypes().then(function(logTypes) {
                if (logTypes.indexOf(logType) > -1) {
                    browser.driver
                        .manage()
                        .logs()
                        .get(logType)
                        .then(function(logsEntries) {
                            var len = logsEntries.length;
                            for (var i = 0; i < len; ++i) {
                                var logEntry = logsEntries[i];
                                var showLog = hasLogLevel(
                                    logEntry.level.name,
                                    global.sprintDemoLogLevels
                                );
                                if (showLog) {
                                    waitForSprintDemo(global.waitForSprintDemoLogTime);
                                    try {
                                        var msg = JSON.parse(logEntry.message);
                                        console.log(msg.message.text);
                                    } catch (err) {
                                        if (global.sprintDemoShowLogParsingErrors) {
                                            console.log('Error parsing log:  ' + logEntry.message);
                                        }
                                    }
                                }
                            }
                        }, null);
                }
            });
        }
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
};
