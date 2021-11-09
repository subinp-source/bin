/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    pageChangeEvictionTag,
    perspectiveChangedEvictionTag,
    rarelyChangingContent,
    Cached,
    CrossFrameEventService,
    ICatalogService,
    IDefaultExperienceParams,
    IExperienceService,
    IPerspectiveService,
    IRestService,
    IRestServiceFactory,
    ISharedDataService,
    ISite,
    Page,
    Pageable,
    Pagination,
    SearchParams,
    SeInjectable,
    SystemEventService,
    WindowUtils
} from 'smarteditcommons';
import {
    Workflow,
    WorkflowAction,
    WorkflowActionComment,
    WorkflowActionStatus,
    WorkflowEditableItem,
    WorkflowEditableItemsList,
    WorkflowList,
    WorkflowOperations,
    WorkflowStatus,
    WorkflowTask,
    WorkflowTemplate,
    WorkflowTemplateList
} from 'cmssmarteditcontainer/components/workflow/dtos';
import {
    workflowCompletedEvictionTag,
    workflowTasksMenuOpenedEvictionTag,
    CMSModesService,
    ICMSPage
} from 'cmscommons';
import { WorkflowTasksPollingService } from '../services/WorkflowTasksPollingService';

/** @internal */
export enum OpenPageWorkflowMenu {
    Default = 'Default',
    SwitchPerspective = 'SwitchPerspective'
}

/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.service:WorkflowService
 *
 * @description
 * This service is used to manage workflows
 */
@SeInjectable()
export class WorkflowService {
    private resourceWorkflowURI: string;
    private resourceWorkflowOperationsURI: string;
    private resourceWorkflowTemplateURI: string;
    private resourceWorkflowActionsURI: string;
    private resourceWorkflowActionCommentsURI: string;
    private resourceWorkflowEditableItemsURI: string;
    private resourceWorkflowInboxTasksURI: string;

    private workflowRESTService: IRestService<WorkflowList>;
    private workflowActionsRESTService: IRestService<Workflow>;
    private workflowOperationsRESTService: IRestService<Workflow>;
    private workflowTemplateRESTService: IRestService<WorkflowTemplateList>;
    private workflowActionCommentsRESTService: IRestService<WorkflowActionComment>;
    private workflowEditableItemsRESTService: IRestService<WorkflowEditableItemsList>;
    private workflowInboxTasksRESTService: IRestService<WorkflowTask>;

    private totalNumberOfTasks: number;

