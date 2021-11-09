/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Optional;


/**
 * Provides methods for retrieving users by specified property
 */
public interface UserPropertyMatchingStrategy
{
	/**
	 * Gets the user by the unique property and return as a specified class model
	 *
	 * @param property
	 * 		a unique property value for identify a user
	 * @param clazz
	 * 		class of returned user model
	 * @param <U>
	 * 		type of returned user model
	 * @return optional with the found user or empty if not found or not supported class model
	 */
	<U extends UserModel> Optional<U> getUserByProperty(String property, Class<U> clazz);
}
