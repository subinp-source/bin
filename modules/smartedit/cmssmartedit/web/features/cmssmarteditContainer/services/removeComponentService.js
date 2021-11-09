/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('removeComponentServiceModule', [
        'removeComponentServiceInterfaceModule',
        'smarteditServicesModule'
    ])

    .factory('removeComponentService', function(
        $q,
        $log,
        extend,
        gatewayProxy,
        RemoveComponentServiceInterface
    ) {
        var REMOVE_COMPONENT_CHANNEL_ID = 'RemoveComponent';

        var removeComponentService = function(gatewayId) {
            this.gatewayId = gatewayId;

            gatewayProxy.initForService(this, ['removeComponent']);
        };

        removeComponentService = extend(RemoveComponentServiceInterface, removeComponentService);

        return new removeComponentService(REMOVE_COMPONENT_CHANNEL_ID);
    });
