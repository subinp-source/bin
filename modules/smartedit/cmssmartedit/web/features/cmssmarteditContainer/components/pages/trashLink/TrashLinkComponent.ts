/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ICatalogService,
    ISeComponent,
    IUriContext,
    IUrlService,
    SeComponent,
    SystemEventService
} from 'smarteditcommons';
import { ManagePageService } from 'cmssmarteditcontainer/services/pages/ManagePageService';

@SeComponent({
    templateUrl: 'trashLinkTemplate.html'
})
export class TrashLinkComponent implements ISeComponent {
    public trashedPagesTranslationData: any;
    public isNonActiveCatalog: boolean;

    private siteId: string;
    private catalogId: string;
    private catalogVersion: string;
    private uriContext: IUriContext;
    private unsibscribeContentCatalogUpdateEvent: any;

    constructor(
        private $routeParams: angular.route.IRouteParamsService,
        private TRASHED_PAGE_LIST_PATH: string,
        private EVENT_CONTENT_CATALOG_UPDATE: string,
        private $location: angular.ILocationService,
        private managePageService: ManagePageService,
        private urlService: IUrlService,
        private catalogService: ICatalogService,
        private systemEventService: SystemEventService
    ) {
        this.isNonActiveCatalog = false;
    }

    $onInit() {
        this.siteId = this.$routeParams.siteId;
        this.catalogId = this.$routeParams.catalogId;
        this.catalogVersion = this.$routeParams.catalogVersion;
        this.uriContext = this.urlService.buildUriContext(
            this.siteId,
            this.catalogId,
            this.catalogVersion
        );
        this.catalogService.isContentCatalogVersionNonActive().then((isNonActiveCatalog) => {
            this.isNonActiveCatalog = isNonActiveCatalog;
            if (isNonActiveCatalog) {
                this.updateTrashedPagesCount();
            }
        });

        this.unsibscribeContentCatalogUpdateEvent = this.systemEventService.subscribe(
            this.EVENT_CONTENT_CATALOG_UPDATE,
            this.updateTrashedPagesCount.bind(this)
        );
    }

    $onDestroy() {
        this.unsibscribeContentCatalogUpdateEvent();
    }

    /**
     * Retrieves the total count of all soft-deleted pages.
     */
    updateTrashedPagesCount(): Promise<void> {
        return this.managePageService
            .getSoftDeletedPagesCount(this.uriContext)
            .then((trashedPagesCount) => {
                this.trashedPagesTranslationData = {
                    totalCount: trashedPagesCount
                };
            });
    }

    goToTrash() {
        this.$location.path(
            this.TRASHED_PAGE_LIST_PATH.replace(':siteId', this.siteId)
                .replace(':catalogId', this.catalogId)
                .replace(':catalogVersion', this.catalogVersion)
        );
    }
}
