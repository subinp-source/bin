/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { PageRestrictionsCriteriaService } from 'cmssmarteditcontainer/services/pageRestrictions/PageRestrictionCriteriaService';
import { ICMSPage } from 'cmscommons';

describe('pageRestrictionsCriteriaService', () => {
    let pageRestrictionsCriteriaService: PageRestrictionsCriteriaService;

    beforeEach(() => {
        pageRestrictionsCriteriaService = new PageRestrictionsCriteriaService();
    });

    // ------------------------------------------------------------------------------------------

    it('should return all criteria options', () => {
        expect(pageRestrictionsCriteriaService.getRestrictionCriteriaOptions().length).toBe(2);
        expect(pageRestrictionsCriteriaService.getRestrictionCriteriaOptions()[0].value).toBe(
            false
        );
        expect(pageRestrictionsCriteriaService.getRestrictionCriteriaOptions()[1].value).toBe(true);
    });

    it('should get the "All" restriction criteria from a given page object', () => {
        const fakePage = {
            onlyOneRestrictionMustApply: false
        } as ICMSPage;
        expect(pageRestrictionsCriteriaService.getRestrictionCriteriaOptionFromPage(fakePage)).toBe(
            pageRestrictionsCriteriaService.getRestrictionCriteriaOptions()[0]
        );
    });

    it('should get the "Any" restriction criteria from a given page object', () => {
        const fakePage = {
            onlyOneRestrictionMustApply: true
        } as ICMSPage;
        expect(pageRestrictionsCriteriaService.getRestrictionCriteriaOptionFromPage(fakePage)).toBe(
            pageRestrictionsCriteriaService.getRestrictionCriteriaOptions()[1]
        );
    });
});
