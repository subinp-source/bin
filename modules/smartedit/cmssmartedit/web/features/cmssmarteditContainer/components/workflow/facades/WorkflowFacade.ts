/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    GenericEditorField,
    IAlertService,
    IPageInfoService,
    Page,
    Pageable,
    SeInjectable,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';
import {
    Workflow,
    WorkflowAction,
    WorkflowActionComment,
    WorkflowDecision,
    WorkflowOperations,
    WorkflowStatus,
    WorkflowTask,
    WorkflowTemplate
} from 'cmssmarteditcontainer/components/workflow/dtos';
import { WorkflowService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowService';

/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.service:WorkflowFacade
 *
 * @description
 * This facade is used to manage workflows
 */
@SeInjectable()
export class WorkflowFacade {
    private VERSION_LABEL_QUALIFIER = 'versionLabel';
    private CREATE_VERSION_QUALIFIER = 'createVersion';

    private startWorkflowStructureAttributes: GenericEditorField[];
    private editWorkflowStructureAttributes: GenericEditorField[];
    private makeDecisionStructureAttributes: GenericEditorField[];

    constructor(
        private alertService: IAlertService,
        private workflowService: WorkflowService,
        private confirmationModalService: any,
        private genericEditorModalService: any,
        private pageInfoService: IPageInfoService,
        private systemEventService: SystemEventService,
        private SYNCHRONIZATION_POLLING: TypedMap<string>,
        private WORKFLOW_FINISHED_EVENT: string,
        private GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT: string,
        private $q: angular.IQService
    ) {
        this.startWorkflowStructureAttributes = [
            {
                cmsStructureType: 'EditableDropdown',
                qualifier: 'templateCode',
                i18nKey: 'se.cms.workflow.editor.template',
                localized: false,
                required: true,
                idAttribute: 'code',
                labelAttributes: ['name']
            },
            {
                cmsStructureType: 'LongString',
                qualifier: 'description',
                i18nKey: 'se.cms.workflow.editor.description',
                required: false
            },
            {
                cmsStructureType: 'WorkflowCreateVersionField',
                qualifier: this.CREATE_VERSION_QUALIFIER,
                required: false
            }
        ];

        this.editWorkflowStructureAttributes = [
            {
                cmsStructureType: 'LongString',
                qualifier: 'description',
                i18nKey: 'se.cms.workflow.editor.description',
                required: false
            }
        ];

        this.makeDecisionStructureAttributes = [
            {
                cmsStructureType: 'LongString',
                qualifier: 'comment',
                i18nKey: 'se.cms.workflow.editor.comment',
                required: false
            },
            {
                cmsStructureType: 'WorkflowCreateVersionField',
                qualifier: this.CREATE_VERSION_QUALIFIER,
                required: false
            }
        ];
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowFacade#startWorkflow
     * @methodOf cmsSmarteditServicesModule.service:WorkflowFacade
     *
     * @description
     * Opens the generic editor form that is used to start a workflow.
     *
     * @returns {angular.IPromise<IWorkflow>} A promise that resolves to a workflow instance.
     */
    public startWorkflow(): angular.IPromise<Workflow> {
        return this._getWorkflowDataForEditor().then((componentData: any) => {
            const unregisterErrorListener = this.systemEventService.subscribe(
                this.GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
                this._handleUnrelatedValidationErrors.bind(this)
            );

            return this.genericEditorModalService
                .open(componentData, (result: Workflow) => {
                    this.alertService.showSuccess('se.cms.workflow.create.alert.success');
                    return result;
                })
                .finally(() => {
                    unregisterErrorListener();
                });
        });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowFacade#cancelWorflow
     * @methodOf cmsSmarteditServicesModule.service:WorkflowFacade
     *
     * @description
     * Cancels the workflow. Shows the confirmation message before sending the request.
     *
     * @param {IWorkflow} workflow The workflow to cancel.
     * @returns {angular.IPromise<void>} A promise that resolves to void.
     *
     */
    public cancelWorflow(workflow: Workflow): angular.IPromise<void> {
        return this.confirmationModalService
            .confirm({
                title: 'se.cms.workflow.cancel.confirmation.title',
                description: 'se.cms.workflow.cancel.confirmation.description'
            })
            .then(() => {
                return this.workflowService.cancelWorflow(workflow).then(() => {
                    this.alertService.showSuccess('se.cms.workflow.cancel.alert.success');
                });
            });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowFacade#editWorkflow
     * @methodOf cmsSmarteditServicesModule.service:WorkflowFacade
     *
     * @description
     * Edit the workflow.
     *
     * @param {Workflow} workflow The workflow to cancel.
     * @returns {angular.IPromise<Workflow>} A promise that resolves to void.
     */
    public editWorkflow(workflow: Workflow): angular.IPromise<Workflow> {
        return this._getWorkflowDataForEditor(workflow).then((componentData: any) => {
            return this.genericEditorModalService.open(componentData, (result: Workflow) => {
                return result;
            });
        });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowFacade#getAllActionsForWorkflow
     * @methodOf cmsSmarteditServicesModule.service:WorkflowFacade
     *
     * @description
     * Return all actions for a given workflow code.
     *
     * @param {string} workflowCode The code of the workflow.
     * @returns {angular.IPromise<WorkflowAction[]>} A promise that resolves to list of available actions.
     */
    public getAllActionsForWorkflow(workflowCode: string): angular.IPromise<WorkflowAction[]> {
        return this.workflowService.getAllActionsForWorkflowCode(workflowCode);
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowFacade#getAllActionsForWorkflow
     * @methodOf cmsSmarteditServicesModule.service:WorkflowFacade
     *
     * @description
     * Return all active actions for a given workflow code and for the current user.
     *
     * @param {string} workflowCode The code of the workflow.
     * @returns {angular.IPromise<WorkflowAction[]>} A promise that resolves to list of available actions.
     */
    public getActiveActionsForWorkflow(workflowCode: string): angular.IPromise<WorkflowAction[]> {
        return this.workflowService.getActiveActionsForWorkflowCode(workflowCode);
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowFacade#getCommentsForworkflowAction
     * @methodOf cmsSmarteditServicesModule.service:WorkflowFacade
     *
     * @description
     * Return a paged list of comments for a given workflow action.
     * Since the result is paginated and used by the yInfiniteScrollingTemplate, the list of components are populated in the results property.
     *
     * @param {string} workflowCode The code of the workflow.
     * @param {string} workflowActionCode The code of the workflow action.
     * @param {Pageable} payload The pageable information.
     * @returns {angular.IPromise<WorkflowActionComment>} A promise that resolves to list of available comments for a given workflow and workflow action.
     */
    public getCommentsForWorkflowAction(
        workflowCode: string,
        workflowActionCode: string,
        payload: Pageable
    ): angular.IPromise<Page<WorkflowActionComment>> {
        return this.workflowService
            .getCommentsForWorkflowAction(workflowCode, workflowActionCode, payload)
            .then((response: Page<WorkflowActionComment>) => {
                response.results = response.comments as WorkflowActionComment[];
                delete response.comments;
                return response;
            });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowFacade#getWorkflowInboxTasks
     * @methodOf cmsSmarteditServicesModule.service:WorkflowFacade
     *
     * @description
     * Returns a paged list of workflow inbox tasks active for a given user.
     *
     * @param {Pageable} payload The pageable information.
     * @returns {angular.IPromise<Page<WorkflowTask>>} A promise that resolves to a page of workflow inbox tasks for a given user.
     */
    public getWorkflowInboxTasks(payload: Pageable): angular.IPromise<Page<WorkflowTask>> {
        return this.workflowService
            .getWorkflowInboxTasks(payload)
            .then((page: Page<WorkflowTask>) => {
                page.results = page.tasks as WorkflowTask[];
                delete page.tasks;
                return page;
            });
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowFacade#makeDecision
     * @methodOf cmsSmarteditServicesModule.service:WorkflowFacade
     *
     * @description
     * Makes a decision for a given workflow action and workflow decision.
     *
     * @param {string} workflowCode The code of the workflow.
     * @param {WorkflowAction} workflowAction The workflow action object.
     * @param {WorkflowDecision} decision The workflow decision object.
     * @returns {angular.IPromise<void>} A promise that resolves to void.
     */
    public makeDecision(
        workflowCode: string,
        workflowAction: WorkflowAction,
        decision: WorkflowDecision
    ): angular.IPromise<Workflow> {
        const componentData = {
            title: decision.name,
            structure: {
                attributes: this.makeDecisionStructureAttributes
            },
            contentApi: this.workflowService.getResourceWorkflowOperationsURI(),
            saveLabel: 'se.cms.workflow.editor.button.submit',
            content: {
                operation: WorkflowOperations.MAKE_DECISION,
                workflowCode,
                actionCode: workflowAction.code,
                decisionCode: decision.code
            },
            initialDirty: true
        };
        return this.genericEditorModalService.open(
            componentData,
            (result: Workflow) => {
                return this.pageInfoService.getPageUUID().then((currentPageUuid: string) => {
                    return this.systemEventService
                        .publish(this.SYNCHRONIZATION_POLLING.SPEED_UP, currentPageUuid)
                        .then(() => {
                            if (result.status.toLowerCase() === WorkflowStatus.FINISHED) {
                                this.alertService.showSuccess(
                                    'se.cms.workflow.completed.alert.success'
                                );
                                this.systemEventService.publishAsync(this.WORKFLOW_FINISHED_EVENT);
                            } else {
                                this.alertService.showSuccess({
                                    message: 'se.cms.workflow.make.decision.success',
                                    messagePlaceholders: {
                                        workflowDecisionName: decision.name,
                                        workflowActionName: workflowAction.name
                                    }
                                });
                            }
                            // resets back to slow polling
                            this.systemEventService.publish(
                                this.SYNCHRONIZATION_POLLING.SLOW_DOWN,
                                currentPageUuid
                            );
                            return result;
                        });
                });
            },
            null
        );
    }

    /**
     * Returns the data structure for a generic editor.
     * @param {Object} content the data object to populate generic editor.
     * If content is null the generic editor displays all field to start a workflow.
     * If content is not null the generic editor dispalys fields to edit a workflow.
     *
     * @returns {Object} the data structure for generic editor.
     */
    private _getWorkflowDataForEditor(workflow: Workflow = null): angular.IPromise<any> {
        const promise = this.pageInfoService.getPageUUID().then((pageUuid: string) => {
            const isStartingWorkflow = workflow === null;
            const componentData: any = {
                cssClasses: '',
                contentApi: this.workflowService.getResourceWorkflowURI()
            };

            if (isStartingWorkflow) {
                const templateCodeField = this.startWorkflowStructureAttributes.find(
                    (structureAttribute: GenericEditorField) => {
                        return structureAttribute.qualifier === 'templateCode';
                    }
                );
                return this.workflowService
                    .getWorkflowTemplates({})
                    .then((workflowTemplates: WorkflowTemplate[]) => {
                        templateCodeField.options = workflowTemplates;

                        componentData.title = 'se.cms.workflow.editor.start.workflow.title';
                        componentData.saveLabel = 'se.cms.workflow.editor.button.start';
                        componentData.structure = {
                            attributes: this.startWorkflowStructureAttributes
                        };
                        componentData.content = {
                            attachments: [pageUuid]
                        };
                        return componentData;
                    });
            } else {
                componentData.title = 'se.cms.workflow.editor.edit.workflow.title';
                componentData.saveLabel = 'se.cms.workflow.editor.button.save';
                componentData.structure = {
                    attributes: this.editWorkflowStructureAttributes
                };
                componentData.componentUuid = workflow.workflowCode;
                componentData.componentType = 'workflow';
            }
            return componentData;
        });

        return this.$q.when(promise);
    }

    /**
     * Handles errors that the generic editor didn't handle directly (for example, due to unknown qualifiers) in two
     * ways:
     * 1. Even though there are different attributes for createVersion and versionLabel, in the front-end they are handled
     *    in the same widget, under the createVersion qualifier. However, when validating, the backend sends the error
     *    directly to versionLabel. Thus, any time there's an error directed to versionLabel it needs to be assigned to
     *    createVersion and republished for it to become visible.
     * 2. Any other error will be displayed in an alert.
     *
     * @param {String} key The key of the event that this method is handling.
     * @param {Object} eventData Object containing the errors that were not handled by the generic editor.
     */
    private _handleUnrelatedValidationErrors(key: string, eventData: any): void {
        if (eventData.sourceGenericEditorId) {
            let alertMessage = '';
            const errorsToRepublish: any[] = [];

            eventData.messages.forEach((error: any) => {
                if (error.subject === this.VERSION_LABEL_QUALIFIER) {
                    error.subject = this.CREATE_VERSION_QUALIFIER;
                    errorsToRepublish.push(error);
                } else {
                    alertMessage += error.message + ' ';
                }
            });

            if (alertMessage) {
                this.alertService.showDanger(alertMessage);
            }

            if (errorsToRepublish.length > 0) {
                this.systemEventService.publishAsync(
                    this.GENERIC_EDITOR_UNRELATED_VALIDATION_MESSAGES_EVENT,
                    {
                        messages: errorsToRepublish
                    }
                );
            }
        }
    }
}
