/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc overview
 * @name tabsetModule
 * @deprecated since 2005
 * @description
 * Use <se-tabs /> instead.
 *
 * E.g: for angularJS: <se-tabs [model]='myModel' [tabs-list]="tabsList" [num-tabs-displayed]="3"></se-tab>
 * E.g: for angular: <se-tabs [model]='myModel' [tabList]="tabsList" [numTabsDisplayed]="3"></se-tab>
 *
 *
 */
angular
    .module('tabsetModule', [])
    .component('yTabset', {
        transclude: false,
        templateUrl: 'yTabsetTemplate.html',
        controller: 'yTabsetController',
        controllerAs: 'yTabset',
        bindings: {
            model: '=',
            tabsList: '<',
            tabControl: '=',
            numTabsDisplayed: '@'
        }
    })
    .controller('yTabsetController', function() {});
