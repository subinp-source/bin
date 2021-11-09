/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.security.impl;

import de.hybris.platform.integrationservices.config.IntegrationServicesConfiguration;
import de.hybris.platform.integrationservices.security.AccessRightsService;
import de.hybris.platform.integrationservices.security.TypeAccessPermissionException;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;

import javax.validation.constraints.NotNull;

import de.hybris.platform.servicelayer.security.permissions.PermissionsConstants;
import org.slf4j.Logger;

/**
 * Default implementation for {@link AccessRightsService}
 */
public class DefaultAccessRightsService implements AccessRightsService
{
	private static final Logger LOGGER = Log.getLogger(DefaultAccessRightsService.class);

	private final PermissionCRUDService permissionCRUDService;
	private final IntegrationServicesConfiguration integrationServicesConfiguration;

	/**
	 * DefaultIntegrationObjectPermissionService class constructor.
	 *
	 * @param permissionCRUDService            permission CRUD service see {@link PermissionCRUDService}
	 * @param integrationServicesConfiguration configuration service for reading properties {@link IntegrationServicesConfiguration }
	 */
	public DefaultAccessRightsService(
			@NotNull final PermissionCRUDService permissionCRUDService,
			@NotNull final IntegrationServicesConfiguration integrationServicesConfiguration)
	{
		this.permissionCRUDService = permissionCRUDService;
		this.integrationServicesConfiguration = integrationServicesConfiguration;
	}

	/**
	 * {@inheritDoc}
	 * {@link TypeAccessPermissionException} is thrown if the user doesn't have the permission.
	 */
	@Override
	public void checkCreatePermission(@NotNull final String type)
	{
		if (integrationServicesConfiguration.isAccessRightsEnabled() && !permissionCRUDService.canCreateTypeInstance(type))
		{
			LOGGER.debug("The user does not have the permission to create an instance of the '{}' type", type);
			throw new TypeAccessPermissionException(type, PermissionsConstants.CREATE);
		}
	}

	/**
	 * {@inheritDoc}
	 * {@link TypeAccessPermissionException} is thrown if the user doesn't have the permission.
	 */
	@Override
	public void checkReadPermission(@NotNull final String type)
	{
		if (integrationServicesConfiguration.isAccessRightsEnabled() && !permissionCRUDService.canReadType(type))
		{
			LOGGER.debug("The user does not have the permission to read the '{}' type", type);
			throw new TypeAccessPermissionException(type, PermissionsConstants.READ);
		}
	}

	/**
	 * {@inheritDoc}
	 * {@link TypeAccessPermissionException} is thrown if the user doesn't have the permission.
	 */
	@Override
	public void checkUpdatePermission(@NotNull final String type)
	{
		if (integrationServicesConfiguration.isAccessRightsEnabled() && !permissionCRUDService.canChangeType(type))
		{
			LOGGER.debug("The user does not have the permission to change the '{}' type", type);
			throw new TypeAccessPermissionException(type, PermissionsConstants.CHANGE);
		}
	}

	/**
	 * {@inheritDoc}
	 * {@link TypeAccessPermissionException} is thrown if the user doesn't have the permission.
	 */
	@Override
	public void checkDeletePermission(@NotNull final String type)
	{
		if (integrationServicesConfiguration.isAccessRightsEnabled() && !permissionCRUDService.canRemoveTypeInstance(type))
		{
			LOGGER.debug("The user does not have the permission to remove an instance of the '{}' type!", type);
			throw new TypeAccessPermissionException(type, PermissionsConstants.REMOVE);
		}
	}
}
