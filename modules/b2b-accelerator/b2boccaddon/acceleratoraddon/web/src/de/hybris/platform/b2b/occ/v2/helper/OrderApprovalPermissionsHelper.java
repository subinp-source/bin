/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.v2.helper;

import de.hybris.platform.b2bapprovalprocessfacades.company.B2BPermissionFacade;
import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionListWsDTO;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BPermissionsData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;


@Component
public class OrderApprovalPermissionsHelper extends AbstractHelper
{
	@Resource(name = "b2bPermissionFacade")
	protected B2BPermissionFacade b2bPermissionFacade;

	public B2BPermissionListWsDTO getPermissions(final int currentPage, final int pageSize, final String sort,
			final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);

		final B2BPermissionsData permissionsData = createPermissionList(b2bPermissionFacade.getPagedPermissions(pageableData));
		return getDataMapper().map(permissionsData, B2BPermissionListWsDTO.class, fields);
	}

	public B2BPermissionListWsDTO getPermissionsForCustomer(final String userId, final int currentPage, final int pageSize, final String sort,
			final String fields)
	{
		final PageableData pageableData = createPageableData(currentPage, pageSize, sort);

		final B2BPermissionsData permissionsData = createPermissionList(b2bPermissionFacade.getPagedPermissionsForCustomer(pageableData, userId));
		return getDataMapper().map(permissionsData, B2BPermissionListWsDTO.class, fields);
	}

	protected B2BPermissionsData createPermissionList(final SearchPageData<B2BPermissionData> result)
	{
		final B2BPermissionsData permissionsData = new B2BPermissionsData();

		permissionsData.setOrderApprovalPermissions(result.getResults());
		permissionsData.setSorts(result.getSorts());
		permissionsData.setPagination(result.getPagination());

		return permissionsData;
	}
}
