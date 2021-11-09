/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import {
    ICatalogService,
    ICatalogVersion,
    IExperience,
    IExperiencePageContext,
    ISeComponent,
    ISharedDataService,
    SeComponent,
    TypedMap
} from 'smarteditcommons';
import { ICMSPage, ISyncStatus } from 'cmscommons';

import { SynchronizationPageConditions } from '../index';

@SeComponent({
    templateUrl: 'pageSynchronizationHeaderComponentTemplate.html',
    inputs: ['syncStatus', 'pageSyncConditions', 'showDetailedInfo']
})
export class PageSynchronizationHeaderComponent implements ISeComponent {
    public syncStatus: ISyncStatus;
    public pageSyncConditions: SynchronizationPageConditions;
    public showDetailedInfo: boolean;

    public ready: boolean = false;
    public headerText: string;
    public helpTemplate: string;

    constructor(
        private $q: angular.IQService,
        private $translate: angular.translate.ITranslateService,
        private sharedDataService: ISharedDataService,
        private catalogService: ICatalogService,
        private l10nFilter: (s: TypedMap<string>) => string,
        private cmsitemsRestService: any
    ) {}

    $onChanges(): void {
        if (!this.syncStatus) {
            return;
        }
        this.ready = false;

        if (!this.pageSyncConditions.pageHasNoDepOrNoSyncStatus) {
            const translation = this.$translate.instant('se.cms.synchronization.page.header.help');
            this.helpTemplate = '<span>' + translation + '</span>';
            this.ready = true;

            return;
        }

        let catalog: ICatalogVersion = null;
        let catalogId: string = null;
        let catalogName: string = null;
        let catalogVersion: string = null;

        this.sharedDataService
            .get('experience')
            .then((data: IExperience) => {
                catalog = this.getCurrentCatalogIdFromExperience(data);
                catalogId = catalog.catalogId;
                catalogName = this.l10nFilter(catalog.catalogName);

                return this.catalogService.getActiveContentCatalogVersionByCatalogId(catalogId);
            })
            .then((currentCatalogVersion) => {
                catalogVersion = currentCatalogVersion;
                if (this.pageSyncConditions.pageHasUnavailableDependencies) {
                    return this.fetchUnavailableDependencies();
                }
                return this.$q.resolve(null);
            })
            .then((itemNames: string) => {
                this.headerText = this.pageSyncConditions.pageHasUnavailableDependencies
                    ? this.$translate.instant(
                          'se.cms.synchronization.page.unavailable.items.description',
                          {
                              itemNames,
                              catalogName,
                              catalogVersion
                          }
                      )
                    : this.$translate.instant('se.cms.synchronization.page.new.description', {
                          catalogName,
                          catalogVersion
                      });
                this.ready = true;
            });
    }

    public isNewPage(): boolean {
        return this.pageSyncConditions.pageHasNoDepOrNoSyncStatus;
    }

    public isSyncOldHomeHeader(): boolean {
        return !(
            this.pageSyncConditions.pageHasSyncStatus && !this.pageSyncConditions.canSyncHomepage
        );
    }

    public isDefaultSubHeader(): boolean {
        return this.isSyncOldHomeHeader() && !this.isNewPage();
    }

    private userIsInsidePage(pageContext: IExperiencePageContext): boolean {
        return !!pageContext;
    }

    private getCurrentCatalogIdFromExperience({
        pageContext,
        catalogDescriptor
    }: IExperience): ICatalogVersion {
        return {
            catalogId: this.userIsInsidePage(pageContext)
                ? pageContext.catalogId
                : catalogDescriptor.catalogId,
            catalogName: this.userIsInsidePage(pageContext)
                ? pageContext.catalogName
                : catalogDescriptor.name
        } as ICatalogVersion;
    }

    private fetchUnavailableDependencies(): string {
        return this.cmsitemsRestService
            .getByIds(
                this.syncStatus.unavailableDependencies.map(({ itemId }: ISyncStatus) => itemId)
            )
            .then(({ response }: { response: ICMSPage[] }) =>
                response.map(({ name }) => name).join(', ')
            );
    }
}
