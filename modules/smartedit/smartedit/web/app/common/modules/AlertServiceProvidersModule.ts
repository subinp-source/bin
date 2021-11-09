/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModuleWithProviders, NgModule } from '@angular/core';
import { IBaseAlertService } from 'smarteditcommons/services/interfaces';

/**
 * Temporary module providing AlertService for legacy token, until AlertService migration to Angular is completed.
 * Used by Outer / Inner modules to import.
 * It uses "useFactory" to return the same service but for different token.
 *
 * When it is in "web/app/smartedit/smartedit.ts" it results in the following error.
 * "ERROR: File web/app/smartedit/smartedit.ts contains forbidden namespace 'useFactory', consider using 'useFactory is part of DI and hence should only be used in Modules"
 */
@NgModule()
export class AlertServiceProvidersModule {
    static forRoot<T>(token: any, AlertServiceClass: any): ModuleWithProviders {
        return {
            ngModule: AlertServiceProvidersModule,
            providers: [
                {
                    provide: token,
                    useClass: AlertServiceClass
                },
                {
                    provide: IBaseAlertService,
                    useFactory: (alertService: T) => {
                        return alertService;
                    },
                    deps: [token]
                }
            ]
        };
    }
}
