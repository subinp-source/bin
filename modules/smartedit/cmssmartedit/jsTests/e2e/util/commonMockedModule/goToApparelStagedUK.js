/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
angular.module('goToApparelStagedUK', ['resourceLocationsModule']).run(function(experienceService) {
    experienceService.loadExperience({
        siteId: 'apparel-uk',
        catalogId: 'apparel-ukContentCatalog',
        catalogVersion: 'Staged'
    });
});
try {
    angular.module('smarteditloader').requires.push('goToApparelStagedUK');
} catch (e) {}
try {
    angular.module('smarteditcontainer').requires.push('goToApparelStagedUK');
} catch (e) {}
