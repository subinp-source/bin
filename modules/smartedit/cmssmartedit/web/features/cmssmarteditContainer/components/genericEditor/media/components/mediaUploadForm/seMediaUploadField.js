/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('seMediaUploadFieldModule', ['cmsSmarteditServicesModule'])
    .controller('seMediaUploadFieldController', function(assetsService) {
        this.displayImage = false;
        this.assetsRoot = assetsService.getAssetsRoot();
    })
    .directive('seMediaUploadField', function() {
        return {
            templateUrl: 'seMediaUploadFieldTemplate.html',
            restrict: 'E',
            scope: {},
            bindToController: {
                field: '<',
                model: '<',
                error: '<'
            },
            controller: 'seMediaUploadFieldController',
            controllerAs: 'ctrl',
            link: function(scope, element, attrs, ctrl) {
                element.bind('mouseover', function() {
                    ctrl.displayImage = true;
                    scope.$digest();
                });
                element.bind('mouseout', function() {
                    ctrl.displayImage = false;
                    scope.$digest();
                });
            }
        };
    });
