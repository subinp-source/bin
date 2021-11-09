/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.user;

import de.hybris.platform.core.model.user.UserModel;


/**
 * Provides methods for retrieving user or customer by specified property
 */
public interface UserMatchingService
{

	/**
	 * Gets the user by the property and cast to UserModel subtype
	 *
	 * @param propertyValue
	 * 		the unique value of property used to identify the user.
	 * @param clazz
	 * 		- the class of UserModel subtype
	 * @param <U>
	 * 		UserModel subtype
	 * @return the found user
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 * 		if the user not found,
	 * @throws de.hybris.platform.servicelayer.exceptions.ClassMismatchException
	 * 		if clazz is not supported by strategy
	 */
	<U extends UserModel> U getUserByProperty(final String propertyValue, final Class<U> clazz);

	/**
	 * Verifies whether the user exist
	 *
	 * @param propertyValue
	 * 		the property value used to identify the user
	 * @return <code>true</code> if user exists
	 */
	boolean isUserExisting(final String propertyValue);
}
