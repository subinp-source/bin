/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IUriContext, SelectItem } from 'smarteditcommons';
import {
    DisplayConditionsFacade,
    IDisplayConditionsPrimaryPage
} from 'cmssmarteditcontainer/facades/displayConditionsFacade';
import { CMSPageTypes, ICMSPage, IPageService } from 'cmscommons';

describe('displayConditionsFacade', () => {
    const MOCK_PAGE = {
        uid: 'somePageUid',
        name: 'Some Page Name',
        typeCode: 'somePageTypeCode',
        label: 'some-page-label'
    };

    const MOCK_VARIATION_PAGES = [
        {
            uid: 'someVariationPageId',
            name: 'Some Variation Page Name',
            creationtime: '2016-07-07T14:33:37Z'
        },
        {
            uid: 'someOtherVariationPageId',
            name: 'Some Other Variation Page Name',
            creationtime: '2016-07-08T14:33:37Z'
        }
    ];

    const MOCK_SELECT_ITEM: SelectItem = {
        id: MOCK_PAGE.uid,
        label: MOCK_PAGE.name
    };

    let service: DisplayConditionsFacade;
    let pageRestrictionsService: jasmine.SpyObj<any>;
    let pageService: jasmine.SpyObj<IPageService>;
    let $log: jasmine.SpyObj<angular.ILogService>;

    beforeEach(() => {
        pageRestrictionsService = jasmine.createSpyObj('pageRestrictionsService', [
            'getPageRestrictionsCountForPageUID'
        ]);

        pageService = jasmine.createSpyObj<IPageService>('pageService', [
            'getPageById',
            'getVariationPages',
            'getPrimaryPage',
            'isPagePrimary',
            'updatePageById',
            'getPaginatedPrimaryPagesForPageType'
        ]);

        $log = jasmine.createSpyObj<angular.ILogService>('$log', ['error']);

        service = new DisplayConditionsFacade(pageService, pageRestrictionsService, $log);
    });

    describe('getPageInfoForPageUid', () => {
        beforeEach(() => {
            pageService.getPageById.and.returnValue(Promise.resolve(MOCK_PAGE));
            pageService.isPagePrimary.and.returnValue(Promise.resolve(true));
        });

        it('should retrieve the page name, type code, and whether or not the page is primary', async () => {
            const result = await service.getPageInfoForPageUid('somePageUid');

            expect(result).toEqual({
                pageName: 'Some Page Name',
                pageType: 'somePageTypeCode',
                isPrimary: true
            });
        });
    });

    describe('getVariationsForPageUid', () => {
        it('will return a promise resolving to an empty array if no variations are found', async () => {
            pageService.getVariationPages.and.returnValue(Promise.resolve([]));

            const result = await service.getVariationsForPageUid('somePageUid');

            expect(result).toEqual([]);
        });

        it('will return a list of variation pages, each of which having a page name, creation date, and number of restrictions', async () => {
            pageService.getVariationPages.and.returnValue(Promise.resolve(MOCK_VARIATION_PAGES));
            pageRestrictionsService.getPageRestrictionsCountForPageUID.and.callFake(function(
                pageUid: string
            ) {
                if (pageUid === 'someVariationPageId') {
                    return Promise.resolve(1);
                } else {
                    return Promise.resolve(2);
                }
            });

            const result = await service.getVariationsForPageUid('somePageUid');

            expect(result).toEqual([
                {
                    pageName: 'Some Variation Page Name',
                    creationDate: '2016-07-07T14:33:37Z',
                    restrictions: 1
                },
                {
                    pageName: 'Some Other Variation Page Name',
                    creationDate: '2016-07-08T14:33:37Z',
                    restrictions: 2
                }
            ]);
        });
    });

    describe('getPrimaryPageForVariationPage', () => {
        beforeEach(() => {
            pageService.getPrimaryPage.and.returnValue(Promise.resolve(MOCK_PAGE));
        });

        it('should return the primary page uid, label, and name', async () => {
            const result = await service.getPrimaryPageForVariationPage('someVariationPageUid');

            expect(result).toEqual({
                uid: 'somePageUid',
                name: 'Some Page Name',
                label: 'some-page-label'
            } as IDisplayConditionsPrimaryPage);
        });
    });

    describe('updatePage', () => {
        it('should delegate to the pageService to update the page', () => {
            service.updatePage('somePageUid', {} as ICMSPage);
            expect(pageService.updatePageById).toHaveBeenCalledWith('somePageUid', {});
        });
    });

    describe('isPagePrimary', () => {
        it('should delegate the call to the pageService', () => {
            service.isPagePrimary('somePageId');
            expect(pageService.isPagePrimary).toHaveBeenCalledWith('somePageId');
        });
    });

    describe('getPrimaryPagesForPageType', () => {
        it('WHEN is called THEN it returns paginated list of primary pages as Select Items for the given page type', async () => {
            const PAGE_TYPE = CMSPageTypes.CategoryPage;
            const URI_CONTEXT: IUriContext = {
                siteUID: 'mySite',
                catalogId: 'myCatalog',
                catalogVersion: 'myCatalogVersion'
            };

            pageService.getPaginatedPrimaryPagesForPageType.and.returnValue(
                Promise.resolve({
                    response: [MOCK_PAGE]
                })
            );

            // WHEN
            const value = await service.getPrimaryPagesForPageType(PAGE_TYPE, URI_CONTEXT);

            // THEN
            expect(value.results).toEqual([MOCK_SELECT_ITEM]);
        });
    });
});
