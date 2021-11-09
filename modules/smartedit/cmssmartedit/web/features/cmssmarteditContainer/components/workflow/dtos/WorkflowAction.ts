/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { WorkflowDecision } from './WorkflowDecision';
import { WorkflowActionStatus } from './WorkflowActionStatus';

/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:WorkflowAction
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowService WorkflowService} to query
 * workflow action.
 */
export interface WorkflowAction {
    code: string;
    name: string;
    description: string;
    actionType: string;
    status: WorkflowActionStatus;
    startedAgoInMillis: number;
    isCurrentUserParticipant: boolean;
    decisions: WorkflowDecision[];
}
