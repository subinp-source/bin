/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.core.PK;
import de.hybris.platform.searchservices.indexer.SnIndexerException;

import java.util.List;


/**
 * Implementors for this interface are responsible for collecting the items to be indexed/deleted.
 */
public interface SnIndexerItemSource
{
	/**
	 * Returns the pks of the items to be indexed/deleted.
	 *
	 * @param indexerContext
	 *           - the indexer context
	 *
	 * @return the pks of the items to be indexed/deleted.
	 */
	List<PK> getPks(SnIndexerContext indexerContext) throws SnIndexerException;
}
