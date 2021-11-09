/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.v2.controllers;

import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.b2bocc.strategy.OrgUnitUserRoleManagementStrategy;
import de.hybris.platform.b2bocc.strategy.OrgUnitUsersDisplayStrategy;
import de.hybris.platform.b2bocc.strategy.UserRoleManagementStrategy;
import de.hybris.platform.b2bocc.v2.helper.OrgUnitsHelper;
import de.hybris.platform.b2bcommercefacades.company.B2BUnitFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitNodeData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BApprovalProcessListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BSelectionDataWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitNodeListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitNodeWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BUnitWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserListWsDTO;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.webservicescommons.errors.exceptions.AlreadyExistsException;
import de.hybris.platform.webservicescommons.errors.exceptions.NotFoundException;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.util.Map;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@ApiVersion("v2")
@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
@Api(tags = "Organizational Unit Management")
public class OrgUnitsController extends BaseController
{
	private static final String OBJECT_NAME_ORG_UNIT = "OrgUnit";

	private static final String UNIT_NOT_FOUND_MESSAGE = "Organizational unit with id [%s] was not found";
	private static final String ROLE_NOT_FOUND_MESSAGE = "Supplied parameter [%s] is not valid";
	private static final String UNIT_ALREADY_EXISTS_MESSAGE = "Organizational unit with uid [%s] already exists";
	private static final String ADDRESS_NOT_FOUND_MESSAGE = "Address with id [%s] is not found";

	@Resource(name = "b2bUnitFacade")
	protected B2BUnitFacade b2bUnitFacade;

	@Resource(name = "wsUserFacade")
	private UserFacade wsUserFacade;

	@Resource(name = "b2BUnitWsDTOValidator")
	protected Validator b2BUnitWsDTOValidator;

	@Resource(name = "addressDTOValidator")
	private Validator addressDTOValidator;

	@Resource(name = "orgUnitsHelper")
	private OrgUnitsHelper orgUnitsHelper;

	@Resource(name = "userRoleManagementStrategyMap")
	protected Map<String, UserRoleManagementStrategy> userRoleManagementStrategyMap;

	@Resource(name = "orgUnitUsersDisplayStrategyMap")
	protected Map<String, OrgUnitUsersDisplayStrategy> orgUnitUsersDisplayStrategyMap;

