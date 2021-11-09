/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
    AlertModule,
    ButtonModule,
    FormModule,
    MenuModule,
    ModalModule,
    PaginationModule,
    PopoverModule
} from '@fundamental-ngx/core';

@NgModule({
    imports: [
        ModalModule,
        ButtonModule,
        BrowserAnimationsModule,
        FormsModule,
        PopoverModule,
        MenuModule,
        AlertModule,
        PaginationModule
    ],
    exports: [
        ModalModule,
        ButtonModule,
        FormModule,
        PopoverModule,
        MenuModule,
        AlertModule,
        PaginationModule
    ]
})
export class FundamentalsModule {}
