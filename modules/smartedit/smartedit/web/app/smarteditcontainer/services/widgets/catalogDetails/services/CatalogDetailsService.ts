/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import {
    CatalogDetailsItem,
    CATALOG_DETAILS_COLUMNS,
    ICatalogDetailsService,
    SeDowngradeService
} from 'smarteditcommons';
import { HomePageLinkComponent } from '../components/HomePageLinkComponent';

/**
 * @ngdoc service
 * @name CatalogDetailsModule.service:catalogDetailsService
 * @description
 *
 * The catalog details Service makes it possible to add items in form of directive
 * to the catalog details directive
 *
 */
@SeDowngradeService(ICatalogDetailsService)
export class CatalogDetailsService implements ICatalogDetailsService {
    private _customItems: { left: CatalogDetailsItem[]; right: CatalogDetailsItem[] } = {
        left: [],
        right: []
    };

    constructor() {
        this.addItems(
            [
                {
                    component: HomePageLinkComponent
                }
            ],
            CATALOG_DETAILS_COLUMNS.LEFT
        );
    }

    /**
     * @ngdoc method
     * @name CatalogDetailsModule.service:catalogDetailsService#addItems
     * @methodOf CatalogDetailsModule.service:catalogDetailsService
     *
     * @description
     * This method allows to add a new item/items to the template array.
     *
     * @param {Array} items An array that hold a list of items.
     * @param {String=} column The place where the template will be added to. If this value is empty
     * the template will be added to the left side by default. The available places are defined in the
     * constant {@link CatalogDetailsModule.object:CATALOG_DETAILS_COLUMNS}
     */
    public addItems(items: CatalogDetailsItem[], column?: string): void {
        if (column === CATALOG_DETAILS_COLUMNS.RIGHT) {
            this._customItems.right = this._customItems.right.concat(items);
        } else {
            this._customItems.left = this._customItems.left.concat(items);
        }
    }

    /**
     * @ngdoc method
     * @name CatalogDetailsModule.service:catalogDetailsService#getItems
     * @methodOf CatalogDetailsModule.service:catalogDetailsService
     *
     * @description
     * This retrieves the list of items currently extending catalog version details components.
     */
    public getItems(): { left: CatalogDetailsItem[]; right: CatalogDetailsItem[] } {
        return this._customItems;
    }
}
