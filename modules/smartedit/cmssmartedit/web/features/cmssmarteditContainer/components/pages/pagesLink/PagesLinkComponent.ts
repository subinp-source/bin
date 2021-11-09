/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISeComponent, SeComponent } from 'smarteditcommons';

@SeComponent({
    templateUrl: 'pagesLinkTemplate.html'
})
export class PagesLinkComponent implements ISeComponent {
    private siteId: string;
    private catalogId: string;
    private catalogVersion: string;

    constructor(
        private $routeParams: angular.route.IRouteParamsService,
        private $location: angular.ILocationService,
        private PAGE_LIST_PATH: string
    ) {}

    $onInit() {
        this.siteId = this.$routeParams.siteId;
        this.catalogId = this.$routeParams.catalogId;
        this.catalogVersion = this.$routeParams.catalogVersion;
    }

    public backToPagelist(): void {
        this.$location.path(
            this.PAGE_LIST_PATH.replace(':siteId', this.siteId)
                .replace(':catalogId', this.catalogId)
                .replace(':catalogVersion', this.catalogVersion)
        );
    }
}
