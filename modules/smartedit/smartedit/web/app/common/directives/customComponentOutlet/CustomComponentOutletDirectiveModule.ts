/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { CustomComponentOutletDirective } from './CustomComponentOutletDirective';

/** @internal */
@NgModule({
    declarations: [CustomComponentOutletDirective],
    exports: [CustomComponentOutletDirective]
})
export class CustomComponentOutletDirectiveModule {}
