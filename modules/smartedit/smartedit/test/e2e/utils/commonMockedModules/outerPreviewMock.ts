/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModuleWithProviders, NgModule, Provider } from '@angular/core';
import {
    moduleUtils,
    stringUtils,
    HttpBackendService,
    PREVIEW_RESOURCE_URI,
    SeEntryModule
} from 'smarteditcommons';
import { STOREFRONT_URI } from '../outerConstants';

@SeEntryModule('OuterPreviewMocks')
@NgModule({
    providers: [
        moduleUtils.initialize(
            (httpBackendService: HttpBackendService) => {
                httpBackendService
                    .whenPOST(stringUtils.resourceLocationToRegex(PREVIEW_RESOURCE_URI))
                    .respond(function(method, url, data) {
                        return [
                            200,
                            { ...data, ticketId: 'dasdfasdfasdfa', resourcePath: STOREFRONT_URI }
                        ];
                    });
            },
            [HttpBackendService]
        )
    ]
})
export class OuterPreviewMocks {
    public static provide(providers: Provider[]): ModuleWithProviders {
        return {
            ngModule: OuterPreviewMocks,
            providers: [...providers]
        };
    }
}

window.pushModules(OuterPreviewMocks);
