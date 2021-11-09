/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
angular
    .module('customMocksModule', ['backendMocksUtilsModule'])
    .run(function(httpBackendService, parseQuery, backendMocksUtils) {
        // Component Types Tab
        var componentTypes = [
            {
                category: 'COMPONENT',
                code: 'CMSParagraphComponent',
                i18nKey: 'type.cmsparagraphcomponent.name',
                name: 'Paragraph'
            },
            {
                category: 'COMPONENT',
                code: 'SimpleBannerComponent',
                i18nKey: 'type.simplebannercomponent.name',
                name: 'Simple Banner Component'
            }
        ];

        httpBackendService
            .whenGET(
                /cmssmarteditwebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/pages\/homepage\/types((?!\/).)*$/
            )
            .respond(function(method, url, data) {
                var query = parseQuery(data);
                var componentsMask = query.mask.toLowerCase();

                var paginatedData = componentsMask
                    ? componentTypes.filter(function(item) {
                          return item.name.toLowerCase().indexOf(componentsMask) > -1;
                      })
                    : componentTypes;

                return [
                    200,
                    {
                        pagination: {
                            totalCount: paginatedData.length
                        },
                        results: paginatedData
                    }
                ];
            });

        // Components Tab
        var componentsListGETMock = backendMocksUtils.getBackendMock('componentsListGETMock');
        componentsListGETMock.respond(function(method, url, data, headers) {
            var params = parseQuery(data);
            var currentPage = params.currentPage;
            var mask = params.mask;
            var pageSize = params.pageSize;
            var typeCode = params.typeCode;
            var uuids = params.uuids && params.uuids.split(',');
            var additionalParams = params.params && params.params.split(',');

            var filteredItems = JSON.parse(sessionStorage.getItem('componentMocks')).componentItems;

            if (uuids) {
                filteredItems = items.componentItems.filter(function(item) {
                    return uuids.indexOf(item.uuid) > -1;
                });
                return [
                    200,
                    {
                        response: filteredItems
                    }
                ];
            }

            if (typeCode) {
                filteredItems = filteredItems.filter(function(item) {
                    return item.typeCode === typeCode;
                });
            }

            if (
                params.catalogId === 'apparel-ukContentCatalog' &&
                params.catalogVersion === 'Staged'
            ) {
                filteredItems.splice(20);
            }

            filteredItems = filteredItems.filter(function(item) {
                return mask
                    ? (item.name &&
                          typeof item.name === 'string' &&
                          item.name.toUpperCase().indexOf(mask.toUpperCase()) > -1) ||
                          item.uid.toUpperCase().indexOf(mask.toUpperCase()) > -1
                    : true;
            });

            var results = filteredItems.slice(currentPage * 10, currentPage * 10 + 10);

            var pagedResults = {
                pagination: {
                    count: 10,
                    page: currentPage,
                    totalCount: filteredItems.length,
                    totalPages: 2
                },
                response: results
            };

            return [200, pagedResults];
        });
    });
try {
    angular.module('smarteditloader').requires.push('customMocksModule');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('customMocksModule');
} catch (e) {}
