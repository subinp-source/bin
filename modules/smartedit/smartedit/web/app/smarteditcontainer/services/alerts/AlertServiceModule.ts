/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
    AlertServiceProvidersModule,
    ALERT_CONFIG_DEFAULTS,
    ALERT_CONFIG_DEFAULTS_TOKEN,
    ALERT_SERVICE_TOKEN,
    CompileHtmlModule,
    IAlertService,
    SharedComponentsModule
} from 'smarteditcommons';

import {
    AlertModule,
    AlertService as FundamentalAlertService,
    ModalModule
} from '@fundamental-ngx/core';

import { AlertFactory } from './AlertFactory';
import { AlertService } from './AlertServiceOuter';
import { AlertTemplateComponent } from './AlertTemplateComponent';

@NgModule({
    imports: [
        AlertModule,
        CommonModule,
        CompileHtmlModule,
        SharedComponentsModule,
        AlertServiceProvidersModule.forRoot<AlertService>(IAlertService, AlertService),
        /**
         * FIXME: TEMPORARY WORKAROUND. Remove it after it is fixed in @fundamental-ngx
         *
         * In @fundamental-ngx: 1.12.0, AlertModule is missing "DynamicComponentService" provider
         * which is a dependency of AlertService.
         * Import ModalModule because it provides the "DynamicComponentService".
         */
        ModalModule
    ],
    declarations: [AlertTemplateComponent],
    entryComponents: [AlertTemplateComponent],
    providers: [
        AlertFactory,
        {
            provide: ALERT_SERVICE_TOKEN,
            useClass: FundamentalAlertService
        },
        {
            provide: ALERT_CONFIG_DEFAULTS_TOKEN,
            useValue: ALERT_CONFIG_DEFAULTS
        }
    ]
})
export class AlertServiceModule {}
