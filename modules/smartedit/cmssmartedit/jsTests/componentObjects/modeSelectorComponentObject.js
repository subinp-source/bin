/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {
        PREVIEW_PERSPECTIVE: 'PREVIEW',
        VERSIONING_PERSPECTIVE: 'VERSIONING',
        ADVANCED_CMS_PERSPECTIVE: 'Advanced CMS',
        BASIC_CMS_PERSPECTIVE: 'Basic CMS',
        OVERLAY_SELECTOR: '#smarteditoverlay',

        select: function(perspectiveName) {
            return browser.waitUntilNoModal().then(
                function() {
                    return browser
                        .switchToParent()
                        .then(
                            function() {
                                return browser
                                    .findElement(by.css('.se-perspective-selector__btn'), true)
                                    .getText()
                                    .then(
                                        function(perspectiveSelected) {
                                            if (
                                                perspectiveSelected.toUpperCase() !==
                                                perspectiveName.toUpperCase()
                                            ) {
                                                browser.waitForWholeAppToBeReady();
                                                browser.waitForVisibility(
                                                    '.se-perspective-selector'
                                                );
                                                return browser
                                                    .click(
                                                        browser.findElement(
                                                            by.css('.se-perspective-selector')
                                                        ),
                                                        true
                                                    )
                                                    .then(
                                                        function() {
                                                            return browser
                                                                .click(
                                                                    browser.findElement(
                                                                        by.cssContainingText(
                                                                            '.se-perspective__list-item',
                                                                            perspectiveName
                                                                        ),
                                                                        true
                                                                    ),
                                                                    'perspective ' +
                                                                        perspectiveName +
                                                                        ' is not clickable'
                                                                )
                                                                .then(
                                                                    function() {
                                                                        return browser
                                                                            .waitForContainerToBeReady()
                                                                            .then(
                                                                                function() {
                                                                                    return browser
                                                                                        .switchToIFrame()
                                                                                        .then(
                                                                                            function() {
                                                                                                return perspectiveName ===
                                                                                                    this
                                                                                                        .PREVIEW_PERSPECTIVE
                                                                                                    ? true
                                                                                                    : browser.waitForVisibility(
                                                                                                          this
                                                                                                              .OVERLAY_SELECTOR
                                                                                                      );
                                                                                            }.bind(
                                                                                                this
                                                                                            )
                                                                                        );
                                                                                }.bind(this)
                                                                            );
                                                                    }.bind(this)
                                                                );
                                                        }.bind(this)
                                                    );
                                            } else {
                                                browser.waitForWholeAppToBeReady();
                                                return browser.switchToIFrame().then(
                                                    function() {
                                                        return perspectiveName ===
                                                            this.PREVIEW_PERSPECTIVE
                                                            ? true
                                                            : browser.waitForVisibility(
                                                                  this.OVERLAY_SELECTOR
                                                              );
                                                    }.bind(this)
                                                );
                                            }
                                        }.bind(this)
                                    );
                            }.bind(this)
                        )
                        .then(function() {
                            return browser.switchToParent();
                        });
                }.bind(this)
            );
        },

        selectPreviewPerspective: function() {
            return this.select(this.PREVIEW_PERSPECTIVE);
        },

        selectVersioningPerspective: function() {
            return this.select(this.VERSIONING_PERSPECTIVE);
        },

        selectBasicPerspective: function() {
            return this.select(this.BASIC_CMS_PERSPECTIVE);
        },

        selectAdvancedPerspective: function() {
            return this.select(this.ADVANCED_CMS_PERSPECTIVE);
        },

        actions: {
            openPerspectiveList: function() {
                return browser.click(browser.findElement(by.css('.se-perspective-selector')));
            }
        },

        elements: {
            getSelectedModeName: function() {
                return browser.findElement(by.css('.se-perspective-selector__btn')).getText();
            },
            getPerspectiveModeByName: function(perspectiveName) {
                return element(
                    by.cssContainingText('.se-perspective__list-item-text', perspectiveName)
                );
            }
        },

        assertions: {
            expectedModeIsSelected: function(perspectiveName) {
                expect(componentObject.elements.getSelectedModeName()).toBe(
                    perspectiveName,
                    'Expected current mode (perspective) name to be ' + perspectiveName
                );
            },
            expectVersioningModeIsAbsent: function() {
                expect(
                    componentObject.elements.getPerspectiveModeByName(
                        componentObject.VERSIONING_PERSPECTIVE
                    )
                ).toBeAbsent();
            },
            expectVersioningModeIsPresent: function() {
                expect(
                    componentObject.elements.getPerspectiveModeByName(
                        componentObject.VERSIONING_PERSPECTIVE
                    )
                ).toBeDisplayed();
            }
        }
    };

    return componentObject;
})();
