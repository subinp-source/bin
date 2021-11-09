/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('nestedComponentModule', ['cmsSmarteditServicesModule', 'smarteditServicesModule'])
    .constant('ON_EDIT_NESTED_COMPONENT_EVENT', 'ON_EDIT_NESTED_COMPONENT')
    .controller('nestedComponentController', function(
        ON_EDIT_NESTED_COMPONENT_EVENT,
        systemEventService
    ) {
        // ---------------------------------------------------------------
        // Constants
        // ---------------------------------------------------------------

        // ---------------------------------------------------------------
        // Variables
        // ---------------------------------------------------------------

        // ---------------------------------------------------------------
        // Lifecycle Methods
        // ---------------------------------------------------------------

        this.onComponentClick = function() {
            if (this.isSelected) {
                systemEventService.publishAsync(ON_EDIT_NESTED_COMPONENT_EVENT, {
                    qualifier: this.qualifier,
                    item: this.item
                });
            }
        };
    })
    .component('nestedComponent', {
        controller: 'nestedComponentController',
        templateUrl: 'nestedComponentTemplate.html',
        bindings: {
            item: '<',
            qualifier: '<',
            isSelected: '<'
        }
    });
