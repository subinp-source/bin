/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeDowngradeService } from 'smarteditcommons';
import { PageTypesRestrictionTypesService } from './PageTypesRestrictionTypesService';
import {
    IRestrictionType,
    RestrictionTypesRestService
} from '../../dao/RestrictionTypesRestService';

@SeDowngradeService()
export class RestrictionTypesService {
    private cache: Promise<IRestrictionType[]>;

    constructor(
        private pageTypesRestrictionTypesService: PageTypesRestrictionTypesService,
        private restrictionTypesRestService: RestrictionTypesRestService
    ) {}

    async getRestrictionTypesByPageType(pageType: string): Promise<IRestrictionType[]> {
        const restrictionTypes = await this.getRestrictionTypes();
        const restrictionTypeCodes = await this.pageTypesRestrictionTypesService.getRestrictionTypeCodesForPageType(
            pageType
        );

        return restrictionTypes.filter(
            (restrictionType) => restrictionTypeCodes.indexOf(restrictionType.code) >= 0
        );
    }

    async getRestrictionTypeForTypeCode(typeCode: string): Promise<IRestrictionType> {
        const restrictionTypes = await this.getRestrictionTypes();

        return restrictionTypes.find((restrictionType) => restrictionType.code === typeCode);
    }

    getRestrictionTypes(): Promise<IRestrictionType[]> {
        if (this.cache) {
            return this.cache;
        } else {
            this.cache = this.restrictionTypesRestService.getRestrictionTypes().then((response) => {
                return response.restrictionTypes;
            });
        }
        return this.cache;
    }
}
