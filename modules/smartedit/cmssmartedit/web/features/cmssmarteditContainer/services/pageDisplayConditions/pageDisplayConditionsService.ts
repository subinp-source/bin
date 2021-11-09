/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    IBaseCatalogVersion,
    ICatalogService,
    IPageDisplayConditions,
    IPageDisplayConditionsOption,
    IUriContext,
    SeInjectable
} from 'smarteditcommons';

export interface IDisplayCondition {
    label: string;
    description: string;
    isPrimary: boolean;
}
/**
 * @ngdoc service
 * @name pageDisplayConditionsServiceModule.service:pageDisplayConditionsService
 *
 * @description
 * The pageDisplayConditionsService provides an abstraction layer for the business logic of
 * primary/variant display conditions of a page
 */

@SeInjectable()
export class PageDisplayConditionsService {
    /**
     * @ngdoc object
     * @name pageDisplayConditionsServiceModule.object:CONDITION
     *
     * @description
     * An object representing a page display condition<br/>
     * Structure:<br/>
     * ```
     * {
     *      label: [string] key to be localized to render this condition on a webpage
     *      description: [string] key to be localized to render this condition description on a webpage
     *      isPrimary: [boolean]
     * }
     * ```
     */

    constructor(private catalogService: ICatalogService) {}

    /**
     * @ngdoc method
     * @name pageDisplayConditionsServiceModule.service:pageDisplayConditionsService#getNewPageConditions
     * @methodOf pageDisplayConditionsServiceModule.service:pageDisplayConditionsService
     *
     * @param {String} pageTypeCode The page typeCode of a potential new page
     * @param {Object} uriContext A {@link resourceLocationsModule.object:UriContext UriContext}
     *
     * @returns {Array} An array of {@link pageDisplayConditionsServiceModule.object:CONDITION page conditions} that are the
     * possible conditions if you wish to create a new page of the given pagetype that has the given possible primary
     * pages
     */

    getNewPageConditions(
        pageTypeCode: string,
        uriContext: IUriContext
    ): Promise<IDisplayCondition[]> {
        return this.getPageDisplayConditionsByPageType(pageTypeCode, uriContext);
    }

    private fetchDisplayConditionsForPageType(
        pageType: string,
        uriContext: IUriContext
    ): Promise<IPageDisplayConditions> {
        return this.catalogService
            .getContentCatalogVersion(uriContext)
            .then((catalogVersion: IBaseCatalogVersion) => {
                return catalogVersion.pageDisplayConditions.find(
                    (condition: IPageDisplayConditions) => {
                        return condition.typecode === pageType;
                    }
                );
            });
    }

    private getPageDisplayConditionsByPageType(
        pageType: string,
        uriContext: IUriContext
    ): Promise<IDisplayCondition[]> {
        return this.fetchDisplayConditionsForPageType(pageType, uriContext).then((obj) => {
            if (!obj || !obj.options) {
                return [];
            }

            return obj.options.map((option: IPageDisplayConditionsOption) => ({
                label: option.label,
                description: option.label + '.description',
                isPrimary: option.id === 'PRIMARY'
            }));
        });
    }
}
