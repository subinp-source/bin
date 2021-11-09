/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.workflowtemplates.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cmsfacades.workflow.CMSWorkflowTemplateFacade;
import de.hybris.platform.cmswebservices.dto.WorkflowTemplateListWsDTO;
import de.hybris.platform.cmswebservices.dto.WorkflowTemplateWsDTO;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

/**
 * Controller that provides an API to retrieve workflow templates for a given catalog version.
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/catalogs/{catalogId}/versions/{versionId}/workflowtemplates")
@Api(tags = "workflow templates")
public class WorkflowTemplateController
{

	@Resource
	private DataMapper dataMapper;

	@Resource
	private CMSWorkflowTemplateFacade workflowTemplateFacade;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Gets Workflow templates for catalog version.", notes = "Retrieves the list of available workflow templates for given catalogId and versionId.",
					nickname = "getWorkflowTemplatesForCatalogVersion")
	@ApiImplicitParams(
	{ //
			@ApiImplicitParam(name = "catalogId", value = "The catalog id", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	public WorkflowTemplateListWsDTO getWorkflowTemplatesForCatalogVersion(@PathVariable
	final String catalogId, @PathVariable
	final String versionId)
	{
		final WorkflowTemplateListWsDTO workflowTemplates = new WorkflowTemplateListWsDTO();
		final List<WorkflowTemplateWsDTO> worflowTemplateDataList = getDataMapper()
				.mapAsList(getWorkflowTemplateFacade().getWorkflowTemplates(catalogId, versionId), WorkflowTemplateWsDTO.class, null);
		workflowTemplates.setTemplates(worflowTemplateDataList);

		return workflowTemplates;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}

	protected CMSWorkflowTemplateFacade getWorkflowTemplateFacade()
	{
		return workflowTemplateFacade;
	}

	public void setWorkflowTemplateFacade(final CMSWorkflowTemplateFacade workflowTemplatesFacade)
	{
		this.workflowTemplateFacade = workflowTemplatesFacade;
	}

}
