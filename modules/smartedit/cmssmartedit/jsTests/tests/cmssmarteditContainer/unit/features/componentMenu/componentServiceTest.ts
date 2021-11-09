/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    CmsitemsRestService,
    ComponentInfo,
    ComponentService,
    CMSItem,
    CMSItemStructure,
    CONTEXT_CATALOG,
    CONTEXT_CATALOG_VERSION,
    IPageContentSlotsComponentsRestService
} from 'cmscommons';
import {
    annotationService,
    pageChangeEvictionTag,
    rarelyChangingContent,
    Cached,
    ICatalogService,
    IPageInfoService,
    IRestService,
    Page,
    RestServiceFactory
} from 'smarteditcommons';

describe('componentService', () => {
    // service under test
    let componentService: ComponentService;

    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;
    let cmsitemsRestService: jasmine.SpyObj<CmsitemsRestService>;
    let catalogService: jasmine.SpyObj<ICatalogService>;
    let restServiceForAddExistingComponent: jasmine.SpyObj<IRestService<void>>;
    let pageContentSlotsComponentsRestService: jasmine.SpyObj<
        IPageContentSlotsComponentsRestService
    >;
    let pageComponentTypesRestService: jasmine.SpyObj<IRestService<Page<CMSItemStructure>>>;
    let pageInfoService: jasmine.SpyObj<IPageInfoService>;

    beforeEach(() => {
        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);

        pageComponentTypesRestService = jasmine.createSpyObj('pageComponentTypesRestService', [
            'get'
        ]);

        restServiceForAddExistingComponent = jasmine.createSpyObj(
            'restServiceForAddExistingComponent',
            ['save']
        );

        restServiceFactory.get.and.returnValues(
            pageComponentTypesRestService,
            restServiceForAddExistingComponent
        );

        cmsitemsRestService = jasmine.createSpyObj('cmsitemsRestService', [
            'get',
            'getById',
            'create',
            'update'
        ]);

        catalogService = jasmine.createSpyObj('catalogService', ['retrieveUriContext']);

        pageInfoService = jasmine.createSpyObj('pageInfoService', ['getPageUID']);

        pageContentSlotsComponentsRestService = jasmine.createSpyObj(
            'pageContentSlotsComponentsRestService',
            ['getSlotsToComponentsMapForPageUid']
        );

        componentService = new ComponentService(
            restServiceFactory,
            cmsitemsRestService,
            catalogService,
            pageInfoService,
            pageContentSlotsComponentsRestService
        );
    });

    describe('getSupportedComponentTypesForCurrentPage() ', () => {
        let payload: any;
        let expectedResult: Page<CMSItemStructure>;

        beforeEach(() => {
            payload = {
                catalogId: 'someCatalogId',
                catalogVersion: 'someCatalogVersion',
                pageId: 'somePageId',
                pageSize: 10,
                currentPage: 1,
                mask: 'someMask'
            };

            expectedResult = {
                pagination: {
                    count: 10,
                    page: 1,
                    totalCount: 100,
                    totalPages: 10
                },
                results: [
                    {
                        attributes: [],
                        category: 'someCategory',
                        code: 'someCode',
                        i18nKey: 'someKey',
                        name: 'someName',
                        type: 'someType'
                    }
                ]
            };
            pageComponentTypesRestService.get.and.returnValue(Promise.resolve(expectedResult));
        });

        it('Retrieves the types for the current page from the pageComponentTypesRestService endpoint', async (done) => {
            // WHEN
            const result = await componentService.getSupportedComponentTypesForCurrentPage(payload);

            // THEN
            expect(pageComponentTypesRestService.get).toHaveBeenCalledWith(payload);
            expect(result).toBe(expectedResult as any);
            done();
        });

        it('checks Cached annotation on getSupportedComponentTypesForCurrentPage', async () => {
            // WHEN
            await componentService.getSupportedComponentTypesForCurrentPage(payload);

            // THEN
            const decoratorObj: any = annotationService.getMethodAnnotation(
                ComponentService,
                'getSupportedComponentTypesForCurrentPage',
                Cached
            );
            expect(decoratorObj).toEqual(
                jasmine.objectContaining([
                    {
                        actions: [rarelyChangingContent],
                        tags: [pageChangeEvictionTag]
                    }
                ])
            );
        });
    });

    describe('loadComponentItem() ', () => {
        it('delegates to the cmsitems rest service layer', async (done) => {
            const id = 'id';
            const response = { name: 'name' };

            cmsitemsRestService.getById.and.returnValue(Promise.resolve(response));

            const result = await componentService.loadComponentItem(id);

            expect(cmsitemsRestService.getById).toHaveBeenCalledWith(id);
            expect(result).toEqual(response as CMSItem);

            done();
        });
    });

    describe('updateComponent() ', () => {
        it('delegates to the rest service layer with proper data', async (done) => {
            const response = { name: 'name' };
            const payload = { name: 'name' };

            cmsitemsRestService.update.and.returnValue(Promise.resolve(response));

            const result = await componentService.updateComponent(payload as CMSItem);

            expect(cmsitemsRestService.update).toHaveBeenCalledWith(payload);
            expect(result).toEqual(response as CMSItem);

            done();
        });
    });

    describe('loadPagedComponentItems() ', () => {
        it('delegates to the rest service layer with proper data', async (done) => {
            const response = { name: 'name' };
            const uriContext = {
                CURRENT_CONTEXT_CATALOG: CONTEXT_CATALOG,
                CURRENT_CONTEXT_CATALOG_VERSION: CONTEXT_CATALOG_VERSION
            };
            const expectedInput = {
                pageSize: 1,
                currentPage: 2,
                mask: 'mask',
                sort: 'name',
                typeCode: 'AbstractCMSComponent',
                catalogId: CONTEXT_CATALOG,
                catalogVersion: CONTEXT_CATALOG_VERSION,
                itemSearchParams: ''
            };
            catalogService.retrieveUriContext.and.returnValue(Promise.resolve(uriContext));
            cmsitemsRestService.get.and.returnValue(Promise.resolve(response));

            const result = await componentService.loadPagedComponentItems(
                expectedInput.mask,
                expectedInput.pageSize,
                expectedInput.currentPage
            );

            expect(cmsitemsRestService.get).toHaveBeenCalledWith(expectedInput);
            expect(result).toEqual(response as CMSItem);

            done();
        });
    });

    describe('addExistingComponent() ', () => {
        it('delegates to the rest service layer with proper data', async (done) => {
            const response = Promise.resolve();
            const input = {
                pageId: 'pageId',
                componentId: 'componentId',
                slotId: 'slotId',
                position: 1
            };
            restServiceForAddExistingComponent.save.and.returnValue(response);

            const result = await componentService.addExistingComponent(
                input.pageId,
                input.componentId,
                input.slotId,
                input.position
            );

            expect(restServiceForAddExistingComponent.save).toHaveBeenCalledWith(input);
            expect(result).toBe(undefined);

            done();
        });
    });

    describe('createNewComponent() ', () => {
        it('delegates to the rest service layer with proper data', async (done) => {
            const response = { name: 'name' };

            const componentInput = {
                name: 'name',
                slotId: 'slotId',
                targetSlotId: 'targetSlotId',
                pageId: 'pageId',
                componentType: 'componentType',
                position: 1,
                typeCode: 'code',
                itemtype: 'type',
                catalogVersion: 'cv',
                catalogVersionUuid: 'catalogVersionUuid',
                someKey: 'key'
            };
            const componentPayload = {
                someKey: 'someValue'
            };
            const restLayerInput = {
                name: componentInput.name,
                slotId: componentInput.targetSlotId,
                pageId: componentInput.pageId,
                position: componentInput.position,
                typeCode: componentInput.componentType,
                itemtype: componentInput.componentType,
                catalogVersion: componentInput.catalogVersionUuid,
                someKey: 'someValue',
                uid: '',
                uuid: ''
            };
            cmsitemsRestService.create.and.returnValue(Promise.resolve(response));

            const result = await componentService.createNewComponent(
                componentInput as ComponentInfo,
                (componentPayload as unknown) as CMSItem
            );

            expect(cmsitemsRestService.create).toHaveBeenCalledWith(restLayerInput);
            expect(result).toEqual(response as CMSItem);

            done();
        });
    });
});
