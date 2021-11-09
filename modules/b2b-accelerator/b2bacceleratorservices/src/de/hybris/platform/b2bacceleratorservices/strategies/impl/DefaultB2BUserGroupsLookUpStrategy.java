/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bacceleratorservices.strategies.impl;

import de.hybris.platform.b2bacceleratorservices.strategies.B2BUserGroupsLookUpStrategy;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 * @deprecated Since 6.0. Use {@link de.hybris.platform.b2b.strategies.impl.DefaultB2BUserGroupsLookUpStrategy} instead.
 */
@Deprecated(since = "6.0", forRemoval = true)
public class DefaultB2BUserGroupsLookUpStrategy implements B2BUserGroupsLookUpStrategy
{
	private List<String> groups;

	@Override
	public List<String> getUserGroups()
	{
		return getGroups();
	}

	protected List<String> getGroups()
	{
		return groups;
	}

	@Required
	public void setGroups(final List<String> groups)
	{
		this.groups = groups;
	}
}
