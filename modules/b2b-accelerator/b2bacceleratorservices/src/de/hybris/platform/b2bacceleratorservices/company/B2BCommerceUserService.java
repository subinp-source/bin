/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.company;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BPermissionModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;


/**
 * A service for user management within b2b commerce
 *
 * @deprecated Since 6.0. {@link de.hybris.platform.b2b.company.B2BCommerceUserService} instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public interface B2BCommerceUserService extends de.hybris.platform.b2b.company.B2BCommerceUserService
{

	/**
	 * Remove Usergroups @link B2BCustomerModel} object for a given unique id of a customer
	 *
	 * @param user
	 *           unique identifier for {@link B2BCustomerModel} representing a customer
	 * @param usergroup
	 *           unique identifier for {@link B2BUserGroupModel} representing a user group
	 * @return updated {@link B2BUserGroupModel} object with user group removed from the given unique identifier for
	 *         {@link B2BUserGroupModel}
	 *
	 * @deprecated Since 6.0. Use deselectB2BUserGroupFromCustomer(String user, String usergroup) or
	 *             removeB2BUserGroupToCustomer(String user, String usergroup) instead.
	 */
	@Deprecated(since = "6.0", forRemoval = true)
	B2BUserGroupModel removeB2BUserGroupToCustomer(String user, String usergroup);

	/**
	 * Add permission for a given user and return the updated {@link B2BCustomerModel} object
	 *
	 * @param user
	 *           A unique identifier for {@link B2BCustomerModel} representing a user
	 * @param permission
	 *           A unique identifier for {@link B2BPermissionModel} which is added to the given user
	 * @return Updated {@link B2BPermissionModel} object with permissions added
	 */
	B2BPermissionModel addPermissionToCustomer(String user, String permission);

	/**
	 * Remove permission for a given user and return the updated {@link B2BCustomerModel} object
	 *
	 * @param user
	 *           A unique identifier for {@link B2BCustomerModel} representing a user
	 * @param permission
	 *           A unique identifier for {@link B2BPermissionModel} which is removed from the given user
	 * @return Updated {@link B2BPermissionModel} object with permissions removed
	 */
	B2BPermissionModel removePermissionFromCustomer(String user, String permission);

	/**
	 * Add an approver for a given user and return the updated {@link B2BCustomerModel} object updated with approver
	 * details
	 *
	 * @param user
	 *           A unique identifier for {@link B2BCustomerModel} representing a user
	 * @param approver
	 *           A unique identifier for {@link B2BCustomerModel} representing a approver
	 * @return Updated {@link B2BCustomerModel} object updated with approvers
	 *
	 */
	B2BCustomerModel addApproverToCustomer(String user, String approver);

	/**
	 * Remove an approver for a given user and return the updated {@link B2BCustomerModel} object updated with approver
	 * details
	 *
	 * @param user
	 *           A unique identifier for {@link B2BCustomerModel} representing a user
	 * @param approver
	 *           A unique identifier for {@link B2BCustomerModel} representing a approver
	 * @return Updated {@link B2BCustomerModel} object removed with approver
	 */
	B2BCustomerModel removeApproverFromCustomer(String user, String approver);
}
