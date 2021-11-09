/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
(function() {
    angular
        .module('e2eBackendMocks', ['resourceLocationsModule', 'smarteditServicesModule'])
        .constant('SMARTEDIT_ROOT', 'web/webroot')
        .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/test\/e2e/)
        .constant('STOREFRONT_URI', 'http://127.0.0.1:9000/test/e2e/routing/smarteditiframe.html')
        .run(function(httpBackendService) {
            var map = [
                {
                    value: '"thepreviewTicketURI"',
                    key: 'previewTicketURI'
                },
                {
                    value: '{"smartEditLocation":"/test/e2e/routing/generated_innerDecorators.js"}',
                    key: 'applications.CMSApp'
                },
                {
                    value:
                        '{"smartEditContainerLocation":"/test/e2e/routing/generated_outerapp.js"}',
                    key: 'applications.outerapp'
                },
                {
                    value: '"somepath"',
                    key: 'i18nAPIRoot'
                }
            ];

            httpBackendService.whenGET(/configuration/).respond(function() {
                return [200, map];
            });

            var allSites = [
                {
                    previewUrl: '/test/e2e/routing/smarteditiframe.html',
                    name: {
                        en: 'Electronics'
                    },
                    redirectUrl: 'redirecturlElectronics',
                    uid: 'electronics',
                    contentCatalogs: ['electronicsContentCatalog']
                },
                {
                    previewUrl: '/test/e2e/routing/smarteditiframe.html',
                    name: {
                        en: 'Apparels'
                    },
                    redirectUrl: 'redirecturlApparels',
                    uid: 'apparel-uk',
                    contentCatalogs: ['apparel-ukContentCatalog']
                }
            ];

            httpBackendService.whenGET(/cmswebservices\/sites$/).respond({
                sites: allSites
            });

            httpBackendService.whenGET(/generated\_/).passThrough();
        });

    angular.module('smarteditloader').requires.push('e2eBackendMocks');
    angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
})();
