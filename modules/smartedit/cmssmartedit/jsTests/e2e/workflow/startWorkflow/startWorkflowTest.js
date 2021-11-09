/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Start Workflow Test - \n', function() {
    var perspective = e2e.componentObjects.modeSelector;
    var workflow = e2e.componentObjects.workflow;
    var typePermissions = e2e.componentObjects.typePermissions;

    describe('Do not consider type permissions', function() {
        beforeEach(function(done) {
            workflow.utils.setNumberOfWorkflowsToReturn(0);
            allWorkflowPermissions();
            workflow.actions.navigateToPage(__dirname, done);
            perspective.selectAdvancedPerspective();
        });

        it('Should open modal form to start workflow when clicks on start workflow button', function() {
            // WHEN
            workflow.actions.clickStartWorkflowButton();

            // THEN
            workflow.assertions.modalFormIsDispalyed();
        });

        it('Should display three default fields on modal form and not display version name field', function() {
            // WHEN
            workflow.actions.clickStartWorkflowButton();

            // THEN
            workflow.assertions.templateFieldIsDisplayed();
            workflow.assertions.descriptionFieldIsDisplayed();
            workflow.assertions.createVersionFieldIsDisplayed();
            workflow.assertions.versionLabelFieldIsAbsent();
        });

        it('Should display version name field if create version checkbox is checked', function() {
            // GIVEN
            workflow.actions.clickStartWorkflowButton();
            workflow.actions.clickCreatePageVersionButton();

            // THEN
            workflow.assertions.versionLabelFieldIsDisplayed();
        });

        it('Should prepopulate version label using template name WHEN workflow template is selected AND create version checkbox is checked', function() {
            // GIVEN
            workflow.actions.clickStartWorkflowButton();

            // WHEN
            workflow.actions.clickCreatePageVersionButton();
            workflow.actions.selectWorkflowTemplageByname('Page Approval');

            // THEN
            workflow.assertions.versionLabelFieldContainsText('Page Approval workflow started');
        });
    });

    describe('Create type permission is absent', function() {
        beforeEach(function(done) {
            workflow.utils.setNumberOfWorkflowsToReturn(0);
            noWorkflowCreatePermission();
            workflow.actions.navigateToPage(__dirname, done);
        });
        it('GIVEN user without CREATE Workflow permission WHEN Advanced Perspective is selected THEN start workflow button is absent', function() {
            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            workflow.assertions.startWorkflowButtonIsAbsent();
        });
    });

    describe('Create type permission exists', function() {
        beforeEach(function(done) {
            workflow.utils.setNumberOfWorkflowsToReturn(0);
            allWorkflowPermissions();
            workflow.actions.navigateToPage(__dirname, done);
        });
        it('GIVEN user with CREATE Workflow permission WHEN Advanced Perspective is selected THEN start workflow button is displayed', function() {
            // WHEN
            perspective.selectAdvancedPerspective();

            // THEN
            workflow.assertions.startWorkflowButtonIsDisplayed();
        });
    });

    function noWorkflowCreatePermission() {
        typePermissions.setTypePermission('workflowTypePermissions', {
            read: true,
            change: true,
            create: false,
            remove: true
        });
    }

    function allWorkflowPermissions() {
        typePermissions.setTypePermission('workflowTypePermissions', {
            read: true,
            change: true,
            create: true,
            remove: true
        });
    }
});
