/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { PageTypesRestrictionTypesService } from 'cmssmarteditcontainer/services/pageRestrictions/PageTypesRestrictionTypesService';
import { PageTypesRestrictionTypesRestService } from 'cmssmarteditcontainer/dao';

import { MockPageTypesRestrictionTypes } from '../../../../../../mockData/PageTypesRestrictionTypesMockData';

describe('pageTypesRestrictionTypesService', () => {
    let pageTypesRestrictionTypesService: PageTypesRestrictionTypesService;
    let pageTypesRestrictionTypesRestService: jasmine.SpyObj<PageTypesRestrictionTypesRestService>;

    const pageTypesRestrictionTypesMocks = new MockPageTypesRestrictionTypes();

    beforeEach(() => {
        pageTypesRestrictionTypesRestService = jasmine.createSpyObj(
            'pageTypesRestrictionTypesRestService',
            ['getRestrictionTypeCodesForPageType', 'getPageTypesRestrictionTypes']
        );

        pageTypesRestrictionTypesRestService.getPageTypesRestrictionTypes.and.callFake(() => {
            return Promise.resolve(pageTypesRestrictionTypesMocks.getMocks());
        });

        pageTypesRestrictionTypesService = new PageTypesRestrictionTypesService(
            pageTypesRestrictionTypesRestService
        );
    });
    // ------------------------------------------------------------------------------------------

    it('should return all pageTypesRestrictionTypes', async () => {
        const result = await pageTypesRestrictionTypesService.getPageTypesRestrictionTypes();
        expect(result).toEqual(
            pageTypesRestrictionTypesMocks.getMocks().pageTypeRestrictionTypeList
        );
    });

    it('should cache the results and return cache if it exists', async () => {
        const orig = await pageTypesRestrictionTypesService.getPageTypesRestrictionTypes();
        const second = await pageTypesRestrictionTypesService.getPageTypesRestrictionTypes();

        expect(
            pageTypesRestrictionTypesRestService.getPageTypesRestrictionTypes.calls.count()
        ).toBe(1);

        expect(orig).toEqual(second);
    });

    it('should return page types restriction types for specific page type', async () => {
        const result = await pageTypesRestrictionTypesService.getRestrictionTypeCodesForPageType(
            'ContentPage'
        );
        expect(result).toEqual(pageTypesRestrictionTypesMocks.getTypeCodesForContentPageMocks());
    });
});
