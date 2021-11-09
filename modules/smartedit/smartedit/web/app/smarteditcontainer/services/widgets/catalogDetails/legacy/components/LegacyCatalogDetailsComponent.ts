/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponent } from 'smarteditcommons';

/**
 * @ngdoc directive
 * @name CatalogDetailsModule.component:catalogDetails
 * @scope
 * @deprecated since 2005
 * @restrict E
 * @element catalog-details
 *
 * @description
 * Deprecated, use <se-catalog-details>
 * Component responsible for displaying a catalog details. It contains a thumbnail representing the whole
 * catalog and the list of catalog versions available to the current user.
 *
 * This component is currently used in the landing page.
 *
 * @param {< String} catalog The catalog that needs to be displayed
 * @param {< String} siteId The catalog that needs to be displayed
 * @param {< Boolean} isCatalogForCurrentSite A flag that specifies if the provided catalog is associated with the selected site in the landing page
 */
@SeComponent({
    selector: 'catalog-details',
    template: `
        <se-catalog-details [catalog]="$ctrl.catalog" [site-id]="$ctrl.siteId" [is-catalog-for-current-site]="$ctrl.isCatalogForCurrentSite"></se-catalog-details>
    `,
    inputs: ['catalog', 'siteId', 'isCatalogForCurrentSite']
})
export class LegacyCatalogDetailsComponent {}
