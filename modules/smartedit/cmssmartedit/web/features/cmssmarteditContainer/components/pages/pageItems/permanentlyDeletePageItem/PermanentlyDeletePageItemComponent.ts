/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    CrossFrameEventService,
    ICatalogService,
    ISeComponent,
    IUriContext,
    MultiNamePermissionContext,
    SeComponent
} from 'smarteditcommons';
import { ManagePageService } from 'cmssmarteditcontainer/services/pages/ManagePageService';
import { CMSPageStatus, ICMSPage } from 'cmscommons/dtos/ICMSPage';
import { CmsitemsRestService } from 'cmscommons/dao/cmswebservices/sites/CmsitemsRestService';

@SeComponent({
    templateUrl: 'permanentlyDeletePageItemTemplate.html',
    inputs: ['pageInfo', 'uriContext']
})
export class PermanentlyDeletePageItemComponent implements ISeComponent {
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    public pageInfo: ICMSPage;
    public uriContext: IUriContext;
    public permanentlyDeletePagePermission: MultiNamePermissionContext[];
    private unregPageSyncStatusUpdate: () => void;
    private isDeletable: boolean = false;

    // ------------------------------------------------------------------------
    // Lifecycle Methods
    // ------------------------------------------------------------------------
    constructor(
        private managePageService: ManagePageService,
        private cmsitemsRestService: CmsitemsRestService,
        private catalogService: ICatalogService,
        private crossFrameEventService: CrossFrameEventService
    ) {}

    $onInit(): void {
        this.unregPageSyncStatusUpdate = this.crossFrameEventService.subscribe(
            'EVENT_PAGE_STATUS_UPDATED_IN_ACTIVE_CV',
            this.fetchPageDeletableConditions.bind(this)
        );
        this.fetchPageDeletableConditions();

        this.permanentlyDeletePagePermission = [
            {
                names: ['se.permanently.delete.page.type'],
                context: {
                    typeCode: this.pageInfo.typeCode
                }
            }
        ];
    }

    $onDestroy(): void {
        this.unregPageSyncStatusUpdate();
    }

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    fetchPageDeletableConditions(): void {
        this.catalogService.retrieveUriContext().then((uriContext: IUriContext) => {
            this.catalogService
                .getContentCatalogActiveVersion(this.uriContext)
                .then((activeVersion: string) => {
                    this.cmsitemsRestService
                        .get({
                            pageSize: 1,
                            currentPage: 0,
                            typeCode: 'AbstractPage',
                            itemSearchParams: 'uid:' + this.pageInfo.uid,
                            catalogId: uriContext.CURRENT_CONTEXT_CATALOG,
                            catalogVersion: activeVersion
                        })
                        .then((result) => {
                            this.isDeletable =
                                result.pagination.totalCount === 0 ||
                                result.response[0].pageStatus === CMSPageStatus.DELETED;
                        });
                });
        });
    }

    permanentlyDelete(): void {
        this.managePageService.hardDeletePage(this.pageInfo);
    }

    isDeleteButtonDisabled(): boolean {
        return !this.isDeletable;
    }
}
