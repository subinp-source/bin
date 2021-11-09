/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('customMocksModule', ['backendMocksUtilsModule'])
    .run(function(httpBackendService, backendMocksUtils) {
        var workflowOperationsPOSTMock = httpBackendService
            .whenPOST(
                /\/cmssmarteditwebservices\/v1\/sites\/.*\/catalogs\/.*\/pages\/.*\/operations/
            )
            .respond(function(method, url, data) {
                var items = JSON.parse(sessionStorage.getItem('componentMocks'));
                var catalogId = /catalogs\/(.*)\/pages\/(.*)\/operations/.exec(url)[1];
                var pageId = /catalogs\/(.*)\/pages\/(.*)\/operations/.exec(url)[2];

                var dataObject = angular.fromJson(data);

                if (dataObject.operation === 'DELETE_PAGE' && dataObject.targetCatalogVersion) {
                    var catalogVersion = catalogId + '/' + dataObject.targetCatalogVersion;
                    items.componentItems.forEach(
                        function(item) {
                            if (item.uid === pageId && item.catalogVersion === catalogVersion) {
                                item.pageStatus = 'DELETED';
                            }
                        }.bind(this)
                    );

                    sessionStorage.setItem('componentMocks', JSON.stringify(items));
                }

                return [
                    200,
                    {
                        operation: 'DELETE_PAGE',
                        sourceCatalogVersion: dataObject.sourceCatalogVersion,
                        targetCatalogVersion: dataObject.targetCatalogVersion
                    }
                ];
            });
        backendMocksUtils.storeBackendMock(
            'workflowOperationsPOSTMock',
            workflowOperationsPOSTMock
        );
    });
try {
    angular.module('smarteditloader').requires.push('customMocksModule');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('customMocksModule');
} catch (e) {}
