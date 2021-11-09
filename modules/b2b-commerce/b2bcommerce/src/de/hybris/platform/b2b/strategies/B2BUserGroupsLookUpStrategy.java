/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies;

import java.util.List;


/**
 * A strategy for getting available user group codes that a B2BCustomer can be assigned to.
 */
public interface B2BUserGroupsLookUpStrategy
{

	/**
	 * Gets the user groups.
	 *
	 * @return the user groups
	 */
	List<String> getUserGroups();
}
