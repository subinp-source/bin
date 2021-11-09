/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.v2.controllers;

import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.b2bocc.v2.helper.OrgUnitUserGroupsHelper;
import de.hybris.platform.b2bapprovalprocessfacades.company.B2BPermissionFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUserGroupFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BSelectionDataWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserGroupListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserGroupWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserListWsDTO;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.AlreadyExistsException;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@ApiVersion("v2")
@Api(tags = "Organizational Unit User Groups")
public class OrgUnitUserGroupsController extends BaseController
{
	private static final Logger LOG = LoggerFactory.getLogger(OrgUnitUserGroupsController.class);

	private static final String USERGROUP_NOT_FOUND_ERROR_MESSAGE = "Member Permission not found";
	private static final String USERGROUP_ALREADY_EXISTS_ERROR_MESSAGE = "Member Permission with the same id already exists";
	private static final String OBJECT_NAME_ORG_USERGROUP = "orgUserGroup";

	protected static final String ILLEGAL_ARGUMENT_ERROR_MESSAGE = "Illegal argument error.";

	@Resource(name = "orgUnitUserGroupsHelper")
	protected OrgUnitUserGroupsHelper orgUnitUserGroupsHelper;

	@Resource(name = "b2bUserGroupFacade")
	protected B2BUserGroupFacade b2bUserGroupFacade;

	@Resource(name = "b2bPermissionFacade")
	protected B2BPermissionFacade b2bPermissionFacade;

	@Resource(name = "orgUnitUserGroupWsDTOValidator")
	private Validator orgUnitUserGroupWsDTOValidator;

