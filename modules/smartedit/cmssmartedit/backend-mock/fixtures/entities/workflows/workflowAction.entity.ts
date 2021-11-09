/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export interface IWorkflowAction {
    actionType: string;
    code: string;
    decisions: { code: string; description: string; name: string }[];
    description: string;
    isCurrentUserParticipant: boolean;
    startedAgoInMillis: number;
    name: string;
    status: string;
}
