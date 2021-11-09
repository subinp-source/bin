/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ISeComponent,
    Page,
    Pageable,
    PopupOverlayConfig,
    SeComponent,
    SystemEventService
} from 'smarteditcommons';
import { CMSTimeService } from 'cmscommons';
import {
    Workflow,
    WorkflowAction,
    WorkflowActionComment,
    WorkflowActionStatus,
    WorkflowDecision
} from '../../dtos';
import { WorkflowFacade } from 'cmssmarteditcontainer/components/workflow/facades/WorkflowFacade';

@SeComponent({
    templateUrl: 'workflowActionItemTemplate.html',
    inputs: ['workflow', 'workflowAction', 'canMakeDecisions']
})
export class WorkflowActionItemComponent implements ISeComponent {
    public pageSize: number;
    public commentsCount: number = 0;
    public isReady: boolean = false;
    public hasNoComments: boolean = false;

    public workflow: Workflow;
    public workflowAction: WorkflowAction;
    public canMakeDecisions: boolean;

    public isMenuOpen: boolean = false;
    public popupConfig: PopupOverlayConfig;

    private collapsibleContainerApi: any;
    private isContainerContentLoaded: boolean;

    private unregWorkflowMenuOpenedEvent: () => void;

    constructor(
        private workflowFacade: WorkflowFacade,
        private cMSTimeService: CMSTimeService,
        private $translate: angular.translate.ITranslateService,
        private systemEventService: SystemEventService,
        private WORKFLOW_DECISION_SELECTED_EVENT: string
    ) {
        this.pageSize = 10;
        this.unregWorkflowMenuOpenedEvent = this.systemEventService.subscribe(
            'WORKFLOW_ITEM_MENU_OPENED_EVENT',
            this.onOtherMenuOpening.bind(this)
        );

        this.popupConfig = {
            templateUrl: 'WorkflowActionItemMultipleDecisionsTemplate.html',
            legacyController: { alias: '$ctrl', value: () => this },
            halign: 'right',
            valign: 'bottom'
        };

        this.isContainerContentLoaded = false;
    }

    $onInit(): void {
        this.isReady = true;
    }

    $onDestroy(): void {
        this.unregWorkflowMenuOpenedEvent();
    }

    public getCollapsibleContainerApi($api: any): void {
        this.collapsibleContainerApi = $api;
    }

    public loadComments = (
        mask: string,
        pageSize: number,
        currentPage: number
    ): angular.IPromise<Page<WorkflowActionComment>> => {
        const payload: Pageable = {
            currentPage,
            mask,
            pageSize
        };
        return this.workflowFacade
            .getCommentsForWorkflowAction(
                this.workflow.workflowCode,
                this.workflowAction.code,
                payload
            )
            .then((page: Page<WorkflowActionComment>) => {
                this.hasNoComments = page.pagination.totalCount === 0;
                return page;
            });
    };

    public getActiveSince = (): string => {
        if (!!this.workflowAction.startedAgoInMillis) {
            return this.cMSTimeService.getTimeAgo(this.workflowAction.startedAgoInMillis);
        }
        return null;
    };

    public getReadableStatus = (): string => {
        if (this.workflowAction.status.toLowerCase() === WorkflowActionStatus.IN_PROGRESS) {
            return this.$translate.instant('se.cms.actionitem.page.workflow.action.status.started');
        } else if (this.workflowAction.status.toLowerCase() === WorkflowActionStatus.PENDING) {
            return this.$translate.instant(
                'se.cms.actionitem.page.workflow.action.status.not.started'
            );
        }
        return this.workflowAction.status;
    };

    public hasMultipleDecisions = (): boolean => {
        return this.workflowAction.decisions && this.workflowAction.decisions.length > 1;
    };

    public makeDecision = (decision: WorkflowDecision): angular.IPromise<Workflow> => {
        // Close menus
        this.isMenuOpen = false;
        this.systemEventService.publish(this.WORKFLOW_DECISION_SELECTED_EVENT);

        return this.workflowFacade.makeDecision(
            this.workflow.workflowCode,
            this.workflowAction,
            decision
        );
    };

    public displayDecisionsButtons = (): boolean => {
        const decisions = this.workflowAction.decisions;
        return (
            this.canMakeDecisions &&
            this.workflowAction.isCurrentUserParticipant &&
            decisions.length > 0
        );
    };

    /**
     * This method is necessary to make sure that comments are loaded only on-demand. Without this method the
     * infinite scroll used inside the container would load the comments as soon as the workflow menu is opened. Instead, this
     * method only shows the infinite scrolling (and thus loads the comments) only the first time the collapsible container
     * is expanded.
     */
    public showComments(): boolean {
        const isContainerExpanded =
            this.collapsibleContainerApi && this.collapsibleContainerApi.isExpanded();

        if (isContainerExpanded && !this.isContainerContentLoaded) {
            this.isContainerContentLoaded = true;
        }

        return isContainerExpanded || this.isContainerContentLoaded;
    }

    public getWorkflowActionStatusClass = (): string => {
        if (this.workflowAction.status.toLowerCase() === WorkflowActionStatus.IN_PROGRESS) {
            return 'se-workflow-action-item--started';
        } else if (this.workflowAction.status.toLowerCase() === WorkflowActionStatus.COMPLETED) {
            return 'se-workflow-action-item--completed';
        }
        return 'se-workflow-action-item';
    };

    // --------------------------------------------------------------------------------------
    // Event Handlers
    // --------------------------------------------------------------------------------------
    public onMainButtonClick = ($event: any, decision: WorkflowDecision): void => {
        // This is necessary to stop the action history from opening when the main button is clicked.
        $event.preventDefault();
        $event.stopPropagation();

        this.makeDecision(decision);
    };

    public onSplitButtonClick = ($event: any): void => {
        $event.preventDefault();
        $event.stopPropagation();

        this.isMenuOpen = !this.isMenuOpen;
        if (this.isMenuOpen) {
            this.systemEventService.publishAsync('WORKFLOW_ITEM_MENU_OPENED_EVENT', {
                code: this.workflowAction.code
            });
        }
    };

    onMenuHide(): void {
        this.isMenuOpen = false;
    }

    onOtherMenuOpening(eventId: string, eventData: any): void {
        if (this.workflowAction.code !== eventData.code) {
            this.isMenuOpen = false;
        }
    }
}
