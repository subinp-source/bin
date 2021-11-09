/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { diNameUtils, SeModule } from 'smarteditcommons';
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
import { PageContentSlotsComponentsRestService } from 'cmssmarteditcontainer/dao/PageContentSlotsComponentsRestServiceOuter';
import { PageSynchronizationService } from 'cmssmarteditcontainer/dao/PageSynchronizationService';
import { PageVersioningService } from 'cmssmarteditcontainer/services/pageVersioning/PageVersioningService';
import { ManagePageService } from 'cmssmarteditcontainer/services/pages/ManagePageService';
import { PageServicesModule } from './pages';
import { PageRestoredAlertService } from './actionableAlert';
import { WorkflowService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowService';
import { ComponentSharedService } from './components';

import {
    DEFAULT_CMS_EVENT_HIDE_REPLACE_PARENT_HOMEPAGE_INFO,
    DEFAULT_CMS_EVENT_SHOW_REPLACE_PARENT_HOMEPAGE_INFO,
    HomepageService
} from 'cmssmarteditcontainer/services/pageDisplayConditions/HomepageService';
import { SyncPollingService } from './SyncPollingServiceOuter';
import { EditorModalService } from './EditorModalServiceOuter';
import { ContextAwareEditableItemService } from './contextAwareEditableItem/ContextAwareEditableItemServiceOuter';
import {
    cmsitemsUri,
    CmsitemsRestService
} from 'cmscommons/dao/cmswebservices/sites/CmsitemsRestService';
import { SeMediaService } from 'cmssmarteditcontainer/components/genericEditor/media/services/SeMediaService';
import { DisplayConditionsFacade } from '../facades/displayConditionsFacade';
import { PageDisplayConditionsService } from './pageDisplayConditions/pageDisplayConditionsService';
import { DisplayConditionsEditorModel } from './pageDisplayConditions/displayConditionsEditorModel';
import { PageRestrictionsCriteriaService } from './pageRestrictions/PageRestrictionCriteriaService';
import { PageRestrictionsService } from './pageRestrictions/PageRestrictionsService';
import { PageRestrictionsFacade } from '../facades/PageRestrictionsFacade';

/**
 * @ngdoc overview
 * @name cmsSmarteditServicesModule
 *
 * @description
 * Module containing all the services shared within the CmsSmartEdit application.
 */
@SeModule({
    imports: ['smarteditServicesModule', PageServicesModule, CmsConstantsModule],
    providers: [
        diNameUtils.makeValueProvider({ cmsitemsUri }),
        PageRestrictionsCriteriaService,
        PageRestrictionsService,
        PageRestrictionsFacade,
        CmsitemsRestService,
        SeMediaService,
        ContextAwareEditableItemService,
        EditorModalService,
        SyncPollingService,
        AssetsService,
        CMSModesService,
        CMSTimeService,
        HomepageService,
        AttributePermissionsRestService,
        TypePermissionsRestService,
        ManagePageService,
        PageVersioningService,
        PageContentSlotsComponentsRestService,
        PageSynchronizationService,
        PageRestoredAlertService,
        WorkflowService,
        ComponentSharedService,
        DisplayConditionsFacade,
        PageDisplayConditionsService,
        DisplayConditionsEditorModel,
        ComponentService,
        SynchronizationService,
        diNameUtils.makeValueProvider({ DEFAULT_CMS_EVENT_HIDE_REPLACE_PARENT_HOMEPAGE_INFO }),
        diNameUtils.makeValueProvider({ DEFAULT_CMS_EVENT_SHOW_REPLACE_PARENT_HOMEPAGE_INFO })
    ]
})
export class CmsSmarteditServicesModule {}
