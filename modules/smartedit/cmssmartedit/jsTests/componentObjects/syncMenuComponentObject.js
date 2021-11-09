/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var obj = {};

    var modeSelector, synchronizationPanel;
    if (typeof require !== 'undefined') {
        modeSelector = require('./modeSelectorComponentObject');
        synchronizationPanel = require('./synchronizationPanelComponentObject');
    }

    obj.constants = {
        BUTTON_SELECTOR: '[data-item-key="se.cms.pageSyncMenu"] > button'
    };

    obj.elements = {
        getPanel: function() {
            return element(by.css('synchronization-panel'));
        },
        getPanelHeader: function() {
            return element(by.css('.se-sync-panel__sub-header'));
        },
        getPanelMessage: function() {
            return element(by.css('.se-sync-panel__message'));
        },
        getSyncCautionIcon: function() {
            return element(by.css('.se-toolbar-menu-ddlb--button__caution'));
        },
        getSyncToolbarMenuPanel: function() {
            return element(by.css('page-sync-menu-toolbar-item'));
        },
        getPanelTitleTooltip: function() {
            return obj.elements
                .getSyncToolbarMenuPanel()
                .element(by.css('.se-sync-menu-content__header se-help'));
        },
        getSyncToolbarButton: function() {
            return element(by.css(obj.constants.BUTTON_SELECTOR));
        }
    };

    obj.actions = {
        click: function() {
            return browser.switchToParent().then(function() {
                browser.click(obj.constants.BUTTON_SELECTOR);
                browser.click(obj.constants.BUTTON_SELECTOR); //Open and close dropdown to ensure the menu is made available
                return browser.click(obj.constants.BUTTON_SELECTOR);
            });
        },
        prepareApp: function(done, syncPermissions) {
            browser.executeScript(
                'window.sessionStorage.setItem("syncPermissions", arguments[0])',
                JSON.stringify(syncPermissions)
            );
            browser.waitForWholeAppToBeReady().then(function() {
                modeSelector.selectAdvancedPerspective().then(function() {
                    synchronizationPanel.setupTest();
                    done();
                });
            });
        }
    };

    obj.assertions = {
        syncCautionIconIsDisplayed: function() {
            expect(browser.isPresent(obj.elements.getSyncCautionIcon())).toBe(true);
        },
        syncCautionIconIsHidden: function() {
            expect(browser.isAbsent(obj.elements.getSyncCautionIcon())).toBe(true);
        },
        syncPanelIsPresent: function() {
            expect(browser.isPresent(obj.elements.getPanel())).toBe(true);
        },
        syncPanelHeaderContains: function(text) {
            expect(obj.elements.getPanelHeader().getText()).toEqual(text);
        },
        syncPanelMessageContains: function(text) {
            expect(obj.elements.getPanelMessage().getText()).toEqual(text);
        },
        syncPanelTitleTooltipToBePresent: function(present) {
            if (present === undefined) {
                throw new Error('present argument undefined');
            }
            expect(browser.isPresent(obj.elements.getPanelTitleTooltip())).toBe(present);
        },
        syncToolbarButtonToBeAbsent: function() {
            expect(browser.isAbsent(obj.elements.getSyncToolbarButton())).toBe(true);
        },
        syncToolbarButtonToBePresent: function() {
            expect(browser.isPresent(obj.elements.getSyncToolbarButton())).toBe(true);
        }
    };

    return obj;
})();
