/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.security;

/**
 * This Service provides convenience methods to check for the basic CRUD permissions on item types.
 */
public interface AccessRightsService
{
	/**
	 * Checks if the current user can create an instance of the type. The implementation may throw a
	 * {@link RuntimeException} if the user does not have the create permission for the type.
	 *
	 * @param type type code to check permissions for
	 */
	void checkCreatePermission(String type);

	/**
	 * Checks if the current user can read the type. The implementation may throw a
	 * {@link RuntimeException} if the user does not have the read permission for the type.
	 *
	 * @param type type code to check permissions for
	 */
	void checkReadPermission(String type);

	/**
	 * Checks if the current user can update an instance of the type. The implementation may throw a
	 * {@link RuntimeException} if the user does not have the update permission for the type.
	 *
	 * @param type type code to check permissions for
	 */
	void checkUpdatePermission(String type);

	/**
	 * Checks if the current user can delete an instance of the type. The implementation may throw a
	 * {@link RuntimeException} if the user does not have the delete permission for the type.
	 *
	 * @param type type code to check permissions for
	 */
	void checkDeletePermission(String type);
}
