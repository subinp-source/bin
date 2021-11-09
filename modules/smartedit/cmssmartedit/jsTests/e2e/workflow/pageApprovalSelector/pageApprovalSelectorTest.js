/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Page approval selector - \n', function() {
    var perspective = e2e.componentObjects.modeSelector;
    var pageApprovalSelector = e2e.componentObjects.pageApprovalSelector;
    var attributePermissions = e2e.componentObjects.attributePermissions;
    var workflow = e2e.componentObjects.workflow;
    var editor = e2e.componentObjects.genericEditor;
    var alerts = e2e.componentObjects.alerts;

    beforeEach(function() {
        makePageDraft();
    });

    describe('No page approval status CHANGE permission - ', function() {
        beforeEach(function(done) {
            removeActiveWorkflow(true);
            denyChangePermissionToApprovalStatus();
            navigateToPage(done);
        });

        it('GIVEN basic perspective AND current user has no attribute permission on page approval WHEN page is loaded THEN no page approval selector is shown', function() {
            // WHEN
            perspective.selectBasicPerspective();

            // THEN
            pageApprovalSelector.assertions.pageApprovalSelectorIsNotDisplayed();
        });

        it('GIVEN advanced perspective AND current user has no attribute permission on page approval WHEN page is loaded THEN no page approval selector is shown', function() {
            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            pageApprovalSelector.assertions.pageApprovalSelectorIsNotDisplayed();
        });
    });

    describe('With page approval status CHANGE permission AND no active workflow - ', function() {
        beforeEach(function(done) {
            removeActiveWorkflow(true);
            grantChangePermissionToApprovalStatus();
            navigateToPage(done);
        });

        it('GIVEN basic perspective AND user has attribute permissions on page approval WHEN page is loaded THEN the page approval selector is shown', function() {
            // WHEN
            perspective.selectBasicPerspective();

            // THEN
            pageApprovalSelector.assertions.pageApprovalSelectorIsDisplayed();
        });

        it('GIVEN advanced perspective AND user has attribute permissions on page approval WHEN page is loaded THEN the page approval selector is shown', function() {
            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            pageApprovalSelector.assertions.pageApprovalSelectorIsDisplayed();
        });

        it('GIVEN page is in a different state WHEN user selects draft state THEN the page is updated and the page is reloaded with the new status', function() {
            // GIVEN
            perspective.selectBasicPerspective();

            // WHEN
            pageApprovalSelector.actions.forceReadyToSyncPageApprovalStatus();

            // THEN
            pageApprovalSelector.actions.openPageApprovalDropdown();
            pageApprovalSelector.assertions.hasDraftOption();
        });
    });

    describe('With page approval status CHANGE permission AND active workflow - ', function() {
        beforeEach(function(done) {
            removeActiveWorkflow(false);
            grantChangePermissionToApprovalStatus();
            navigateToPage(done);
        });

        it('GIVEN existing workflow WHEN page loads THEN it does not show the page approval selector', function() {
            // WHEN
            perspective.selectBasicPerspective();

            // THEN
            pageApprovalSelector.assertions.pageApprovalSelectorIsNotDisplayed();
        });

        it('GIVEN existing workflow WHEN workflow is completed THEN the page approval selector appears', function() {
            // GIVEN
            perspective.selectBasicPerspective();

            // WHEN
            makeDecision('Action2', 'Approve');
            makeDecision('Action4', 'Approve');

            // THEN
            pageApprovalSelector.assertions.pageApprovalSelectorIsDisplayed();
        });
    });

    function navigateToPage(done) {
        return browser.bootstrap(__dirname).then(function() {
            browser.waitForWholeAppToBeReady().then(function() {
                done();
            });
        });
    }

    function grantChangePermissionToApprovalStatus() {
        attributePermissions.setPermissionsToAttributeInType('ContentPage', 'approvalStatus', {
            read: true,
            change: true
        });
    }

    function denyChangePermissionToApprovalStatus() {
        attributePermissions.setPermissionsToAttributeInType('ContentPage', 'approvalStatus', {
            read: true,
            change: false
        });
    }

    function makeDecision(actionName, decisionName) {
        workflow.actions.clickPageWorkflowTasksButton();
        workflow.actions.clickDecisionButton(actionName, decisionName, false);
        editor.actions.save();

        alerts.actions.flush();
    }

    function removeActiveWorkflow(removeWorkflow) {
        browser.executeScript(
            'window.sessionStorage.setItem("removeActiveWorkflow", ' + removeWorkflow + ')'
        );
    }

    function makePageDraft() {
        var pageUpdates = {
            homepage: {
                approvalStatus: 'CHECKED',
                displayStatus: 'DRAFT'
            }
        };

        browser.executeScript(
            'window.sessionStorage.setItem("updatePageApprovalStatus", arguments[0])',
            JSON.stringify(pageUpdates)
        );
    }
});
