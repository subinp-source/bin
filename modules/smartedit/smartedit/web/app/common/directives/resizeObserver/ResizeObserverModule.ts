/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { ResizeObserverDirective } from './ResizeObserverDirective';

@NgModule({
    declarations: [ResizeObserverDirective],
    exports: [ResizeObserverDirective]
})
export class ResizeObserverModule {}
