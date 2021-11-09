/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule, NgZone } from '@angular/core';
import {
    moduleUtils,
    AngularJSBootstrapIndicatorService,
    CrossFrameEventService,
    SeEntryModule,
    YJQUERY_TOKEN
} from 'smarteditcommons';

@SeEntryModule('InnerClickThroughOverlay')
@NgModule({
    providers: [
        moduleUtils.initialize(
            (
                crossFrameEventService: CrossFrameEventService,
                zone: NgZone,
                yjQuery: JQueryStatic,
                angularJSBootstrapIndicatorService: AngularJSBootstrapIndicatorService
            ) => {
                angularJSBootstrapIndicatorService.onSmarteditReady().subscribe(() => {
                    crossFrameEventService.subscribe('PREVENT_OVERLAY_CLICKTHROUGH', function() {
                        yjQuery('#smarteditoverlay').css('pointer-events', 'auto');
                        yjQuery('body').removeClass('clickthroughflash');
                    });

                    crossFrameEventService.subscribe('CLICK_THROUGH_OVERLAY', function() {
                        yjQuery('#smarteditoverlay').css('pointer-events', 'none');
                        yjQuery('body').removeClass('clickthroughflash');

                        zone.runOutsideAngular(() => {
                            setTimeout(() => {
                                zone.run(() => {
                                    yjQuery('body').addClass('clickthroughflash');
                                });
                            }, 0);
                        });
                    });
                });
            },
            [CrossFrameEventService, NgZone, YJQUERY_TOKEN, AngularJSBootstrapIndicatorService]
        )
    ]
})
export class InnerClickThroughOverlay {}
