/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.navigations.service;

import de.hybris.platform.core.model.ItemModel;

import java.util.List;
import java.util.Optional;


/**
 * Registry that stores a collection of {@link NavigationEntryItemModelConverter}.
 */
public interface NavigationEntryConverterRegistry
{
	/**
	 * Get a specific {@code NavigationEntryItemModelConverter} by its itemType.
	 *
	 * @param itemType
	 * 		the itemType {@link ItemModel#getItemtype()} of the element to retrieve from the registry
	 * @return an {@code Optional} element matching the itemType
	 */
	Optional<NavigationEntryItemModelConverter> getNavigationEntryItemModelConverter(String itemType);


	/**
	 * Get a list of supported navigation entry item types.
	 *
	 * @return an {@code Optional} list of item types supported by this registry.
	 * @deprecated since 1811.
	 */
	@Deprecated(since = "1811", forRemoval = true)
	Optional<List<String>> getSupportedItemTypes();
}
