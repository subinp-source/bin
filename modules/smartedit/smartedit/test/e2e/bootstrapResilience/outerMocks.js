/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('e2eBackendMocks', ['resourceLocationsModule', 'smarteditServicesModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/test\/e2e/)
    .run(function(httpBackendService) {
        var map = [
            {
                value: '"thepreviewTicketURI"',
                key: 'previewTicketURI'
            },
            {
                value: '"somepath"',
                key: 'i18nAPIRoot'
            },
            {
                value:
                    '{"smartEditLocation":"/test/e2e/bootstrapResilience/innerExtendingModule.js" , "extends":"dummyCmsDecorators"}',
                key: 'applications.innerExtendingModule'
            },
            {
                value:
                    '{"smartEditContainerLocation":"/test/e2e/bootstrapResilience/outerExtendingModule.js", "extends":"dummyToolbars"}',
                key: 'applications.outerExtendingModule'
            },
            {
                value:
                    '{"smartEditContainerLocation":"/path/to/some/non/existent/container/script.js"}',
                key: 'applications.nonExistentSmartEditContainerModule'
            },
            {
                value: '{"smartEditLocation":"/path/to/some/non/existent/application/script.js"}',
                key: 'applications.nonExistentSmartEditModule'
            },
            {
                value:
                    '{"smartEditLocation":"/test/e2e/bootstrapResilience/dummyCmsDecorators.js"}',
                key: 'applications.dummyCmsDecorators'
            },
            {
                value:
                    '{"smartEditContainerLocation":"/test/e2e/bootstrapResilience/dummyToolbars.js"}',
                key: 'applications.dummyToolbars'
            }
        ];

        httpBackendService.whenGET(/configuration/).respond(function() {
            return [200, map];
        });

        httpBackendService.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
            languages: [
                {
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                }
            ]
        });

        httpBackendService.whenGET(/^\w+.*/).passThrough();
    });
angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
