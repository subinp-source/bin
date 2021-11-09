/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { HeartBeatAlertComponent } from 'smarteditcontainer/components/heartBeat/HeartBeatAlertComponent';
import { PerspectiveService } from 'smarteditcontainer/services/perspectives/PerspectiveServiceOuter';
import { AlertRef } from '@fundamental-ngx/core';
import {
    CrossFrameEventService,
    EVENT_STRICT_PREVIEW_MODE_REQUESTED,
    NONE_PERSPECTIVE
} from 'smarteditcommons';

describe('HeartbeatAlert', () => {
    let heartBeatAlertComponent: HeartBeatAlertComponent;
    let perspectiveService: jasmine.SpyObj<PerspectiveService>;
    let alertRef: jasmine.SpyObj<AlertRef>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;

    beforeEach(() => {
        perspectiveService = jasmine.createSpyObj('perspectiveService', ['switchTo']);
        alertRef = jasmine.createSpyObj('alertRef', ['dismiss']);
        crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['publish']);

        heartBeatAlertComponent = new HeartBeatAlertComponent(
            alertRef,
            perspectiveService,
            crossFrameEventService
        );
    });

    it('switch to preview mode and publish the event to disable the mode selector', () => {
        // WHEN
        heartBeatAlertComponent.switchToPreviewMode();

        // THEN
        expect(alertRef.dismiss).toHaveBeenCalled();
        expect(perspectiveService.switchTo).toHaveBeenCalledWith(NONE_PERSPECTIVE);
        expect(crossFrameEventService.publish).toHaveBeenCalledWith(
            EVENT_STRICT_PREVIEW_MODE_REQUESTED,
            true
        );
    });
});
