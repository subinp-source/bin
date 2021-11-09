/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.user.impl;

import de.hybris.platform.commerceservices.strategies.UserPropertyMatchingStrategy;
import de.hybris.platform.commerceservices.user.UserMatchingService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.List;
import java.util.Optional;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Provides methods for retrieving the user or customer and verifying its existence.
 * The user is searched by matching one of his unique identifiers. This is accomplished using the list of matching strategies.
 * By default, the user is searched by matching the UID.
 */
public class DefaultUserMatchingService implements UserMatchingService
{

	private List<UserPropertyMatchingStrategy> matchingStrategies;

	@Override
	public <T extends UserModel> T getUserByProperty(final String propertyValue, final Class<T> clazz)
	{
		final T user = getUserOptionalByProperty(propertyValue, clazz)
				.orElseThrow(() -> new UnknownIdentifierException("Cannot find user with propertyValue '" + propertyValue + "'"));

		return validateType(user, clazz);
	}

	@Override
	public boolean isUserExisting(final String propertyValue)
	{
		return getUserOptionalByProperty(propertyValue, UserModel.class).isPresent();
	}

	protected <T extends UserModel> Optional<T> getUserOptionalByProperty(final String propertyValue, final Class<T> clazz)
	{
		validateParameterNotNullStandardMessage("propertyValue", propertyValue);
		validateParameterNotNullStandardMessage("clazz", clazz);

		return matchingStrategies.stream().flatMap(s -> s.getUserByProperty(propertyValue, clazz).stream()).findFirst();
	}

	protected <T> T validateType(final T value, final Class type)
	{
		if (!type.isInstance(value))
		{
			throw new ClassMismatchException(type, value.getClass());
		}
		else
		{
			return value;
		}
	}

	protected List<UserPropertyMatchingStrategy> getMatchingStrategies()
	{
		return matchingStrategies;
	}

	public void setMatchingStrategies(final List<UserPropertyMatchingStrategy> matchingStrategies)
	{
		this.matchingStrategies = matchingStrategies;
	}
}
