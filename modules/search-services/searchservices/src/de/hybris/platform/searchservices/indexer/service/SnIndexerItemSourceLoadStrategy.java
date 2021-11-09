/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.model.AbstractSnIndexerItemSourceModel;

import java.util.Map;


/**
 * Strategy for loading indexer item sources.
 *
 * @param <T>
 *           - the type of indexer item source model
 * @param <R>
 *           - the type of indexer item source data
 */
public interface SnIndexerItemSourceLoadStrategy<T extends AbstractSnIndexerItemSourceModel, R extends SnIndexerItemSource>
{
	/**
	 * Loads the indexer item source model and converts it to some data object.
	 *
	 * @param source
	 *           - the indexer item source model
	 * @param parameters
	 *           - the parameters
	 *
	 * @return the indexer item source data
	 */
	R load(T source, Map<String, Object> parameters);
}
