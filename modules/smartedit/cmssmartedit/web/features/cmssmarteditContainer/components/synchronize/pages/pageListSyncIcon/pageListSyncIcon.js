/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('pageListSyncIconModule', ['cmsSmarteditServicesModule'])

    .controller('pageListSyncIconController', function(
        pageSynchronizationService,
        catalogService,
        SYNCHRONIZATION_STATUSES,
        SYNCHRONIZATION_POLLING,
        crossFrameEventService
    ) {
        this.unRegisterSyncPolling = angular.noop;
        this.classes = {};
        this.classes[SYNCHRONIZATION_STATUSES.UNAVAILABLE] = '';
        this.classes[SYNCHRONIZATION_STATUSES.IN_SYNC] = 'se-sync-btn__status--done';
        this.classes[SYNCHRONIZATION_STATUSES.NOT_SYNC] = 'se-sync-btn__status--not';
        this.classes[SYNCHRONIZATION_STATUSES.IN_PROGRESS] = 'se-sync-btn__status--not';
        this.classes[SYNCHRONIZATION_STATUSES.SYNC_FAILED] = 'se-sync-btn__status--not';

        this.fetchSyncStatus = function() {
            return pageSynchronizationService.getSyncStatus(this.pageId, this.uriContext).then(
                function(response) {
                    this.syncStatus = response;
                }.bind(this),
                function() {
                    this.syncStatus.status = SYNCHRONIZATION_STATUSES.UNAVAILABLE;
                }.bind(this)
            );
        }.bind(this);

        this.triggerFetch = function(eventId, eventData) {
            if (eventData.itemId === this.pageId) {
                this.fetchSyncStatus();
            }
        };

        this.$onInit = function() {
            catalogService.isContentCatalogVersionNonActive(this.uriContext).then(
                function(isNonActive) {
                    if (isNonActive) {
                        // set initial sync status to unavailable
                        this.syncStatus = {
                            status: SYNCHRONIZATION_STATUSES.UNAVAILABLE
                        };

                        this.unRegisterSyncPolling = crossFrameEventService.subscribe(
                            SYNCHRONIZATION_POLLING.FAST_FETCH,
                            this.triggerFetch.bind(this)
                        );

                        // the first sync fetch is done manually
                        this.fetchSyncStatus();
                    }
                }.bind(this)
            );
        };

        this.$onDestroy = function() {
            this.unRegisterSyncPolling();
        };
    })

    /**
     * @ngdoc directive
     * @name pageListSyncIconModule.directive:pageListSyncIcon
     * @restrict E
     * @element sync-icon
     *
     * @description
     * The Page Synchronization Icon component is used to display the icon that describes the synchronization status of a page.
     *
     * @param {string} pageId The identifier of the page for which the synchronzation status must be displayed.
     *
     */
    .component('pageListSyncIcon', {
        templateUrl: 'pageListSyncIconTemplate.html',
        controller: 'pageListSyncIconController',
        controllerAs: '$ctrl',
        bindings: {
            pageId: '<',
            uriContext: '<'
        }
    });
