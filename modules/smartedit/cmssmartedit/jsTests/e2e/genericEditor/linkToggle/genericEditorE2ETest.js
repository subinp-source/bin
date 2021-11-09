/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
describe('GenericEditor With LinkToggle', function() {
    var linkToggle = e2e.componentObjects.linkToggle;

    beforeEach(function() {
        browser.bootstrap(__dirname);
        browser.waitForPresence(by.css('se-generic-editor'));
    });

    it("GIVEN GenericEditor with linkToggle field THEN it will set 'Existing Page' radio button to selected and 'urlLink' value to '/url-link'", function() {
        // GIVEN THEN
        linkToggle.assertions.externalRadioNotSelected();
        linkToggle.assertions.internalRadioIsSelected();
        linkToggle.assertions.urlLinkEqualTo('/url-link');
    });

    it("GIVEN GenericEditor with linkToggle field AND 'Existing Page' radio is selected WHEN 'External Link' is selected THEN 'urlLink' is cleared", function() {
        // GIVEN
        linkToggle.assertions.externalRadioNotSelected();
        linkToggle.assertions.internalRadioIsSelected();
        linkToggle.assertions.urlLinkEqualTo('/url-link');

        // WHEN
        linkToggle.actions.clickOnExternalRadio();

        // THEN
        linkToggle.assertions.urlLinkIsEmpty();
    });
});
