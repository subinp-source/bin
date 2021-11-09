/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.security.impl;

import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.security.AccessRightsService;
import de.hybris.platform.integrationservices.security.TypePermissionService;

import javax.validation.constraints.NotNull;

/**
 * Default implementation for {@link TypePermissionService}
 */
public class DefaultTypePermissionService implements TypePermissionService
{

	private final AccessRightsService accessRightsService;

	/**
	 * DefaultTypePermissionService class constructor.
	 *
	 * @param accessRightsService access rights service.
	 */
	public DefaultTypePermissionService(@NotNull final AccessRightsService accessRightsService)
	{
		this.accessRightsService = accessRightsService;
	}

	@Override
	public void checkReadPermission(final TypeDescriptor typeDescriptor)
	{
		if(isItemTypeDescriptor(typeDescriptor))
		{
			accessRightsService.checkReadPermission(typeDescriptor.getTypeCode());
		}
	}

	private boolean isItemTypeDescriptor(final TypeDescriptor typeDescriptor)
	{
		return !(typeDescriptor.isPrimitive() || typeDescriptor.isMap());
	}
}
