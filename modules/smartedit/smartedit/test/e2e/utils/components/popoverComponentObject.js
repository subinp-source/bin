/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = {
    _getAnchorYHelpSelector: function(selector) {
        return by.css(selector + ' [se-tooltip-trigger]');
    },
    _getAnchorYPopoverSector: function(selector) {
        return by.css(selector + '[data-y-popover]');
    },
    _getAnchorYHelp: function(selector) {
        return element(this._getAnchorYHelpSelector(selector));
    },
    _getAnchorYPopover: function(selector) {
        return element(this._getAnchorYPopoverSector(selector));
    },
    getPopover: function() {
        return element(by.css('.popover'));
    },
    hoverYHelp: function(selector) {
        return browser
            .actions()
            .mouseMove(this._getAnchorYHelp(selector))
            .perform();
    },
    hoverYPopover: function(selector) {
        return browser
            .actions()
            .mouseMove(this._getAnchorYPopover(selector))
            .perform();
    },
    clickYHelp: function(selector) {
        return browser.click(this._getAnchorYHelpSelector(selector));
    },
    clickYPopover: function(selector) {
        return browser.click(this._getAnchorYPopoverSector(selector));
    },
    getTitleBox: function() {
        return element(by.css('.popover .se-popover__title'));
    },
    getTitleText: function() {
        return this.getTitleBox('.popover .se-popover__title').getText();
    },
    getBodyText: function() {
        return element(by.css('.popover .se-popover__content'))
            .getText()
            .then(function(text) {
                return text.replace(/\n|\r/g, ' ');
            });
    }
};
