/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISeComponent, SeComponent } from 'smarteditcommons';
import { WorkflowTask } from '../../dtos';
import { WorkflowService } from '../../services/WorkflowService';
import { CMSTimeService } from 'cmscommons';

@SeComponent({
    templateUrl: 'workflowInboxTaskTemplate.html',
    inputs: ['task']
})
export class WorkflowInboxTaskComponent implements ISeComponent {
    public task: WorkflowTask;

    constructor(private cMSTimeService: CMSTimeService, private workflowService: WorkflowService) {}

    public getTaskName = (): string => {
        return this.task.action.name;
    };

    public getTaskDescription = (): string => {
        return (
            this.task.attachments[0].catalogName +
            ' ' +
            this.task.attachments[0].catalogVersion +
            ' | ' +
            this.task.attachments[0].pageName
        );
    };

    public getTaskCreatedAgo = (): string => {
        return this.cMSTimeService.getTimeAgo(this.task.action.startedAgoInMillis);
    };

    public onClick = (): void => {
        this.workflowService.loadExperienceAndOpenPageWorkflowMenu(this.task);
    };
}
