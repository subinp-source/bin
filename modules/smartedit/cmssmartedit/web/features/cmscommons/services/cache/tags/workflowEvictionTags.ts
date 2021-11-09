/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { EvictionTag } from 'smarteditcommons';

export const workflowCompletedEvictionTag = new EvictionTag({ event: 'WORKFLOW_FINISHED_EVENT' });

export const workflowTasksMenuOpenedEvictionTag = new EvictionTag({
    event: 'WORKFLOW_TASKS_MENU_OPENED_EVENT'
});
