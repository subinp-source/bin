/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.util.builder.WorkflowItemAttachmentModelBuilder;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;


public class WorkflowItemAttachmentModelMother extends AbstractModelMother<WorkflowItemAttachmentModel>
{
	public WorkflowItemAttachmentModel createAttachment(final WorkflowModel workflow, final CMSItemModel cmsItem)
	{
		return saveModel(() -> WorkflowItemAttachmentModelBuilder.aModel()
				.withWorkflow(workflow)
				.withJob(cmsItem)
				.build());
	}
}
