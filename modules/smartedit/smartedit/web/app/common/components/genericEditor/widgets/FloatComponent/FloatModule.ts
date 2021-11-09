/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';

import { FloatComponent } from './FloatComponent';

@NgModule({
    imports: [CommonModule, TranslateModule.forChild(), FormsModule],
    declarations: [FloatComponent],
    entryComponents: [FloatComponent],
    exports: [FloatComponent]
})
export class FloatModule {}
