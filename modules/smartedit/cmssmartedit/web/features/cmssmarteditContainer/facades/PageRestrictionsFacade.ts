/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeInjectable } from 'smarteditcommons';

import {
    IPageRestrictionCriteria,
    PageRestrictionsCriteriaService
} from '../services/pageRestrictions/PageRestrictionCriteriaService';
import { PageRestrictionsService } from '../services/pageRestrictions/PageRestrictionsService';
import { RestrictionTypesService } from '../services/pageRestrictions/RestrictionTypesService';
import { ICMSPage } from 'cmscommons';
import { IRestrictionType } from '../dao/RestrictionTypesRestService';

@SeInjectable()
export class PageRestrictionsFacade {
    constructor(
        private pageRestrictionsCriteriaService: PageRestrictionsCriteriaService,
        private pageRestrictionsService: PageRestrictionsService,
        private restrictionTypesService: RestrictionTypesService
    ) {}

    getRestrictionCriteriaOptions(): IPageRestrictionCriteria[] {
        return this.pageRestrictionsCriteriaService.getRestrictionCriteriaOptions();
    }

    getRestrictionCriteriaOptionFromPage(page?: ICMSPage): IPageRestrictionCriteria {
        return this.pageRestrictionsCriteriaService.getRestrictionCriteriaOptionFromPage(page);
    }

    getRestrictionTypesByPageType(type: string): Promise<IRestrictionType[]> {
        return this.restrictionTypesService.getRestrictionTypesByPageType(type);
    }

    isRestrictionTypeSupported(code: string): Promise<boolean> {
        return this.pageRestrictionsService.isRestrictionTypeSupported(code);
    }

    getRestrictionsByPageUUID(uuid: string): Promise<ICMSPage[]> {
        return this.pageRestrictionsService.getRestrictionsByPageUUID(uuid);
    }
}
