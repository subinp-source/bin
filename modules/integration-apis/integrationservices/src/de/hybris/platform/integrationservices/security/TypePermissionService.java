/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.security;


import de.hybris.platform.integrationservices.model.TypeDescriptor;

/**
 * This Service is a facade for {@link AccessRightsService}. Allows for permission checks for {@link TypeDescriptor} types.
 */
public interface TypePermissionService
{
	/**
	 * Checks if the current user can read the type within the {@link TypeDescriptor}. The implementation may throw a
	 * {@link RuntimeException} if the user does not have the read permission for the type within the TypeDescriptor.
	 *
	 * @param typeDescriptor to check permissions for model within it
	 */
	void checkReadPermission(TypeDescriptor typeDescriptor);
}
