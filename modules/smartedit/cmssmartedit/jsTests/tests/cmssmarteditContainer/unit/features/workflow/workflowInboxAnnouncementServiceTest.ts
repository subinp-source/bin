/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { WorkflowTasksPollingService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowTasksPollingService';
import { WorkflowInboxAnnouncementService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowInboxAnnouncementService';
import { WorkflowTask } from 'cmssmarteditcontainer/components/workflow/dtos/WorkflowTask';
import { IAnnouncementService } from 'smarteditcommons';

describe('Test WorkflowInboxAnnouncementService', () => {
    let workflowInboxAnnouncementService: WorkflowInboxAnnouncementService;
    let workflowTasksPollingService: jasmine.SpyObj<WorkflowTasksPollingService>;
    let announcementService: jasmine.SpyObj<IAnnouncementService>;

    beforeEach(() => {
        workflowTasksPollingService = jasmine.createSpyObj('workflowTasksPollingService', [
            'addSubscriber'
        ]);
        announcementService = jasmine.createSpyObj('announcementService', ['showAnnouncement']);

        workflowInboxAnnouncementService = new WorkflowInboxAnnouncementService(
            workflowTasksPollingService,
            announcementService
        );
    });

    it('Should call addSubscriber when initialized', () => {
        expect(workflowTasksPollingService.addSubscriber).toHaveBeenCalledWith(
            jasmine.any(Function),
            false
        );
    });

    it('Should show single task announcement', () => {
        // GIVEN
        const tasks: WorkflowTask[] = getListOfTasks(1);
        const callbackFn = workflowTasksPollingService.addSubscriber.calls.argsFor(0)[0];
        spyOn(workflowInboxAnnouncementService as any, '_showSingleTaskAnnouncement');

        // WHEN
        callbackFn(tasks, jasmine.any(Object));

        // THEN
        expect(
            (workflowInboxAnnouncementService as any)._showSingleTaskAnnouncement
        ).toHaveBeenCalledWith(tasks[0]);
    });

    it('Should show multiple tasks announcement', () => {
        // GIVEN
        const tasks: WorkflowTask[] = getListOfTasks(2);
        const callbackFn = workflowTasksPollingService.addSubscriber.calls.argsFor(0)[0];
        spyOn(workflowInboxAnnouncementService as any, '_showMultipleTasksAnnouncement');

        // WHEN
        callbackFn(tasks, jasmine.any(Object));

        expect(
            (workflowInboxAnnouncementService as any)._showMultipleTasksAnnouncement
        ).toHaveBeenCalledWith(tasks);
    });

    function getListOfTasks(numberOfTasks: number, startingTaskIndex: number = 1): WorkflowTask[] {
        const result: WorkflowTask[] = [];
        for (let i = 1; i <= numberOfTasks; i++) {
            result.push({
                action: null,
                attachments: [
                    {
                        pageName: 'page' + startingTaskIndex,
                        pageUid: 'pageUid' + startingTaskIndex,
                        catalogId: 'catalogId' + startingTaskIndex,
                        catalogVersion: 'catalogVersion' + startingTaskIndex,
                        catalogName: 'catalogName' + startingTaskIndex
                    }
                ]
            });
            startingTaskIndex++;
        }
        return result;
    }
});
