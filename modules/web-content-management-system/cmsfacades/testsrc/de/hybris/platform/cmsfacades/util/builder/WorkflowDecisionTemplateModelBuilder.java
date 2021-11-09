/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;

import java.util.Locale;


public class WorkflowDecisionTemplateModelBuilder
{

	private final WorkflowDecisionTemplateModel model;

	private WorkflowDecisionTemplateModelBuilder()
	{
		model = new WorkflowDecisionTemplateModel();
	}

	public static WorkflowDecisionTemplateModelBuilder aModel()
	{
		return new WorkflowDecisionTemplateModelBuilder();
	}

	public WorkflowDecisionTemplateModel build()
	{
		return this.getModel();
	}

	protected WorkflowDecisionTemplateModel getModel()
	{
		return this.model;
	}

	public WorkflowDecisionTemplateModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

	public WorkflowDecisionTemplateModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}


	public WorkflowDecisionTemplateModelBuilder withDescription(final String description)
	{
		getModel().setDescription(description);
		return this;
	}

	public WorkflowDecisionTemplateModelBuilder withActionTemplate(final WorkflowActionTemplateModel workflowActionTemplateModel)
	{
		getModel().setActionTemplate(workflowActionTemplateModel);
		return this;
	}
}
