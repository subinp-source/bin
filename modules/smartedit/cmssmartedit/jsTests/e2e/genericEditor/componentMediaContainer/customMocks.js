/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('customMocksModule', ['backendMocksUtilsModule']).run(function(httpBackendService) {
    // Directly trying to find configuration mock cause there is no direct reference to it anywhere.
    var backendEntry = httpBackendService.backends.GET.find(function(backend) {
        return backend.pattern.toString() === '/configuration$/';
    });

    // Override configuration mock so that it can add additional payload to existing one based on
    // the global attribute advancedMediaContainerManagementEnabled.
    var oldRef = backendEntry.mock;
    backendEntry.mock = function(method, url, data, headers) {
        var result = oldRef(method, url, data, headers);

        var advancedMediaContainerManagementEnabled = sessionStorage.getItem(
            'advancedMediaContainerManagementEnabled'
        );
        var isEnabled =
            advancedMediaContainerManagementEnabled &&
            JSON.parse(advancedMediaContainerManagementEnabled);
        if (isEnabled) {
            if (result[1]) {
                result[1].push({
                    key: 'advancedMediaContainerManagement',
                    value: true
                });
            }
        }
        return result;
    };

    httpBackendService
        .whenGET(
            /cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/mediacontainers/
        )
        .respond(function() {
            return [
                200,
                {
                    mediaContainers: [
                        {
                            catalogVersion: 'apparel-deContentCatalog/Staged',
                            mediaContainerUuid: 'mediaContainerUuid',
                            medias: {
                                tablet: 'clone4',
                                desktop: 'clone4',
                                mobile: 'clone4',
                                widescreen: 'clone4'
                            },
                            qualifier: 'apparel-de-errorpage-pagenotfound',
                            thumbnailUrl: '/web/webroot/images/product_thumbnail_default.png'
                        },
                        {
                            catalogVersion: 'apparel-deContentCatalog/Staged',
                            mediaContainerUuid: 'mediaContainerUuid2',
                            medias: {
                                tablet: 'dnd5',
                                desktop: 'dnd5',
                                mobile: 'dnd5',
                                widescreen: 'dnd5'
                            },
                            qualifier: 'apparel-de-homepage-brands-de',
                            thumbnailUrl: '/web/webroot/images/product_thumbnail_default.png'
                        }
                    ],
                    pagination: {
                        count: 2,
                        page: 0,
                        totalCount: 44,
                        totalPages: 5
                    }
                }
            ];
        });
});
