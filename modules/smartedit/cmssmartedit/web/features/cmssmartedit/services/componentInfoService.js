/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('componentInfoServiceModule', ['yLoDashModule', 'cmsSmarteditServicesModule'])
    /**
     * This service is used to fetch and cache components information.
     * This service keeps track of components added, edited and removed. It also automatically fetches and caches components when they are visible
     * in the viewport (and invalidates them).
     *
     * This service is intended to be used to improve the performance of the application by reducing the number of xhr calls to the cmsitems api.
     * Example:
     * - a component in the overlay that is doing a fetch to the cmsitems api should use this service instead of using cmsitemsRestService.
     *   When a lot of components are rendered in the overlay we want to avoid one xhr call per component, but instead use this service that is listening
     *   to the 'OVERLAY_RERENDERED_EVENT' and fetch components information in batch (POST to cmsitems endpoint with an Array of uuids).
     */
    .service('componentInfoService', function(
        $log,
        $q,
        yjQuery,
        lodash,
        crossFrameEventService,
        cmsitemsRestService,
        UUID_ATTRIBUTE,
        EVENTS,
        OVERLAY_RERENDERED_EVENT,
        COMPONENT_CREATED_EVENT,
        COMPONENT_UPDATED_EVENT,
        COMPONENT_REMOVED_EVENT
    ) {
        var cachedComponents = {};
        var deferredMap = {};

        // returns a Promise that will be resolved only if the component was added previously in the overlay and if not will resolve only when the component is added to the overlay.
        /**
         * @internal
         * Returns a Promise that will be resolved with the component identified by the given uuid.
         * When called this method works like this:
         * - If the component is in the cache, the promise resolves right away.
         * - If the component is not in the cache, and the forceRetrieval flag is not set, this method won't call the cmsItem backend API right away.
         *   Instead, it waits until the component is cached (e.g., it is added to the overlay).
         * - If the forceRetrieval flag is set, then the method will call the cmsItem backend API right away.
         *
         * @param uuid The uuid of the item to retrieve
         * @param forceRetrieval Boolean flag. It specifies whether to retrieve the cmsItem right away.
         *
         */
        this.getById = function(uuid, forceRetrieval) {
            if (
                !forceRetrieval &&
                !cachedComponents[uuid] &&
                !document.querySelectorAll('[' + UUID_ATTRIBUTE + "='" + uuid + "']").length
            ) {
                // For hidden components that are not present in the DOM
                forceRetrieval = true;
            }
            if (cachedComponents[uuid]) {
                return $q.when(cachedComponents[uuid]);
            } else if (forceRetrieval) {
                return cmsitemsRestService.getById(uuid).then(
                    function(data) {
                        this._resolvePromises(data);
                        return cachedComponents[uuid];
                    }.bind(this),
                    function(error) {
                        this._rejectPromises([uuid], error);
                        throw error;
                    }.bind(this)
                );
            } else {
                var deferred = deferredMap[uuid] || $q.defer();
                if (!deferredMap[uuid]) {
                    deferredMap[uuid] = deferred;
                }
                return deferred.promise;
            }
        };

        this._resolvePromises = function(data) {
            (data.response ? data.response : [data]).forEach(function(component) {
                cachedComponents[component.uuid] = component;
                if (deferredMap[component.uuid]) {
                    deferredMap[component.uuid].resolve(component);
                    delete deferredMap[component.uuid];
                }
            });
        };

        this._rejectPromises = function(uuids, error) {
            $log.error('componentInfoService:: getById error:', error.message);
            uuids.forEach(function(uuid) {
                if (deferredMap[uuid]) {
                    deferredMap[uuid].reject(error);
                    delete deferredMap[uuid];
                }
            });
        };

        this._getComponentsDataByUUIDs = function(uuids) {
            cmsitemsRestService.getByIds(uuids).then(
                this._resolvePromises,
                function(e) {
                    this._rejectPromises(uuids, e);
                }.bind(this)
            );
        };

        this._onComponentsAddedToOverlay = function(addedComponentsDomElements) {
            var uuids = lodash
                .map(addedComponentsDomElements, function(component) {
                    return yjQuery(component).attr(UUID_ATTRIBUTE);
                })
                .filter(function(uuid) {
                    return !lodash.includes(Object.keys(cachedComponents), uuid);
                });
            if (uuids.length) {
                this._getComponentsDataByUUIDs(uuids);
            }
        };

        // delete from the cache the components that were removed from the DOM
        // note: components that are still in the DOM were only removed from the overlay
        this._onComponentsRemovedFromOverlay = function(removedComponentsDomElements) {
            removedComponentsDomElements
                .filter(function(component) {
                    return !yjQuery.find(
                        '[' + UUID_ATTRIBUTE + "='" + yjQuery(component).attr(UUID_ATTRIBUTE) + "']"
                    ).length;
                })
                .filter(function(component) {
                    return lodash.includes(
                        Object.keys(cachedComponents),
                        yjQuery(component).attr(UUID_ATTRIBUTE)
                    );
                })
                .map(function(component) {
                    return yjQuery(component).attr(UUID_ATTRIBUTE);
                })
                .forEach(function(uuid) {
                    delete cachedComponents[uuid];
                });
        };

        this._forceAddComponent = function(cmsComponentToAdd) {
            this._resolvePromises({
                response: [cmsComponentToAdd]
            });
        };

        this._forceRemoveComponent = function(componentToRemove) {
            delete cachedComponents[componentToRemove.uuid];
        };

        this._isComponentCached = function(componentUuid) {
            return !!cachedComponents[componentUuid];
        };

        this._clearCache = function() {
            cachedComponents = {};
            deferredMap = {};
        };

        // components added & removed from overlay
        crossFrameEventService.subscribe(
            OVERLAY_RERENDERED_EVENT,
            function(evtId, data) {
                if (data) {
                    if (data.addedComponents && data.addedComponents.length) {
                        this._onComponentsAddedToOverlay(data.addedComponents);
                    }
                    if (data.removedComponents && data.removedComponents.length) {
                        this._onComponentsRemovedFromOverlay(data.removedComponents);
                    }
                }
            }.bind(this)
        );

        // Components added & removed from storefront page.
        this._onComponentAdded = function(eventId, data) {
            this._forceAddComponent(data);
        };

        this._onComponentRemoved = function(eventId, data) {
            this._forceRemoveComponent(data);
        };
        crossFrameEventService.subscribe(
            COMPONENT_CREATED_EVENT,
            this._onComponentAdded.bind(this)
        );
        crossFrameEventService.subscribe(
            COMPONENT_UPDATED_EVENT,
            this._onComponentAdded.bind(this)
        );
        crossFrameEventService.subscribe(
            COMPONENT_REMOVED_EVENT,
            this._onComponentRemoved.bind(this)
        );

        // clear cache
        crossFrameEventService.subscribe(EVENTS.PAGE_CHANGE, this._clearCache.bind(this));
        crossFrameEventService.subscribe(EVENTS.USER_HAS_CHANGED, this._clearCache.bind(this));
    });
