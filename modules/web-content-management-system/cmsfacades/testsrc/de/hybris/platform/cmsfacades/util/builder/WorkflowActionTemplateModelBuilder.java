/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Locale;


public class WorkflowActionTemplateModelBuilder
{
	private final WorkflowActionTemplateModel model;

	private WorkflowActionTemplateModelBuilder()
	{
		model = new WorkflowActionTemplateModel();
	}

	public static WorkflowActionTemplateModelBuilder aModel()
	{
		return new WorkflowActionTemplateModelBuilder();
	}

	public WorkflowActionTemplateModel build()
	{
		return this.getModel();
	}

	protected WorkflowActionTemplateModel getModel()
	{
		return this.model;
	}

	public WorkflowActionTemplateModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public WorkflowActionTemplateModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

	public WorkflowActionTemplateModelBuilder withWorkflowTemplate(final WorkflowTemplateModel template)
	{
		getModel().setWorkflow(template);
		return this;
	}

	public WorkflowActionTemplateModelBuilder withType(final WorkflowActionType type)
	{
		getModel().setActionType(type);
		return this;
	}
}
