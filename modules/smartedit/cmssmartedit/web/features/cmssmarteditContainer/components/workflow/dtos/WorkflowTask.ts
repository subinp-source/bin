/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { WorkflowAction } from './WorkflowAction';
import { WorkflowTaskPage } from './WorkflowTaskPage';

/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:WorkflowTask
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowTasksPollingService WorkflowTasksPollingService} to represent a
 * workflow task.
 */
export interface WorkflowTask {
    action: WorkflowAction;
    attachments: WorkflowTaskPage[];
}
