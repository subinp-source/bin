/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeInjectable } from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';

export interface IPageRestrictionCriteria {
    id: string;
    editLabel: string;
    label: string;
    value: boolean;
}

/**
 * @ngdoc overview
 * @name pageRestrictionsCriteriaModule
 * @description
 * This module defines the {@link pageRestrictionsCriteriaModule.service:pageRestrictionsCriteriaService pageRestrictionsCriteriaService} service used to consolidate business logic for restriction criteria.
 */

/**
 * @ngdoc service
 * @name pageRestrictionsCriteriaModule.service:pageRestrictionsCriteriaService
 * @description
 * A service for working with restriction criteria.
 */

@SeInjectable()
export class PageRestrictionsCriteriaService {
    private readonly ALL: IPageRestrictionCriteria = {} as IPageRestrictionCriteria;
    private readonly ANY: IPageRestrictionCriteria = {} as IPageRestrictionCriteria;
    private readonly restrictionCriteriaOptions: IPageRestrictionCriteria[] = [this.ALL, this.ANY];

    constructor() {
        this.setupCriteria(this.ALL, 'all', false);
        this.setupCriteria(this.ANY, 'any', true);
    }

    /**
     * @ngdoc method
     * @name pageRestrictionsCriteriaModule.service:pageRestrictionsCriteriaService#getMatchCriteriaLabel
     * @methodOf pageRestrictionsCriteriaModule.service:pageRestrictionsCriteriaService
     * @param {Boolean} onlyOneRestrictionMustApply A boolean to determine whether one restriction should be applied.
     * @return {String} The i18n key of the restriction criteria.
     */
    public getMatchCriteriaLabel(onlyOneRestrictionMustApply: boolean): string {
        if (onlyOneRestrictionMustApply) {
            return this.ANY.label;
        }
        return this.ALL.label;
    }

    /**
     * @ngdoc method
     * @name pageRestrictionsCriteriaModule.service:pageRestrictionsCriteriaService#getRestrictionCriteriaOptions
     * @methodOf pageRestrictionsCriteriaModule.service:pageRestrictionsCriteriaService
     * @return {IPageRestrictionCriteria[]} An array of criteria options.
     */
    public getRestrictionCriteriaOptions(): IPageRestrictionCriteria[] {
        return this.restrictionCriteriaOptions;
    }

    /**
     * @ngdoc method
     * @name pageRestrictionsCriteriaModule.service:pageRestrictionsCriteriaService#getRestrictionCriteriaOptionFromPage
     * @methodOf pageRestrictionsCriteriaModule.service:pageRestrictionsCriteriaService
     * @return {IPageRestrictionCriteria} An object of the restriction criteria for the given page.
     */
    public getRestrictionCriteriaOptionFromPage(page?: ICMSPage): IPageRestrictionCriteria {
        if (page && typeof page.onlyOneRestrictionMustApply === 'boolean') {
            if (page.onlyOneRestrictionMustApply) {
                return this.ANY;
            }
        }
        return this.ALL;
    }

    private setupCriteria(
        criteria: IPageRestrictionCriteria,
        id: string,
        boolValue: boolean
    ): void {
        Object.defineProperty(criteria, 'id', {
            writable: false,
            value: id
        });
        Object.defineProperty(criteria, 'label', {
            writable: false,
            value: 'se.cms.restrictions.criteria.' + id
        });
        Object.defineProperty(criteria, 'editLabel', {
            writable: false,
            value: 'se.cms.restrictions.criteria.select.' + id
        });
        Object.defineProperty(criteria, 'value', {
            writable: false,
            value: boolValue
        });
    }
}
