/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { HasOperationPermissionDirective } from './HasOperationPermissionDirective';

@NgModule({
    declarations: [HasOperationPermissionDirective],
    exports: [HasOperationPermissionDirective]
})
export class HasOperationPermissionDirectiveModule {}
