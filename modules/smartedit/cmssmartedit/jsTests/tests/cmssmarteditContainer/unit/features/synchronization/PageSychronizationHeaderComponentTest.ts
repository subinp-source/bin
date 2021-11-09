/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ICatalogService, ISharedDataService, TypedMap } from 'smarteditcommons';
import { PageSynchronizationHeaderComponent } from 'cmssmarteditcontainer/components/synchronize/pages/PageSynchronizationHeaderComponent';
import { promiseHelper } from 'testhelpers';

describe('Page Synchronization Header Component Test', () => {
    let pageSynchronizationHeaderComponent: PageSynchronizationHeaderComponent = null;

    let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
    let sharedDataService: jasmine.SpyObj<ISharedDataService>;
    let catalogService: jasmine.SpyObj<ICatalogService>;
    let l10nFilter: (s: TypedMap<string>) => string;
    let cmsitemsRestService: jasmine.SpyObj<any>;
    let $q: jasmine.SpyObj<angular.IQService>;

    const setSyncConditions = (
        canSyncHomepage: boolean = false,
        pageHasSyncStatus: boolean = false,
        pageHasNoDepOrNoSyncStatus: boolean = false,
        pageHasUnavailableDependencies: boolean = false
    ) => {
        pageSynchronizationHeaderComponent.pageSyncConditions = {
            canSyncHomepage,
            pageHasSyncStatus,
            pageHasNoDepOrNoSyncStatus,
            pageHasUnavailableDependencies
        };
    };

    const setSyncStatusUnavailableDeps = (unavailableDependencies: any = []) => {
        (pageSynchronizationHeaderComponent as any).syncStatus = {
            unavailableDependencies
        };
    };

    beforeEach(() => {
        $q = promiseHelper.$q();

        $translate = jasmine.createSpyObj<angular.translate.ITranslateService>('$translate', [
            'instant'
        ]);
        $translate.instant.and.callFake(() => 'translation');

        sharedDataService = jasmine.createSpyObj<ISharedDataService>('sharedDataService', ['get']);
        catalogService = jasmine.createSpyObj<ICatalogService>('catalogService', [
            'getActiveContentCatalogVersionByCatalogId'
        ]);
        l10nFilter = (s: TypedMap<string>) => s.en;
        cmsitemsRestService = jasmine.createSpyObj<any>('cmsitemsRestService', ['getByIds']);

        pageSynchronizationHeaderComponent = new PageSynchronizationHeaderComponent(
            $q,
            $translate,
            sharedDataService,
            catalogService,
            l10nFilter,
            cmsitemsRestService
        );
    });

    it('should display the right help message if the page has dependencies and a sync status', function() {
        setSyncConditions(false, false, false, false);
        setSyncStatusUnavailableDeps();

        pageSynchronizationHeaderComponent.$onChanges();

        expect($translate.instant).toHaveBeenCalledWith('se.cms.synchronization.page.header.help');
        expect(sharedDataService.get).not.toHaveBeenCalled();

        expect(pageSynchronizationHeaderComponent.helpTemplate).toBe('<span>translation</span>');
    });

    it('should display the description when the sync page ', function() {
        setSyncConditions(false, false, true, true);
        setSyncStatusUnavailableDeps([
            {
                itemId: 'ITEM_ID_1'
            },
            {
                itemId: 'ITEM_ID_2'
            }
        ]);

        sharedDataService.get.and.returnValue(
            $q.resolve({
                pageContext: {
                    catalogId: 'CATALOG_ID',
                    catalogName: { en: 'CATALOG_NAME' }
                }
            })
        );
        catalogService.getActiveContentCatalogVersionByCatalogId.and.returnValue(
            $q.resolve('CATALOG_VERSION_UUID')
        );
        cmsitemsRestService.getByIds.and.returnValue(
            $q.resolve({
                response: [{ name: 'ITEM_ID_1_PAGE' }, { name: 'ITEM_ID_2_PAGE' }]
            })
        );

        pageSynchronizationHeaderComponent.$onChanges();

        expect(sharedDataService.get).toHaveBeenCalledWith('experience');
        expect(catalogService.getActiveContentCatalogVersionByCatalogId).toHaveBeenCalledWith(
            'CATALOG_ID'
        );
        expect(cmsitemsRestService.getByIds).toHaveBeenCalledWith(['ITEM_ID_1', 'ITEM_ID_2']);
        expect($translate.instant.calls.argsFor(0)[0]).toBe(
            'se.cms.synchronization.page.unavailable.items.description'
        );
        expect($translate.instant.calls.argsFor(0)[1]).toEqual({
            itemNames: 'ITEM_ID_1_PAGE, ITEM_ID_2_PAGE',
            catalogName: 'CATALOG_NAME',
            catalogVersion: 'CATALOG_VERSION_UUID'
        });
    });

    it('should display the description when the page has not been synchronization yet ', function() {
        setSyncConditions(false, false, true, false);
        setSyncStatusUnavailableDeps();

        sharedDataService.get.and.returnValue(
            $q.resolve({
                catalogDescriptor: {
                    catalogId: 'DESCRIPTOR_CATALOG_ID',
                    name: { en: 'DESCRIPTOR_CATALOG_NAME' }
                }
            })
        );
        catalogService.getActiveContentCatalogVersionByCatalogId.and.returnValue(
            $q.resolve('CATALOG_VERSION_UUID')
        );

        pageSynchronizationHeaderComponent.$onChanges();

        expect(sharedDataService.get).toHaveBeenCalledWith('experience');
        expect(catalogService.getActiveContentCatalogVersionByCatalogId).toHaveBeenCalledWith(
            'DESCRIPTOR_CATALOG_ID'
        );
        expect(cmsitemsRestService.getByIds).not.toHaveBeenCalled();
        expect($translate.instant.calls.argsFor(0)[0]).toBe(
            'se.cms.synchronization.page.new.description'
        );
        expect($translate.instant.calls.argsFor(0)[1]).toEqual({
            catalogName: 'DESCRIPTOR_CATALOG_NAME',
            catalogVersion: 'CATALOG_VERSION_UUID'
        });
    });
});
