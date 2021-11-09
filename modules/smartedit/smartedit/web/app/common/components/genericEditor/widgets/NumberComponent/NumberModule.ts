/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';

import { NumberComponent } from './NumberComponent';

@NgModule({
    imports: [CommonModule, TranslateModule.forChild(), FormsModule],
    declarations: [NumberComponent],
    entryComponents: [NumberComponent],
    exports: [NumberComponent]
})
export class NumberModule {}
