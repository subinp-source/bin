/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.customergroups;

import de.hybris.platform.commercefacades.catalog.PageOption;
import de.hybris.platform.commercefacades.user.UserGroupOption;
import de.hybris.platform.commercefacades.user.data.UserGroupData;
import de.hybris.platform.commercefacades.user.data.UserGroupDataList;

import java.util.List;
import java.util.Set;


/**
 * Facade for management of customer groups - that is user groups which are sub group of user group with id defined via
 * {@link #setBaseCustomerGroupId(String)}. Typically customer group id = 'customergroup'
 */
public interface CustomerGroupFacade
{
	/**
	 * Create customer group (direct sub group of 'customergroup') with given uid and localized name in current locale
	 *
	 * @param uid
	 * 		the customer group uid
	 * @param localizedName
	 * 		the customer group localized name
	 */
	void createCustomerGroup(String uid, String localizedName);

	/**
	 * Assign user to customer group
	 *
	 * @param customerGroupid
	 * 		customer group uid
	 * @param userId
	 * 		user uid
	 */
	void addUserToCustomerGroup(String customerGroupid, String userId);

	/**
	 * Remove user from customer group
	 *
	 * @param customerGroupid
	 * 		customer group uid
	 * @param userId
	 * 		user uid
	 */
	void removeUserFromCustomerGroup(String customerGroupid, String userId);

	/**
	 * Returns all customers groups for the current user.
	 *
	 * @return all customer groups of a current customer
	 */
	List<UserGroupData> getCustomerGroupsForCurrentUser();

	/**
	 * Gets a user's customer groups
	 * The given <code>userId</code> is one of the unique identifiers that is used to recognize the user in matching strategies.
	 *
	 * @param userId
	 * 		the user's id used to identify the user.
	 * @return all customer groups of a given customer
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 * 		if user doesn't exist
	 * @see de.hybris.platform.commerceservices.user.impl.DefaultUserMatchingService
	 */
	List<UserGroupData> getCustomerGroupsForUser(String userId);

	/**
	 * Returns user group with uid 'customergroup' and all it's direct subgroups
	 *
	 * @param pageOption
	 * 		- result paging option.
	 * @return All customer groups as {@link UserGroupDataList}.
	 */
	UserGroupDataList getAllCustomerGroups(PageOption pageOption);

	/**
	 * Returns customer group (a sub-group of 'cutomergroup') by uid.
	 *
	 * @param uid
	 * 		the customer group uid
	 * @param options
	 * 		a {@link Set} of required {@link UserGroupOption}s
	 * @return the customer group
	 */
	UserGroupData getCustomerGroup(String uid, Set<UserGroupOption> options);
}
