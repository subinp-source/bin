/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('componentVisibilityAlertServiceModule', [
        'componentVisibilityAlertServiceInterfaceModule'
    ])

    .factory('componentVisibilityAlertService', function(
        ComponentVisibilityAlertServiceInterface,
        extend,
        gatewayProxy
    ) {
        var ComponentVisibilityAlertService = function() {
            this.gatewayId = 'ComponentVisibilityAlertService';
            gatewayProxy.initForService(this, ['checkAndAlertOnComponentVisibility']);
        };

        ComponentVisibilityAlertService = extend(
            ComponentVisibilityAlertServiceInterface,
            ComponentVisibilityAlertService
        );

        return new ComponentVisibilityAlertService();
    });
