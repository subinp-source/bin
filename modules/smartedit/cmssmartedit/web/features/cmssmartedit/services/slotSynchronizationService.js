/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('slotSynchronizationServiceModule', [])
    .constant(
        'SYNCHRONIZATION_SLOTS_SELECT_ALL_COMPONENTS_LABEL',
        'se.cms.synchronization.slots.select.all.components'
    )
    .service('slotSynchronizationService', function(
        SYNCHRONIZATION_SLOTS_SELECT_ALL_COMPONENTS_LABEL,
        syncPollingService
    ) {
        this.getSyncStatus = function(pageUUID, slotId) {
            return syncPollingService.getSyncStatus(pageUUID).then(function(syncStatus) {
                var slotSyncStatus =
                    (syncStatus.selectedDependencies || [])
                        .concat(syncStatus.sharedDependencies || [])
                        .find(function(slot) {
                            return slot.name === slotId;
                        }) || {};
                slotSyncStatus.selectAll = SYNCHRONIZATION_SLOTS_SELECT_ALL_COMPONENTS_LABEL;
                return slotSyncStatus;
            });
        };

        this.performSync = function(array, uriContext) {
            return syncPollingService.performSync(array, uriContext);
        };
    });
