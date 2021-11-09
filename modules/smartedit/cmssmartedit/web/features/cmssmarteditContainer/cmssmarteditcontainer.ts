/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* forbiddenNameSpaces useClass:false */
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { UpgradeModule } from '@angular/upgrade/static';
import {
    MessageModule,
    ResponseAdapterInterceptor,
    SeEntryModule,
    SeGenericEditorModule,
    SeTranslationModule,
    SharedComponentsModule,
    TooltipModule
} from 'smarteditcommons';
import { CmsCommonsModule, VersionExperienceInterceptor } from 'cmscommons';
import { ProductCategoryService } from './services/ProductCategoryService';
import { GenericEditorModalService } from './services/GenericEditorModalService';
import { GenericEditorModalComponent } from './services/components/GenericEditorModalComponent';
import {
    CatalogVersionRestService,
    PageRestrictionsRestService,
    PageTypesRestrictionTypesRestService
} from './dao';
import { PageTypesRestrictionTypesService } from './services/pageRestrictions/PageTypesRestrictionTypesService';
import { RestrictionTypesRestService } from './dao/RestrictionTypesRestService';
import { RestrictionTypesService } from './services/pageRestrictions/RestrictionTypesService';

@SeEntryModule('cmssmarteditContainer')
@NgModule({
    imports: [
        CmsCommonsModule,
        BrowserModule,
        UpgradeModule,
        SharedComponentsModule,
        SeGenericEditorModule,
        MessageModule,
        TooltipModule,
        SeTranslationModule.forChild()
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: VersionExperienceInterceptor,
            multi: true
        },
        GenericEditorModalService,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: ResponseAdapterInterceptor,
            multi: true
        },
        PageRestrictionsRestService,
        PageTypesRestrictionTypesRestService,
        PageTypesRestrictionTypesService,
        RestrictionTypesRestService,
        RestrictionTypesService,
        ProductCategoryService,
        CatalogVersionRestService
    ],
    declarations: [GenericEditorModalComponent],
    entryComponents: [GenericEditorModalComponent]
})
export class CmssmarteditContainerModule {}
