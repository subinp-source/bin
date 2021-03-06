/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name slotVisibilityServiceModule
 * @description
 *
 * The slot visibility service module provides factories and services to manage all backend calls and loads an internal
 * structure that provides the necessary data to the slot visibility button and slot visibility component.
 */
angular
    .module('slotVisibilityServiceModule', ['resourceModule', 'cmsSmarteditServicesModule'])
    /**
     * @ngdoc service
     * @name slotVisibilityServiceModule.service:slotVisibilityService
     * @description
     *
     * The slotVisibilityService provides methods to reload slot information and manage hidden components.
     */
    .service('slotVisibilityService', function(
        COMPONENT_CREATED_EVENT,
        COMPONENT_UPDATED_EVENT,
        COMPONENT_REMOVED_EVENT,
        $log,
        crossFrameEventService,
        componentHandlerService,
        pageInfoService,
        pageContentSlotsComponentsRestService
    ) {
        /**
         * Function that filters the given SlotsToComponentsMap to return only those components that are hidden in the storefront.
         * @param {Object} allSlotsToComponentsMap object containing slotId - components list.
         *
         * @return {Object} allSlotsToComponentsMap object containing slotId - components list.
         */
        var _filterVisibleComponents = function(allSlotsToComponentsMap) {
            //filter allSlotsToComponentsMap to show only hidden components
            Object.keys(allSlotsToComponentsMap).forEach(function(slotId) {
                var componentsOnDOM = [];
                componentHandlerService
                    .getOriginalComponentsWithinSlot(slotId)
                    .get()
                    .forEach(function(component) {
                        componentsOnDOM.push(componentHandlerService.getId(component));
                    });

                var hiddenComponents = allSlotsToComponentsMap[slotId].filter(function(component) {
                    return componentsOnDOM.indexOf(component.uid) === -1;
                });

                allSlotsToComponentsMap[slotId] = hiddenComponents;
            });

            return allSlotsToComponentsMap;
        };

        /**
         * @ngdoc method
         * @name slotVisibilityServiceModule.service:slotVisibilityService#reloadSlotsInfo
         * @methodOf slotVisibilityServiceModule.service:slotVisibilityService
         *
         * @description
         * Reloads and cache's the pagesContentSlotsComponents for the current page in context.
         * this method can be called when ever a component is added or modified to the slot so that the pagesContentSlotsComponents is re-evalated.
         *
         * @return {Promise} A promise that resolves to the contentSlot - Components [] map for the page in context.
         */
        this.reloadSlotsInfo = function() {
            return pageInfoService.getPageUID().then(
                function(pageUID) {
                    pageContentSlotsComponentsRestService.clearCache();
                    return pageContentSlotsComponentsRestService.getSlotsToComponentsMapForPageUid(
                        pageUID
                    );
                }.bind(this),
                function(e) {
                    $log.error(
                        'slotVisibilityService::reloadSlotsInfo - failed call to pageInfoService.getPageUID'
                    );
                    throw e;
                }
            );
        };

        /**
         * Function to load slot to component map for the current page in context
         *
         * @return {Promise}
         */
        var _getSlotToComponentsMap = function() {
            return pageInfoService.getPageUID().then(
                function(pageUID) {
                    return pageContentSlotsComponentsRestService.getSlotsToComponentsMapForPageUid(
                        pageUID
                    );
                }.bind(this),
                function(e) {
                    $log.error(
                        'slotVisibilityService::_getSlotToComponentsMap - failed call to pageInfoService.getPageUID'
                    );
                    throw e;
                }
            );
        };

        /**
         * @ngdoc method
         * @name slotVisibilityServiceModule.service:slotVisibilityService#getHiddenComponents
         * @methodOf slotVisibilityServiceModule.service:slotVisibilityService
         *
         * @description
         * Returns the list of hidden components for a given slotId
         *
         * @param {String} slotId the slot id
         *
         * @return {Promise} A promise that resolves to a list of hidden components for the slotId
         */
        this.getHiddenComponents = function(slotId) {
            return _getSlotToComponentsMap()
                .then(_filterVisibleComponents)
                .then(
                    function(hiddenComponentsMap) {
                        return hiddenComponentsMap[slotId] || [];
                    },
                    function() {
                        return [];
                    }
                );
        };

        this._clearComponentsCache = function() {
            pageContentSlotsComponentsRestService.clearCache();
        };

        crossFrameEventService.subscribe(COMPONENT_CREATED_EVENT, this._clearComponentsCache);
        crossFrameEventService.subscribe(COMPONENT_UPDATED_EVENT, this._clearComponentsCache);
        crossFrameEventService.subscribe(COMPONENT_REMOVED_EVENT, this._clearComponentsCache);
    });
