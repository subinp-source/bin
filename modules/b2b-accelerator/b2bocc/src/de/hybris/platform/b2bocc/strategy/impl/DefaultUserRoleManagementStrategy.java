/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2bocc.strategy.impl;

import de.hybris.platform.b2bocc.strategy.UserRoleManagementStrategy;
import de.hybris.platform.b2bcommercefacades.company.B2BUserFacade;
import de.hybris.platform.b2bcommercefacades.company.data.B2BSelectionData;

import org.springframework.beans.factory.annotation.Required;


public class DefaultUserRoleManagementStrategy implements UserRoleManagementStrategy
{
	protected String role;
	protected B2BUserFacade b2bUserFacade;

	@Override
	public B2BSelectionData addRoleToUser(final String userId)
	{
		return getB2bUserFacade().addUserRole(userId, getRole());
	}

	@Override
	public B2BSelectionData removeRoleFromUser(final String userId)
	{
		return getB2bUserFacade().removeUserRole(userId, getRole());
	}

	protected String getRole()
	{
		return role;
	}

	@Required
	public void setRole(String role)
	{
		this.role = role;
	}

	protected B2BUserFacade getB2bUserFacade()
	{
		return b2bUserFacade;
	}

	@Required
	public void setB2bUserFacade(B2BUserFacade b2bUserFacade)
	{
		this.b2bUserFacade = b2bUserFacade;
	}

}
