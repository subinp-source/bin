/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject } from '@angular/core';
import { AlertRef } from '@fundamental-ngx/core';
import {
    CrossFrameEventService,
    EVENT_SERVICE,
    EVENT_STRICT_PREVIEW_MODE_REQUESTED,
    IPerspectiveService,
    NONE_PERSPECTIVE
} from 'smarteditcommons';

@Component({
    selector: 'se-heartbeat-alert',
    templateUrl: './HeartBeatAlertComponent.html'
})
export class HeartBeatAlertComponent {
    constructor(
        private alertRef: AlertRef,
        private perspectiveService: IPerspectiveService,
        @Inject(EVENT_SERVICE) private crossFrameEventService: CrossFrameEventService
    ) {}

    switchToPreviewMode() {
        this.alertRef.dismiss();
        this.perspectiveService.switchTo(NONE_PERSPECTIVE);
        // Enable the strict preview mode by calling publish() with 'true' as event payload in order to disable the perspective selector
        this.crossFrameEventService.publish(EVENT_STRICT_PREVIEW_MODE_REQUESTED, true);
    }
}
