/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { moduleUtils, HttpBackendService, SeEntryModule } from 'smarteditcommons';
import { NgModule } from '@angular/core';

@SeEntryModule('UserMocks')
@NgModule({
    providers: [
        moduleUtils.bootstrap(
            (httpBackendService: HttpBackendService) => {
                httpBackendService.matchLatestDefinitionEnabled(true);

                const USER_ID = 'cmsmanager';
                const ALL_LANGUAGES = ['en', 'fr', 'it', 'pl', 'hi'];

                const rawConfig = window.sessionStorage.getItem('TEST_CONFIGS');
                const config = rawConfig ? JSON.parse(rawConfig) : {};

                httpBackendService
                    .whenGET(/authorizationserver\/oauth\/whoami/)
                    .respond(function() {
                        return [
                            200,
                            {
                                displayName: 'CMS Manager',
                                uid: USER_ID
                            }
                        ];
                    });

                httpBackendService
                    .whenGET(/cmswebservices\/v1\/users\/*/)
                    .respond(function(method, url) {
                        const userUid = url.substring(url.lastIndexOf('/') + 1);

                        const readableLanguages = config.readableLanguages
                            ? config.readableLanguages
                            : ALL_LANGUAGES;
                        const writeableLanguages = config.writeableLanguages
                            ? config.writeableLanguages
                            : ALL_LANGUAGES;

                        return [
                            200,
                            {
                                uid: userUid,
                                readableLanguages,
                                writeableLanguages
                            }
                        ];
                    });
            },
            [HttpBackendService]
        )
    ]
})
export class UserMocks {}
