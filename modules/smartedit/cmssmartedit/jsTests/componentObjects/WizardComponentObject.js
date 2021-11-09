/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    var selectors = {
        nextButton: function() {
            return by.css('.fd-modal__footer button#ACTION_NEXT');
        },
        backButton: function() {
            return by.css('.fd-modal__footer button#ACTION_BACK');
        }
    };

    componentObject.elements = {
        // Elements
        window: function() {
            return browser.findElement(by.css('se-modal-wizard-template'));
        },
        nextButton: function() {
            return browser.findElement(selectors.nextButton());
        },
        backButton: function() {
            return browser.findElement(selectors.backButton());
        },
        submitButton: function() {
            return browser.findElement(by.css('.fd-modal__footer button#ACTION_DONE'));
        },
        getCurrentStepText: function() {
            return browser
                .findElement(by.css('.se-modal-wizard-step__action--current'))
                .getText()
                .then(function(text) {
                    return text.trim();
                });
        }
    };

    componentObject.actions = {
        isWindowsOpen: function() {
            browser.switchToParent();
            return componentObject.elements.window().isPresent();
        },
        assertWindowIsOpen: function() {
            return browser.waitForPresence(
                componentObject.elements.window(),
                'modal window is not open'
            );
        },
        assertWindowIsClosed: function() {
            return browser.waitUntilNoModal();
        },
        moveNext: function() {
            return browser.click(selectors.nextButton());
        },
        scrollIntoView: function(item) {
            return browser.executeScript('arguments[0].scrollIntoView()', item.getWebElement());
        },
        submit: function() {
            var button = componentObject.elements.submitButton();
            return this.scrollIntoView(button).then(function() {
                return browser
                    .actions()
                    .mouseMove(button)
                    .click()
                    .perform();
            });
        }
    };

    componentObject.assertions = {
        currentStepTextIs: function(expectedLabel) {
            browser.waitUntil(function() {
                return componentObject.elements.getCurrentStepText().then(function(label) {
                    return label === expectedLabel;
                });
            }, 'current step was expected to be ' + expectedLabel);
        }
    };

    return componentObject;
})();
