/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('abAnalyticsDecoratorModule', [
        'ysmarteditmoduleTemplates',
        'ui.bootstrap',
        'pascalprecht.translate',
        'abAnalyticsDecoratorControllerModule'
    ])
    .directive('abAnalyticsDecorator', function() {
        return {
            templateUrl: 'abAnalyticsDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            controller: 'abAnalyticsDecoratorController',
            controllerAs: '$ctrl',
            bindToController: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                smarteditProperties: '@',
                active: '<'
            }
        };
    });
