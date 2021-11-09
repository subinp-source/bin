/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ICatalogVersion } from 'fixtures/entities/versions';

export const apparelUkContentCatalogOnlineVersion: ICatalogVersion = {
    active: true,
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
    uuid: 'apparel-ukContentCatalog/Online',
    version: 'Online'
};
