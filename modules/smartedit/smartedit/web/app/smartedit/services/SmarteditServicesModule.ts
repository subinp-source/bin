/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import {
    moduleUtils,
    DragAndDropServiceModule,
    IAnnouncementService,
    ICatalogVersionPermissionService,
    IDragAndDropCrossOrigin,
    IFeatureService,
    INotificationMouseLeaveDetectionService,
    INotificationService,
    IPageInfoService,
    IPermissionService,
    IPerspectiveService,
    IPositionRegistry,
    IPreviewService,
    IRenderService,
    IResizeListener,
    IRestServiceFactory,
    ISmartEditContractChangeListener,
    IWaitDialogService,
    PolyfillService,
    PriorityService,
    SmarteditCommonsModule
} from 'smarteditcommons';

import { DelegateRestService } from './DelegateRestServiceInner';
import { RestServiceFactory } from './RestServiceFactory';
import { ResizeListener } from './ResizeListener';
import { ComponentHandlerService } from './ComponentHandlerService';
import { SeNamespaceService } from './SeNamespaceService';
import { PermissionService } from './PermissionServiceInner';
import { PageInfoService } from './PageInfoServiceInner';
import { NotificationMouseLeaveDetectionService } from './NotificationMouseLeaveDetectionServiceInner';
import { AnnouncementService } from './AnnouncementServiceInner';
import { WaitDialogService } from './WaitDialogServiceInner';
import { PreviewService } from './PreviewServiceInner';
import { ContextualMenuService } from './ContextualMenuService';
import { FeatureService } from './FeatureServiceInner';
import { PerspectiveService } from './PerspectiveServiceInner';
import { PositionRegistry } from './PositionRegistry';
import { SmartEditContractChangeListener } from './SmartEditContractChangeListener';
import { CatalogVersionPermissionService } from './catalogVersionPermissionServiceInner';
import { DragAndDropCrossOrigin } from './DragAndDropCrossOriginInner';
import { NotificationService } from './NotificationServiceInner';
import { RenderService } from './RenderServiceInner';
import { ResizeComponentService } from './resizeComponentService';

@NgModule({
    imports: [DragAndDropServiceModule, SmarteditCommonsModule],
    providers: [
        { provide: IPermissionService, useClass: PermissionService },
        DelegateRestService,
        RestServiceFactory,
        ResizeListener,
        SeNamespaceService,
        ContextualMenuService,
        ComponentHandlerService,
        ResizeComponentService,
        PriorityService,
        {
            provide: IRestServiceFactory,
            useClass: RestServiceFactory
        },
        {
            provide: IRenderService,
            useClass: RenderService
        },
        {
            provide: ICatalogVersionPermissionService,
            useClass: CatalogVersionPermissionService
        },
        {
            provide: ISmartEditContractChangeListener,
            useClass: SmartEditContractChangeListener
        },
        { provide: IPageInfoService, useClass: PageInfoService },
        {
            provide: IResizeListener,
            useClass: ResizeListener
        },
        {
            provide: IPositionRegistry,
            useClass: PositionRegistry
        },
        {
            provide: IAnnouncementService,
            useClass: AnnouncementService
        },
        {
            provide: IPerspectiveService,
            useClass: PerspectiveService
        },
        {
            provide: IFeatureService,
            useClass: FeatureService
        },
        {
            provide: INotificationMouseLeaveDetectionService,
            useClass: NotificationMouseLeaveDetectionService
        },
        {
            provide: IWaitDialogService,
            useClass: WaitDialogService
        },
        {
            provide: IPreviewService,
            useClass: PreviewService
        },
        {
            provide: IDragAndDropCrossOrigin,
            useClass: DragAndDropCrossOrigin
        },
        PolyfillService,
        {
            provide: INotificationService,
            useClass: NotificationService
        },
        moduleUtils.initialize(
            (notificationMouseLeaveDetectionService: NotificationMouseLeaveDetectionService) => {
                //
            },
            [INotificationMouseLeaveDetectionService]
        )
    ]
})
export class SmarteditServicesModule {}
