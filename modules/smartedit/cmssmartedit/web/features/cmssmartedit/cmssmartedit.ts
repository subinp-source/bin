/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { BrowserModule } from '@angular/platform-browser';
import { UpgradeModule } from '@angular/upgrade/static';
import { NgModule } from '@angular/core';
import { SyncIndicatorDecorator } from 'cmssmartedit/components/synchronize/slots/SyncIndicatorDecorator';
import { CmsCommonsModule } from 'cmscommons';
import { diBridgeUtils, ICatalogService, SeEntryModule } from 'smarteditcommons';
import './components/contextualSlotDropdown.scss';

@SeEntryModule('cmssmartedit')
@NgModule({
    imports: [CmsCommonsModule, BrowserModule, UpgradeModule],
    declarations: [SyncIndicatorDecorator],
    entryComponents: [SyncIndicatorDecorator],
    providers: [
        diBridgeUtils.upgradeProvider('$q'),
        diBridgeUtils.upgradeProvider('catalogService', ICatalogService),
        diBridgeUtils.upgradeProvider('slotSynchronizationService'),
        diBridgeUtils.upgradeProvider('pageInfoService'),
        diBridgeUtils.upgradeProvider('SYNCHRONIZATION_STATUSES'),
        diBridgeUtils.upgradeProvider('SYNCHRONIZATION_POLLING')
    ]
})
export class CmssmarteditModule {}
