/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { ICatalogService } from 'smarteditcommons';
import { PageDisplayConditionsService } from 'cmssmarteditcontainer/services/pageDisplayConditions/pageDisplayConditionsService';
import { Unit } from '../../../../../../mockData/Unit';

const mockCatalog = {
    name: {
        de: 'Deutscher Produktkatalog Kleidung'
    },
    pageDisplayConditions: [
        {
            options: [
                {
                    label: 'page.displaycondition.primary',
                    id: 'PRIMARY'
                }
            ],
            typecode: 'ProductPage'
        },
        {
            options: [
                {
                    label: 'page.displaycondition.variation',
                    id: 'VARIATION'
                }
            ],
            typecode: 'CategoryPage'
        },
        {
            options: [
                {
                    label: 'page.displaycondition.primary',
                    id: 'PRIMARY'
                },
                {
                    label: 'page.displaycondition.variation',
                    id: 'VARIATION'
                }
            ],
            typecode: 'ContentPage'
        }
    ],
    uid: 'apparel-ukContentCatalog',
    version: 'Online'
};

describe('pageDisplayConditionsService - ', () => {
    let pageDisplayConditionsService: PageDisplayConditionsService;
    let catalogService: jasmine.SpyObj<ICatalogService>;

    const uriContextMocks = Unit.MockData.UriContext;
    const pageTypeMocks = Unit.MockData.pageType;
    const pageDisplayConditionMockClass = Unit.MockData.PageDisplayCondition;

    beforeEach(() => {
        catalogService = jasmine.createSpyObj('catalogService', ['getContentCatalogVersion']);
        catalogService.getContentCatalogVersion.and.callFake(() => {
            return Promise.resolve(mockCatalog);
        });

        pageDisplayConditionsService = new PageDisplayConditionsService(catalogService);
    });

    describe('getNewPageConditions() - ', () => {
        it('Page Type with only primary option returns primary display condition', () => {
            pageDisplayConditionsService
                .getNewPageConditions(
                    pageTypeMocks.byTypeCode.PRODUCT_PAGE.typeCode,
                    uriContextMocks
                )
                .then((data) => {
                    expect(data).toEqual([new pageDisplayConditionMockClass().PRIMARY]);
                });
        });

        it('Page Type with only variation option returns variation display condition', () => {
            pageDisplayConditionsService
                .getNewPageConditions(
                    pageTypeMocks.byTypeCode.CATEGORY_PAGE.typeCode,
                    uriContextMocks
                )
                .then((data) => {
                    expect(data).toEqual([new pageDisplayConditionMockClass().VARIANT]);
                });
        });
        it('Page Type with both primary and variation options return both display conditions', () => {
            pageDisplayConditionsService
                .getNewPageConditions(
                    pageTypeMocks.byTypeCode.CONTENT_PAGE.typeCode,
                    uriContextMocks
                )
                .then((data) => {
                    expect(data).toEqual(new pageDisplayConditionMockClass().ALL);
                });
        });
    });
});
