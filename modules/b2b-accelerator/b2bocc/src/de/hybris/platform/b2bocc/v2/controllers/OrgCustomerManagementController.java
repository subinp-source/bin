/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2bocc.v2.controllers;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.b2bocc.v2.helper.OrderApprovalPermissionsHelper;
import de.hybris.platform.b2bocc.v2.helper.OrgCustomerManagementHelper;
import de.hybris.platform.b2bapprovalprocessfacades.company.B2BApproverFacade;
import de.hybris.platform.b2bapprovalprocessfacades.company.B2BPermissionFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.B2BUserFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BSelectionDataWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgCustomerCreationWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgCustomerModificationWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserGroupListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserListWsDTO;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.AlreadyExistsException;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import javax.annotation.Resource;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
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
@RequestMapping(value = "/{baseSiteId}/users/{userId}/orgCustomers")
@ApiVersion("v2")
@Api(tags = "Organizational Unit Customer Management")
public class OrgCustomerManagementController extends BaseController
{
	private static final String OBJECT_NAME_CUSTOMER = "customer";

	protected static final String RESOURCE_NOT_FOUND_ERROR_MESSAGE = "Requested resource cannot be found.";
	private static final String USER_ALREADY_EXISTS_ERROR_KEY = "User already exists";
	protected static final String MODEL_SAVING_ERROR_MESSAGE = "Model Saving error";
	private static final String USER_ADMIN_ROLE_DOWNGRADE_ERROR_KEY = "form.b2bcustomer.adminrole.error";
	private static final String USER_ADMIN_ROLE_PARENT_ROLE_CHANGE_ERROR_KEY = "form.b2bcustomer.parentunit.error";

	private static final Logger LOG = LoggerFactory.getLogger(OrgCustomerManagementController.class);

