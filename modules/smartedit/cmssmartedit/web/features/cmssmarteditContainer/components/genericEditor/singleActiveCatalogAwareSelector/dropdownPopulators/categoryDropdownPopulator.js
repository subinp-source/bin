/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('categoryDropdownPopulatorModule', [
        'contextAwareCatalogModule',
        'dropdownPopulatorModule',
        'functionsModule'
    ])
    .factory('categoryDropdownPopulator', function(
        $q,
        DropdownPopulatorInterface,
        extend,
        contextAwareCatalogService,
        uriDropdownPopulator,
        languageService
    ) {
        var dropdownPopulator = function() {};
        dropdownPopulator = extend(DropdownPopulatorInterface, dropdownPopulator);

        dropdownPopulator.prototype.fetchPage = function(payload) {
            if (payload.model.productCatalog !== undefined) {
                return contextAwareCatalogService
                    .getProductCategorySearchUri(payload.model.productCatalog)
                    .then(
                        function(uri) {
                            payload.field.uri = uri;
                            return languageService.getResolveLocale().then(function(langIsoCode) {
                                payload.field.params.langIsoCode = langIsoCode;
                                return uriDropdownPopulator.fetchPage(payload);
                            }.bind(this));
                        }.bind(this)
                    );
            } else {
                return $q.when({});
            }
        };

        dropdownPopulator.prototype.isPaged = function() {
            return true;
        };

        dropdownPopulator.prototype.getItem = function(payload) {
            return contextAwareCatalogService.getProductCategoryItemUri().then(
                function(uri) {
                    payload.field.uri = uri;
                    return uriDropdownPopulator.getItem(payload);
                }.bind(this)
            );
        };

        return new dropdownPopulator();
    });
