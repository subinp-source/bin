/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { promiseHelper } from 'testhelpers';
import {
    IAlertService,
    IPageInfoService,
    Page,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';
import { WorkflowFacade } from 'cmssmarteditcontainer/components/workflow/facades/WorkflowFacade';
import { WorkflowService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowService';
import {
    Workflow,
    WorkflowAction,
    WorkflowActionComment,
    WorkflowActionStatus,
    WorkflowDecision,
    WorkflowOperations,
    WorkflowStatus
} from 'cmssmarteditcontainer/components/workflow/dtos';

describe('Test WorkflowFacade', () => {
    let alertService: jasmine.SpyObj<IAlertService>;
    let workflowService: jasmine.SpyObj<WorkflowService>;
    let pageInfoService: jasmine.SpyObj<IPageInfoService>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let genericEditorModalService: any;
    let confirmationModalService: any;
    let workflowFacade: WorkflowFacade;
    let unregisterErrorListener: jasmine.Spy;

    const $q = promiseHelper.$q();

    const PAGE_UUID = 'some page UUID';
    const GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT = 'some event';
    const WORKFLOW_FINISHED_EVENT = 'some other event';
    const WORKFLOW_CODE = 'workflowCode';
    const WORKFLOW_ACTION_CODE = 'workflowActionCode';
    const WORKFLOW_DECISION: WorkflowDecision = {
        code: 'some code',
        name: 'some name',
        description: 'some description'
    };
    const WORKFLOW_ACTION: WorkflowAction = {
        code: 'some code',
        name: 'some name',
        description: 'some description',
        actionType: 'some type',
        status: WorkflowActionStatus.IN_PROGRESS,
        startedAgoInMillis: 5435564,
        isCurrentUserParticipant: false,
        decisions: [WORKFLOW_DECISION]
    };
    const WORKFLOW_TEMPLATE_CODE1 = 'WorkflowTemplateCode1';
    const WORKFLOW_TEMPLATE_NAME1 = 'WorkflowTemplateName1';
    const WORKFLOW_TEMPLATE_CODE2 = 'WorkflowTemplateCode2';
    const WORKFLOW_TEMPLATE_NAME2 = 'WorkflowTemplateName2';

    const SYNCHRONIZATION_POLLING: TypedMap<any> = {
        SPEED_UP: 'syncPollingSpeedUp'
    };

    let testWorkflow: Workflow;

    beforeEach(() => {
        alertService = jasmine.createSpyObj<IAlertService>('alertService', [
            'showSuccess',
            'showDanger'
        ]);
        genericEditorModalService = jasmine.createSpyObj('genericEditorModalService', ['open']);
        confirmationModalService = jasmine.createSpyObj('confirmationModalService', ['confirm']);
        pageInfoService = jasmine.createSpyObj('pageInfoService', ['getPageUUID']);
        systemEventService = jasmine.createSpyObj('systemEventService', [
            'subscribe',
            'publishAsync',
            'publish'
        ]);
        workflowService = jasmine.createSpyObj('workflowService', [
            'getWorkflowTemplates',
            'cancelWorflow',
            'getResourceWorkflowURI',
            'getAllActionsForWorkflowCode',
            'getActiveActionsForWorkflowCode',
            'getCommentsForWorkflowAction',
            'getResourceWorkflowOperationsURI'
        ]);
        unregisterErrorListener = jasmine.createSpy('unregisterErrorListener');

        workflowFacade = new WorkflowFacade(
            alertService,
            workflowService,
            confirmationModalService,
            genericEditorModalService,
            pageInfoService,
            systemEventService,
            SYNCHRONIZATION_POLLING,
            WORKFLOW_FINISHED_EVENT,
            GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
            $q
        );

        testWorkflow = {
            workflowCode: WORKFLOW_CODE,
            templateCode: WORKFLOW_TEMPLATE_CODE1,
            isAvailableForCurrentPrincipal: true,
            status: WorkflowStatus.RUNNING
        };

        genericEditorModalService.open.and.callFake(
            (componentData: any, saveCallback: (workflow: Workflow) => any) => {
                return $q.when(saveCallback(testWorkflow));
            }
        );

        workflowService.getWorkflowTemplates.and.returnValue(
            $q.when({
                templates: [
                    {
                        code: WORKFLOW_TEMPLATE_CODE1,
                        name: WORKFLOW_TEMPLATE_NAME1
                    },
                    {
                        code: WORKFLOW_TEMPLATE_CODE2,
                        name: WORKFLOW_TEMPLATE_NAME2
                    }
                ]
            })
        );
    });

    describe('start workflow ', () => {
        const QUALIFIER_1 = 'some qualifier';
        const ERROR_MSG_1 = 'some unrelated error 1';

        const VERSION_LABEL_QUALIFIER = 'versionLabel';
        const CREATE_VERSION_QUALIFIER = 'createVersion';

        let eventHandler: (key: string, eventData: any) => void;
        let eventData: any;

        beforeEach(() => {
            pageInfoService.getPageUUID.and.returnValue($q.when(PAGE_UUID));

            eventData = {
                sourceGenericEditorId: 'some editor',
                messages: [
                    {
                        subject: QUALIFIER_1,
                        message: ERROR_MSG_1
                    }
                ]
            };
            systemEventService.subscribe.and.callFake(
                (eventName: string, handler: (key: string, eventData: any) => void) => {
                    eventHandler = handler;
                    return unregisterErrorListener;
                }
            );
        });

        it('WHEN start workflow is called THEN the generic editor is opened with the current page already as an attachment', () => {
            // WHEN
            workflowFacade.startWorkflow();

            // THEN
            expect(genericEditorModalService.open).toHaveBeenCalledWith(
                jasmine.objectContaining({
                    content: {
                        attachments: [PAGE_UUID]
                    }
                }),
                jasmine.any(Function)
            );
        });

        it('WHEN workflow is started THEN a success message is shown AND the editor is properly cleaned', () => {
            // WHEN
            workflowFacade.startWorkflow();

            // THEN
            expect(systemEventService.subscribe).toHaveBeenCalledWith(
                GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
                jasmine.any(Function)
            );
            expect(alertService.showSuccess).toHaveBeenCalled();
            expect(unregisterErrorListener).toHaveBeenCalled();
        });

        it('WHEN start workflow modal is opened AND cancelled THEN the editor is properly cleaned', () => {
            // GIVEN
            genericEditorModalService.open.and.returnValue($q.reject());

            // WHEN
            workflowFacade.startWorkflow();

            // THEN
            expect(systemEventService.subscribe).toHaveBeenCalledWith(
                GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
                jasmine.any(Function)
            );
            expect(unregisterErrorListener).toHaveBeenCalled();
        });

        it('GIVEN a workflow with a validation error not handled by the generic editor WHEN start workflow modal is saved THEN an alert is shown', () => {
            // GIVEN
            workflowFacade.startWorkflow();

            // WHEN
            eventHandler(GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT, eventData);

            // THEN
            expect(alertService.showDanger).toHaveBeenCalledWith(ERROR_MSG_1 + ' ');
            expect(systemEventService.publishAsync).not.toHaveBeenCalled();
        });

        it('GIVEN a workflow with a version label validation error WHEN start workflow modal is saved THEN it republishes the error to createVersion field', () => {
            // GIVEN
            eventData.messages = [
                {
                    subject: VERSION_LABEL_QUALIFIER,
                    message: ERROR_MSG_1
                }
            ];
            const expectedEventData = {
                messages: [
                    {
                        subject: CREATE_VERSION_QUALIFIER,
                        message: ERROR_MSG_1
                    }
                ]
            };

            workflowFacade.startWorkflow();

            // WHEN
            eventHandler(GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT, eventData);

            // THEN
            expect(alertService.showDanger).not.toHaveBeenCalled();
            expect(systemEventService.publishAsync).toHaveBeenCalledWith(
                GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
                jasmine.objectContaining(expectedEventData)
            );
        });
    });

    it('WHEN cancel workflow is called THEN confirmation message is displayed AND success alert is shown', () => {
        // GIVEN
        const workflow: Workflow = {} as Workflow;
        confirmationModalService.confirm.and.returnValue($q.when({}));
        workflowService.cancelWorflow.and.returnValue($q.when({}));

        // WHEN
        workflowFacade.cancelWorflow(workflow);

        // THEN
        expect(confirmationModalService.confirm).toHaveBeenCalled();
        expect(alertService.showSuccess).toHaveBeenCalled();
    });

    it('WHEN getAllActionsForWorkflow is called THEN it calls the getAllActionsForWorkflowCode of workflowService', () => {
        workflowService.getAllActionsForWorkflowCode.and.returnValue($q.when({}));

        workflowFacade.getAllActionsForWorkflow(WORKFLOW_CODE);

        expect(workflowService.getAllActionsForWorkflowCode).toHaveBeenCalledWith(WORKFLOW_CODE);
    });

    it('WHEN getActiveActionsForWorkflow is called THEN it calls the getActiveActionsForWorkflowCode of workflowService', () => {
        workflowService.getActiveActionsForWorkflowCode.and.returnValue($q.when({}));

        workflowFacade.getActiveActionsForWorkflow(WORKFLOW_CODE);

        expect(workflowService.getActiveActionsForWorkflowCode).toHaveBeenCalledWith(WORKFLOW_CODE);
    });

    it('WHEN getCommentsForWorkflowAction is called THEN it calls the getCommentsForworkflowAction of workflowService and copies the comments object to results', () => {
        workflowService.getCommentsForWorkflowAction.and.returnValue(
            $q.when({
                comments: ['comment1', 'comment2'],
                pagination: {
                    someKey: 'someValue'
                }
            })
        );

        const result = workflowFacade.getCommentsForWorkflowAction(
            WORKFLOW_CODE,
            WORKFLOW_ACTION_CODE,
            { currentPage: 0 }
        );

        expect(workflowService.getCommentsForWorkflowAction).toHaveBeenCalledWith(
            WORKFLOW_CODE,
            WORKFLOW_ACTION_CODE,
            { currentPage: 0 }
        );
        result.then((workflowActionCommentsPage: Page<WorkflowActionComment>) => {
            expect(workflowActionCommentsPage.results.length).toBe(2);
        });
    });

    describe('make decision', () => {
        const expectedConfig = {
            title: WORKFLOW_DECISION.name,
            content: {
                operation: WorkflowOperations.MAKE_DECISION,
                workflowCode: WORKFLOW_CODE,
                actionCode: WORKFLOW_ACTION.code,
                decisionCode: WORKFLOW_DECISION.code
            }
        };

        beforeEach(() => {
            workflowService.getCommentsForWorkflowAction.and.returnValue(
                $q.when({
                    comments: ['comment1', 'comment2'],
                    pagination: {
                        someKey: 'someValue'
                    }
                })
            );

            pageInfoService.getPageUUID.and.returnValue($q.when(PAGE_UUID));
            systemEventService.publish.and.returnValue($q.when());
        });

        it('GIVEN normal action WHEN makeDecision is called THEN it opens the generic editor', () => {
            // WHEN
            const result = workflowFacade.makeDecision(
                WORKFLOW_CODE,
                WORKFLOW_ACTION,
                WORKFLOW_DECISION
            );

            // THEN
            expect(genericEditorModalService.open).toHaveBeenCalledWith(
                jasmine.objectContaining(expectedConfig),
                jasmine.any(Function),
                null
            );

            expect(alertService.showSuccess).toHaveBeenCalledWith(
                jasmine.objectContaining({
                    message: 'se.cms.workflow.make.decision.success',
                    messagePlaceholders: {
                        workflowDecisionName: WORKFLOW_DECISION.name,
                        workflowActionName: WORKFLOW_ACTION.name
                    }
                })
            );

            result.then((workflowReturned: Workflow) => {
                expect(workflowReturned).toBe(testWorkflow);
            });
        });

        it('GIVEN end action WHEN makeDecision is called THEN workflow completed message is displayed', () => {
            // GIVEN
            testWorkflow.status = WorkflowStatus.FINISHED;

            // WHEN
            const result = workflowFacade.makeDecision(
                WORKFLOW_CODE,
                WORKFLOW_ACTION,
                WORKFLOW_DECISION
            );

            // THEN
            expect(genericEditorModalService.open).toHaveBeenCalledWith(
                jasmine.objectContaining(expectedConfig),
                jasmine.any(Function),
                null
            );
            expect(alertService.showSuccess).toHaveBeenCalledWith(
                'se.cms.workflow.completed.alert.success'
            );

            result.then((workflowReturned: Workflow) => {
                expect(workflowReturned).toBe(testWorkflow);
            });
        });

        it('GIVEN normal action WHEN makeDecision is called AND user cancels the editor THEN no action takes place', () => {
            // GIVEN
            genericEditorModalService.open.and.returnValue($q.reject());

            // WHEN
            workflowFacade.makeDecision(WORKFLOW_CODE, WORKFLOW_ACTION, WORKFLOW_DECISION);

            // THEN
            expect(genericEditorModalService.open).toHaveBeenCalledWith(
                jasmine.objectContaining(expectedConfig),
                jasmine.any(Function),
                null
            );
            expect(alertService.showSuccess).not.toHaveBeenCalled();
        });

        it('GIVEN any action WHEN makeDecision is called THEN the synchronization polling speed event is triggered to force a sync job to start', () => {
            // WHEN
            workflowFacade.makeDecision(WORKFLOW_CODE, WORKFLOW_ACTION, WORKFLOW_DECISION);

            // THEN
            expect(systemEventService.publish).toHaveBeenCalledWith(
                SYNCHRONIZATION_POLLING.SPEED_UP,
                PAGE_UUID
            );
        });

        it('GIVEN any action WHEN makeDecision is called AND user cancels the editor THEN no synchronization polling speed even is triggered', () => {
            // GIVEN
            genericEditorModalService.open.and.returnValue($q.reject());

            // WHEN
            workflowFacade.makeDecision(WORKFLOW_CODE, WORKFLOW_ACTION, WORKFLOW_DECISION);

            // THEN
            expect(systemEventService.publish).not.toHaveBeenCalled();
        });
    });
});
