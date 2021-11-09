/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('workflowMocks', [])
    .run(function(httpBackendService, parseQuery, backendMocksUtils) {
        // Constants
        var WorkflowActionType = {
            START: 'START',
            NORMAL: 'NORMAL',
            END: 'END'
        };

        var WorkflowStatus = {
            FINISHED: 'finished',
            RUNNING: 'running'
        };

        var WorkflowActionStatus = {
            PENDING: 'PENDING',
            IN_PROGRESS: 'IN_PROGRESS',
            COMPLETED: 'COMPLETED'
        };

        // WORKFLOW TEMPLATES
        var workflowTemplates = [
            {
                code: 'PageApproval',
                name: 'Page Approval'
            },
            {
                code: 'PageTranslation',
                name: 'Page Translation and Approval'
            }
        ];

        var workflowTemplatesGETMock = httpBackendService
            .whenGET(/\/cmswebservices\/v1\/catalogs\/.*\/versions\/.*\/workflowtemplates/)
            .respond(function() {
                return [
                    200,
                    {
                        templates: workflowTemplates
                    }
                ];
            });
        backendMocksUtils.storeBackendMock('workflowTemplatesGETMock', workflowTemplatesGETMock);

        // WORKFLOW FOR ATTACHMENTS
        var workflowSearchGETMock = httpBackendService
            .whenGET(
                /\/cmswebservices\/v1\/catalogs\/.*\/versions\/.*\/workflows\?attachment=.*?&currentPage=0&pageSize=1&statuses=running,paused/
            )
            .respond(function() {
                var numberOfWorkflowsToReturn = JSON.parse(
                    sessionStorage.getItem('numberOfWorkflowsToReturn')
                );
                var workflowsAvailableForCurrentPrincipal = sessionStorage.getItem(
                    'workflowsAvailableForCurrentPrincipal'
                )
                    ? JSON.parse(sessionStorage.getItem('workflowsAvailableForCurrentPrincipal'))
                    : true;

                numberOfWorkflowsToReturn =
                    numberOfWorkflowsToReturn === null ? 1 : numberOfWorkflowsToReturn;

                var workflows = [];
                for (var i = 0; i < numberOfWorkflowsToReturn; i++) {
                    workflows.push({
                        createVersion: false,
                        description: '',
                        isAvailableForCurrentPrincipal: workflowsAvailableForCurrentPrincipal,
                        status: 'RUNNING',
                        workflowCode: '000001J' + i
                    });
                }
                return [
                    200,
                    {
                        pagination: {
                            count: 1,
                            page: 0,
                            totalCount: 0,
                            totalPages: 0
                        },
                        workflows: workflows
                    }
                ];
            });
        backendMocksUtils.storeBackendMock('workflowSearchGETMock', workflowSearchGETMock);

        // WORKFLOW ACTION COMMENTS
        var workflowCommentsGETMock = httpBackendService
            .whenGET(
                /\/cmswebservices\/v1\/catalogs\/.*\/versions\/.*\/workflows\/.*\/actions\/.*\/comments/
            )
            .respond(function(method, url) {
                var queryString = parseQuery(url);
                var workflowInfo = parseWorkflowActionsInfoFromUrl(url);

                var comments = [];

                if (workflowInfo.workflowActionCode === 'Action1') {
                    comments.push(
                        createRegularComment('Comment1', 'This is the 1st workflow comment'),
                        createDecisionComment('Action1', 'DecisionComment1')
                    );
                }
                if (workflowInfo.workflowActionCode === 'Action2') {
                    comments.push(
                        createRegularComment('Comment2', 'please fix the component names'),
                        createDecisionComment('Action1', 'DecisionComment1'),
                        createRegularComment('Comment3', 'please fix the changes'),
                        createRegularComment('Comment4', 'please review translations'),
                        createRegularComment('Comment5', 'please review translations'),
                        createRegularComment('Comment6', 'please review translations'),
                        createDecisionComment('Action2', 'DecisionComment2', 'some description'),
                        createRegularComment('Comment7', 'please review translations'),
                        createRegularComment('Comment8', 'please review translations')
                    );
                }
                if (workflowInfo.workflowActionCode === 'Action3') {
                    comments.push(
                        createRegularComment('Comment9', 'Good work'),
                        createRegularComment('Comment10', 'missing translations')
                    );
                }

                return [
                    200,
                    {
                        pagination: {
                            count: comments.length,
                            page: queryString.currentPage,
                            totalCount: comments.length,
                            totalPages: 1
                        },
                        comments: comments
                    }
                ];
            });
        backendMocksUtils.storeBackendMock('workflowCommentsGETMock', workflowCommentsGETMock);

        // WORKFLOW ACTIONS
        var workflowActionsGETMock = httpBackendService
            .whenGET(/\/cmswebservices\/v1\/catalogs\/.*\/versions\/.*\/workflows\/.*\/actions/)
            .respond(function(method, url) {
                var workflowCode = url.match(/\/workflows\/([\w-]+)\/actions/)[1];

                return [
                    200,
                    {
                        actions: workflowActions,
                        workflowCode: workflowCode
                    }
                ];
            });
        backendMocksUtils.storeBackendMock('workflowActionsGETMock', workflowActionsGETMock);

        // WORKFLOWS
        var workflowGETMock = httpBackendService
            .whenGET(/\/cmswebservices\/v1\/catalogs\/.*\/versions\/.*\/workflows\/.*/)
            .respond(function(method, url) {
                var workflowCode = url.match(/\/workflows\/([\w-]+)/)[1];
                return [
                    200,
                    {
                        createVersion: false,
                        description: '',
                        isAvailableForCurrentPrincipal: true,
                        status: 'RUNNING',
                        workflowCode: workflowCode
                    }
                ];
            });
        backendMocksUtils.storeBackendMock('workflowGETMock', workflowGETMock);

        // WORKFLOW OPERATIONS
        var workflowOperationsPOSTMock = httpBackendService
            .whenPOST(/\/cmswebservices\/v1\/catalogs\/.*\/versions\/.*\/workflows\/.*\/operations/)
            .respond(function(method, url, data) {
                var dataObject = angular.fromJson(data);

                var actionFound = workflowActions.find(function(action) {
                    return action.code === dataObject.actionCode;
                });

                if (!actionFound) {
                    return [
                        404,
                        {
                            errors: [
                                {
                                    message: 'No workflow action item found for code',
                                    type: 'UnknownIdentifierError'
                                }
                            ]
                        }
                    ];
                }

                // Changing the actions status in response to the decision made.
                var index = workflowActions.findIndex(function(action) {
                    return action.code === dataObject.actionCode;
                });
                workflowActions.splice(
                    index,
                    1,
                    createAction(
                        'Action2',
                        WorkflowActionType.NORMAL,
                        WorkflowActionStatus.COMPLETED,
                        true
                    )
                );

                var index2 = workflowActions.findIndex(function(action) {
                    return action.code === 'Action4';
                });
                workflowActions.splice(
                    index2,
                    1,
                    createAction(
                        'Action4',
                        WorkflowActionType.END,
                        WorkflowActionStatus.IN_PROGRESS,
                        true
                    )
                );

                var updatedWorkflowStatus = WorkflowStatus.RUNNING;
                if (actionFound.actionType === WorkflowActionType.END) {
                    updatedWorkflowStatus = WorkflowStatus.FINISHED;
                    sessionStorage.setItem('numberOfWorkflowsToReturn', 0);
                }

                return [
                    200,
                    {
                        type: 'cmsWorkflowWsDTO',
                        isAvailableForCurrentPrincipal: true,
                        status: updatedWorkflowStatus,
                        workflowCode: '000001J'
                    }
                ];
            });
        backendMocksUtils.storeBackendMock(
            'workflowOperationsPOSTMock',
            workflowOperationsPOSTMock
        );

        var workflowEditableItemssGETMock = httpBackendService
            .whenGET(
                /\/cmssmarteditwebservices\/v1\/catalogs\/.*\/versions\/.*?\/workfloweditableitems\?itemUids=.*/
            )
            .respond(function(method, url) {
                var urlItemUids = /itemUids=(.*)/.exec(url)[1];
                var itemUids = urlItemUids.split(',');

                var workflowItemsEditable = sessionStorage.getItem('workflowItemsEditable')
                    ? JSON.parse(sessionStorage.getItem('workflowItemsEditable'))
                    : itemUids;

                var data = itemUids.map(function(uid) {
                    return {
                        uid: uid,
                        uuid: uid,
                        editableByUser:
                            workflowItemsEditable.findIndex(function(editableUid) {
                                return editableUid === uid;
                            }) > -1
                    };
                });
                return [
                    200,
                    {
                        editableItems: data
                    }
                ];
            });
        backendMocksUtils.storeBackendMock(
            'workflowEditableItemssGETMock',
            workflowEditableItemssGETMock
        );

        // HELPER METHODS //
        var createAction = function(code, type, status, isCurrentUserParticipant) {
            return {
                actionType: type,
                code: code,
                decisions: [
                    {
                        code: code + 'Approve',
                        description: 'Approve For ' + code,
                        name: 'Approve'
                    },
                    {
                        code: code + 'Reject',
                        description: 'Reject For ' + code,
                        name: 'Reject'
                    }
                ],
                description: 'This is ' + code,
                isCurrentUserParticipant: isCurrentUserParticipant,
                startedAgoInMillis: 86841180,
                name: code,
                status: status
            };
        };

        var createRegularComment = function(code, description) {
            return {
                authorName: 'CMS Translator',
                code: code,
                createdAgoInMillis: 521011030,
                text: description
            };
        };

        var createDecisionComment = function(originalActionCode, code, description) {
            var decsisionComment = {
                authorName: 'CMS Manager',
                code: code,
                createdAgoInMillis: 75752260,
                creationtime: '2019-01-23T19:37:03+0000',
                decisionCode: 'Reject',
                decisionName: 'Reject',
                originalActionCode: originalActionCode
            };

            if (description) {
                decsisionComment.text = description;
            }

            return decsisionComment;
        };

        var parseWorkflowActionsInfoFromUrl = function(url) {
            var regex = /\/workflows\/([\w-]+)\/actions\/([\w-]+)/;
            var parsedUrl = url.match(regex);
            return {
                workflowCode: parsedUrl[1],
                workflowActionCode: parsedUrl[2]
            };
        };

        var workflowActions = [
            createAction(
                'Action1',
                WorkflowActionType.NORMAL,
                WorkflowActionStatus.COMPLETED,
                true
            ),
            createAction(
                'Action2',
                WorkflowActionType.NORMAL,
                WorkflowActionStatus.IN_PROGRESS,
                true
            ),
            createAction(
                'Action3',
                WorkflowActionType.NORMAL,
                WorkflowActionStatus.IN_PROGRESS,
                false
            ),
            createAction('Action4', WorkflowActionType.END, WorkflowActionStatus.PENDING, true)
        ];
    });

try {
    angular.module('smarteditloader').requires.push('workflowMocks');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('workflowMocks');
} catch (e) {}
