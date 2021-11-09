/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { WorkflowEditableItem } from './WorkflowEditableItem';

/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:ItemsRelatedWorkflows
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowService WorkflowService} to represent a
 * list of editable items from some workflow.
 */
export interface WorkflowEditableItemsList {
    editableItems: WorkflowEditableItem[];
}
