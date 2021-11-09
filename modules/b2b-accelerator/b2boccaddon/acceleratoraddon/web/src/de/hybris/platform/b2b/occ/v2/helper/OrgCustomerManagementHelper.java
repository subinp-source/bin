/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2b.occ.v2.helper;

import de.hybris.platform.b2bapprovalprocessfacades.company.impl.DefaultB2BApproverFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUserGroupData;
import de.hybris.platform.b2bcommercefacades.company.impl.DefaultB2BUserFacade;
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
public class OrgCustomerManagementHelper extends AbstractHelper
{
	@Resource(name = "defaultB2BUserFacade")
	private DefaultB2BUserFacade b2BUserFacade;

	@Resource(name = "defaultB2BApproverFacade")
	private DefaultB2BApproverFacade b2bApproverFacade;

	public OrgUnitUserListWsDTO getCustomers(final int currentPage, final int pageSize, final String sort, final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final SearchPageData<CustomerData> searchPageData = b2BUserFacade.getPagedCustomers(pageableData);

		final OrgUnitUsersData usersData = createUserList(searchPageData);

		return getDataMapper().map(usersData, OrgUnitUserListWsDTO.class, fields);
	}

	public OrgUnitUserListWsDTO getCustomerApprovers(final String userId, final int currentPage, final int pageSize,
			final String sort, final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final SearchPageData<CustomerData> searchPageData = b2bApproverFacade.getPagedApproversForCustomer(pageableData, userId);

		final OrgUnitUsersData customersData = createUserList(searchPageData);

		return getDataMapper().map(customersData, OrgUnitUserListWsDTO.class, fields);
	}

	public OrgUnitUserGroupListWsDTO getOrgUnitUserGroups(final String userId, final int currentPage, final int pageSize,
			final String sort, final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);
		final SearchPageData<B2BUserGroupData> searchPageData = b2BUserFacade
				.getPagedB2BUserGroupsForCustomer(pageableData, userId);

		final OrgUnitUserGroupsData userGroupsData = createUserGroupList(searchPageData);

		return getDataMapper().map(userGroupsData, OrgUnitUserGroupListWsDTO.class, fields);
	}

	protected OrgUnitUsersData createUserList(final SearchPageData<CustomerData> result)
	{
		final OrgUnitUsersData usersData = new OrgUnitUsersData();

		usersData.setUsers(result.getResults());
		usersData.setSorts(result.getSorts());
		usersData.setPagination(result.getPagination());

		return usersData;
	}

	protected OrgUnitUserGroupsData createUserGroupList(final SearchPageData<B2BUserGroupData> result)
	{
		final OrgUnitUserGroupsData orgUnitUserGroupsData = new OrgUnitUserGroupsData();

		orgUnitUserGroupsData.setOrgUnitUserGroups(result.getResults());
		orgUnitUserGroupsData.setSorts(result.getSorts());
		orgUnitUserGroupsData.setPagination(result.getPagination());

		return orgUnitUserGroupsData;
	}
}
