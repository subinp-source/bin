/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('componentMenuService', function() {
    var fixture, $q, $rootScope, $timeout;
    var componentMenuService;
    var catalogService, experienceService, cmsDragAndDropService, storageService;

    beforeEach(function() {
        fixture = AngularUnitTestHelper.prepareModule('componentMenuServiceModule')
            .mock('catalogService', 'getContentCatalogsForSite')
            .mock('experienceService', 'getCurrentExperience')
            .mock('cmsDragAndDropService', 'update')
            .mock('storageService', 'getValueFromLocalStorage')
            .mock('storageService', '_setValueInLocalStorage')
            .service('componentMenuService');

        catalogService = fixture.mocks.catalogService;
        experienceService = fixture.mocks.experienceService;
        cmsDragAndDropService = fixture.mocks.cmsDragAndDropService;
        storageService = fixture.mocks.storageService;
        componentMenuService = fixture.service;

        $q = fixture.injected.$q;
        $rootScope = fixture.injected.$rootScope;
        $timeout = fixture.injected.$timeout;
    });

    describe('getContentCatalogs', function() {
        var SITE_ID = 'some site ID';
        var EXPERIENCE = {
            pageContext: {
                siteId: SITE_ID
            }
        };

        var CATALOG_1 = {
            name: 'catalog1',
            catalogId: 'catalog1_ID',
            versions: [
                {
                    version: 'catalog1_inactive_1',
                    uuid: 'catalog1_inactive_1_uuid',
                    active: false
                },
                {
                    version: 'catalog1_active',
                    uuid: 'catalog1_active_uuid',
                    active: true
                },
                {
                    version: 'catalog1_inactive_2',
                    uuid: 'catalog1_inactive_2_uuid',
                    active: false
                }
            ]
        };

        var CATALOG_2 = {
            name: 'catalog2',
            catalogId: 'catalog2_ID',
            versions: [
                {
                    version: 'catalog2_active',
                    uuid: 'catalog2_active_uuid',
                    active: true
                },
                {
                    version: 'catalog2_inactive_1',
                    uuid: 'catalog2_inactive_1_uuid',
                    active: false
                },
                {
                    version: 'catalog2_inactive_2',
                    uuid: 'catalog2_inactive_2_uuid',
                    active: false
                }
            ]
        };

        beforeEach(function() {
            experienceService.getCurrentExperience.and.returnValue($q.when(EXPERIENCE));
            $rootScope.$digest();
        });

        it(
            'GIVEN the site has multiple content catalogs ' +
                'WHEN hasMultipleContentCatalogs is called ' +
                'THEN it returns true',
            function() {
                // Arrange
                EXPERIENCE.pageContext.catalogId = 'someCatalogId2';
                // The contentCatalogs array is sorted by parent-child sequence.
                var contentCatalogs = [{
                    catalogId: 'someCatalogId1'
                }, {
                    catalogId: 'someCatalogId2'
                }];
                catalogService.getContentCatalogsForSite.and.returnValue($q.when(contentCatalogs));

                // Act
                var returnedPromise = componentMenuService.hasMultipleContentCatalogs();

                // Assert
                returnedPromise.then(function(hasMultipleContentCatalogs) {
                    expect(hasMultipleContentCatalogs).toBe(true);
                });
                $rootScope.$digest();
            }
        );

        it(
            'GIVEN the site has a single content catalog ' +
                'WHEN hasMultipleContentCatalogs is called ' +
                'THEN it returns false',
            function() {
                // Arrange
                EXPERIENCE.pageContext.catalogId = 'someCatalogId1';
                var contentCatalogs = [{
                    catalogId: 'someCatalogId1'
                }];
                catalogService.getContentCatalogsForSite.and.returnValue($q.when(contentCatalogs));

                // Act
                var returnedPromise = componentMenuService.hasMultipleContentCatalogs();

                // Assert
                returnedPromise.then(function(hasMultipleContentCatalogs) {
                    expect(hasMultipleContentCatalogs).toBe(false);
                });
                $rootScope.$digest();
            }
        );

        it(
            'WHEN getContentCatalogs is called ' +
                'THEN it returns only the catalog version selected in the experience',
            function(done) {
                // Arrange
                var expectedCatalogs = ['SomeCatalog1', 'SomeCatalog2'];
                catalogService.getContentCatalogsForSite.and.returnValue($q.when(expectedCatalogs));

                // Act
                var returnedPromise = componentMenuService.getContentCatalogs();

                // Assert
                returnedPromise.then(function(contentCatalogsReturned) {
                    expect(catalogService.getContentCatalogsForSite).toHaveBeenCalledWith(SITE_ID);
                    expect(contentCatalogsReturned).toEqual(expectedCatalogs);
                    done();
                });
                $rootScope.$digest();
            }
        );

        it(
            'GIVEN the site has only one content catalog ' +
                'WHEN getValidContentCatalogVersions is called ' +
                'THEN it returns only the catalog version selected in the experience',
            function() {
                // Arrange
                EXPERIENCE.pageContext.catalogId = CATALOG_1.id;
                EXPERIENCE.pageContext.catalogVersion = 'catalog1_active';

                var contentCatalogs = [CATALOG_1];
                catalogService.getContentCatalogsForSite.and.returnValue($q.when(contentCatalogs));

                // Act
                var returnedPromise = componentMenuService.getValidContentCatalogVersions();

                // Assert
                returnedPromise.then(function(catalogVersionsReturned) {
                    expect(catalogVersionsReturned.length).toBe(1);
                    expect(catalogVersionsReturned[0]).toEqual({
                        isCurrentCatalog: true,
                        catalogName: 'catalog1',
                        catalogId: 'catalog1_ID',
                        catalogVersionId: 'catalog1_active',
                        id: 'catalog1_active_uuid'
                    });
                });
                $rootScope.$digest();
            }
        );

        it(
            'GIVEN the site has multiple content catalogs ' +
                'WHEN getValidContentCatalogVersions is called ' +
                'THEN it returns the catalog version selected in the experience and active version of the other catalogs',
            function() {
                // Arrange
                EXPERIENCE.pageContext.catalogId = CATALOG_1.id;
                EXPERIENCE.pageContext.catalogVersion = 'catalog1_active';

                var contentCatalogs = [CATALOG_2, CATALOG_1];
                catalogService.getContentCatalogsForSite.and.returnValue($q.when(contentCatalogs));

                // Act
                var returnedPromise = componentMenuService.getValidContentCatalogVersions();

                // Assert
                returnedPromise.then(function(catalogVersionsReturned) {
                    expect(catalogVersionsReturned.length).toBe(2);
                    expect(catalogVersionsReturned[0]).toEqual({
                        isCurrentCatalog: false,
                        catalogName: 'catalog2',
                        catalogId: 'catalog2_ID',
                        catalogVersionId: 'catalog2_active',
                        id: 'catalog2_active_uuid'
                    });
                    expect(catalogVersionsReturned[1]).toEqual({
                        isCurrentCatalog: true,
                        catalogName: 'catalog1',
                        catalogId: 'catalog1_ID',
                        catalogVersionId: 'catalog1_active',
                        id: 'catalog1_active_uuid'
                    });
                });
                $rootScope.$digest();
            }
        );
    });

    describe('cookie methods', function() {
        var CATALOG_VERSIONS = [
            {
                id: 'versionABC'
            },
            {
                id: 'versionCDE'
            },
            {
                id: 'versionXYZ'
            }
        ];

        it(
            'GIVEN no cookie is set ' +
                'WHEN getInitialCatalogVersion is called ' +
                'THEN it returns the last catalog version',
            function() {
                // Arrange
                storageService.getValueFromLocalStorage.and.returnValue($q.when());

                // Act
                var returnedPromise = componentMenuService.getInitialCatalogVersion(
                    CATALOG_VERSIONS
                );

                // Assert
                returnedPromise.then(function(selectedCatalogVersion) {
                    expect(storageService.getValueFromLocalStorage).toHaveBeenCalledWith(
                        jasmine.any(String)
                    );
                    expect(selectedCatalogVersion).toBe(CATALOG_VERSIONS[2]);
                });
                $rootScope.$digest();
            }
        );

        it(
            'GIVEN a cookie is set ' +
                'WHEN getInitialCatalogVersion is called ' +
                'THEN it returns the catalog version stored in the cookie',
            function() {
                // Arrange
                var expectedCatalogVersion = CATALOG_VERSIONS[1];
                storageService.getValueFromLocalStorage.and.returnValue(
                    $q.when(expectedCatalogVersion.id)
                );

                // Act
                var returnedPromise = componentMenuService.getInitialCatalogVersion(
                    CATALOG_VERSIONS
                );

                // Assert
                returnedPromise.then(function(selectedCatalogVersion) {
                    expect(selectedCatalogVersion).toBe(expectedCatalogVersion);
                });
                $rootScope.$digest();
            }
        );

        it(
            'WHEN persistCatalogVersion is called ' +
                'THEN it stores the catalog version in the cookie',
            function() {
                // Arrange
                var catalogVersionToStore = 'some catalog version';

                // Act
                componentMenuService.persistCatalogVersion(catalogVersionToStore);

                // Assert
                expect(storageService._setValueInLocalStorage).toHaveBeenCalledWith(
                    jasmine.any(String),
                    catalogVersionToStore
                );
            }
        );
    });
});
