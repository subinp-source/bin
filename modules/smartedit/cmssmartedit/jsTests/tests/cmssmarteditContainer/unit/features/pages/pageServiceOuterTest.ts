/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import {
    annotationService,
    pageChangeEvictionTag,
    pageEvictionTag,
    rarelyChangingContent,
    Cached,
    GatewayProxied,
    IExperience,
    IExperienceService,
    IPageInfoService,
    IUriContext,
    IUrlService
} from 'smarteditcommons';
import { CmsitemsRestService, CmsApprovalStatus, CMSPageTypes, ICMSPage } from 'cmscommons';
import { PageService } from 'cmssmarteditcontainer/services/pages';

describe('Page Service ->', () => {
    const PAGE_UID = 'SOME PAGE ID';
    const PAGE_UUID = 'SOME PAGE UUID';
    const PAGE_TYPE = CMSPageTypes.CategoryPage;
    const URI_CONTEXT: IUriContext = {
        siteUID: 'mySite',
        catalogId: 'myCatalog',
        catalogVersion: 'myCatalogVersion'
    };
    const DEFAULT_PAYLOAD = {
        typeCode: PAGE_TYPE,
        itemSearchParams: 'defaultPage:true,pageStatus:ACTIVE',
        currentPage: 0,
        pageSize: 10
    };

    const pagesRestService = jasmine.createSpyObj<any>('pagesRestService', [
        'get',
        'getById',
        'update'
    ]);
    const pagesFallbacksRestService = jasmine.createSpyObj<any>('pagesFallbackRestService', [
        'getFallbacksForPageId',
        'getFallbacksForPageIdAndContext'
    ]);
    const pagesVariationsRestService = jasmine.createSpyObj<any>('pagesVariationsRestService', [
        'getVariationsForPrimaryPageId'
    ]);
    const cmsitemsRestService = jasmine.createSpyObj<CmsitemsRestService>('cmsitemsRestService', [
        'get',
        'getById',
        'getByIdAndVersion',
        'update'
    ]);
    const experienceService = jasmine.createSpyObj<IExperienceService>('experienceService', [
        'getCurrentExperience'
    ]);
    const pageInfoService = jasmine.createSpyObj<IPageInfoService>('pageInfoService', [
        'getPageUUID'
    ]);
    const urlService = jasmine.createSpyObj<IUrlService>('urlService', ['buildUriContext']);
    const copy = jasmine.createSpy('copy');

    let page: ICMSPage;
    let pageService: PageService;
    let $routeParams: ng.route.IRouteParamsService;

    beforeEach(() => {
        $routeParams = {
            siteId: 'someSiteId',
            catalogId: 'someCatalogId',
            catalogVersion: 'someCatalogVersion'
        };

        copy.and.callFake((originalValue: any) => originalValue);
        urlService.buildUriContext.and.callFake(
            (contextSiteId: string, contextCatalogId: string, contextCatalogVersion: string) => {
                return {
                    contextSiteId,
                    contextCatalogId,
                    contextCatalogVersion
                };
            }
        );

        page = {
            name: 'some page name',
            uid: PAGE_UID,
            uuid: PAGE_UUID,
            title: { en: 'some title' },
            typeCode: CMSPageTypes.ContentPage,
            defaultPage: true,
            homepage: false,
            catalogVersion: null,
            restrictions: [],
            approvalStatus: CmsApprovalStatus.CHECK,
            pageStatus: null,
            displayStatus: null,
            masterTemplate: null,
            masterTemplateId: null,
            creationtime: null,
            modifiedtime: null
        };

        pageService = new PageService(
            pagesRestService,
            pagesFallbacksRestService,
            pagesVariationsRestService,
            pageInfoService,
            cmsitemsRestService,
            experienceService,
            $routeParams,
            urlService,
            copy
        );
    });

    describe('initialization', () => {
        it('WHEN initialized THEN it must be GatewayProxied', () => {
            expect(annotationService.getClassAnnotation(PageService, GatewayProxied)).toEqual([]);
        });
    });

    describe('getPageById', () => {
        it('WHEN getPageById is called THEN it will retrieve the page matching that uid', async (done) => {
            // GIVEN
            pagesRestService.getById.and.returnValue(Promise.resolve(page));

            // WHEN
            const value = await pageService.getPageById(PAGE_UID);

            // THEN
            expect(value).toEqual(page);
            done();
        });
    });

    describe('getPageByUuid', () => {
        it('WHEN initialized THEN it is annotated with pageEvictionTag', () => {
            // GIVEN
            const decoratorObj = annotationService.getMethodAnnotation(
                PageService,
                'getPageByUuid',
                Cached
            );

            // WHEN/THEN
            expect(decoratorObj).toEqual(
                jasmine.objectContaining([
                    {
                        actions: [rarelyChangingContent],
                        tags: [pageEvictionTag]
                    }
                ])
            );
        });

        it('WHEN getPageByUuid is called THEN it will retrieve the page matching that uuid', async (done) => {
            // GIVEN
            cmsitemsRestService.getById.and.returnValue(Promise.resolve(page));

            // WHEN
            const value = await pageService.getPageByUuid(PAGE_UUID);

            // THEN
            expect(value).toEqual(page);

            done();
        });
    });

    describe('getCurrentPageInfo', () => {
        it('WHEN initialized THEN it is annotated with pageEvictionTag AND pageChangeEvictionTag', () => {
            // GIVEN
            const decoratorObj = annotationService.getMethodAnnotation(
                PageService,
                'getCurrentPageInfo',
                Cached
            );

            // WHEN/THEN
            expect(decoratorObj).toEqual(
                jasmine.objectContaining([
                    {
                        actions: [rarelyChangingContent],
                        tags: [pageEvictionTag, pageChangeEvictionTag]
                    }
                ])
            );
        });

        it('WHEN getCurrentPageInfo is called THEN it will return the page information of the current page loaded in the storefront', async (done) => {
            // GIVEN
            pageInfoService.getPageUUID.and.returnValue(Promise.resolve(PAGE_UUID));
            cmsitemsRestService.getById.and.returnValue(Promise.resolve(page));

            // WHEN
            const value = await pageService.getCurrentPageInfo();

            // THEN
            expect(cmsitemsRestService.getById).toHaveBeenCalledWith(PAGE_UUID);
            expect(value).toEqual(page);

            done();
        });
    });

    describe('getCurrentPageInfoByVersion', () => {
        it('WHEN getCurrentPageInfoByVersion is called it returns the information of the version of the current page loaded in the storefront', async (done) => {
            // GIVEN
            const versionId = 'some version Id';
            cmsitemsRestService.getByIdAndVersion.and.returnValue(Promise.resolve(page));
            pageInfoService.getPageUUID.and.returnValue(Promise.resolve(PAGE_UUID));

            // WHEN
            const value = await pageService.getCurrentPageInfoByVersion(versionId);

            // THEN
            expect(value).toEqual(page);
            expect(cmsitemsRestService.getByIdAndVersion).toHaveBeenCalledWith(
                PAGE_UUID,
                versionId
            );

            done();
        });
    });

    describe('isPagePrimary', () => {
        const MOCK_FALLBACK_PAGE_IDS = ['some primary page id'];

        it('GIVEN no fallbacks exist for the current page WHEN isPagePrimary is called THEN it should return true', async (done) => {
            // GIVEN
            pagesFallbacksRestService.getFallbacksForPageId.and.returnValue(Promise.resolve([]));

            // WHEN
            const value = await pageService.isPagePrimary(PAGE_UID);

            // THEN
            expect(value).toEqual(true);
            expect(pagesFallbacksRestService.getFallbacksForPageId).toHaveBeenCalledWith(PAGE_UID);

            done();
        });

        it('GIVEN there are fallbacks for the current page WHEN isPagePrimary is called THEN it should return false', async (done) => {
            // GIVEN
            pagesFallbacksRestService.getFallbacksForPageId.and.returnValue(
                Promise.resolve(MOCK_FALLBACK_PAGE_IDS)
            );

            // WHEN
            const value = await pageService.isPagePrimary(PAGE_UID);

            // THEN
            expect(value).toEqual(false);
            expect(pagesFallbacksRestService.getFallbacksForPageId).toHaveBeenCalledWith(PAGE_UID);
            done();
        });

        it('GIVEN no fallbacks exist for the current page WHEN isPagePrimaryWithContext is called THEN it should return true', async (done) => {
            // GIVEN
            pagesFallbacksRestService.getFallbacksForPageIdAndContext.and.returnValue(
                Promise.resolve([])
            );

            // WHEN
            const value = await pageService.isPagePrimaryWithContext(PAGE_UID, URI_CONTEXT);

            // THEN
            expect(value).toEqual(true);
            expect(pagesFallbacksRestService.getFallbacksForPageIdAndContext).toHaveBeenCalledWith(
                PAGE_UID,
                URI_CONTEXT
            );

            done();
        });

        it('GIVEN there are fallbacks for the current page WHEN isPagePrimaryWithContext is called THEN it should return false', async (done) => {
            // GIVEN
            pagesFallbacksRestService.getFallbacksForPageIdAndContext.and.returnValue(
                Promise.resolve(MOCK_FALLBACK_PAGE_IDS)
            );

            // WHEN
            const value = await pageService.isPagePrimaryWithContext(PAGE_UID, URI_CONTEXT);

            // THEN
            expect(value).toEqual(false);
            expect(pagesFallbacksRestService.getFallbacksForPageIdAndContext).toHaveBeenCalledWith(
                PAGE_UID,
                URI_CONTEXT
            );

            done();
        });
    });

    describe('getPrimaryPage', () => {
        const PRIMARY_PAGE_UID = 'some primary page uid';

        it('GIVEN uid corresponds to primary page WHEN getPrimaryPage is called THEN it should not return an empty promise', async (done) => {
            // GIVEN
            pagesFallbacksRestService.getFallbacksForPageId.and.returnValue(Promise.resolve([]));

            // WHEN
            const value = await pageService.getPrimaryPage(PAGE_UID);

            // THEN
            expect(value).toEqual(undefined);
            done();
        });

        it('GIVEN uid corresponds to page variation WHEN getPrimaryPage is called THEN it returns the primary page', async (done) => {
            // GIVEN
            pagesFallbacksRestService.getFallbacksForPageId.and.returnValue(
                Promise.resolve([PRIMARY_PAGE_UID])
            );
            pagesRestService.getById.and.returnValue(page);

            // WHEN
            const value = await pageService.getPrimaryPage(PAGE_UID);

            // THEN
            expect(value).toEqual(page);
            expect(pagesRestService.getById).toHaveBeenCalledWith(PRIMARY_PAGE_UID);
            done();
        });
    });

    describe('getVariationPages', () => {
        const VARIATION_PAGE_ID = 'some variation page ID';

        it('GIVEN there are no variations for primary page WHEN getVariationPages is called THEN it returns a promise with an empty list', async (done) => {
            // GIVEN
            pagesVariationsRestService.getVariationsForPrimaryPageId.and.returnValue(
                Promise.resolve([])
            );

            // WHEN
            const value = await pageService.getVariationPages(PAGE_UID);

            // THEN
            expect(value).toEqual([]);

            done();
        });

        it('GIVEN there are variations for primary page WHEN getVariationPages is called THEN it returns a promise with the list of variations', async (done) => {
            // GIVEN
            pagesVariationsRestService.getVariationsForPrimaryPageId.and.returnValue(
                Promise.resolve([VARIATION_PAGE_ID])
            );
            pagesRestService.get.and.returnValue(Promise.resolve([page]));

            // WHEN
            const value = await pageService.getVariationPages(PAGE_UID);

            // THEN
            expect(value).toEqual([page]);
            expect(pagesRestService.get).toHaveBeenCalledWith({
                uids: [VARIATION_PAGE_ID]
            });

            done();
        });
    });

    describe('updatePageById', () => {
        it('WHEN updatePageById is called THEN it updates the page with the provided information', async (done) => {
            // GIVEN
            const newPageName = 'Some new page name';
            const newPageInfo = lo.assign({}, page, {
                pageName: newPageName
            });

            pagesRestService.getById.and.returnValue(Promise.resolve(page));
            pagesRestService.update.and.returnValue(Promise.resolve(newPageInfo));

            // WHEN
            const value = await pageService.updatePageById(PAGE_UID, newPageInfo);

            // THEN
            expect(value).toEqual(newPageInfo);
            expect(pagesRestService.update).toHaveBeenCalledWith(PAGE_UID, newPageInfo);

            done();
        });
    });

    describe('forcePageApprovalStatus', () => {
        const NEW_APPROVAL_STATUS = CmsApprovalStatus.APPROVED;

        it('WHEN forcePageApprovalStatus is called THEN it retrieves the current page info AND updates it with the new status', async (done) => {
            // GIVEN
            const expectedUpdatePayload = lo.assign({}, page, {
                approvalStatus: NEW_APPROVAL_STATUS,
                identifier: PAGE_UUID
            });

            const expectedPageInfo = lo.assign({}, page, {
                approvalStatus: NEW_APPROVAL_STATUS
            });

            pageInfoService.getPageUUID.and.returnValue(Promise.resolve(PAGE_UUID));
            cmsitemsRestService.getById.and.returnValue(Promise.resolve(page));
            cmsitemsRestService.update.and.returnValue(Promise.resolve(expectedPageInfo));

            // WHEN
            const value = await pageService.forcePageApprovalStatus(NEW_APPROVAL_STATUS);

            // THEN
            expect(value).toEqual(expectedPageInfo);
            expect(cmsitemsRestService.update).toHaveBeenCalledWith(expectedUpdatePayload);

            done();
        });
    });

    describe('isPageApproved', () => {
        beforeEach(() => {
            cmsitemsRestService.getById.and.returnValue(Promise.resolve(page));
        });

        it('GIVEN the uuid of an unapproved page WHEN isPageApproved is called THEN it returns false', async (done) => {
            // GIVEN
            cmsitemsRestService.getById.calls.reset();

            // WHEN
            const value = await pageService.isPageApproved(PAGE_UUID);

            // THEN
            expect(value).toEqual(false);
            expect(cmsitemsRestService.getById).toHaveBeenCalledWith(PAGE_UUID);

            done();
        });

        it('GIVEN the uuid of an approved page WHEN isPageApproved is called THEN it returns true', async (done) => {
            // GIVEN
            cmsitemsRestService.getById.calls.reset();
            page.approvalStatus = CmsApprovalStatus.APPROVED;

            // WHEN
            const value = await pageService.isPageApproved(PAGE_UUID);

            // THEN
            expect(value).toEqual(true);
            expect(cmsitemsRestService.getById).toHaveBeenCalledWith(PAGE_UUID);

            done();
        });

        it('GIVEN an unapproved page WHEN isPageApproved is called THEN it returns false', async (done) => {
            // GIVEN
            cmsitemsRestService.getById.calls.reset();

            // WHEN
            const value = await pageService.isPageApproved(page);

            // THEN
            expect(value).toEqual(false);
            expect(cmsitemsRestService.getById).not.toHaveBeenCalledWith(PAGE_UUID);

            done();
        });

        it('GIVEN an approved page WHEN isPageApproved is called THEN it returns true', async (done) => {
            // GIVEN
            cmsitemsRestService.getById.calls.reset();
            page.approvalStatus = CmsApprovalStatus.APPROVED;

            // WHEN
            const value = await pageService.isPageApproved(page);

            // THEN
            expect(value).toEqual(true);
            expect(cmsitemsRestService.getById).not.toHaveBeenCalledWith(PAGE_UUID);

            done();
        });
    });

    describe('buildUriContextForCurrentPage', () => {
        it('GIVEN $routeParams has been set WHEN buildUriContextForCurrentPage is called THEN it returns the page context', async (done) => {
            // GIVEN
            experienceService.getCurrentExperience.calls.reset();

            // WHEN
            const value = await pageService.buildUriContextForCurrentPage();

            // THEN
            expect(experienceService.getCurrentExperience).not.toHaveBeenCalled();
            expect(value).toEqual({
                contextSiteId: $routeParams.siteId,
                contextCatalogId: $routeParams.catalogId,
                contextCatalogVersion: $routeParams.catalogVersion
            });

            done();
        });

        it('GIVEN $routeParams has not been set WHEN buildUriContextForCurrentPage is called THEN it returns the page context from the current experience', async (done) => {
            // GIVEN
            const experience: IExperience = {
                siteDescriptor: null,
                catalogDescriptor: null,
                productCatalogVersions: null,
                time: null,
                pageContext: {
                    siteId: $routeParams.siteId,
                    catalogName: null,
                    catalogId: $routeParams.catalogId,
                    catalogVersion: $routeParams.catalogVersion,
                    catalogVersionUuid: null,
                    active: true
                }
            };

            const emptyRouteParams = {
                siteId: null,
                catalogId: null,
                catalogVersion: null
            } as any;

            const service = new PageService(
                pagesRestService,
                pagesFallbacksRestService,
                pagesVariationsRestService,
                pageInfoService,
                cmsitemsRestService,
                experienceService,
                emptyRouteParams,
                urlService,
                copy
            );

            experienceService.getCurrentExperience.and.returnValue(Promise.resolve(experience));

            // WHEN
            const value = await service.buildUriContextForCurrentPage();

            // THEN
            expect(experienceService.getCurrentExperience).toHaveBeenCalled();
            expect(value).toEqual({
                contextSiteId: $routeParams.siteId,
                contextCatalogId: $routeParams.catalogId,
                contextCatalogVersion: $routeParams.catalogVersion
            });
            done();
        });
    });

    describe('primaryPageForPageTypeExists', () => {
        it('GIVEN primary page for page type exists THEN it should return true', async () => {
            // GIVEN
            cmsitemsRestService.get.and.returnValue(
                Promise.resolve({
                    response: [page]
                })
            );

            // WHEN
            const actual = await pageService.primaryPageForPageTypeExists(PAGE_TYPE);

            // THEN
            expect(actual).toBe(true);
        });

        it('GIVEN primary page for page type does not exist THEN it should return false', async () => {
            // GIVEN
            cmsitemsRestService.get.and.returnValue(
                Promise.resolve({
                    response: []
                })
            );

            // WHEN
            const actual = await pageService.primaryPageForPageTypeExists(PAGE_TYPE);

            // THEN
            expect(actual).toBe(false);
        });
    });

    describe('getPaginatedPrimaryPagesForPageType', () => {
        it(
            'GIVEN fetchPageParams parameter is not provided WHEN getPaginatedPrimaryPagesForPageType is called' +
                'THEN it calls the CMS Items API with the right parameters',
            async () => {
                await pageService.getPaginatedPrimaryPagesForPageType(PAGE_TYPE, URI_CONTEXT);

                const expectedPayload = lo.assign({}, URI_CONTEXT, DEFAULT_PAYLOAD);

                expect(cmsitemsRestService.get).toHaveBeenCalledWith(expectedPayload);
            }
        );

        it(
            'GIVEN fetchPageParams parameter is not provided WHEN getPaginatedPrimaryPagesForPageType is called' +
                'THEN it calls the CMS Items API with the right parameters',
            async () => {
                await pageService.getPaginatedPrimaryPagesForPageType(PAGE_TYPE, URI_CONTEXT, {
                    search: 'somelabel',
                    pageSize: 20,
                    currentPage: null
                });
                const expectedPayload1 = lo.assign({}, URI_CONTEXT, DEFAULT_PAYLOAD, {
                    mask: 'somelabel',
                    pageSize: 20,
                    currentPage: 0
                });
                expect(cmsitemsRestService.get).toHaveBeenCalledWith(expectedPayload1);

                await pageService.getPaginatedPrimaryPagesForPageType(PAGE_TYPE, URI_CONTEXT, {
                    search: 'somelabel',
                    pageSize: null,
                    currentPage: null
                });
                const expectedPayload2 = lo.assign({}, URI_CONTEXT, DEFAULT_PAYLOAD, {
                    mask: 'somelabel',
                    pageSize: 10,
                    currentPage: 0
                });
                expect(cmsitemsRestService.get).toHaveBeenCalledWith(expectedPayload2);
            }
        );

        it('WHEN is called THEN it returns the paginated list of primary pages for the given page type', async () => {
            cmsitemsRestService.get.and.returnValue(
                Promise.resolve({
                    response: [page]
                })
            );

            // WHEN
            const value = await pageService.getPaginatedPrimaryPagesForPageType(
                PAGE_TYPE,
                URI_CONTEXT
            );

            // THEN
            expect(value.response).toEqual([page]);
        });
    });
});
