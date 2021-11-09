/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import {
    moduleUtils,
    AngularJSBootstrapIndicatorService,
    CrossFrameEventService,
    IToolbarServiceFactory,
    SeEntryModule,
    ToolbarItemType,
    ToolbarSection
} from 'smarteditcommons';

@SeEntryModule('OuterClickThroughOverlay')
@NgModule({
    providers: [
        moduleUtils.initialize(
            (
                crossFrameEventService: CrossFrameEventService,
                toolbarServiceFactory: IToolbarServiceFactory,
                angularJSBootstrapIndicatorServic: AngularJSBootstrapIndicatorService
            ) => {
                angularJSBootstrapIndicatorServic.onSmarteditContainerReady().subscribe(() => {
                    const tbs = toolbarServiceFactory.getToolbarService(
                        'smartEditPerspectiveToolbar'
                    );

                    tbs.addItems([
                        {
                            key: 'se.CLICK_THROUGH_OVERLAY',
                            type: ToolbarItemType.ACTION,
                            nameI18nKey: 'CLICK_THROUGH_OVERLAY',
                            descriptionI18nKey: 'CLICK_THROUGH_OVERLAY',
                            section: ToolbarSection.right,
                            iconClassName:
                                'hyicon hyicon-dragdrop se-toolbar-menu-ddlb--button__icon',
                            callback() {
                                crossFrameEventService.publish('CLICK_THROUGH_OVERLAY');
                            }
                        }
                    ]);
                    tbs.addItems([
                        {
                            key: 'se.PREVENT_OVERLAY_CLICKTHROUGH',
                            type: ToolbarItemType.ACTION,
                            nameI18nKey: 'PREVENT_OVERLAY_CLICKTHROUGH',
                            descriptionI18nKey: 'PREVENT_OVERLAY_CLICKTHROUGH',
                            section: ToolbarSection.right,
                            iconClassName: 'hyicon hyicon-list se-toolbar-menu-ddlb--button__icon',
                            callback() {
                                crossFrameEventService.publish('PREVENT_OVERLAY_CLICKTHROUGH');
                            }
                        }
                    ]);
                });
            },
            [CrossFrameEventService, IToolbarServiceFactory, AngularJSBootstrapIndicatorService]
        )
    ]
})
export class OuterClickThroughOverlay {}
