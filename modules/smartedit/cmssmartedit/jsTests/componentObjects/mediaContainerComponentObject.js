/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.elements = {
        getMediaContainerListSelector: function() {
            return componentObject.elements.getElementById('mediaContainer-selector');
        },
        getMediaContainerListElements: function() {
            return element.all(by.css('.ui-select-choices-row .media-container-row-label'));
        },
        getMediaContainerListElementByQUalifier: function(mediaContainerQualifier) {
            return element(
                by.cssContainingText(
                    '.ui-select-choices-row .media-container-row-label',
                    mediaContainerQualifier
                )
            );
        },
        getElementById: function(elementId) {
            return element(by.id(elementId));
        },
        getElementByClass: function(className) {
            return element(by.css(className));
        },
        getMediaContainerClearButton: function() {
            return componentObject.elements.getElementByClass(
                '.se-generic-editor-dropdown__remove-button'
            );
        },
        getMediaContainerSearchField: function() {
            return componentObject.elements
                .getMediaContainerListSelector()
                .element(by.css('.ui-select-search'));
        },
        getMediaContainerSearchCreateButton: function() {
            return componentObject.elements
                .getMediaContainerListSelector()
                .element(by.css('.se-actionable-search-item__create-btn'));
        }
    };

    componentObject.actions = {
        openMediaContainerList: function() {
            browser.click(componentObject.elements.getMediaContainerListSelector());
        },
        selectMediaContainerFromList: function(mediaContainerQualifier) {
            browser.click(
                componentObject.elements.getMediaContainerListElementByQUalifier(
                    mediaContainerQualifier
                )
            );
        },
        clearSelectedMediaContainer: function() {
            browser.click(componentObject.elements.getMediaContainerClearButton());
        },
        inputTextToSearchFieldOfMediaContainerList: function(searchText) {
            browser.sendKeys(componentObject.elements.getMediaContainerSearchField(), searchText);
        },
        clickCreateNewMediaContainer: function() {
            browser.click(componentObject.elements.getMediaContainerSearchCreateButton());
        }
    };

    componentObject.assertions = {
        mediaContainerListIsDispalyed: function() {
            expect(componentObject.elements.getMediaContainerListSelector()).toBeDisplayed();
        },
        mediaContainerListHasData: function(expectedNumberOfElements) {
            componentObject.elements.getMediaContainerListElements().then(function(elements) {
                expect(elements.length).toBe(expectedNumberOfElements);
            });
        },
        mediaContainerQualifierFieldDisplayed: function(fieldId) {
            expect(componentObject.elements.getElementById(fieldId)).toBeDisplayed();
        },
        mediaContainerQualifierFieldIsReadOnly: function(fieldId) {
            expect(componentObject.elements.getElementById(fieldId).getAttribute('readonly')).toBe(
                'true'
            );
        },
        mediaContainerQualifierFieldIsEditable: function(fieldId) {
            expect(componentObject.elements.getElementById(fieldId).getAttribute('readonly')).toBe(
                null
            );
        },
        mediaContainerQualifierFieldContainsValue: function(fieldId, value) {
            expect(
                componentObject.elements.getElementById(fieldId).getAttribute('value')
            ).toContain(value);
        },
        mediaContainerQualifierFieldNotDisplayed: function(fieldId) {
            expect(componentObject.elements.getElementById(fieldId)).toBeAbsent();
        },
        mediaFormatIsDisplayed: function(format) {
            expect(
                componentObject.elements.getElementByClass('.se-media-container-cell--' + format)
            ).toBeDisplayed();
        },
        mediaFormatIsNotDisplayed: function(format) {
            expect(
                componentObject.elements.getElementByClass('.se-media-container-cell--' + format)
            ).toBeAbsent();
        }
    };

    componentObject.utils = {
        switchToAdvancedMediaContainer: function() {
            browser.executeScript(
                'window.sessionStorage.setItem("advancedMediaContainerManagementEnabled", arguments[0])',
                JSON.stringify(true)
            );
        }
    };

    return componentObject;
})();
