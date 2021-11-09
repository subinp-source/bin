/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    IPermissionService,
    ISeComponent,
    PopupOverlayConfig,
    SeComponent,
    SystemEventService
} from 'smarteditcommons';
import { ManagePageVersionService } from 'cmssmarteditcontainer/components/versioning/services/ManagePageVersionService';
import { PageVersionSelectionService } from 'cmssmarteditcontainer/components/versioning/services/PageVersionSelectionService';
import { RollbackPageVersionService } from 'cmssmarteditcontainer/components/versioning/services/RollbackPageVersionService';
import { IPageVersion } from '../../../../services/pageVersioning/PageVersioningService';

interface IButtonConfig {
    i18nKey: string;
    permissions?: string[];
    callback?(configuration?: any, $event?: Event): void;
}

@SeComponent({
    templateUrl: 'versionItemMenuTemplate.html',
    inputs: ['item']
})
export class VersionItemMenuComponent implements ISeComponent {
    public isMenuOpen: boolean = false;
    public popupConfig: PopupOverlayConfig;
    public menuItems: IButtonConfig[];
    public configuration: any;
    public version: PageVersionSelectionService;
    public item: IPageVersion;

    constructor(
        private managePageVersionService: ManagePageVersionService,
        private pageVersionSelectionService: PageVersionSelectionService,
        private rollbackPageVersionService: RollbackPageVersionService,
        private permissionService: IPermissionService,
        private $q: angular.IQService,
        private systemEventService: SystemEventService
    ) {
        this.popupConfig = {
            templateUrl: 'versionItemMenuItemsTemplate.html',
            legacyController: { alias: '$ctrl', value: () => this },
            halign: 'left',
            valign: 'bottom'
        };

        this.getButtonsAfterApplyingPermissions(this.getButtonsConfiguration()).then(
            (buttons: IButtonConfig[]) => (this.menuItems = buttons)
        );

        this.systemEventService.subscribe(
            'VERSION_ITEM_MENU_OPENED_EVENT',
            this.onOtherMenuOpening.bind(this)
        );
    }

    getButtonsConfiguration(): IButtonConfig[] {
        return [
            {
                i18nKey: 'se.cms.version.item.menu.view.label',
                callback: this.viewItemCallback.bind(this),
                permissions: ['se.version.page']
            },
            {
                i18nKey: 'se.cms.version.item.menu.edit.label',
                callback: this.editItemCallback.bind(this),
                permissions: ['se.edit.version.page']
            },
            {
                i18nKey: 'se.cms.version.item.menu.rollback.label',
                callback: this.rollbackItemCallback.bind(this),
                permissions: ['se.rollback.version.page.versions.menu']
            },
            {
                i18nKey: 'se.cms.version.item.menu.delete.label',
                callback: this.deleteItemCallback.bind(this),
                permissions: ['se.delete.version.page']
            }
        ];
    }

    getButtonsAfterApplyingPermissions(
        menuItemsConfig: IButtonConfig[]
    ): angular.IPromise<IButtonConfig[]> {
        const promises = menuItemsConfig.map((menuItem: IButtonConfig) => {
            return this.$q.when(
                this.permissionService.isPermitted([
                    {
                        names: menuItem.permissions
                    }
                ])
            );
        });
        return this.$q.all(promises).then((result: boolean[]) => {
            return menuItemsConfig.filter((menuItem: IButtonConfig, index: number) => {
                return result[index];
            });
        });
    }

    // --------------------------------------------------------------------------------------
    // Event Handlers
    // --------------------------------------------------------------------------------------
    onButtonClick($event: any): void {
        $event.stopPropagation();

        this.isMenuOpen = !this.isMenuOpen;
        if (this.isMenuOpen) {
            this.systemEventService.publishAsync('VERSION_ITEM_MENU_OPENED_EVENT', {
                uid: this.item.uid
            });
        }
    }

    onMenuHide(): void {
        this.isMenuOpen = false;
    }

    onOtherMenuOpening(eventId: string, eventData: any): void {
        if (this.item.uid !== eventData.uid) {
            this.isMenuOpen = false;
        }
    }

    executeItemCallback(menuItem: IButtonConfig, $event: IPageVersion): void {
        if (menuItem.callback) {
            menuItem.callback(this.item);
            this.isMenuOpen = false;
        }
    }

    // --------------------------------------------------------------------------------------
    // Version Item Menu Items Callbacks
    // --------------------------------------------------------------------------------------
    deleteItemCallback(versionItem: IPageVersion): void {
        this.managePageVersionService.deletePageVersion(versionItem.uid);
    }

    editItemCallback(versionItem: IPageVersion): void {
        this.managePageVersionService.editPageVersion(versionItem);
    }

    viewItemCallback(versionItem: IPageVersion): void {
        this.pageVersionSelectionService.selectPageVersion(versionItem);
    }

    rollbackItemCallback(versionItem: IPageVersion): void {
        this.rollbackPageVersionService.rollbackPageVersion(versionItem);
    }
}
