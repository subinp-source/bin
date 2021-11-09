/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('backendMocks', [
        'functionsModule',
        'resourceLocationsModule',
        'smarteditServicesModule'
    ])
    .constant(
        'URL_FOR_ITEM',
        /cmswebservices\/v1\/catalogs\/electronics\/versions\/staged\/items\/thesmarteditComponentId/
    )
    .run(function(httpBackendService, parseQuery) {
        httpBackendService.whenGET(/\/loadItems/).respond(function(method, url, data) {
            var query = parseQuery(data);
            var currentPage = query.currentPage;
            var mask = query.mask;

            var items = [];
            for (var i = 1; i < 26; i++) {
                items.push({
                    name: 'item' + i,
                    id: i
                });
            }

            var filtered = items.filter(function(item) {
                return mask ? item.name.toUpperCase().indexOf(mask.toUpperCase()) > -1 : true;
            });

            var results = filtered.slice(currentPage * 10, currentPage * 10 + 10);

            var pagedResults = {
                pagination: {
                    totalCount: filtered.length
                },
                results: results
            };

            return [200, pagedResults];
        });

        httpBackendService.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
            languages: [
                {
                    nativeName: 'English',
                    isocode: 'en',
                    required: true
                },
                {
                    nativeName: 'Polish',
                    isocode: 'pl'
                },
                {
                    nativeName: 'Italian',
                    isocode: 'it'
                }
            ]
        });

        httpBackendService.whenGET(/i18n/).passThrough();
        httpBackendService.whenGET(/view$/).passThrough(); //calls to storefront render API
        httpBackendService.whenPUT(/contentslots/).passThrough();
        httpBackendService.whenGET(/\.html/).passThrough();
    });
angular.module('yInfiniteScrollingApp').requires.push('backendMocks');
