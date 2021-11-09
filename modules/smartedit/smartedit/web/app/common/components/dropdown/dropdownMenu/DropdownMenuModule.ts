/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslationModule } from '@smart/utils';

import { DropdownMenuComponent } from './DropdownMenuComponent';
import { DropdownMenuItemComponent } from './DropdownMenuItemComponent';
import { DropdownMenuItemDefaultComponent } from './DropdownMenuItemDefaultComponent';
import { FundamentalsModule } from '../../../FundamentalsModule';
import { CompileHtmlModule } from '../../../directives';

@NgModule({
    imports: [CommonModule, FundamentalsModule, CompileHtmlModule, TranslationModule.forChild()],
    declarations: [
        DropdownMenuComponent,
        DropdownMenuItemComponent,
        DropdownMenuItemDefaultComponent
    ],
    entryComponents: [DropdownMenuComponent, DropdownMenuItemDefaultComponent],
    exports: [DropdownMenuComponent]
})
export class DropdownMenuModule {}
