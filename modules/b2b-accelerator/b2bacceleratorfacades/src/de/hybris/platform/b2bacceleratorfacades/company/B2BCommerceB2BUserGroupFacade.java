/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorfacades.company;

import de.hybris.platform.b2bapprovalprocessfacades.company.data.B2BPermissionData;
import de.hybris.platform.b2bcommercefacades.company.B2BUserGroupFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;


/**
 * Facade for user group management in B2B commerce.
 *
 * @deprecated Since 6.0. Use {@link B2BUserGroupFacade} instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface B2BCommerceB2BUserGroupFacade extends B2BUserGroupFacade
{
	/**
	 * Get paginated list of permissions associated to a {@link de.hybris.platform.b2b.model.B2BUserGroupModel}
	 *
	 * @param pageableData
	 *           Pagination data
	 * @param usergroupUID
	 * @return A paginated list of permissions
	 */
	SearchPageData<B2BPermissionData> getPagedPermissionsForUserGroup(PageableData pageableData, String usergroupUID);

	/**
	 * Add a permission to a {@link de.hybris.platform.b2b.model.B2BUserGroupModel}
	 *
	 * @param userGroupUid
	 *           A uid of a UserGroupModel
	 * @param permission
	 *           A permission code of B2BPermissionModel
	 * @return A data object with information about the selected permission
	 */
	B2BSelectionData addPermissionToUserGroup(String userGroupUid, String permission);

	/**
	 * Removes a permission to a {@link de.hybris.platform.b2b.model.B2BUserGroupModel}
	 *
	 * @param userGroupUid
	 *           A uid of a UserGroupModel
	 * @param permission
	 *           A permission code of B2BPermissionModel
	 * @return A data object with information about the deselected permission
	 */
	B2BSelectionData removePermissionFromUserGroup(String userGroupUid, String permission);
}
