/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bpunchoutaddon.security;

import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.Collection;


/**
 * Defines the strategy to select user groups.
 */
public interface PunchOutUserGroupSelectionStrategy {

	/**
	 * Selects the user groups.
	 * 
	 * @param userId
	 * @return A collection of user groups related to this {@code userId}.
	 */
	Collection<UserGroupModel> select(String userId);
}
