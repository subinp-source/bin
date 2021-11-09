/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject } from '@angular/core';

import {
    CatalogDetailsItemData,
    CATALOG_DETAILS_ITEM_DATA,
    IExperienceService,
    SeDowngradeComponent
} from 'smarteditcommons';

/**
 * @ngdoc component
 * @name CatalogDetailsModule.directive:homePageLink
 * @element <se-home-page-link></home-page-link>
 *
 * @description
 * Directive that displays a link to the main storefront page.
 *
 * @param {< Object} catalog Object representing the provided catalog.
 * @param {< Boolean} catalogVersion Object representing the provided catalog version.
 * @param {< String} siteId The ID of the site the provided catalog is associated with.
 */
@SeDowngradeComponent()
@Component({
    selector: 'se-home-page-link',
    templateUrl: './HomePageLinkComponent.html'
})
export class HomePageLinkComponent {
    constructor(
        private experienceService: IExperienceService,
        @Inject(CATALOG_DETAILS_ITEM_DATA) private data: CatalogDetailsItemData
    ) {}

    public onClick(): void {
        const {
            siteId,
            catalog: { catalogId },
            catalogVersion: { version: catalogVersion }
        } = this.data;

        this.experienceService.loadExperience({
            siteId,
            catalogId,
            catalogVersion
        });
    }
}
