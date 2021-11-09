/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Page workflow menu tasks - \n', function() {
    var perspective = e2e.componentObjects.modeSelector;
    var workflow = e2e.componentObjects.workflow;
    var typePermissions = e2e.componentObjects.typePermissions;
    var editor = e2e.componentObjects.genericEditor;
    var editorModal = e2e.componentObjects.editorModal;
    var alerts = e2e.componentObjects.alerts;

    beforeEach(function(done) {
        allWorkflowPermissions();
        workflow.utils.setNumberOfWorkflowsToReturn(1);
        workflow.utils.setWorkflowAvailableToCurrentUser(true);
        workflow.actions.navigateToPage(__dirname, done);
        perspective.selectAdvancedPerspective();
        workflow.actions.clickPageWorkflowTasksButton();
    });

    it('GIVEN page workflow menu is opened WHEN all tasks tab is selected THEN it has 4 actions available', function() {
        // WHEN
        workflow.actions.selectAllTasksTab();

        // THEN
        workflow.assertions.assertNumberOfActionsAvailable('allTasksTab', 4);
    });

    it('GIVEN not all actions are assigned to the current user WHEN current tasks tab is selected THEN it has all current tasks listed', function() {
        // THEN
        workflow.assertions.assertNumberOfActionsAvailable('currentTasksTab', 2);
        workflow.assertions.decisionButtonsAreDisplayed('currentTasksTab', 'Action2');
        workflow.assertions.decisionButtonsNotDisplayed('currentTasksTab', 'Action3');
    });

    it('GIVEN current tasks tab is selected WHEN "Action2" is expanded THEN it has 9 comments', function() {
        // THEN
        workflow.assertions.assertNumberOfCommentsAvailable('currentTasksTab', 'Action2', 9);
    });

    it('GIVEN all tasks tab is selected WHEN "Action3" is expanded THEN it has 2 comments', function() {
        // WHEN
        workflow.actions.selectAllTasksTab();

        // THEN
        workflow.assertions.assertNumberOfCommentsAvailable('allTasksTab', 'Action3', 2);
    });

    it('WHEN decision button is clicked THEN Should open modal form to making a decision', function() {
        // WHEN
        workflow.actions.clickDecisionButton('Action2', 'Approve', false);

        // THEN
        workflow.assertions.modalFormIsDispalyed();
    });

    it('WHEN make decision modal is opened THEN Should have comment and create version fields', function() {
        // WHEN
        workflow.actions.clickDecisionButton('Action2', 'Approve', false);

        // THEN
        workflow.assertions.commentFieldIsDisplayed();
        workflow.assertions.createVersionFieldIsDisplayed();
        workflow.assertions.versionLabelFieldIsAbsent();
    });

    it('WHEN create version checkbox is checked THEN Should prepopulate version label field', function() {
        // WHEN
        workflow.actions.clickDecisionButton('Action2', 'Approve', false);
        workflow.actions.clickCreatePageVersionButton();

        // THEN
        workflow.assertions.versionLabelFieldContainsText('Approve for Action2');
    });

    it('WHEN submit is clicked on decision modal THEN Should display a success alert', function() {
        // WHEN
        workflow.actions.clickDecisionButton('Action2', 'Approve', false);
        editor.actions.save();

        // THEN
        editorModal.assertions.assertSuccessAlertIsDisplayed();
    });

    it('WHEN workflow is ended THEN it should display the start button again', function() {
        // GIVEN
        workflow.actions.clickDecisionButton('Action2', 'Approve', false);
        editor.actions.save();

        alerts.actions.flush();

        workflow.actions.clickPageWorkflowTasksButton();

        // WHEN
        workflow.actions.clickDecisionButton('Action4', 'Approve', false);
        editor.actions.save();

        // THEN
        workflow.assertions.startWorkflowButtonIsDisplayed();
    });

    function allWorkflowPermissions() {
        typePermissions.setTypePermission('workflowTypePermissions', {
            read: true,
            change: true,
            create: true,
            remove: true
        });
    }
});
