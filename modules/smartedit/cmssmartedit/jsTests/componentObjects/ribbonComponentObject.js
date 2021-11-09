/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
var PLUS_BUTTON_SELECTOR =
    'div.se-toolbar--perspective button.toolbar-action--button span.icon-add.se-toolbar-menu-ddlb--button__icon';

module.exports = {
    elements: {
        addComponentButton: function() {
            return element(by.css(PLUS_BUTTON_SELECTOR));
        }
    },
    hasAddComponentButton: function() {
        expect(this.elements.addComponentButton().isPresent()).toBe(true);
    },

    doesNotHaveAddComponentButton: function() {
        browser.waitForAbsence(this.elements.addComponentButton());
    }
};
