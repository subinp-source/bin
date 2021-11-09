/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.consts = {
        PAGE_WORKFLOW_MENU_TITLE: 'Page Tasks',
        ITEM_MENU_WORKFLOW_DESCRIPTION_HEADER: 'Workflow Description',
        ITEM_MENU_WORKFLOW_CANCEL_BUTTON: 'Cancel Workflow',
        ITEM_MENU_WORKFLOW_DESCRIPTION_FIELD: 'Description',
        ITEM_MENU_WORKFLOW_CANCEL_CONFIRMATION_MESSAGE:
            'When canceling workflow, the page will be moved back to Draft'
    };

    componentObject.elements = {
        // Generic elements
        getGenericEditorFieldSelector: function(qualifier) {
            return by.css('div[data-cms-field-qualifier="' + qualifier + '"]');
        },
        getGenericEditorFieldLabelSelector: function(qualifier) {
            return by.id(qualifier + '-label');
        },
        getGenericEditorFieldLabel: function(qualifier) {
            return element(this.getGenericEditorFieldLabelSelector(qualifier));
        },
        getModalForm: function() {
            return element(by.css('.fd-modal__content'));
        },
        getConfirmationModal: function() {
            return element(by.css('.se-confirmation-dialog'));
        },
        getStartWorkflowButton: function() {
            return element(by.id('smartEditPerspectiveToolbar_option_se.cms.startWorkflow_btn'));
        },

        // Workflow template field
        getTemplateFieldSelector: function() {
            return this.getGenericEditorFieldSelector('templateCode');
        },
        getTemplateField: function() {
            return element(this.getTemplateFieldSelector());
        },
        getTemplateFieldOptionByName: function(name) {
            return this.getTemplateField().element(
                by.cssContainingText('.select2-result-single li div div', name)
            );
        },

        // Workflow description field
        getDescriptionField: function() {
            return element(this.getGenericEditorFieldSelector('description'));
        },

        // Workflow version field
        getCreateVersionField: function() {
            return element(this.getGenericEditorFieldSelector('createVersion'));
        },
        getCreateVersionFieldLabel: function() {
            return element(by.css('workflow-create-version-field'));
        },
        getCreateVersionCheckboxField: function() {
            return this.getCreateVersionField().element(by.css("input[type='checkbox']"));
        },
        getVersionLabelField: function() {
            return element(by.css('input[name="versionLabel"]'));
        },

        // Page workflow menu
        getPageWorkflowMenu: function() {
            return element(by.css('page-workflow-menu'));
        },
        getPageTasksButton: function() {
            return element(by.css('.sap-icon--workflow-tasks'));
        },
        getPageWorkflowMenuHeader: function() {
            return this.getPageWorkflowMenu().element(
                by.css('.se-page-workflow-menu__header-text')
            );
        },

        // Workflow item menu more button
        getItemMenuMoreButton: function() {
            return element(by.css('workflow-item-menu'));
        },

        // Workflow cancel/edit button
        getItemMenuCancelItem: function() {
            return element(
                by.cssContainingText(
                    'div[id="cancel-workflow-item-menu"]',
                    componentObject.consts.ITEM_MENU_WORKFLOW_CANCEL_BUTTON
                )
            );
        },
        getItemMenuEditItem: function() {
            return element(by.id('edit-workflow-item-menu'));
        },

        // Workflow description header
        getItemMenuDescriptionHeader: function() {
            return element(
                by.cssContainingText(
                    'div[id="workflow-description-item-menu"]',
                    componentObject.consts.ITEM_MENU_WORKFLOW_DESCRIPTION_HEADER
                )
            );
        },

        // Workflow modal form description field
        getItemMenuEditModalFormDescriptionField: function() {
            return this.getModalForm().element(
                by.id(
                    'description-label',
                    componentObject.consts.ITEM_MENU_WORKFLOW_DESCRIPTION_FIELD
                )
            );
        },

        // Workflow cancel confirmation message
        getItemMenuCancelConfirmationMessage: function() {
            return this.getConfirmationModal().element(
                by.id(
                    'confirmationModalDescription',
                    componentObject.consts.ITEM_MENU_WORKFLOW_CANCEL_CONFIRMATION_MESSAGE
                )
            );
        },

        // Workflow actions and comments
        getAllTasksTabLink: function() {
            return element(by.css('li[tab-id="allTasksTab"] a'));
        },
        getTab: function(tabId) {
            return element(by.css('se-tab[tab-id="' + tabId + '"]'));
        },
        getActionsForTabId: function(tabId) {
            return element.all(by.css('se-tab[tab-id="' + tabId + '"]' + ' workflow-action-item'));
        },
        getWorkflowActionByCode: function(tabId, actionCode) {
            return this.getTab(tabId).element(
                by.css('se-collapsible-container[id="' + actionCode + '"]')
            );
        },
        getWorkflowActionCollapseButton: function(tabId, actionCode) {
            return componentObject.elements
                .getWorkflowActionByCode(tabId, actionCode)
                .element(by.css('.collapsible-container__header__button'));
        },
        getCommentsForAction: function(tabId, actionCode) {
            return componentObject.elements
                .getWorkflowActionByCode(tabId, actionCode)
                .all(by.css('workflow-action-comment'));
        },
        getDecisionButtonsGroup: function(tabId, actionCode) {
            return componentObject.elements
                .getWorkflowActionByCode(tabId, actionCode)
                .element(by.css('.se-workflow-action-item__decision-btn'));
        },
        getDecisionButtonForAction: function(actionCode, decisionName) {
            var tabId = 'currentTasksTab';
            return this.getWorkflowActionByCode(tabId, actionCode).element(
                by.cssContainingText(
                    'button[id="' + actionCode + '-decision-button"]',
                    decisionName
                )
            );
        },
        getSplitDecisionButtonForAction: function(actionCode) {
            var tabId = 'currentTasksTab';
            return componentObject.elements
                .getWorkflowActionByCode(tabId, actionCode)
                .element(by.css('button[id="' + actionCode + '"-decision-split-button]'));
        },

        //Workflow Decisions
        getCommentField: function() {
            return element(this.getGenericEditorFieldSelector('comment'));
        }
    };

    componentObject.actions = {
        // Start Workflow
        clickStartWorkflowButton: function() {
            return browser.click(componentObject.elements.getStartWorkflowButton());
        },
        clickCreatePageVersionButton: function() {
            return browser.click(componentObject.elements.getCreateVersionCheckboxField());
        },
        selectWorkflowTemplageByname: function(name) {
            return browser.click(componentObject.elements.getTemplateField()).then(function() {
                return browser.click(componentObject.elements.getTemplateFieldOptionByName(name));
            });
        },

        // Workflow Tasks button
        clickPageWorkflowTasksButton: function() {
            return browser.click(componentObject.elements.getPageTasksButton());
        },
        hoverOverPageWorkflowTasksButton: function() {
            return browser.hoverElement(componentObject.elements.getPageTasksButton());
        },

        // Workflow Tasks More (...) button
        clickItemMenuMoreButton: function() {
            return browser.click(componentObject.elements.getItemMenuMoreButton());
        },

        // Workflow Edit/Cancel buttons
        clickEditMenuButton: function() {
            return browser.click(componentObject.elements.getItemMenuEditItem());
        },
        clickCancelMenuButton: function() {
            return browser.click(componentObject.elements.getItemMenuCancelItem());
        },

        // Workflow Tasks - All Tasks Tabs
        selectAllTasksTab: function() {
            return browser.click(componentObject.elements.getAllTasksTabLink());
        },

        // Workflow Action
        expandWorkflowAction: function(tabId, actionCode) {
            return browser.click(
                componentObject.elements.getWorkflowActionCollapseButton(tabId, actionCode)
            );
        },

        clickDecisionButton: function(actionCode, decisionName, isSplitButton) {
            if (isSplitButton) {
                return browser.click(
                    componentObject.elements.getSplitDecisionButtonForAction(
                        actionCode,
                        decisionName
                    )
                );
            }
            return browser.click(
                componentObject.elements.getDecisionButtonForAction(actionCode, decisionName)
            );
        },

        navigateToPage: function(dirname, done) {
            return browser.bootstrap(dirname).then(function() {
                return browser.waitForWholeAppToBeReady().then(function() {
                    return browser.waitUntilNoModal().then(function() {
                        done();
                    });
                });
            });
        }
    };

    componentObject.assertions = {
        // Start Workflow
        startWorkflowButtonIsAbsent: function() {
            expect(componentObject.elements.getStartWorkflowButton()).toBeAbsent();
        },
        startWorkflowButtonIsDisplayed: function() {
            expect(componentObject.elements.getStartWorkflowButton()).toBeDisplayed();
        },
        modalFormIsDispalyed: function() {
            browser.waitForPresence(componentObject.elements.getModalForm());
        },
        templateFieldIsDisplayed: function() {
            expect(componentObject.elements.getTemplateField()).toBeDisplayed();
            expect(
                componentObject.elements.getGenericEditorFieldLabel('templateCode').getText()
            ).toContain('Workflow');
        },
        templateFieldIsAbsent: function() {
            expect(componentObject.elements.getTemplateField()).toBeAbsent();
            expect(
                componentObject.elements.getGenericEditorFieldLabel('templateCode')
            ).toBeAbsent();
        },
        descriptionFieldIsDisplayed: function() {
            expect(componentObject.elements.getDescriptionField()).toBeDisplayed();
            expect(
                componentObject.elements.getGenericEditorFieldLabel('description').getText()
            ).toContain('Description');
        },
        descriptionFieldIsAbsent: function() {
            expect(componentObject.elements.getDescriptionField()).toBeAbsent();
            expect(componentObject.elements.getGenericEditorFieldLabel('description')).toBeAbsent();
        },
        createVersionFieldIsDisplayed: function() {
            expect(componentObject.elements.getCreateVersionField()).toBeDisplayed();
            expect(componentObject.elements.getCreateVersionFieldLabel().getText()).toContain(
                'Create page version'
            );
        },
        createVersionFieldIsAbsent: function() {
            expect(componentObject.elements.getCreateVersionField()).toBeAbsent();
            expect(componentObject.elements.getCreateVersionFieldLabel()).toBeAbsent();
        },
        versionLabelFieldIsDisplayed: function() {
            expect(componentObject.elements.getVersionLabelField()).toBeDisplayed();
        },
        versionLabelFieldIsAbsent: function() {
            expect(componentObject.elements.getVersionLabelField()).toBeAbsent();
        },
        versionLabelFieldContainsText: function(text) {
            browser
                .waitForSelectorToContainValue(
                    componentObject.elements.getVersionLabelField(),
                    text
                )
                .then(function() {
                    componentObject.elements
                        .getVersionLabelField()
                        .getAttribute('value')
                        .then(function(actualText) {
                            expect(actualText).toBe(text);
                        });
                });
        },

        // Workflow Item Menu
        menuHeaderContainsText: function(text) {
            expect(componentObject.elements.getPageWorkflowMenuHeader().getText()).toContain(text);
        },
        itemMenuContainsWorkflowDescriptionHeader: function() {
            expect(componentObject.elements.getItemMenuDescriptionHeader()).toBeDisplayed();
        },
        itemMenuContainsWorkflowCancel: function() {
            expect(componentObject.elements.getItemMenuCancelItem()).toBeDisplayed();
        },
        itemEditModalFormContainsDescriptionField: function() {
            expect(
                componentObject.elements.getItemMenuEditModalFormDescriptionField()
            ).toBeDisplayed();
        },
        itemCancelConfirmationDisplayed: function() {
            expect(componentObject.elements.getItemMenuCancelConfirmationMessage()).toBeDisplayed();
        },
        cancelButtonIsAbsent: function() {
            expect(componentObject.elements.getItemMenuCancelItem()).toBeAbsent();
        },
        editIconIsAbsent: function() {
            expect(componentObject.elements.getItemMenuEditItem()).toBeAbsent();
        },

        // Workflow Tasks Menu
        workflowTasksButtonIsDisplayed: function() {
            expect(componentObject.elements.getPageTasksButton()).toBeDisplayed();
        },

        // Workflow Actions and Comments
        decisionButtonsNotDisplayed: function(tabId, actionCode) {
            expect(
                componentObject.elements.getDecisionButtonsGroup(tabId, actionCode)
            ).toBeAbsent();
        },
        decisionButtonsAreDisplayed: function(tabId, actionCode) {
            expect(
                componentObject.elements.getDecisionButtonsGroup(tabId, actionCode)
            ).toBeDisplayed();
        },
        assertNumberOfActionsAvailable: function(tabId, expectedElementCount) {
            componentObject.elements.getActionsForTabId(tabId).then(function(elements) {
                expect(elements.length).toBe(expectedElementCount);
            });
        },
        assertNumberOfCommentsAvailable: function(tabId, actionCode, expectedElementCount) {
            componentObject.actions.expandWorkflowAction(tabId, actionCode).then(function() {
                componentObject.elements
                    .getCommentsForAction(tabId, actionCode)
                    .then(function(comments) {
                        expect(comments.length).toBe(expectedElementCount);
                    });
            });
        },

        // Workflow Decisions
        commentFieldIsDisplayed: function() {
            expect(componentObject.elements.getCommentField()).toBeDisplayed();
            expect(
                componentObject.elements.getGenericEditorFieldLabel('comment').getText()
            ).toContain('Comment');
        }
    };

    componentObject.utils = {
        setNumberOfWorkflowsToReturn: function(count) {
            browser.executeScript(
                'window.sessionStorage.setItem("numberOfWorkflowsToReturn", arguments[0])',
                count
            );
        },
        setWorkflowAvailableToCurrentUser: function(makeAvailable) {
            // Note: By default, the workflow is available to the current user.
            browser.executeScript(
                'window.sessionStorage.setItem("workflowsAvailableForCurrentPrincipal", arguments[0])',
                makeAvailable
            );
        },
        makeItemsEditable: function(items) {
            browser.executeScript(
                'window.sessionStorage.setItem("workflowItemsEditable", arguments[0])',
                JSON.stringify(items)
            );
        }
    };

    return componentObject;
})();
