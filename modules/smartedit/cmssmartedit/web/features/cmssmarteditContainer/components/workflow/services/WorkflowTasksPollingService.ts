/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';

import {
    CrossFrameEventService,
    IRestService,
    IRestServiceFactory,
    Pagination,
    SeInjectable,
    TimerService
} from 'smarteditcommons';

import { WorkflowTaskList } from '../dtos/WorkflowTaskList';
import { WorkflowTask } from '../dtos/WorkflowTask';

type WorkflowTaskSubscriber = (tasks: WorkflowTask[], pagination: Pagination) => any;

interface Subscriber {
    subscriber: WorkflowTaskSubscriber;
    callOnInit: boolean;
}

export const INBOX_POLLING_PARAMS = {
    INBOX_POLLING_TIMEOUT: 20000,
    INBOX_POLLING_PAGESIZE: 10,
    INBOX_POLLING_CURRENTPAGE: 0
};

/**
 * @ngdoc service
 * @name cmsSmarteditServicesModule.service:WorkflowTasksPollingService
 *
 * @description
 * This service is used to retrieve inbox tasks.
 */
@SeInjectable()
export class WorkflowTasksPollingService {
    private resourceInboxURI: string;
    private subscribers: Subscriber[];
    private syncPollingTimer: any;
    private savedHashedTasks: string[];

    private inboxRESTService: IRestService<WorkflowTaskList>;