    constructor(
        private $q: angular.IQService,
        private restServiceFactory: IRestServiceFactory,
        private crossFrameEventService: CrossFrameEventService,
        private systemEventService: SystemEventService,
        private sharedDataService: ISharedDataService,
        private perspectiveService: IPerspectiveService,
        private catalogService: ICatalogService,
        private experienceService: IExperienceService,
        private workflowTasksPollingService: WorkflowTasksPollingService,
        private windowUtils: WindowUtils,
        private EVENT_PERSPECTIVE_REFRESHED: string,
        private EVENT_PERSPECTIVE_CHANGED: string,
        private OPEN_PAGE_WORKFLOW_MENU: string,
        private CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU: string,
        private PAGE_CONTEXT_CATALOG: string,
        private PAGE_CONTEXT_CATALOG_VERSION: string,
        private WORKFLOW_TASKS_COUNT_UPDATED: string
    ) {
        this.resourceWorkflowURI = `/cmswebservices/v1/catalogs/${
            this.PAGE_CONTEXT_CATALOG
        }/versions/${this.PAGE_CONTEXT_CATALOG_VERSION}/workflows`;
        this.resourceWorkflowActionsURI = `/cmswebservices/v1/catalogs/${
            this.PAGE_CONTEXT_CATALOG
        }/versions/${this.PAGE_CONTEXT_CATALOG_VERSION}/workflows/:workflowCode/actions`;
        this.resourceWorkflowTemplateURI = `/cmswebservices/v1/catalogs/${
            this.PAGE_CONTEXT_CATALOG
        }/versions/${this.PAGE_CONTEXT_CATALOG_VERSION}/workflowtemplates`;
        this.resourceWorkflowOperationsURI = `/cmswebservices/v1/catalogs/${
            this.PAGE_CONTEXT_CATALOG
        }/versions/${this.PAGE_CONTEXT_CATALOG_VERSION}/workflows/:workflowCode/operations`;
        this.resourceWorkflowActionCommentsURI = `/cmswebservices/v1/catalogs/${
            this.PAGE_CONTEXT_CATALOG
        }/versions/${
            this.PAGE_CONTEXT_CATALOG_VERSION
        }/workflows/:workflowCode/actions/:actionCode/comments`;
        this.resourceWorkflowEditableItemsURI = `/cmssmarteditwebservices/v1/catalogs/${
            this.PAGE_CONTEXT_CATALOG
        }/versions/${this.PAGE_CONTEXT_CATALOG_VERSION}/workfloweditableitems`;
        this.resourceWorkflowInboxTasksURI = `/cmssmarteditwebservices/v1/inbox/workflowtasks`;

        this.workflowRESTService = this.restServiceFactory.get(this.resourceWorkflowURI);
        this.workflowTemplateRESTService = this.restServiceFactory.get(
            this.resourceWorkflowTemplateURI
        );
        this.workflowActionsRESTService = this.restServiceFactory.get(
            this.resourceWorkflowActionsURI
        );
        this.workflowInboxTasksRESTService = this.restServiceFactory.get(
            this.resourceWorkflowInboxTasksURI
        );
        this.workflowEditableItemsRESTService = this.restServiceFactory.get(
            this.resourceWorkflowEditableItemsURI
        );

        this.crossFrameEventService.subscribe(
            this.EVENT_PERSPECTIVE_REFRESHED,
            this.openPageWorkflowMenu.bind(this)
        );
        this.crossFrameEventService.subscribe(
            this.EVENT_PERSPECTIVE_CHANGED,
            this.openPageWorkflowMenu.bind(this)
        );

        this.workflowTasksPollingService.addSubscriber(
            (tasks: WorkflowTask[], pagination: Pagination) => {
                this.totalNumberOfTasks = pagination.totalCount || 0;
                this.crossFrameEventService.publish(
                    this.WORKFLOW_TASKS_COUNT_UPDATED,
                    this.totalNumberOfTasks
                );
            },
            true
        );
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#cancelWorflow
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Cancels the workflow. Shows the confirmation message before sending the request.
     *
     * @param {IWorkflow} workflow The workflow to cancel.
     * @returns {angular.IPromise<void>} A promise that resolves to void.
     *
     */
    public cancelWorflow(workflow: Workflow): angular.IPromise<Workflow> {
        this.workflowOperationsRESTService = this.restServiceFactory.get(
            this.resourceWorkflowOperationsURI.replace(':workflowCode', workflow.workflowCode)
        );
        return this.$q.when(
            this.workflowOperationsRESTService.save({
                operation: WorkflowOperations.CANCEL
            })
        );
    }

    /**
     * Returns a workflow template using its code.
     * @param code the code of a template.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves with the workflow template result. If the
     * request fails, it resolves with errors from the backend.
     */
    public getWorkflowTemplateByCode(code: string): angular.IPromise<WorkflowTemplate> {
        return this.getWorkflowTemplates({}).then((workflowTemplates: WorkflowTemplate[]) => {
            const workflow: WorkflowTemplate = workflowTemplates.find((wf: WorkflowTemplate) => {
                return wf.code === code;
            });
            return workflow;
        });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#areWorkflowsEnabledOnCurrentCatalogVersion
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * This method determines whether the current catalog version (the one in the current experience) has workflows
     * enabled. A catalog version has workflows enabled if it has at least one workflow template assigned to it.
     *
     * @returns {angular.IPromise<boolean>} A promise that resolves to a boolean. It will be true, if the workflow is
     * enabled for the current catalog version. False, otherwise.
     */
    public areWorkflowsEnabledOnCurrentCatalogVersion(): angular.IPromise<boolean> {
        return this.getWorkflowTemplates({}).then((workflowTemplates: WorkflowTemplate[]) => {
            return workflowTemplates && workflowTemplates.length > 0;
        });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#getWorkflows
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Fetch workflow search result by making a REST call to the workflow API.
     *
     * @param {Object} queryParams The object representing the query params
     * @param {String} queryParams.pageSize number of items in the page
     * @param {String} queryParams.currentPage current page number
     * @param {String} queryParams.attachments comma separated list of attachment id
     * @param {String} queryParams.status comma separated list of workflow status
     * @param {String =} queryParams.catalogId the catalog to search items in. If empty, the current context catalog will be used.
     * @param {String =} queryParams.catalogVersion the catalog version to search items in. If empty, the current context catalog version will be used.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves with the workflow search result. If the
     * request fails, it resolves with errors from the backend.
     */
    @Cached({
        actions: [rarelyChangingContent],
        tags: [
            pageChangeEvictionTag,
            perspectiveChangedEvictionTag,
            workflowTasksMenuOpenedEvictionTag,
            workflowCompletedEvictionTag
        ]
    })
    public getWorkflows(queryParams: SearchParams): angular.IPromise<Workflow[]> {
        return this.$q.when(
            this.workflowRESTService.get(queryParams).then((response: WorkflowList) => {
                return response.workflows;
            })
        );
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#getActiveWorkflowForPageUuid
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Fetch an active workflow for a page uuid.
     * @param {String} pageUuid the page uuid
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves with the workflow object
     * or null if there is no active workflow for provided page uuid. If the request fails, it resolves with errors
     * from the backend.
     */
    public getActiveWorkflowForPageUuid(pageUuid: string): angular.IPromise<Workflow> {
        return this.getWorkflows({
            pageSize: 1,
            currentPage: 0,
            attachment: pageUuid,
            statuses: WorkflowStatus.RUNNING + ',' + WorkflowStatus.PAUSED
        }).then((workflows: Workflow[]) => {
            return workflows[0] === undefined ? null : workflows[0];
        });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#isPageInWorkflow
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Verifies whether the page is in a workflow or not.
     *
     * @param {ICMSPage} page the page to verify
     *
     * @returns {boolean} If request is successful, it returns a promise that resolves with boolean value.
     * If the request fails, it resolves with errors from the backend.
     */
    public isPageInWorkflow(page: ICMSPage): angular.IPromise<boolean> {
        return this.getActiveWorkflowForPageUuid(page.uuid).then((workflow: Workflow) => {
            return workflow ? true : false;
        });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#getWorkflowTemplates
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Fetch workflow templates search result by making a REST call to the workflow API.
     *
     * @param {Object} queryParams The object representing the query params
     * @param {String =} queryParams.catalogId the catalog to search items in. If empty, the current context catalog will be used.
     * @param {String =} queryParams.catalogVersion the catalog version to search items in. If empty, the current context catalog version will be used.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves with the workflow template search result. If the
     * request fails, it resolves with errors from the backend.
     */
    @Cached({ actions: [rarelyChangingContent], tags: [pageChangeEvictionTag] })
    public getWorkflowTemplates(queryParams: SearchParams): angular.IPromise<WorkflowTemplate[]> {
        return this.$q.when(
            this.workflowTemplateRESTService
                .get(queryParams)
                .then((response: WorkflowTemplateList) => {
                    return response.templates;
                })
        );
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#getAllActionsForWorkflowCode
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Fetch all actions for a given workflow code.
     *
     * @param {String} workflowCode The code of the workflow.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves to list of available actions. If the
     * request fails, it resolves with errors from the backend.
     */
    @Cached({
        actions: [rarelyChangingContent],
        tags: [
            pageChangeEvictionTag,
            workflowTasksMenuOpenedEvictionTag,
            workflowCompletedEvictionTag
        ]
    })
    public getAllActionsForWorkflowCode(workflowCode: string): angular.IPromise<WorkflowAction[]> {
        return this.$q
            .when(
                this.workflowActionsRESTService.get({
                    workflowCode
                })
            )
            .then((response: Workflow) => {
                return response.actions;
            });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#isUserParticipanInActiveAction
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Verifies whether the use is a participant of a current active action.
     *
     * @param {String} workflowCode The code of the workflow.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves to a boolean. If the
     * request fails, it resolves with errors from the backend.
     */
    public isUserParticipanInActiveAction(workflowCode: string): angular.IPromise<boolean> {
        return this.getActiveActionsForWorkflowCode(workflowCode).then((activeActions) => {
            return activeActions.length > 0;
        });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#getActiveActionsForWorkflowCode
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Fetch all active actions for a given workflow code and for the current user.
     *
     * @param {String} workflowCode The code of the workflow.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves to list of active actions. If the
     * request fails, it resolves with errors from the backend.
     */
    public getActiveActionsForWorkflowCode(
        workflowCode: string
    ): angular.IPromise<WorkflowAction[]> {
        return this.getAllActionsForWorkflowCode(workflowCode).then((actions: WorkflowAction[]) => {
            return actions.filter(
                (action) =>
                    action.isCurrentUserParticipant &&
                    (WorkflowActionStatus.IN_PROGRESS === action.status.toLowerCase() ||
                        WorkflowActionStatus.PAUSED === action.status.toLowerCase())
            );
        });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#getCommentsForWorkflowAction
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Fetch a page of comments for a given workflow action and some pageable data.
     *
     * @param {String} workflowCode The code of the workflow.
     * @param {String} workflowActionCode The code of the workflow action.
     * @param {Pageable} payload The pageable information.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves to list of available comments for a given workflow and workflow action. If the
     * request fails, it resolves with errors from the backend.
     */
    public getCommentsForWorkflowAction(
        workflowCode: string,
        workflowActionCode: string,
        payload: Pageable
    ): angular.IPromise<Page<WorkflowActionComment>> {
        this.workflowActionCommentsRESTService = this.restServiceFactory.get(
            this.resourceWorkflowActionCommentsURI
                .replace(':workflowCode', workflowCode)
                .replace(':actionCode', workflowActionCode)
        );
        return this.$q.when(this.workflowActionCommentsRESTService.page(payload));
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#getWorkflowInboxTasks
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Fetches a page of workflow inbox tasks active for a given user.
     *
     * @param {Pageable} payload The pageable information.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves to a page of workflow inbox tasks for a given user. If the
     * request fails, it resolves with errors from the backend.
     */
    public getWorkflowInboxTasks(payload: Pageable): angular.IPromise<Page<WorkflowTask>> {
        return this.$q.when(this.workflowInboxTasksRESTService.page(payload));
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#getTotalNumberOfActiveWorkflowTasks
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Returns the total number of active workflow tasks for a given user.
     *
     * @returns {number} The total number of active workflow tasks.
     */
    public getTotalNumberOfActiveWorkflowTasks(): number {
        return this.totalNumberOfTasks || 0;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowService#getWorkflowEditableItems
     * @methodOf cmsSmarteditServicesModule.service:WorkflowService
     *
     * @description
     * Returns information about whether each item is editable or not. It also returns a workflow code where item is editable.
     *
     * @param {string[]} itemUids The list of uids
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves to a list of objects where each object
     * contains information about whether each item is editable or not. If the request fails, it resolves with errors from the backend.
     */
    public getWorkflowEditableItems(itemUids: string[]): angular.IPromise<WorkflowEditableItem[]> {
        return this.$q
            .when(
                this.workflowEditableItemsRESTService.get({
                    itemUids
                })
            )
            .then((data: WorkflowEditableItemsList) => {
                return data.editableItems;
            });
    }

    /**
     * Returns a resource uri for workflows.
     */
    public getResourceWorkflowURI(): string {
        return this.resourceWorkflowURI;
    }

    /**
     * Returns a resource uri for workflow operations.
     */
    public getResourceWorkflowOperationsURI(): string {
        return this.resourceWorkflowOperationsURI;
    }

    /**
     * Opens the page workflow menu. If the current perspective is not basic or advanced, it will switch to advanced perspective and then opens the menu.
     */
    public openPageWorkflowMenu() {
        this.sharedDataService.get(this.OPEN_PAGE_WORKFLOW_MENU).then((data: string) => {
            if (data === OpenPageWorkflowMenu.Default) {
                this.perspectiveService
                    .getActivePerspectiveKey()
                    .then((activePerspective: string) => {
                        if (
                            activePerspective === CMSModesService.BASIC_PERSPECTIVE_KEY ||
                            activePerspective === CMSModesService.ADVANCED_PERSPECTIVE_KEY
                        ) {
                            this.systemEventService.publish(
                                this.CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU,
                                true
                            );
                            this.sharedDataService.remove(this.OPEN_PAGE_WORKFLOW_MENU);
                        } else {
                            this.sharedDataService
                                .set(
                                    this.OPEN_PAGE_WORKFLOW_MENU,
                                    OpenPageWorkflowMenu.SwitchPerspective
                                )
                                .then(() => {
                                    this.perspectiveService.switchTo(
                                        CMSModesService.ADVANCED_PERSPECTIVE_KEY
                                    );
                                });
                        }
                    });
            } else if (data === OpenPageWorkflowMenu.SwitchPerspective) {
                this.systemEventService.publish(this.CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU, true);
                this.sharedDataService.remove(this.OPEN_PAGE_WORKFLOW_MENU);
            }
        });
    }

    /**
     * Loads the experience by building experience params from the given Workflow Task and then opens the page workflow menu.
     * If the current experience is same as the experience params from the given workflow task, it just opens the page workflow menu.
     * Otherwise, it loads the experience and then opens the page workflow menu.
     * @param {WorkflowTask} task the workflow inbox task
     */
    public loadExperienceAndOpenPageWorkflowMenu(task: WorkflowTask) {
        if (task) {
            this.catalogService
                .getDefaultSiteForContentCatalog(task.attachments[0].catalogId)
                .then((defaultSite: ISite) => {
                    const experienceParams: IDefaultExperienceParams = {
                        siteId: defaultSite.uid,
                        catalogId: task.attachments[0].catalogId,
                        catalogVersion: task.attachments[0].catalogVersion,
                        pageId: task.attachments[0].pageUid
                    };

                    /**
                     * First check if you are in storefront view or not,
                     * - If in storefront view, then check if same as current experience or not.
                     * 		- If requested experience is same as current experience then just open the workflow task menu.
                     * 		- If requested experience is not same as current experience then load the provided experience.
                     * - If not in storefront view, then load the provided experience.
                     */
                    if (!!this.windowUtils.getGatewayTargetFrame()) {
                        this.experienceService
                            .compareWithCurrentExperience(experienceParams)
                            .then((isEqual: boolean) => {
                                if (isEqual) {
                                    this.sharedDataService
                                        .set(
                                            this.OPEN_PAGE_WORKFLOW_MENU,
                                            OpenPageWorkflowMenu.Default
                                        )
                                        .then(() => {
                                            this.openPageWorkflowMenu();
                                        });
                                } else {
                                    this._loadExperience(experienceParams);
                                }
                            });
                    } else {
                        this._loadExperience(experienceParams);
                    }
                });
        }
    }

    private _loadExperience(experinece: IDefaultExperienceParams): void {
        this.experienceService.loadExperience(experinece).then(() => {
            this.sharedDataService.set(this.OPEN_PAGE_WORKFLOW_MENU, OpenPageWorkflowMenu.Default);
        });
    }
}