	@Resource(name = "orgCustomerManagementHelper")
	private OrgCustomerManagementHelper orgCustomerManagementHelper;
	@Resource(name = "orderApprovalPermissionsHelper")
	private OrderApprovalPermissionsHelper orderApprovalPermissionsHelper;
	@Resource(name = "b2bApproverFacade")
	private B2BApproverFacade b2bApproverFacade;
	@Resource(name = "b2bUserFacade")
	private B2BUserFacade b2bUserFacade;
	@Resource(name = "b2bPermissionFacade")
	private B2BPermissionFacade b2BPermissionFacade;
	@Resource(name = "b2bCustomerFacade")
	private CustomerFacade b2bCustomerFacade;
	@Resource(name = "b2bUnitFacade")
	protected B2BUnitFacade b2bUnitFacade;
	@Resource(name = "wsUserFacade")
	private UserFacade wsUserFacade;
	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;
	@Resource(name = "orgCustomerCreationWsDTOValidator")
	protected Validator orgCustomerCreationWsDTOValidator;
	@Resource(name = "orgCustomerModificationWsDTOValidator")
	protected Validator orgCustomerModificationWsDTOValidator;
	@Resource(name = "orgCustomerPasswordResetValidator")
	protected Validator orgCustomerPasswordResetValidator;

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{orgCustomerId}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(nickname = "getOrgCustomer", value = "Get a org customer profile", notes = "Returns a org customer profile.")
	@ApiBaseSiteIdAndUserIdParam
	public UserWsDTO getOrgCustomer(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "Response configuration. This is the list of fields that should be returned in the response body.", allowableValues = "BASIC, DEFAULT, FULL") @RequestParam(defaultValue = "DEFAULT") final String fields)
	{
		final CustomerData customerData = b2bCustomerFacade.getUserForUID(wsUserFacade.getUserUID(orgCustomerId));
		return dataMapper.map(customerData, UserWsDTO.class, fields);
	}

	@ApiOperation(nickname = "getOrgCustomers", value = "Gets the list of org customers for a specified base store", notes = "Returns the list of org customers for a specified base store. The response can display the results across multiple pages, if required.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public OrgUnitUserListWsDTO getOrgCustomers(
			@ApiParam(value = "The current result page requested.") @RequestParam(value = "currentPage", defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.") @RequestParam(value = "sort", defaultValue = CustomerModel.NAME) final String sort,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return orgCustomerManagementHelper.getCustomers(currentPage, pageSize, sort, addPaginationField(fields));
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(nickname = "createOrgCustomer", value = " Registers a org customer", notes = "Creates a new organizational customer.", consumes = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public UserWsDTO createOrgCustomer(
			@ApiParam(value = "Data object that contains information necessary for user creation", required = true) @RequestBody final OrgCustomerCreationWsDTO orgCustomerCreation,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		if (isUserExisting(orgCustomerCreation.getEmail()))
		{
			throw new AlreadyExistsException(USER_ALREADY_EXISTS_ERROR_KEY);
		}

		validate(orgCustomerCreation, OBJECT_NAME_CUSTOMER, orgCustomerCreationWsDTOValidator);

		final CustomerData orgCustomerData = getDataMapper().map(orgCustomerCreation, CustomerData.class);
		orgCustomerData.setDisplayUid(orgCustomerCreation.getEmail());

		b2bUserFacade.updateCustomer(orgCustomerData);

		final CustomerData updatedCustomerData = b2bUserFacade.getCustomerForUid(orgCustomerCreation.getEmail());

		return getDataMapper().map(updatedCustomerData, UserWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/{orgCustomerId}", method = RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(nickname = "updateOrgCustomer", value = "Updates org customer profile", notes = "Updates org customer profile. Only attributes provided in the request body will be changed.", consumes = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public void updateOrgCustomer(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "Data object which contains information necessary for user modification", required = true) @RequestBody final OrgCustomerModificationWsDTO orgCustomerModification)
	{
		final CustomerData orgCustomerData = b2bCustomerFacade.getUserForUID(wsUserFacade.getUserUID(orgCustomerId));

		if (isUserExisting(orgCustomerModification.getEmail()) && !StringUtils
				.equals(orgCustomerData.getUid(), orgCustomerModification.getEmail()))
		{
			throw new AlreadyExistsException(USER_ALREADY_EXISTS_ERROR_KEY);
		}

		Boolean active = orgCustomerModification.getActive();
		orgCustomerModification.setActive(null);

		//Email is not populated, but validated, populate it from displayUid
		orgCustomerData.setEmail(orgCustomerData.getDisplayUid());

		getDataMapper().map(orgCustomerModification, orgCustomerData, false);

		//Update the displayUid in case email is updated from WsDTO
		orgCustomerData.setDisplayUid(orgCustomerData.getEmail());

		final OrgCustomerModificationWsDTO orgCustomerModificationToBeValidated = getDataMapper()
				.map(orgCustomerData, OrgCustomerModificationWsDTO.class);
		validate(orgCustomerModificationToBeValidated, OBJECT_NAME_CUSTOMER, orgCustomerModificationWsDTOValidator);

		if (doesUserIdBelongToCurrentCustomer(orgCustomerData.getUid()))
		{

			boolean doRolesContainB2BAdmin = orgCustomerData.getRoles().stream().anyMatch(o -> o.equals(B2BConstants.B2BADMINGROUP));
			if (!doRolesContainB2BAdmin)
			{
				throw new ForbiddenException(USER_ADMIN_ROLE_DOWNGRADE_ERROR_KEY);
			}

			if (!isCurrentCustomerUnitIdEqualToUnitId(orgCustomerData.getUnit().getUid()))
			{
				throw new ForbiddenException(USER_ADMIN_ROLE_PARENT_ROLE_CHANGE_ERROR_KEY);
			}
		}

		if (orgCustomerModification.getPassword() != null)
		{
			validate(orgCustomerModification, OBJECT_NAME_CUSTOMER, orgCustomerPasswordResetValidator);
			b2bUserFacade.resetCustomerPassword(orgCustomerData.getUid(), orgCustomerModification.getPassword());
		}

		if (active != null)
		{
			if (active)
			{
				b2bUserFacade.enableCustomer(orgCustomerData.getUid());
			}
			else
			{
				b2bUserFacade.disableCustomer(orgCustomerData.getUid());
			}
		}

		b2bUserFacade.updateCustomer(orgCustomerData);
	}

	@ApiOperation(nickname = "getOrgCustomerApprovers", value = "Gets the list of approvers for an specified org customer", notes = "Returns the list of approvers for an specified org customer. The response can display the results across multiple pages, if required.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = "/{orgCustomerId}/approvers", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public OrgUnitUserListWsDTO getOrgCustomerApprovers(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "The current result page requested.", required = false) @RequestParam(value = "currentPage", defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.", required = false) @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.", required = false) @RequestParam(value = "sort", defaultValue = CustomerModel.NAME) final String sort,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return orgCustomerManagementHelper
				.getCustomerApprovers(wsUserFacade.getUserUID(orgCustomerId), currentPage, pageSize, sort, addPaginationField(fields));
	}

	@ApiOperation(nickname = "doAddApproverToOrgCustomer", value = "Add an approver to an specific org customer", notes = "Add an approver to an specific org customer", produces = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{orgCustomerId}/approvers/{approverId}", method = RequestMethod.POST)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public B2BSelectionDataWsDTO addApproverToOrgCustomer(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "Approver GUID (Globally Unique Identifier).", required = true) @PathVariable final String approverId,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BSelectionData selectionData = b2bApproverFacade.addApproverForCustomer(wsUserFacade.getUserUID(orgCustomerId),
				wsUserFacade.getUserUID(approverId));
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@ApiOperation(nickname = "removeApproverFromOrgCustomer", value = "Deletes an approver from an specific org customer with the provided approverId", notes = "Deletes an approver from an specific org customer with the provided approverId", produces = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{orgCustomerId}/approvers/{approverId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public B2BSelectionDataWsDTO removeApproverFromOrgCustomer(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "Approver GUID (Globally Unique Identifier).", required = true) @PathVariable final String approverId,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BSelectionData selectionData = b2bApproverFacade.removeApproverFromCustomer(wsUserFacade.getUserUID(orgCustomerId),
				wsUserFacade.getUserUID(approverId));
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@ApiOperation(nickname = "getOrgCustomerOrgUserGroups", value = "Gets the list of org user groups for a specified org customer", notes = "Returns the list of org user gruops for a specified org customer. The response can display the results across multiple pages, if required.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = "/{orgCustomerId}/orgUserGroups", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public OrgUnitUserGroupListWsDTO getOrgCustomerOrgUserGroups(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "The current result page requested.", required = false) @RequestParam(value = "currentPage", defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.", required = false) @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.", required = false) @RequestParam(value = "sort", defaultValue = CustomerModel.NAME) final String sort,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return orgCustomerManagementHelper
				.getOrgUnitUserGroups(wsUserFacade.getUserUID(orgCustomerId), currentPage, pageSize, sort, addPaginationField(fields));
	}

	@ApiOperation(nickname = "doAddOrgUserGroupToOrgCustomer", value = "Add an org user group to an specific org customer", notes = "Add an org user group to an specific org customer", produces = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{orgCustomerId}/orgUserGroups/{userGroupId}", method = RequestMethod.POST)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public B2BSelectionDataWsDTO addOrgUserGroupToOrgCustomer(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "Org User Group GUID (Globally Unique Identifier).", required = true) @PathVariable final String userGroupId,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BSelectionData selectionData = b2bUserFacade.addB2BUserGroupToCustomer(wsUserFacade.getUserUID(orgCustomerId), userGroupId);
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@ApiOperation(nickname = "removeOrgUserGroupFromOrgCustomer", value = "Deletes an org user group from an specific org customer with the provided orgUserGroupId", notes = "Deletes an org user group from an specific org customer with the provided orgUserGroupId", produces = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{orgCustomerId}/orgUserGroups/{userGroupId}", method = RequestMethod.DELETE)
	@ApiBaseSiteIdAndUserIdParam
	public void removeOrgUserGroupFromOrgCustomer(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "Org User Group GUID (Globally Unique Identifier).", required = true) @PathVariable final String userGroupId)
	{
		b2bUserFacade.removeB2BUserGroupFromCustomerGroups(wsUserFacade.getUserUID(orgCustomerId), userGroupId);
	}

	@ApiOperation(nickname = "getOrgCustomerPermissions", value = "Gets the list of permissions for an org customer", notes = "Returns the list of permissions for a user. The response can display the results across multiple pages, if required.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = "/{orgCustomerId}/permissions", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public B2BPermissionListWsDTO getOrgCustomerPermissions(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "The current result page requested.", required = false) @RequestParam(value = "currentPage", defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.", required = false) @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the returned results.", required = false) @RequestParam(value = "sort", defaultValue = CustomerModel.NAME) final String sort,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return orderApprovalPermissionsHelper
				.getPermissionsForCustomer(wsUserFacade.getUserUID(orgCustomerId), currentPage, pageSize, sort,
						addPaginationField(fields));
	}

	@ApiOperation(nickname = "doAddPermissionToOrgCustomer", value = "Add a permission to an specific org customer", notes = "Add a permission to an specific org customer", produces = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{orgCustomerId}/permissions/{permissionId}", method = RequestMethod.POST)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public B2BSelectionDataWsDTO addPermissionToOrgCustomer(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "Permission GUID (Globally Unique Identifier).", required = true) @PathVariable final String permissionId,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		try
		{
			final B2BSelectionData selectionData = b2BPermissionFacade
					.addPermissionToCustomer(wsUserFacade.getUserUID(orgCustomerId), permissionId);
			return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
		}
		catch (ModelSavingException ex)
		{
			LOG.debug("Exception:", ex);
			throw new NotFoundException(RESOURCE_NOT_FOUND_ERROR_MESSAGE);
		}
	}

	@ApiOperation(nickname = "removePermissionFromOrgCustomer", value = "Deletes a permission from an specific org customer with the provided permissionId", notes = "Deletes a permission from an specific org customer with the provided permissionId", produces = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/{orgCustomerId}/permissions/{permissionId}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public B2BSelectionDataWsDTO removePermissionFromOrgCustomer(
			@ApiParam(value = "Org Customer GUID (Globally Unique Identifier).", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "Permission GUID (Globally Unique Identifier).", required = true) @PathVariable final String permissionId,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final B2BSelectionData selectionData = b2BPermissionFacade
				.removePermissionFromCustomer(wsUserFacade.getUserUID(orgCustomerId), permissionId);
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler({ ModelSavingException.class })
	public ErrorListWsDTO handleModelSavingException(final Exception ex)
	{
		LOG.debug("Operation not successful", ex);
		return handleErrorInternal(ModelSavingException.class.getSimpleName(), MODEL_SAVING_ERROR_MESSAGE);
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler({ UnknownIdentifierException.class, NullPointerException.class })
	public ErrorListWsDTO handleNotFoundException(final Exception ex)
	{
		LOG.debug("Unknown identifier error", ex);
		return handleErrorInternal(NotFoundException.class.getSimpleName(), RESOURCE_NOT_FOUND_ERROR_MESSAGE);
	}

	protected boolean isUserExisting(String orgUnitUserId)
	{
		return orgUnitUserId != null && wsUserFacade.isUserExisting(orgUnitUserId);
	}

	protected boolean doesUserIdBelongToCurrentCustomer(String orgUnitUserId)
	{
		return b2bCustomerFacade.getCurrentCustomer().getUid().equals(orgUnitUserId);
	}

	protected boolean isCurrentCustomerUnitIdEqualToUnitId(String orgUnitId)
	{
		final B2BUnitData parentUnit = b2bUnitFacade.getParentUnit();

		return parentUnit.getUid().equals(orgUnitId);
	}
}
