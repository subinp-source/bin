/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('displayConditionsPageVariationsModule', [
        'cmssmarteditContainerTemplates',
        'displayConditionsPageVariationsControllerModule',
        'clientPagedListModule'
    ])
    .component('displayConditionsPageVariations', {
        templateUrl: 'displayConditionsPageVariationsTemplate.html',
        controller: 'displayConditionsPageVariationsController',
        bindings: {
            variations: '<'
        }
    });
