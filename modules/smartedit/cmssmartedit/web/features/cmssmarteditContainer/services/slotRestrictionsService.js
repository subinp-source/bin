/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('slotRestrictionsServiceModule', ['yLoDashModule'])
    .service('slotRestrictionsService', function(gatewayProxy) {
        this.getAllComponentTypesSupportedOnPage = function() {};

        this.getSlotRestrictions = function() {};

        gatewayProxy.initForService(
            this,
            ['getAllComponentTypesSupportedOnPage', 'getSlotRestrictions'],
            'SLOT_RESTRICTIONS'
        );
    });
