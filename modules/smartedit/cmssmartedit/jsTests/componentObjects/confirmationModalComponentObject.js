/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.elements = {
        getConfirmationModalOkButton: function() {
            return by.css('.se-confirmation-dialog #confirmOk');
        },
        getConfirmationModalCancelButton: function() {
            return by.css('.se-confirmation-dialog #confirmCancel');
        }
    };

    componentObject.assertions = {
        cancelButtonIsNotDisplayed: function() {
            return browser.waitForAbsence(
                element(componentObject.elements.getConfirmationModalCancelButton())
            );
        }
    };

    componentObject.actions = {
        confirmConfirmationModal: function() {
            browser.click(
                componentObject.elements.getConfirmationModalOkButton(),
                'Could not click confirm button'
            );
        },
        dismissConfirmationModal: function() {
            browser.click(
                componentObject.elements.getConfirmationModalCancelButton(),
                'Could not click dismiss button'
            );
        }
    };

    return componentObject;
})();
