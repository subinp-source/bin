/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular.module('OthersMockModule', []).run(function(httpBackendService) {
    // Pass through all other requests
    httpBackendService.whenGET(/^\w+.*/).passThrough();
});

try {
    angular.module('smarteditloader').requires.push('OthersMockModule');
    angular.module('smarteditcontainer').requires.push('OthersMockModule');
} catch (ex) {}
