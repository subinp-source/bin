/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('externalComponentButtonModule', ['smarteditServicesModule'])
    .controller('externalComponentButtonController', function(l10nFilter, catalogService) {
        this.$onInit = function() {
            this.isReady = false;

            return catalogService.getCatalogVersionByUuid(this.catalogVersionUuid).then(
                function(catalogVersion) {
                    this.catalogVersion =
                        l10nFilter(catalogVersion.catalogName) +
                        ' (' +
                        catalogVersion.version +
                        ')';
                    this.isReady = true;
                }.bind(this)
            );
        };
    })
    .component('externalComponentButton', {
        templateUrl: 'externalComponentButtonTemplate.html',
        controller: 'externalComponentButtonController',
        controllerAs: 'ctrl',
        bindings: {
            catalogVersionUuid: '<'
        }
    });
