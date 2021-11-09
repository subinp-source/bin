/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    // --------------------------------------------------------------------------------------------------
    // Variables
    // --------------------------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------------------------
    // Constants
    // --------------------------------------------------------------------------------------------------
    componentObject.constants = {
        DRAFT_DISPLAY_STATUS: 'DRAFT',
        IN_PROGRESS_DISPLAY_STATUS: 'IN_PROGRESS',
        READY_TO_SYNC_DISPLAY_STATUS: 'READY_TO_SYNC',
        SYNCHED_DISPLAY_STATUS: 'SYNCED'
    };

    // --------------------------------------------------------------------------------------------------
    // Elements
    // --------------------------------------------------------------------------------------------------
    componentObject.elements = {
        getPageStatusComponent: function(parentElement) {
            var selector = by.css('page-display-status');
            return parentElement ? parentElement.element(selector) : browser.element(selector);
        },
        getPageStatusIcon: function() {
            return this.getPageStatusComponent().element(by.css('.se-page-status-icon'));
        },
        getPageStatusLabel: function() {
            return this.getPageStatusComponent()
                .element(by.css('.se-page-status-label'))
                .getText();
        }
    };

    // --------------------------------------------------------------------------------------------------
    // Actions
    // --------------------------------------------------------------------------------------------------
    componentObject.actions = {
        hoverPageStatus: function() {
            return browser.hoverElement(componentObject.elements.getPageStatusLabel());
        }
    };

    // --------------------------------------------------------------------------------------------------
    // Assertions
    // --------------------------------------------------------------------------------------------------
    componentObject.assertions = {
        pageStatusComponentIsDisplayed: function(containingElement) {
            expect(
                componentObject.elements.getPageStatusComponent(containingElement)
            ).toBeDisplayed();
        },
        pageStatusComponentIsNotDisplayed: function(containingElement) {
            expect(componentObject.elements.getPageStatusComponent(containingElement)).toBeAbsent();
        },
        pageStatusIsDisplayedCorrectly: function(expectedIcon, expectedLabel) {
            expect(
                componentObject.utils.hasClass(
                    componentObject.elements.getPageStatusIcon(),
                    expectedIcon
                )
            ).toBeTruthy();
            expect(componentObject.elements.getPageStatusLabel()).toBe(expectedLabel);
        },
        pageIsInDraftStatus: function() {
            this.pageStatusIsDisplayedCorrectly('se-page-status-draft__icon', 'Draft');
        },
        pageIsInProgressStatus: function() {
            this.pageStatusIsDisplayedCorrectly('se-page-status-in_progress__icon', 'In Progress');
        },
        pageIsInReadyToSyncStatus: function() {
            this.pageStatusIsDisplayedCorrectly(
                'se-page-status-ready_to_sync__icon',
                'Ready To Sync'
            );
        },
        pageIsSyncedStatus: function() {
            this.pageStatusIsDisplayedCorrectly('se-page-status-synced__icon', 'Synched');
        },
        workflowIsUnavailableToCurrentUser: function() {
            browser.waitForVisibility(componentObject.elements.getPageStatusIcon());
            expect(
                componentObject.utils.hasClass(
                    componentObject.elements.getPageStatusIcon(),
                    'se-page-status-locked__icon'
                )
            ).toBeTruthy();
        }
    };

    // --------------------------------------------------------------------------------------------------
    // Utils
    // --------------------------------------------------------------------------------------------------
    componentObject.utils = {
        setMockPageDisplayStatus: function(status) {
            browser.executeScript(
                'window.sessionStorage.setItem("customDisplayStatus", arguments[0])',
                status
            );
        },
        hasClass: function(element, cls) {
            return element.getAttribute('class').then(function(classes) {
                return classes.split(' ').indexOf(cls) !== -1;
            });
        }
    };

    return componentObject;
})();
