/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = (function() {
    var componentObject = {};

    componentObject.selectors = {
        getDecoratorBySlotIdAndSyncStatus: function(slotId, isInSync) {
            return by.css(
                '[data-smartedit-component-id="' +
                    slotId +
                    '"] .sync-indicator-decorator.' +
                    (isInSync ? 'IN_SYNC' : 'NOT_SYNC')
            );
        },
        getDecoratorStatusBySlotId: function(slotId) {
            return element(componentObject.selectors.getDecoratorBySlotId(slotId)).getAttribute(
                'data-sync-status'
            );
        }
    };

    componentObject.assertions = {
        slotIsOutOfSync: function(slotId) {
            return browser.waitToBeDisplayed(
                componentObject.selectors.getDecoratorBySlotIdAndSyncStatus(slotId, false),
                'expected sync decorator on slot ' + slotId
            );
        },
        slotIsInSync: function(slotId) {
            return browser.waitToBeDisplayed(
                componentObject.selectors.getDecoratorBySlotIdAndSyncStatus(slotId, true),
                'expected sync decorator on slot ' + slotId
            );
        }
    };

    return componentObject;
})();
