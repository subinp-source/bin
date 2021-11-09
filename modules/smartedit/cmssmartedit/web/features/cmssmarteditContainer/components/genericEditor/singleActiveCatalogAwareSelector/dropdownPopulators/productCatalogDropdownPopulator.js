/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('productCatalogDropdownPopulatorModule', [
        'dropdownPopulatorModule',
        'resourceLocationsModule',
        'functionsModule'
    ])
    .factory('productCatalogDropdownPopulator', function(
        DropdownPopulatorInterface,
        optionsDropdownPopulator,
        CONTEXT_SITE_ID,
        catalogService,
        extend
    ) {
        var dropdownPopulator = function() {};
        dropdownPopulator = extend(DropdownPopulatorInterface, dropdownPopulator);

        dropdownPopulator.prototype.fetchAll = function(payload) {
            return catalogService.getProductCatalogsBySiteKey(CONTEXT_SITE_ID).then(
                function(catalogs) {
                    payload.field.options = catalogs.filter(function(catalog) {
                        return (
                            catalog.versions.filter(function(version) {
                                return version.active === true;
                            }).length === 1
                        );
                    });
                    return optionsDropdownPopulator.populate(payload);
                }.bind(this)
            );
        };

        dropdownPopulator.prototype.isPaged = function() {
            return false;
        };

        return new dropdownPopulator();
    });