	@Resource(name = "orgUnitUserRoleManagementStrategyMap")
	protected Map<String, OrgUnitUserRoleManagementStrategy> orgUnitUserRoleManagementStrategyMap;

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getOrgUnit", value = "Get an organizational unit.", notes = "Returns a specific organizational unit based on specific id. The response contains detailed organizational unit information.", produces = MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/orgUnits/{orgUnitId}", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public B2BUnitWsDTO getOrgUnit(
			@ApiParam(value = "Organizational Unit identifier.", required = true) @PathVariable final String orgUnitId,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final B2BUnitData unitData = getUnitForUid(orgUnitId);
		return getDataMapper().map(unitData, B2BUnitWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnits", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ApiOperation(nickname = "createOrgUnit", value = "Create a new organizational unit.", notes = "Creates a new organizational unit.", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public B2BUnitWsDTO createUnit(
			@ApiParam(value = "Organizational Unit object.", required = true) @RequestBody final B2BUnitWsDTO orgUnit,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		validate(orgUnit, OBJECT_NAME_ORG_UNIT, b2BUnitWsDTOValidator);

		if (b2bUnitFacade.getUnitForUid(orgUnit.getUid()) != null)
		{
			throw new AlreadyExistsException(String.format(UNIT_ALREADY_EXISTS_MESSAGE, orgUnit.getUid()));
		}

		final B2BUnitData unitData = getDataMapper().map(orgUnit, B2BUnitData.class);
		b2bUnitFacade.updateOrCreateBusinessUnit(unitData.getUid(), unitData);

		final B2BUnitData createdUnitData = b2bUnitFacade.getUnitForUid(unitData.getUid());
		return getDataMapper().map(createdUnitData, B2BUnitWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getAvailableParentUnits", value = "Get available parent units.", notes = "Returns a list of parent units for which the unit with id can be assigned as a child.", produces = MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/orgUnits/{orgUnitId}/availableParents", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public B2BUnitNodeListWsDTO getAvailableParentUnits(
			@ApiParam(value = "Organizational Unit identifier.", required = true) @PathVariable final String orgUnitId,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return orgUnitsHelper.getAvailableParentUnits(orgUnitId, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "updateOrgUnit", value = "Update the organizational unit", notes = "Updates the organizational unit. Only attributes provided in the request body will be changed.", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/orgUnits/{orgUnitId}", method = RequestMethod.PATCH)
	@ApiBaseSiteIdAndUserIdParam
	public void updateOrgUnit(
			@ApiParam(value = "Organizational Unit identifier.", required = true) @PathVariable final String orgUnitId,
			@ApiParam(value = "Organizational Unit object.", required = true) @RequestBody final B2BUnitWsDTO orgUnit)
	{
		// active status changes are handled later because b2bUnitFacade.updateOrCreateBusinessUnit overwrites it to true
		final Boolean isActive = orgUnit.getActive();
		orgUnit.setActive(null);

		final B2BUnitData unitData = getUnitForUid(orgUnitId);
		getDataMapper().map(orgUnit, unitData, false);

		final B2BUnitWsDTO unitToBeValidated = getDataMapper().map(unitData, B2BUnitWsDTO.class);
		validate(unitToBeValidated, OBJECT_NAME_ORG_UNIT, b2BUnitWsDTOValidator);

		b2bUnitFacade.updateOrCreateBusinessUnit(orgUnitId, unitData);

		if (isActive != null)
		{
			if (isActive)
			{
				b2bUnitFacade.enableUnit(unitData.getUid());
			}
			else
			{
				b2bUnitFacade.disableUnit(unitData.getUid());
			}
		}
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnits/{orgUnitId}/addresses", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ApiOperation(nickname = "createOrgUnitAddress", value = "Create a new organizational unit address", notes = "Creates a new organizational unit address", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public AddressWsDTO createOrgUnitAddress(
			@ApiParam(value = "Address object.", required = true) @RequestBody final AddressWsDTO address,
			@ApiParam(value = "Organizational unit id.", required = true) @PathVariable final String orgUnitId,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		validate(address, OBJECT_NAME_ORG_UNIT, addressDTOValidator);

		final AddressData addressData = getDataMapper().map(address, AddressData.class);
		addressData.setBillingAddress(false);
		addressData.setShippingAddress(true);

		b2bUnitFacade.addAddressToUnit(addressData, orgUnitId);
		return getDataMapper().map(addressData, AddressWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnits/{orgUnitId}/addresses", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ApiOperation(nickname = "getOrgUnitAddresses", value = "Get organizational unit addresses", notes = "Retrieves organizational unit addresses", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public AddressListWsDTO getOrgUnitAddresses(
			@ApiParam(value = "Organizational unit id.", required = true) @PathVariable final String orgUnitId,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final B2BUnitData unitData = getUnitForUid(orgUnitId);
		final B2BUnitWsDTO unitDataWsDTO = getDataMapper().map(unitData, B2BUnitWsDTO.class, fields);
		final List<AddressWsDTO> addressList = unitDataWsDTO.getAddresses();
		final AddressListWsDTO addressDataList = new AddressListWsDTO();
		addressDataList.setAddresses(addressList);
		return getDataMapper().map(addressDataList, AddressListWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnits/{orgUnitId}/addresses/{addressId}", method = RequestMethod.PATCH)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(nickname = "updateOrgUnitAddress", value = "Update the organizational unit address.", notes = "Updates the organizational unit address. Only attributes provided in the request body will be changed.", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public void updateOrgUnitAddress(@ApiParam(value = "Address object.", required = true) @RequestBody final AddressWsDTO address,
			@ApiParam(value = "Organizational unit id.", required = true) @PathVariable final String orgUnitId,
			@ApiParam(value = "Address id.", required = true) @PathVariable final String addressId)
	{
		final AddressData addressData = getAddressData(addressId, orgUnitId);

		getDataMapper().map(address, addressData, false);
		addressData.setId(addressId);
		addressData.setBillingAddress(false);
		addressData.setShippingAddress(true);

		final AddressWsDTO addressToBeValidated = getDataMapper().map(addressData, AddressWsDTO.class);
		validate(addressToBeValidated, OBJECT_NAME_ORG_UNIT, addressDTOValidator);

		b2bUnitFacade.editAddressOfUnit(addressData, orgUnitId);
	}


	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "removeOrgUnitAddress", value = "Remove the organizational unit address.", notes = "Removes the organizational unit address.", produces = MediaType.APPLICATION_JSON)
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/orgUnits/{orgUnitId}/addresses/{addressId}", method = RequestMethod.DELETE)
	@ApiBaseSiteIdAndUserIdParam
	public void removeOrgUnitAddress(
			@ApiParam(value = "Organizational unit id.", required = true) @PathVariable final String orgUnitId,
			@ApiParam(value = "Address id.", required = true) @PathVariable final String addressId)
	{
		b2bUnitFacade.removeAddressFromUnit(orgUnitId, addressId);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnits/{orgUnitId}/availableUsers/{roleId}", method = RequestMethod.GET)
	@ApiOperation(nickname = "getOrgUnitUsers", value = "Get users who belongs to the organization unit.", notes = "Returns list of users which belongs to the organizational unit and can be assigned to a specific role. Users who are already assigned to the role are flagged by 'selected' attribute. ", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public OrgUnitUserListWsDTO getOrgUnitUsers(
			@ApiParam(value = "Organizational unit id.", required = true) @PathVariable final String orgUnitId,
			@ApiParam(value = "Filtering parameter which is used to return a specific role. Example roles are: b2bapprovergroup, b2badmingroup, b2bmanagergroup, b2bcustomergroup", required = true) @PathVariable final String roleId,
			@ApiParam(value = "The current result page requested.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@ApiParam(value = "The number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@ApiParam(value = "Sorting method applied to the display search results.") @RequestParam(required = false) final String sort,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{

		final OrgUnitUsersDisplayStrategy orgUnitUsersDisplayStrategy = getOrgUnitUsersDisplayStrategy(roleId);

		return orgUnitsHelper.convertPagedUsersForUnit(
				orgUnitUsersDisplayStrategy.getPagedUsersForUnit(currentPage, pageSize, sort, orgUnitId), fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgCustomers/{orgCustomerId}/roles", method = RequestMethod.POST)
	@ApiOperation(nickname = "doAddRoleToOrgCustomer", value = "Add a role to a specific organizational customer", notes = "Adds a role to a specific organizational customer", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	public B2BSelectionDataWsDTO addRoleToOrgCustomer(
			@ApiParam(value = "Identifier of the organizational customer which the role will be added.", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "The role which is added to the organizational customer. Example roles are: b2badmingroup, "
					+ "b2bmanagergroup, b2bcustomergroup", required = true) @RequestParam final String roleId,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final UserRoleManagementStrategy userRoleManagementStrategy = getUserRoleManagementStrategy(roleId);
		final B2BSelectionData selectionData = userRoleManagementStrategy.addRoleToUser(wsUserFacade.getUserUID(orgCustomerId));
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgCustomers/{orgCustomerId}/roles/{roleId}", method = RequestMethod.DELETE)
	@ApiOperation(nickname = "removeRoleFromOrgCustomer", value = "Remove a role from a specific organizational customer", notes = "Removes a role from a specific organizational customer", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseBody
	public B2BSelectionDataWsDTO removeRoleFromOrgCustomer(
			@ApiParam(value = "Identifier of the organizational customer which the role will be removed.", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "The role which is removed from the user. Example roles are: b2badmingroup, b2bmanagergroup, "
					+ "b2bcustomergroup", required = true) @PathVariable final String roleId,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final UserRoleManagementStrategy userRoleManagementStrategy = getUserRoleManagementStrategy(roleId);
		final B2BSelectionData selectionData = userRoleManagementStrategy
				.removeRoleFromUser(wsUserFacade.getUserUID(orgCustomerId));
		return getDataMapper().map(selectionData, B2BSelectionDataWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnits/{orgUnitId}/orgCustomers/{orgCustomerId}/roles", method = RequestMethod.POST)
	@ApiOperation(nickname = "doAddOrgUnitRoleToOrgCustomer", value = "Add an organizational unit dependent role to a specific organizational customer", notes = "Adds an organizational unit dependent role to a specific organizational customer", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	@ResponseStatus(value = HttpStatus.CREATED)
	public void addOrgUnitRoleToOrgCustomer(
			@ApiParam(value = "Organizational unit id.", required = true) @PathVariable final String orgUnitId,
			@ApiParam(value = "Identifier of the organizational customer which the role will be added.", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "The role which is added to the user. Example roles are: b2bapprovergroup", required = true) @RequestParam final String roleId)
	{
		final OrgUnitUserRoleManagementStrategy orgUnitUserRoleManagementStrategy = getOrgUnitUserRoleManagementStrategy(roleId);
		orgUnitUserRoleManagementStrategy.addRoleToUser(orgUnitId, wsUserFacade.getUserUID(orgCustomerId));
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/orgUnits/{orgUnitId}/orgCustomers/{orgCustomerId}/roles/{roleId}", method = RequestMethod.DELETE)
	@ApiOperation(nickname = "removeOrgUnitRoleFromOrgCustomer", value = "Remove an organizational unit dependent role from a specific organizational customer.", notes = "Removes an organizational unit dependent role from a specific organizational customer.", produces = MediaType.APPLICATION_JSON)
	@ApiBaseSiteIdAndUserIdParam
	public void removeOrgUnitRoleFromOrgCustomer(
			@ApiParam(value = "Organizational unit id.", required = true) @PathVariable final String orgUnitId,
			@ApiParam(value = "Identifier of the organizational customer which the role will be removed.", required = true) @PathVariable final String orgCustomerId,
			@ApiParam(value = "The role which is removed from the user. Example roles are: b2bapprovergroup, b2badmingroup, b2bmanagergroup, b2bcustomergroup", required = true) @PathVariable final String roleId)
	{
		final OrgUnitUserRoleManagementStrategy orgUnitUserRoleManagementStrategy = getOrgUnitUserRoleManagementStrategy(roleId);
		orgUnitUserRoleManagementStrategy.removeRoleFromUser(orgUnitId, wsUserFacade.getUserUID(orgCustomerId));
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getOrgUnitsAvailableApprovalProcesses", value = "Get available approval business processes.", notes = "Returns list of available approval business processes.", produces = MediaType.APPLICATION_JSON)
	@RequestMapping(value = "/orgUnitsAvailableApprovalProcesses", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public B2BApprovalProcessListWsDTO getOrgUnitsAvailableApprovalProcesses(
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return orgUnitsHelper.getApprovalProcesses();
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getOrgUnitsRootNodeTree", value = "Get the root organizational unit node.", notes = "Returns the root organizational unit node. The response contains detailed organizational unit node information and the child nodes associated to it.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = "/orgUnitsRootNodeTree", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public B2BUnitNodeWsDTO getOrgUnitsRootNodeTree(
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		final B2BUnitNodeData unitNodeData = b2bUnitFacade.getParentUnitNode();
		return getDataMapper().map(unitNodeData, B2BUnitNodeWsDTO.class, fields);
	}

	@Secured({ SecuredAccessConstants.ROLE_B2BADMINGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@ApiOperation(nickname = "getAvailableOrgUnitNodes", value = "Get available organizational unit nodes.", notes = "Returns list of available organizational unit nodes.", produces = MediaType.APPLICATION_JSON)
	@ResponseBody
	@RequestMapping(value = "/availableOrgUnitNodes", method = RequestMethod.GET)
	@ApiBaseSiteIdAndUserIdParam
	public B2BUnitNodeListWsDTO getBranchNodes(
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields)
	{
		return orgUnitsHelper.getAvailableOrgUnitNodes(fields);
	}

	protected UserRoleManagementStrategy getUserRoleManagementStrategy(final String roleId)
	{
		final UserRoleManagementStrategy userRoleManagementStrategy = userRoleManagementStrategyMap.get(roleId);
		if (userRoleManagementStrategy == null)
		{
			throw new RequestParameterException(String.format(ROLE_NOT_FOUND_MESSAGE, sanitize(roleId)));
		}

		return userRoleManagementStrategy;
	}

	protected OrgUnitUserRoleManagementStrategy getOrgUnitUserRoleManagementStrategy(final String roleId)
	{
		final OrgUnitUserRoleManagementStrategy orgUnitUserRoleManagementStrategy = orgUnitUserRoleManagementStrategyMap
				.get(roleId);
		if (orgUnitUserRoleManagementStrategy == null)
		{
			throw new RequestParameterException(String.format(ROLE_NOT_FOUND_MESSAGE, sanitize(roleId)));
		}

		return orgUnitUserRoleManagementStrategy;
	}

	protected OrgUnitUsersDisplayStrategy getOrgUnitUsersDisplayStrategy(final String roleId)
	{
		final OrgUnitUsersDisplayStrategy orgUnitUsersDisplayStrategy = orgUnitUsersDisplayStrategyMap.get(roleId);
		if (orgUnitUsersDisplayStrategy == null)
		{
			throw new RequestParameterException(String.format(ROLE_NOT_FOUND_MESSAGE, sanitize(roleId)));
		}

		return orgUnitUsersDisplayStrategy;
	}

	protected B2BUnitData getUnitForUid(final String orgUnitId)
	{
		final B2BUnitData unitData = b2bUnitFacade.getUnitForUid(orgUnitId);
		if (unitData == null)
		{
			throw new NotFoundException(String.format(UNIT_NOT_FOUND_MESSAGE, sanitize(orgUnitId)));
		}
		return unitData;
	}

	protected AddressData getAddressData(final String addressId, final String orgUnitId)
	{
		final B2BUnitData unit = b2bUnitFacade.getUnitForUid(orgUnitId);

		final AddressData addressData = unit.getAddresses().stream()
				.filter(address -> StringUtils.equals(address.getId(), addressId)).findFirst()
				.orElseThrow(() -> new NotFoundException(String.format(ADDRESS_NOT_FOUND_MESSAGE, addressId)));

		return addressData;
	}
}
