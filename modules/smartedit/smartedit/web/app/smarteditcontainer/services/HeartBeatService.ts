/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { TranslateService } from '@ngx-translate/core';
import { Inject } from '@angular/core';
import { HeartBeatAlertComponent } from '../components/heartBeat/HeartBeatAlertComponent';

import {
    Alert,
    AngularJSLazyDependenciesService,
    CrossFrameEventService,
    EVENT_STRICT_PREVIEW_MODE_REQUESTED,
    EVENTS,
    GatewayFactory,
    HEART_BEAT_TIMEOUT_THRESHOLD_MS_TOKEN,
    IConfiguration,
    ISharedDataService,
    MessageGateway,
    SeDowngradeService,
    STORE_FRONT_CONTEXT,
    WindowUtils
} from 'smarteditcommons';
import { AlertFactory } from './alerts';

/* @internal */
@SeDowngradeService()
export class HeartBeatService {
    static readonly HEART_BEAT_GATEWAY_ID = 'heartBeatGateway';
    static readonly HEART_BEAT_MSG_ID = 'heartBeat';

    private reconnectingAlert: Alert;
    private reconnectedAlert: Alert;

    private reconnectingInProgress = false;
    private cancellableTimeoutTimer: number;

    constructor(
        @Inject(HEART_BEAT_TIMEOUT_THRESHOLD_MS_TOKEN)
        private HEART_BEAT_TIMEOUT_THRESHOLD_MS: number,
        translate: TranslateService,
        angularJSDependenciesService: AngularJSLazyDependenciesService,
        private windowUtils: WindowUtils,
        alertFactory: AlertFactory,
        private crossFrameEventService: CrossFrameEventService,
        gatewayFactory: GatewayFactory,
        private sharedDataService: ISharedDataService
    ) {
        this.reconnectingAlert = alertFactory.createInfo({
            component: HeartBeatAlertComponent,
            duration: -1,
            dismissible: false
        });

        this.reconnectedAlert = alertFactory.createInfo({
            message: translate.instant('se.heartbeat.reconnection'),
            timeout: 3000
        });

        const heartBeatGateway: MessageGateway = gatewayFactory.createGateway(
            HeartBeatService.HEART_BEAT_GATEWAY_ID
        );
        heartBeatGateway.subscribe(HeartBeatService.HEART_BEAT_MSG_ID, () => {
            return this.heartBeatReceived();
        });

        this.crossFrameEventService.subscribe(EVENTS.PAGE_CHANGE, () => {
            this.resetAndStop();
            // assume every page is smarteditable ¯\_(ツ)_/¯
            return this.heartBeatReceived();
        });

        angularJSDependenciesService.$rootScope().$on('$routeChangeSuccess', () => {
            if (angularJSDependenciesService.$location().path() === STORE_FRONT_CONTEXT) {
                return this.heartBeatReceived();
            }
            return Promise.resolve();
        });

        angularJSDependenciesService.$rootScope().$on('$routeChangeStart', () => {
            this.resetAndStop();
        });
    }

    /**
     * @internal
     * Hide all alerts and cancel all pending actions and timers.
     */
    public resetAndStop = () => {
        this.reconnectingInProgress = false;
        if (this.cancellableTimeoutTimer) {
            clearTimeout(this.cancellableTimeoutTimer);
            this.cancellableTimeoutTimer = null;
        }
        this.reconnectingAlert.hide();
        this.reconnectedAlert.hide();
    };

    /**
     * @internal
     * Heartbeat received from iframe, show reconnected if connection was previously
     * lost, and restart the timer to wait for iframe heartbeat
     */
    private heartBeatReceived(): Promise<void> {
        const reconnecting = this.reconnectingInProgress;
        this.resetAndStop();
        if (reconnecting) {
            this.reconnectedAlert.show();
            this.reconnectingInProgress = false;

            // Publish an event to enable the perspective selector in case if it is disabled
            this.crossFrameEventService.publish(EVENT_STRICT_PREVIEW_MODE_REQUESTED, false);
        }

        return this.sharedDataService
            .get('configuration')
            .then(({ heartBeatTimeoutThreshold }: IConfiguration) => {
                if (!heartBeatTimeoutThreshold) {
                    heartBeatTimeoutThreshold = this.HEART_BEAT_TIMEOUT_THRESHOLD_MS;
                }

                this.cancellableTimeoutTimer = this.windowUtils.runTimeoutOutsideAngular(
                    this.connectionLost,
                    +heartBeatTimeoutThreshold
                );
            });
    }

    /**
     * Connection to iframe has been lost, show reconnected alert to user
     */
    private connectionLost = () => {
        this.resetAndStop();
        this.reconnectingAlert.show();
        this.reconnectingInProgress = true;
    };
}
