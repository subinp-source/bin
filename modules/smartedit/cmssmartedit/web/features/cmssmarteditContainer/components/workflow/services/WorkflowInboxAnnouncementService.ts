/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    IAnnouncementService,
    Pagination,
    SeInjectable,
    SystemEventService
} from 'smarteditcommons';

import { WorkflowTasksPollingService } from './WorkflowTasksPollingService';
import { WorkflowTask } from '../dtos/WorkflowTask';
import { WorkflowService } from './WorkflowService';
import { CMSTimeService } from 'cmscommons';

/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.service:WorkflowInboxAnnouncemnetService
 *
 * @description
 * This service is used to show announcements for workflow inbox tasks.
 */
@SeInjectable()
export class WorkflowInboxAnnouncementService {
    private unsubscribePollingService: () => void;
    constructor(
        private workflowTasksPollingService: WorkflowTasksPollingService,
        private announcementService: IAnnouncementService
    ) {
        this.unsubscribePollingService = this.workflowTasksPollingService.addSubscriber(
            (tasks: WorkflowTask[], pagination: Pagination) => {
                if (tasks) {
                    this._displayAnnouncement(tasks);
                }
            },
            false
        );
    }

    $onDestroy(): void {
        this.unsubscribePollingService();
    }

    private _displayAnnouncement(tasks: WorkflowTask[]) {
        if (tasks.length === 1) {
            this._showSingleTaskAnnouncement(tasks[0]);
        } else if (tasks.length > 1) {
            this._showMultipleTasksAnnouncement(tasks);
        }
    }

    private _showSingleTaskAnnouncement(task: WorkflowTask) {
        let announcementId: string;

        function showAnnouncementController(
            cMSTimeService: CMSTimeService,
            announcementService: IAnnouncementService,
            workflowService: WorkflowService
        ) {
            'ngInject';
            this.task = task;
            this.getStartedAgo = () => {
                if (!!task.action.startedAgoInMillis) {
                    return cMSTimeService.getTimeAgo(task.action.startedAgoInMillis);
                }
                return null;
            };
            this.onClick = function($event: Event) {
                $event.stopPropagation();
                workflowService.loadExperienceAndOpenPageWorkflowMenu(this.task);
                announcementService.closeAnnouncement(announcementId);
            };
        }

        this._showAnnouncement(
            'workflowInboxSingleTaskAnnouncementTemplate.html',
            showAnnouncementController
        ).then((id: string) => {
            announcementId = id;
        });
    }

    private _showMultipleTasksAnnouncement(tasks: WorkflowTask[]) {
        let announcementId: string;

        function showAnnouncementController(
            systemEventService: SystemEventService,
            announcementService: IAnnouncementService,
            CMS_EVENT_OPEN_WORKFLOW_INBOX_DROPDOWN: string
        ) {
            'ngInject';
            this.getCount = function() {
                return {
                    announcementCount: tasks.length
                };
            };

            this.onClick = function($event: Event) {
                $event.stopPropagation();
                systemEventService.publish(CMS_EVENT_OPEN_WORKFLOW_INBOX_DROPDOWN);
                announcementService.closeAnnouncement(announcementId);
            };
        }

        this._showAnnouncement(
            'workflowInboxMultipleTasksAnnouncementTemplate.html',
            showAnnouncementController
        ).then((id: string) => {
            announcementId = id;
        });
    }

    private _showAnnouncement(
        templateUrl: string,
        controller: angular.IControllerConstructor
    ): PromiseLike<string> {
        return this.announcementService.showAnnouncement({
            templateUrl,
            controller
        });
    }
}
