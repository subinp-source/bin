/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import * as angular from 'angular';

import { IBaseCatalog, ICatalogService, IModalService, SystemEventService } from 'smarteditcommons';
import { ProductCatalogVersionsSelectorComponent } from './productCatalogVersionsSelector';

describe('seProductCatalogVersionsSelector controller', () => {
    let controller: ProductCatalogVersionsSelectorComponent;
    let catalogService: jasmine.SpyObj<ICatalogService>;
    let modalService: jasmine.SpyObj<IModalService>;
    let systemEventService: jasmine.SpyObj<SystemEventService>;
    let $translate: jasmine.SpyObj<angular.translate.ITranslateService>;
    let l10n: jasmine.Spy;

    const VERSION1 = 'version1';
    const VERSION2 = 'version2';
    const CATALOG_VERSION_UUID1 = 'catalog1Version/Online';
    const CATALOG_VERSION_UUID2 = 'catalog2Version/Online';

    const DUMMY_CATALOG_SINGLE_RESULT = [
        {
            catalogId: 'catalog1',
            versions: [
                {
                    version: VERSION1,
                    active: true,
                    uuid: CATALOG_VERSION_UUID1
                },
                {
                    version: VERSION2,
                    active: false,
                    uuid: CATALOG_VERSION_UUID2
                }
            ]
        }
    ];

    const DUMMY_MULTIPLE_CATALOGS_RESULT = [
        {
            catalogId: 'catalog1',
            versions: [
                {
                    version: VERSION1,
                    active: true,
                    uuid: CATALOG_VERSION_UUID1
                },
                {
                    version: VERSION2,
                    active: false,
                    uuid: CATALOG_VERSION_UUID2
                }
            ]
        },
        {
            catalogId: 'catalog2',
            versions: [
                {
                    version: VERSION1,
                    active: true,
                    uuid: CATALOG_VERSION_UUID1
                },
                {
                    version: VERSION2,
                    active: false,
                    uuid: CATALOG_VERSION_UUID2
                }
            ]
        }
    ];

    const PARSED_VERSION_ID_LABEL = [
        {
            id: CATALOG_VERSION_UUID1,
            label: VERSION1
        },
        {
            id: CATALOG_VERSION_UUID2,
            label: VERSION2
        }
    ];

    const l10nFilterFunction = () => {
        return 'catalogName';
    };

    beforeEach(() => {
        angular.mock.module('yLoDashModule');
    });

    beforeEach(() => {
        catalogService = jasmine.createSpyObj('catalogService', ['getProductCatalogsForSite']);

        modalService = jasmine.createSpyObj('modalService', ['open']);

        l10n = jasmine.createSpy('l10n');
        l10n.and.callFake(l10nFilterFunction);

        $translate = jasmine.createSpyObj('$translate', ['instant']);
        $translate.instant.and.callFake((str: string) => {
            return '_' + str;
        });

        systemEventService = jasmine.createSpyObj('systemEventService', ['subscribe']);
        controller = new ProductCatalogVersionsSelectorComponent(
            $translate,
            l10n,
            catalogService,
            modalService,
            systemEventService
        );

        (controller as any).model = {
            catalogDescriptor: {
                siteId: 'electronics'
            },
            previewCatalog: 'electronics_electyronics-catalog_online'
        };
    });

    it('should set single product catalog version select template when only one product catalog is available for site', () => {
        // Given
        (controller as any).model.catalogVersions = [CATALOG_VERSION_UUID1];
        catalogService.getProductCatalogsForSite.and.returnValue(
            Promise.resolve(DUMMY_CATALOG_SINGLE_RESULT)
        );

        // When
        controller.$onInit();

        // Then
        expect(controller.isSingleVersionSelector).toBe(true);
        expect(controller.isMultiVersionSelector).toBe(false);
    });

    it('should set fetchStrategy.fetchOptions to be set for single product catalog version', async (done) => {
        // Given
        (controller as any).model.catalogVersions = [CATALOG_VERSION_UUID1];
        catalogService.getProductCatalogsForSite.and.returnValue(
            Promise.resolve(DUMMY_CATALOG_SINGLE_RESULT)
        );

        // When
        await controller.$onInit();

        const productCatalogVersionsPromise = controller.fetchStrategy.fetchAll();

        // Then
        expect(controller.fetchStrategy.fetchAll).toEqual(jasmine.any(Function));
        expect(await productCatalogVersionsPromise).toEqual(PARSED_VERSION_ID_LABEL);

        done();
    });

    it('should set multiple product catalog version select template when only one product catalog is available for site', async (done) => {
        // Given
        (controller as any).model.catalogVersions = [CATALOG_VERSION_UUID1, CATALOG_VERSION_UUID2];
        catalogService.getProductCatalogsForSite.and.returnValue(
            Promise.resolve(DUMMY_MULTIPLE_CATALOGS_RESULT)
        );

        // When
        await controller.$onInit();

        // Then
        expect(controller.isSingleVersionSelector).toBe(false);
        expect(controller.isMultiVersionSelector).toBe(true);

        done();
    });

    it('should map product catalogs versions to id and label fields', () => {
        // Given
        const mappedVersionsExpected = [
            {
                id: CATALOG_VERSION_UUID1,
                label: VERSION1
            },
            {
                id: CATALOG_VERSION_UUID2,
                label: VERSION2
            }
        ];

        // When
        const mappedVersions = controller.parseSingleCatalogVersion(
            (DUMMY_CATALOG_SINGLE_RESULT[0] as unknown) as IBaseCatalog
        );

        // Then
        expect(mappedVersions).toEqual(mappedVersionsExpected);
    });
});