    constructor(
        private lodash: lo.LoDashStatic,
        private timerService: TimerService,
        private EVENTS: any,
        private restServiceFactory: IRestServiceFactory,
        private crossFrameEventService: CrossFrameEventService
    ) {
        this.resourceInboxURI = '/cmssmarteditwebservices/v1/inbox/workflowtasks';
        this.subscribers = [];
        this.syncPollingTimer = null;
        this.savedHashedTasks = [];

        this.inboxRESTService = this.restServiceFactory.get(this.resourceInboxURI);
        this.crossFrameEventService.subscribe(
            this.EVENTS.AUTHORIZATION_SUCCESS,
            () => this._initPolling()
        );
        this.crossFrameEventService.subscribe(this.EVENTS.LOGOUT, this.stopPolling.bind(this));
        this.crossFrameEventService.subscribe(
            this.EVENTS.REAUTH_STARTED,
            this.stopPolling.bind(this)
        );
        this._initPolling();
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowTasksPollingService#setPageSize
     * @methodOf cmsSmarteditServicesModule.service:WorkflowTasksPollingService
     *
     * @description
     * Sets a new value for a page size.
     */
    setPageSize(newPageSize: number): void {
        INBOX_POLLING_PARAMS.INBOX_POLLING_PAGESIZE = newPageSize;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowTasksPollingService#setCurrentPage
     * @methodOf cmsSmarteditServicesModule.service:WorkflowTasksPollingService
     *
     * @description
     * Sets a new value for a current page.
     */
    setCurrentPage(newCurrentPage: number): void {
        INBOX_POLLING_PARAMS.INBOX_POLLING_CURRENTPAGE = newCurrentPage;
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowTasksPollingService#stopPolling
     * @methodOf cmsSmarteditServicesModule.service:WorkflowTasksPollingService
     *
     * @description
     * Stops a polling timer.
     */
    stopPolling() {
        if (this.syncPollingTimer.isActive()) {
            this.syncPollingTimer.stop();
        }
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowTasksPollingService#startPolling
     * @methodOf cmsSmarteditServicesModule.service:WorkflowTasksPollingService
     *
     * @description
     * Starts a polling timer.
     */
    startPolling() {
        if (!this.syncPollingTimer.isActive()) {
            this.syncPollingTimer.restart(INBOX_POLLING_PARAMS.INBOX_POLLING_TIMEOUT);
        }
    }

    /**
     * @ngdoc method
     * @name cmsSmarteditServicesModule.service:WorkflowTasksPollingService#addSubscriber
     * @methodOf cmsSmarteditServicesModule.service:WorkflowTasksPollingService
     *
     * @description
     * Adds a new subscriber to the polling service. The subscriber is called with a list of new tasks and a pagination information.
     *
     * @param {WorkflowTaskSubscriber} subscriber the subscriber.
     * @param {boolean} callOnInit Default is true, when set to false, will not call the subscriber on initialization of the polling.
     *
     * @returns {() => void} the method that can be used to unsubscribe.
     */
    addSubscriber(subscriber: WorkflowTaskSubscriber, callOnInit: boolean) {
        let unsubscribeFn: () => void;
        this.subscribers.push({
            subscriber,
            callOnInit
        });

        unsubscribeFn = () => {
            this._unsubscribe(subscriber);
        };

        return unsubscribeFn;
    }

    /**
     * Initializes a polling process.
     */
    private _initPolling() {
        this.syncPollingTimer = this.timerService.createTimer(
            this._fetchInboxTasks.bind(this, false),
            INBOX_POLLING_PARAMS.INBOX_POLLING_TIMEOUT
        );
        this._fetchInboxTasks(true);
        this.startPolling();
    }

    /**
     * Unsubscribes a subscriber.
     * @param subscriber the subscriber that will be unsubscribed.
     */
    private _unsubscribe(subscriber: WorkflowTaskSubscriber) {
        const index = this.subscribers.findIndex((subs: Subscriber) => {
            return subs.subscriber === subscriber;
        });

        if (index > -1) {
            this.subscribers.splice(index, 1);
        }
    }

    /**
     * Returns tasks that have not been yet delivered to subscribers.
     * @param {WorkflowTask[]} tasks the list of retrieved tasks from the backend.
     * @return {WorkflowTask[]} the list of new tasks.
     */
    private _getNewTasks(tasks: WorkflowTask[]): WorkflowTask[] {
        const newTasks = tasks.filter((task) => {
            const hashedTask = this._encodeTask(task);
            return this.savedHashedTasks.indexOf(hashedTask) === -1;
        });
        return newTasks;
    }

    /**
     * New tasks are added at the end of the array. If the array is bigger than INBOX_POLLING_PAGESIZE
     * it shrinks from the beginning to the INBOX_POLLING_PAGESIZE size.
     * @param {WorkflowTask[]} newTasks the list of new tasks that will be stored in cache. Each task stored as encoded base-64 string.
     */
    private _saveNewHashedTasks(newTasks: WorkflowTask[]) {
        newTasks.forEach((task) => {
            this.savedHashedTasks.push(this._encodeTask(task));
        });
        const sizeDiff = this.savedHashedTasks.length - INBOX_POLLING_PARAMS.INBOX_POLLING_PAGESIZE;
        if (sizeDiff > 0) {
            for (let i = 0; i < sizeDiff; i++) {
                this.savedHashedTasks.shift();
            }
        }
    }

    /**
     * Encodes a task.
     * @param {WorkflowTask} task the task that will be encoded to a base-64 string.
     * @return {String} the encoded string
     */
    private _encodeTask(task: WorkflowTask): string {
        const taskClone: WorkflowTask = this.lodash.cloneDeep(task);
        if (taskClone.action) {
            delete taskClone.action.startedAgoInMillis;
        }
        return btoa(JSON.stringify(taskClone));
    }

    /**
     * Retrieves the list if tasks from the backend in paginated view.
     * It calls each subscriber with a list of new tasks and pagination information.
     */
    private _fetchInboxTasks(isInit: boolean): void {
        this.inboxRESTService
            .get({
                pageSize: INBOX_POLLING_PARAMS.INBOX_POLLING_PAGESIZE,
                currentPage: INBOX_POLLING_PARAMS.INBOX_POLLING_CURRENTPAGE
            })
            .then(
                (response: WorkflowTaskList) => {
                    const newTasks = this._getNewTasks(response.tasks);
                    this._saveNewHashedTasks(newTasks);

                    this.subscribers.forEach((subscriber: Subscriber) => {
                        if (!isInit || (isInit && !!subscriber.callOnInit)) {
                            subscriber.subscriber(newTasks, response.pagination);
                        }
                    });
                },
                () => {
                    this.stopPolling();
                }
            );
    }
}
