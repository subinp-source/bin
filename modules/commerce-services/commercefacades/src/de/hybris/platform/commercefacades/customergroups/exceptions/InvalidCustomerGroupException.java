/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.customergroups.exceptions;

import de.hybris.platform.core.model.user.UserGroupModel;


/**
 *
 * 
 */
public class InvalidCustomerGroupException extends RuntimeException
{

	private final UserGroupModel group;

	public InvalidCustomerGroupException(final UserGroupModel group)
	{
		super("UserGroup [" + group.getUid() + "] is not member of customergroup");
		this.group = group;
	}

	/**
	 * @return the group
	 */
	public UserGroupModel getGroup()
	{
		return group;
	}
}
