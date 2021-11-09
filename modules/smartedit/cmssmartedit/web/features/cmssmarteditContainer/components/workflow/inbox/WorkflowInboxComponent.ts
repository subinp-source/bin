/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISeComponent, Page, Pageable, SeComponent, SystemEventService } from 'smarteditcommons';
import { WorkflowTask } from '../dtos';
import { WorkflowFacade } from 'cmssmarteditcontainer/components/workflow/facades/WorkflowFacade';
import { WorkflowTasksPollingService } from '../services/WorkflowTasksPollingService';
import './workflowInbox.scss';

@SeComponent({
    templateUrl: 'workflowInboxTemplate.html',
    inputs: ['actionItem']
})
export class WorkflowInboxComponent implements ISeComponent {
    public pageSize = 10;
    public tasksNotReady: boolean;
    public totalNumberOfTasks: number;
    public actionItem: any;
    private unRegisterEvent: () => void;

    constructor(
        private workflowFacade: WorkflowFacade,
        private systemEventService: SystemEventService,
        private workflowTasksPollingService: WorkflowTasksPollingService,
        private CMS_EVENT_OPEN_WORKFLOW_INBOX_DROPDOWN: string,
        private WORKFLOW_TASKS_COUNT_UPDATED: string
    ) {}

    $onInit() {
        this.tasksNotReady = true;
        this.unRegisterEvent = this.systemEventService.subscribe(
            this.CMS_EVENT_OPEN_WORKFLOW_INBOX_DROPDOWN,
            this.openDropdown.bind(this)
        );
    }

    $onDestroy() {
        this.workflowTasksPollingService.stopPolling();
        this.unRegisterEvent();
    }

    public loadInboxTasks = (
        mask: string,
        pageSize: number,
        currentPage: number
    ): angular.IPromise<Page<WorkflowTask>> => {
        const payload: Pageable = {
            currentPage,
            mask,
            pageSize
        };
        return this.workflowFacade
            .getWorkflowInboxTasks(payload)
            .then((page: Page<WorkflowTask>) => {
                this.tasksNotReady = false;
                this.totalNumberOfTasks = page.pagination.totalCount;
                this.systemEventService.publish(
                    this.WORKFLOW_TASKS_COUNT_UPDATED,
                    this.totalNumberOfTasks
                );
                return page;
            });
    };

    public onDropdownToggle(open: boolean) {
        if (open) {
            this.workflowTasksPollingService.stopPolling();
            return;
        }
        this.workflowTasksPollingService.startPolling();
        this.tasksNotReady = true;
    }

    private openDropdown() {
        this.actionItem.isOpen = true;
    }
}
