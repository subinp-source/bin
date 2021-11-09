/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.workflow.service.CMSWorkflowTemplateService;
import de.hybris.platform.cmsfacades.data.WorkflowTemplateData;
import de.hybris.platform.cmsfacades.workflow.CMSWorkflowTemplateFacade;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link CMSWorkflowTemplateFacade}.
 */
public class DefaultCMSWorkflowTemplateFacade implements CMSWorkflowTemplateFacade
{

	private CatalogVersionService catalogVersionService;
	private CMSWorkflowTemplateService cmsWorkflowTemplateService;

	@Override
	public List<WorkflowTemplateData> getWorkflowTemplates(final String catalogId, final String versionId)
	{

		final CatalogVersionModel catalogVersionModel = getCatalogVersionService().getCatalogVersion(catalogId, versionId);

		return getCmsWorkflowTemplateService().getVisibleWorkflowTemplatesForCatalogVersion(catalogVersionModel).stream()
				.map(this::convertModelTodata).collect(Collectors.toList());

	}

	/**
	 * Converts the given {@link WorkflowTemplateModel} into its equivalent {@link WorkflowTemplateData} representation.
	 *
	 * @param workflowTemplate
	 *           The {@link WorkflowTemplateModel}
	 * @return The {@link WorkflowTemplateData}
	 */
	protected WorkflowTemplateData convertModelTodata(final WorkflowTemplateModel workflowTemplate)
	{
		final WorkflowTemplateData workflowTemplateData = new WorkflowTemplateData();
		workflowTemplateData.setCode(workflowTemplate.getCode());
		workflowTemplateData.setName(workflowTemplate.getName());
		return workflowTemplateData;
	}

	protected CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	@Required
	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	protected CMSWorkflowTemplateService getCmsWorkflowTemplateService()
	{
		return cmsWorkflowTemplateService;
	}

	@Required
	public void setCmsWorkflowTemplateService(final CMSWorkflowTemplateService cmsWorkflowTemplateService)
	{
		this.cmsWorkflowTemplateService = cmsWorkflowTemplateService;
	}

}
