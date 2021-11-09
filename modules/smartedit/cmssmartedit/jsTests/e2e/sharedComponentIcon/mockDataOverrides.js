/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('mockDataOverridesModule', ['backendMocksUtilsModule'])
    .run(function(backendMocksUtils) {
        var SHARED_COMPONENTS = ['component3'];

        var adjustSlotsIfComponentIsShared = function(component) {
            if (SHARED_COMPONENTS.indexOf(component.uuid) > -1) {
                component.slots = ['abc', 'cde'];
            }
        };

        var items = JSON.parse(sessionStorage.getItem('componentMocks'));
        backendMocksUtils.getBackendMock('componentGETMock').respond(function(method, url) {
            var uuid = /cmsitems\/(.*)/.exec(url)[1];

            var item = items.componentItems.find(function(item) {
                return item.uuid === uuid;
            });

            adjustSlotsIfComponentIsShared(item);

            return [200, item];
        });

        backendMocksUtils
            .getBackendMock('getComponentsByUuidsPOSTMock')
            .respond(function(method, url, data) {
                var dataObject = angular.fromJson(data);

                var response = items.componentItems.filter(function(item) {
                    adjustSlotsIfComponentIsShared(item);

                    return dataObject.uuids.indexOf(item.uuid) > -1;
                });
                return [
                    200,
                    {
                        response: response
                    }
                ];
            });
    });

try {
    angular.module('smarteditloader').requires.push('mockDataOverridesModule');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('mockDataOverridesModule');
} catch (e) {}
