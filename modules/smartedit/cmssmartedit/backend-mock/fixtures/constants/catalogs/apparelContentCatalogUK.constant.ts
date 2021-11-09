/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IContentCatalog } from 'fixtures/entities/catalogs';
import { apparelUkContentCatalogOnlineVersion } from 'fixtures/constants/versions';

export const apparelContentCatalogUK: IContentCatalog = {
    catalogId: 'apparel-ukContentCatalog',
    name: {
        en: 'Apparel UK Content Catalog'
    },
    versions: [
        {
            active: false,
            pageDisplayConditions: [
                {
                    options: [
                        {
                            label: 'page.displaycondition.variation',
                            id: 'VARIATION'
                        }
                    ],
                    typecode: 'ProductPage'
                },
                {
                    options: [
                        {
                            label: 'page.displaycondition.variation',
                            id: 'VARIATION'
                        }
                    ],
                    typecode: 'CategoryPage'
                },
                {
                    options: [
                        {
                            label: 'page.displaycondition.primary',
                            id: 'PRIMARY'
                        },
                        {
                            label: 'page.displaycondition.variation',
                            id: 'VARIATION'
                        }
                    ],
                    typecode: 'ContentPage'
                }
            ],
            uuid: 'apparel-ukContentCatalog/Staged',
            version: 'Staged',
            homepage: {
                current: {
                    uid: 'homepage',
                    name: 'Homepage',
                    catalogVersionUuid: 'apparel-ukContentCatalog/Staged'
                },
                old: {
                    uid: 'thirdpage',
                    name: 'Some Other Page',
                    catalogVersionUuid: 'apparel-ukContentCatalog/Staged'
                }
            }
        },
        apparelUkContentCatalogOnlineVersion
    ]
};
