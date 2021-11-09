/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PaginationModule } from '../pagination/PaginationModule';
import { DataTableModule } from '../dataTable/DataTableModule';
import { SeTranslationModule } from '../../modules';
import { DynamicPagedListComponent } from './DynamicPagedListComponent';
import { SpinnerModule } from '../spinner';

@NgModule({
    imports: [
        CommonModule,
        PaginationModule,
        DataTableModule,
        SeTranslationModule.forChild(),
        SpinnerModule
    ],
    declarations: [DynamicPagedListComponent],
    entryComponents: [DynamicPagedListComponent],
    exports: [DynamicPagedListComponent]
})
export class DynamicPagedListModule {}
