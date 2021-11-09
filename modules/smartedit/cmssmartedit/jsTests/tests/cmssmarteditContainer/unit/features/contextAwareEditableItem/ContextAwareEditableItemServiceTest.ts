/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { WorkflowService } from 'cmssmarteditcontainer/components/workflow/services/WorkflowService';
import { ContextAwareEditableItemService } from 'cmssmarteditcontainer/services/contextAwareEditableItem/ContextAwareEditableItemServiceOuter';
import { promiseHelper } from 'testhelpers';

describe('ContextAwareEditableItemServiceTest', () => {
    let workflowService: jasmine.SpyObj<WorkflowService>;
    let service: ContextAwareEditableItemService;
    let pageService: any;
    const ITEM_UID = 'itemUid';
    const EDITABLE_IN_WORFKLOW_CODE = 'editableInWorkflow';
    const ANOTHER_WORKFLOW_CODE = 'anotherWorkflowCode';
    const PREVIEWED_PAGE_UID = 'previewedPageUid';
    const $q: jasmine.SpyObj<angular.IQService> = promiseHelper.$q();

    beforeEach(() => {
        workflowService = jasmine.createSpyObj<WorkflowService>('workflowService', [
            'getWorkflowEditableItems',
            'getActiveWorkflowForPageUuid'
        ]);
        pageService = jasmine.createSpyObj<any>('pageService', ['getCurrentPageInfo']);
        service = new ContextAwareEditableItemService(workflowService, pageService);

        pageService.getCurrentPageInfo.and.returnValue(
            $q.when({
                uid: PREVIEWED_PAGE_UID
            })
        );

        workflowService.getActiveWorkflowForPageUuid.and.returnValue(
            $q.when({
                workflowCode: EDITABLE_IN_WORFKLOW_CODE
            })
        );
    });

    it('should return true if the item is editable and active workflow same as the previewed page workflow', () => {
        // GIVEN
        workflowService.getWorkflowEditableItems.and.returnValue(
            $q.when([
                {
                    uid: ITEM_UID,
                    uuid: ITEM_UID,
                    editableByUser: true,
                    editableInWorkflow: EDITABLE_IN_WORFKLOW_CODE
                }
            ])
        );

        // WHEN
        const result = service.isItemEditable(ITEM_UID);

        // THEN
        result.then((editable) => {
            expect(editable).toBe(true);
        });
    });

    it('should return false if the item is editable and active workflow not the same as the previewed page workflow', () => {
        // GIVEN
        workflowService.getWorkflowEditableItems.and.returnValue(
            $q.when([
                {
                    uid: ITEM_UID,
                    uuid: ITEM_UID,
                    editableByUser: true,
                    editableInWorkflow: EDITABLE_IN_WORFKLOW_CODE
                }
            ])
        );
        workflowService.getActiveWorkflowForPageUuid.and.returnValue(
            $q.when({
                workflowCode: ANOTHER_WORKFLOW_CODE
            })
        );

        // WHEN
        const result = service.isItemEditable(ITEM_UID);

        // THEN
        result.then((editable) => {
            expect(editable).toBe(false);
        });
    });

    it('should return false if the item is not editable', () => {
        // GIVEN
        workflowService.getWorkflowEditableItems.and.returnValue(
            $q.when([
                {
                    uid: ITEM_UID,
                    uuid: ITEM_UID,
                    editableByUser: false
                }
            ])
        );

        // WHEN
        const result = service.isItemEditable(ITEM_UID);

        // THEN
        result.then((editable) => {
            expect(editable).toBe(false);
        });
    });

    it('should return true if the item is editable and the item is in workflow and there is no context', function() {
        // GIVEN
        workflowService.getWorkflowEditableItems.and.returnValue(
            $q.when([
                {
                    uid: ITEM_UID,
                    uuid: ITEM_UID,
                    editableByUser: true,
                    editableInWorkflow: EDITABLE_IN_WORFKLOW_CODE
                }
            ])
        );
        pageService.getCurrentPageInfo.and.callFake(function() {
            return $q.reject();
        });

        // WHEN
        const result = service.isItemEditable(ITEM_UID);

        // THEN
        result.then((editable) => {
            expect(editable).toBe(true);
        });
    });
});
