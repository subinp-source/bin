/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;
import java.util.Locale;


public class WorkflowModelBuilder
{
	private final WorkflowModel model;

	private WorkflowModelBuilder()
	{
		model = new WorkflowModel();
	}

	private WorkflowModelBuilder(final WorkflowModel model)
	{
		this.model = model;
	}

	protected WorkflowModel getModel()
	{
		return this.model;
	}

	public static WorkflowModelBuilder aModel()
	{
		return new WorkflowModelBuilder();
	}

	public static WorkflowModelBuilder fromModel(final WorkflowModel model)
	{
		return new WorkflowModelBuilder(model);
	}

	public WorkflowModel build()
	{
		return this.getModel();
	}

	public WorkflowModelBuilder withName(final String name, final Locale locale)
	{
		getModel().setName(name, locale);
		return this;
	}

	public WorkflowModelBuilder withCode(final String code)
	{
		getModel().setCode(code);
		return this;
	}

	public WorkflowModelBuilder withJob(final WorkflowTemplateModel workflowTemplate)
	{
		getModel().setJob(workflowTemplate);
		return this;
	}

}
