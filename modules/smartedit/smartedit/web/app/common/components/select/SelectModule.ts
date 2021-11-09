/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DragDropModule } from '@angular/cdk/drag-drop';
import {
    ButtonModule,
    FormModule as FundamentalFormModule,
    MenuModule,
    PopoverModule,
    SelectModule as FundamentalSelectModule
} from '@fundamental-ngx/core';
import { ListKeyboardControlModule, TranslationModule } from '@smart/utils';

import { L10nPipeModule } from '../../pipes';
import { SelectComponent } from './SelectComponent';
import { ItemPrinterComponent } from './itemPrinter/ItemPrinterComponent';
import { ActionableSearchItemComponent } from './actionableSearchItem';
import { DefaultItemPrinterComponent } from './defaultItemPrinter/DefaultItemPrinterComponent';
import { InfiniteScrollingModule } from '../infiniteScrolling';
import { CompileHtmlModule } from '../../directives/CompileHtmlModule';
import { SelectListComponent } from './selectList/SelectListComponent';
import { SearchInputComponent } from './searchInput/SearchInputComponent';
import { ResultsHeaderComponent } from './resultsHeader/ResultsHeaderComponent';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        DragDropModule,
        PopoverModule,
        ButtonModule,
        MenuModule,
        FundamentalSelectModule,
        FundamentalFormModule,
        InfiniteScrollingModule,
        CompileHtmlModule,
        L10nPipeModule,
        ListKeyboardControlModule,
        TranslationModule.forChild()
    ],
    declarations: [
        SelectComponent,
        DefaultItemPrinterComponent,
        ItemPrinterComponent,
        ActionableSearchItemComponent,
        SelectListComponent,
        SearchInputComponent,
        ResultsHeaderComponent
    ],
    entryComponents: [
        SelectComponent,
        DefaultItemPrinterComponent,
        ItemPrinterComponent,
        ActionableSearchItemComponent,
        SelectListComponent,
        SearchInputComponent,
        ResultsHeaderComponent
    ],
    exports: [SelectComponent]
})
export class SelectModule {}
