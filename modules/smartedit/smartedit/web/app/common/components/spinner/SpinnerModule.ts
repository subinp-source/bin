/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SpinnerComponent } from './SpinnerComponent';

@NgModule({
    imports: [CommonModule],
    declarations: [SpinnerComponent],
    entryComponents: [SpinnerComponent],
    exports: [SpinnerComponent]
})
export class SpinnerModule {}
