/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject, Injectable } from '@angular/core';
import { DOCUMENT } from '@angular/common';

import {
    GatewayProxied,
    IBound,
    INotificationMouseLeaveDetectionService,
    SeDowngradeService
} from 'smarteditcommons';

/**
 * @ngdoc service
 * @name smarteditServicesModule.service:NotificationMouseLeaveDetectionService
 * @extends {smarteditServicesModule.interface:INotificationMouseLeaveDetectionService}
 * @description
 * This service makes it possible to track the mouse position to detect when it leaves the notification panel.
 * It is solely meant to be used with the notificationService.
 */
/** @internal */
@SeDowngradeService(INotificationMouseLeaveDetectionService)
@GatewayProxied('stopDetection', '_remoteStartDetection', '_remoteStopDetection', '_callCallback')
@Injectable()
export class NotificationMouseLeaveDetectionService extends INotificationMouseLeaveDetectionService {
    private notificationPanelBounds: IBound;
    private mouseLeaveCallback: () => void;

    constructor(@Inject(DOCUMENT) private document: Document) {
        super();
        this.notificationPanelBounds = null;
        this.mouseLeaveCallback = null;
        /*
         * We need to bind the function in order for it to execute within the service's
         * scope and store it to be able to un-register the listener.
         */
        this._onMouseMove = this._onMouseMove.bind(this);
    }

    protected _remoteStartDetection(innerBounds: IBound): Promise<void> {
        this.notificationPanelBounds = innerBounds;
        this.document.addEventListener('mousemove', this._onMouseMove);
        return Promise.resolve();
    }

    protected _remoteStopDetection(): Promise<void> {
        this.document.removeEventListener('mousemove', this._onMouseMove);
        this.notificationPanelBounds = null;
        return Promise.resolve();
    }

    protected _getBounds(): Promise<IBound> {
        return Promise.resolve(this.notificationPanelBounds);
    }

    protected _getCallback(): Promise<() => void> {
        return Promise.resolve(this.mouseLeaveCallback);
    }
}
