/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name slotRestrictionsServiceModule
 * @description
 * # The slotRestrictionsServiceModule
 *
 * The slotRestrictionsServiceModule contains a service that caches and returns the restrictions of a slot in a page. This restrictions determine
 * whether a component of a certain type is allowed or forbidden in a particular slot.
 *
 */
/* jshint undef:false */

angular
    .module('slotRestrictionsServiceModule', [
        'yLoDashModule',
        'slotSharedServiceModule',
        'functionsModule',
        'cmsSmarteditServicesModule'
    ])
    .constant('CONTENT_SLOTS_TYPE_RESTRICTION_FETCH_LIMIT', 100)
    .constant('COMPONENT_IN_SLOT_STATUS', {
        ALLOWED: 'allowed',
        DISALLOWED: 'disallowed',
        MAYBEALLOWED: 'mayBeAllowed'
    })
    /**
     * @ngdoc service
     * @name slotRestrictionsServiceModule.service:slotRestrictionsService
     *
     * @description
     * This service provides methods that cache and return the restrictions of a slot in a page. This restrictions determine
     * whether a component of a certain type is allowed or forbidden in a particular slot.
     */
    .service('slotRestrictionsService', function(
        $log,
        lodash,
        isBlank,
        yjQuery,
        gatewayProxy,
        crossFrameEventService,
        EVENTS,
        componentHandlerService,
        pageInfoService,
        slotSharedService,
        restServiceFactory,
        pageContentSlotsComponentsRestService,
        typePermissionsRestService,
        CONTENT_SLOTS_TYPE_RESTRICTION_RESOURCE_URI,
        CONTENT_SLOTS_TYPE_RESTRICTION_FETCH_LIMIT,
        COMPONENT_IN_SLOT_STATUS,
        CONTENT_SLOT_TYPE
    ) {
        var _slotRestrictions = {};
        var _currentPageId = null;
        var _slotsRestrictionsRestService;
        /**
         * @ngdoc method
         * @name slotRestrictionsServiceModule.service:slotRestrictionsService#getAllComponentTypesSupportedOnPage
         * @methodOf slotRestrictionsServiceModule.service:slotRestrictionsService
         * @deprecated since 2005
         * @description
         * This methods retrieves the list of component types droppable in at least one of the slots of the current page
         * @returns {Promise} A promise containing an array with the component types droppable on the current page
         */
        this.getAllComponentTypesSupportedOnPage = function() {
            var slots = yjQuery(componentHandlerService.getAllSlotsSelector());
            var slotIds = Array.prototype.slice.call(
                slots.map(function() {
                    return componentHandlerService.getId(yjQuery(this));
                })
            );

            return Promise.all(
                slotIds.map(
                    function(slotId) {
                        return this.getSlotRestrictions(slotId);
                    }.bind(this)
                )
            ).then(
                function(arrayOfSlotRestrictions) {
                    return lodash.flatten(arrayOfSlotRestrictions);
                },
                function(error) {
                    $log.info(error);
                }
            );
        };

        /**
         * @ngdoc method
         * @name slotRestrictionsServiceModule.service:slotRestrictionsService#getSlotRestrictions
         * @methodOf slotRestrictionsServiceModule.service:slotRestrictionsService
         * @description
         * This methods retrieves the list of restrictions applied to the slot identified by the provided ID.
         *
         * @param {String} slotId The ID of the slot whose restrictions to retrieve.
         * @returns {Promise} A promise containing an array with the restrictions applied to the slot.
         */
        this.getSlotRestrictions = function(slotId) {
            return this._getPageUID(_currentPageId).then(
                function(pageId) {
                    _currentPageId = pageId;
                    var restrictionId = this._getEntryId(_currentPageId, slotId);
                    if (_slotRestrictions[restrictionId]) {
                        return Promise.resolve(_slotRestrictions[restrictionId]);
                    } else if (this._isExternalSlot(slotId)) {
                        _slotRestrictions[restrictionId] = [];
                        return Promise.resolve([]);
                    }
                    return Promise.resolve();
                }.bind(this)
            );
        };

        this._cacheSlotsRestrictions = function() {
            var originalSlotIds = componentHandlerService.getAllSlotUids() || [];
            originalSlotIds = originalSlotIds.filter(
                function(slotId) {
                    return !this._isExternalSlot(slotId, CONTENT_SLOT_TYPE);
                }.bind(this)
            );
            var uniqueSlotIds = lodash.uniq(originalSlotIds);
            var chunks = lodash.chunk(uniqueSlotIds, CONTENT_SLOTS_TYPE_RESTRICTION_FETCH_LIMIT);
            return this._recursiveFetchSlotsRestrictions(chunks, 0);
        };

        // Recursively fetch slots restrictions by the number of chunks of slotIds split by fetch limit
        this._recursiveFetchSlotsRestrictions = function(slotIdsByChunks, chunkIndex) {
            if (chunkIndex === slotIdsByChunks.length) {
                return Promise.resolve();
            }
            return this._fetchSlotsRestrictions(slotIdsByChunks[chunkIndex]).then(
                function() {
                    return this._recursiveFetchSlotsRestrictions(slotIdsByChunks, chunkIndex + 1);
                }.bind(this)
            );
        };

        // Fetch slot restriction and cache them in-memory
        this._fetchSlotsRestrictions = function(slotIds) {
            return this._getPageUID(_currentPageId).then(
                function(pageId) {
                    _currentPageId = pageId;

                    _slotsRestrictionsRestService =
                        _slotsRestrictionsRestService ||
                        restServiceFactory.get(
                            CONTENT_SLOTS_TYPE_RESTRICTION_RESOURCE_URI,
                            _currentPageId
                        );

                    return _slotsRestrictionsRestService
                        .save({
                            slotIds: slotIds,
                            pageUid: _currentPageId
                        })
                        .then(
                            function(response) {
                                var contentSlots = response || [];
                                contentSlots.forEach(
                                    function(slot) {
                                        var restrictionId = this._getEntryId(
                                            _currentPageId,
                                            slot.contentSlotUid
                                        );
                                        _slotRestrictions[restrictionId] = slot.validComponentTypes;
                                    }.bind(this)
                                );
                                return Promise.resolve();
                            }.bind(this),
                            function(error) {
                                $log.info(error);
                                return Promise.reject();
                            }
                        );
                }.bind(this)
            );
        };

        /**
         * @ngdoc method
         * @name slotRestrictionsServiceModule.service:slotRestrictionsService#isComponentAllowedInSlot
         * @methodOf slotRestrictionsServiceModule.service:slotRestrictionsService
         *
         * @description
         * This methods determines whether a component of the provided type is allowed in the slot.
         *
         * @param {Object} slot the slot for which to verify if it allows a component of the provided type.
         * @param {String} slot.id The ID of the slot.
         * @param {Array} slot.components the list of components contained in the slot, they must contain an "id" property.
         * @param {Object} dragInfo contains the dragged object information
         * @param {String} dragInfo.componentType The smartedit type of the component being checked.
         * @param {String} dragInfo.componentId The smartedit id of the component being checked.
         * @param {String} dragInfo.slotId The smartedit id of the slot from which the component originates
         * @param {String} dragInfo.cloneOnDrop The boolean that determines if the component should be cloned or not
         * @returns {Promise} A promise containing COMPONENT_IN_SLOT_STATUS (ALLOWED, DISALLOWED, MAYBEALLOWED) string that determines whether a component of the provided type is allowed in the slot.
         */
        this.isComponentAllowedInSlot = function(slot, dragInfo) {
            return this.getSlotRestrictions(slot.id).then(function(currentSlotRestrictions) {
                return pageContentSlotsComponentsRestService
                    .getComponentsForSlot(slot.id)
                    .then(function(componentsForSlot) {
                        var isComponentIdAllowed =
                            slot.id === dragInfo.slotId ||
                            !componentsForSlot.some(function(component) {
                                return component.uid === dragInfo.componentId;
                            });

                        if (isComponentIdAllowed) {
                            if (currentSlotRestrictions) {
                                return lodash.includes(
                                    currentSlotRestrictions,
                                    dragInfo.componentType
                                )
                                    ? Promise.resolve(COMPONENT_IN_SLOT_STATUS.ALLOWED)
                                    : Promise.resolve(COMPONENT_IN_SLOT_STATUS.DISALLOWED);
                            } else {
                                return Promise.resolve(COMPONENT_IN_SLOT_STATUS.MAYBEALLOWED);
                            }
                        }
                        return Promise.resolve(COMPONENT_IN_SLOT_STATUS.DISALLOWED);
                    });
            });
        };

        /**
         * @ngdoc method
         * @name slotRestrictionsServiceModule.service:slotRestrictionsService#isSlotEditable
         * @methodOf slotRestrictionsServiceModule.service:slotRestrictionsService
         *
         * @description
         * This method determines whether slot is editable or not.
         *
         * @param {String} slotId The ID of the slot.
         *
         * @returns {Promise} A promise containing a boolean flag that shows whether if the slot is editable or not.
         */
        this.isSlotEditable = function(slotId) {
            return typePermissionsRestService.hasUpdatePermissionForTypes([CONTENT_SLOT_TYPE]).then(
                function(slotPermissions) {
                    return slotSharedService.isSlotShared(slotId).then(
                        function(isShared) {
                            var result = slotPermissions[CONTENT_SLOT_TYPE];
                            if (isShared) {
                                var isExternalSlot = this._isExternalSlot(slotId);
                                result =
                                    result &&
                                    !isExternalSlot &&
                                    !slotSharedService.areSharedSlotsDisabled();
                            }

                            return result;
                        }.bind(this)
                    );
                }.bind(this)
            );
        };

        this._emptyCache = function() {
            _slotRestrictions = {};
            _currentPageId = null;
        };

        this._getEntryId = function(pageId, slotId) {
            return pageId + '_' + slotId;
        };

        this._isExternalSlot = function(slotId) {
            return componentHandlerService.isExternalComponent(slotId, CONTENT_SLOT_TYPE);
        };

        this._getPageUID = function(pageUID) {
            return !isBlank(pageUID) ? Promise.resolve(pageUID) : pageInfoService.getPageUID();
        };

        crossFrameEventService.subscribe(
            EVENTS.PAGE_CHANGE,
            function() {
                this._emptyCache();
                this._cacheSlotsRestrictions();
            }.bind(this)
        );

        gatewayProxy.initForService(
            this,
            ['getAllComponentTypesSupportedOnPage', 'getSlotRestrictions'],
            'SLOT_RESTRICTIONS'
        );
    });
