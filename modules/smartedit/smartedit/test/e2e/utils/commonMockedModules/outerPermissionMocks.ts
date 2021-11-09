/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import {
    moduleUtils,
    stringUtils,
    HttpBackendService,
    USER_GLOBAL_PERMISSIONS_RESOURCE_URI
} from 'smarteditcommons';

@NgModule({
    providers: [
        moduleUtils.initialize(
            (httpBackendService: HttpBackendService) => {
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
                    .whenGET(
                        stringUtils.resourceLocationToRegex(USER_GLOBAL_PERMISSIONS_RESOURCE_URI)
                    )
                    .respond(function(method, url) {
                        const user = getUserFromUrl(url);
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

                function getUserFromUrl(url: string) {
                    return /principals\/(.+)\/.*/.exec(url)[1];
                }
            },
            [HttpBackendService]
        )
    ]
})
export class OuterPermissionMocks {}

window.pushModules(OuterPermissionMocks);
