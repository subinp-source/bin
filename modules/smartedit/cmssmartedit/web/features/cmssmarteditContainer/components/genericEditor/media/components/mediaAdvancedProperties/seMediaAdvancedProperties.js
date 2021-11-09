/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('seMediaAdvancedPropertiesModule', ['ui.bootstrap'])
    .constant('seMediaAdvancedPropertiesConstants', {
        I18N_KEYS: {
            DESCRIPTION: 'se.media.advanced.information.description',
            CODE: 'se.media.advanced.information.code',
            ALT_TEXT: 'se.media.advanced.information.alt.text',
            ADVANCED_INFORMATION: 'se.media.advanced.information',
            INFORMATION: 'se.media.information'
        }
    })
    .controller('seMediaAdvancedPropertiesController', function(
        seMediaAdvancedPropertiesConstants
    ) {
        this.$onInit = function() {
            this.i18nKeys = seMediaAdvancedPropertiesConstants.I18N_KEYS;
        };
    })
    .component('seMediaAdvancedProperties', {
        bindings: {
            code: '=',
            description: '=',
            altText: '='
        },
        controller: 'seMediaAdvancedPropertiesController',
        controllerAs: 'ctrl',
        templateUrl: 'seMediaAdvancedPropertiesTemplate.html'
    });
