/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { DisplayConditionsEditorModel } from 'cmssmarteditcontainer/services/pageDisplayConditions/displayConditionsEditorModel';
import {
    DisplayConditionsFacade,
    IDisplayConditionsPrimaryPage
} from 'cmssmarteditcontainer/facades/displayConditionsFacade';
import { SystemEventService } from 'smarteditcommons';

describe('displayConditionsEditorModel', () => {
    const MOCK_PRIMARY_PAGE_INFO = {
        pageName: 'Some Page Name',
        pageType: 'somePageType',
        isPrimary: true
    };

    const MOCK_VARIATION_PAGE_INFO = {
        pageName: 'Some Page Name',
        pageType: 'somePageType',
        isPrimary: false
    };

    const MOCK_VARIATIONS = [
        {
            pageName: 'Some Variation Page Name',
            creationDate: new Date().toString(),
            restrictions: 1
        },
        {
            pageName: 'Some Other Variation Page Name',
            creationDate: new Date().toString(),
            restrictions: 2
        }
    ];

    const MOCK_PRIMARY_PAGE = {
        uid: 'somePrimaryPage',
        uuid: 'somePrimaryPage',
        name: 'Some Primary Page',
        label: 'some-primary-page'
    };

    const ANOTHER_MOCK_PRIMARY_PAGE = {
        uid: 'someOtherPrimaryPage',
        name: 'Some Other Primary Page',
        label: 'some-other-primary-page'
    };

    let service: DisplayConditionsEditorModel;
    let displayConditionsFacade: jasmine.SpyObj<DisplayConditionsFacade>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;

    beforeEach(() => {
        displayConditionsFacade = jasmine.createSpyObj<DisplayConditionsFacade>(
            'displayConditionsFacade',
            [
                'getVariationsForPageUid',
                'getPrimaryPageForVariationPage',
                'getPageInfoForPageUid',
                'updatePage'
            ]
        );

        systemEventService = jasmine.createSpyObj('systemEventService', ['publishAsync']);

        service = new DisplayConditionsEditorModel(displayConditionsFacade, systemEventService);
    });

    describe('initModel', () => {
        beforeEach(() => {
            (service as any)._initModelForPrimary = jasmine.createSpy('_initModelForPrimary');
            (service as any)._initModelForVariation = jasmine.createSpy('_initModelForVariation');
        });

        it('should put the page name, page, type, and is primary values on the model scope', () => {
            displayConditionsFacade.getPageInfoForPageUid.and.returnValue(
                Promise.resolve(MOCK_PRIMARY_PAGE_INFO)
            );

            service.initModel('somePageUid').then(() => {
                expect(service.pageName).toBe('Some Page Name');
                expect(service.pageType).toBe('somePageType');
                expect(service.isPrimary).toBe(true);
            });
        });

        it('should delegate to _initModelForPrimary if the page is primary', () => {
            displayConditionsFacade.getPageInfoForPageUid.and.returnValue(
                Promise.resolve(MOCK_PRIMARY_PAGE_INFO)
            );
            service.initModel('somePageUid').then(() => {
                expect((service as any)._initModelForPrimary).toHaveBeenCalledWith('somePageUid');
            });
        });

        it('should delegate to _initModelForVariation if the page is variation', () => {
            displayConditionsFacade.getPageInfoForPageUid.and.returnValue(
                Promise.resolve(MOCK_VARIATION_PAGE_INFO)
            );
            service.initModel('somePageUid').then(() => {
                expect((service as any)._initModelForVariation).toHaveBeenCalledWith('somePageUid');
            });
        });
    });

    describe('_initModelForPrimary', () => {
        beforeEach(() => {
            displayConditionsFacade.getVariationsForPageUid.and.returnValue(
                Promise.resolve(MOCK_VARIATIONS)
            );
        });

        it('should put the variations on the model scope', () => {
            (service as any)._initModelForPrimary('somePageUid').then(() => {
                expect(service.variations).toEqual(MOCK_VARIATIONS);
            });
        });
    });

    describe('_initModelForVariation', () => {
        beforeEach(() => {
            displayConditionsFacade.getPrimaryPageForVariationPage.and.returnValue(
                Promise.resolve(MOCK_PRIMARY_PAGE)
            );
        });

        it('should put the associated primary page on the model scope', () => {
            (service as any)._initModelForVariation('somePageUid').then(() => {
                expect(service.associatedPrimaryPage).toEqual(MOCK_PRIMARY_PAGE);
                expect(service.originalPrimaryPage).toEqual(MOCK_PRIMARY_PAGE);
            });
        });
    });

    describe('setAssociatedPrimaryPage', () => {
        it('should put the associated primary page on the scope', () => {
            service.setAssociatedPrimaryPage(MOCK_PRIMARY_PAGE);
            expect(service.associatedPrimaryPage).toEqual(MOCK_PRIMARY_PAGE);
        });
    });

    describe('save', () => {
        let isDirty: jasmine.Spy;

        beforeEach(() => {
            isDirty = spyOn(service, 'isDirty');
            service.pageUid = 'somePageUid';
            service.associatedPrimaryPage = {
                label: 'some-label-to-save'
            } as IDisplayConditionsPrimaryPage;
            displayConditionsFacade.updatePage.and.returnValue(Promise.resolve(true));
        });

        it('should delegate to the facade to update page if dirty', () => {
            isDirty.and.returnValue(true);
            expect(service.save()).toBeResolvedWithData(true);
            expect(displayConditionsFacade.updatePage).toHaveBeenCalledWith('somePageUid', {
                label: 'some-label-to-save'
            });
        });

        it('should not delegate to the facade if not dirty', () => {
            isDirty.and.returnValue(false);
            expect(service.save()).toBeResolvedWithData(true);
            expect(displayConditionsFacade.updatePage).not.toHaveBeenCalled();
        });
    });

    describe('isDirty', () => {
        it('should return false if there is no original primary page on the model', () => {
            service.originalPrimaryPage = undefined;
            service.associatedPrimaryPage = ANOTHER_MOCK_PRIMARY_PAGE as IDisplayConditionsPrimaryPage;
            expect(service.isDirty()).toBe(false);
        });

        it('should return false if there is no associated primary page on the model', () => {
            service.originalPrimaryPage = MOCK_PRIMARY_PAGE;
            service.associatedPrimaryPage = undefined;
            expect(service.isDirty()).toBe(false);
        });

        it('should return false if the associated primary page and original primary page are the same', () => {
            service.originalPrimaryPage = MOCK_PRIMARY_PAGE;
            service.associatedPrimaryPage = MOCK_PRIMARY_PAGE;
            expect(service.isDirty()).toBe(false);
        });

        it('should return false if the associated primary page and original primary page are different', () => {
            service.originalPrimaryPage = MOCK_PRIMARY_PAGE;
            service.associatedPrimaryPage = ANOTHER_MOCK_PRIMARY_PAGE as IDisplayConditionsPrimaryPage;
            expect(service.isDirty()).toBe(true);
        });
    });
});
