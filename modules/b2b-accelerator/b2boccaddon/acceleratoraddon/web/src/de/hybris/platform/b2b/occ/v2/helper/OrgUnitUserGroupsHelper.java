/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.helper;

import de.hybris.platform.b2bapprovalprocessfacades.company.B2BPermissionFacade;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bcommercefacades.company.B2BUserGroupFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionsData;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserGroupListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserGroupsData;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUserListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.OrgUnitUsersData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


@Component
public class OrgUnitUserGroupsHelper extends AbstractHelper
{
	@Resource(name = "b2bUserGroupFacade")
	protected B2BUserGroupFacade b2bUserGroupFacade;

	@Resource(name = "b2bPermissionFacade")
	protected B2BPermissionFacade b2bPermissionFacade;

	public OrgUnitUserGroupListWsDTO searchUserGroups(final int currentPage, final int pageSize, final String sort,
			final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final OrgUnitUserGroupsData orgUnitUserGroupsData = createUserGroupList(
				b2bUserGroupFacade.getPagedB2BUserGroups(pageableData));

		return getDataMapper().map(orgUnitUserGroupsData, OrgUnitUserGroupListWsDTO.class, fields);
	}

	public B2BPermissionListWsDTO searchPermissionsForOrgUnitUserGroup(final String orgUnitUserGroupId, final int currentPage,
			final int pageSize, final String sort, final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final B2BPermissionsData permissionsData = createPermissionList(
				b2bPermissionFacade.getPagedPermissionsForUserGroup(pageableData, orgUnitUserGroupId));

		return getDataMapper().map(permissionsData, B2BPermissionListWsDTO.class, fields);
	}

	public OrgUnitUserListWsDTO searchOrgCustomersForUserGroup(final String orgUnitUserGroupId, final int currentPage,
			final int pageSize, final String sort, final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final OrgUnitUsersData permissionsData = createUserList(
				b2bUserGroupFacade.getPagedCustomersForUserGroup(pageableData, orgUnitUserGroupId));

		return getDataMapper().map(permissionsData, OrgUnitUserListWsDTO.class, fields);
	}

	protected B2BPermissionsData createPermissionList(final SearchPageData<B2BPermissionData> result)
	{
		final B2BPermissionsData permissionsData = new B2BPermissionsData();

		permissionsData.setOrderApprovalPermissions(result.getResults());
		permissionsData.setSorts(result.getSorts());
		permissionsData.setPagination(result.getPagination());

		return permissionsData;
	}

	protected OrgUnitUserGroupsData createUserGroupList(final SearchPageData<B2BUserGroupData> result)
	{
		final OrgUnitUserGroupsData orgUnitUserGroupsData = new OrgUnitUserGroupsData();

		orgUnitUserGroupsData.setOrgUnitUserGroups(result.getResults());
		orgUnitUserGroupsData.setSorts(result.getSorts());
		orgUnitUserGroupsData.setPagination(result.getPagination());

		return orgUnitUserGroupsData;
	}

	protected OrgUnitUsersData createUserList(final SearchPageData<CustomerData> result)
	{
		final OrgUnitUsersData usersData = new OrgUnitUsersData();

		usersData.setUsers(result.getResults());
		usersData.setSorts(result.getSorts());
		usersData.setPagination(result.getPagination());

		return usersData;
	}
}
