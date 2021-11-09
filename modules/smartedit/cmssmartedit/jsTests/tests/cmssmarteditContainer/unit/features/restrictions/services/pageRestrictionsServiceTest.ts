/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { PageRestrictionsService } from 'cmssmarteditcontainer/services/pageRestrictions/PageRestrictionsService';
import { RestrictionTypesService } from 'cmssmarteditcontainer/services/pageRestrictions/RestrictionTypesService';
import { PageRestrictionsRestService } from 'cmssmarteditcontainer/dao';
import { ICMSPage } from 'cmscommons';

import { mockPagesRestrictions } from '../../../../../../mockData/PagesRestrictionsMockData';
import { mockCmsItems } from '../../../../../../mockData/CmsItemsMocksData';

describe('pageRestrictionsService', () => {
    let pageRestrictionsService: PageRestrictionsService;
    let restrictionTypesService: jasmine.SpyObj<RestrictionTypesService>;
    let pageRestrictionsRestService: jasmine.SpyObj<PageRestrictionsRestService>;
    let restrictionsService: jasmine.SpyObj<any>;
    let cmsitemsRestService: jasmine.SpyObj<any>;

    const MOCK_PAGES_RESTRICTIONS = mockPagesRestrictions;
    const MOCK_TIME_RESTRICTIONS_TYPE = {
        code: 'CMSTimeRestriction',
        name: {
            de: 'DAS blabla',
            en: 'Time Restriction'
        }
    };

    const MOCK_CMS_ITEMS = mockCmsItems.componentItems;

    beforeEach(() => {
        restrictionTypesService = jasmine.createSpyObj('restrictionTypesService', [
            'getRestrictionTypeForTypeCode'
        ]);

        pageRestrictionsRestService = jasmine.createSpyObj('pageRestrictionsRestService', [
            'getPagesRestrictionsForPageId',
            'getPagesRestrictionsForCatalogVersion'
        ]);

        restrictionsService = jasmine.createSpyObj('restrictionsService', ['getAllRestrictions']);

        cmsitemsRestService = jasmine.createSpyObj('cmsitemsRestService', ['getByIds', 'getById']);

        pageRestrictionsService = new PageRestrictionsService(
            pageRestrictionsRestService,
            restrictionsService,
            cmsitemsRestService
        );
    });

    beforeEach(() => {
        restrictionTypesService.getRestrictionTypeForTypeCode.and.returnValue(
            Promise.resolve(MOCK_TIME_RESTRICTIONS_TYPE)
        );
        pageRestrictionsRestService.getPagesRestrictionsForPageId.and.returnValue(
            Promise.resolve(MOCK_PAGES_RESTRICTIONS)
        );
        pageRestrictionsRestService.getPagesRestrictionsForCatalogVersion.and.returnValue(
            Promise.resolve(MOCK_PAGES_RESTRICTIONS)
        );
        cmsitemsRestService.getByIds.and.returnValue(
            Promise.resolve([MOCK_CMS_ITEMS[1], MOCK_CMS_ITEMS[2]])
        );
        cmsitemsRestService.getById.and.returnValue(Promise.resolve(MOCK_CMS_ITEMS[0]));
    });

    describe('getPageRestrictionsCountMapForCatalogVersion', () => {
        it('should return a map of page to number of restrictions', async () => {
            const result = await pageRestrictionsService.getPageRestrictionsCountMapForCatalogVersion(
                '',
                '',
                ''
            );

            expect(result).toEqual({
                homepage: 2
            });
        });
    });

    describe('getPageRestrictionsCountForPageUID', () => {
        it('should return the page to number of restrictions for a given page UID', async () => {
            const result = await pageRestrictionsService.getPageRestrictionsCountForPageUID('');

            expect(result).toEqual(2);
        });
    });

    describe('getRestrictionsByPageUUID', () => {
        it('should return the page to number of restrictions for a given page UUID', async () => {
            const result = await pageRestrictionsService.getRestrictionsByPageUUID('somePageUUId');
            expect(result).toEqual([MOCK_CMS_ITEMS[1] as ICMSPage, MOCK_CMS_ITEMS[2] as ICMSPage]);
        });
    });
});
