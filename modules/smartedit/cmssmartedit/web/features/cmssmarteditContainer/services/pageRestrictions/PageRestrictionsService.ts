/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { countBy } from 'lodash';

import { SeInjectable, TypedMap } from 'smarteditcommons';
import { PageRestrictionsRestService } from '../../dao';
import { CmsitemsRestService, ICMSPage } from 'cmscommons';

/**
 * @ngdoc overview
 * @name pageRestrictionsServiceModule
 * @requires pageRestrictionsRestServiceModule
 * @requires restrictionsServiceModule
 * @requires restrictionTypesServiceModule
 * @description
 * This module defines the {@link pageRestrictionsServiceModule.service:pageRestrictionsService pageRestrictionsService} service used to consolidate business logic for SAP Hybris platform CMS restrictions for pages.
 */

/**
 * @ngdoc service
 * @name pageRestrictionsServiceModule.service:pageRestrictionsService
 * @requires $q
 * @requires pageRestrictionsRestService
 * @requires restrictionsService
 * @requires typeStructureRestService
 * @description
 * Service that concerns business logic tasks related to CMS restrictions for CMS pages.
 */

@SeInjectable()
export class PageRestrictionsService {
    constructor(
        private pageRestrictionsRestService: PageRestrictionsRestService,
        private restrictionsService: any,
        private cmsitemsRestService: CmsitemsRestService
    ) {}

    /**
     * @ngdoc service
     * @name pageRestrictionsServiceModule.service:pageRestrictionsService
     * @requires $q
     * @requires yLoDashModule
     * @requires pageRestrictionsRestService
     * @requires restrictionsService
     * @requires typeStructureRestService
     * @description
     * Service that concerns business logic tasks related to CMS restrictions for CMS pages.
     */

    /**
     * @ngdoc method
     * @name pageRestrictionsServiceModule.service:pageRestrictionsService#getPageRestrictionsCountMapForCatalogVersion
     * @methodOf pageRestrictionsServiceModule.service:pageRestrictionsService
     * @param {String} siteUID The site Id.
     * @param {String} catalogUID The catalog Id.
     * @param {String} catalogVersionUID The catalog version.
     * @returns {Object} A map of all pageId as keys, and the number of restrictions applied to that page as the values.
     */
    public async getPageRestrictionsCountMapForCatalogVersion(
        siteUID: string,
        catalogUID: string,
        catalogVersionUID: string
    ): Promise<TypedMap<number>> {
        const relations = await this.pageRestrictionsRestService.getPagesRestrictionsForCatalogVersion(
            siteUID,
            catalogUID,
            catalogVersionUID
        );

        return countBy(relations.pageRestrictionList, 'pageId');
    }

    /**
     * @ngdoc method
     * @name pageRestrictionsServiceModule.service:pageRestrictionsService#getPageRestrictionsCountForPageUID
     * @methodOf pageRestrictionsServiceModule.service:pageRestrictionsService
     * @param {String} pageUID The page Id.
     * @returns {Number} The number of restrictions applied to the page with the give page UID.
     */
    public async getPageRestrictionsCountForPageUID(pageUID: string): Promise<number> {
        const response = await this.pageRestrictionsRestService.getPagesRestrictionsForPageId(
            pageUID
        );

        return response.pageRestrictionList.length;
    }

    /**
     * @ngdoc method
     * @name pageRestrictionsServiceModule.service:pageRestrictionsService#isRestrictionTypeSupported
     * @methodOf pageRestrictionsServiceModule.service:pageRestrictionsService
     * @param {String} restrictionTypeCode Code for the restriction type.
     * @returns {Boolean} True if smartedit supports editing or creating restrictions of this type.
     */
    public async isRestrictionTypeSupported(restrictionTypeCode: string): Promise<boolean> {
        const supportedTypes = await this.restrictionsService.getSupportedRestrictionTypeCodes();

        return supportedTypes.indexOf(restrictionTypeCode) >= 0;
    }

    /**
     * @ngdoc method
     * @name pageRestrictionsServiceModule.service:pageRestrictionsService#getRestrictionsByPageUID
     * @methodOf pageRestrictionsServiceModule.service:pageRestrictionsService
     * @param {String} pageUuid The uuid of the page for which to fetch the restrictions.
     * @returns {Array} An array of all restrictions applied to the page with the given page ID
     */
    public async getRestrictionsByPageUUID(pageUuid: string): Promise<ICMSPage[]> {
        const pageData = await this.cmsitemsRestService.getById(pageUuid);

        return this.cmsitemsRestService.getByIds(pageData.restrictions as string[]);
    }
}
