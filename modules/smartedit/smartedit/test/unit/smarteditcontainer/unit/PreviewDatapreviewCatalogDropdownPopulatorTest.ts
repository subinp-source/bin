/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    PreviewDatapreviewCatalogDropdownPopulator,
    SiteService
} from 'smarteditcontainer/services';
import {
    CrossFrameEventService,
    DropdownPopulatorPayload,
    ICatalog,
    ICatalogService,
    ISite,
    LanguageService,
    TypedMap
} from 'smarteditcommons';

interface ICatalogMock extends ICatalog {
    _siteId: string;
}

describe('PreviewDatapreviewCatalogDropdownPopulator', () => {
    let previewDatapreviewCatalogDropdownPopulator: PreviewDatapreviewCatalogDropdownPopulator;
    let siteService: jasmine.SpyObj<SiteService>;
    let catalogService: jasmine.SpyObj<ICatalogService>;
    let crossFrameEventService: jasmine.SpyObj<CrossFrameEventService>;
    let languageService: jasmine.SpyObj<LanguageService>;
    const l10nFilter = (localizedMap: TypedMap<string>): string => 'catalogName';

    const siteDescriptors: Partial<ISite>[] = [
        {
            uid: 'siteId1'
        },
        {
            uid: 'siteId2'
        },
        {
            uid: 'siteId3'
        },
        {
            uid: 'siteId4'
        }
    ];

    const catalogDescriptors: ICatalogMock[] = [
        {
            catalogId: 'myCatalogId1',
            _siteId: 'siteId1',
            name: {
                en: 'myCatalog1'
            },
            versions: [
                {
                    version: 'myCatalogVersion1',
                    active: null,
                    homepage: null,
                    pageDisplayConditions: null,
                    uuid: null
                }
            ]
        },
        {
            catalogId: 'myCatalogId2',
            _siteId: 'siteId2',
            name: {
                en: 'myCatalog2'
            },
            versions: [
                {
                    version: 'myCatalogVersion2',
                    active: null,
                    homepage: null,
                    pageDisplayConditions: null,
                    uuid: null
                }
            ]
        },
        {
            catalogId: 'myCatalogId3',
            _siteId: 'siteId3',
            name: {
                en: 'myCatalog3'
            },
            versions: [
                {
                    version: 'myCatalogVersion3',
                    active: null,
                    homepage: null,
                    pageDisplayConditions: null,
                    uuid: null
                }
            ]
        },
        {
            catalogId: 'myCatalogId4',
            _siteId: 'siteId4',
            name: {
                en: 'myCatalog4'
            },
            versions: [
                {
                    version: 'myCatalogVersion4',
                    active: null,
                    homepage: null,
                    pageDisplayConditions: null,
                    uuid: null
                }
            ]
        },
        {
            catalogId: 'myCatalogId5',
            _siteId: 'siteId5',
            name: {
                en: 'myCatalog5'
            },
            versions: [
                {
                    version: 'myCatalogVersion5',
                    active: null,
                    homepage: null,
                    pageDisplayConditions: null,
                    uuid: null
                }
            ]
        }
    ];

    beforeEach(() => {
        siteService = jasmine.createSpyObj('siteService', ['getSites']);
        catalogService = jasmine.createSpyObj('catalogService', ['getContentCatalogsForSite']);
        languageService = jasmine.createSpyObj('languageService', [
            'getLanguagesForSite',
            'getResolveLocaleIsoCode'
        ]);
        crossFrameEventService = jasmine.createSpyObj('crossFrameEventService', ['subscribe']);
        previewDatapreviewCatalogDropdownPopulator = new PreviewDatapreviewCatalogDropdownPopulator(
            catalogService,
            siteService,
            languageService,
            crossFrameEventService
        );

        (previewDatapreviewCatalogDropdownPopulator as any).l10nFn = l10nFilter;
    });

    it('GIVEN siteService returns a resolved promise WHEN PreviewDatapreviewCatalogDropdownPopulator.populate is called THEN it will return a list of catalog ID - catalog versions', async () => {
        // GIVEN
        siteService.getSites.and.returnValue(Promise.resolve(siteDescriptors));
        catalogService.getContentCatalogsForSite.and.callFake((siteId: string) =>
            mockGetContentCatalogsForSite(siteId)
        );

        // WHEN
        const catalogs = await previewDatapreviewCatalogDropdownPopulator.populate(
            {} as DropdownPopulatorPayload
        );

        // THEN
        expect(catalogs).toEqual([
            {
                id: 'siteId1|myCatalogId1|myCatalogVersion1',
                label: 'catalogName - myCatalogVersion1'
            },
            {
                id: 'siteId2|myCatalogId2|myCatalogVersion2',
                label: 'catalogName - myCatalogVersion2'
            },
            {
                id: 'siteId3|myCatalogId3|myCatalogVersion3',
                label: 'catalogName - myCatalogVersion3'
            },
            {
                id: 'siteId4|myCatalogId4|myCatalogVersion4',
                label: 'catalogName - myCatalogVersion4'
            }
        ]);

        expect(siteService.getSites).toHaveBeenCalled();
        expect(catalogService.getContentCatalogsForSite.calls.count()).toBe(4);
        expect(catalogService.getContentCatalogsForSite.calls.argsFor(0)).toEqual(['siteId1']);
        expect(catalogService.getContentCatalogsForSite.calls.argsFor(1)).toEqual(['siteId2']);
        expect(catalogService.getContentCatalogsForSite.calls.argsFor(2)).toEqual(['siteId3']);
        expect(catalogService.getContentCatalogsForSite.calls.argsFor(3)).toEqual(['siteId4']);
    });

    it('GIVEN siteService returns a resolved promise WHEN PreviewDatapreviewCatalogDropdownPopulator.populate is called with a search string THEN it will return a list of catalog ID - catalog versions filtered based on the search string', async () => {
        // GIVEN
        siteService.getSites.and.returnValue(Promise.resolve(siteDescriptors));
        catalogService.getContentCatalogsForSite.and.callFake((siteId: string) =>
            mockGetContentCatalogsForSite(siteId)
        );

        // WHEN
        const catalogs = await previewDatapreviewCatalogDropdownPopulator.populate({
            field: null,
            model: null,
            selection: null,
            search: 'myCatalogVersion1'
        });

        // THEN
        expect(catalogs).toEqual([
            {
                id: 'siteId1|myCatalogId1|myCatalogVersion1',
                label: 'catalogName - myCatalogVersion1'
            }
        ]);
    });

    it('GIVEN siteService returns a rejected promise WHEN PreviewDatapreviewCatalogDropdownPopulator.populate is called THEN it will return a rejected promise', async (done) => {
        // GIVEN
        siteService.getSites.and.returnValue(Promise.reject());
        catalogService.getContentCatalogsForSite.and.callFake((siteId: string) =>
            mockGetContentCatalogsForSite(siteId)
        );

        // WHEN
        await previewDatapreviewCatalogDropdownPopulator
            .populate({} as DropdownPopulatorPayload)
            .catch((e) => {
                // THEN
                expect(siteService.getSites).toHaveBeenCalled();
                expect(catalogService.getContentCatalogsForSite).not.toHaveBeenCalled();
                done();
            });
    });

    function mockGetContentCatalogsForSite(siteId: string): Promise<any[]> {
        const catalogDescriptorsForSite = catalogDescriptors.filter(
            (catalogDescriptor) => catalogDescriptor._siteId === siteId
        );
        return Promise.resolve(catalogDescriptorsForSite);
    }
});
