/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModuleWithProviders, NgModule, Provider } from '@angular/core';
import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';

@SeEntryModule('OuterSitesMocks')
@NgModule({
    providers: [
        moduleUtils.initialize(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.whenGET(/cmswebservices\/v1\/sites/).respond({ sites: [] });
            },
            [HttpBackendService]
        )
    ]
})
export class OuterSitesMocks {
    public static provide(providers: Provider[]): ModuleWithProviders {
        return {
            ngModule: OuterSitesMocks,
            providers: [...providers]
        };
    }
}

window.pushModules(OuterSitesMocks);
