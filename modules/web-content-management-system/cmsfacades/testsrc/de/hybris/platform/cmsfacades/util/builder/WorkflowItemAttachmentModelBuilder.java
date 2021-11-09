/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.builder;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;


public class WorkflowItemAttachmentModelBuilder
{
	private final WorkflowItemAttachmentModel model;

	private WorkflowItemAttachmentModelBuilder()
	{
		model = new WorkflowItemAttachmentModel();
	}

	private WorkflowItemAttachmentModelBuilder(final WorkflowItemAttachmentModel model)
	{
		this.model = model;
	}

	protected WorkflowItemAttachmentModel getModel()
	{
		return this.model;
	}

	public static WorkflowItemAttachmentModelBuilder aModel()
	{
		return new WorkflowItemAttachmentModelBuilder();
	}

	public static WorkflowItemAttachmentModelBuilder fromModel(final WorkflowItemAttachmentModel model)
	{
		return new WorkflowItemAttachmentModelBuilder(model);
	}

	public WorkflowItemAttachmentModel build()
	{
		return this.getModel();
	}

	public WorkflowItemAttachmentModelBuilder withWorkflow(final WorkflowModel workflow)
	{
		getModel().setWorkflow(workflow);
		return this;
	}

	public WorkflowItemAttachmentModelBuilder withJob(final CMSItemModel item)
	{
		getModel().setItem(item);
		return this;
	}
}
