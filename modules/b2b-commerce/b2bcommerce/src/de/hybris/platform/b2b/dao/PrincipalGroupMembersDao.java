/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.dao;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;

import java.util.List;
import java.util.Set;



public interface PrincipalGroupMembersDao
{
	/**
	 * Finds all members of a Principal Group of a given type. FlexibleSearch filters non specified type members so as
	 * not to have to iterate and instantiate entire collection to filter undesired types
	 */
	<T extends PrincipalModel> List<T> findHierarchyMembersByType(final Set<UserGroupModel> parents, final Class<T> memberType);

	/**
	 * Finds all members of a Principal Group of a given type. FlexibleSearch filters non specified type members so as
	 * not to have to iterate and instantiate entire collection to filter undesired types
	 */
	<T extends PrincipalModel> List<T> findAllMembersByType(final UserGroupModel parent, final Class<T> memberType);

	/**
	 * Finds members of a Principal Group of a given type. FlexibleSearch filters non specified type members so as not to
	 * have to iterate and instantiate entire collection to filter undesired types *
	 */
	<T extends PrincipalModel> List<T> findMembersByType(final UserGroupModel parent, final Class<T> memberType, final int count,
			final int start);

}
