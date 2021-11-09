/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CustomComponentOutletDirectiveModule, SharedComponentsModule } from 'smarteditcommons';
import { SmarteditServicesModule } from '../../services/SmarteditServicesModule';

import { NotificationPanelComponent } from './NotificationPanelComponent';
import { NotificationComponent } from './NotificationComponent';

@NgModule({
    imports: [
        SmarteditServicesModule,
        SharedComponentsModule,
        CommonModule,
        CustomComponentOutletDirectiveModule
    ],
    declarations: [NotificationPanelComponent, NotificationComponent],
    exports: [NotificationPanelComponent]
})
export class NotificationPanelModule {}
