/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { PageApprovalSelectorComponent, PageDisplayStatusComponent } from './components';
import {
    CrossFrameEventService,
    INotificationService,
    IPageInfoService,
    SeModule,
    TypedMap
} from 'smarteditcommons';
import { PageWorkflowMenuComponent } from './pageWorkflowMenu/PageWorkflowMenuComponent';
import { WorkflowService } from './services/WorkflowService';
import { WorkflowTasksPollingService } from './services/WorkflowTasksPollingService';
import { WorkflowItemMenuComponent } from './pageWorkflowMenu/workflowItemMenu/WorkflowItemMenuComponent';
import { WorkflowFacade } from './facades/WorkflowFacade';
import { WorkflowActionItemComponent } from './pageWorkflowMenu/components/WorkflowActionItemComponent';
import { WorkflowActionCommentComponent } from './pageWorkflowMenu/components/WorkflowActionCommentComponent';
import { WorkflowInboxComponent } from './inbox/WorkflowInboxComponent';
import { WorkflowInboxBadgeComponent } from './inbox/WorkflowInboxBadgeComponent';
import { WorkflowInboxTaskComponent } from './inbox/components/WorkflowInboxTaskComponent';
import { CmsSmarteditServicesModule } from 'cmssmarteditcontainer/services/cmsSmarteditServicesModule';
import { Workflow } from './dtos/Workflow';
import { WorkflowInboxAnnouncementService } from './services/WorkflowInboxAnnouncementService';
import './workflow.scss';

@SeModule({
    imports: [CmsSmarteditServicesModule, 'seConstantsModule'],
    declarations: [
        PageApprovalSelectorComponent,
        PageDisplayStatusComponent,
        PageWorkflowMenuComponent,
        WorkflowItemMenuComponent,
        WorkflowActionItemComponent,
        WorkflowActionCommentComponent,
        WorkflowInboxComponent,
        WorkflowInboxBadgeComponent,
        WorkflowInboxTaskComponent
    ],
    providers: [
        WorkflowService,
        WorkflowFacade,
        WorkflowTasksPollingService,
        WorkflowInboxAnnouncementService,
        {
            provide: 'OPEN_PAGE_WORKFLOW_MENU',
            useValue: 'OPEN_PAGE_WORKFLOW_MENU'
        },
        {
            provide: 'CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU',
            useValue: 'CMS_EVENT_OPEN_PAGE_WORKFLOW_MENU'
        },
        {
            provide: 'CMS_EVENT_OPEN_WORKFLOW_INBOX_DROPDOWN',
            useValue: 'CMS_EVENT_OPEN_WORKFLOW_INBOX_DROPDOWN'
        },
        {
            provide: 'WORKFLOW_FINISHED_EVENT',
            useValue: 'WORKFLOW_FINISHED_EVENT'
        },
        {
            provide: 'WORKFLOW_DECISION_SELECTED_EVENT',
            useValue: 'WORKFLOW_DECISION_SELECTED_EVENT'
        },
        {
            provide: 'WORKFLOW_TASKS_COUNT_UPDATED',
            useValue: 'WORKFLOW_TASKS_COUNT_UPDATED'
        },
        {
            provide: 'WORKFLOW_TASKS_MENU_OPENED_EVENT',
            useValue: 'WORKFLOW_TASKS_MENU_OPENED_EVENT'
        }
    ],
    initialize: (
        $rootScope: angular.IRootScopeService,
        $location: angular.ILocationService,
        crossFrameEventService: CrossFrameEventService,
        notificationService: INotificationService,
        pageInfoService: IPageInfoService,
        workflowService: WorkflowService,
        EVENTS: TypedMap<string>,
        STORE_FRONT_CONTEXT: string,
        workflowInboxAnnouncementService: WorkflowInboxAnnouncementService
    ) => {
        'ngInject';

        const NOTIFICATION_ID = 'PAGE_IN_WORKFLOW_NOTIFICATION_ID';
        const NOTIFICATION_TEMPLATE = 'pageInWorkflowNotificationTemplate.html';

        crossFrameEventService.subscribe(EVENTS.PAGE_CHANGE, () => {
            // Using pageInfoService.getPageUUID instaed of pageService.getCurrentPageInfo because pageInfoService uses information from the DOM
            // instead of fetching from the backend and hence preventing any race condition while clearing the cache.
            pageInfoService.getPageUUID().then((pageUuid: string) => {
                workflowService
                    .getActiveWorkflowForPageUuid(pageUuid)
                    .then((workflow: Workflow) => {
                        if (workflow !== null && !workflow.isAvailableForCurrentPrincipal) {
                            notificationService.pushNotification({
                                id: NOTIFICATION_ID,
                                templateUrl: NOTIFICATION_TEMPLATE
                            });
                        } else {
                            notificationService.removeNotification(NOTIFICATION_ID);
                        }
                    });
            });
        });

        $rootScope.$on('$routeChangeSuccess', () => {
            if ($location.path() !== STORE_FRONT_CONTEXT) {
                notificationService.removeNotification(NOTIFICATION_ID);
            }
        });
    }
})
export class WorkflowModule {}
