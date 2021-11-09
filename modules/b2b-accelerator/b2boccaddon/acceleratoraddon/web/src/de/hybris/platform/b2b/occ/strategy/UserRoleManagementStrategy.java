/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.strategy;

import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;

public interface UserRoleManagementStrategy
{
	/**
	 * Adds a role to the user
	 *
	 * @param userId is the identifier of the user
	 * @return the resulting {@link B2BSelectionData}
	 */
	B2BSelectionData addRoleToUser(String userId);

	/**
	 * Removes a role from the user
	 *
	 * @param userId is the identifier of the user
	 * @return the resulting {@link B2BSelectionData}
	 */
	B2BSelectionData removeRoleFromUser(String userId);
}
