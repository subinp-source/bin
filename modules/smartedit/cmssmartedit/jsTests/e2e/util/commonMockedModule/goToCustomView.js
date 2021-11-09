/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
angular
    .module('customViewModule', []) // this module will be overriden by custom views
    .constant('PATH_TO_CUSTOM_VIEW', null); //set constant to null by default. custom view will override it.

angular.module('customViewHTML', []);
angular
    .module('goToCustomView', ['ngRoute', 'customViewModule', 'customViewHTML'])
    .config(function($routeProvider, PATH_TO_CUSTOM_VIEW) {
        $routeProvider.when('/customView', {
            templateUrl: PATH_TO_CUSTOM_VIEW,
            controller: 'customViewController',
            controllerAs: 'controller'
        });
    })
    .run(function($location, PATH_TO_CUSTOM_VIEW) {
        if (PATH_TO_CUSTOM_VIEW !== null) {
            $location.path('/customView');
        }
    });

angular.module('smarteditcontainer').requires.push('customViewHTML');
angular.module('smarteditcontainer').requires.push('goToCustomView');
