/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import * as lodash from 'lodash';
import * as angular from 'angular';

import {
    annotationService,
    pageChangeEvictionTag,
    perspectiveChangedEvictionTag,
    rarelyChangingContent,
    Cached,
    CrossFrameEventService,
    ICatalogService,
    IExperience,
    IExperienceService,
    IPerspectiveService,
    IRestService,
    IRestServiceFactory,
    ISharedDataService,
    Page,
    Pagination,
    SystemEventService,
    WindowUtils
} from 'smarteditcommons';
import { promiseHelper } from 'testhelpers';
import {
    Workflow,
    WorkflowAction,
    WorkflowActionComment,
    WorkflowStatus,
    WorkflowTask,
    WorkflowTemplate
} from 'cmssmarteditcontainer/components/workflow/dtos';
import {
    OpenPageWorkflowMenu,
    WorkflowService
} from 'cmssmarteditcontainer/components/workflow/services/WorkflowService';
import { WorkflowTasksPollingService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowTasksPollingService';
import {
    workflowCompletedEvictionTag,
    workflowTasksMenuOpenedEvictionTag,
    CMSModesService
} from 'cmscommons';

describe('Test WorkflowService', () => {
    let restServiceFactory: jasmine.SpyObj<IRestServiceFactory>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let sharedDataService: jasmine.SpyObj<ISharedDataService>;
    let perspectiveService: jasmine.SpyObj<IPerspectiveService>;
    let catalogService: jasmine.SpyObj<ICatalogService>;
    let workflowOperationsRESTService: jasmine.SpyObj<IRestService<Workflow>>;
    let workflowRESTService: jasmine.SpyObj<IRestService<Workflow>>;
    let workflowTemplateRESTService: jasmine.SpyObj<IRestService<WorkflowTemplate>>;
    let workflowActionsRESTService: jasmine.SpyObj<IRestService<Workflow>>;
    let workflowActionCommentsRESTService: jasmine.SpyObj<IRestService<WorkflowActionComment>>;
    let experienceService: jasmine.SpyObj<IExperienceService>;
    let windowUtils: jasmine.SpyObj<WindowUtils>;
    let workflowTasksPollingService: jasmine.SpyObj<WorkflowTasksPollingService>;
    let currentExperience: IExperience;

    const $q = promiseHelper.$q();
    const PAGE_CONTEXT_CATALOG = 'catalogId';
    const PAGE_CONTEXT_CATALOG_VERSION = 'catalogVersion';
    const WORKFLOW_TEMPLATE_CODE1 = 'WorkflowTemplateCode1';
    const WORKFLOW_TEMPLATE_NAME1 = 'WorkflowTemplateName1';
    const WORKFLOW_TEMPLATE_CODE2 = 'WorkflowTemplateCode2';
    const WORKFLOW_TEMPLATE_NAME2 = 'WorkflowTemplateName2';
    const CATALOG_ID = 'contextCatalogId';
    const CATALOG_VERSION = 'contextCatalogVersion';
    const EVENT_PERSPECTIVE_REFRESHED = 'EVENT_PERSPECTIVE_REFRESHED';
    const EVENT_PERSPECTIVE_CHANGED = 'EVENT_PERSPECTIVE_CHANGED';
    const OPEN_PAGE_WORKFLOW_MENU = 'OPEN_PAGE_WORKFLOW_MENU';
    const CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU = 'CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU';
    const WORKFLOW_TASKS_COUNT_UPDATED = 'WORKFLOW_TASKS_COUNT_UPDATED';

    const uriContext = {
        CURRENT_CONTEXT_CATALOG: CATALOG_ID,
        CURRENT_CONTEXT_CATALOG_VERSION: CATALOG_VERSION
    };
    let workflowService: WorkflowService;

    beforeEach(() => {
        restServiceFactory = jasmine.createSpyObj<IRestServiceFactory>('restServiceFactory', [
            'get'
        ]);
        experienceService = jasmine.createSpyObj<IExperienceService>('experienceService', [
            'getCurrentExperience',
            'compareWithCurrentExperience',
            'loadExperience'
        ]);
        crossFrameEventService = jasmine.createSpyObj<CrossFrameEventService>(
            'crossFrameEventService',
            ['subscribe', 'publish']
        );
        sharedDataService = jasmine.createSpyObj<ISharedDataService>('sharedDataService', [
            'set',
            'get',
            'remove'
        ]);
        perspectiveService = jasmine.createSpyObj<IPerspectiveService>('perspectiveService', [
            'getActivePerspectiveKey',
            'switchTo'
        ]);
        systemEventService = jasmine.createSpyObj<SystemEventService>('systemEventService', [
            'subscribe',
            'publish'
        ]);
        catalogService = jasmine.createSpyObj<ICatalogService>('catalogService', [
            'retrieveUriContext',
            'getDefaultSiteForContentCatalog'
        ]);
        windowUtils = jasmine.createSpyObj<WindowUtils>('windowUtils', ['getGatewayTargetFrame']);
        workflowTasksPollingService = jasmine.createSpyObj<WorkflowTasksPollingService>(
            'workflowTasksPollingService',
            ['addSubscriber']
        );
        catalogService.retrieveUriContext.and.returnValue($q.when(uriContext));
    });

    function createWorkflowInstance(): WorkflowService {
        return new WorkflowService(
            $q,
            restServiceFactory,
            crossFrameEventService,
            systemEventService,
            sharedDataService,
            perspectiveService,
            catalogService,
            experienceService,
            workflowTasksPollingService,
            windowUtils,
            EVENT_PERSPECTIVE_REFRESHED,
            EVENT_PERSPECTIVE_CHANGED,
            OPEN_PAGE_WORKFLOW_MENU,
            CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU,
            PAGE_CONTEXT_CATALOG,
            PAGE_CONTEXT_CATALOG_VERSION,
            WORKFLOW_TASKS_COUNT_UPDATED
        );
    }

    describe('Open Page Workflow Menu', () => {
        beforeEach(() => {
            workflowService = createWorkflowInstance();
        });

        it('WHEN initialized THEN subscribes to EVENT_PERSPECTIVE_REFRESHED and EVENT_PERSPECTIVE_CHANGED event', () => {
            expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
                EVENT_PERSPECTIVE_REFRESHED,
                jasmine.any(Function)
            );
            expect(crossFrameEventService.subscribe).toHaveBeenCalledWith(
                EVENT_PERSPECTIVE_CHANGED,
                jasmine.any(Function)
            );
            expect(workflowTasksPollingService.addSubscriber).toHaveBeenCalledWith(
                jasmine.any(Function),
                true
            );
        });

        it('WHEN currently in basic edit perspective mode THEN publishes an event to open the menu', () => {
            // GIVEN
            const openPageWorkflowMenu = crossFrameEventService.subscribe.calls.argsFor(0)[1];
            sharedDataService.get.and.returnValue($q.resolve(OpenPageWorkflowMenu.Default));
            perspectiveService.getActivePerspectiveKey.and.returnValue(
                $q.resolve(CMSModesService.BASIC_PERSPECTIVE_KEY)
            );

            // WHEN
            openPageWorkflowMenu();

            // THEN
            expect(systemEventService.publish).toHaveBeenCalledWith(
                CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU,
                true
            );
            expect(sharedDataService.remove).toHaveBeenCalledWith(OPEN_PAGE_WORKFLOW_MENU);
            expect(sharedDataService.set).not.toHaveBeenCalled();
            expect(perspectiveService.switchTo).not.toHaveBeenCalled();
        });

        it('WHEN currently in versioning perspective mode THEN switches to advanced mode and sets OPEN_PAGE_WORKFLOW_MENU in sharedDataService to SwitchPerspective', () => {
            // GIVEN
            const openPageWorkflowMenu = crossFrameEventService.subscribe.calls.argsFor(0)[1];
            sharedDataService.get.and.returnValue($q.resolve(OpenPageWorkflowMenu.Default));
            perspectiveService.getActivePerspectiveKey.and.returnValue(
                $q.resolve(CMSModesService.VERSIONING_PERSPECTIVE_KEY)
            );
            sharedDataService.set.and.returnValue($q.resolve());
            perspectiveService.switchTo.and.returnValue($q.resolve());

            // WHEN
            openPageWorkflowMenu();

            // THEN
            expect(sharedDataService.set).toHaveBeenCalledWith(
                OPEN_PAGE_WORKFLOW_MENU,
                OpenPageWorkflowMenu.SwitchPerspective
            );
            expect(perspectiveService.switchTo).toHaveBeenCalledWith(
                CMSModesService.ADVANCED_PERSPECTIVE_KEY
            );
        });

        it('WHEN OPEN_PAGE_WORKFLOW_MENU is SwitchPerspective THEN it publishes an event to open the menu', () => {
            // GIVEN
            const openPageWorkflowMenu = crossFrameEventService.subscribe.calls.argsFor(0)[1];
            sharedDataService.get.and.returnValue(
                $q.resolve(OpenPageWorkflowMenu.SwitchPerspective)
            );

            // WHEN
            openPageWorkflowMenu();

            // THEN
            expect(systemEventService.publish).toHaveBeenCalledWith(
                CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU,
                true
            );
            expect(sharedDataService.remove).toHaveBeenCalledWith(OPEN_PAGE_WORKFLOW_MENU);
        });

        it('WHEN there is no OPEN_PAGE_WORKFLOW_MENU in sharedDataservice THEN it does not publish an event to open the menu', () => {
            // GIVEN
            const openPageWorkflowMenu = crossFrameEventService.subscribe.calls.argsFor(0)[1];
            sharedDataService.get.and.returnValue($q.resolve());

            // WHEN
            openPageWorkflowMenu();

            // THEN
            expect(systemEventService.publish).not.toHaveBeenCalled();
            expect(perspectiveService.switchTo).not.toHaveBeenCalled();
            expect(sharedDataService.set).not.toHaveBeenCalled();
        });

        it('WHEN given experience from workflow task is same as current experience THEN it does not load experience', () => {
            // GIVEN
            const task: WorkflowTask = {
                action: null,
                attachments: [
                    {
                        pageName: 'pageName',
                        pageUid: 'pageUid',
                        catalogId: 'catalogId',
                        catalogVersion: 'catalogVersion',
                        catalogName: 'catalogName'
                    }
                ]
            };
            catalogService.getDefaultSiteForContentCatalog.and.returnValue(
                $q.when({ uid: 'siteId' })
            );
            experienceService.compareWithCurrentExperience.and.returnValue($q.when(true));
            sharedDataService.set.and.returnValue($q.when());
            windowUtils.getGatewayTargetFrame.and.returnValue(true);
            spyOn(workflowService, 'openPageWorkflowMenu').and.callFake(angular.noop);

            // WHEN
            workflowService.loadExperienceAndOpenPageWorkflowMenu(task);

            // THEN
            expect(experienceService.compareWithCurrentExperience).toHaveBeenCalledWith({
                siteId: 'siteId',
                catalogId: 'catalogId',
                catalogVersion: 'catalogVersion',
                pageId: 'pageUid'
            });
            expect(sharedDataService.set).toHaveBeenCalledWith(
                OPEN_PAGE_WORKFLOW_MENU,
                OpenPageWorkflowMenu.Default
            );
            expect(workflowService.openPageWorkflowMenu).toHaveBeenCalled();
            expect(experienceService.loadExperience).not.toHaveBeenCalled();
        });

        it('WHEN given experience from workflow task is not as same as current experience THEN it loads experience', () => {
            // GIVEN
            const task: WorkflowTask = {
                action: null,
                attachments: [
                    {
                        pageName: 'pageName',
                        pageUid: 'pageUid',
                        catalogId: 'catalogId',
                        catalogVersion: 'catalogVersion',
                        catalogName: 'catalogName'
                    }
                ]
            };
            catalogService.getDefaultSiteForContentCatalog.and.returnValue(
                $q.when({ uid: 'siteId' })
            );
            experienceService.compareWithCurrentExperience.and.returnValue($q.when(false));
            experienceService.loadExperience.and.returnValue($q.when());
            windowUtils.getGatewayTargetFrame.and.returnValue(true);

            // WHEN
            workflowService.loadExperienceAndOpenPageWorkflowMenu(task);

            // THEN
            expect(experienceService.loadExperience).toHaveBeenCalledWith({
                siteId: 'siteId',
                catalogId: 'catalogId',
                catalogVersion: 'catalogVersion',
                pageId: 'pageUid'
            });
            expect(sharedDataService.set).toHaveBeenCalledWith(
                OPEN_PAGE_WORKFLOW_MENU,
                OpenPageWorkflowMenu.Default
            );
        });

        it('WHEN there is no current experience THEN it loads the experience from the given parameters', () => {
            // GIVEN
            const task: WorkflowTask = {
                action: null,
                attachments: [
                    {
                        pageName: 'pageName',
                        pageUid: 'pageUid',
                        catalogId: 'catalogId',
                        catalogVersion: 'catalogVersion',
                        catalogName: 'catalogName'
                    }
                ]
            };
            catalogService.getDefaultSiteForContentCatalog.and.returnValue(
                $q.when({ uid: 'siteId' })
            );
            experienceService.compareWithCurrentExperience.and.returnValue($q.when(false));
            experienceService.loadExperience.and.returnValue($q.when());
            windowUtils.getGatewayTargetFrame.and.returnValue(null);

            // WHEN
            workflowService.loadExperienceAndOpenPageWorkflowMenu(task);

            // THEN
            expect(experienceService.loadExperience).toHaveBeenCalledWith({
                siteId: 'siteId',
                catalogId: 'catalogId',
                catalogVersion: 'catalogVersion',
                pageId: 'pageUid'
            });
            expect(sharedDataService.set).toHaveBeenCalledWith(
                OPEN_PAGE_WORKFLOW_MENU,
                OpenPageWorkflowMenu.Default
            );
        });
    });

    describe('Workflow cancel', () => {
        beforeEach(() => {
            workflowService = createWorkflowInstance();
        });

        it('WHEN cancel workflow is called THEN update rest call is made', () => {
            // GIVEN
            workflowOperationsRESTService = jasmine.createSpyObj<IRestService<Workflow>>(
                'workflowOperationsRESTService',
                ['save']
            );
            restServiceFactory.get.and.returnValue(workflowOperationsRESTService);
            const workflow: Workflow = {} as Workflow;
            workflowOperationsRESTService.save.and.returnValue($q.when());

            // WHEN
            workflowService.cancelWorflow(workflow);

            // THEN
            expect(workflowOperationsRESTService.save).toHaveBeenCalled();
        });
    });

    describe('Workflow templates', () => {
        beforeEach(() => {
            workflowTemplateRESTService = jasmine.createSpyObj<IRestService<Workflow>>(
                'workflowTemplateRESTService',
                ['get']
            );
            restServiceFactory.get.and.returnValue(workflowTemplateRESTService);
            workflowTemplateRESTService.get.and.returnValue(
                $q.when({
                    templates: []
                })
            );

            workflowService = createWorkflowInstance();

            currentExperience = {
                catalogDescriptor: null,
                siteDescriptor: null,
                productCatalogVersions: null,
                time: null,
                pageContext: {
                    catalogId: CATALOG_ID,
                    catalogVersion: CATALOG_VERSION,
                    catalogVersionUuid: 'some uuid',
                    catalogName: { en: 'some catalog name' },
                    active: true,
                    siteId: 'some site ID'
                }
            };
            experienceService.getCurrentExperience.and.returnValue($q.when(currentExperience));
        });

        it('GIVEN current catalog version in experience has no workflows assigned to it WHEN areWorkflowsEnabledOnCurrentCatalogVersion is called THEN it returns false', () => {
            // WHEN
            const resultPromise = workflowService.areWorkflowsEnabledOnCurrentCatalogVersion();

            // THEN
            resultPromise.then((result: boolean) => {
                expect(result).toBeFalsy();
            });
        });

        it('GIVEN current catalog version in experience has workflows assigned to it WHEN areWorkflowsEnabledOnCurrentCatalogVersion is called THEN it returns true', () => {
            // GIVEN
            workflowTemplateRESTService.get.and.returnValue(
                $q.when({
                    templates: [
                        {
                            code: WORKFLOW_TEMPLATE_CODE1,
                            name: WORKFLOW_TEMPLATE_NAME1
                        }
                    ]
                })
            );

            // WHEN
            const resultPromise = workflowService.areWorkflowsEnabledOnCurrentCatalogVersion();

            // THEN
            resultPromise.then((result: boolean) => {
                expect(workflowTemplateRESTService.get).toHaveBeenCalledWith(
                    jasmine.objectContaining({})
                );
                expect(result).toBeTruthy();
            });
        });
    });

    describe('Active workflow for page', () => {
        const pageUuid = 'some page UUID';
        const expectedQueryParams = {
            pageSize: 1,
            currentPage: 0,
            attachment: pageUuid,
            statuses: WorkflowStatus.RUNNING + ',' + WorkflowStatus.PAUSED
        };

        beforeEach(() => {
            workflowRESTService = jasmine.createSpyObj<IRestService<Workflow>>(
                'workflowRESTService',
                ['get']
            );
            restServiceFactory.get.and.returnValue(workflowRESTService);
            workflowService = createWorkflowInstance();
        });

        it('GIVEN there is an active workflow for current page WHEN getActiveWorkflowForPageUuid is called THEN it returns the workflow found', () => {
            // GIVEN
            const expectedResult = 'some workflow property';
            const response: any = {
                workflows: [expectedResult]
            };
            workflowRESTService.get.and.returnValue($q.when(response));

            // WHEN
            const result = workflowService.getActiveWorkflowForPageUuid(pageUuid);

            // THEN
            result.then((resultWorkflow: any) => {
                expect(resultWorkflow).toBe(expectedResult);
            });
            expect(workflowRESTService.get).toHaveBeenCalledWith(expectedQueryParams);
        });

        it('GIVEN no active workflow for current page exists WHEN getActiveWorkflowForPageUuid is called THEN it returns  null', () => {
            // GIVEN
            const response: any = {
                workflows: []
            };
            workflowRESTService.get.and.returnValue($q.when(response));

            // WHEN
            const result = workflowService.getActiveWorkflowForPageUuid(pageUuid);

            // THEN
            result.then((resultWorkflow: any) => {
                expect(resultWorkflow).toBe(null);
            });
            expect(workflowRESTService.get).toHaveBeenCalledWith(expectedQueryParams);
        });

        it('GIVEN there is an active workflow for current page WHEN isPageInWorkflow is called THEN it returns true', () => {
            // GIVEN
            const page: any = {
                uuid: pageUuid
            };
            const expectedResult = 'some workflow property';
            const response: any = {
                workflows: [expectedResult]
            };
            workflowRESTService.get.and.returnValue($q.when(response));

            // WHEN
            const result = workflowService.isPageInWorkflow(page);

            // THEN
            result.then((isPageInWorkflow: any) => {
                expect(isPageInWorkflow).toBe(true);
            });
        });

        it('GIVEN no active workflow for current page WHEN isPageInWorkflow is called THEN it returns false', () => {
            // GIVEN
            const page: any = {
                uuid: pageUuid
            };
            const response: any = {
                workflows: []
            };
            workflowRESTService.get.and.returnValue($q.when(response));

            // WHEN
            const result = workflowService.isPageInWorkflow(page);

            // THEN
            result.then((isPageInWorkflow: any) => {
                expect(isPageInWorkflow).toBe(false);
            });
        });
    });

    describe('Workflow search', () => {
        beforeEach(() => {
            workflowRESTService = jasmine.createSpyObj<IRestService<Workflow>>(
                'workflowRESTService',
                ['get']
            );
            restServiceFactory.get.and.returnValue(workflowRESTService);
            workflowRESTService.get.and.returnValue($q.when({}));
            workflowService = createWorkflowInstance();
        });

        it('WHEN getWorkflows is called THEN it should be cached', () => {
            // WHEN / THEN
            const decoratorObj: any = annotationService.getMethodAnnotation(
                WorkflowService,
                'getWorkflows',
                Cached
            );
            expect(decoratorObj).toEqual(
                jasmine.objectContaining([
                    {
                        actions: [rarelyChangingContent],
                        tags: [
                            pageChangeEvictionTag,
                            perspectiveChangedEvictionTag,
                            workflowTasksMenuOpenedEvictionTag,
                            workflowCompletedEvictionTag
                        ]
                    }
                ])
            );
        });

        it('Should not prepopulate catalogId and catalogVersion with default values if they were provided during workflow search', () => {
            // GIVEN
            const queryParams = {
                pageSize: 10,
                currentPage: 0,
                status: 'STATUS',
                catalogId: 'catalogId',
                catalogVersion: 'catalogVersion'
            };
            const expectedQueryParams = lodash.clone(queryParams);

            // WHEN
            workflowService.getWorkflows(queryParams);

            // THEN
            expect(workflowRESTService.get).toHaveBeenCalledWith(expectedQueryParams);
        });
    });

    describe('Workflow Template search', () => {
        beforeEach(() => {
            workflowTemplateRESTService = jasmine.createSpyObj<IRestService<Workflow>>(
                'workflowTemplateRESTService',
                ['get']
            );
            restServiceFactory.get.and.returnValue(workflowTemplateRESTService);
            workflowTemplateRESTService.get.and.returnValue(
                $q.when({
                    templates: []
                })
            );
            workflowService = createWorkflowInstance();
        });

        it('WHEN getWorkflowTemplates is called THEN it should be cached', () => {
            // WHEN / THEN
            const decoratorObj: any = annotationService.getMethodAnnotation(
                WorkflowService,
                'getWorkflowTemplates',
                Cached
            );
            expect(decoratorObj).toEqual(
                jasmine.objectContaining([
                    {
                        actions: [rarelyChangingContent],
                        tags: [pageChangeEvictionTag]
                    }
                ])
            );
        });

        it('Should not prepopulate catalogId and catalogVersion with default values if they were provided during workflow template search', () => {
            // GIVEN
            const queryParams = {
                catalogId: 'catalogId',
                catalogVersion: 'catalogVersion'
            };
            const expectedQueryParams = lodash.clone(queryParams);

            // WHEN
            workflowService.getWorkflowTemplates(queryParams);

            // THEN
            expect(workflowTemplateRESTService.get).toHaveBeenCalledWith(expectedQueryParams);
        });

        it('Should return workflow template by code', function() {
            // GIVEN
            workflowTemplateRESTService.get.and.returnValue(
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

            // WHEN
            const result = workflowService.getWorkflowTemplateByCode(WORKFLOW_TEMPLATE_CODE1);

            // THEN
            result.then((workflow: WorkflowTemplate) => {
                expect(workflow.code).toBe(WORKFLOW_TEMPLATE_CODE1);
            });
        });
    });

    describe('Actions For Workflow Code', () => {
        let WORKFLOW_ACTION_1: any;
        let WORKFLOW_ACTION_2: any;

        beforeEach(() => {
            WORKFLOW_ACTION_1 = {
                actionType: 'type1',
                code: 'code1',
                decisions: [
                    {
                        code: 'Approve',
                        description: 'Page is correct and ready to be published',
                        name: 'Approve'
                    },
                    {
                        code: 'Reject',
                        description: 'Page needs to be reworked',
                        name: 'Reject'
                    }
                ],
                description: 'Review page to ensure it is ready to be published',
                isCurrentUserParticipant: false,
                name: 'Action 1',
                status: 'COMPLETED'
            };

            WORKFLOW_ACTION_2 = {
                actionType: 'type2',
                code: 'code2',
                decisions: [
                    {
                        code: 'Approve2',
                        description: 'Page is correct and ready to be published',
                        name: 'Approve'
                    },
                    {
                        code: 'Reject2',
                        description: 'Page needs to be reworked',
                        name: 'Reject'
                    }
                ],
                description: 'Review page to ensure it is ready to be published',
                isCurrentUserParticipant: true,
                name: 'Action 2',
                status: 'IN_PROGRESS'
            };

            workflowActionsRESTService = jasmine.createSpyObj<IRestService<Workflow>>(
                'workflowActionsRESTService',
                ['get']
            );
            restServiceFactory.get.and.returnValue(workflowActionsRESTService);
            workflowActionsRESTService.get.and.returnValue(
                $q.when({
                    workflowCode: 'someWorkflowCode',
                    actions: [WORKFLOW_ACTION_1, WORKFLOW_ACTION_2]
                })
            );
            workflowService = createWorkflowInstance();
        });

        it('WHEN getAllActionsForWorkflowCode is called THEN it should be cached', () => {
            // WHEN / THEN
            const decoratorObj: any = annotationService.getMethodAnnotation(
                WorkflowService,
                'getAllActionsForWorkflowCode',
                Cached
            );
            expect(decoratorObj).toEqual(
                jasmine.objectContaining([
                    {
                        actions: [rarelyChangingContent],
                        tags: [
                            pageChangeEvictionTag,
                            workflowTasksMenuOpenedEvictionTag,
                            workflowCompletedEvictionTag
                        ]
                    }
                ])
            );
        });

        it('WHEN getAllActionsForWorkflowCode is called THEN should return all avaialble actions', () => {
            // WHEN
            const result = workflowService.getAllActionsForWorkflowCode('someWorkflowCode');

            // THEN
            result.then((workflowActions: WorkflowAction[]) => {
                expect(workflowActions.length).toBe(2);
            });
        });

        it('WHEN getActiveActionsForWorkflowCode is called THEN should return active actions for the current user', () => {
            // WHEN
            const result = workflowService.getActiveActionsForWorkflowCode('someWorkflowCode');

            // THEN
            result.then((workflowActions: WorkflowAction[]) => {
                expect(workflowActions.length).toBe(1);
                expect(workflowActions[0].code).toBe('code2');
            });
        });

        it('WHEN isUserParticipanInActiveAction is called THEN should return true if user is participan of active action', () => {
            // WHEN
            const result = workflowService.isUserParticipanInActiveAction('someWorkflowCode');

            // THEN
            result.then((isParticipant) => {
                expect(isParticipant).toBe(true);
            });
        });

        it('WHEN isUserParticipanInActiveAction is called THEN should return false if user is not participan of active action', () => {
            // GIVEN
            WORKFLOW_ACTION_2.isCurrentUserParticipant = false;

            // WHEN
            const result = workflowService.isUserParticipanInActiveAction('someWorkflowCode');

            // THEN
            result.then((isParticipant) => {
                expect(isParticipant).toBe(false);
            });
        });
    });

    describe('Comments For Workflow Action', () => {
        const WORKFLOW_ACTION_COMMENT_1 = {
            authorName: 'Author1',
            code: 'Code1',
            creationtime: 'Time1',
            text: 'Text1'
        };

        const WORKFLOW_ACTION_COMMENT_2 = {
            authorName: 'Author2',
            code: 'Code2',
            creationtime: 'Time2',
            text: 'Text2'
        };

        beforeEach(() => {
            workflowActionCommentsRESTService = jasmine.createSpyObj<IRestService<Workflow>>(
                'workflowActionCommentsRESTService',
                ['page']
            );
            restServiceFactory.get.and.returnValue(workflowActionCommentsRESTService);
            workflowActionCommentsRESTService.page.and.returnValue(
                $q.when({
                    comments: [WORKFLOW_ACTION_COMMENT_1, WORKFLOW_ACTION_COMMENT_2],
                    pagination: {
                        currentPage: 0
                    }
                })
            );
            workflowService = createWorkflowInstance();
        });

        it('WHEN getCommentsForworkflowAction is called THEN should return all available comments associated to a given action', () => {
            // WHEN
            const result = workflowService.getCommentsForWorkflowAction(
                'someWorkflowCode',
                'someWorkflowAction',
                {
                    currentPage: 0
                }
            );

            // THEN
            result.then((response: Page<WorkflowActionComment>) => {
                expect((response.comments as WorkflowActionComment[]).length).toBe(2);
            });
        });
    });

    describe('WorkflowInboxTasks', () => {
        beforeEach(() => {
            workflowActionCommentsRESTService = jasmine.createSpyObj<IRestService<Workflow>>(
                'workflowActionCommentsRESTService',
                ['get']
            );
            restServiceFactory.get.and.returnValue(workflowActionCommentsRESTService);
            workflowService = createWorkflowInstance();
        });

        it('WHEN getTotalNumberOfActiveWorkflowTasks is called THEN it returns total count absed on information thae polling service returned', () => {
            // GIVEN
            const tasks: WorkflowTask[] = [];
            const pagination: Pagination = {
                count: 3,
                page: 0,
                totalPages: 10,
                totalCount: 20
            };
            const callbackFn = workflowTasksPollingService.addSubscriber.calls.argsFor(0)[0];
            callbackFn(tasks, pagination);

            // WHEN
            const count: number = workflowService.getTotalNumberOfActiveWorkflowTasks();

            // THEN
            expect(count).toBe(20);
        });
    });
});
