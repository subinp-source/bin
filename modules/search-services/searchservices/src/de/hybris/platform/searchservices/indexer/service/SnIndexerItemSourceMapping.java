/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

/**
 * Mapping for strategies that are specific to a indexer item source type.
 */
public interface SnIndexerItemSourceMapping
{
	/**
	 * Returns the item type.
	 *
	 * @return the item type
	 */
	String getItemType();

	/**
	 * Returns the indexer item source load strategy.
	 *
	 * @return the indexer item source load strategy
	 */
	SnIndexerItemSourceLoadStrategy getLoadStrategy();
}
