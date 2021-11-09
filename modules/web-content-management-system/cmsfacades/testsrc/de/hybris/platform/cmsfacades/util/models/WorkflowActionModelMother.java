/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.cmsfacades.util.builder.WorkflowActionModelBuilder;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class WorkflowActionModelMother extends AbstractModelMother<WorkflowActionModel>
{
	private WorkflowDecisionModelMother workflowDecisionModelMother;

	public WorkflowActionModel createAction(final WorkflowActionTemplateModel actionTemplate, final WorkflowModel workflow)
	{
		final WorkflowActionModel actionModel = saveModel(() -> WorkflowActionModelBuilder.aModel()
				.withName(actionTemplate.getName(Locale.ENGLISH), Locale.ENGLISH)
				.withWorkflow(workflow)
				.withWorkflowActionTemplate(actionTemplate)
				.withActionType(actionTemplate.getActionType())
				.build());

		actionModel.setDecisions(Arrays.asList(getWorkflowDecisionModelMother().createWorkflowApproveDecision(actionModel),
				getWorkflowDecisionModelMother().createWorkflowRejectDecision(actionModel)));

		saveModel(() -> actionModel);

		return actionModel;
	}

	protected WorkflowDecisionModelMother getWorkflowDecisionModelMother()
	{
		return workflowDecisionModelMother;
	}

	@Required
	public void setWorkflowDecisionModelMother(
			WorkflowDecisionModelMother workflowDecisionModelMother)
	{
		this.workflowDecisionModelMother = workflowDecisionModelMother;
	}
}
