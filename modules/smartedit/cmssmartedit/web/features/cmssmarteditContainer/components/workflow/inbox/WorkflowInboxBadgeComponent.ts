/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CrossFrameEventService, ISeComponent, SeComponent } from 'smarteditcommons';
import './workflowInboxBadge.scss';
import { WorkflowService } from '../services/WorkflowService';

@SeComponent({
    templateUrl: 'workflowInboxBadgeTemplate.html'
})
export class WorkflowInboxBadgeComponent implements ISeComponent {
    private inboxCount: number = 0;
    private unRegisterEvent: () => void;

    constructor(
        private workflowService: WorkflowService,
        private crossFrameEventService: CrossFrameEventService,
        private WORKFLOW_TASKS_COUNT_UPDATED: string
    ) {}

    $onInit(): void {
        this.unRegisterEvent = this.crossFrameEventService.subscribe(
            this.WORKFLOW_TASKS_COUNT_UPDATED,
            this.updateBadgeCount.bind(this)
        );
        this.inboxCount = this.workflowService.getTotalNumberOfActiveWorkflowTasks();
    }

    $onDestroy(): void {
        this.unRegisterEvent();
    }

    updateBadgeCount(key: string, inboxCount: number): void {
        this.inboxCount = inboxCount;
    }

    get displayCount(): string {
        return this.inboxCount <= 99 ? this.inboxCount.toString() : '99+';
    }
}
