/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* jshint unused:false, undef:false */
angular
    .module('customViewModule', ['smarteditServicesModule', 'backendMocksUtilsModule'])
    .constant('MOCK_PRIMARY_PAGES', [
        {
            uid: 'primaryPage1',
            label: 'primary-page-1',
            name: 'First Primary Page'
        },
        {
            uid: 'primaryPage2',
            label: 'primary-page-2',
            name: 'Second Primary Page'
        }
    ])
    .constant('PATH_TO_CUSTOM_VIEW', 'displayConditionsPrimaryPage/customView.html')
    .run(function(httpBackendService, backendMocksUtils, MOCK_PRIMARY_PAGES) {
        backendMocksUtils
            .getBackendMock('componentsListGETMock')
            .respond(function(_method, _url, _data) {
                var response = {
                    pagination: {
                        count: 2,
                        page: 0,
                        totalCount: 2,
                        totalPages: 1
                    },
                    response: MOCK_PRIMARY_PAGES
                };
                return [200, response];
            });

        httpBackendService
            .whenGET(
                /\/cmswebservices\/v1\/sites\/apparel-uk\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/pages\/.*$/
            )
            .respond(function(_method, url) {
                var uid = /pages\/(.*$)/.exec(url)[1];
                var targetPage = MOCK_PRIMARY_PAGES.find(function(page) {
                    return page.uid === uid;
                });
                return [200, targetPage];
            });
    })
    .controller('customViewController', function(MOCK_PRIMARY_PAGES) {
        this.onPrimaryPageSelectedCounter = 0;

        this.readOnly = false;
        this.associatedPrimaryPage = MOCK_PRIMARY_PAGES[0];
        this.pageType = 'ContentPage';
        this.onPrimaryPageSelect = function onPrimaryPageSelect(_primaryPage) {
            this.onPrimaryPageSelectedCounter++;
        };
    });

try {
    angular.module('smarteditcontainer').requires.push('customViewModule');
} catch (e) {}
