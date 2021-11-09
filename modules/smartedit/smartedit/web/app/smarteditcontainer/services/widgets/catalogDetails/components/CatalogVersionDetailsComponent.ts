/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { Component, Input, OnInit } from '@angular/core';

import {
    CatalogDetailsItem,
    ICatalog,
    ICatalogDetailsService,
    ICatalogVersion,
    SeDowngradeComponent
} from 'smarteditcommons';

/**
 * @ngdoc component
 * @name CatalogDetailsModule.component:catalogVersionDetails
 * @element se-catalog-version-details
 *
 * @description
 * Component responsible for displaying a catalog version details. Contains a link, called homepage, that
 * redirects to the default page with the right experience (site, catalog, and catalog version).
 *
 * Can be extended with custom items to provide new links and functionality.
 *
 * @param {<Object} catalog Object representing the parent catalog of the catalog version to display.
 * @param {<Object} catalogVersion Object representing the catalog version to display.
 * @param {<Object} activeCatalogVersion Object representing the active catalog version of the parent catalog.
 * @param {<String} siteId The site associated with the provided catalog.
 */
@SeDowngradeComponent()
@Component({
    selector: 'se-catalog-version-details',
    templateUrl: './CatalogVersionDetailsComponent.html'
})
export class CatalogVersionDetailsComponent implements OnInit {
    @Input() catalog: ICatalog;
    @Input() catalogVersion: ICatalogVersion;
    @Input() activeCatalogVersion: ICatalogVersion;
    @Input() siteId: string;

    public leftItems: CatalogDetailsItem[];
    public rightItems: CatalogDetailsItem[];

    constructor(private catalogDetailsService: ICatalogDetailsService) {}

    ngOnInit() {
        const { left, right } = this.catalogDetailsService.getItems();

        this.leftItems = left;
        this.rightItems = right;
    }
}
