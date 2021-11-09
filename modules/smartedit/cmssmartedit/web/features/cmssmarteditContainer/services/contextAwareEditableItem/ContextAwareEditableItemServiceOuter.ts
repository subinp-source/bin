/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, SeInjectable } from 'smarteditcommons';
import { WorkflowService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowService';
import { WorkflowEditableItem } from 'cmssmarteditcontainer/components/workflow/dtos/WorkflowEditableItem';
import { Workflow } from 'cmssmarteditcontainer/components/workflow/dtos/Workflow';
import { IContextAwareEditableItemService } from 'cmscommons/services';

@SeInjectable()
@GatewayProxied()
export class ContextAwareEditableItemService extends IContextAwareEditableItemService {
    constructor(private workflowService: WorkflowService, private pageService: any) {
        super();
    }

    public isItemEditable(itemUid: string): angular.IPromise<boolean> {
        return this.workflowService
            .getWorkflowEditableItems([itemUid])
            .then((workflowEditableItems: WorkflowEditableItem[]) => {
                const item = workflowEditableItems.find((data: WorkflowEditableItem) => {
                    return data.uid === itemUid;
                });
                if (item.editableInWorkflow) {
                    return this._editableInCurrentPageContext(item.editableInWorkflow).then(
                        (editable) => {
                            return item.editableByUser && editable;
                        }
                    );
                } else {
                    return item.editableByUser;
                }
            });
    }

    /**
     * Verifies whether the item's active workflow equals to the workflow of page currently in preview.
     * @param editableInWorkflow the workflow where the item can be edited.
     *
     * @returns {Promise} A promise that resolves to a boolean. It will be true, if the item is editable in page context, false otherwise.
     */
    private _editableInCurrentPageContext(editableInWorkflow: string): angular.IPromise<boolean> {
        return this.pageService.getCurrentPageInfo().then(
            (pageInfo: any) => {
                return this.workflowService
                    .getActiveWorkflowForPageUuid(pageInfo.uid)
                    .then((activeWorkflow: Workflow) => {
                        return (
                            activeWorkflow !== null &&
                            activeWorkflow.workflowCode === editableInWorkflow
                        );
                    });
            },
            (e: any) => {
                return true;
            }
        );
    }
}
