/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.cmsfacades.util.builder.WorkflowDecisionTemplateModelBuilder;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class WorkflowDecisionTemplateModelMother extends AbstractModelMother<WorkflowDecisionTemplateModel>
{
	public static final String APPROVE_DECISION = "APPROVE";
	public static final String REJECT_DECISION = "REJECT";

	private WorkflowActionTemplateModelMother workflowActionTemplateModelMother;

	public WorkflowDecisionTemplateModel createWorkflowApproveDecision(
			final WorkflowActionTemplateModel workflowActionTemplateModel)
	{
		return createWorkflowDecision(workflowActionTemplateModel, APPROVE_DECISION);
	}

	public WorkflowDecisionTemplateModel createWorkflowRejectDecision(
			final WorkflowActionTemplateModel workflowActionTemplateModel)
	{
		return createWorkflowDecision(workflowActionTemplateModel, REJECT_DECISION);
	}

	protected WorkflowDecisionTemplateModel createWorkflowDecision(final WorkflowActionTemplateModel workflowActionTemplateModel,
			final String name)
	{
		return saveModel(() -> WorkflowDecisionTemplateModelBuilder.aModel() //
				.withActionTemplate(workflowActionTemplateModel) //
				.withName(name, Locale.ENGLISH) //
				.withCode(name) //
				.build());
	}

	protected WorkflowActionTemplateModelMother getWorkflowActionTemplateModelMother()
	{
		return workflowActionTemplateModelMother;
	}

	@Required
	public void setWorkflowActionTemplateModelMother(final WorkflowActionTemplateModelMother workflowActionTemplateModelMother)
	{
		this.workflowActionTemplateModelMother = workflowActionTemplateModelMother;
	}

}
