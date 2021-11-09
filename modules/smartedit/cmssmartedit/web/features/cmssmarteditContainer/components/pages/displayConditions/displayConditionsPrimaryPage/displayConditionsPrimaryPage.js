/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('displayConditionsPrimaryPageModule', [
        'cmssmarteditContainerTemplates',
        'displayConditionsPrimaryPageControllerModule'
    ])
    .component('displayConditionsPrimaryPage', {
        templateUrl: 'displayConditionsPrimaryPageTemplate.html',
        controller: 'displayConditionsPrimaryPageController',
        bindings: {
            readOnly: '<',
            associatedPrimaryPage: '<',
            onPrimaryPageSelect: '&?',
            pageType: '<'
        }
    });
