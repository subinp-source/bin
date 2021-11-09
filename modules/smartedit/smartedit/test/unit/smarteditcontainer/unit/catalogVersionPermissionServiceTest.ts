/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    CatalogService,
    CatalogVersionPermission,
    CatalogVersionPermissionRestService,
    CatalogVersionPermissionService
} from 'smarteditcontainer/services';

describe('catalogVersionPermissionRestServiceModule', () => {
    let catalogService: jasmine.SpyObj<CatalogService>;
    let catalogVersionPermissionRestService: jasmine.SpyObj<CatalogVersionPermissionRestService>;
    let catalogVersionPermissionService: CatalogVersionPermissionService;

    const uriContext = {
        staged: {
            CURRENT_CONTEXT_CATALOG: 'fakeCatalogId',
            CURRENT_CONTEXT_CATALOG_VERSION: 'Staged',
            CURRENT_CONTEXT_SITE_ID: 'SiteId'
        },
        active: {
            CURRENT_CONTEXT_CATALOG: 'fakeCatalogId',
            CURRENT_CONTEXT_CATALOG_VERSION: 'SomeActiveCatalogVersion',
            CURRENT_CONTEXT_SITE_ID: 'SiteId'
        }
    };

    const catalogVersionPermissionRestServiceMeaningfulResult: CatalogVersionPermission = {
        catalogId: null,
        catalogVersion: null,
        permissions: [
            {
                key: 'read',
                value: 'true'
            },
            {
                key: 'write',
                value: 'false'
            }
        ],
        syncPermissions: [
            {
                canSynchronize: true,
                targetCatalogVersion: 'SomeActiveCatalogVersion'
            }
        ]
    };

    const catalogVersionPermissionRestServiceAllFalse: CatalogVersionPermission = {
        catalogId: null,
        catalogVersion: null,
        permissions: [
            {
                key: 'read',
                value: 'false'
            },
            {
                key: 'write',
                value: 'false'
            }
        ],
        syncPermissions: null
    };

    beforeEach(() => {
        catalogVersionPermissionRestService = jasmine.createSpyObj(
            'catalogVersionPermissionRestService',
            ['getCatalogVersionPermissions']
        );
        catalogService = jasmine.createSpyObj('catalogService', [
            'retrieveUriContext',
            'getActiveContentCatalogVersionByCatalogId'
        ]);
        catalogVersionPermissionService = new CatalogVersionPermissionService(
            catalogVersionPermissionRestService,
            catalogService
        );
    });

    it('GIVEN catalogVersionPermissionRestService that returns empty object WHEN hasReadPermissionOnCurrent/hasWritePermissionOnCurrent is called  THEN it returns false', async () => {
        // GIVEN
        catalogVersionPermissionRestService.getCatalogVersionPermissions.and.returnValue(
            Promise.resolve({})
        );
        catalogService.retrieveUriContext.and.returnValue(Promise.resolve(uriContext.staged));
        catalogService.getActiveContentCatalogVersionByCatalogId.and.returnValue(
            Promise.resolve(uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION)
        );

        // WHEN
        const hasReadPermissionOnCurrent = await catalogVersionPermissionService.hasReadPermissionOnCurrent();
        const hasWritePermissionOnCurrent = await catalogVersionPermissionService.hasWritePermissionOnCurrent();

        // THEN
        expect(hasReadPermissionOnCurrent).toBe(false);
        expect(hasWritePermissionOnCurrent).toBe(false);
    });

    it('GIVEN catalogVersionPermissionRestService that returns empty permissions list WHEN hasReadPermission/hasWritePermissionOnCurrent is called  THEN it returns false', async () => {
        // GIVEN
        catalogVersionPermissionRestService.getCatalogVersionPermissions.and.returnValue(
            Promise.resolve({
                permissions: []
            })
        );
        catalogService.getActiveContentCatalogVersionByCatalogId.and.returnValue(
            Promise.resolve(uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION)
        );

        // WHEN
        const hasReadPermission = await catalogVersionPermissionService.hasReadPermission(
            uriContext.staged.CURRENT_CONTEXT_CATALOG,
            uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
        );
        const hasWritePermission = await catalogVersionPermissionService.hasWritePermission(
            uriContext.staged.CURRENT_CONTEXT_CATALOG,
            uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
        );

        // THEN
        expect(hasReadPermission).toBe(false);
        expect(hasWritePermission).toBe(false);
    });

    describe('CurrentCatalogPermission', () => {
        beforeEach(() => {
            catalogVersionPermissionRestService.getCatalogVersionPermissions.and.returnValue(
                Promise.resolve(catalogVersionPermissionRestServiceMeaningfulResult)
            );
            catalogService.retrieveUriContext.and.returnValue(Promise.resolve(uriContext.staged));
            catalogService.getActiveContentCatalogVersionByCatalogId.and.returnValue(
                Promise.resolve(uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION)
            );
        });

        it('WHEN hasWritePermissionOnCurrent is called THEN it retrieves catalog data from experience and calls catalogVersionPermissionRestService to get write permission for the catalog', async () => {
            // WHEN
            const hasWritePermissionOnCurrent = await catalogVersionPermissionService.hasWritePermissionOnCurrent();

            // THEN
            expect(
                catalogVersionPermissionRestService.getCatalogVersionPermissions
            ).toHaveBeenCalledWith(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
            );
            expect(hasWritePermissionOnCurrent).toBe(false);
        });

        it('WHEN hasReadPermissionOnCurrent is called THEN it retrieves catalog data from experience and then calls catalogVersionPermissionRestService to get read permission for the catalog', async () => {
            // WHEN
            const hasReadPermissionOnCurrent = await catalogVersionPermissionService.hasReadPermissionOnCurrent();

            // THEN
            expect(
                catalogVersionPermissionRestService.getCatalogVersionPermissions
            ).toHaveBeenCalledWith(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
            );
            expect(hasReadPermissionOnCurrent).toBe(true);
        });

        it('WHEN hasReadPermissionOnCurrent is called and the current catalog is active catalog THEN it retrieves catalog data from experience and then calls catalogVersionPermissionRestService to get read permission for the catalog', async () => {
            // GIVEN
            catalogVersionPermissionRestService.getCatalogVersionPermissions.and.returnValue(
                Promise.resolve(catalogVersionPermissionRestServiceAllFalse)
            );
            catalogService.retrieveUriContext.and.returnValue(Promise.resolve(uriContext.active));

            // WHEN
            const hasReadPermissionOnCurrent = await catalogVersionPermissionService.hasReadPermissionOnCurrent();

            // THEN
            expect(
                catalogVersionPermissionRestService.getCatalogVersionPermissions
            ).toHaveBeenCalledWith(
                uriContext.active.CURRENT_CONTEXT_CATALOG,
                uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION
            );
            expect(catalogService.getActiveContentCatalogVersionByCatalogId).toHaveBeenCalledWith(
                uriContext.active.CURRENT_CONTEXT_CATALOG
            );
            expect(hasReadPermissionOnCurrent).toBe(true);
        });
    });

    describe('AnyCatalogPermission', () => {
        beforeEach(() => {
            catalogVersionPermissionRestService.getCatalogVersionPermissions.and.returnValue(
                Promise.resolve(catalogVersionPermissionRestServiceMeaningfulResult)
            );
        });

        it('WHEN hasReadPermission is called THEN calls catalogVersionPermissionRestService to get read permission for the catalogId and catalogVersion', async () => {
            // WHEN
            const hasReadPermission = await catalogVersionPermissionService.hasReadPermission(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
            );

            // THEN
            expect(
                catalogVersionPermissionRestService.getCatalogVersionPermissions
            ).toHaveBeenCalledWith(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
            );
            expect(hasReadPermission).toBe(true);
        });

        it('WHEN hasWritePermission is called THEN calls catalogVersionPermissionRestService to get write permission for the catalogId and catalogVersion', async () => {
            // WHEN
            const hasWritePermission = await catalogVersionPermissionService.hasWritePermission(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
            );

            // THEN
            expect(
                catalogVersionPermissionRestService.getCatalogVersionPermissions
            ).toHaveBeenCalledWith(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
            );
            expect(hasWritePermission).toBe(false);
        });
    });

    describe('SyncCatalogPermission', () => {
        beforeEach(() => {
            catalogVersionPermissionRestService.getCatalogVersionPermissions.and.returnValue(
                Promise.resolve(catalogVersionPermissionRestServiceMeaningfulResult)
            );
        });
        it('WHEN hasSyncPermission is called THEN calls catalogVersionPermissionRestService to get sync permission for the catalogId, sourceCatalogVersion and targetCatalogVersion', async () => {
            // WHEN
            const hasSyncPermission = await catalogVersionPermissionService.hasSyncPermission(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION,
                uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION
            );

            // THEN
            expect(
                catalogVersionPermissionRestService.getCatalogVersionPermissions
            ).toHaveBeenCalledWith(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
            );

            expect(hasSyncPermission).toBe(true);
        });

        it('GIVEN catalogVersionPermissionRestService that returns empty permissions list WHEN hasSyncPermission is called THEN it returns false', async () => {
            // GIVEN
            catalogVersionPermissionRestService.getCatalogVersionPermissions.and.returnValue(
                Promise.resolve({
                    syncPermissions: [{}]
                })
            );

            // WHEN
            const hasSyncPermission = await catalogVersionPermissionService.hasSyncPermission(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION,
                uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION
            );

            // THEN
            expect(hasSyncPermission).toBe(false);
        });

        it('GIVEN catalogVersionPermissionRestService that returns permissions list containing different target catalog version than expected WHEN hasSyncPermission is called THEN it returns false', async () => {
            // GIVEN
            catalogVersionPermissionRestService.getCatalogVersionPermissions.and.returnValue(
                Promise.resolve({
                    syncPermissions: [
                        {
                            canSynchronize: true,
                            targetCatalogVersion: 'DifferentCatalogVersion'
                        }
                    ]
                })
            );

            // WHEN
            const hasSyncPermission = await catalogVersionPermissionService.hasSyncPermission(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION,
                uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION
            );

            // THEN
            expect(hasSyncPermission).toBe(false);
        });

        it('GIVEN catalogVersionPermissionRestService that returns permissions list containing canSynchronize false WHEN hasSyncPermission is called THEN it returns false', async () => {
            // GIVEN
            catalogVersionPermissionRestService.getCatalogVersionPermissions.and.returnValue(
                Promise.resolve({
                    syncPermissions: [
                        {
                            canSynchronize: false,
                            targetCatalogVersion: 'SomeActiveCatalogVersion'
                        }
                    ]
                })
            );

            // WHEN
            const hasSyncPermission = await catalogVersionPermissionService.hasSyncPermission(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION,
                uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION
            );

            // THEN
            expect(hasSyncPermission).toBe(false);
        });

        it('GIVEN catalogVersionPermissionRestService that returns permissions list WHEN hasSyncPermissionFromCurrentToActiveCatalogVersion is called THEN it returns true', async () => {
            // GIVEN
            catalogService.retrieveUriContext.and.returnValue(Promise.resolve(uriContext.staged));
            spyOn(
                catalogVersionPermissionService,
                'hasSyncPermissionToActiveCatalogVersion'
            ).and.callThrough();
            spyOn(catalogVersionPermissionService, 'hasSyncPermission').and.callThrough();
            catalogService.getActiveContentCatalogVersionByCatalogId.and.returnValue(
                Promise.resolve(uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION)
            );

            // WHEN
            const hasSyncPermissionFromCurrentToActiveCatalogVersion = await catalogVersionPermissionService.hasSyncPermissionFromCurrentToActiveCatalogVersion();

            // THEN
            expect(hasSyncPermissionFromCurrentToActiveCatalogVersion).toBe(true);
            expect(
                catalogVersionPermissionService.hasSyncPermissionToActiveCatalogVersion
            ).toHaveBeenCalledWith(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION
            );
            expect(catalogService.getActiveContentCatalogVersionByCatalogId).toHaveBeenCalledWith(
                uriContext.staged.CURRENT_CONTEXT_CATALOG
            );
            expect(catalogVersionPermissionService.hasSyncPermission).toHaveBeenCalledWith(
                uriContext.staged.CURRENT_CONTEXT_CATALOG,
                uriContext.staged.CURRENT_CONTEXT_CATALOG_VERSION,
                uriContext.active.CURRENT_CONTEXT_CATALOG_VERSION
            );
        });
    });
});
