/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DataTableComponent } from './DataTableComponent';
import { DataTableRendererComponent } from './DataTableRenderer';
import { SeTranslationModule } from '../../modules';
import { CompileHtmlModule } from '../../directives';

@NgModule({
    imports: [CommonModule, SeTranslationModule.forChild(), CompileHtmlModule],
    declarations: [DataTableComponent, DataTableRendererComponent],
    entryComponents: [DataTableComponent, DataTableRendererComponent],
    exports: [DataTableComponent, DataTableRendererComponent]
})
export class DataTableModule {}
