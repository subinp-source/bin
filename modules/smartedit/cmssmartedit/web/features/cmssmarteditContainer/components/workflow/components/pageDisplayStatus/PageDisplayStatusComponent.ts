/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CrossFrameEventService, ISeComponent, SeComponent, TypedMap } from 'smarteditcommons';
import { ICMSPage, IPageService, ISyncStatus } from 'cmscommons';
import { WorkflowService } from '../../services/WorkflowService';
import { Workflow } from '../../dtos';
import './pageDisplayStatus.scss';

@SeComponent({
    templateUrl: 'pageDisplayStatusTemplate.html',
    inputs: ['cmsPage:?', 'uriContext:?', 'showLastSyncTime:?']
})
export class PageDisplayStatusComponent implements ISeComponent {
    public showLastSyncTime: boolean = true;

    private cmsPage: ICMSPage;
    private page: ICMSPage;
    private LOCALIZATION_PREFIX = 'se.cms.page.displaystatus.';
    private currentStatusKey: string;
    private statusIconCssClass: string;
    private lastSynchedDate: number;

    private unRegPageSyncEvent: () => void;
    private unRegPageChangeEvent: () => void;

    constructor(
        private pageService: IPageService,
        private workflowService: WorkflowService,
        private crossFrameEventService: CrossFrameEventService,
        private SYNCHRONIZATION_POLLING: TypedMap<string>,
        private EVENTS: TypedMap<string>
    ) {}

    public $onInit() {
        this.updatePageInfo({
            uuid: (this.cmsPage && this.cmsPage.uuid) || null
        });

        this.unRegPageSyncEvent = this.crossFrameEventService.subscribe(
            this.SYNCHRONIZATION_POLLING.FAST_FETCH,
            this.onPoll.bind(this)
        );
        this.unRegPageChangeEvent = this.crossFrameEventService.subscribe(
            this.EVENTS.PAGE_UPDATED,
            this.updatePageInfo.bind(this)
        );
    }

    public $onDestroy() {
        this.unRegPageSyncEvent();
        this.unRegPageChangeEvent();
    }

    public getCurrentStatusKey(): string {
        return this.currentStatusKey;
    }

    public getStatusIconCssClass(): string {
        return this.statusIconCssClass;
    }

    public isPageLockedToCurrentUser(pageInfo: ICMSPage): angular.IPromise<boolean> {
        const pageUuid = pageInfo.uuid;

        return this.workflowService
            .getActiveWorkflowForPageUuid(pageUuid)
            .then((workflow: Workflow) => {
                return workflow && !workflow.isAvailableForCurrentPrincipal;
            });
    }

    public getTooltipTemplate(): string {
        if (this.hasBeenSynchedBefore()) {
            return `<div class="popover-tooltip">
                <span data-translate="se.cms.page.displaystatus.lastpublished.date"></span>
                <span>{{${this.lastSynchedDate} | date:'short'}}</span
            </div>`;
        } else {
            return `<div class="popover-tooltip">
                    <span data-translate="se.cms.page.displaystatus.not.published"></span>
                </div>`;
        }
    }

    public hasBeenSynchedBefore(): boolean {
        return this.lastSynchedDate !== undefined;
    }

    /**
     * Method called when SYNCHRONIZATION_POLLING.FAST_FETCH event is triggered.
     */
    private onPoll(eventId: string, eventData: ISyncStatus): void {
        if (this.page && this.page.uuid === eventData.itemId) {
            this.lastSynchedDate = eventData.lastSyncStatus;
        }
    }

    private updatePageInfo(eventData: { uuid: string }): void {
        if (this.cmsPage && this.cmsPage.uuid && eventData.uuid !== this.cmsPage.uuid) {
            return;
        }
        this.getPage().then((pageInfo: ICMSPage) => {
            const displayStatus = pageInfo.displayStatus
                ? pageInfo.displayStatus.toLowerCase()
                : '';
            this.currentStatusKey = displayStatus ? this.LOCALIZATION_PREFIX + displayStatus : '';

            this.isPageLockedToCurrentUser(pageInfo).then((isPageLocked: boolean) => {
                if (isPageLocked) {
                    this.statusIconCssClass = 'icon-locked se-page-status-locked__icon';
                } else {
                    this.statusIconCssClass = `se-page-status-${displayStatus}__icon`;
                }
            });
        });
    }

    private getPage(): Promise<ICMSPage> {
        const promise = this.cmsPage
            ? Promise.resolve(this.cmsPage)
            : this.pageService.getCurrentPageInfo();
        return promise.then((page: ICMSPage) => {
            this.page = page;
            return page;
        });
    }
}
