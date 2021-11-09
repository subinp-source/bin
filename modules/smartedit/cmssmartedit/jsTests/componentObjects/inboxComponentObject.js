/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.consts = {};

    componentObject.elements = {
        getInboxButton: function() {
            return element(by.css('.se-workflow-inbox-toggle'));
        },
        getInboxTasks: function() {
            return element.all(by.css('.se-cms-dev-workflow-inbox-task'));
        },
        getInboxTask: function(taskNumber) {
            return element(by.id('workflow-inbox-task-' + taskNumber));
        },
        getInboxTaskHeader: function(taskNumber) {
            return componentObject.elements
                .getInboxTask(taskNumber)
                .element(by.css('.se-cms-dev-workflow-inbox-task-header'));
        },
        getInboxTaskDescription: function(taskNumber) {
            return componentObject.elements
                .getInboxTask(taskNumber)
                .element(by.css('.se-cms-dev-workflow-inbox-task-desc'));
        },
        getInboxTaskCreatedAgo: function(taskNumber) {
            return componentObject.elements
                .getInboxTask(taskNumber)
                .element(by.css('.se-cms-dev-workflow-inbox-task-body'));
        },
        getInboxTaskHeaderLink: function(taskNumber) {
            return componentObject.elements
                .getInboxTask(taskNumber)
                .element(by.css('.se-cms-dev-workflow-inbox-task-header a'));
        },
        getInboxBadgeCount: function() {
            return element(by.css('.se-inbox-badge'));
        }
    };

    componentObject.actions = {
        openInbox: function() {
            return browser.waitUntilNoModal().then(function() {
                return browser.click(componentObject.elements.getInboxButton());
            });
        },
        selectTask: function(taskNumber) {
            return browser
                .click(componentObject.elements.getInboxTaskHeaderLink(taskNumber))
                .then(function() {
                    browser.waitForWholeAppToBeReady();
                });
        },
        navigateToPage: function(dirname, done) {
            return browser.bootstrap(dirname).then(function() {
                browser.waitForWholeAppToBeReady().then(function() {
                    done();
                });
            });
        }
    };

    componentObject.assertions = {
        inboxHasTasks: function(expectedTasksCount) {
            componentObject.elements.getInboxTasks().then(function(tasks) {
                expect(tasks.length).toBe(expectedTasksCount);
            });
        },
        inboxTaskHasName: function(taskNumber, name) {
            expect(componentObject.elements.getInboxTaskHeader(taskNumber).getText()).toContain(
                name
            );
        },
        inboxTaskHasDescription: function(taskNumber, description) {
            expect(
                componentObject.elements.getInboxTaskDescription(taskNumber).getText()
            ).toContain(description);
        },
        inboxTaskHasCreatedAgo: function(taskNumber, createdAgo) {
            expect(componentObject.elements.getInboxTaskCreatedAgo(taskNumber).getText()).toContain(
                createdAgo
            );
        },
        inboxBadgeHasUpdated: function(expectedTasksCount) {
            expect(componentObject.elements.getInboxBadgeCount().getText()).toEqual(
                expectedTasksCount
            );
        }
    };

    componentObject.utils = {
        updateInboxTasks: function(canUpdateInboxTasks) {
            return browser.executeScript(
                'window.sessionStorage.setItem("updateWorkflowInbox", arguments[0])',
                canUpdateInboxTasks
            );
        }
    };

    return componentObject;
})();
