/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('LanguageMockModule', ['resourceLocationsModule', 'functionsModule'])
    .run(function(
        httpBackendService,
        resourceLocationToRegex,
        LANGUAGE_RESOURCE_URI,
        I18N_LANGUAGES_RESOURCE_URI
    ) {
        httpBackendService
            .whenGET(resourceLocationToRegex(LANGUAGE_RESOURCE_URI))
            .respond(function() {
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

        httpBackendService.whenGET(resourceLocationToRegex(I18N_LANGUAGES_RESOURCE_URI)).respond({
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
    });

try {
    angular.module('smarteditloader').requires.push('LanguageMockModule');
    angular.module('smarteditcontainer').requires.push('LanguageMockModule');
} catch (ex) {}