	@Resource(name = "wsUserFacade")
	private UserFacade wsUserFacade;

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getOrgUnitUserGroups", value = "Gets the list of organizational unit user groups for a specified base store", notes = "Returns the list of organizational unit user groups accessible for a specified base store.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = "/orgUnitUserGroups", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public OrgUnitUserGroupListWsDTO getOrgUnitUserGroups(
			@ApiParam(value = "The current result page requested.", required = false) @RequestParam(value = "currentPage", defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.", required = false) @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.", required = false) @RequestParam(value = "sort", defaultValue = B2BUserGroupModel.UID) final String sort,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return orgUnitUserGroupsHelper.searchUserGroups(currentPage, pageSize, sort, addPaginationField(fields));
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getOrgUnitUserGroup", value = "Gets specific organizational unit user group details accessible for a specified base store based on user group code", notes = "Returns specific UserGroup details accessible for a specified base store based on UserGroup code. The response contains detailed order information.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public OrgUnitUserGroupWsDTO getOrgUnitUserGroup(
			@ApiParam(value = "Organizational unit user group identifier", required = true) @PathVariable final String orgUnitUserGroupId,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final B2BUserGroupData userGroupData = b2bUserGroupFacade.getB2BUserGroup(orgUnitUserGroupId);

		if (userGroupData == null)
		{
			throw new NotFoundException(USERGROUP_NOT_FOUND_ERROR_MESSAGE);
		}

		return getDataMapper().map(userGroupData, OrgUnitUserGroupWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "updateOrgUnitUserGroup", value = "Updates the organizational unit user group", notes = "Updates the organizational unit user group. Only attributes provided in the request body will be changed.", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}", method = RequestMethod.PATCH)
	@ApiBaseSiteIdAndUserIdParam
	public void updateOrgUnitUserGroup(
			@ApiParam(value = "Organizational unit user group object.", required = true) @RequestBody final OrgUnitUserGroupWsDTO orgUnitUserGroup,
			@ApiParam(value = "Organizational unit user group identifier", required = true) @PathVariable final String orgUnitUserGroupId)
	{
		final B2BUserGroupData userGroupData = b2bUserGroupFacade.getB2BUserGroup(orgUnitUserGroupId);
		if (userGroupData == null)
		{
			throw new NotFoundException(USERGROUP_NOT_FOUND_ERROR_MESSAGE);
		}

		getDataMapper().map(orgUnitUserGroup, userGroupData, false);

		final OrgUnitUserGroupWsDTO orgUnitUserGroupToBeValidated = getDataMapper().map(userGroupData, OrgUnitUserGroupWsDTO.class);
		if (!StringUtils.equals(orgUnitUserGroupId, orgUnitUserGroupToBeValidated.getUid()) && !isUserGroupIdUnique(
				orgUnitUserGroupToBeValidated.getUid()))
		{
			throw new AlreadyExistsException(USERGROUP_ALREADY_EXISTS_ERROR_MESSAGE);
		}
		validate(orgUnitUserGroupToBeValidated, OBJECT_NAME_ORG_USERGROUP, orgUnitUserGroupWsDTOValidator);

		b2bUserGroupFacade.updateUserGroup(orgUnitUserGroupId, userGroupData);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "createOrgUnitUserGroup", value = " Creates a new organizational unit user group", notes = "Creates a new organizational unit user group.", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value = "/orgUnitUserGroups", method = RequestMethod.POST)
	@ApiBaseSiteIdAndUserIdParam
	public OrgUnitUserGroupWsDTO createOrgUnitUserGroup(
			@ApiParam(value = "Organizational unit user group object.", required = true) @RequestBody final OrgUnitUserGroupWsDTO orgUnitUserGroup,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		validate(orgUnitUserGroup, OBJECT_NAME_ORG_USERGROUP, orgUnitUserGroupWsDTOValidator);

		if (!isUserGroupIdUnique(orgUnitUserGroup.getUid()))
		{
			throw new AlreadyExistsException(USERGROUP_ALREADY_EXISTS_ERROR_MESSAGE);
		}

		final B2BUserGroupData userGroupData = getDataMapper().map(orgUnitUserGroup, B2BUserGroupData.class);

		b2bUserGroupFacade.updateUserGroup(orgUnitUserGroup.getUid(), userGroupData);

		final B2BUserGroupData createdUserGroupData = b2bUserGroupFacade.getB2BUserGroup(orgUnitUserGroup.getUid());

		return getDataMapper().map(createdUserGroupData, OrgUnitUserGroupWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "removeOrgUnitUserGroup", value = "Removes the organizational unit user group", notes = "Removes the organizational unit user group.", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}", method = RequestMethod.DELETE)
	@ApiBaseSiteIdAndUserIdParam
	public void removeOrgUnitUserGroup(
			@ApiParam(value = "Organizational unit user group identifier", required = true) @PathVariable final String orgUnitUserGroupId)
	{
		b2bUserGroupFacade.removeUserGroup(orgUnitUserGroupId);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getOrderApprovalPermissionsForOrgUnitUserGroup", value = "Gets the list of order approval permissions for a specified organizational unit user group", notes = "Returns the list of order approval permissions  who can belong to a specific organizational unit user group. Order approval permissions  who already belong to the user group are flagged by 'selected' attribute.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}/availableOrderApprovalPermissions", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public B2BPermissionListWsDTO getOrderApprovalPermissionsForOrgUnitUserGroup(
			@ApiParam(value = "The current result page requested.") @RequestParam(value = "currentPage", defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.") @RequestParam(value = "sort", defaultValue = UserModel.NAME) final String sort,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields,
			@ApiParam(value = "Organizational unit user group identifier", required = true) @PathVariable final String orgUnitUserGroupId)
	{
		return orgUnitUserGroupsHelper
				.searchPermissionsForOrgUnitUserGroup(orgUnitUserGroupId, currentPage, pageSize, sort, addPaginationField(fields));
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}/orderApprovalPermissions", method = RequestMethod.POST)
	@ApiOperation(nickname = "doAddOrderApprovalPermissionToOrgUnitUserGroup", value = "Add an order approval permission to a specific organizational unit user group", notes = "Adds an order approval permission to a specific organizational unit user group", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public B2BSelectionDataWsDTO addOrderApprovalPermissionToOrgUnitUserGroup(
			@ApiParam(value = "Identifier of the organizational unit user group which the order approval permission will be added.", required = true) @PathVariable final String orgUnitUserGroupId,
			@ApiParam(value = "Order approval permission identifier which is added to the organizational unit user group.", required = true) @RequestParam final String orderApprovalPermissionCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BSelectionData selectionData = b2bPermissionFacade.addPermissionToUserGroup(orgUnitUserGroupId,
				orderApprovalPermissionCode);
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}/orderApprovalPermissions/{orderApprovalPermissionCode}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(nickname = "removeOrderApprovalPermissionFromOrgUnitUserGroup", value = "Remove an order approval permission from a specific organizational unit user group", notes = "Removes an order approval permission from a specific organizational unit user group", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BSelectionDataWsDTO removeOrderApprovalPermissionFromOrgUnitUserGroup(
			@ApiParam(value = "Organizational unit user group identifier which is added to the organizational customer.", required = true) @PathVariable final String orgUnitUserGroupId,
			@ApiParam(value = "Order approval permission identifier which is removed from the organizational unit user group.", required = true) @PathVariable final String orderApprovalPermissionCode,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BSelectionData selectionData = b2bPermissionFacade.removePermissionFromUserGroup(orgUnitUserGroupId,
				orderApprovalPermissionCode);
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getAvailableOrgCustomersForUserGroup", value = "Gets the list of organizational customers for a specified organizational unit user group", notes = "Returns the list of organizational customers who can belong to a specific organizational unit user group. Users who already belong to the user group are flagged by 'selected' attribute.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}/availableOrgCustomers", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public OrgUnitUserListWsDTO getAvailableOrgCustomersForUserGroup(
			@ApiParam(value = "The current result page requested.") @RequestParam(value = "currentPage", defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.") @RequestParam(value = "sort", defaultValue = UserModel.NAME) final String sort,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields,
			@ApiParam(value = "Organizational unit user group identifier", required = true) @PathVariable final String orgUnitUserGroupId)
	{
		return orgUnitUserGroupsHelper
				.searchOrgCustomersForUserGroup(orgUnitUserGroupId, currentPage, pageSize, sort, addPaginationField(fields));
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}/members", method = RequestMethod.POST)
	@ApiOperation(nickname = "doAddOrgCustomerToOrgUnitUserGroupMembers", value = "Add an organizational customer to a specific unit user group members", notes = "Adds an organizational customer to a specific unit user group members", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(value = HttpStatus.CREATED)
	public void addOrgCustomerToOrgUnitUserGroupMembers(
			@ApiParam(value = "Identifier of the organizational customer which will be added to the organizational unit user group members", required = true) @RequestParam final String orgCustomerId,
			@ApiParam(value = "Organizational unit user group identifier", required = true) @PathVariable final String orgUnitUserGroupId)
	{
		b2bUserGroupFacade.addMemberToUserGroup(orgUnitUserGroupId, wsUserFacade.getUserUID(orgCustomerId));
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}/members/{orgCustomerId:.+}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(nickname = "removeOrgCustomerFromOrgUnitUserGroupMembers", value = "Remove an organizational customer from the organizational unit user group members", notes = "Removes an organizational customer from the organizational unit user group members", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public void removeOrgCustomerFromOrgUnitUserGroupMembers(
			@ApiParam(value = "Identifier of the organizational customer which will be removed from the organizational unit user group members", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "Organizational unit user group identifier", required = true) @PathVariable final String orgUnitUserGroupId)
	{
		b2bUserGroupFacade.removeMemberFromUserGroup(orgUnitUserGroupId, wsUserFacade.getUserUID(orgCustomerId));
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnitUserGroups/{orgUnitUserGroupId}/members", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(nickname = "removeOrgUnitUserGroupMembers", value = "Removes all organizational customers in the organizational unit user group members", notes = "Removes all organizational customers in the organizational unit user group members which marks the user group disabled until a new member is added", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public void removeOrgUnitUserGroupMembers(
			@ApiParam(value = "Organizational unit user group identifier", required = true) @PathVariable final String orgUnitUserGroupId)
	{
		b2bUserGroupFacade.disableUserGroup(orgUnitUserGroupId);
	}

	protected boolean isUserGroupIdUnique(String orgUnitUserGroupId)
	{
		return b2bUserGroupFacade.getUserGroupDataForUid(orgUnitUserGroupId) == null;
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler({ ModelSavingException.class, NullPointerException.class })
	public ErrorListWsDTO handleIllegalArgumentException(final Exception ex)
	{
		LOG.debug("IllegalArgumentException", ex);
		return handleErrorInternal(IllegalArgumentException.class.getSimpleName(), ILLEGAL_ARGUMENT_ERROR_MESSAGE);
	}
}

