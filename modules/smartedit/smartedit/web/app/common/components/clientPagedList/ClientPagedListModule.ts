/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SeTranslationModule } from '../../modules';
import { FilterByFieldPipeModule, StartFromPipeModule } from '../../pipes';
import { PaginationModule } from '../pagination';
import { FundamentalsModule } from '../../FundamentalsModule';
import { CompileHtmlModule, HasOperationPermissionDirectiveModule } from '../../directives';
import { ClientPagedListComponent } from './ClientPagedListComponent';
import { ClientPagedListCellComponent } from './ClientPagedListCellComponent';
import { TooltipModule } from '../tooltip';
import { DropdownMenuModule } from '../dropdown/dropdownMenu';

/**
 * @ngdoc overview
 * @name smarteditCommonsModule.module:ClientPagedListModule
 * @description
 * Provides a component to display a paginated list of items with custom renderers.
 * Allows the user to search and sort the list.
 */
@NgModule({
    imports: [
        CommonModule,
        PaginationModule,
        FundamentalsModule,
        TooltipModule,
        DropdownMenuModule,
        HasOperationPermissionDirectiveModule,
        CompileHtmlModule,
        FilterByFieldPipeModule,
        StartFromPipeModule,
        SeTranslationModule.forChild()
    ],
    entryComponents: [ClientPagedListComponent],
    declarations: [ClientPagedListComponent, ClientPagedListCellComponent],
    exports: [ClientPagedListComponent]
})
export class ClientPagedListModule {}
