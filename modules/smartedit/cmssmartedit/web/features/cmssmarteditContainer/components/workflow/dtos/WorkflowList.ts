/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Workflow } from './Workflow';

/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:IWorkflowList
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowService WorkflowService} to query
 * workflows.
 */
export interface WorkflowList {
    workflows: Workflow[];
}
