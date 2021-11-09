/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.service.converters;

import de.hybris.platform.cmsfacades.data.NavigationEntryData;
import de.hybris.platform.cmsfacades.navigations.service.NavigationEntryItemModelConverter;
import de.hybris.platform.core.model.ItemModel;

import java.util.function.Function;


/**
 * Default implementation of {@link NavigationEntryItemModelConverter}
 * @deprecated since 1811 - no longer needed
 */
@Deprecated(since = "1811", forRemoval = true)
public class DefaultNavigationEntryItemModelConverter implements NavigationEntryItemModelConverter
{

	private String itemType;

	private Function<NavigationEntryData, ItemModel> converter;

	private Function<ItemModel, String> uniqueIdentifierConverter;

	@Override
	public String getItemType()
	{
		return itemType;
	}

	@Override
	public Function<NavigationEntryData, ItemModel> getConverter()
	{
		return converter;
	}

	@Override
	public Function<ItemModel, String> getUniqueIdentifierConverter()
	{
		return uniqueIdentifierConverter;
	}

	public void setConverter(final Function<NavigationEntryData, ItemModel> converter)
	{
		this.converter = converter;
	}

	public void setUniqueIdentifierConverter(final Function<ItemModel, String> uniqueIdentifierConverter)
	{
		this.uniqueIdentifierConverter = uniqueIdentifierConverter;
	}

	public void setItemType(final String itemType)
	{
		this.itemType = itemType;
	}
}
