/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import * as angular from 'angular';
import {
    annotationService,
    GatewayProxied,
    IBaseCatalog,
    IBaseCatalogs,
    ICatalogService,
    IDefaultExperienceParams,
    IExperience,
    ILanguage,
    ISharedDataService,
    ISite,
    IStorage,
    IStorageManager,
    LanguageService
} from 'smarteditcommons';
import {
    ExperienceService,
    IframeManagerService,
    PreviewService,
    SiteService,
    StoragePropertiesService
} from 'smarteditcontainer/services';

describe('experienceService', () => {
    const PRODUCT_CATALOGS = [
        {
            catalogId: 'catalog1',
            versions: [
                {
                    active: true,
                    uuid: 'catalog1Version/Online',
                    version: 'Online'
                },
                {
                    active: false,
                    uuid: 'catalog1Version/Staged',
                    version: 'Staged'
                }
            ]
        },
        {
            catalogId: 'catalog2',
            versions: [
                {
                    active: true,
                    uuid: 'catalog2Version/Online',
                    version: 'Online'
                },
                {
                    active: false,
                    uuid: 'catalog2Version/Staged',
                    version: 'Staged'
                }
            ]
        }
    ];

    const ACTIVE_PRODUCT_CATALOG_VERSIONS = [
        {
            catalog: 'catalog1',
            catalogName: undefined as {},
            catalogVersion: 'Online',
            active: true,
            uuid: 'catalog1Version/Online'
        },
        {
            catalog: 'catalog2',
            catalogName: undefined as {},
            catalogVersion: 'Online',
            active: true,
            uuid: 'catalog2Version/Online'
        }
    ];

    const MOCK_STOREFRONT_PREVIEW_URL = 'someMockPreviewStorefronUrl';
    const MOCK_RESOURCE_PATH = 'https://somedomain/storefronturl';
    const MOCK_PREVIEW_TICKET_ID = 1234;

    const $location = jasmine.createSpyObj<angular.ILocationService>('location', ['path', 'url']);
    const $route = jasmine.createSpyObj<angular.route.IRouteService>('route', ['reload']);

    const logService = jasmine.createSpyObj('logService', ['error']);
    const crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['publish']);
    const siteService = jasmine.createSpyObj<SiteService>('siteService', ['getSiteById']);
    const catalogService = jasmine.createSpyObj<ICatalogService>('catalogService', [
        'getContentCatalogsForSite',
        'getProductCatalogsForSite'
    ]);
    const languageService = jasmine.createSpyObj<LanguageService>('languageService', [
        'getLanguagesForSite'
    ]);
    const previewService = jasmine.createSpyObj<PreviewService>('previewService', [
        'getResourcePathFromPreviewUrl',
        'createPreview',
        'updateUrlWithNewPreviewTicketId'
    ]);
    const sharedDataService = jasmine.createSpyObj<ISharedDataService>('sharedDataService', [
        'get',
        'set'
    ]);
    const seStorageManager = jasmine.createSpyObj<IStorageManager>('seStorageManager', [
        'getStorage'
    ]);
    const experienceStorage = jasmine.createSpyObj<IStorage<string, IExperience>>(
        'experienceStorage',
        ['get', 'put']
    );

    const iframeManagerService = jasmine.createSpyObj<IframeManagerService>(
        'iframeManagerService',
        ['loadPreview', 'setCurrentLocation']
    );

    const LANDING_PAGE_PATH: string = '/';
    const STORE_FRONT_CONTEXT: string = '/storefront';

    let experienceService: ExperienceService;

    let siteDescriptor: ISite;
    let catalogVersionDescriptor: IBaseCatalogs;
    let languageDescriptor: ILanguage;
    let getCurrentExperienceSpy: jasmine.Spy;

    const oldPageId = 'oldPageId';

    let previousExperience: IExperience;
    const locationPath = jasmine.createSpyObj<angular.ILocationService>('locationPath', [
        'replace'
    ]);

    beforeEach(() => {
        previousExperience = {
            siteDescriptor: {
                previewUrl: '/someURI/?someSite=site',
                uid: 'someSite'
            },
            catalogDescriptor: {
                catalogId: 'someCatalog',
                catalogVersion: 'someVersion'
            },
            languageDescriptor: {
                isocode: 'someLanguage'
            },
            pageId: oldPageId
        } as IExperience;

        $location.path.and.returnValue(locationPath);

        experienceStorage.put.and.returnValue(Promise.resolve({}));

        seStorageManager.getStorage.and.returnValue(Promise.resolve(experienceStorage));

        siteDescriptor = {
            contentCatalogs: ['myCatalogId'],
            name: {
                en: 'mySiteName'
            },
            previewUrl: '/yacceleratorstorefront/?site=mySiteUid',
            uid: 'mySiteUid'
        };

        catalogVersionDescriptor = {
            catalogs: [
                {
                    catalogId: 'myCatalogId',
                    name: {
                        en: 'myCatalogName'
                    },
                    versions: [
                        {
                            homepage: null,
                            pageDisplayConditions: null,
                            version: 'myCatalogVersion',
                            active: true,
                            uuid: 'myCatalogId/myCatalogVersion',
                            thumbnailUrl: '/url1'
                        }
                    ]
                }
            ]
        };

        languageDescriptor = {
            active: true,
            isocode: 'en',
            name: 'English',
            nativeName: 'English-en',
            required: true
        };

        catalogService.getProductCatalogsForSite.and.returnValue(Promise.resolve(PRODUCT_CATALOGS));

        previewService.getResourcePathFromPreviewUrl.and.callFake(() => {
            return Promise.resolve(MOCK_RESOURCE_PATH);
        });

        previewService.createPreview.and.callFake(() => {
            return Promise.resolve({
                resourcePath: MOCK_RESOURCE_PATH,
                ticketId: MOCK_PREVIEW_TICKET_ID
            });
        });

        previewService.updateUrlWithNewPreviewTicketId.and.callFake(() => {
            return Promise.resolve(MOCK_STOREFRONT_PREVIEW_URL);
        });

        iframeManagerService.loadPreview.and.callFake(() => {
            return Promise.resolve();
        });

        siteService.getSiteById.calls.reset();
        catalogService.getContentCatalogsForSite.calls.reset();
        catalogService.getProductCatalogsForSite.calls.reset();
        languageService.getLanguagesForSite.calls.reset();
        sharedDataService.set.calls.reset();
        sharedDataService.get.calls.reset();
    });

    beforeEach(() => {
        const propsService = new StoragePropertiesService([]);

        experienceService = new ExperienceService(
            seStorageManager,
            propsService,
            logService,
            crossFrameEventService,
            siteService,
            previewService,
            catalogService,
            languageService,
            sharedDataService,
            iframeManagerService,
            $location,
            $route
        );

        getCurrentExperienceSpy = spyOn(experienceService, 'getCurrentExperience').and.returnValue(
            Promise.resolve(previousExperience)
        );
        spyOn(experienceService, 'updateExperiencePageContext');
    });

    it('checks GatewayProxied', () => {
        const decoratorObj = annotationService.getClassAnnotation(
            ExperienceService,
            GatewayProxied
        );
        expect(decoratorObj).toEqual([
            'loadExperience',
            'updateExperiencePageContext',
            'getCurrentExperience',
            'hasCatalogVersionChanged',
            'buildRefreshedPreviewUrl',
            'compareWithCurrentExperience'
        ]);
    });

    it('GIVEN a pageId has been passed to the params WHEN I call buildAndSetExperience THEN it will return an experience with a pageId', async (done) => {
        // GIVEN
        siteService.getSiteById.and.returnValue(Promise.resolve(siteDescriptor));
        catalogService.getContentCatalogsForSite.and.returnValue(
            Promise.resolve([
                {
                    catalogId: 'myCatalogId',
                    name: {
                        en: 'myCatalogName'
                    },
                    versions: [
                        {
                            homepage: null,
                            pageDisplayConditions: null,
                            version: 'myCatalogVersion',
                            active: true,
                            thumbnailUrl: '/url',
                            uuid: 'myCatalogId/myCatalogVersion'
                        }
                    ]
                } as IBaseCatalog
            ])
        );

        languageService.getLanguagesForSite.and.returnValue(
            Promise.resolve([languageDescriptor, {}])
        );

        // WHEN
        const result = await experienceService.buildAndSetExperience({
            siteId: 'mySiteId',
            catalogId: 'myCatalogId',
            catalogVersion: 'myCatalogVersion',
            pageId: 'myPageId'
        });

        const expectedValue: IExperience = {
            pageId: 'myPageId',
            siteDescriptor,
            catalogDescriptor: {
                catalogId: 'myCatalogId',
                catalogVersion: 'myCatalogVersion',
                catalogVersionUuid: 'myCatalogId/myCatalogVersion',
                name: {
                    en: 'myCatalogName'
                },
                siteId: 'mySiteId',
                active: true
            },
            languageDescriptor,
            time: null,
            productCatalogVersions: ACTIVE_PRODUCT_CATALOG_VERSIONS
        };

        // THEN
        expect(result).toEqual(expectedValue);
        done();
    });

    it('GIVEN pageId has not been passed to the params WHEN I call buildAndSetExperience THEN it will return an experience without a pageId', async (done) => {
        // GIVEN
        siteService.getSiteById.and.returnValue(Promise.resolve(siteDescriptor));
        catalogService.getContentCatalogsForSite.and.returnValue(
            Promise.resolve([
                {
                    catalogId: 'myCatalogId',
                    name: {
                        en: 'myCatalogName'
                    },
                    versions: [
                        {
                            homepage: null,
                            pageDisplayConditions: null,
                            version: 'myCatalogVersion',
                            active: true,
                            thumbnailUrl: '/url',
                            uuid: 'uuid2'
                        }
                    ]
                } as IBaseCatalog
            ])
        );
        languageService.getLanguagesForSite.and.returnValue(
            Promise.resolve([languageDescriptor, {}])
        );

        // WHEN
        const result = await experienceService.buildAndSetExperience({
            siteId: 'mySiteId',
            catalogId: 'myCatalogId',
            catalogVersion: 'myCatalogVersion'
        });

        // THEN
        expect(result).toEqual({
            siteDescriptor,
            catalogDescriptor: {
                catalogId: 'myCatalogId',
                catalogVersion: 'myCatalogVersion',
                catalogVersionUuid: 'uuid2',
                name: {
                    en: 'myCatalogName'
                },
                siteId: 'mySiteId',
                active: true
            },
            languageDescriptor,
            time: null,
            productCatalogVersions: ACTIVE_PRODUCT_CATALOG_VERSIONS
        });
        done();
    });

    it('GIVEN a siteId, catalogId and catalogVersion, buildAndSetExperience will reconstruct an experience', async (done) => {
        // GIVEN
        siteService.getSiteById.and.returnValue(Promise.resolve(siteDescriptor));
        catalogService.getContentCatalogsForSite.and.returnValue(
            Promise.resolve([
                {
                    catalogId: 'myCatalogId',
                    name: {
                        en: 'myCatalogName'
                    },
                    versions: [
                        {
                            homepage: null,
                            pageDisplayConditions: null,
                            version: 'myCatalogVersion',
                            active: true,
                            thumbnailUrl: '/url',
                            uuid: 'uuid2'
                        }
                    ]
                } as IBaseCatalog
            ])
        );
        languageService.getLanguagesForSite.and.returnValue(
            Promise.resolve([languageDescriptor, {}])
        );

        // WHEN
        const result = await experienceService.buildAndSetExperience({
            siteId: 'mySiteId',
            catalogId: 'myCatalogId',
            catalogVersion: 'myCatalogVersion'
        });

        // THEN
        expect(result).toEqual({
            siteDescriptor,
            catalogDescriptor: {
                catalogId: 'myCatalogId',
                catalogVersion: 'myCatalogVersion',
                catalogVersionUuid: 'uuid2',
                name: {
                    en: 'myCatalogName'
                },
                siteId: 'mySiteId',
                active: true
            },
            languageDescriptor,
            time: null,
            productCatalogVersions: ACTIVE_PRODUCT_CATALOG_VERSIONS
        } as IExperience);

        expect(siteService.getSiteById).toHaveBeenCalledWith('mySiteId');
        expect(catalogService.getContentCatalogsForSite).toHaveBeenCalledWith('mySiteId');
        expect(languageService.getLanguagesForSite).toHaveBeenCalledWith('mySiteId');
        done();
    });

    it('GIVEN a siteId, catalogId and unknown catalogVersion, buildAndSetExperience will return a rejected promise', (done) => {
        // GIVEN
        siteService.getSiteById.and.returnValue(Promise.resolve(siteDescriptor));
        catalogService.getContentCatalogsForSite.and.returnValue(
            Promise.resolve([
                {
                    catalogId: 'someValue',
                    catalogVersion: 'someCatalogVersion'
                },
                catalogVersionDescriptor
            ])
        );
        languageService.getLanguagesForSite.and.returnValue(
            Promise.resolve([languageDescriptor, {}])
        );

        experienceService
            .buildAndSetExperience({
                siteId: 'mySiteId',
                catalogId: 'myCatalogId',
                catalogVersion: 'unknownVersion'
            })
            .catch((e) => {
                expect(e).toEqual(
                    'no catalogVersionDescriptor found for myCatalogId catalogId and unknownVersion catalogVersion'
                );
                expect(siteService.getSiteById).toHaveBeenCalledWith('mySiteId');
                expect(catalogService.getContentCatalogsForSite).toHaveBeenCalledWith('mySiteId');
                done();
            });
    });

    it('GIVEN a siteId, unknown catalogId and right catalogVersion, buildAndSetExperience will return a rejected promise', (done) => {
        // GIVEN
        siteService.getSiteById.and.returnValue(Promise.resolve(siteDescriptor));
        catalogService.getContentCatalogsForSite.and.returnValue(
            Promise.resolve([
                {
                    catalogId: 'someValue',
                    catalogVersion: 'someCatalogVersion'
                },
                catalogVersionDescriptor
            ])
        );
        languageService.getLanguagesForSite.and.returnValue(
            Promise.resolve([languageDescriptor, {}])
        );

        // WHEN
        experienceService
            .buildAndSetExperience({
                siteId: 'mySiteId',
                catalogId: 'unknownCatalogId',
                catalogVersion: 'myCatalogVersion'
            })
            .catch((e) => {
                // THEN
                expect(e).toEqual(
                    'no catalogVersionDescriptor found for unknownCatalogId catalogId and myCatalogVersion catalogVersion'
                );

                expect(siteService.getSiteById).toHaveBeenCalledWith('mySiteId');
                expect(catalogService.getContentCatalogsForSite).toHaveBeenCalledWith('mySiteId');
                done();
            });
    });

    it('GIVEN a wrong siteId, buildAndSetExperience will return a rejected promise', (done) => {
        // GIVEN
        siteService.getSiteById.and.returnValue(Promise.reject(siteDescriptor));
        catalogService.getContentCatalogsForSite.and.returnValue(
            Promise.resolve([
                {
                    catalogId: 'someValue',
                    catalogVersion: 'someCatalogVersion'
                },
                catalogVersionDescriptor
            ])
        );
        languageService.getLanguagesForSite.and.returnValue(
            Promise.resolve([languageDescriptor, {}])
        );

        // WHEN
        const promise = experienceService.buildAndSetExperience({
            siteId: 'mySiteId',
            catalogId: 'myCatalogId',
            catalogVersion: 'myCatalogVersion'
        });

        // THEN
        promise.catch((e) => {
            expect(e).toEqual(siteDescriptor);
            expect(siteService.getSiteById).toHaveBeenCalledWith('mySiteId');
            done();
        });
    });

    it('WHEN updateExperiencePageID is called THEN it retrieves the current experience, changes it and re-initializes the catalog', async () => {
        // Arrange
        const newPageId = 'newPageId';

        $location.path.and.returnValue('/');
        const setCurrentExperienceSpy = spyOn(experienceService, 'setCurrentExperience');

        // Act
        await experienceService.updateExperiencePageId(newPageId);

        // Assert
        expect(previousExperience.pageId).toEqual(newPageId);
        expect(setCurrentExperienceSpy).toHaveBeenCalledWith(previousExperience);

        expect($location.path).toHaveBeenCalledWith(STORE_FRONT_CONTEXT);
    });

    it('WHEN setCurrentExperience is called THEN it will store new experience AND set this experience in Shared Data Service AND resolve with new experience', async () => {
        const currentExperience = lo.merge(lo.cloneDeep(previousExperience), {
            pageId: 'someOtherPageId'
        });

        const actual = await experienceService.setCurrentExperience(currentExperience);

        expect(experienceStorage.put).toHaveBeenCalledWith(
            currentExperience,
            ExperienceService.EXPERIENCE_STORAGE_KEY
        );
        expect(sharedDataService.set).toHaveBeenCalledWith(
            ExperienceService.EXPERIENCE_STORAGE_KEY,
            currentExperience
        );
        expect(actual).toBe(currentExperience);
    });

    it('WHEN loadExperience is not called from the storefront view THEN it creates an experience, redirects to the storefront view and reloads the view', async (done) => {
        // GIVEN
        const siteId = 'someSite';
        const catalogId = 'someCatalog';
        const catalogVersion = 'someVersion';
        const pageId = 'somePageId';

        spyOn<ExperienceService>(experienceService, 'buildAndSetExperience').and.returnValue(
            Promise.resolve()
        );

        // WHEN
        await experienceService.loadExperience({
            siteId,
            catalogId,
            catalogVersion,
            pageId
        } as IDefaultExperienceParams);

        // THEN
        expect(experienceService.buildAndSetExperience).toHaveBeenCalledWith({
            siteId,
            catalogId,
            catalogVersion,
            pageId
        });
        expect($location.path).toHaveBeenCalledWith(STORE_FRONT_CONTEXT);
        expect(locationPath.replace).toHaveBeenCalled();
        done();
    });

    it('WHEN loadExperience is called from the storefront view THEN it creates an experience and reloads the storefront view', async (done) => {
        // GIVEN
        const siteId = 'someSite';
        const catalogId = 'someCatalog';
        const catalogVersion = 'someVersion';
        const pageId = 'somePageId';

        spyOn<ExperienceService>(experienceService, 'buildAndSetExperience').and.returnValue(
            Promise.resolve()
        );

        $location.path.and.returnValue(STORE_FRONT_CONTEXT);

        // WHEN
        await experienceService.loadExperience({
            siteId,
            catalogId,
            catalogVersion,
            pageId
        } as IDefaultExperienceParams);

        // THEN
        expect(experienceService.buildAndSetExperience).toHaveBeenCalledWith({
            siteId,
            catalogId,
            catalogVersion,
            pageId
        });
        expect($route.reload).toHaveBeenCalled();
        done();
    });

    it('GIVEN that an experience has been set WHEN I request to load a storefront THEN initializeExperience will call updateExperience', async () => {
        // Arrange
        sharedDataService.get.and.returnValue(Promise.resolve(previousExperience));
        const updateExperienceSpy = spyOn(experienceService, 'updateExperience').and.returnValue(
            Promise.resolve(previousExperience)
        );

        // Act
        await experienceService.initializeExperience();

        // Assert
        expect(updateExperienceSpy).toHaveBeenCalled();
        expect(iframeManagerService.setCurrentLocation).toHaveBeenCalled();
    });

    it('GIVEN that no experience has been set THEN initializeExperience will redirect to landing page', async () => {
        // Arrange
        getCurrentExperienceSpy.and.returnValue(Promise.resolve(null));
        const updateExperienceSpy = spyOn(experienceService, 'updateExperience').and.callThrough();

        // Act
        await experienceService.initializeExperience();

        // Assert
        expect($location.url).toHaveBeenCalledWith(LANDING_PAGE_PATH);
        expect(updateExperienceSpy).not.toHaveBeenCalled();
        expect(iframeManagerService.setCurrentLocation).toHaveBeenCalled();
    });

    it('WHEN updateExperience is called THEN it retrieves the current experience, merges it with the new experience, creates a preview ticket and reloads the iFrame', async () => {
        const newExperience = {
            pageId: 'someOtherPageId'
        } as IExperience;
        const setCurrentExperience = spyOn(experienceService, 'setCurrentExperience');
        const temp = lo.merge(lo.cloneDeep(previousExperience), newExperience);

        const previewData = experienceService._convertExperienceToPreviewData(
            temp,
            MOCK_RESOURCE_PATH
        );

        // Act
        await experienceService.updateExperience(newExperience);

        // Assert

        expect(setCurrentExperience).toHaveBeenCalledTimes(1);
        expect(previewService.getResourcePathFromPreviewUrl).toHaveBeenCalledWith(
            previousExperience.siteDescriptor.previewUrl
        );
        expect(previewService.createPreview).toHaveBeenCalledWith(previewData);
        expect(iframeManagerService.loadPreview).toHaveBeenCalledWith(
            MOCK_RESOURCE_PATH,
            MOCK_PREVIEW_TICKET_ID
        );
    });

    it('WHEN updateExperience is called with no experience THEN it retrieves the current experience, creates a preview ticket and reloads the iFrame', async (done) => {
        const previewData = experienceService._convertExperienceToPreviewData(
            previousExperience,
            MOCK_RESOURCE_PATH
        );
        // Act
        await experienceService.updateExperience();

        // Assert
        expect(experienceService.getCurrentExperience).toHaveBeenCalled();
        expect(previewService.getResourcePathFromPreviewUrl).toHaveBeenCalledWith(
            previousExperience.siteDescriptor.previewUrl
        );
        expect(previewService.createPreview).toHaveBeenCalledWith(previewData);
        expect(iframeManagerService.loadPreview).toHaveBeenCalledWith(
            MOCK_RESOURCE_PATH,
            MOCK_PREVIEW_TICKET_ID
        );
        done();
    });

    it('WHEN compareWithCurrentExperience is called with no experience THEN it returns promise with false', async (done) => {
        // WHEN
        const result = await experienceService.compareWithCurrentExperience(null);

        // THEN
        expect(result).toEqual(false);
        expect(experienceService.getCurrentExperience).not.toHaveBeenCalled();
        done();
    });

    it('WHEN compareWithCurrentExperience is called with experience same as current experience THEN it returns promise with true', async (done) => {
        // WHEN
        const result = await experienceService.compareWithCurrentExperience({
            pageId: 'oldPageId',
            catalogId: 'someCatalog',
            catalogVersion: 'someVersion',
            siteId: 'someSite'
        });

        // THEN
        expect(result).toEqual(true);
        expect(experienceService.getCurrentExperience).toHaveBeenCalled();
        done();
    });

    it('WHEN compareWithCurrentExperience is called with experience which is different than current experience THEN it returns promise with false', async (done) => {
        // WHEN
        const result = await experienceService.compareWithCurrentExperience({
            pageId: 'oldPageId',
            catalogId: 'someCatalog',
            catalogVersion: 'someVersion',
            siteId: 'differentSite'
        });

        // THEN
        expect(result).toEqual(false);
        expect(experienceService.getCurrentExperience).toHaveBeenCalled();
        done();
    });
});
