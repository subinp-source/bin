/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('componentSearchModule', ['smarteditServicesModule'])
    .controller('componentSearchController', function(systemEventService) {
        // Constants
        var RESET_COMPONENT_MENU_EVENT = 'RESET_COMPONENT_MENU_EVENT';

        // Methods
        this.$onInit = function() {
            this._oldValue = '';
            this._resetSearchBox();

            this.removeResetEventListener = systemEventService.subscribe(
                RESET_COMPONENT_MENU_EVENT,
                this._resetSearchBox
            );
        };

        this.$onDestroy = function() {
            this.removeResetEventListener();
        };

        this.$doCheck = function() {
            if (this.searchTerm !== this._oldValue) {
                this._oldValue = this.searchTerm;
                this.showResetButton = this.searchTerm !== '';
                this.onChange({
                    searchTerm: this.searchTerm
                });
            }
        };

        // Helper Methods
        this._resetSearchBox = function() {
            this.searchTerm = '';
        }.bind(this);
    })
    .component('componentSearch', {
        templateUrl: 'componentSearchTemplate.html',
        controller: 'componentSearchController',
        bindings: {
            onChange: '&',
            placeholder: '@'
        }
    });
