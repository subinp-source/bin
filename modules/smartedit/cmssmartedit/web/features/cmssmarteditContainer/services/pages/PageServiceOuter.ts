/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { assign, isEmpty, isNumber } from 'lodash';
import {
    pageChangeEvictionTag,
    pageEvictionTag,
    rarelyChangingContent,
    Cached,
    GatewayProxied,
    IExperience,
    IExperienceService,
    IPageInfoService,
    IUriContext,
    IUrlService,
    SeInjectable
} from 'smarteditcommons';
import { CmsApprovalStatus, CMSItemSearch, ICMSPage, IPageService, Page } from 'cmscommons';
import { CmsitemsRestService } from 'cmscommons/dao/cmswebservices/sites/CmsitemsRestService';
import * as angular from 'angular';

@GatewayProxied()
@SeInjectable()
export class PageService extends IPageService {
    constructor(
        private pagesRestService: any,
        private pagesFallbacksRestService: any,
        private pagesVariationsRestService: any,
        private pageInfoService: IPageInfoService,
        private cmsitemsRestService: CmsitemsRestService,
        private experienceService: IExperienceService,
        private $routeParams: angular.route.IRouteParamsService,
        private urlService: IUrlService,
        private copy: (src: any) => any
    ) {
        super();
    }

    public getPageById(pageUid: string) {
        return this.pagesRestService.getById(pageUid);
    }

    @Cached({ actions: [rarelyChangingContent], tags: [pageEvictionTag] })
    public getPageByUuid(pageUuid: string): Promise<ICMSPage> {
        return this.cmsitemsRestService.getById<ICMSPage>(pageUuid);
    }

    @Cached({ actions: [rarelyChangingContent], tags: [pageEvictionTag, pageChangeEvictionTag] })
    public getCurrentPageInfo(): Promise<ICMSPage> {
        return this.pageInfoService.getPageUUID().then((pageUuid: string) => {
            return this.cmsitemsRestService.getById<ICMSPage>(pageUuid);
        });
    }

    public getCurrentPageInfoByVersion(versionId: string): Promise<ICMSPage> {
        return this.pageInfoService.getPageUUID().then((pageUUID: string) => {
            return this.cmsitemsRestService.getByIdAndVersion<ICMSPage>(pageUUID, versionId);
        });
    }

    public async primaryPageForPageTypeExists(
        pageTypeCode: string,
        uriParams?: IUriContext
    ): Promise<boolean> {
        const page = await this.getPaginatedPrimaryPagesForPageType(pageTypeCode, uriParams, {
            search: null,
            pageSize: 1,
            currentPage: 0
        });
        return page.response.length > 0;
    }

    public getPaginatedPrimaryPagesForPageType(
        pageTypeCode: string,
        uriParams?: IUriContext,
        fetchPageParams?: {
            search: string;
            pageSize: number;
            currentPage: number;
        }
    ): Promise<Page<ICMSPage>> {
        const itemSearchParams = 'defaultPage:true,pageStatus:ACTIVE';

        const extendedParams = assign({}, uriParams || {}, fetchPageParams || {}, {
            typeCode: pageTypeCode,
            itemSearchParams
        }) as CMSItemSearch;

        if (extendedParams.search) {
            extendedParams.mask = extendedParams.search as string;
            delete extendedParams.search;
        }
        if (!isNumber(extendedParams.pageSize)) {
            extendedParams.pageSize = 10;
        }
        if (!isNumber(extendedParams.currentPage)) {
            extendedParams.currentPage = 0;
        }

        return this.cmsitemsRestService.get(extendedParams);
    }

    public isPagePrimary(pageUid: string): Promise<boolean> {
        return this.pagesFallbacksRestService
            .getFallbacksForPageId(pageUid)
            .then((fallbacks: any) => {
                return fallbacks.length === 0;
            });
    }

    public isPagePrimaryWithContext(pageUid: string, uriContext: IUriContext): Promise<boolean> {
        return this.pagesFallbacksRestService
            .getFallbacksForPageIdAndContext(pageUid, uriContext)
            .then((fallbacks: any) => {
                return fallbacks.length === 0;
            });
    }

    public getPrimaryPage(variationPageId: string) {
        return this.pagesFallbacksRestService
            .getFallbacksForPageId(variationPageId)
            .then((fallbacks: any) => {
                return fallbacks[0]
                    ? this.pagesRestService.getById(fallbacks[0])
                    : Promise.resolve();
            });
    }

    public getVariationPages(primaryPageId: string) {
        return this.pagesVariationsRestService
            .getVariationsForPrimaryPageId(primaryPageId)
            .then((variationPageIds: any) => {
                return variationPageIds.length > 0
                    ? this.pagesRestService.get({
                          uids: variationPageIds
                      })
                    : Promise.resolve([]);
            });
    }

    public updatePageById(pageUid: string, payload: ICMSPage): Promise<ICMSPage> {
        return this.pagesRestService.getById(pageUid).then((originalPage: ICMSPage) => {
            // This call is done to ensure that default promise properties are removed from the payload.
            const originalPagePayload = this.copy(originalPage);

            payload = { ...originalPagePayload, ...payload };
            return this.pagesRestService.update(pageUid, payload);
        });
    }

    public forcePageApprovalStatus(newPageStatus: CmsApprovalStatus): Promise<ICMSPage> {
        return this.getCurrentPageInfo().then((pageInfo: ICMSPage) => {
            const clonePageInfo = Object.assign({}, pageInfo);
            clonePageInfo.approvalStatus = newPageStatus;
            clonePageInfo.identifier = pageInfo.uuid;

            return this.cmsitemsRestService.update(clonePageInfo);
        });
    }

    public isPageApproved(pageParam: string | ICMSPage): Promise<boolean> {
        let promise: Promise<ICMSPage>;

        if (typeof pageParam === 'string') {
            promise = this.getPageByUuid(pageParam);
        } else {
            promise = Promise.resolve(pageParam);
        }

        return promise.then((page: ICMSPage) => {
            return page.approvalStatus === CmsApprovalStatus.APPROVED;
        });
    }

    public buildUriContextForCurrentPage(): Promise<IUriContext> {
        let uriContext = {} as IUriContext;

        if (
            this.$routeParams.siteId &&
            this.$routeParams.catalogId &&
            this.$routeParams.catalogVersion
        ) {
            uriContext = this.urlService.buildUriContext(
                this.$routeParams.siteId,
                this.$routeParams.catalogId,
                this.$routeParams.catalogVersion
            );
        }

        if (!isEmpty(uriContext)) {
            return Promise.resolve(uriContext);
        } else {
            return this.experienceService.getCurrentExperience().then((experience: IExperience) => {
                return this.urlService.buildUriContext(
                    experience.pageContext.siteId,
                    experience.pageContext.catalogId,
                    experience.pageContext.catalogVersion
                );
            });
        }
    }
}
