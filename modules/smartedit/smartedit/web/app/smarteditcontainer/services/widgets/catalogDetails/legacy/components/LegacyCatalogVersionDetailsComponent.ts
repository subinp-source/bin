/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponent } from 'smarteditcommons';

/**
 * @ngdoc directive
 * @name CatalogDetailsModule.component:catalogVersionDetails
 * @scope
 * @deprecated since 2005
 * @restrict E
 * @element catalog-version-details
 *
 * @description
 * Deprecated, use <se-catalog-version-details>
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
@SeComponent({
    selector: 'catalog-version-details',
    template: `
        <se-catalog-version-details 
            [catalog]="$ctrl.catalog" 
            [site-id]="$ctrl.siteId" 
            [catalog-version]="$ctrl.catalogVersion" 
            [active-catalog-version]="$ctrl.activeCatalogVersion">
        </se-catalog-version-details>
    `,
    inputs: ['catalog', 'catalogVersion', 'activeCatalogVersion', 'siteId']
})
export class LegacyCatalogVersionDetailsComponent {}
