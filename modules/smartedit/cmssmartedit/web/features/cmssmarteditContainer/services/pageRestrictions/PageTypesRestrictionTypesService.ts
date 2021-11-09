/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeDowngradeService } from 'smarteditcommons';
import { ICMSPageTypeRestriction, PageTypesRestrictionTypesRestService } from '../../dao';

@SeDowngradeService()
export class PageTypesRestrictionTypesService {
    private cache: Promise<ICMSPageTypeRestriction[]>;

    constructor(
        private pageTypesRestrictionTypesRestService: PageTypesRestrictionTypesRestService
    ) {}

    async getRestrictionTypeCodesForPageType(pageType: string): Promise<string[]> {
        const pageTypesRestrictionTypes = await this.getPageTypesRestrictionTypes();

        return pageTypesRestrictionTypes
            .filter((pageTypeRestrictionType) => pageTypeRestrictionType.pageType === pageType)
            .map((pageTypeRestrictionType) => pageTypeRestrictionType.restrictionType);
    }

    getPageTypesRestrictionTypes(): Promise<ICMSPageTypeRestriction[]> {
        if (this.cache) {
            return this.cache;
        } else {
            this.cache = this.pageTypesRestrictionTypesRestService
                .getPageTypesRestrictionTypes()
                .then((response) => {
                    return response.pageTypeRestrictionTypeList;
                });
        }

        return this.cache;
    }
}
