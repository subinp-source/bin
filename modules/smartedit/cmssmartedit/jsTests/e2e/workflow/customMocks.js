/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('customMocksModule', ['backendMocksUtilsModule'])
    .run(function(backendMocksUtils, httpBackendService) {
        // Items
        var updatePageApprovalStatus = sessionStorage.getItem('updatePageApprovalStatus');
        if (updatePageApprovalStatus) {
            var pagesToUpdate = JSON.parse(updatePageApprovalStatus);
            var componentMocks = JSON.parse(sessionStorage.getItem('componentMocks'));

            var modifiedMocks = {
                componentItems: componentMocks.componentItems.map(function(item) {
                    if (pagesToUpdate[item.uuid]) {
                        var updateInfo = pagesToUpdate[item.uuid];
                        item.approvalStatus = updateInfo.approvalStatus;
                        item.displayStatus = updateInfo.displayStatus;
                    }

                    return item;
                })
            };

            sessionStorage.setItem('componentMocks', JSON.stringify(modifiedMocks));
        }

        // Permissions
        backendMocksUtils.getBackendMock('componentTypesPermissionsGET').respond(function() {
            var typePermissions = JSON.parse(sessionStorage.getItem('workflowTypePermissions'));
            var defaultWorkflowPermissions = [
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
            ];

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
                            id: 'Workflow',
                            permissions: typePermissions || defaultWorkflowPermissions
                        }
                    ]
                }
            ];
        });

        httpBackendService
            .whenGET(
                /permissionswebservices\/v1\/permissions\/principals\/(.*)\/attributes\?attributes=(.*)&permissionNames=change,read/
            )
            .respond(function() {
                var customAttributePermissions = Object.keys(sessionStorage)
                    .filter(function(key) {
                        return key.startsWith('attributePermissions_');
                    })
                    .map(function(key) {
                        return JSON.parse(sessionStorage[key]);
                    });

                return [
                    200,
                    {
                        permissionsList: customAttributePermissions
                    }
                ];
            });

        // Custom Workflow
        var removeActiveWorkflow = sessionStorage.getItem('removeActiveWorkflow');
        if (removeActiveWorkflow && JSON.parse(removeActiveWorkflow)) {
            backendMocksUtils.getBackendMock('workflowSearchGETMock').respond(function() {
                return [
                    200,
                    {
                        pagination: {
                            count: 0,
                            page: 0,
                            totalCount: 0,
                            totalPages: 0
                        },
                        workflows: []
                    }
                ];
            });
        }

        // Custom Page status
        if (sessionStorage.getItem('customDisplayStatus')) {
            backendMocksUtils.getBackendMock('componentGETMock').respond(function(method, url) {
                var uuid = /cmsitems\/(.*)/.exec(url)[1];
                var displayStatus = sessionStorage.getItem('customDisplayStatus');

                var item = JSON.parse(sessionStorage.getItem('componentMocks')).componentItems.find(
                    function(item) {
                        return item.uuid === uuid;
                    }
                );
                if (uuid === 'homepage') {
                    item.displayStatus = displayStatus;
                }

                return [200, item];
            });
        }

        function setupWorkflowTasks() {
            var workflowTasks = [
                {
                    action: {
                        code: 'code1',
                        description: 'desc1',
                        name: 'Publish Page',
                        startedAgoInMillis: 7573,
                        status: 'IN_PROGRESS'
                    },
                    attachments: [
                        {
                            catalogId: 'apparelContentCatalog',
                            catalogName: 'Apparel Content Catalog',
                            catalogVersion: 'Staged',
                            pageUid: 'homepage',
                            pageName: 'Homepage'
                        }
                    ]
                },
                {
                    action: {
                        code: 'code2',
                        description: 'desc2',
                        name: 'Review Page',
                        startedAgoInMillis: 430580111,
                        status: 'IN_PROGRESS'
                    },
                    attachments: [
                        {
                            catalogId: 'apparel-ukContentCatalog',
                            catalogName: 'Apparel UK Content Catalog',
                            catalogVersion: 'Staged',
                            pageUid: 'homepage',
                            pageName: 'Homepage'
                        }
                    ]
                },
                {
                    action: {
                        code: 'code2',
                        description: 'desc2',
                        name: 'Review Page',
                        startedAgoInMillis: 430580111,
                        status: 'IN_PROGRESS'
                    },
                    attachments: [
                        {
                            catalogId: 'apparel-deContentCatalog',
                            catalogName: 'Apparel DE Content Catalog',
                            catalogVersion: 'Staged',
                            pageUid: 'homepage',
                            pageName: 'Homepage'
                        }
                    ]
                },
                {
                    action: {
                        code: 'code2',
                        description: 'desc2',
                        name: 'Review Page',
                        startedAgoInMillis: 430580111,
                        status: 'IN_PROGRESS'
                    },
                    attachments: [
                        {
                            catalogId: 'apparelContentCatalog',
                            catalogName: 'Apparel Content Catalog',
                            catalogVersion: 'Staged',
                            pageUid: 'homepage',
                            pageName: 'Homepage'
                        }
                    ]
                }
            ];

            httpBackendService
                .whenGET(
                    /\/cmssmarteditwebservices\/v1\/inbox\/workflowtasks\?currentPage=.*&pageSize=.*/
                )
                .respond(function() {
                    if (JSON.parse(sessionStorage.getItem('updateWorkflowInbox'))) {
                        workflowTasks.shift();
                    }

                    return [
                        200,
                        {
                            pagination: {
                                count: workflowTasks.length,
                                page: 0,
                                totalCount: workflowTasks.length,
                                totalPages: 1
                            },
                            tasks: workflowTasks
                        }
                    ];
                });
        }

        setupWorkflowTasks();
    });

try {
    angular.module('smarteditloader').requires.push('customMocksModule');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('customMocksModule');
} catch (e) {}
