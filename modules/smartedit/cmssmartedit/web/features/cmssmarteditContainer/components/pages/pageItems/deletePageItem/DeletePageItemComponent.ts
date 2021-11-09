/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    ICatalogService,
    ISeComponent,
    IUriContext,
    MultiNamePermissionContext,
    SeComponent,
    SystemEventService
} from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';
import { ManagePageService } from 'cmssmarteditcontainer/services/pages/ManagePageService';

/**
 * @ngdoc directive
 * @name pageComponentsModule.directive:deletePageItem
 * @scope
 * @restrict E
 *
 * @description
 * deletePageItem builds a dropdown item allowing for the soft
 * deletion of a given CMS page .
 *
 * @param {< Object} pageInfo An object defining the context of the
 * CMS page associated to the deletePage item.
 */
@SeComponent({
    templateUrl: 'deletePageItemTemplate.html',
    inputs: ['pageInfo']
})
export class DeletePageItemComponent implements ISeComponent {
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    public pageInfo: ICMSPage;
    public isDeletePageEnabled = false;
    public tooltipMessage = 'se.cms.tooltip.movetotrash';
    public deletePagePermission: MultiNamePermissionContext[];

    constructor(
        private managePageService: ManagePageService,
        private systemEventService: SystemEventService,
        private catalogService: ICatalogService,
        private EVENT_CONTENT_CATALOG_UPDATE: string
    ) {}

    // ------------------------------------------------------------------------
    // Lifecycle Methods
    // ------------------------------------------------------------------------
    $onInit() {
        this.catalogService.retrieveUriContext().then((uriContext: IUriContext) => {
            this.managePageService
                .isPageTrashable(this.pageInfo, uriContext)
                .then((isEnabled: boolean) => {
                    this.isDeletePageEnabled = isEnabled;

                    if (this.isDeletePageEnabled) {
                        this.tooltipMessage = null;
                    } else {
                        this.managePageService
                            .getDisabledTrashTooltipMessage(this.pageInfo, uriContext)
                            .then((tooltipMessage: string) => {
                                this.tooltipMessage = tooltipMessage;
                            });
                    }
                });
        });

        this.deletePagePermission = [
            {
                names: ['se.delete.page.type'],
                context: {
                    typeCode: this.pageInfo.typeCode
                }
            },
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
    onClickOnDeletePage() {
        this.catalogService.retrieveUriContext().then((uriContext: IUriContext) => {
            return this.managePageService
                .softDeletePage(this.pageInfo, uriContext)
                .then((response: any) => {
                    this.systemEventService.publishAsync(
                        this.EVENT_CONTENT_CATALOG_UPDATE,
                        response
                    );
                });
        });
    }

    getTooltipTemplate() {
        return (
            '<div class="popover-tooltip"><span data-translate="' +
            this.tooltipMessage +
            '" /></div>'
        );
    }
}
