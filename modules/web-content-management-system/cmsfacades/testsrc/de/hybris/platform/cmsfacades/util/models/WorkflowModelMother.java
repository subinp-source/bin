/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cmsfacades.util.builder.WorkflowModelBuilder;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


public class WorkflowModelMother extends AbstractModelMother<WorkflowModel>
{
	public static final String APPROVAL_WORKFLOW_TEMPLATE_CODE = "PageApproval";
	public static final String APPROVAL_WORKFLOW_TEMPLATE_NAME = "Page Approval";

	private WorkflowItemAttachmentModelMother workflowItemAttachmentModelMother;
	private WorkflowActionModelMother workflowActionModelMother;
	private WorkflowProcessingService workflowProcessingService;

	public WorkflowModel createAndStartApprovalWorkflow(final WorkflowTemplateModel workflowTemplate,
			final List<CMSItemModel> attachments)
	{
		return createAndStartWorkflow(APPROVAL_WORKFLOW_TEMPLATE_CODE, APPROVAL_WORKFLOW_TEMPLATE_NAME, workflowTemplate,
				attachments);
	}

	protected WorkflowModel createAndStartWorkflow(final String code, final String name,
			final WorkflowTemplateModel workflowTemplate,
			final
			List<CMSItemModel> attachmentItems)
	{
		final WorkflowModel workflowModel = saveModel(() -> WorkflowModelBuilder.aModel()
						.withCode(code)
						.withName(name, Locale.ENGLISH)
						.withJob(workflowTemplate)
						.build());

		final List<WorkflowItemAttachmentModel> attachments = attachmentItems.stream()
				.map(cmsItem -> getWorkflowItemAttachmentModelMother().createAttachment(workflowModel, cmsItem))
				.collect(Collectors.toList());

		workflowModel.setAttachments(attachments);

		final List<WorkflowActionModel> actions = workflowTemplate.getActions().stream()
				.map(actionTemplate -> getWorkflowActionModelMother().createAction(actionTemplate, workflowModel))
				.collect(Collectors.toList());

		workflowModel.setActions(actions);

		saveModel(() -> workflowModel);

		if (getWorkflowProcessingService().startWorkflow(workflowModel))
		{
			workflowModel.setStatus(CronJobStatus.RUNNING);
			saveModel(() -> workflowModel);
		}

		return workflowModel;
	}

	protected WorkflowItemAttachmentModelMother getWorkflowItemAttachmentModelMother()
	{
		return workflowItemAttachmentModelMother;
	}

	@Required
	public void setWorkflowItemAttachmentModelMother(
			WorkflowItemAttachmentModelMother workflowItemAttachmentModelMother)
	{
		this.workflowItemAttachmentModelMother = workflowItemAttachmentModelMother;
	}

	protected WorkflowProcessingService getWorkflowProcessingService()
	{
		return workflowProcessingService;
	}

	@Required
	public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService)
	{
		this.workflowProcessingService = workflowProcessingService;
	}

	protected WorkflowActionModelMother getWorkflowActionModelMother()
	{
		return workflowActionModelMother;
	}

	@Required
	public void setWorkflowActionModelMother(WorkflowActionModelMother workflowActionModelMother)
	{
		this.workflowActionModelMother = workflowActionModelMother;
	}
}
