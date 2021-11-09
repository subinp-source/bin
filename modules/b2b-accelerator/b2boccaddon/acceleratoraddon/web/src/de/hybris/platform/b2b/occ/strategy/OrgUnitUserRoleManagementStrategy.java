/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.strategy;


public interface OrgUnitUserRoleManagementStrategy
{

	/**
	 * Adds an organizational unit dependent role to the user
	 *
	 * @param userId is the identifier of the user
	 */
	void addRoleToUser(String unitId, String userId);

	/**
	 * Removes an organizational unit dependent role from the user
	 *
	 * @param userId is the identifier of the user
	 */
	void removeRoleFromUser(String unitId, String userId);
}
