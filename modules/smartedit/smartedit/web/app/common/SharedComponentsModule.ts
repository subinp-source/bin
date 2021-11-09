/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
    FundamentalModalTemplateModule,
    LanguageDropdownModule,
    LoginDialogModule,
    SelectModule,
    TranslationModule
} from '@smart/utils';

import {
    CompileHtmlModule,
    HasOperationPermissionDirectiveModule,
    ResizeObserverModule
} from './directives';
import { ReversePipeModule, StartFromPipeModule } from './pipes';
import { FundamentalsModule } from './FundamentalsModule';
import { MoreTextComponent } from './components/yMoreText/MoreTextComponent';
import { NgTreeModule } from './components/treeModule/TreeModule';
import { CollapsibleContainerModule } from './components/collapsible';
import { SpinnerModule } from './components/spinner';
import { HelpComponent } from './components/SeHelp/HelpComponent';
import { TabsComponent } from './components/tabs/TabsComponent';
import { TabComponent } from './components/tabs/TabComponent';
import { TooltipModule } from './components/tooltip';
import { WaitDialogComponent } from './components/WaitDialogComponent/WaitDialogComponent';
import { DropdownMenuModule } from './components/dropdown/dropdownMenu';
import { InfiniteScrollingModule } from './components/infiniteScrolling/InfiniteScrollingModule';
import { PopupOverlayModule } from './components/popupOverlay/PopupOverlayModule';
import { MessageModule } from './components/message';
import { PaginationModule } from './components/pagination';
import { EditableListModule } from './components/list';
import { SliderPanelModule } from './components/sliderPanel';
import { DynamicPagedListModule } from './components/dynamicPagedList';
import { DataTableModule } from './components/dataTable';

@NgModule({
    imports: [
        TranslationModule.forChild(),
        FundamentalsModule,
        CommonModule,
        FormsModule,
        SelectModule,
        LanguageDropdownModule,
        ReactiveFormsModule,
        CollapsibleContainerModule,
        CompileHtmlModule,
        MessageModule,
        NgTreeModule,
        HasOperationPermissionDirectiveModule,
        DropdownMenuModule,
        ResizeObserverModule,
        InfiniteScrollingModule,
        SpinnerModule,
        PopupOverlayModule,
        StartFromPipeModule,
        PaginationModule,
        TooltipModule,
        ReversePipeModule,
        EditableListModule,
        SliderPanelModule,
        LoginDialogModule,
        FundamentalModalTemplateModule,
        DynamicPagedListModule,
        DataTableModule
    ],
    declarations: [
        MoreTextComponent,
        WaitDialogComponent,
        TabsComponent,
        TabComponent,
        HelpComponent
    ],
    entryComponents: [
        MoreTextComponent,
        WaitDialogComponent,
        TabsComponent,
        TabComponent,
        HelpComponent
    ],
    exports: [
        MoreTextComponent,
        WaitDialogComponent,
        TabsComponent,
        TabComponent,
        SelectModule,
        LanguageDropdownModule,
        CompileHtmlModule,
        MessageModule,
        NgTreeModule,
        CollapsibleContainerModule,
        HasOperationPermissionDirectiveModule,
        DropdownMenuModule,
        PopupOverlayModule,
        ResizeObserverModule,
        InfiniteScrollingModule,
        StartFromPipeModule,
        PaginationModule,
        TooltipModule,
        ReversePipeModule,
        EditableListModule,
        SpinnerModule,
        SliderPanelModule,
        LoginDialogModule,
        FundamentalModalTemplateModule,
        DynamicPagedListModule,
        DataTableModule
    ]
})
export class SharedComponentsModule {}
