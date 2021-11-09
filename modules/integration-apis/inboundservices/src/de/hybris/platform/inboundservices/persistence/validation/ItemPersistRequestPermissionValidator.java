/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.validation;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.security.AccessRightsService;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Nonnull;

public class ItemPersistRequestPermissionValidator implements ItemPersistRequestValidator
{
	private final AccessRightsService accessRightsService;
	private final ModelService modelService;

	public ItemPersistRequestPermissionValidator(@Nonnull final AccessRightsService accessRightsService,
	                                             @Nonnull final ModelService modelService)
	{
		this.accessRightsService = accessRightsService;
		this.modelService = modelService;
	}

	@Override
	public void validate(final PersistenceContext context, final ItemModel itemModel)
	{
		if (context != null && itemModel != null)
		{
			final String type = itemModel.getItemtype();
			if (modelService.isNew(itemModel))
			{
				accessRightsService.checkCreatePermission(type);
			}
			else if (modelService.isModified(itemModel))
			{
				accessRightsService.checkUpdatePermission(type);
			}
		}
	}
}
