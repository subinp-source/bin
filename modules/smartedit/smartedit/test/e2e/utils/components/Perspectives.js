/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var OVERLAY_SELECTOR = '#smarteditoverlay';

    var perspectiveSelectorObject = {};

    perspectiveSelectorObject.constants = {
        DEFAULT_PERSPECTIVES: {
            ALL: 'All',
            NONE: 'Preview'
        }
    };

    perspectiveSelectorObject.elements = {
        getTooltipIcon: function() {
            return element(by.id('perspectiveTooltip'));
        },

        getPerspectiveDropdownToggle: function() {
            return element(by.css('.se-perspective-selector__btn'));
        },

        getPerspectiveDropdownMenu: function() {
            return element(by.css('.se-perspective__list'));
        },

        getPerspectiveDropdownOption: function(perspectiveName) {
            return element(
                by.cssContainingText('.se-perspective__list-item-text', perspectiveName)
            );
        },

        getActivePerspectiveName: function() {
            return browser.switchToParent().then(function() {
                return element(by.css('.se-perspective-selector__btn')).getText();
            });
        },

        deprecated_getElementInOverlay: function(componentID, componentType) {
            var selector =
                '#smarteditoverlay .smartEditComponentX[data-smartedit-component-id="' +
                componentID +
                '"]';

            if (componentType) {
                selector += '[data-smartedit-component-type="' + componentType + '"]';
            }

            return element(by.css(selector));
        }
    };

    perspectiveSelectorObject.actions = {
        openAndBeReady: function() {
            browser.get('test/e2e/perspectiveService/index.html');

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
        },

        refreshAndWaitForAngularEnabled: function() {
            browser.get('test/e2e/perspectiveService/index.html').then(function() {
                return browser.waitForAngularEnabled(false);
            });
        },

        openPerspectiveSelectorDropdown: function() {
            return browser.switchToParent().then(function() {
                return browser.click(
                    perspectiveSelectorObject.elements.getPerspectiveDropdownToggle()
                );
            });
        },

        selectPerspective: function(perspectiveName) {
            return browser
                .switchToParent()
                .then(
                    function() {
                        return perspectiveSelectorObject.elements
                            .getActivePerspectiveName()
                            .then(function(perspectiveSelected) {
                                if (
                                    perspectiveSelected.toUpperCase() !==
                                    perspectiveName.toUpperCase()
                                ) {
                                    return perspectiveSelectorObject.actions
                                        .openPerspectiveSelectorDropdown()
                                        .then(function() {
                                            return browser
                                                .click(
                                                    perspectiveSelectorObject.elements.getPerspectiveDropdownOption(
                                                        perspectiveName
                                                    ),
                                                    'perspective ' +
                                                        perspectiveName +
                                                        ' is not clickable'
                                                )
                                                .then(function() {
                                                    return browser
                                                        .waitForContainerToBeReady()
                                                        .then(function() {
                                                            return browser
                                                                .switchToIFrame()
                                                                .then(function() {
                                                                    return perspectiveName ===
                                                                        perspectiveSelectorObject
                                                                            .constants
                                                                            .DEFAULT_PERSPECTIVES
                                                                            .NONE
                                                                        ? true
                                                                        : browser.waitForVisibility(
                                                                              OVERLAY_SELECTOR
                                                                          );
                                                                });
                                                        });
                                                });
                                        });
                                } else {
                                    browser.waitForWholeAppToBeReady();
                                    return browser.switchToIFrame().then(
                                        function() {
                                            return perspectiveName ===
                                                perspectiveSelectorObject.constants
                                                    .DEFAULT_PERSPECTIVES.NONE
                                                ? true
                                                : browser.waitForVisibility(OVERLAY_SELECTOR);
                                        }.bind(this)
                                    );
                                }
                            });
                    }.bind(this)
                )
                .then(function() {
                    return browser.switchToParent();
                });
        }
    };

    perspectiveSelectorObject.assertions = {
        assertPerspectiveActive: function() {
            expect(perspectiveSelectorObject.elements.getActivePerspectiveName());
        },

        assertPerspectiveSelectorDropdownDisplayed: function(isDisplayed) {
            expect(
                perspectiveSelectorObject.elements.getPerspectiveDropdownMenu().isDisplayed()
            ).toBe(isDisplayed);
        },

        assertPerspectiveSelectorDropdownIsAbsent: function() {
            return browser.waitForAbsence(
                perspectiveSelectorObject.elements.getPerspectiveDropdownMenu()
            );
        },

        assertSmarteditOverlayIsPresent: function() {
            expect(browser.waitToBeDisplayed(by.css(OVERLAY_SELECTOR))).toBe(true);
        },

        assertSmarteditOverlayIsAbsent: function() {
            expect(browser.isAbsent(by.id('perspectiveTooltip'))).toBe(true);
        }
    };

    return perspectiveSelectorObject;
})();
