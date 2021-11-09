/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl.user;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.strategies.UserPropertyMatchingStrategy;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Optional;

/**
 * External implementation strategy for {@link UserPropertyMatchingStrategy}
 * that returns empty optional when searching for a user by a given property.
 */
public class AlwaysNegativeUserPropertyMatchingStrategy implements UserPropertyMatchingStrategy
{
	@Override
	public <T extends UserModel> Optional<T> getUserByProperty(final String propertyValue, final Class<T> clazz)
	{
		validateParameterNotNull(propertyValue, "The property value used to identify a customer must not be null");
		validateParameterNotNull(clazz, "The class of returned user model must not be null");

		return Optional.empty();
	}

}
