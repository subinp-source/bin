/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';

import { ICatalogService, SeInjectable } from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';

@SeInjectable()
export class PageRestoredAlertService {
    // ---------------------------------------------------------------------------
    // Variables
    // ---------------------------------------------------------------------------

    // ---------------------------------------------------------------------------
    // Constructor
    // ---------------------------------------------------------------------------
    constructor(
        private catalogService: ICatalogService,
        private actionableAlertService: any,
        private actionableAlertConstants: any,
        private $q: angular.IQService
    ) {}

    // ---------------------------------------------------------------------------
    // Public API
    // ---------------------------------------------------------------------------
    public displayPageRestoredSuccessAlert(pageInfo: ICMSPage): angular.IPromise<void> {
        if (!pageInfo) {
            throw new Error('[pageRestoredAlertService] - page info not provided.');
        }

        const promise = this.catalogService
            .getCatalogVersionByUuid(pageInfo.catalogVersion)
            .then((catalogVersion: any) => {
                const alertConfig = {
                    controller: [
                        'experienceService',
                        function(experienceService: any) {
                            this.description = 'se.cms.page.restored.alert.info.description';
                            this.hyperlinkLabel = 'se.cms.page.restored.alert.info.hyperlink';
                            this.hyperlinkDetails = {
                                pageName: pageInfo.name
                            };

                            this.onClick = () => {
                                experienceService.loadExperience({
                                    siteId: catalogVersion.siteId,
                                    catalogId: catalogVersion.catalogId,
                                    catalogVersion: catalogVersion.version,
                                    pageId: pageInfo.uid
                                });
                            };
                        }
                    ]
                };

                return this.actionableAlertService.displayActionableAlert(
                    alertConfig,
                    this.actionableAlertConstants.ALERT_TYPES.SUCCESS
                );
            });

        return this.$q.when(promise);
    }
}
