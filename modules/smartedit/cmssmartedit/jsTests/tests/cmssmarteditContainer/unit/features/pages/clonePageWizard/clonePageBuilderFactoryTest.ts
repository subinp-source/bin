/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { promiseHelper } from 'testhelpers';

import { ClonePageBuilderFactory, IClonePageBuilder } from 'cmssmarteditcontainer/services';
import { CmsApprovalStatus, CMSPageStatus, CMSRestriction, ICMSPage } from 'cmscommons';
import {
    GenericEditorStructure,
    ICatalogService,
    ICatalogVersion,
    IPageInfoService
} from 'smarteditcommons';

function mockPage(): ICMSPage {
    return {
        uuid: '',
        uid: '',
        creationtime: new Date(0),
        modifiedtime: new Date(0),
        pk: '',
        masterTemplate: '',
        masterTemplateId: '',
        name: '',
        label: '',
        typeCode: '',
        pageUuid: '',
        itemtype: '',
        type: '',
        catalogVersion: '',
        pageStatus: CMSPageStatus.ACTIVE,
        approvalStatus: CmsApprovalStatus.APPROVED,
        displayStatus: '',
        title: null,
        defaultPage: null,
        restrictions: [],
        homepage: null
    };
}
describe('clonePageBuilderFactory', () => {
    let ClonePageBuilder: new (...args: any[]) => IClonePageBuilder;
    let pageInfo: ICMSPage;
    let newPageInfo: ICMSPage;
    let pageBuilder: IClonePageBuilder;

    let cmsitemsRestService: jasmine.SpyObj<any>;
    let mockpageInfoService: jasmine.SpyObj<IPageInfoService>;
    let mockContextAwarePageStructureService: jasmine.SpyObj<any>;
    let mockTypeStructureRestService: jasmine.SpyObj<any>;
    let restrictionsStepHandlerFactory: jasmine.SpyObj<any>;
    let catalogService: jasmine.SpyObj<ICatalogService>;

    const PageStructureMocks = require('../../common/pageStructureMocks');

    const restrictionsStepHandler = {
        hideStep() {
            //
        },
        showStep() {
            //
        }
    };

    const primaryPageDisplayConditionData = {
        ...mockPage(),
        isPrimary: true
    } as ICMSPage;

    const variationPageDisplayConditionData = {
        ...mockPage(),
        isPrimary: false,
        primaryPage: {
            label: 'newPrimaryLabel'
        }
    } as ICMSPage;

    const restrictionsData = [
        {
            uid: 'restrictionId1',
            uuid: 'restrictionUuid1',
            name: '',
            type: null,
            typeCode: '',
            description: '',
            language: null
        } as CMSRestriction,
        {
            uid: 'restrictionId2',
            uuid: 'restrictionUuid2',
            name: '',
            type: null,
            typeCode: '',
            description: '',
            language: null
        } as CMSRestriction
    ];

    beforeEach((done) => {
        pageInfo = {
            ...mockPage(),
            pk: 'some pk',
            masterTemplate: 'PageTemplateUuid',
            masterTemplateId: 'PageTemplateUid',
            name: 'pageName',
            label: 'pageLabel',
            typeCode: 'pageTypeCode',
            uid: 'pageUid',
            uuid: 'somePageUUID',
            itemtype: 'pageType'
        } as ICMSPage;

        newPageInfo = {
            ...mockPage(),
            pk: 'some pk',
            masterTemplate: 'PageTemplateUuid',
            masterTemplateId: 'PageTemplateUid',
            name: 'pageName',
            label: 'pageLabel',
            typeCode: 'pageTypeCode',
            uid: 'pageUid',
            pageUuid: 'somePageUUID',
            itemtype: 'pageType',
            template: 'PageTemplateUid',
            type: 'somePageType',
            catalogVersion: 'someCatalogVersionUUID'
        } as ICMSPage;

        mockpageInfoService = jasmine.createSpyObj('pageInfoService', ['getPageUUID']);
        mockpageInfoService.getPageUUID.and.returnValue(Promise.resolve('somePageUUID'));

        cmsitemsRestService = jasmine.createSpyObj('cmsitemsRestService', ['getById']);
        mockContextAwarePageStructureService = jasmine.createSpyObj(
            'mockContextAwarePageStructureService',
            ['getPageStructureForNewPage']
        );
        mockTypeStructureRestService = jasmine.createSpyObj('mockTypeStructureRestService', [
            'getStructureByTypeAndMode'
        ]);
        mockContextAwarePageStructureService.getPageStructureForNewPage.and.returnValue(
            Promise.resolve(PageStructureMocks.getFields())
        );
        mockTypeStructureRestService.getStructureByTypeAndMode.and.returnValue(
            Promise.resolve({
                type: 'somePageType'
            })
        );
        restrictionsStepHandlerFactory = jasmine.createSpyObj('restrictionsStepHandlerFactory', [
            'createRestrictionsStepHandler'
        ]);
        restrictionsStepHandlerFactory.createRestrictionsStepHandler.and.returnValue(
            restrictionsStepHandler
        );

        catalogService = jasmine.createSpyObj('catalogService', ['getCatalogVersionUUid']);

        catalogService.getCatalogVersionUUid.and.returnValue('someCatalogVersionUUID');

        cmsitemsRestService.getById.and.callFake((itemId: string) => {
            if (itemId === 'somePageUUID') {
                return Promise.resolve(pageInfo);
            }
            return Promise.resolve({});
        });

        ClonePageBuilder = ClonePageBuilderFactory(
            promiseHelper.$q(),
            mockContextAwarePageStructureService,
            mockpageInfoService,
            mockTypeStructureRestService,
            cmsitemsRestService,
            catalogService
        );

        pageBuilder = new ClonePageBuilder(restrictionsStepHandler);

        (pageBuilder as any).init().then(() => done());
    });

    it('WHEN basePageUid is not passed THEN clonePageBuilder will call pageInfoService to fetch pageUid and then fetch page details', () => {
        expect(cmsitemsRestService.getById).toHaveBeenCalledWith('somePageUUID');
    });

    it('WHEN clonePageBuilder is called THEN basic page information is fetch and set to the page object', () => {
        const info = { ...newPageInfo };
        delete info.uuid;

        expect(pageBuilder.getPageInfo()).toEqual(info);
        expect(pageBuilder.getPageTypeCode()).toEqual('pageTypeCode');
        expect(pageBuilder.getPageTemplate()).toEqual('PageTemplateUid');
    });

    it('WHEN displayConditionSelected for primary is called THEN page structure is fetched based on the type code', async () => {
        spyOn(restrictionsStepHandler, 'hideStep');
        spyOn(restrictionsStepHandler, 'showStep');

        await pageBuilder.displayConditionSelected(primaryPageDisplayConditionData);

        expect(restrictionsStepHandler.hideStep).toHaveBeenCalled();
        expect(restrictionsStepHandler.showStep).not.toHaveBeenCalled();
        expect(
            mockContextAwarePageStructureService.getPageStructureForNewPage
        ).toHaveBeenCalledWith('pageTypeCode', true);
        expect(pageBuilder.getPageInfoStructure()).toEqual(PageStructureMocks.getFields());
    });

    it('WHEN displayConditionSelected for variation is called THEN clone page label is set to the selected primaryPage and the page structure is fetched based on the type code', async () => {
        spyOn(restrictionsStepHandler, 'hideStep');
        spyOn(restrictionsStepHandler, 'showStep');

        await pageBuilder.displayConditionSelected(variationPageDisplayConditionData);

        expect(pageBuilder.getPageInfo().label).toEqual('newPrimaryLabel');
        expect(restrictionsStepHandler.hideStep).not.toHaveBeenCalled();
        expect(restrictionsStepHandler.showStep).toHaveBeenCalled();
        expect(
            mockContextAwarePageStructureService.getPageStructureForNewPage
        ).toHaveBeenCalledWith('pageTypeCode', false);
        expect(pageBuilder.getPageInfoStructure()).toEqual(PageStructureMocks.getFields());
    });

    it('WHEN displayConditionSelected for primary/variation is called and if page has no typeCode THEN page structure is set to empty', () => {
        delete (pageBuilder as any).pageData.typeCode;

        pageBuilder.displayConditionSelected(({ isPrimary: true } as unknown) as ICMSPage);

        expect(
            mockContextAwarePageStructureService.getPageStructureForNewPage
        ).not.toHaveBeenCalled();
        expect(pageBuilder.getPageInfoStructure()).toEqual({} as GenericEditorStructure);
    });

    it('WHEN componentCloneOptionSelected is called with "clone" option THEN getComponentCloneOption will return "clone" ', () => {
        pageBuilder.componentCloneOptionSelected('clone');

        expect(pageBuilder.getComponentCloneOption()).toEqual('clone');
    });

    it('WHEN componentCloneOptionSelected is called with "reference" option THEN getComponentCloneOption will return "reference" ', () => {
        pageBuilder.componentCloneOptionSelected('reference');

        expect(pageBuilder.getComponentCloneOption()).toEqual('reference');
    });

    it('WHEN restrictionsSelected is called with onlyOneRestrictionMustApply and list of restrictions THEN corresponding values are set to the page object ', () => {
        const info = {
            ...newPageInfo,
            restrictions: restrictionsData,
            onlyOneRestrictionMustApply: true
        };
        delete info.uuid;

        pageBuilder.restrictionsSelected(true, restrictionsData);

        expect(pageBuilder.getPageRestrictions()).toEqual(restrictionsData);
        expect(pageBuilder.getPageInfo()).toEqual(info);
    });

    it('WHEN getPageProperties is called THEN it return basic page info such as type, typeCode, template and onlyOneRestrictionMustApply', () => {
        pageBuilder.restrictionsSelected(false, []);

        expect(pageBuilder.getPageProperties()).toEqual({
            type: 'somePageType',
            typeCode: 'pageTypeCode',
            template: 'PageTemplateUid',
            catalogVersion: 'someCatalogVersionUUID',
            onlyOneRestrictionMustApply: false
        });
    });

    it('WHEN targetCatalogVersionSelected is called with selected catalog version THEN catalogVersion value is set in the page object', () => {
        const catalogVersion = {
            uuid: 'someCatalogVersionUUID'
        } as ICatalogVersion;

        pageBuilder.onTargetCatalogVersionSelected(catalogVersion);

        expect(pageBuilder.getTargetCatalogVersion()).toEqual(catalogVersion);
        expect(pageBuilder.getPageInfo().catalogVersion).toEqual(catalogVersion.uuid);
    });
});
