/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISeComponent, SeComponent } from 'smarteditcommons';
import { CMSTimeService } from 'cmscommons';
import { WorkflowAction, WorkflowActionComment } from '../../dtos';
import './workflowAction.scss';

@SeComponent({
    templateUrl: 'workflowActionCommentTemplate.html',
    inputs: ['actionComment', 'workflowAction']
})
export class WorkflowActionCommentComponent implements ISeComponent {
    public actionComment: WorkflowActionComment;
    public workflowAction: WorkflowAction;
    public isDecisionComment: boolean;

    constructor(private cMSTimeService: CMSTimeService) {
        //
    }

    $onInit() {
        if (!!this.actionComment.decisionName) {
            this.isDecisionComment = true;
        } else {
            this.isDecisionComment = false;
        }
    }

    public getCreatedAgo = (): string => {
        if (!!this.actionComment.createdAgoInMillis) {
            return this.cMSTimeService.getTimeAgo(this.actionComment.createdAgoInMillis);
        }
        return null;
    };

    public isIncomingDecision = (): boolean => {
        return this.actionComment.originalActionCode === this.workflowAction.code ? false : true;
    };
}
