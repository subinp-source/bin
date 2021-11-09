/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('customMocksModule', ['backendMocksUtilsModule']).run(function(backendMocksUtils) {
    backendMocksUtils.getBackendMock('componentTypesPermissionsGET').respond({
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
                        value: 'false'
                    },
                    {
                        key: 'create',
                        value: 'false'
                    },
                    {
                        key: 'remove',
                        value: 'false'
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
            }
        ]
    });
});
try {
    angular.module('smarteditloader').requires.push('customMocksModule');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('customMocksModule');
} catch (e) {}
