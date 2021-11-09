/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    CrossFrameEventService,
    ICatalogService,
    IUriContext,
    SeComponent
} from 'smarteditcommons';
import { ICMSPage } from 'cmscommons/dtos/ICMSPage';
import { ManagePageService } from 'cmssmarteditcontainer/services/pages/ManagePageService';
import { CmsitemsRestService } from 'cmscommons/dao/cmswebservices/sites/CmsitemsRestService';

/**
 * Component that updates the page status in the active catalog version to "DELETED".
 */
@SeComponent({
    templateUrl: 'updatePageStatusTemplate.html',
    inputs: ['pageInfo']
})
export class UpdatePageStatusComponent {
    public pageInfo: ICMSPage;
    public showButton: boolean = false;

    constructor(
        private managePageService: ManagePageService,
        private cmsitemsRestService: CmsitemsRestService,
        private catalogService: ICatalogService,
        private crossFrameEventService: CrossFrameEventService
    ) {
        this.onLoad();
    }

    onLoad() {
        this.catalogService.retrieveUriContext().then((uriContext: IUriContext) => {
            this.catalogService
                .getContentCatalogActiveVersion(uriContext)
                .then((activeVersion: string) => {
                    this.cmsitemsRestService
                        .get<ICMSPage>({
                            pageSize: 1,
                            currentPage: 0,
                            typeCode: 'AbstractPage',
                            itemSearchParams: 'uid:' + this.pageInfo.uid,
                            catalogId: uriContext.CONTEXT_CATALOG,
                            catalogVersion: activeVersion
                        })
                        .then((result) => {
                            this.showButton = result.pagination.totalCount === 1;
                        });
                });
        });
    }

    onClickOnSync() {
        return this.managePageService
            .trashPageInActiveCatalogVersion(this.pageInfo.uid)
            .then(() => {
                this.crossFrameEventService.publish('EVENT_PAGE_STATUS_UPDATED_IN_ACTIVE_CV');
            });
    }
}
