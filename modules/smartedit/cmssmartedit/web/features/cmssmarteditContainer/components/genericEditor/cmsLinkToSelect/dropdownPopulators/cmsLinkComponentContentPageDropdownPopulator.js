/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('cmsLinkComponentContentPageDropdownPopulatorModule', [
        'contextAwareCatalogModule',
        'dropdownPopulatorModule'
    ])
    .factory('CMSLinkComponentcontentPageDropdownPopulator', function(
        DropdownPopulatorInterface,
        extend,
        contextAwareCatalogService,
        uriDropdownPopulator
    ) {
        var dropdownPopulator = function() {};

        dropdownPopulator = extend(DropdownPopulatorInterface, dropdownPopulator);

        dropdownPopulator.prototype.fetchPage = function(payload) {
            return contextAwareCatalogService.getContentPageSearchUri().then(
                function(uri) {
                    payload.field.uri = uri;
                    return uriDropdownPopulator.fetchPage(payload);
                }.bind(this)
            );
        };

        dropdownPopulator.prototype.isPaged = function() {
            return true;
        };

        dropdownPopulator.prototype.getItem = function(payload) {
            return contextAwareCatalogService.getContentPageItemUri().then(
                function(uri) {
                    payload.field.uri = uri;
                    return uriDropdownPopulator.getItem(payload);
                }.bind(this)
            );
        };

        return new dropdownPopulator();
    });
