/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PopoverModule } from '@fundamental-ngx/core';

import { PopupOverlayComponent } from './PopupOverlayComponent';
import { CompileHtmlModule } from '../../directives/CompileHtmlModule';

@NgModule({
    imports: [CommonModule, PopoverModule, CompileHtmlModule],
    declarations: [PopupOverlayComponent],
    entryComponents: [PopupOverlayComponent],
    exports: [PopupOverlayComponent]
})
export class PopupOverlayModule {}
