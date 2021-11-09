/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClickOutsideDirective } from './ClickOutsideDirective';

@NgModule({
    imports: [CommonModule],
    declarations: [ClickOutsideDirective],
    exports: [ClickOutsideDirective]
})
export class ClickOutsideModule {}
