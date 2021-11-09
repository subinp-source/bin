/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { PopoverModule } from '@fundamental-ngx/core';

import {
    ClickOutsideModule,
    CompileHtmlModule,
    IToolbarServiceFactory,
    PropertyPipeModule,
    ResizeObserverModule
} from 'smarteditcommons';
import { ToolbarServiceFactory } from './services';
import {
    ToolbarActionComponent,
    ToolbarActionOutletComponent,
    ToolbarComponent,
    ToolbarItemContextComponent,
    ToolbarSectionItemComponent
} from './components';
import { TopToolbarsModule } from '../../components/topToolbars';

@NgModule({
    imports: [
        TopToolbarsModule,
        CommonModule,
        TranslateModule.forChild(),
        CompileHtmlModule,
        PropertyPipeModule,
        ResizeObserverModule,
        PopoverModule,
        ClickOutsideModule
    ],
    providers: [
        {
            provide: IToolbarServiceFactory,
            useClass: ToolbarServiceFactory
        }
    ],
    declarations: [
        ToolbarActionComponent,
        ToolbarActionOutletComponent,
        ToolbarComponent,
        ToolbarItemContextComponent,
        ToolbarSectionItemComponent
    ],
    entryComponents: [
        ToolbarActionComponent,
        ToolbarActionOutletComponent,
        ToolbarComponent,
        ToolbarItemContextComponent,
        ToolbarSectionItemComponent
    ],
    exports: [
        ToolbarActionComponent,
        ToolbarActionOutletComponent,
        ToolbarComponent,
        ToolbarItemContextComponent,
        ToolbarSectionItemComponent
    ]
})
export class ToolbarModule {}
