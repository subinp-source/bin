/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Generic Editor - Nested Element Refresh', function() {
    var contextualMenu = e2e.componentObjects.componentContextualMenu;
    var storefront = e2e.componentObjects.storefront;
    var perspective = e2e.componentObjects.modeSelector;
    var genericEditor = e2e.componentObjects.genericEditor;

    var COMPONENT = 'component1';

    function modifyComponent() {
        return perspective.selectPreviewPerspective().then(function() {
            return browser.switchToIFrame().then(function() {
                return browser.click(element(by.id('addToCart'))).then(function() {
                    return perspective.selectAdvancedPerspective();
                });
            });
        });
    }

    function assertComponentModified() {
        return browser.waitUntil(function() {
            return browser.switchToIFrame().then(function() {
                return element(by.id('feedback'))
                    .getText()
                    .then(function(text) {
                        return !!text;
                    });
            });
        });
    }

    function assertComponentNotModified() {
        return browser.waitUntil(function() {
            return browser.switchToIFrame().then(function() {
                return element(by.id('feedback'))
                    .getText()
                    .then(function(text) {
                        return !text;
                    });
            });
        });
    }

    function openGenericEditor() {
        return storefront.actions.moveToComponent(COMPONENT).then(function() {
            return contextualMenu.actions.clickEditButton(COMPONENT).then(function() {
                return browser.switchToParent();
            });
        });
    }

    function selectFirstDropdownElement() {
        return browser.click(element(by.css('se-dropdown input'))).then(function() {
            browser.click(element(by.css('li[role=option]')));
        });
    }

    function selectNestedDropdownElement(idx) {
        return browser.click(element.all(by.css('.ui-select-toggle')).get(idx)).then(function() {
            browser.click(element(by.css('li[role=option]')));
        });
    }

    function openNestedEditor() {
        return browser.click(element(by.css('.cms-nested-component')));
    }

    beforeEach(function(done) {
        browser.bootstrap(__dirname);
        browser.waitForWholeAppToBeReady().then(function() {
            return perspective.selectAdvancedPerspective().then(function() {
                done();
            });
        });
    });

    it('Component inner content is modified after clicking the add to cart button', function() {
        modifyComponent().then(function() {
            assertComponentModified();
        });
    });

    it('Component is refreshed after 1st level modification', function() {
        modifyComponent().then(function() {
            return assertComponentModified().then(function() {
                return openGenericEditor().then(function() {
                    return element(by.id('name-shortstring'))
                        .sendKeys('name')
                        .then(function() {
                            return genericEditor.actions.save().then(function() {
                                return assertComponentNotModified();
                            });
                        });
                });
            });
        });
    });

    it('Component is refreshed after 2nd level modification', function() {
        modifyComponent().then(function() {
            return assertComponentModified().then(function() {
                return openGenericEditor().then(function() {
                    return selectFirstDropdownElement().then(function() {
                        return genericEditor.actions.save().then(function() {
                            return modifyComponent().then(function() {
                                return openGenericEditor().then(function() {
                                    return openNestedEditor().then(function() {
                                        return selectNestedDropdownElement(0).then(function() {
                                            return selectNestedDropdownElement(1).then(function() {
                                                return genericEditor.actions
                                                    .save()
                                                    .then(function() {
                                                        return genericEditor.actions
                                                            .cancel()
                                                            .then(function() {
                                                                return assertComponentNotModified();
                                                            });
                                                    });
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    });
});
