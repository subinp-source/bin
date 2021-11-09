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
import { CMSItemStructure, ICMSPage } from 'cmscommons';
import { PageInfoForViewing, PageInfoMenuService } from './services/PageInfoMenuService';
import * as angular from 'angular';
import './pageInfoMenu.scss';

/**
 * This component is used to create a Page Info Menu to be displayed in the toolbar.
 */
@SeComponent({
    templateUrl: 'pageInfoMenuTemplate.html',
    inputs: ['actionItem']
})
export class PageInfoMenuComponent implements ISeComponent {
    // ------------------------------------------------------------------------
    // Properties
    // ------------------------------------------------------------------------
    public actionItem: any;
    public pageInfo: PageInfoForViewing;
    public pageStructure: CMSItemStructure;
    public isComponentReady: boolean;
    public cmsPage: ICMSPage = null;
    public uriContext: IUriContext = null;
    public editPagePermission: MultiNamePermissionContext[];

    private unRegisterSubscription: () => void;

    constructor(
        private pageInfoMenuService: PageInfoMenuService,
        private pageService: any,
        private $q: angular.IQService,
        private catalogService: ICatalogService,
        private systemEventService: SystemEventService,
        private EVENT_CONTENT_CATALOG_UPDATE: string
    ) {
        this.isComponentReady = false;
    }

    // ------------------------------------------------------------------------
    // Lifecycle methods
    // ------------------------------------------------------------------------
    $onInit() {
        this.unRegisterSubscription = this.systemEventService.subscribe(
            this.EVENT_CONTENT_CATALOG_UPDATE,
            this.updateHomepageIcon.bind(this)
        );
        this.updateHomepageIcon();
    }

    $onDestroy() {
        this.unRegisterSubscription();
    }

    // ------------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------------
    onEditPageClick() {
        this.pageInfoMenuService.openPageEditor(this.pageInfo.content);
        this.closeMenu();
    }

    updateHomepageIcon() {
        this.$q
            .all([
                this.pageService.getCurrentPageInfo(),
                this.$q.when(this.catalogService.retrieveUriContext())
            ])
            .then(([page, uriContext]: [ICMSPage, IUriContext]) => {
                this.cmsPage = page;
                this.uriContext = uriContext;

                this.editPagePermission = [
                    {
                        names: ['se.edit.page.type'],
                        context: {
                            typeCode: page.typeCode
                        }
                    },
                    {
                        names: ['se.edit.page.link']
                    }
                ];
            });
    }

    onDropdownToggle(open: boolean) {
        if (!open) {
            return;
        }

        this.pageInfoMenuService
            .getCurrentPageInfo()
            .then((pageInfo: PageInfoForViewing) => {
                this.pageInfo = pageInfo;

                // TODO: customize the retrieval of page structure based on the page.
                return this.pageInfoMenuService.getPageStructureForViewing(
                    pageInfo.typeCode,
                    pageInfo.defaultPage
                );
            })
            .then((pageStructure: CMSItemStructure) => {
                this.pageStructure = pageStructure;
                this.isComponentReady = true;
            });
    }

    // ------------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------------

    private closeMenu(): void {
        if (this.actionItem) {
            this.actionItem.isOpen = false;
        }
    }
}
