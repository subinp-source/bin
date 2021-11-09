/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { InjectionToken } from '@angular/core';
import { ICatalog, ICatalogVersion } from '../dtos';

export interface CatalogDetailsItemData {
    siteId: string;
    catalog: ICatalog;
    catalogVersion: ICatalogVersion;
    activeCatalogVersion: ICatalogVersion;
}

export const CATALOG_DETAILS_ITEM_DATA = new InjectionToken<CatalogDetailsItemData>(
    'CATALOG_DETAILS_ITEM_DATA'
);
