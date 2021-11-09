/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('SitesMockModule', []).run(function(httpBackendService) {
    httpBackendService.whenGET(/cmswebservices\/v1\/sites/).respond({ sites: [] });
});

angular.module('smarteditloader').requires.push('SitesMockModule');
angular.module('smarteditcontainer').requires.push('SitesMockModule');
