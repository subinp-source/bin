/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import {
    CollapsibleContainerModule,
    CompileHtmlModule,
    ICatalogDetailsService,
    L10nPipeModule
} from 'smarteditcommons';
import { CatalogDetailsService } from './services/CatalogDetailsService';
import { CatalogDetailsComponent } from './components/CatalogDetailsComponent';
import { CatalogVersionDetailsComponent } from './components/CatalogVersionDetailsComponent';
import { CatalogVersionsThumbnailCarouselComponent } from './components/CatalogVersionsThumbnailCarouselComponent';
import { HomePageLinkComponent } from './components/HomePageLinkComponent';
import { CatalogVersionItemRendererComponent } from './components/CatalogVersionItemRendererComponent';

/**
 * @ngdoc overview
 * @name CatalogDetailsModule
 * @description
 * This module contains the {@link CatalogDetailsModule.component:catalogVersionDetails} component.
 */

/**
 * @ngdoc object
 * @name CatalogDetailsModule.object:CATALOG_DETAILS_COLUMNS
 *
 * @description
 * Injectable angular constant<br/>
 * This object provides an enumeration with values for each of the possible places to add items to
 * extend the {@link CatalogDetailsModule.component:catalogVersionDetails} component. Currently,
 * the available options are CATALOG_DETAILS_COLUMNS.LEFT and CATALOG_DETAILS_COLUMNS.RIGHT.
 *
 */

@NgModule({
    imports: [
        CommonModule,
        CollapsibleContainerModule,
        CompileHtmlModule,
        L10nPipeModule,
        TranslateModule.forChild()
    ],
    providers: [{ provide: ICatalogDetailsService, useClass: CatalogDetailsService }],
    declarations: [
        HomePageLinkComponent,
        CatalogDetailsComponent,
        CatalogVersionDetailsComponent,
        CatalogVersionsThumbnailCarouselComponent,
        CatalogVersionItemRendererComponent
    ],
    entryComponents: [
        CatalogVersionsThumbnailCarouselComponent,
        CatalogVersionDetailsComponent,
        HomePageLinkComponent,
        CatalogDetailsComponent,
        CatalogVersionItemRendererComponent
    ]
})
export class CatalogDetailsModule {}
