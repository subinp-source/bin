/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.cmsfacades.util.builder.WorkflowDecisionModelBuilder;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import java.util.Locale;


public class WorkflowDecisionModelMother extends AbstractModelMother<WorkflowDecisionModel>
{
	public static final String APPROVE_DECISION = "APPROVE";
	public static final String REJECT_DECISION = "REJECT";

	public WorkflowDecisionModel createWorkflowApproveDecision(
			final WorkflowActionModel workflowActionModel)
	{
		return createWorkflowDecision(workflowActionModel, APPROVE_DECISION);
	}

	public WorkflowDecisionModel createWorkflowRejectDecision(
			final WorkflowActionModel workflowActionModel)
	{
		return createWorkflowDecision(workflowActionModel, REJECT_DECISION);
	}

	protected WorkflowDecisionModel createWorkflowDecision(final WorkflowActionModel workflowActionModel,
			final String name)
	{
		return saveModel(() -> WorkflowDecisionModelBuilder.aModel() //
				.withAction(workflowActionModel) //
				.withName(name, Locale.ENGLISH) //
				.withCode(name) //
				.build());
	}

}
