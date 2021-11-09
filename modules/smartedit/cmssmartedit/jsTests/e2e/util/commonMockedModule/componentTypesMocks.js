/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false */
angular
    .module('componentTypesMocks', [])
    .run(function(httpBackendService, parseQuery, backendMocksUtils) {
        var componentTypesPermissionsGET = httpBackendService
            .whenGET(
                /permissionswebservices\/v1\/permissions\/principals\/(.*)\/types\?permissionNames=create,change,read,remove&types=(.*)/
            )
            .respond({
                permissionsList: [
                    {
                        id: 'ProductCarouselComponent',
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
                        id: 'CMSParagraphComponent',
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
                        id: 'FooterNavigationComponent',
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
                        id: 'CMSLinkComponent',
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
                        id: 'SimpleBannerComponent',
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
                        id: 'SimpleResponsiveBannerComponent',
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
                        id: 'componentType1',
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
                        id: 'componentType2',
                        permissions: [
                            {
                                key: 'read',
                                value: 'true'
                            },
                            {
                                key: 'change',
                                value: 'false'
                            },
                            {
                                key: 'create',
                                value: 'true'
                            },
                            {
                                key: 'remove',
                                value: 'false'
                            }
                        ]
                    },
                    {
                        id: 'componentType4',
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
                        id: 'componentType10',
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
                        id: 'ContentSlot',
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
                        id: 'ContentSlotForPage',
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
                        id: 'AbstractCMSComponent',
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
                    }
                ]
            });

        backendMocksUtils.storeBackendMock(
            'componentTypesPermissionsGET',
            componentTypesPermissionsGET
        );
    });
try {
    angular.module('smarteditloader').requires.push('componentTypesMocks');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('componentTypesMocks');
} catch (e) {}
