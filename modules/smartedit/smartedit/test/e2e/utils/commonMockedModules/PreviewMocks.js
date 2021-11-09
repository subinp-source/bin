/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('PreviewMocksModule', ['functionsModule'])
    .constant('STOREFRONT_URI', 'http://127.0.0.1:9000/test/utils/storefront.html')
    .run(function(
        httpBackendService,
        PREVIEW_RESOURCE_URI,
        STOREFRONT_URI,
        resourceLocationToRegex
    ) {
        httpBackendService
            .whenPOST(resourceLocationToRegex(PREVIEW_RESOURCE_URI))
            .respond(function(method, url, data) {
                var returnedPayload = angular.extend({}, data, {
                    ticketId: 'dasdfasdfasdfa',
                    resourcePath: STOREFRONT_URI
                });

                return [200, returnedPayload];
            });
    });

angular.module('smarteditloader').requires.push('PreviewMocksModule');
angular.module('smarteditcontainer').requires.push('PreviewMocksModule');
