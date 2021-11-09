/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;

import java.util.Collection;
import java.util.Locale;


public class WorkflowActionModelBuilder
{
	private final WorkflowActionModel model;

	private WorkflowActionModelBuilder()
	{
		model = new WorkflowActionModel();
	}

	public static WorkflowActionModelBuilder aModel()
	{
		return new WorkflowActionModelBuilder();
	}

	public WorkflowActionModel build()
	{
		return this.getModel();
	}

	protected WorkflowActionModel getModel()
	{
		return this.model;
	}

	public WorkflowActionModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public WorkflowActionModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

	public WorkflowActionModelBuilder withWorkflow(final WorkflowModel workflow)
	{
		getModel().setWorkflow(workflow);
		return this;
	}

	public WorkflowActionModelBuilder withWorkflowActionTemplate(final WorkflowActionTemplateModel actionTemplate)
	{
		getModel().setTemplate(actionTemplate);
		return this;
	}

	public WorkflowActionModelBuilder withActionType(final WorkflowActionType actionType)
	{
		getModel().setActionType(actionType);
		return this;
	}

	public WorkflowActionModelBuilder withDecisions(final Collection<WorkflowDecisionModel> decisions)
	{
		getModel().setDecisions(decisions);
		return this;
	}
}
