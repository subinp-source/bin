/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name DecoratorsModule
 * @description
 * Wires dummy decorators to dummy components provided in the dummy storefront.
 */
angular
    .module('DummyDecoratorsModule', [
        'textDisplayDecorator',
        'slotTextDisplayDecorator',
        'buttonDisplayDecorator',
        'slotButtonDisplayDecorator',
        'pageSpecificDecorator',
        'buttonDisplayAndRefreshDecorator'
    ])
    .run(function(decoratorService) {
        decoratorService.addMappings({
            componentType1: ['textDisplay', 'pageSpecific'],
            componentType2: ['buttonDisplay'],
            SimpleResponsiveBannerComponent: ['textDisplay', 'buttonDisplay'],
            componentType4: ['textDisplay', 'buttonDisplayAndRefresh'],
            ContentSlot: ['slotTextDisplay', 'slotButtonDisplay']
        });

        decoratorService.enable('textDisplay');
        decoratorService.enable('buttonDisplay');
        decoratorService.enable('slotTextDisplay');
        decoratorService.enable('slotButtonDisplay');
        decoratorService.enable('pageSpecific');
        decoratorService.enable('buttonDisplayAndRefresh');
    });

/*
 * Provides a simple text display decorator for components.
 */
angular
    .module('textDisplayDecorator', ['DummyDecoratorTemplatesModule', 'translationServiceModule'])
    .directive('textDisplay', function() {
        return {
            templateUrl: 'textDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },
            link: function($scope) {
                $scope.textDisplayContent = 'Text_is_been_displayed_TextDisplayDecorator';
            }
        };
    });

/*
 * Provides a simple text display decorator for slots.
 */
angular
    .module('slotTextDisplayDecorator', [
        'DummyDecoratorTemplatesModule',
        'translationServiceModule'
    ])
    .directive('slotTextDisplay', function() {
        return {
            templateUrl: 'textDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },
            link: function($scope) {
                $scope.textDisplayContent = 'slot_text_is_been_displayed_SlotTextDisplayDecorator';
            }
        };
    });

/*
 * Provides a simple button display decorator for components.
 */
angular
    .module('buttonDisplayDecorator', ['DummyDecoratorTemplatesModule', 'translationServiceModule'])
    .directive('buttonDisplay', function() {
        return {
            templateUrl: 'buttonDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },
            link: function($scope) {
                $scope.buttonDisplayContent = 'Button_is_been_Displayed';
            }
        };
    });

/*
 * Provides a simple button display decorator for slots.
 */
angular
    .module('slotButtonDisplayDecorator', [
        'DummyDecoratorTemplatesModule',
        'translationServiceModule'
    ])
    .directive('slotButtonDisplay', function() {
        return {
            templateUrl: 'buttonDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },
            link: function($scope) {
                $scope.buttonDisplayContent = 'Slot_button_is_been_Displayed';
            }
        };
    });

/*
 * Provides a simple button display decorator for slots.
 */
angular
    .module('pageSpecificDecorator', ['smarteditServicesModule'])
    .directive('pageSpecific', function(pageInfoService) {
        return {
            templateUrl: 'pageSpecificDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },
            link: function($scope) {
                pageInfoService.getPageUUID().then(function(pageUUID) {
                    $scope.pageUUID = pageUUID;
                });
            }
        };
    });

/* Provides text that can be changed by clicking a button. There's another button that will replay the decorator. */
angular
    .module('buttonDisplayAndRefreshDecorator', [
        'DummyDecoratorTemplatesModule',
        'translationServiceModule',
        'smarteditServicesModule'
    ])
    .directive('buttonDisplayAndRefresh', function(
        crossFrameEventService,
        EVENT_SMARTEDIT_COMPONENT_UPDATED
    ) {
        return {
            templateUrl: 'buttonAndRefreshDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },
            link: function($scope) {
                $scope.buttonDisplayContent = 'Button_is_been_Displayed';
                $scope.onButtonClicked = function() {
                    $scope.buttonDisplayContent = 'Button_has_been_Clicked';
                };

                $scope.onRefreshButtonClicked = function() {
                    crossFrameEventService.publish(EVENT_SMARTEDIT_COMPONENT_UPDATED, {
                        componentId: $scope.smarteditComponentId,
                        componentType: $scope.smarteditComponentType,
                        requiresReplayingDecorators: true
                    });
                };
            }
        };
    });

/*
 * Provides templates for the dummy decorators.
 */
angular.module('DummyDecoratorTemplatesModule', []).run(function($templateCache) {
    $templateCache.put(
        'textDisplayDecoratorTemplate.html',
        '<div >\n' + '<div data-ng-transclude></div>\n' + '{{textDisplayContent}}\n' + '</div>'
    );

    $templateCache.put(
        'buttonDisplayDecoratorTemplate.html',
        '<div>\n' +
            '<div data-ng-transclude></div>\n' +
            '<button>{{buttonDisplayContent}}</button>\n' +
            '</div>'
    );

    $templateCache.put(
        'pageSpecificDecoratorTemplate.html',
        '<div>\n' +
            '<div data-ng-transclude></div>\n' +
            '<label>pageUUID: {{pageUUID}}</label>\n' +
            '</div>'
    );

    $templateCache.put(
        'buttonAndRefreshDecoratorTemplate.html',
        '<div>\n' +
            '<div data-ng-transclude></div>\n' +
            "<button class='main-button' data-ng-click='onButtonClicked()'>{{buttonDisplayContent}}</button>\n" +
            "<button class='refresh-button' data-ng-click='onRefreshButtonClicked()'>Refresh Content</button>\n" +
            '</div>'
    );
});
