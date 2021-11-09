/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.model.AbstractSnIndexerItemSourceModel;

import java.util.Map;


/**
 * Implementations of this interface are responsible for creating instances of {@link SnIndexerItemSource}.
 */
public interface SnIndexerItemSourceFactory
{
	/**
	 * Creates a new instance of {@link SnIndexerItemSource}.
	 *
	 * @param <T>
	 *           - the type of indexer item source model
	 * @param <R>
	 *           - the type of indexer item source data
	 * @param itemSourceModel
	 *           - the item source model
	 * @param parameters
	 *           - the parameters
	 *
	 * @return the new instance of {@link SnIndexerItemSource}
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	<T extends AbstractSnIndexerItemSourceModel, R extends SnIndexerItemSource> R createItemSource(T itemSourceModel,
			Map<String, Object> parameters) throws SnIndexerException;
}
