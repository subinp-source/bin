/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.constants = {
        MODAL_SELECTOR: '.fd-modal'
    };

    componentObject.elements = {
        getSuccessAlert: function() {
            return element(by.css('.fd-alert--success'));
        },
        getModalDialog: function() {
            return browser.switchToParent().then(function() {
                return element(by.css(componentObject.constants.MODAL_SELECTOR));
            });
        },
        getModalDialogTitle: function() {
            return browser.switchToParent().then(function() {
                return element(by.css('.se-confirmation-dialog .fd-modal__title')).getText();
            });
        },
        getCancelButton: function() {
            return element(
                by.css(componentObject.constants.MODAL_SELECTOR + " button[id='cancel']")
            );
        },
        getSaveButton: function() {
            return element(by.css(componentObject.constants.MODAL_SELECTOR + " button[id='save']"));
        },
        getAttributeValueByName: function(attributeName) {
            return browser.switchToParent().then(function() {
                return element(by.name(attributeName)).getAttribute('value');
            });
        }
    };

    componentObject.actions = {
        modalDialogClickCancel: function() {
            return browser.switchToParent().then(function() {
                return browser.click(componentObject.elements.getCancelButton()).then(function() {
                    return browser.waitForAbsence(
                        by.css(componentObject.constants.MODAL_SELECTOR),
                        'could not close modal window'
                    );
                });
            });
        },
        modalDialogClickSave: function() {
            return browser.switchToParent().then(function() {
                return browser.click(componentObject.elements.getSaveButton()).then(function() {
                    return browser.waitForAbsence(
                        by.css(componentObject.constants.MODAL_SELECTOR),
                        'could not close modal window'
                    );
                });
            });
        }
    };

    componentObject.assertions = {
        assertModalIsNotPresent: function() {
            browser.switchToParent().then(function() {
                browser.waitUntilNoModal();
            });
        },
        assertModalIsPresent: function() {
            browser.switchToParent().then(function() {
                browser.waitUntil(
                    EC.presenceOf(element(by.css(componentObject.constants.MODAL_SELECTOR))),
                    'Expected modal to be present'
                );
                expect(element(by.css(componentObject.constants.MODAL_SELECTOR)).isPresent()).toBe(
                    true
                );
            });
        },
        assertSuccessAlertIsDisplayed: function() {
            expect(componentObject.elements.getSuccessAlert().isPresent()).toBe(true);
        }
    };

    return componentObject;
})();
