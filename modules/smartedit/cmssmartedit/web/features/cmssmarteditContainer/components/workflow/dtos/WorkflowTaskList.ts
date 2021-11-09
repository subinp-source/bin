/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { WorkflowTask } from './WorkflowTask';
import { Pagination } from 'smarteditcommons';

/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:WorkflowTaskList
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowTasksPollingService WorkflowTasksPollingService} to represent a
 * workflow task list.
 */
export interface WorkflowTaskList {
    tasks: WorkflowTask[];
    pagination: Pagination;
}
