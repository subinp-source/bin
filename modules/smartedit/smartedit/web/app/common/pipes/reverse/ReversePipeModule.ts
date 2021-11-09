/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { ReversePipe } from './ReversePipe';

@NgModule({
    declarations: [ReversePipe],
    exports: [ReversePipe]
})
export class ReversePipeModule {}
