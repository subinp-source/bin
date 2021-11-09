/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('seMediaPrinterModule', ['seMediaPreviewModule'])
    .controller('seMediaPrinterController', function($scope) {
        this.$onChanges = function() {
            $scope.selected = this.selected;
            $scope.item = this.item;
            $scope.ySelect = this.ySelect;
        };
    })
    .component('seMediaPrinter', {
        templateUrl: 'seMediaPrinterTemplate.html',
        controller: 'seMediaPrinterController',
        controllerAs: 'printer',
        require: {
            ySelect: '^ySelect'
        },
        bindings: {
            selected: '<',
            item: '<'
        }
    });
