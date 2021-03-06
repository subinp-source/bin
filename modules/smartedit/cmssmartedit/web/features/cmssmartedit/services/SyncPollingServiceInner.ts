/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    GatewayProxied,
    IUriContext,
    SeInjectable,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';
import { ISyncStatus } from 'cmscommons/dtos/ISyncStatus';
import * as angular from 'angular';

@GatewayProxied(
    'getSyncStatus',
    '_fetchSyncStatus',
    'changePollingSpeed',
    'registerSyncPollingEvents',
    'performSync'
)
@SeInjectable()
export class SyncPollingService {
    constructor(
        private systemEventService: SystemEventService,
        private SYNCHRONIZATION_POLLING: TypedMap<any>
    ) {
        this.registerSyncPollingEvents();
    }

    registerSyncPollingEvents(): void {
        this.systemEventService.subscribe(
            this.SYNCHRONIZATION_POLLING.SPEED_UP,
            this.changePollingSpeed.bind(this)
        );
        this.systemEventService.subscribe(
            this.SYNCHRONIZATION_POLLING.SLOW_DOWN,
            this.changePollingSpeed.bind(this)
        );
    }

    changePollingSpeed(eventId: string, itemId?: string): void {
        'proxyFunction';
        return null;
    }

    _fetchSyncStatus(_pageUUID: string, uriContext: IUriContext): angular.IPromise<ISyncStatus> {
        'proxyFunction';
        return null;
    }

    getSyncStatus(pageUUID: string, uriContext: IUriContext): angular.IPromise<ISyncStatus> {
        'proxyFunction';
        return null;
    }

    performSync(array: any[], uriContext: IUriContext): angular.IPromise<any> {
        'proxyFunction';
        return null;
    }
}
