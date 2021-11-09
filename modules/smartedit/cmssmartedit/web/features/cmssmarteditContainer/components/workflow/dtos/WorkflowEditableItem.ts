/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:WorkflowEditableItem
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowService WorkflowService} to represent an
 * item that is editable in some workflow.
 */
export interface WorkflowEditableItem {
    uid: string;
    uuid: string;
    editableByUser: boolean;
    editableInWorkflow: string;
}
