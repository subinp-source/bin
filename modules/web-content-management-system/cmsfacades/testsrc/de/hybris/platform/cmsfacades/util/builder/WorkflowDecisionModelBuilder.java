/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

import java.util.Locale;


public class WorkflowDecisionModelBuilder
{

	private final WorkflowDecisionModel model;

	private WorkflowDecisionModelBuilder()
	{
		model = new WorkflowDecisionModel();
	}

	public static WorkflowDecisionModelBuilder aModel()
	{
		return new WorkflowDecisionModelBuilder();
	}

	public WorkflowDecisionModel build()
	{
		return this.getModel();
	}

	protected WorkflowDecisionModel getModel()
	{
		return this.model;
	}

	public WorkflowDecisionModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

	public WorkflowDecisionModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}


	public WorkflowDecisionModelBuilder withDescription(final String description)
	{
		getModel().setDescription(description);
		return this;
	}

	public WorkflowDecisionModelBuilder withAction(final WorkflowActionModel workflowActionModel)
	{
		getModel().setAction(workflowActionModel);
		return this;
	}
}
