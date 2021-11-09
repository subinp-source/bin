/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmswebservices.workflows.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.API_VERSION;

import de.hybris.platform.cmsfacades.data.CMSWorkflowData;
import de.hybris.platform.cmsfacades.data.CMSWorkflowOperationData;
import de.hybris.platform.cmsfacades.workflow.CMSWorkflowFacade;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowOperationWsDTO;
import de.hybris.platform.cmswebservices.dto.CMSWorkflowWsDTO;
import de.hybris.platform.cmswebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Api;


/**
 * Controller that provides an API to perform different operations on workflows.
 */
@Controller
@IsAuthorizedCmsManager
@RequestMapping(API_VERSION + "/catalogs/{catalogId}/versions/{versionId}/workflows/{workflowId}/operations")
@Api(tags = "workflow operations")
public class CMSWorkflowOperationsController
{
	@Resource
	private DataMapper dataMapper;

	@Resource
	private CMSWorkflowFacade cmsWorkflowFacade;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation(value = "Performs different operations on the workflow item.", notes = "Executes various actions, such as canceling a workflow, on the workflow item.",
					nickname = "doPerfomActionOnWorkflow")
	@ApiImplicitParams(
	{ //
			@ApiImplicitParam(name = "catalogId", value = "The catalog identifier", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The catalog version identifier", required = true, dataType = "string", paramType = "path") })
	@ApiResponses(
	{ //
			@ApiResponse(code = 404, message = "When the item has not been found (UnknownIdentifierException) "),
			@ApiResponse(code = 400, message = "When the payload does not have the 'operation' property. (IllegalArgumentException)"),
			@ApiResponse(code = 200, message = "The workflow item.", response = CMSWorkflowWsDTO.class) })
	public CMSWorkflowWsDTO perform(
			@ApiParam(value = "The code of the workflow", required = true) @PathVariable final String workflowId,
			@ApiParam(value = "The DTO object containing all the information about operation to be performed", required = true) @RequestBody final CMSWorkflowOperationWsDTO dto)
			throws UnknownIdentifierException
	{
		final CMSWorkflowOperationData data = getDataMapper().map(dto, CMSWorkflowOperationData.class);
		final CMSWorkflowData newWorkflowData = getCmsWorkflowFacade().performOperation(workflowId, data);
		return getDataMapper().map(newWorkflowData, CMSWorkflowWsDTO.class);
	}

	public DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}

	protected CMSWorkflowFacade getCmsWorkflowFacade()
	{
		return cmsWorkflowFacade;
	}

	public void setCmsWorkflowFacade(final CMSWorkflowFacade cmsWorkflowFacade)
	{
		this.cmsWorkflowFacade = cmsWorkflowFacade;
	}

}
