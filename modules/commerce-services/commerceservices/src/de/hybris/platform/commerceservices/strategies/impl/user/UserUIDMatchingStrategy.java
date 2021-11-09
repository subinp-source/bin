/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.impl.user;

import de.hybris.platform.commerceservices.strategies.UserPropertyMatchingStrategy;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Matches the user by the uid
 */
public class UserUIDMatchingStrategy implements UserPropertyMatchingStrategy
{

	private static final Logger LOG = LoggerFactory.getLogger(UserUIDMatchingStrategy.class);
	private final UserService userService;

	public UserUIDMatchingStrategy(final UserService userService)
	{
		this.userService = userService;
	}

	@Override
	public <T extends UserModel> Optional<T> getUserByProperty(final String propertyValue, final Class<T> clazz)
	{
		validateParameterNotNull(propertyValue, "The property value used to identify a user must not be null");
		validateParameterNotNull(clazz, "The class of returned user model must not be null");
		try
		{
			return Optional.ofNullable(getUserService().getUserForUID(propertyValue.toLowerCase(Locale.ENGLISH), clazz));
		}
		catch (final UnknownIdentifierException ex)
		{
			LOG.debug(ex.getMessage(), ex);
			return Optional.empty();
		}
		catch (final ClassMismatchException ex)
		{
			LOG.debug(ex.getMessage(), ex);
			throw ex;
		}
	}

	protected UserService getUserService()
	{
		return userService;
	}
}
