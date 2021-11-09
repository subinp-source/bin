/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IAlertServiceType, IPageInfoService, ISeComponent, SeComponent } from 'smarteditcommons';
import { IPageService, SlotStatus } from 'cmscommons';

@SeComponent({
    templateUrl: 'slotSynchronizationPanelTemplate.html',
    inputs: ['slotId']
})
export class SlotSynchronizationPanel implements ISeComponent {
    private synchronizationPanelApi: any;
    private slotId: string;

    constructor(
        private pageService: IPageService,
        private pageInfoService: IPageInfoService,
        private slotSynchronizationService: any,
        private pageContentSlotsService: any,
        private $translate: angular.translate.ITranslateService,
        private $q: angular.IQService
    ) {}

    public getApi($api: any): void {
        this.synchronizationPanelApi = $api;
    }

    public getSyncStatus = (): angular.IPromise<any> => {
        const promise = this.pageInfoService.getPageUID().then((pageId: string) => {
            return this.slotSynchronizationService
                .getSyncStatus(pageId, this.slotId)
                .then((syncStatus: any) => {
                    return this.isSyncDisallowed().then((isDisallowed: boolean) => {
                        if (isDisallowed) {
                            this.disableSync();
                        }

                        return syncStatus;
                    });
                });
        });

        return this.$q.when(promise);
    };

    public performSync = (itemsToSync: any[]): angular.IPromise<void> => {
        return this.slotSynchronizationService.performSync(itemsToSync);
    };

    private isSyncDisallowed(): angular.IPromise<boolean> {
        return this.isPageSlot().then((isPageSlot: boolean) => {
            return this.isPageApproved().then((isPageApproved: boolean) => {
                return isPageSlot && !isPageApproved;
            });
        });
    }

    private isPageSlot(): angular.IPromise<boolean> {
        return this.pageContentSlotsService
            .getSlotStatus(this.slotId)
            .then((slotStatus: SlotStatus) => {
                return slotStatus === SlotStatus.PAGE || slotStatus === SlotStatus.OVERRIDE;
            });
    }

    private isPageApproved(): angular.IPromise<boolean> {
        const promise = this.pageInfoService.getPageUUID().then((pageUuid: string) => {
            return this.pageService.isPageApproved(pageUuid);
        });

        return this.$q.when(promise);
    }

    private disableSync(): void {
        this.synchronizationPanelApi.setMessage({
            type: IAlertServiceType.WARNING,
            description: this.$translate.instant('se.cms.synchronization.slot.disabled.msg')
        });
        this.synchronizationPanelApi.disableItemList(true);
    }
}
