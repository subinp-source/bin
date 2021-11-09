/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:IWorkflowTemplate
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowService WorkflowService} to represent a
 * workflow template.
 */
export interface WorkflowTemplate {
    code: string;
    name: string;
}
