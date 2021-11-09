/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.config;

import de.hybris.platform.integrationservices.search.ItemTypeMatch;

/**
 * Provides the configuration values for item type match
 */
public interface ItemSearchConfiguration
{
	/**
	 * Provides the ItemTypeMatch
	 *
	 * @return the ItemTypeMatch
	 */
	ItemTypeMatch getItemTypeMatch();
}
