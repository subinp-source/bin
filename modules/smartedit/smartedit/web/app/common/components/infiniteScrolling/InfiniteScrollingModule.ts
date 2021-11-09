/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InfiniteScrollModule } from '@fundamental-ngx/core';

import { InfiniteScrollingComponent } from './InfiniteScrollingComponent';
import { SpinnerModule } from '../spinner';

@NgModule({
    imports: [InfiniteScrollModule, SpinnerModule, CommonModule],
    declarations: [InfiniteScrollingComponent],
    entryComponents: [InfiniteScrollingComponent],
    exports: [InfiniteScrollingComponent]
})
export class InfiniteScrollingModule {}
