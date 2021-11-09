/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:WorkflowDecision
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowService WorkflowService} to query
 * workflow decision.
 */
export interface WorkflowDecision {
    code: string;
    name: string;
    description: string;
}
