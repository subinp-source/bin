/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObjects = {
        actions: {
            clickOnDeletePageAnchor: function() {
                return browser.click(componentObjects.elements.getDeletePageItemAnchor());
            },
            confirmPageSoftDeletion: function() {
                return browser.click(
                    componentObjects.elements.getPageSoftDeletionConfirmationButton()
                );
            },
            hoverDeletePageItemAnchor: function() {
                return browser.hoverElement(
                    componentObjects.elements.getDeletePageItemPopoverAnchor()
                );
            },
            openMoreActionsToolbarItem: function() {
                return browser.click(componentObjects.elements.getMoreActionsItemButton());
            }
        },

        assertions: {
            deletePageItemIsActive: function() {
                browser.waitForAbsence(by.css('.se-dropdown-item--delete-link-wrapper--disabled'));
            },
            deletePageItemIsInactive: function() {
                expect(
                    componentObjects.elements
                        .getDeletePageItemAnchor()
                        .element(by.css('.se-dropdown-item--delete-link-wrapper--disabled'))
                        .isPresent()
                ).toBe(true);
            },
            deletePageItemPopoverAnchorIsNotPresent: function() {
                expect(componentObjects.elements.getDeletePageItemPopoverAnchor()).toBeAbsent();
            }
        },

        constants: {},

        elements: {
            getDeletePageItemAnchor: function() {
                browser.waitForAngularEnabled(true);
                return browser.findElement(
                    by.css('.se-dropdown-more-menu .se-dropdown-item--delete-wrapper')
                );
            },
            getDeletePageItemPopoverAnchor: function() {
                return componentObjects.elements
                    .getDeletePageItemAnchor()
                    .element(by.css('.se-dropdown-item--delete-page-popover span'));
            },
            getMoreActionsItemButton: function() {
                return browser.findElement(by.css('[data-item-key="moreActionsMenu"] button'));
            },
            getPageSoftDeletionConfirmationButton: function() {
                return browser.findElement(by.css('#confirmOk'));
            }
        }
    };

    return componentObjects;
})();
