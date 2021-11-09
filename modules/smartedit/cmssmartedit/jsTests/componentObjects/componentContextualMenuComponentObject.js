/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.elements = {
        getSmarteditOverlayCSSMatcherForComponent: function(componentId) {
            return (
                "#smarteditoverlay .smartEditComponentX[data-smartedit-component-id='" +
                componentId +
                "'] "
            );
        },
        getMoreItemsButton: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return element(
                    by.css(
                        componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                            componentId
                        ) + '.se-ctx-menu-element__btn--more'
                    )
                );
            });
        },
        getRemoveButtonForComponentId: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return element(
                    by.css(
                        componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                            componentId
                        ) + '.removebutton'
                    )
                );
            });
        },
        getMoveButtonForComponentId: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return element(
                    by.css(
                        componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                            componentId
                        ) + '.movebutton'
                    )
                );
            });
        },
        getEditButtonForComponentId: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return element(
                    by.css(
                        componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                            componentId
                        ) + '.editbutton'
                    )
                );
            });
        },
        getCloneButtonForComponentId: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return componentObject.elements
                    .getMoreItemsButton(componentId)
                    .then(function(moreButton) {
                        return moreButton.isPresent().then(function(isPresent) {
                            if (!isPresent) {
                                return moreButton;
                            }
                            return browser.click(by.css(moreButton)).then(function() {
                                return element(by.css('.clonebutton'));
                            });
                        });
                    });
            });
        },
        getExternalComponentButtonForComponentId: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return element(
                    by.css(
                        componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                            componentId
                        ) + '.externalcomponentbutton'
                    )
                );
            });
        },
        getNumContextualMenuItemsForComponentId: function(componentId) {
            return element
                .all(
                    by.css(
                        componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                            componentId
                        ) + '.se-ctx-menu-element__btn'
                    )
                )
                .count();
        },
        getComponentMoreMenu: function() {
            return element(by.css('.se-contextual-more-menu'));
        },
        getButtonInMoreMenu: function(buttonName) {
            var buttonSelector = '.' + buttonName + 'button';
            return this.getComponentMoreMenu().element(by.css(buttonSelector));
        },
        getSharedComponentButtonForComponentId: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return element(
                    by.css(
                        componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                            componentId
                        ) + '.shared-component-button'
                    )
                );
            });
        },
        getSharedComponentButtonMsg: function() {
            return browser.findElement(by.css('.se-ctx-menu-btn__msg'));
        }
    };

    componentObject.actions = {
        clickRemoveButton: function(componentId) {
            return browser.switchToIFrame().then(function() {
                var cloneSelector = componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                    componentId
                );
                var selector = cloneSelector + '.removebutton';
                browser.waitForVisibility(cloneSelector);
                return browser.moveTo(selector).then(function() {
                    return browser.click(by.css(selector));
                });
            });
        },
        clickEditButton: function(componentId) {
            return browser.switchToIFrame().then(function() {
                var cloneSelector = componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                    componentId
                );
                var selector = cloneSelector + '.editbutton';
                browser.waitForVisibility(cloneSelector);
                return browser.moveTo(selector).then(function() {
                    return browser.click(by.css(selector));
                });
            });
        },
        clickCloneButton: function(componentId) {
            return browser.switchToIFrame().then(function() {
                var componentSelector = componentObject.elements.getSmarteditOverlayCSSMatcherForComponent(
                    componentId
                );
                var selector = componentSelector + '.se-ctx-menu-element__btn--more';
                browser.waitForVisibility(selector);
                return browser.moveTo(selector).then(function() {
                    return browser.click(by.css(selector)).then(function() {
                        var button = componentObject.elements.getButtonInMoreMenu('clone');
                        return browser.moveTo(button).then(function() {
                            return browser.click(button);
                        });
                    });
                });
            });
        },
        clickExternalComponentButton: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return componentObject.elements
                    .getExternalComponentButtonForComponentId(componentId)
                    .then(function(externalComponentButton) {
                        return browser
                            .actions()
                            .mouseMove(externalComponentButton)
                            .click()
                            .perform();
                    });
            });
        },
        clickSharedComponentButton: function(componentId) {
            return browser.switchToIFrame().then(function() {
                return componentObject.elements
                    .getSharedComponentButtonForComponentId(componentId)
                    .then(function(sharedComponentButton) {
                        return browser
                            .actions()
                            .mouseMove(sharedComponentButton)
                            .click()
                            .perform();
                    });
            });
        }
    };

    componentObject.assertions = {
        removeMenuItemForComponentIdLoadedRightImg: function(componentID) {
            componentObject.elements
                .getRemoveButtonForComponentId(componentID)
                .then(function(removeButton) {
                    expect(removeButton.getAttribute('class')).toContain('sap-icon--decline');
                });
        },
        editMenuItemForComponentIdLoadedRightImg: function(componentID) {
            componentObject.elements
                .getEditButtonForComponentId(componentID)
                .then(function(editButton) {
                    // this button is still not using the assets service.
                    expect(editButton.getAttribute('class')).toContain('sap-icon--edit');
                });
        },
        moveMenuItemForComponentIdLoadedRightImg: function(componentID) {
            componentObject.elements
                .getMoveButtonForComponentId(componentID)
                .then(function(moveButton) {
                    expect(moveButton.getAttribute('class')).toContain('sap-icon--move');
                });
        },
        externalComponentMenuItemForComponentIdLoadedRightImg: function(componentID) {
            componentObject.elements
                .getExternalComponentButtonForComponentId(componentID)
                .then(function(moveButton) {
                    expect(moveButton.getAttribute('class')).toContain('hyicon-globe');
                });
        },
        externalComponentToShowParentCatalogDetails: function(componentID, catalogVersionName) {
            componentObject.actions.clickExternalComponentButton(componentID).then(function() {
                browser.waitUntil(function() {
                    return browser
                        .findElement(by.css('external-component-button'))
                        .getText()
                        .then(function(text) {
                            return text === catalogVersionName;
                        });
                }, 'external component button text not found');
            });
        },
        assertMoreItemsButtonIsNotPresent: function(componentID) {
            componentObject.elements
                .getMoreItemsButton(componentID)
                .then(function(moreItemsButtons) {
                    browser.waitForAbsence(moreItemsButtons);
                });
        },
        assertEditButtonIsNotPresent: function(componentId) {
            componentObject.elements
                .getEditButtonForComponentId(componentId)
                .then(function(editButton) {
                    browser.waitForAbsence(editButton);
                });
        },
        assertMoveButtonIsNotPresent: function(componentId) {
            componentObject.elements
                .getMoveButtonForComponentId(componentId)
                .then(function(moveButton) {
                    browser.waitForAbsence(moveButton);
                });
        },
        assertRemoveButtonIsNotPresent: function(componentId) {
            componentObject.elements
                .getRemoveButtonForComponentId(componentId)
                .then(function(removeButton) {
                    browser.waitForAbsence(removeButton);
                });
        },
        assertSharedComponentButtonIsPresent: function(componentId) {
            componentObject.elements
                .getSharedComponentButtonForComponentId(componentId)
                .then(function(sharedComponentButton) {
                    browser.waitForPresence(sharedComponentButton);
                });
        },
        assertSharedComponentButtonIsNotPresent: function(componentId) {
            componentObject.elements
                .getSharedComponentButtonForComponentId(componentId)
                .then(function(sharedComponentButton) {
                    browser.waitForAbsence(sharedComponentButton);
                });
        },
        assertSharedComponentToShowDetails: function() {
            browser.waitForPresence(componentObject.elements.getSharedComponentButtonMsg());
        }
    };

    return componentObject;
})();
