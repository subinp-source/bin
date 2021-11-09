/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('linkToggleModule', [])
    .controller('linkToggleController', function() {
        this.emptyUrlLink = function() {
            this.model.linkToggle.urlLink = '';
        };
        this.$onInit = function() {
            if (this.model.linkToggle === undefined) {
                this.model.linkToggle = {};
            }

            if (this.model.linkToggle.external === undefined) {
                this.model.linkToggle.external = true;
            }
        };
    })
    .component('linkToggle', {
        templateUrl: 'linkToggleTemplate.html',
        transclude: true,
        controller: 'linkToggleController',
        bindings: {
            field: '<',
            model: '<',
            editor: '<'
        }
    });
