/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('backendMocks', [
        'functionsModule',
        'resourceLocationsModule',
        'smarteditServicesModule'
    ])
    .run(function(httpBackendService) {
        httpBackendService.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
            languages: [
                {
                    nativeName: 'English',
                    isocode: 'en',
                    required: true
                },
                {
                    nativeName: 'Polish',
                    isocode: 'pl'
                },
                {
                    nativeName: 'Italian',
                    isocode: 'it'
                }
            ]
        });

        httpBackendService.whenGET(/i18n/).passThrough();
        httpBackendService.whenGET(/web\/common\/services\/select\/.*\.html/).passThrough();
    });

angular.module('ySelectApp').requires.push('backendMocks');
