/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('PermissionMockModule', ['resourceLocationsModule', 'functionsModule'])
    .run(function(
        httpBackendService,
        resourceLocationToRegex,
        USER_GLOBAL_PERMISSIONS_RESOURCE_URI
    ) {
        httpBackendService
            .whenGET(
                /permissionswebservices\/v1\/permissions\/principals\/.+\/catalogs\?catalogId=toysContentCatalog&catalogVersion=Online/
            )
            .respond({
                permissionsList: [
                    {
                        catalogId: 'toysContentCatalog',
                        catalogVersion: 'Online',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'write',
                                value: 'false'
                            }
                        ],
                        syncPermissions: [{}]
                    }
                ]
            });

        httpBackendService
            .whenGET(
                /permissionswebservices\/v1\/permissions\/principals\/.+\/catalogs\?catalogId=actionFiguresContentCatalog&catalogVersion=Online/
            )
            .respond({
                permissionsList: [
                    {
                        catalogId: 'actionFiguresContentCatalog',
                        catalogVersion: 'Online',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'write',
                                value: 'false'
                            }
                        ],
                        syncPermissions: [{}]
                    }
                ]
            });

        httpBackendService
            .whenGET(
                /permissionswebservices\/v1\/permissions\/principals\/.+\/catalogs\?catalogId=electronicsContentCatalog&catalogVersion=Online/
            )
            .respond({
                permissionsList: [
                    {
                        catalogId: 'electronicsContentCatalog',
                        catalogVersion: 'Online',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'write',
                                value: 'true'
                            }
                        ],
                        syncPermissions: [{}]
                    }
                ]
            });

        httpBackendService
            .whenGET(
                /permissionswebservices\/v1\/permissions\/principals\/.+\/catalogs\?catalogId=electronicsContentCatalog&catalogVersion=Staged/
            )
            .respond({
                permissionsList: [
                    {
                        catalogId: 'electronicsContentCatalog',
                        catalogVersion: 'Staged',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'write',
                                value: 'true'
                            }
                        ],
                        syncPermissions: [
                            {
                                canSynchronize: true,
                                targetCatalogVersion: 'Online'
                            }
                        ]
                    }
                ]
            });

        httpBackendService
            .whenGET(
                /permissionswebservices\/v1\/permissions\/principals\/.+\/catalogs\?catalogId=apparel-ukContentCatalog&catalogVersion=Online/
            )
            .respond({
                permissionsList: [
                    {
                        catalogId: 'apparel-ukContentCatalog',
                        catalogVersion: 'Online',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'write',
                                value: 'true'
                            }
                        ],
                        syncPermissions: [{}]
                    }
                ]
            });

        httpBackendService
            .whenGET(
                /permissionswebservices\/v1\/permissions\/principals\/.+\/catalogs\?catalogId=apparel-ukContentCatalog&catalogVersion=Staged/
            )
            .respond({
                permissionsList: [
                    {
                        catalogId: 'apparel-ukContentCatalog',
                        catalogVersion: 'Staged',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'write',
                                value: 'true'
                            }
                        ],
                        syncPermissions: [
                            {
                                canSynchronize: true,
                                targetCatalogVersion: 'Online'
                            }
                        ]
                    }
                ]
            });

        httpBackendService
            .whenGET(resourceLocationToRegex(USER_GLOBAL_PERMISSIONS_RESOURCE_URI))
            .respond(function(method, url) {
                var user = getUserFromUrl(url);
                if (user === 'admin') {
                    return [
                        200,
                        {
                            id: 'global',
                            permissions: [
                                {
                                    key: 'smartedit.configurationcenter.read',
                                    value: 'true'
                                }
                            ]
                        }
                    ];
                } else {
                    return [
                        200,
                        {
                            id: 'global',
                            permissions: [
                                {
                                    key: 'smartedit.configurationcenter.read',
                                    value: 'false'
                                }
                            ]
                        }
                    ];
                }
            });

        function getUserFromUrl(url) {
            return /principals\/(.+)\/.*/.exec(url)[1];
        }
    });

try {
    angular.module('smarteditloader').requires.push('PermissionMockModule');
    angular.module('smarteditcontainer').requires.push('PermissionMockModule');
} catch (ex) {}
