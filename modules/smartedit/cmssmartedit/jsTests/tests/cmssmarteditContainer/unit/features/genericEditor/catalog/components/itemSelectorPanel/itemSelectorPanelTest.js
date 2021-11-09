/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('itemSelectorPanel', function() {
    // ---------------------------------------------------------------------------------
    // Constants
    // ---------------------------------------------------------------------------------
    var PANELTITLE_ID = "Item Panel Test";
    var CATALOG_ID = "apparelProductCatalog";
    var CATALOGS_MOCK = [{
            id: 'apparelProductCatalog',
            name: {
                en: 'Apparel Product Catalog',
                de: 'Produktkatalog Kleidung'
            },
            versions: [
                {
                    active: true,
                    uuid: 'apparelProductCatalog/Online',
                    version: 'Online'
                },
                {
                    active: false,
                    uuid: 'apparelProductCatalog/Staged',
                    version: 'Staged'
                }
            ]
        }];

    var $q;
    var element, scope, ctrl;


    // ---------------------------------------------------------------------------------
    // Test Setup
    // ---------------------------------------------------------------------------------
    beforeEach(angular.mock.module('cmssmarteditContainerTemplates'));

    beforeEach(
        angular.mock.module('pascalprecht.translate', function($translateProvider) {
            $translateProvider.translations('en', {
                'se.cms.catalogaware.panel.button.cancel': 'Cancel',
                'se.cms.catalogaware.panel.button.add': 'Add'
            });
            $translateProvider.preferredLanguage('en');
        })
    );
    
    beforeEach(angular.mock.module('itemSelectorPanelModule'));

    beforeEach(inject(function($rootScope, $compile, _$q_) {
        $q = _$q_;

        scope = $rootScope.$new();
        window.smarteditJQuery.extend(scope, {
            panelTitle: PANELTITLE_ID
        });

        element = $compile(
            '<se-item-selector-panel ' +
                'data-panel-title="panelTitle" ' +
                'data-items-selected="" ' +
                'data-items-template="" ' +
                'data-get-catalogs="" ' +
                'data-item-fetch-stratege="" ' +
                'data-on-change="" ' +
                'data-show-panel=true ' +
                'data-hide-panel=false ' +
                'data-catalog-item-type="" ' +
                'data-max-items="" ' +
                '> ' +
                '</se-item-selector-panel>'
        )(scope);
        scope.$digest();

        scope = element.isolateScope();
        ctrl = scope.ctrl;

    }));

    // ---------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------
    it('WHEN catalogVersion changes THEN it should not empty original catalogVersion value', function() {

        //GIVEN
        ctrl.catalogInfo.catalogId = CATALOG_ID;
        ctrl.catalogs = CATALOGS_MOCK;
        ctrl._initCatalogVersionSelector();
        
        // WHEN
        emptyCatalogVersions(ctrl.catalogVersionSelectorFetchStrategy.fetchAll());

        // THEN
        expect(getCatalogVersionByCatalogId(CATALOG_ID).length).toBe(2);
    });

    // ---------------------------------------------------------------------------------
    // Helper Functions
    // ---------------------------------------------------------------------------------
    function emptyCatalogVersions(versions){
        versions.$$state.value.length = 0;
    }

    function getCatalogVersionByCatalogId(id){
        return ctrl.catalogs.find(
            function(catalog) {
                return catalog.id === id;
            }.bind(this)
        ).versions;
    }

});
