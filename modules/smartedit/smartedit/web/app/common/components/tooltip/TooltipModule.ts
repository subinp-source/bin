/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SeTranslationModule } from '../../modules';
import { FundamentalsModule } from '../../FundamentalsModule';
import { TooltipComponent } from './TooltipComponent';

@NgModule({
    imports: [CommonModule, FundamentalsModule, SeTranslationModule.forChild()],
    declarations: [TooltipComponent],
    entryComponents: [TooltipComponent],
    exports: [TooltipComponent]
})
export class TooltipModule {}
