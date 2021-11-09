/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationbackoffice.utility;

import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.integrationservices.config.ItemSearchConfiguration;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.search.ItemTypeMatch;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * Default implementation of the {@link ItemTypeMatchSelector}
 */
public class DefaultItemTypeMatchSelector implements ItemTypeMatchSelector
{
	@Resource
	private ItemSearchConfiguration itemSearchConfiguration;

	public ItemTypeMatch getToSelectItemTypeMatch(@NotNull final IntegrationObjectItemModel integrationObjectItemModel)
	{
		if (integrationObjectItemModel.getItemTypeMatch() != null)
		{
			return ItemTypeMatch.valueOf(integrationObjectItemModel.getItemTypeMatch().getCode());
		}
		final ComposedTypeModel itemType = integrationObjectItemModel.getType();
		return itemType instanceof EnumerationMetaTypeModel || itemType.getAbstract()
				? ItemTypeMatch.ALL_SUBTYPES
				: getDefaultForNonAbstractNonEnumType();
	}

	private ItemTypeMatch getDefaultForNonAbstractNonEnumType()
	{
		return itemSearchConfiguration == null
				? ItemTypeMatch.DEFAULT
				: itemSearchConfiguration.getItemTypeMatch();
	}

	public void setItemSearchConfiguration(final ItemSearchConfiguration itemSearchConfiguration)
	{
		this.itemSearchConfiguration = itemSearchConfiguration;
	}
}
