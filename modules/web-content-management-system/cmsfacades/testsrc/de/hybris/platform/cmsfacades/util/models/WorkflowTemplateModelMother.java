/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.util.models;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmsfacades.util.builder.WorkflowTemplateModelBuilder;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.workflow.daos.WorkflowTemplateDao;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Required;


public class WorkflowTemplateModelMother extends AbstractModelMother<WorkflowTemplateModel>
{
	public static final String APPROVAL_WORKFLOW_TEMPLATE_CODE = "PageApproval";
	public static final String APPROVAL_WORKFLOW_TEMPLATE_NAME = "Page Approval";

	public static final String TRANSLATION_WORKFLOW_TEMPLATE_CODE = "PageTranslation";
	public static final String TRANSLATION_WORKFLOW_TEMPLATE_NAME = "Page Translation and Approval";

	private UserDao userDao;
	private WorkflowTemplateDao workflowTemplateDao;

	public WorkflowTemplateModel createApprovalWorkflowTemplate(final List<CatalogVersionModel> catalogVersions)
	{
		return createWorkflowTemplate(APPROVAL_WORKFLOW_TEMPLATE_CODE, APPROVAL_WORKFLOW_TEMPLATE_NAME, catalogVersions);
	}

	public WorkflowTemplateModel createTranslationWorkflowTemplate(final List<CatalogVersionModel> catalogVersions)
	{
		return createWorkflowTemplate(TRANSLATION_WORKFLOW_TEMPLATE_CODE, TRANSLATION_WORKFLOW_TEMPLATE_NAME, catalogVersions);
	}

	protected WorkflowTemplateModel createWorkflowTemplate(final String code, final String name,
			final List<CatalogVersionModel> catalogVersions)
	{
		return getOrSaveAndReturn(
				() -> getWorkflowTemplateDao().findWorkflowTemplatesByCode(code).stream().findFirst().orElse(null),
				() -> WorkflowTemplateModelBuilder.aModel() //
					.withName(name, Locale.ENGLISH) //
					.withCode(code) //
					.withVisibleForPrincipals(Collections.singletonList(getUserDao().findUserByUID("cmsmanager"))) //
					.withCatalogVersions(catalogVersions).build());
	}

	protected UserDao getUserDao()
	{
		return userDao;
	}

	@Required
	public void setUserDao(final UserDao userDao)
	{
		this.userDao = userDao;
	}

	protected WorkflowTemplateDao getWorkflowTemplateDao()
	{
		return workflowTemplateDao;
	}

	@Required
	public void setWorkflowTemplateDao(WorkflowTemplateDao workflowTemplateDao)
	{
		this.workflowTemplateDao = workflowTemplateDao;
	}
}
