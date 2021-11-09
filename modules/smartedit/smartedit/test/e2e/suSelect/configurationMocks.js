/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('backendMocks', [
        'functionsModule',
        'resourceLocationsModule',
        'smarteditServicesModule',
        'ui.bootstrap'
    ])
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/test\/e2e/)
    .run(function(httpBackendService) {
        var map = [
            {
                value: '"thepreviewTicketURI"',
                key: 'previewTicketURI'
            },
            {
                value: '{"smartEditContainerLocation":"/test/e2e/suSelect/generated_outerapp.js"}',
                key: 'applications.SelectApp'
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

angular.module('smarteditloader').requires.push('backendMocks');
angular.module('smarteditcontainer').requires.push('backendMocks');
