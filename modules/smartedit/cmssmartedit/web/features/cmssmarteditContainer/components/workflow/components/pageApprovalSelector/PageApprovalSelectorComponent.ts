/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import {
    CrossFrameEventService,
    ISeComponent,
    IWaitDialogService,
    SeComponent,
    TypedMap
} from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';
import { Workflow } from '../../dtos';
import { WorkflowService } from '../../services/WorkflowService';
import './pageApprovalSelector.scss';

/**
 * This interface represents one of the approval status options available to a super user.
 */
interface PageApprovalOption {
    key: string;
    i18nKey: string;
    selected?: boolean;
}

/**
 * This component displays a dropdown in the page to allow super users force a page into approved
 * or checked approval status. This is to give them the posibility of skipping a workflow and be
 * able to sync the page.
 */
@SeComponent({
    templateUrl: 'pageApprovalSelectorTemplate.html'
})
export class PageApprovalSelectorComponent implements ISeComponent {
    public isOpen = false;
    public showDropdown = true;

    private unregWfFinishedHandler: () => any;
    private unregPerspectiveChangedHandler: () => any;

    private pageApprovalOptions: { [key: string]: PageApprovalOption } = {
        CHECK: {
            // Draft
            key: 'CHECK',
            i18nKey: 'se.cms.page.approval.check'
        },
        APPROVED: {
            // Ready to Sync
            key: 'APPROVED',
            i18nKey: 'se.cms.page.approval.approved'
        }
    };

    constructor(
        iframeClickDetectionService: any,
        private lodash: lo.LoDashStatic,
        private pageService: any,
        private waitDialogService: IWaitDialogService,
        private workflowService: WorkflowService,
        private crossFrameEventService: CrossFrameEventService,
        private $route: angular.route.IRouteService,
        private $log: angular.ILogService,
        private EVENTS: TypedMap<string>,
        private WORKFLOW_FINISHED_EVENT: string,
        private EVENT_PERSPECTIVE_CHANGED: string
    ) {
        this.unregWfFinishedHandler = this.crossFrameEventService.subscribe(
            this.WORKFLOW_FINISHED_EVENT,
            this.hideComponentIfWorkflowInProgress.bind(this)
        );
        this.unregPerspectiveChangedHandler = this.crossFrameEventService.subscribe(
            this.EVENT_PERSPECTIVE_CHANGED,
            this.hideComponentIfWorkflowInProgress.bind(this)
        );

        iframeClickDetectionService.registerCallback(
            'pageApprovalSelectorClose',
            this.closeDropdown.bind(this)
        );
    }

    public $onInit() {
        this.hideComponentIfWorkflowInProgress();
    }

    public $onDestroy() {
        this.unregWfFinishedHandler();
        this.unregPerspectiveChangedHandler();
    }

    public getPageApprovalOptions(): PageApprovalOption[] {
        return this.lodash.values(this.pageApprovalOptions).filter((option: PageApprovalOption) => {
            return !option.selected;
        });
    }

    /**
     * @internal
     *
     * This method is the callback used to manually force the change of the approval status of a page. The page will be updated with the selected status.
     *
     * @param approvalStatus the approval status to which the page must be manually set to.
     */
    public selectApprovalStatus(approvalStatus: PageApprovalOption): void {
        this.waitDialogService.showWaitModal(null);
        this.pageService
            .forcePageApprovalStatus(approvalStatus.key)
            .then(
                () => {
                    this.crossFrameEventService.publish(this.EVENTS.PAGE_UPDATED);
                    this.$route.reload();
                },
                () => {
                    this.$log.warn("[PageApprovalSelector] - Can't change page status.");
                }
            )
            .finally(() => {
                this.waitDialogService.hideWaitModal();
                this.cleanDropdownSelection();
            });
    }

    public onDropdownClick(): void {
        if (this.isOpen) {
            this.getCurrentPageApprovalStatus();
        }
    }

    private hideComponentIfWorkflowInProgress(): void {
        this.pageService.getCurrentPageInfo().then((pageInfo: ICMSPage) => {
            return this.workflowService
                .getActiveWorkflowForPageUuid(pageInfo.uuid)
                .then((workflow: Workflow) => {
                    this.showDropdown = !workflow;
                });
        });
    }

    /**
     * This method retrieves the approval status of the current page. If the page is currently in checked or approved status,
     * then this method will mark the corresponding status as the 'selected' option.
     */
    private getCurrentPageApprovalStatus(): angular.IPromise<void> {
        return this.pageService.getCurrentPageInfo().then((currentPageInfo: ICMSPage) => {
            const currentApprovalOption = currentPageInfo.approvalStatus;

            // Reset the options.
            this.lodash
                .values(this.pageApprovalOptions)
                .forEach((option: PageApprovalOption) => (option.selected = false));

            if (this.pageApprovalOptions[currentApprovalOption]) {
                this.pageApprovalOptions[currentApprovalOption].selected = true;
            }
        });
    }

    private cleanDropdownSelection(): void {
        this.lodash.forEach(this.pageApprovalOptions, (approvalOption: PageApprovalOption) => {
            approvalOption.selected = false;
        });
    }

    private closeDropdown(): void {
        this.isOpen = false;
    }
}
