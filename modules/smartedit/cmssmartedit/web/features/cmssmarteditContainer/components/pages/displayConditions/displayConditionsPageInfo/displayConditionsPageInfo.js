/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('displayConditionsPageInfoModule', [
        'cmssmarteditContainerTemplates',
        'displayConditionsPageInfoControllerModule',
        'yHelpModule'
    ])
    .component('displayConditionsPageInfo', {
        controller: 'displayConditionsPageInfoController',
        templateUrl: 'displayConditionsPageInfoTemplate.html',
        bindings: {
            pageName: '<',
            pageType: '<',
            isPrimary: '<'
        }
    });
