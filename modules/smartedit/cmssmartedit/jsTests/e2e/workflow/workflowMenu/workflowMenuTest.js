/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Page workflow menu - \n', function() {
    var perspective = e2e.componentObjects.modeSelector;
    var workflow = e2e.componentObjects.workflow;
    var typePermissions = e2e.componentObjects.typePermissions;
    var attributePermissions = e2e.componentObjects.attributePermissions;

    describe('Do not consider type permissions', function() {
        beforeEach(function(done) {
            attributePermissions.setPermissionsToAttributeInType('Workflow', 'status', {
                read: true,
                change: true
            });
            attributePermissions.setPermissionsToAttributeInType('Workflow', 'description', {
                read: true,
                change: true
            });

            allWorkflowPermissions();
            workflow.utils.setNumberOfWorkflowsToReturn(1);
            workflow.actions.navigateToPage(__dirname, done);
            perspective.selectAdvancedPerspective();
        });

        it('GIVEN workflow is started THEN should show page tasks button', function() {
            // WHEN
            workflow.actions.hoverOverPageWorkflowTasksButton();

            // THEN
            workflow.assertions.workflowTasksButtonIsDisplayed();
        });

        it('Should show menu panel after clicking on Page Tasks', function() {
            // WHEN
            workflow.actions.clickPageWorkflowTasksButton();

            // THEN
            workflow.assertions.menuHeaderContainsText(workflow.consts.PAGE_WORKFLOW_MENU_TITLE);
        });

        it('GIVEN Page Tasks page open WHEN click on MORE button THEN shows item menu that contains workflow description and cancel', function() {
            // GIVEN
            workflow.actions.clickPageWorkflowTasksButton();

            // WHEN
            workflow.actions.clickItemMenuMoreButton();

            // THEN
            workflow.assertions.itemMenuContainsWorkflowDescriptionHeader();
            workflow.assertions.itemMenuContainsWorkflowCancel();
        });

        it('GIVEN Page Tasks page open AND no permissions for workflow status and description attributes WHEN click on MORE button THEN item menu does not contain workflow description and cancel', function() {
            // GIVEN
            attributePermissions.setPermissionsToAttributeInType('Workflow', 'status', {
                read: true,
                change: false
            });
            attributePermissions.setPermissionsToAttributeInType('Workflow', 'description', {
                read: true,
                change: false
            });
            workflow.actions.clickPageWorkflowTasksButton();

            // WHEN
            workflow.actions.clickItemMenuMoreButton();

            // THEN
            workflow.assertions.cancelButtonIsAbsent();
            workflow.assertions.editIconIsAbsent();
        });

        it('GIVEN opened item menu WHEN click on edit button THEN show edit form', function() {
            // GIVEN
            workflow.actions.clickPageWorkflowTasksButton();
            workflow.actions.clickItemMenuMoreButton();

            // WHEN
            workflow.actions.clickEditMenuButton();

            // THEN
            workflow.assertions.itemEditModalFormContainsDescriptionField();
        });

        it('GIVEN opened item menu WHEN click on cancel button THEN show cancel confirmation form', function() {
            // GIVEN
            workflow.actions.clickPageWorkflowTasksButton();
            workflow.actions.clickItemMenuMoreButton();

            // WHEN
            workflow.actions.clickCancelMenuButton();

            // THEN
            workflow.assertions.itemCancelConfirmationDisplayed();
        });
    });

    describe('Consider type permissions', function() {
        beforeEach(function(done) {
            noWorkflowEditPermission();
            workflow.utils.setNumberOfWorkflowsToReturn(1);
            workflow.actions.navigateToPage(__dirname, done);
            perspective.selectAdvancedPerspective();
        });

        it('GIVEN opened item menu AND user does not have cancel permission WHEN show more menu THEN not cancel button', function() {
            // GIVEN
            workflow.actions.clickPageWorkflowTasksButton();
            workflow.actions.clickItemMenuMoreButton();

            // THEN
            workflow.assertions.cancelButtonIsAbsent();
        });

        it('GIVEN opened item menu AND user does not have edit permission WHEN show more menu THEN not edit button', function() {
            // GIVEN
            workflow.actions.clickPageWorkflowTasksButton();
            workflow.actions.clickItemMenuMoreButton();

            // THEN
            workflow.assertions.editIconIsAbsent();
        });
    });

    function noWorkflowEditPermission() {
        typePermissions.setTypePermission('workflowTypePermissions', {
            read: true,
            change: false,
            create: false,
            remove: false
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
