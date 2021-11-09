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
import { Workflow } from 'cmssmarteditcontainer/components/workflow/dtos';
import { WorkflowFacade } from 'cmssmarteditcontainer/components/workflow/facades/WorkflowFacade';
import './workflowItemMenu.scss';

interface IButtonConfig {
    i18nKey: string;
    templateUrl: string;
    permissions?: string[];
    callback?(configuration?: any, $event?: Event): void;
}

@SeComponent({
    templateUrl: 'workflowItemMenuTemplate.html',
    inputs: ['workflowInfo']
})
export class WorkflowItemMenuComponent implements ISeComponent {
    public isMenuOpen: boolean = false;
    public popupConfig: PopupOverlayConfig;
    public menuItems: IButtonConfig[];
    public configuration: any;
    public workflowInfo: Workflow;

    constructor(
        private systemEventService: SystemEventService,
        private workflowFacade: WorkflowFacade,
        private $route: angular.route.IRouteService,
        private permissionService: IPermissionService,
        private $q: angular.IQService
    ) {
        this.popupConfig = {
            templateUrl: 'workflowItemMenuItemsTemplate.html',
            legacyController: { alias: '$ctrl', value: () => this },
            halign: 'left',
            valign: 'bottom'
        };

        this.systemEventService.subscribe(
            'WORKFLOW_ITEM_MENU_OPENED_EVENT',
            this.onOtherMenuOpening.bind(this)
        );

        this.getButtonsAfterApplyingPermissions(this.getButtonsConfiguration()).then(
            (menuItems) => {
                this.menuItems = menuItems;
            }
        );
    }

    getButtonsConfiguration(): IButtonConfig[] {
        return [
            {
                i18nKey: 'se.cms.actionitem.page.workflow.description',
                callback: this.editItemCallback.bind(this),
                permissions: ['se.view.page.workflowMenu'],
                templateUrl: 'descriptionWorkflowItemTemplate.html'
            },
            {
                i18nKey: 'se.cms.actionitem.page.workflow.cancel',
                callback: this.cancelItemCallback.bind(this),
                permissions: ['se.cancel.page.workflowMenu'],
                templateUrl: 'cancelWorkflowItemTemplate.html'
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

    onButtonClick($event: any): void {
        $event.stopPropagation();

        this.isMenuOpen = !this.isMenuOpen;
        if (this.isMenuOpen && this.workflowInfo) {
            this.systemEventService.publishAsync('WORKFLOW_ITEM_MENU_OPENED_EVENT', {
                uid: this.workflowInfo.workflowCode
            });
        }
    }

    onMenuHide(): void {
        this.isMenuOpen = false;
    }

    onOtherMenuOpening(eventId: string, eventData: any): void {
        if (this.workflowInfo.workflowCode !== eventData.uid) {
            this.isMenuOpen = false;
        }
    }

    /**
     * Callback for each menu item (see this.menuItems array).
     * @param menuItem the menu item
     * @param $event the event
     */
    executeItemCallback(menuItem: IButtonConfig, $event: Workflow): void {
        if (menuItem.callback) {
            menuItem.callback(this.workflowInfo);
            this.isMenuOpen = false;
        }
    }

    /**
     * Callback for the cancel button in the cancel item menu.
     * @param workflowInfo the workflow object
     */
    cancelItemCallback(workflowInfo: Workflow): void {
        this.workflowFacade.cancelWorflow(workflowInfo).then(() => {
            this.$route.reload();
        });
    }

    /**
     * Callback for the edit button in the description item menu.
     * @param workflowInfo the workflow object
     */
    editItemCallback(workflowInfo: Workflow): angular.IPromise<Workflow> {
        return this.workflowFacade.editWorkflow(workflowInfo).then((updatedWorkflow) => {
            this.workflowInfo.description = updatedWorkflow.description;
            return updatedWorkflow;
        });
    }
}
