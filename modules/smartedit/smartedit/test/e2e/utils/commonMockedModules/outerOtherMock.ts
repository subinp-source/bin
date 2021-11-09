/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModuleWithProviders, NgModule, Provider } from '@angular/core';
import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';

@SeEntryModule('OuterOtherMocks')
@NgModule({
    providers: [
        moduleUtils.initialize(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.whenGET(/^\w+.*/).passThrough();
            },
            [HttpBackendService]
        )
    ]
})
export class OuterOtherMocks {
    public static provide(providers: Provider[]): ModuleWithProviders {
        return {
            ngModule: OuterOtherMocks,
            providers: [...providers]
        };
    }
}

window.pushModules(OuterOtherMocks);
