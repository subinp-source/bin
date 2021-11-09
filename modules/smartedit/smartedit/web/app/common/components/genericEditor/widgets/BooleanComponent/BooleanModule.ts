/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { BooleanComponent } from './BooleanComponent';

@NgModule({
    imports: [CommonModule, FormsModule, TranslateModule.forChild()],
    declarations: [BooleanComponent],
    entryComponents: [BooleanComponent],
    exports: [BooleanComponent]
})
export class BooleanModule {}
