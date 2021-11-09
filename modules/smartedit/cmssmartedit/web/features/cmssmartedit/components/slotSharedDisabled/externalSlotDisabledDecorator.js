/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('externalSlotDisabledDecoratorModule', ['slotDisabledDecoratorModule'])
    .directive('externalSlotDisabledDecorator', function() {
        return {
            templateUrl: 'externalSlotDisabledDecoratorTemplate.html',
            transclude: true,
            restrict: 'C',
            controllerAs: 'ctrl',
            controller: function() {},
            scope: {},
            bindToController: {
                active: '=',
                componentAttributes: '<'
            }
        };
    });
