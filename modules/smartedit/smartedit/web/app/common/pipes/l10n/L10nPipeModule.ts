/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';

import { L10nPipe } from './L10nPipe';

@NgModule({
    declarations: [L10nPipe],
    exports: [L10nPipe]
})
export class L10nPipeModule {}
