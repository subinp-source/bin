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
    IPreviewService,
    IRenderService,
    IWaitDialogService,
    RestServiceFactory,
    SmarteditCommonsModule
} from 'smarteditcommons';
import {
    DelegateRestService,
    HeartBeatService,
    PermissionsRegistrationService,
    PreviewDatalanguageDropdownPopulator,
    PreviewDatapreviewCatalogDropdownPopulator,
    ProductService
} from 'smarteditcontainer/services';
import { CatalogVersionPermissionService } from './catalogVersionPermissionServiceOuter';
import { CatalogVersionPermissionRestService } from './CatalogVersionPermissionRestService';
import { PermissionService } from './PermissionServiceOuter';
import { PageInfoService } from './PageInfoServiceOuter';
import { NotificationMouseLeaveDetectionService } from './notifications/NotificationMouseLeaveDetectionServiceOuter';
import { AnnouncementService } from './announcement/AnnouncementServiceOuter';
import { WaitDialogService } from './WaitDialogServiceOuter';
import { PreviewService } from './PreviewServiceOuter';
import { ConfigurationService } from './ConfigurationService';
import { ConfigurationModalService } from './ConfigurationModalService';
import { FeatureService } from './perspectives/FeatureServiceOuter';
import { PerspectiveService } from './perspectives/PerspectiveServiceOuter';
import { DragAndDropCrossOrigin } from './DragAndDropCrossOriginOuter';
import { NotificationService } from './notifications/NotificationServiceOuter';
import { RenderService } from './RenderServiceOuter';

import { ConfigurationExtractorService } from './bootstrap/ConfigurationExtractorService';
import { BootstrapService } from './bootstrap/BootstrapService';

@NgModule({
    imports: [DragAndDropServiceModule, SmarteditCommonsModule],
    providers: [
        HeartBeatService,
        BootstrapService,
        ConfigurationExtractorService,
        DelegateRestService,
        RestServiceFactory,
        ConfigurationService,
        ConfigurationModalService,
        PreviewDatalanguageDropdownPopulator,
        PreviewDatapreviewCatalogDropdownPopulator,
        CatalogVersionPermissionRestService,
        CatalogVersionPermissionService,
        {
            provide: ICatalogVersionPermissionService,
            useClass: CatalogVersionPermissionService
        },
        { provide: IPermissionService, useClass: PermissionService },
        { provide: IPageInfoService, useClass: PageInfoService },
        {
            provide: IRenderService,
            useClass: RenderService
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
        PermissionsRegistrationService,
        {
            provide: INotificationService,
            useClass: NotificationService
        },
        ProductService,
        moduleUtils.initialize(
            (previewService: IPreviewService) => {
                //
            },
            [IPreviewService]
        )
    ]
})
export class SmarteditServicesModule {}
