import { SeComponent } from 'smarteditcommons';

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

/**
 * @ngdoc directive
 * @name CatalogDetailsModule.directive:homePageLink
 * @scope
 * @restrict E
 * @deprecated since 2005
 * @element <home-page-link></home-page-link>
 *
 * @description
 * Deprecated, use <se-home-page-link>
 * Directive that displays a link to the main storefront page.
 *
 * @param {< Object} catalog Object representing the provided catalog.
 * @param {< Boolean} catalogVersion Object representing the provided catalog version.
 * @param {< String} siteId The ID of the site the provided catalog is associated with.
 */
@SeComponent({
    selector: 'home-page-link',
    template: `
        <se-home-page-link 
            [catalog]="$ctrl.catalog" 
            [catalog-version]="$ctrl.catalogVersion" 
            [site-id]="$ctrl.siteId"> 
        </se-home-page-link>
    `,
    inputs: ['catalog', 'catalogVersion', 'siteId']
})
export class LegacyHomePageLinkComponent {}
