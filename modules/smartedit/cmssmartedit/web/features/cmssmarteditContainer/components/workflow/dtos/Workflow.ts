/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { WorkflowAction } from './WorkflowAction';
import { WorkflowStatus } from './WorkflowStatus';

/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:Workflow
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowService WorkflowService} to represent a
 * workflow.
 */
export interface Workflow {
    workflowCode: string;
    templateCode: string;
    description?: string;
    attachments?: string[];
    status?: WorkflowStatus;
    actions?: WorkflowAction[];
    isAvailableForCurrentPrincipal: boolean;
}
