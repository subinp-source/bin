/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { FundamentalsModule } from '../../FundamentalsModule';
import { PaginationComponent } from './PaginationComponent';

@NgModule({
    imports: [FundamentalsModule],
    declarations: [PaginationComponent],
    exports: [PaginationComponent]
})
export class PaginationModule {}
