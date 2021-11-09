/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * The enum with a list of possible workflow action statuses.
 */
export enum WorkflowActionStatus {
    PENDING = 'pending',
    IN_PROGRESS = 'in_progress',
    PAUSED = 'paused',
    COMPLETED = 'completed',
    DISABLED = 'disabled',
    ENDED_THROUGH_END_OF_WORKFLOW = 'ended_through_end_of_workflow',
    TERMINATED = 'terminated'
}
