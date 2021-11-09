/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('backendMocks', [
        'functionsModule',
        'resourceLocationsModule',
        'smarteditServicesModule'
    ])
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/test\/e2e/)
    .run(function(httpBackendService) {
        var map = [
            {
                value: '"thepreviewTicketURI"',
                key: 'previewTicketURI'
            },
            {
                value: '"/cmswebservices/v1/i18n/languages"',
                key: 'i18nAPIRoot'
            }
        ];
        httpBackendService.whenGET(/configuration/).respond(function() {
            return [200, map];
        });
    });

angular.module('yMessageApp').requires.push('backendMocks');
