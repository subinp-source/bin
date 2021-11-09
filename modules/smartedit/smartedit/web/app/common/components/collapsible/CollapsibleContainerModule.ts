/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { CollapsibleContainerComponent } from './CollapsibleContainerComponent';
import { CollapsibleContainerContentComponent } from './CollapsibleContainerContentComponent';
import { CollapsibleContainerHeaderComponent } from './CollapsibleContainerHeaderComponent';

/**
 * @ngdoc overview
 * @name smarteditCommonsModule.module:CollapsibleContainerModule
 * @description
 *
 *
 * Angular Module that provides accordion functionality.
 */

@NgModule({
    imports: [TranslateModule.forChild(), CommonModule],
    declarations: [
        CollapsibleContainerComponent,
        CollapsibleContainerContentComponent,
        CollapsibleContainerHeaderComponent
    ],
    entryComponents: [
        CollapsibleContainerComponent,
        CollapsibleContainerContentComponent,
        CollapsibleContainerHeaderComponent
    ],
    exports: [
        CollapsibleContainerComponent,
        CollapsibleContainerContentComponent,
        CollapsibleContainerHeaderComponent
    ]
})
export class CollapsibleContainerModule {}
