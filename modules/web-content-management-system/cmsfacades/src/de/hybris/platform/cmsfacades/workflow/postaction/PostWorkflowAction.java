/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.postaction;

import de.hybris.platform.cmsfacades.data.CMSWorkflowOperationData;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.function.BiPredicate;


/**
 * Action to be performed after a workflow action is completed.
 */
public interface PostWorkflowAction
{
	BiPredicate<WorkflowModel, CMSWorkflowOperationData> isApplicable();

	void execute(WorkflowModel workflow);
}
