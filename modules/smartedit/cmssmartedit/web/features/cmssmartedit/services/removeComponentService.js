/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('removeComponentServiceModule', [
        'smarteditServicesModule',
        'removeComponentServiceInterfaceModule',
        'functionsModule',
        'resourceLocationsModule'
    ])
    .constant('COMPONENT_REMOVED_EVENT', 'componentRemovedEvent')
    /**
     * @ngdoc service
     * @name removeComponentService.removeComponentService
     *
     * @description
     * Service to remove a component from a slot
     */
    .factory('removeComponentService', function(
        crossFrameEventService,
        restServiceFactory,
        renderService,
        extend,
        gatewayProxy,
        $q,
        $log,
        RemoveComponentServiceInterface,
        systemEventService,
        componentInfoService,
        PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI,
        COMPONENT_REMOVED_EVENT
    ) {
        var REMOVE_COMPONENT_CHANNEL_ID = 'RemoveComponent';

        var RemoveComponentService = function(gatewayId) {
            this.gatewayId = gatewayId;

            gatewayProxy.initForService(this, ['removeComponent']);
        };

        RemoveComponentService = extend(RemoveComponentServiceInterface, RemoveComponentService);

        var restServiceForRemoveComponent = restServiceFactory.get(
            PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI +
                '/contentslots/:slotId/components/:componentId',
            'componentId'
        );

        RemoveComponentService.prototype.removeComponent = function(configuration) {
            return restServiceForRemoveComponent
                .remove({
                    slotId: configuration.slotId,
                    componentId: configuration.slotOperationRelatedId
                })
                .then(function() {
                    // This call will come from the cache.
                    return componentInfoService.getById(configuration.componentUuid);
                })
                .then(function(component) {
                    systemEventService.publish(COMPONENT_REMOVED_EVENT, component);

                    // This will now come from the backend.
                    return componentInfoService.getById(configuration.componentUuid, true);
                })
                .then(function(component) {
                    renderService.renderSlots(configuration.slotId);

                    return component;
                });
        };

        return new RemoveComponentService(REMOVE_COMPONENT_CHANNEL_ID);
    });
