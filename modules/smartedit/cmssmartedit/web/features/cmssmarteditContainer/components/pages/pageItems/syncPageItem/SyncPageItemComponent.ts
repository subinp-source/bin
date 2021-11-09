/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ICatalogService,
    IUriContext,
    MultiNamePermissionContext,
    SeComponent,
    SystemEventService
} from 'smarteditcommons';
import { ICMSPage } from 'cmscommons/dtos/ICMSPage';

/**
 * @ngdoc directive
 * @name pageComponentsModule.directive:syncPageItem
 * @scope
 * @restrict E
 *
 * @description
 * syncPageItem builds a drop-down item allowing for the
 * edition of a given CMS page .
 *
 * @param {<Object} pageInfo An object defining the context of the
 * CMS page associated to the editPage item.
 */
@SeComponent({
    templateUrl: 'syncPageItemTemplate.html',
    inputs: ['pageInfo']
})
export class SyncPageItemComponent {
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    public pageInfo: ICMSPage;
    public syncPagePermission: MultiNamePermissionContext[];

    constructor(
        private syncPageModalService: any,
        private catalogService: ICatalogService,
        private EVENT_CONTENT_CATALOG_UPDATE: string,
        private systemEventService: SystemEventService
    ) {}

    $onInit(): void {
        this.syncPagePermission = [
            {
                names: ['se.act.on.page.in.workflow'],
                context: {
                    pageInfo: this.pageInfo
                }
            }
        ];
    }

    // ------------------------------------------------------------------------
    // Helper Methods
    // ------------------------------------------------------------------------
    onClickOnSync() {
        this.catalogService.retrieveUriContext().then((uriContext: IUriContext) => {
            this.syncPageModalService.open(this.pageInfo, uriContext).then((response: any) => {
                this.systemEventService.publishAsync(this.EVENT_CONTENT_CATALOG_UPDATE, response);
            });
        });
    }
}
