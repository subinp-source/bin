/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModuleWithProviders, NgModule, Provider } from '@angular/core';
import {
    moduleUtils,
    stringUtils,
    HttpBackendService,
    I18N_LANGUAGES_RESOURCE_URI,
    LANGUAGE_RESOURCE_URI,
    SeEntryModule
} from 'smarteditcommons';

@SeEntryModule('OuterLanguagesMocks')
@NgModule({
    providers: [
        moduleUtils.initialize(
            (httpBackendService: HttpBackendService) => {
                httpBackendService
                    .whenGET(stringUtils.resourceLocationToRegex(LANGUAGE_RESOURCE_URI))
                    .respond(() => {
                        return [
                            200,
                            {
                                languages: [
                                    {
                                        nativeName: 'English',
                                        isocode: 'en',
                                        name: 'English',
                                        required: true
                                    }
                                ]
                            }
                        ];
                    });

                httpBackendService
                    .whenGET(stringUtils.resourceLocationToRegex(I18N_LANGUAGES_RESOURCE_URI))
                    .respond({
                        languages: [
                            {
                                isoCode: 'en',
                                name: 'English'
                            },
                            {
                                isoCode: 'fr',
                                name: 'French'
                            }
                        ]
                    });
            },
            [HttpBackendService]
        )
    ]
})
export class OuterLanguagesMocks {
    public static provide(providers: Provider[]): ModuleWithProviders {
        return {
            ngModule: OuterLanguagesMocks,
            providers: [...providers]
        };
    }
}

window.pushModules(OuterLanguagesMocks);
