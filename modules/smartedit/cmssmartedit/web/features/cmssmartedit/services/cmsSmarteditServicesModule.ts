/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { diNameUtils, SeModule } from 'smarteditcommons';
import { HiddenComponentMenuService } from './HiddenComponentMenuService';
import { SlotContainerService } from './slotContainerService';
import { SyncPollingService } from './SyncPollingServiceInner';
import {
    AssetsService,
    AttributePermissionsRestService,
    CmsConstantsModule,
    ComponentService,
    CMSModesService,
    CMSTimeService,
    SynchronizationService,
    TypePermissionsRestService
} from 'cmscommons';
import { PageContentSlotsComponentsRestService } from 'cmssmartedit/dao/PageContentSlotsComponentsRestServiceInner';
import { EditorModalService } from './EditorModalServiceInner';
import { ComponentSharedService } from './ComponentSharedServiceInner';
import { PageService } from './pages';
import { ContextAwareEditableItemService } from './contextAwareEditableItem/ContextAwareEditableItemServiceInner';
import {
    cmsitemsUri,
    CmsitemsRestService
} from 'cmscommons/dao/cmswebservices/sites/CmsitemsRestService';

/**
 * @ngdoc overview
 * @name cmsSmarteditServicesModule
 *
 * @description
 * Module containing all the services shared within the CmsSmartEdit application.
 */
@SeModule({
    imports: ['yLoDashModule', 'smarteditServicesModule', CmsConstantsModule],
    providers: [
        diNameUtils.makeValueProvider({ cmsitemsUri }),
        CmsitemsRestService,
        EditorModalService,
        SyncPollingService,
        SlotContainerService,
        ComponentSharedService,
        HiddenComponentMenuService,
        AssetsService,
        CMSModesService,
        CMSTimeService,
        PageContentSlotsComponentsRestService,
        AttributePermissionsRestService,
        TypePermissionsRestService,
        PageService,
        ContextAwareEditableItemService,
        ComponentService,
        SynchronizationService
    ]
})
export class CmsSmarteditServicesModule {}
