/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('customMocksModule', ['backendMocksUtilsModule']).run(function(backendMocksUtils) {
    backendMocksUtils.getBackendMock('componentTypesPermissionsGET').respond(function() {
        var typePermissions = JSON.parse(sessionStorage.getItem('cmsVersionTypePermissions'));

        return [
            200,
            {
                permissionsList: [
                    {
                        id: 'ContentPage',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'change',
                                value: 'true'
                            },
                            {
                                key: 'create',
                                value: 'true'
                            },
                            {
                                key: 'remove',
                                value: 'true'
                            }
                        ]
                    },
                    {
                        id: 'CategoryPage',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'change',
                                value: 'true'
                            },
                            {
                                key: 'create',
                                value: 'true'
                            },
                            {
                                key: 'remove',
                                value: 'true'
                            }
                        ]
                    },
                    {
                        id: 'ProductPage',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'change',
                                value: 'true'
                            },
                            {
                                key: 'create',
                                value: 'true'
                            },
                            {
                                key: 'remove',
                                value: 'true'
                            }
                        ]
                    },
                    {
                        id: 'CMSVersion',
                        permissions: typePermissions
                    }
                ]
            }
        ];
    });
});
try {
    angular.module('smarteditloader').requires.push('customMocksModule');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('customMocksModule');
} catch (e) {}
