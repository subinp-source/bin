/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Input, OnInit } from '@angular/core';

import {
    ICatalog,
    ICatalogVersion,
    IExperienceService,
    SeDowngradeComponent
} from 'smarteditcommons';

/**
 * @ngdoc component
 * @name CatalogDetailsModule.component:catalogVersionsThumbnailCarousel
 * @element se-catalog-versions-thumbnail-carousel
 *
 * @description
 * Component responsible for displaying a thumbnail of the provided catalog. When clicked,
 * it redirects to the storefront page for the catalog's active catalog version.
 *
 * @param {< Object} catalog Object representing the current catalog.
 * @param {< String} siteId The ID of the site associated with the provided catalog.
 */
@SeDowngradeComponent()
@Component({
    selector: 'se-catalog-versions-thumbnail-carousel',
    templateUrl: './CatalogVersionsThumbnailCarouselComponent.html'
})
export class CatalogVersionsThumbnailCarouselComponent implements OnInit {
    @Input() catalog: ICatalog;
    @Input() siteId: string;

    public selectedVersion: ICatalogVersion;

    constructor(private experienceService: IExperienceService) {}

    ngOnInit() {
        this.selectedVersion = this.getActiveVersion();
    }

    public onClick(): void {
        this.experienceService.loadExperience({
            siteId: this.siteId,
            catalogId: this.catalog.catalogId,
            catalogVersion: this.selectedVersion.version
        });
    }

    private getActiveVersion() {
        return this.catalog.versions.find((catalogVersion) => catalogVersion.active);
    }
}
