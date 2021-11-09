/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('WhoAmIMockModule', ['resourceLocationsModule', 'functionsModule'])
    .constant('ADMIN_AUTH_TOKEN', {
        access_token: 'admin-access-token',
        token_type: 'bearer'
    })
    .constant('CMSMANAGER_AUTH_TOKEN', {
        access_token: 'cmsmanager-access-token',
        token_type: 'bearer'
    })
    .constant('ADMIN_WHOAMI_DATA', {
        displayName: 'Administrator',
        uid: 'admin'
    })
    .constant('CMSMANAGER_WHOAMI_DATA', {
        displayName: 'CMS Manager',
        uid: 'cmsmanager'
    })
    .run(function(
        httpBackendService,
        ADMIN_AUTH_TOKEN,
        CMSMANAGER_AUTH_TOKEN,
        ADMIN_WHOAMI_DATA,
        CMSMANAGER_WHOAMI_DATA
    ) {
        httpBackendService
            .whenGET(/authorizationserver\/oauth\/whoami/)
            .respond(function(method, url, data, headers) {
                return [
                    200,
                    headers.Authorization === 'bearer ' + ADMIN_AUTH_TOKEN.access_token
                        ? ADMIN_WHOAMI_DATA
                        : CMSMANAGER_WHOAMI_DATA
                ];
            });

        httpBackendService.whenGET(/cmswebservices\/v1\/users\/*/).respond(function(method, url) {
            var userUid = url.substring(url.lastIndexOf('/') + 1);

            return [
                200,
                {
                    uid: userUid,
                    readableLanguages: ['de', 'ja', 'en', 'zh'],
                    writeableLanguages: ['de', 'ja', 'en', 'zh']
                }
            ];
        });
    });

try {
    angular.module('smarteditloader').requires.push('WhoAmIMockModule');
    angular.module('smarteditcontainer').requires.push('WhoAmIMockModule');
} catch (ex) {}
