/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('seFileSelectorModule', [])
    .controller('seFileSelectorController', function() {
        this.$onInit = function() {
            this.disabled = this.disabled || false;
            this.customClass = this.customClass || '';
            this.selectionMode = this.selectionMode || 'replace';
        };

        this.buildAcceptedFileTypesList = function() {
            return this.acceptedFileTypes
                .map(function(fileType) {
                    return '.' + fileType;
                })
                .join(',');
        };

        this.isReplaceMode = function() {
            return this.selectionMode === 'replace';
        };
    })
    .directive('seFileSelector', function($timeout) {
        return {
            templateUrl: 'seFileSelectorTemplate.html',
            restrict: 'E',
            scope: {},
            controller: 'seFileSelectorController',
            controllerAs: 'ctrl',
            bindToController: {
                selectionMode: '<?',
                labelI18nKey: '<',
                acceptedFileTypes: '<',
                customClass: '<?',
                disabled: '<?',
                onFileSelect: '&'
            },
            link: function($scope, $element) {
                $element.find('input').on('change', function(event) {
                    $timeout(function() {
                        $scope.ctrl.onFileSelect({
                            files: event.target.files
                        });
                        var input = $element.find('input');
                        input.replaceWith(input.clone(true));
                    });
                });
            }
        };
    });
