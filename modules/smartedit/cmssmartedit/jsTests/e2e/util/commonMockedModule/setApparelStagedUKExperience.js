/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
angular
    .module('setApparelStagedUKExperience', ['smarteditServicesModule'])
    .run(function(sharedDataService) {
        sharedDataService.set('experience', {
            siteDescriptor: {
                uid: 'apparel-uk'
            },
            catalogDescriptor: {
                catalogId: 'apparel-ukContentCatalog',
                catalogVersion: 'Staged',
                uuid: 'apparel-ukContentCatalog/Staged'
            },
            pageContext: {
                catalogId: 'apparel-ukContentCatalog',
                catalogVersion: 'Staged',
                uuid: 'apparel-ukContentCatalog/Staged',
                siteId: 'apparel-uk'
            }
        });
    });
angular.module('smarteditcontainer').requires.push('setApparelStagedUKExperience');
