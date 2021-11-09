/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import * as lo from 'lodash';
import * as angular from 'angular';
import { WorkflowTasksPollingService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowTasksPollingService';
import { CrossFrameEventService, IRestService, IRestServiceFactory } from 'smarteditcommons';
import { WorkflowTaskList } from 'cmssmarteditcontainer/components/workflow/dtos/WorkflowTaskList';
import { WorkflowTask } from 'cmssmarteditcontainer/components/workflow/dtos/WorkflowTask';
import { promiseHelper } from 'testhelpers';

describe('Test WorkflowTasksPollingService', () => {
    let $q: jasmine.SpyObj<angular.IQService>;
    let workflowTasksPollingService: WorkflowTasksPollingService;
    let jsWorkflowTasksPollingService: any = null;
    let timerService: jasmine.SpyObj<any>;
    let timer: jasmine.SpyObj<any>;
    let inboxRESTService: jasmine.SpyObj<IRestService<WorkflowTaskList>>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;

    const EVENTS = {
        LOGOUT: 'LOGOUT',
        AUTHORIZATION_SUCCESS: 'AUTHORIZATION_SUCCESS'
    };

    const initialTasks = getListOfTasks(0);
    const initialPagination = {
        totalCount: 10
    };

    beforeEach(() => {
        $q = promiseHelper.$q();
        timerService = jasmine.createSpyObj('timerService', ['createTimer']);
        const restServiceFactory = jasmine.createSpyObj<IRestServiceFactory>('restServiceFactory', [
            'get'
        ]);
        timer = jasmine.createSpyObj('Timer', ['start', 'restart', 'stop', 'isActive']);
        inboxRESTService = jasmine.createSpyObj('inboxRESTService', ['get']);
        inboxRESTService.get.and.returnValue(
            $q.when({
                tasks: initialTasks,
                pagination: initialPagination
            })
        );
        timerService.createTimer.and.returnValue(timer);
        restServiceFactory.get.and.returnValue(inboxRESTService);
        crossFrameEventService = jasmine.createSpyObj<any>('crossFrameEventService', ['subscribe']);

        workflowTasksPollingService = new WorkflowTasksPollingService(
            lo,
            timerService,
            EVENTS,
            restServiceFactory,
            crossFrameEventService
        );

        jsWorkflowTasksPollingService = workflowTasksPollingService as any;
    });

    it('Should initialize the polling', () => {
        // THEN
        expect(timerService.createTimer).toHaveBeenCalled();
    });

    it("Should restart polling if it's not active", () => {
        // GIVEN
        timer.isActive.and.returnValue(false);
        expect(timer.restart.calls.count()).toBe(1);

        // WHEN
        workflowTasksPollingService.startPolling();

        // THEN
        expect(timer.restart.calls.count()).toBe(2);
    });

    it("Should not restart polling if it's active", () => {
        // GIVEN
        timer.isActive.and.returnValue(true);
        expect(timer.restart.calls.count()).toBe(1);

        // WHEN
        workflowTasksPollingService.startPolling();

        // THEN
        expect(timer.restart.calls.count()).toBe(1);
    });

    it("Should stop polling if it's active", () => {
        // GIVEN
        timer.isActive.and.returnValue(true);

        // WHEN
        workflowTasksPollingService.stopPolling();

        // THEN
        expect(timer.stop).toHaveBeenCalled();
    });

    it("Should not stop polling if it's not active", () => {
        // GIVEN
        timer.isActive.and.returnValue(false);

        // WHEN
        workflowTasksPollingService.stopPolling();

        // THEN
        expect(timer.stop).not.toHaveBeenCalled();
    });

    it('Should add a subscriber to a list of subscribers', () => {
        // GIVEN
        const fn = (): any => {
            return null;
        };

        // WHEN
        workflowTasksPollingService.addSubscriber(fn, true);

        // THEN
        expect(jsWorkflowTasksPollingService.subscribers).toEqual([
            {
                subscriber: fn,
                callOnInit: true
            }
        ]);
    });

    it('Should unsubscribe a subscriber', () => {
        // GIVEN
        const subscriber1 = (): any => {
            return null;
        };

        const subscriber2 = (): any => {
            return null;
        };

        const subscriber3 = (): any => {
            return null;
        };

        // WHEN
        workflowTasksPollingService.addSubscriber(subscriber1, true);
        const unsubscribe2 = workflowTasksPollingService.addSubscriber(subscriber2, true);
        workflowTasksPollingService.addSubscriber(subscriber3, true);
        expect(jsWorkflowTasksPollingService.subscribers).toEqual([
            {
                subscriber: subscriber1,
                callOnInit: true
            },
            {
                subscriber: subscriber2,
                callOnInit: true
            },
            {
                subscriber: subscriber3,
                callOnInit: true
            }
        ]);

        // THEN
        unsubscribe2();
        expect(jsWorkflowTasksPollingService.subscribers).toEqual([
            {
                subscriber: subscriber1,
                callOnInit: true
            },
            {
                subscriber: subscriber3,
                callOnInit: true
            }
        ]);
    });

    it('Should fetch the whole list of new tasks if retrieved first time and call subscribers that only need to be called on init', () => {
        // GIVEN
        const subscriber1Spy = jasmine.createSpy();
        const subscriber2Spy = jasmine.createSpy();

        const tasks = getListOfTasks(2);
        const pagination = {
            totalCount: 10
        };

        inboxRESTService.get.and.returnValue(
            $q.when({
                tasks,
                pagination
            })
        );

        // WHEN
        workflowTasksPollingService.addSubscriber(subscriber1Spy, true);
        workflowTasksPollingService.addSubscriber(subscriber2Spy, false);
        jsWorkflowTasksPollingService._fetchInboxTasks(true);

        // THEN
        expect(subscriber1Spy).toHaveBeenCalledWith(tasks, pagination);
        expect(subscriber2Spy).not.toHaveBeenCalled();
    });

    it('Should fetch the whole list of new tasks and call all subscribers when called with isInit false', () => {
        // GIVEN
        const subscriber1Spy = jasmine.createSpy();
        const subscriber2Spy = jasmine.createSpy();

        const tasks = getListOfTasks(2);
        const pagination = {
            totalCount: 10
        };

        inboxRESTService.get.and.returnValue(
            $q.when({
                tasks,
                pagination
            })
        );

        // WHEN
        workflowTasksPollingService.addSubscriber(subscriber1Spy, true);
        workflowTasksPollingService.addSubscriber(subscriber2Spy, false);
        jsWorkflowTasksPollingService._fetchInboxTasks(false);

        // THEN
        expect(subscriber1Spy).toHaveBeenCalledWith(tasks, pagination);
        expect(subscriber2Spy).toHaveBeenCalledWith(tasks, pagination);
    });

    it('Should retrive only new tasks and call subscriber for them', () => {
        // GIVEN
        const subscriberSpy = jasmine.createSpy();
        workflowTasksPollingService.addSubscriber(subscriberSpy, true);

        // first call with 10 tasks (index from 1 to 10)
        const firstTasks = getListOfTasks(10, 1);
        const pagination = {
            totalCount: 10
        };
        inboxRESTService.get.and.returnValue(
            $q.when({
                tasks: firstTasks,
                pagination
            })
        );
        jsWorkflowTasksPollingService._fetchInboxTasks();
        expect(subscriberSpy).toHaveBeenCalledWith(firstTasks, pagination);

        // WHEN
        // second call with 2 more new tasks (index from 3 to 12)
        const secondTasks = getListOfTasks(10, 3);
        inboxRESTService.get.and.returnValue(
            $q.when({
                tasks: secondTasks,
                pagination
            })
        );
        jsWorkflowTasksPollingService._fetchInboxTasks();

        // THEN
        // calls subscriberSpy only with new tasks with index starting 11 to 12
        const resultTasks = getListOfTasks(2, 11);
        expect(subscriberSpy).toHaveBeenCalledWith(resultTasks, pagination);
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
