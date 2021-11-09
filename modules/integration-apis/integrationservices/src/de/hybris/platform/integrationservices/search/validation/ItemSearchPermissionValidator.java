/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search.validation;

import de.hybris.platform.integrationservices.search.ItemSearchRequest;
import de.hybris.platform.integrationservices.security.AccessRightsService;

import javax.annotation.Nonnull;

/**
 * Validates the user has the permission to view the item type under concern.
 */
public class ItemSearchPermissionValidator implements ItemSearchRequestValidator
{
	private final AccessRightsService permissionService;

	public ItemSearchPermissionValidator(@Nonnull
			final AccessRightsService permissionService)
	{
		this.permissionService = permissionService;
	}

	/**
	 * Validates read is authorized for the given {@link ItemSearchRequest}.
	 *
	 * * @param request a {@link ItemSearchRequest} that the type is derived from
	 */
	@Override
	public void validate(final ItemSearchRequest request)
	{
		if (request != null)
		{
			permissionService.checkReadPermission(request.getTypeDescriptor().getTypeCode());
		}
	}
}
