/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { FilterByFieldPipe, TypedMap } from 'smarteditcommons';
import { FilterByFieldFilter } from 'smarteditcontainer/services';

describe('FilterByFieldPipe', () => {
    const mockKeys: string[] = ['name', 'template', 'typeCode', 'uid'];
    const mockItems: TypedMap<any>[] = [
        {
            name: 'Add Edit Address Page',
            numberOfRestrictions: 0,
            onlyOneRestrictionMustApply: true,
            template: 'AccountPageTemplate',
            typeCode: 'CategoryPage',
            mockParam: 'Mock',
            uid: 'add-edit-address'
        },
        {
            name: 'Mobile Update Email Page',
            numberOfRestrictions: 1,
            onlyOneRestrictionMustApply: false,
            template: 'MobileAccountPageTemplate',
            typeCode: 'ContentPage',
            mockParam: 'Mock',
            uid: 'mobile-update-email'
        },
        {
            name: 'Frequently Asked Questions FAQ Page',
            numberOfRestrictions: 0,
            onlyOneRestrictionMustApply: true,
            template: 'ContentPage1Template',
            typeCode: 'CategoryPage',
            mockParam: 'Mock',
            uid: 'faq'
        },
        {
            name: 'Mobile Add Edit Address Page',
            numberOfRestrictions: 1,
            onlyOneRestrictionMustApply: false,
            template: 'MobileAccountPageTemplate',
            typeCode: 'ContentPage',
            mockParam: 'Mock',
            uid: 'mobile-add-edit-address'
        },
        {
            name: 'Mobile Address Book Page',
            numberOfRestrictions: 1,
            onlyOneRestrictionMustApply: false,
            template: 'MobileAccountPageTemplate',
            typeCode: 'ContentPage',
            mockParam: 'Mock',
            uid: 'mobile-address-book'
        }
    ];

    let filter: ReturnType<typeof FilterByFieldFilter.transform>;
    let pipe: FilterByFieldPipe;

    beforeEach(() => {
        pipe = new FilterByFieldPipe();
        // AngularJS
        filter = FilterByFieldFilter.transform();
    });

    it('should return no search results when searching for a key that was not specified', () => {
        const criteria = 'Mock';

        const result = pipe.transform(mockItems, criteria, mockKeys);
        expect(result.length).toBe(0);
        // AngularJS
        const filterResult = filter(mockItems, criteria, mockKeys);
        expect(filterResult.length).toBe(0);
    });

    it('should return search results based on fields that only correspond to strings', () => {
        const criteria = 'false';

        const result = pipe.transform(mockItems, criteria, mockKeys);
        expect(result.length).toBe(0);
        // AngularJS
        const filterResult = filter(mockItems, criteria, mockKeys);
        expect(filterResult.length).toBe(0);
    });

    it('should return results based on keys (name)', () => {
        const criteria = 'Add Edit';

        const result = pipe.transform(mockItems, criteria, mockKeys);
        expect(result[0].name).toBe('Add Edit Address Page');
        expect(result.length).toBe(2);
        // AngularJS
        const filterResult = filter(mockItems, criteria, mockKeys);
        expect(filterResult[0].name).toBe('Add Edit Address Page');
        expect(filterResult.length).toBe(2);
    });

    it('should return results based on keys (uid)', () => {
        const criteria = 'faq';

        const result = pipe.transform(mockItems, criteria, mockKeys);
        expect(result[0].uid).toBe('faq');
        expect(result.length).toBe(1);
        // AngularJS
        const filterResult = filter(mockItems, criteria, mockKeys);
        expect(filterResult[0].uid).toBe('faq');
        expect(filterResult.length).toBe(1);
    });

    it('should not be case sensitive', () => {
        const criteria = 'FREQUENTLY';

        const result = pipe.transform(mockItems, criteria, mockKeys);
        expect(result[0].name).toBe('Frequently Asked Questions FAQ Page');
        expect(result.length).toBe(1);
        // AngularJS
        const filterResult = filter(mockItems, criteria, mockKeys);
        expect(filterResult[0].name).toBe('Frequently Asked Questions FAQ Page');
        expect(filterResult.length).toBe(1);
    });

    it('should return results regardless of search string order', () => {
        const criteria = 'Mobile Add';

        const result = pipe.transform(mockItems, criteria, mockKeys);
        expect(result.length).toBe(2);
        // AngularJS
        const filterResult = filter(mockItems, criteria, mockKeys);
        expect(filterResult.length).toBe(2);
    });

    it('should return results based on all the fields when no keys are provided', () => {
        const criteria = 'Mock';

        const result = pipe.transform(mockItems, criteria);
        expect(result.length).toBe(5);
        // AngularJS
        const filterResult = filter(mockItems, criteria);
        expect(filterResult.length).toBe(5);
    });

    it('should return original object when search string is null', () => {
        const criteria = '';

        const result = pipe.transform(mockItems, criteria, mockKeys);
        expect(result.length).toBe(5);
        // AngularJS
        const filterResult = filter(mockItems, criteria, mockKeys);
        expect(filterResult.length).toBe(5);
    });

    it('should call the callbackfcn when provided', () => {
        const criteria = 'Mobile Add';
        const mockCallback = jasmine.createSpy('callback spy');

        pipe.transform(mockItems, criteria, mockKeys, mockCallback);
        expect(mockCallback).toHaveBeenCalled();
        // AngularJS
        mockCallback.calls.reset();
        filter(mockItems, criteria, mockKeys, mockCallback);
        expect(mockCallback).toHaveBeenCalled();
    });

    it('should call the callbackfcn when the criteria is null', () => {
        const criteria = '';
        const mockCallback = jasmine.createSpy('callback spy');

        pipe.transform(mockItems, criteria, mockKeys, mockCallback);
        expect(mockCallback).toHaveBeenCalled();
        // AngularJS
        mockCallback.calls.reset();
        filter(mockItems, criteria, mockKeys, mockCallback);
        expect(mockCallback).toHaveBeenCalled();
    });
});
