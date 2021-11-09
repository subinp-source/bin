/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('goToCustomView', ['ngRoute', 'customViewModule'])
    .config(function($routeProvider, PATH_TO_CUSTOM_VIEW) {
        $routeProvider.when('/customView', {
            templateUrl: PATH_TO_CUSTOM_VIEW,
            controller: 'customViewController',
            controllerAs: 'controller'
        });
    })
    .run(function($location) {
        /*
         * FIXME :without a timeout the redirection of experienceService.loadExperience of e2eOnLoadingSetup.js
         * will trigger after the redirection hereunder
         * to be fixed durign test alignment.
         */
        setTimeout(function() {
            $location.path('/customView');
        }, 1000);
    });
angular.module('smarteditcontainer').requires.push('customViewModule');
angular.module('smarteditcontainer').requires.push('goToCustomView');
