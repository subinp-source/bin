/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import {
    GenericEditorField,
    IBaseCatalog,
    ISeComponent,
    ISeDropdownSelectedOption,
    ISeDropdownSelectedOptionEventData,
    ISite,
    SeComponent,
    SystemEventService,
    TypedMap
} from 'smarteditcommons';
import { CatalogService, SiteService, StorageService } from 'smarteditcontainer/services';
import { AlertService } from 'smarteditcontainer/services/alerts/AlertServiceOuter';

interface ISelectedSite extends ISeDropdownSelectedOption {
    uid: string;
    contentCatalogs: string[];
    name: TypedMap<string>;
    previewUrl: string;
}

/**
 * @ngdoc overview
 * @name LandingPageComponent
 * @description
 *
 * Component responsible of displaying the SmartEdit landing page.
 *
 */
@SeComponent({
    templateUrl: 'LandingPageComponentTemplate.html'
})
export class LandingPageComponent implements ISeComponent {
    public sitesId: string = 'sites-id';
    public catalogs: IBaseCatalog[];
    public qualifier: string;
    public field: Partial<GenericEditorField>;
    public model: { site: string };
    private unregisterSitesDropdownEventHandler: () => void;
    private SELECTED_SITE_COOKIE_NAME = 'seselectedsite';

    constructor(
        private $q: angular.IQService,
        private $routeParams: angular.route.IRouteParamsService,
        private $location: angular.ILocationService,
        private siteService: SiteService,
        private catalogService: CatalogService,
        private systemEventService: SystemEventService,
        private storageService: StorageService,
        private alertService: AlertService,
        private SITES_RESOURCE_URI: string,
        private LINKED_DROPDOWN: string,
        private LANDING_PAGE_PATH: string
    ) {}

    $onInit() {
        this.catalogs = [];
        this.qualifier = 'site';
        this.field = {
            uri: this.SITES_RESOURCE_URI,
            idAttribute: 'uid',
            labelAttributes: ['name'],
            editable: true,
            paged: false
        };

        this.getCurrentSiteId().then((siteId: string) => (this.model = { site: siteId }));

        this.removeStorefrontCssClass();

        this.unregisterSitesDropdownEventHandler = this.systemEventService.subscribe(
            this.sitesId + this.LINKED_DROPDOWN,
            this.selectedSiteDropdownEventHandler.bind(this)
        );
    }

    $onDestroy() {
        this.unregisterSitesDropdownEventHandler();
    }

    private getCurrentSiteId(): angular.IPromise<string> {
        const siteIdFromUrl = this.$routeParams.siteId as string;
        return this.$q
            .when(
                this.storageService.getValueFromLocalStorage(this.SELECTED_SITE_COOKIE_NAME, false)
            )
            .then((siteIdFromCookie: string) => {
                const promise = this.siteService.getAccessibleSites().then((sites: ISite[]) => {
                    const isSiteAvailableFromUrl = sites.some(
                        (site: ISite) => site.uid === siteIdFromUrl
                    );
                    if (isSiteAvailableFromUrl) {
                        this.setSelectedSite(siteIdFromUrl);
                        this.updateRouteToRemoveSite();
                        return siteIdFromUrl;
                    } else {
                        if (siteIdFromUrl) {
                            this.alertService.showInfo('se.landingpage.site.url.error');
                            this.updateRouteToRemoveSite();
                        }

                        const isSelectedSiteAvailableFromCookie = sites.some(
                            (site: ISite) => site.uid === siteIdFromCookie
                        );
                        if (!isSelectedSiteAvailableFromCookie) {
                            const firstSiteId = sites.length > 0 ? sites[0].uid : null;
                            return firstSiteId;
                        } else {
                            return siteIdFromCookie;
                        }
                    }
                });

                return this.$q.when(promise);
            });
    }

    private updateRouteToRemoveSite(): void {
        this.$location.path(this.LANDING_PAGE_PATH).replace();
    }

    private removeStorefrontCssClass(): void {
        const bodyTag = angular.element(document.querySelector('body'));
        if (bodyTag.hasClass('is-storefront')) {
            bodyTag.removeClass('is-storefront');
        }
    }

    private selectedSiteDropdownEventHandler(
        eventId: string,
        data: ISeDropdownSelectedOptionEventData<ISelectedSite>
    ): void {
        if (data.optionObject) {
            const siteId = data.optionObject.id;
            this.setSelectedSite(siteId);
            this.loadCatalogsBySite(siteId);
        } else {
            this.catalogs = [];
        }
    }

    private setSelectedSite(siteId: string): void {
        this.storageService.setValueInLocalStorage(this.SELECTED_SITE_COOKIE_NAME, siteId, false);
    }

    private loadCatalogsBySite(siteId: string): void {
        this.catalogService
            .getContentCatalogsForSite(siteId)
            .then((catalogs: IBaseCatalog[]) => (this.catalogs = catalogs));
    }
}
