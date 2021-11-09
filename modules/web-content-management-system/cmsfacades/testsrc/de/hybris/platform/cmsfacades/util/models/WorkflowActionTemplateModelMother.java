/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.util.builder.WorkflowActionTemplateModelBuilder;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class WorkflowActionTemplateModelMother extends AbstractModelMother<WorkflowActionTemplateModel>
{
	public static final String START_ACTION_NAME = "Start Action";
	public static final String START_ACTION_CODE = "StartAction";

	private WorkflowTemplateModelMother workflowTemplateModelMother;

	public WorkflowActionTemplateModel startApprovalWorkflowAction(final List<CatalogVersionModel> catalogVersions)
	{
		final WorkflowTemplateModel workflowTemplate = getWorkflowTemplateModelMother()
				.createApprovalWorkflowTemplate(catalogVersions);
		return createWorkflowAction(workflowTemplate, START_ACTION_CODE, START_ACTION_NAME, WorkflowActionType.START);
	}

	public WorkflowActionTemplateModel startTranslationWorkflowAction(final List<CatalogVersionModel> catalogVersions)
	{
		final WorkflowTemplateModel workflowTemplate = getWorkflowTemplateModelMother()
				.createTranslationWorkflowTemplate(catalogVersions);
		return createWorkflowAction(workflowTemplate, START_ACTION_CODE, START_ACTION_NAME, WorkflowActionType.START);
	}

	protected WorkflowActionTemplateModel createWorkflowAction(final WorkflowTemplateModel workflowTemplate, final String code,
			final String name, final WorkflowActionType actionType)
	{
		return saveModel(() -> WorkflowActionTemplateModelBuilder.aModel() //
				.withCode(code) //
				.withWorkflowTemplate(workflowTemplate) //
				.withName(name, Locale.ENGLISH) //
				.withType(actionType).build());
	}

	protected WorkflowTemplateModelMother getWorkflowTemplateModelMother()
	{
		return workflowTemplateModelMother;
	}

	@Required
	public void setWorkflowTemplateModelMother(final WorkflowTemplateModelMother workflowTemplateModelMother)
	{
		this.workflowTemplateModelMother = workflowTemplateModelMother;
	}
}
