/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('innerExtendingModule', []);
angular
    .module('textDisplayDecorator', ['decoratortemplates', 'translationServiceModule'])
    .run(function(DummyServiceClass) {
        DummyServiceClass.prototype.getDecoratorClass = function() {
            console.info('override');
            return 'redBackground';
        };
    })
    .directive('textDisplay', function() {
        return {
            templateUrl: 'textDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            bindToController: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },
            controllerAs: 'cont',
            controller: function(DummyServiceClass) {
                var service = new DummyServiceClass();
                this.$onInit = function() {
                    this.textDisplayContent =
                        this.smarteditComponentId + '_Text_from_overriden_dummy_decorator';
                    this.class = service.getDecoratorClass();
                };
            }
        };
    });
