/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmssmarteditwebservices.workflow.controller;

import static de.hybris.platform.cmssmarteditwebservices.constants.CmssmarteditwebservicesConstants.API_VERSION;

import de.hybris.platform.cmsfacades.data.CMSWorkflowEditableItemData;
import de.hybris.platform.cmsfacades.workflow.CMSWorkflowFacade;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSWorkflowEditableItemListWsDTO;
import de.hybris.platform.cmssmarteditwebservices.dto.CMSWorkflowEditableItemWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value = API_VERSION + "/catalogs/{catalogId}/versions/{versionId}/workfloweditableitems")
@Api(tags = "workflows")
public class WorkflowEditableItemsController
{
	@Resource
	private DataMapper dataMapper;

	@Resource
	private CMSWorkflowFacade cmsWorkflowFacade;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiOperation( //
			value = "Retrieves the list of objects that tells whether a particular item is editable in any workflow or not", //
			notes = "Retrieves the list of objects that tells whether a particular item is editable in any workflow or not")
	@ApiImplicitParams(
	{
			@ApiImplicitParam(name = "catalogId", value = "The uid of the catalog", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "versionId", value = "The uid of the catalog version", required = true, dataType = "string", paramType = "path")
	})
	@ApiResponses(
	{ //
			@ApiResponse(code = 404, message = "When no cms item is found matching the given uid (UnknownIdentifierException)."),
			@ApiResponse(code = 200, message = "The dto containing the list of objects that tells whether a particular item is editable in any workflow or not.", response = CMSWorkflowEditableItemListWsDTO.class) })
	public CMSWorkflowEditableItemListWsDTO getEditableWorkflowItems( //
			@ApiParam(value = "List of item uids", required = true)
			@RequestParam("itemUids")
			final List<String> itemUids)
	{
		final List<CMSWorkflowEditableItemData> editableItems = getCmsWorkflowFacade().getSessionUserEditableItems(itemUids);
		final CMSWorkflowEditableItemListWsDTO currentUserEditableItemListWsDTO = new CMSWorkflowEditableItemListWsDTO();
		currentUserEditableItemListWsDTO
				.setEditableItems(getDataMapper().mapAsList(editableItems, CMSWorkflowEditableItemWsDTO.class, null));
		return currentUserEditableItemListWsDTO;
	}

	protected CMSWorkflowFacade getCmsWorkflowFacade()
	{
		return cmsWorkflowFacade;
	}

	public void setCmsWorkflowFacade(final CMSWorkflowFacade cmsWorkflowFacade)
	{
		this.cmsWorkflowFacade = cmsWorkflowFacade;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}
}
