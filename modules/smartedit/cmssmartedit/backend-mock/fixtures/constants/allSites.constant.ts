/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISite } from '../entities/sites/site.entity';
export const allSites: ISite[] = [
    {
        previewUrl: '/jsTests/e2e/storefront.html',
        name: {
            en: 'Apparel - UK'
        },
        redirectUrl: 'redirecturlApparels',
        uid: 'apparel-uk',
        contentCatalogs: ['apparelContentCatalog', 'apparel-ukContentCatalog']
    },
    {
        previewUrl: '/jsTests/e2e/storefront.html',
        name: {
            en: 'Apparel - DE'
        },
        redirectUrl: 'redirecturlApparels',
        uid: 'apparel-de',
        contentCatalogs: ['apparelContentCatalog', 'apparel-deContentCatalog']
    },
    {
        previewUrl: '/smartedit-custom-build/test/e2e/dummystorefront/dummystorefrontApparel.html',
        name: {
            en: 'Apparel UK and EU'
        },
        redirectUrl: 'redirecturlApparels',
        uid: 'apparel',
        contentCatalogs: ['apparelContentCatalog']
    }
];
