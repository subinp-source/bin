/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IContentCatalog } from 'fixtures/entities/catalogs';

export const productCatalogGlobal: IContentCatalog = {
    catalogId: 'apparelProductCatalog',
    name: {
        en: 'Apparel Product Catalog',
        de: 'Produktkatalog Kleidung'
    },
    versions: [
        {
            active: false,
            version: 'Staged'
        },
        {
            active: true,
            version: 'Online'
        }
    ]
};
