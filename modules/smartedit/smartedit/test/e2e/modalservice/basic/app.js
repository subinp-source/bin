/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
window.smartedit = {
    i18nAPIRoot: 'somepath'
};

angular
    .module('app', [
        'templateCacheDecoratorModule',
        'modalServiceModule',
        'resourceLocationsModule',
        'smarteditServicesModule'
    ])
    .directive('configuration', function() {
        return {
            templateUrl: 'test/e2e/modalservice/basic/contentTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: true,
            scope: {
                name: '=',
                cancel: '='
            },
            link: function($scope) {
                $scope.directiveCancel = function() {
                    $scope.cancel({
                        message: 'Cancelled by ' + $scope.name
                    });
                };
            }
        };
    })
    .controller('testController', function($scope, modalManager) {
        $scope.name = 'Not John';
        $scope.directiveCancel = function() {
            modalManager.dismiss({
                message: 'Cancelled by ' + $scope.name
            });
        };
    })
    .controller('defaultController', function(
        $rootScope,
        $scope,
        httpBackendService,
        modalService
    ) {
        function MyModalController(modalManager) {
            this.modalManager = modalManager;
            this.parentName = 'John';

            this.dismiss = function() {
                modalManager.dismiss({ message: 'Cancelled by ' + this.parentName });
            }.bind(this);
        }

        httpBackendService.whenGET(/Template/).passThrough();

        $scope.openModal1 = function() {
            modalService
                .open({
                    templateUrl: 'test/e2e/modalservice/basic/testTemplate.html',
                    controller: MyModalController
                })
                .then(
                    function(result) {
                        $scope.message = result ? result.message : '';
                    },
                    function(failure) {
                        $scope.failure = failure ? failure.message : '';
                    }
                );
        };
        $scope.openModal2 = function() {
            modalService
                .open({
                    templateUrl: 'test/e2e/modalservice/basic/contentTemplate.html',
                    controller: 'testController'
                })
                .then(
                    function(result) {
                        $scope.message = result ? result.message : '';
                    },
                    function(failure) {
                        $scope.failure = failure ? failure.message : '';
                    }
                );
        };
        $scope.openModal3 = function() {
            modalService
                .open({
                    templateUrl: 'test/e2e/modalservice/basic/contentTemplate.html',
                    controller: function($scope, modalManager) {
                        $scope.name = 'John Snow';
                        $scope.directiveCancel = function() {
                            modalManager.dismiss({
                                message: 'Cancelled by ' + $scope.name
                            });
                        };
                    }
                })
                .then(
                    function(result) {
                        $scope.message = result ? result.message : '';
                    },
                    function(failure) {
                        $scope.failure = failure ? failure.message : '';
                    }
                );
        };
        $scope.openModal4 = function() {
            modalService
                .open({
                    templateInline:
                        '<span>' +
                        "{{'hello.message' | translate}} {{name}}" +
                        "<button id='cancel' class='btn btn-primary' data-translate='modal.actions.cancel' data-ng-click='directiveCancel()' />" +
                        '</span>',
                    controller: function($scope, modalManager) {
                        $scope.name = 'Ned Stark';
                        $scope.directiveCancel = function() {
                            modalManager.dismiss({
                                message: 'Cancelled by ' + $scope.name
                            });
                        };
                    }
                })
                .then(
                    function(result) {
                        $scope.message = result ? result.message : '';
                    },
                    function(failure) {
                        $scope.failure = failure ? failure.message : '';
                    }
                );
        };
        $scope.openModal5 = function() {
            modalService
                .open({
                    templateInline:
                        "<configuration data-name='modalController.parentName' data-cancel='modalController.cancel' />",
                    controller: function(modalManager) {
                        this.parentName = 'Sansa Stark';
                        this.cancel = function() {
                            modalManager.dismiss({
                                message: 'Cancelled by ' + this.parentName
                            });
                        }.bind(this);
                    }
                })
                .then(
                    function(result) {
                        $scope.message = result ? result.message : '';
                    },
                    function(failure) {
                        $scope.failure = failure ? failure.message : '';
                    }
                );
        };
    });
