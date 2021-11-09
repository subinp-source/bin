/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IRestService, RestServiceFactory, SeDowngradeService } from 'smarteditcommons';
import {
    CMSRestriction,
    CONTEXTUAL_PAGES_RESTRICTIONS_RESOURCE_URI,
    PAGES_RESTRICTIONS_RESOURCE_URI
} from 'cmscommons';

export interface ICMSPageRestriction {
    pageRestrictionList: CMSRestriction[];
}
@SeDowngradeService()
export class PageRestrictionsRestService {
    private readonly contextualPageRestrictionsRestService: IRestService<ICMSPageRestriction>;
    private readonly pageRestrictionsRestService: IRestService<ICMSPageRestriction>;

    constructor(private restServiceFactory: RestServiceFactory) {
        this.contextualPageRestrictionsRestService = this.restServiceFactory.get(
            CONTEXTUAL_PAGES_RESTRICTIONS_RESOURCE_URI
        );
        this.pageRestrictionsRestService = this.restServiceFactory.get(
            PAGES_RESTRICTIONS_RESOURCE_URI
        );
    }

    getPagesRestrictionsForPageId(pageId: string): Promise<ICMSPageRestriction> {
        return this.contextualPageRestrictionsRestService.get({
            pageId
        });
    }

    getPagesRestrictionsForCatalogVersion(
        siteUID: string,
        catalogId: string,
        catalogVersion: string
    ): Promise<ICMSPageRestriction> {
        return this.pageRestrictionsRestService.get({
            siteUID,
            catalogId,
            catalogVersion
        });
    }
}
