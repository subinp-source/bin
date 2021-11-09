/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject, Injectable } from '@angular/core';
import { DOCUMENT } from '@angular/common';

import {
    GatewayProxied,
    IBound,
    INotificationMouseLeaveDetectionService,
    SeDowngradeService,
    WindowUtils
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

    constructor(@Inject(DOCUMENT) private document: Document, private windowUtils: WindowUtils) {
        super();
        this.notificationPanelBounds = null;
        this.mouseLeaveCallback = null;
        /*
         * We need to bind the function in order for it to execute within the service's
         * scope and store it to be able to un-register the listener.
         */
        this._onMouseMove = this._onMouseMove.bind(this);
    }

    public startDetection(
        outerBounds: IBound,
        innerBounds: IBound,
        callback: () => any
    ): Promise<void> {
        this.validateBounds(outerBounds);

        if (!callback) {
            throw new Error('Callback function is required');
        }

        this.notificationPanelBounds = outerBounds;
        this.mouseLeaveCallback = callback;

        this.document.addEventListener('mousemove', this._onMouseMove);

        if (innerBounds) {
            this.validateBounds(innerBounds);

            this._remoteStartDetection(innerBounds);
        }

        return Promise.resolve();
    }

    public stopDetection(): Promise<void> {
        this.document.removeEventListener('mousemove', this._onMouseMove);

        this.notificationPanelBounds = null;
        this.mouseLeaveCallback = null;

        if (this.windowUtils.getGatewayTargetFrame()) {
            this._remoteStopDetection();
        }

        return Promise.resolve();
    }

    protected _callCallback(): Promise<void> {
        this._getCallback().then((callback: () => void) => {
            if (callback) {
                callback();
            }
        });

        return Promise.resolve();
    }

    protected _getBounds(): Promise<IBound> {
        return Promise.resolve(this.notificationPanelBounds);
    }

    protected _getCallback(): Promise<() => void> {
        return Promise.resolve(this.mouseLeaveCallback);
    }

    private validateBounds(bounds: IBound): void {
        if (!bounds) {
            throw new Error('Bounds are required for mouse leave detection');
        }

        if (!bounds.hasOwnProperty('x')) {
            throw new Error('Bounds must contain the x coordinate');
        }

        if (!bounds.hasOwnProperty('y')) {
            throw new Error('Bounds must contain the y coordinate');
        }

        if (!bounds.hasOwnProperty('width')) {
            throw new Error('Bounds must contain the width dimension');
        }

        if (!bounds.hasOwnProperty('height')) {
            throw new Error('Bounds must contain the height dimension');
        }
    }
}
