/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Type } from '@angular/core';

export interface CatalogDetailsItem {
    include?: string;
    component?: Type<any>;
}

export abstract class ICatalogDetailsService {
    addItems(items: CatalogDetailsItem[], column?: string): void {
        'proxyFunction';
    }

    public getItems(): { left: CatalogDetailsItem[]; right: CatalogDetailsItem[] } {
        'proxyFunction';

        return null;
    }
}
