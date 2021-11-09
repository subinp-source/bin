/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('tabsetApp', [
        'templateCacheDecoratorModule',
        'ui.bootstrap',
        'tabsetModule',
        'translationServiceModule'
    ])
    .controller('defaultController', function() {
        var vm = this;

        // Register tabs (This will be done somewhere else)
        vm.tabsList = [
            {
                id: 'tab1',
                title: 'tab1.name',
                templateUrl: 'tab1Template.html',
                hasErrors: false
            },
            {
                id: 'tab2',
                title: 'tab2.name',
                templateUrl: 'tab2Template.html',
                hasErrors: false
            },
            {
                id: 'tab3',
                title: 'tab3.name',
                templateUrl: 'tab3Template.html',
                hasErrors: false
            },
            {
                id: 'tab4',
                title: 'tab4.name',
                templateUrl: 'tab4Template.html',
                hasErrors: false
            }
        ];

        vm.tabsetData = {
            someData: 'some data'
        };

        vm.addErrorToTab = function(tabIndex) {
            vm.tabsList = vm.tabsList.map(function(tab, id) {
                if (id === tabIndex) {
                    tab.hasErrors = true;
                }
                return tab;
            });
        };

        vm.resetErrors = function() {
            vm.tabsList = vm.tabsList.map(function(tab) {
                tab.hasErrors = false;
                return tab;
            });
        };
    })
    .directive('testTab', function() {
        return {
            restrict: 'E',
            transclude: false,
            scope: {
                innerSave: '=',
                innerReset: '='
            },
            link: function() {}
        };
    })
    .directive('testTab2', function() {
        return {
            restrict: 'E',
            transclude: false,
            scope: {
                insideSave: '=',
                insideReset: '='
            },
            template: '<div>Tab 2 Content</div>',
            link: function() {}
        };
    })
    .directive('testTab3', function() {
        return {
            restrict: 'E',
            transclude: false,
            scope: {
                insideSave: '=',
                insideReset: '='
            },
            template: '<div>Tab 3 Content</div>',
            link: function() {}
        };
    })
    .run(function($templateCache) {
        'use strict';

        $templateCache.put(
            'tab1Template.html',
            "<test-tab class='sm-tab-content' inner-save='onSave' inner-reset='onReset'>Tab 1 Content</test-tab>"
        );

        $templateCache.put(
            'tab2Template.html',
            '<div>' +
                "<test-tab2 class='sm-tab-content' inside-save='onSave' inside-reset='onReset'></test-tab2>" +
                '</div>'
        );

        $templateCache.put(
            'tab3Template.html',
            '<div>' +
                "<test-tab3 class='sm-tab-content' inside-save='onSave' inside-reset='onReset'></test-tab3>" +
                '</div>'
        );

        $templateCache.put(
            'tab4Template.html',
            '<div>' +
                "<test-tab1 class='sm-tab-content' inside-save='onSave' inside-reset='onReset'>Tab 4 (Repeats 1)</test-tab1>" +
                '</div>'
        );
    });
