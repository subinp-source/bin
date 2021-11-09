/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.suggest.service.SnSuggestContext;


/**
 * Implementations of this interface are responsible for creating instances of {@link SnSuggestContext}.
 */
public interface SnIndexerContextFactory
{
	/**
	 * Creates a new instance of {@link SnSuggestContext}.
	 *
	 * @param indexerRequest
	 *           - the indexer request
	 *
	 * @return the new instance of {@link SnSuggestContext}
	 *
	 * @throws SnIndexerException
	 *            if an error occurs
	 */
	SnIndexerContext createIndexerContext(SnIndexerRequest indexerRequest) throws SnIndexerException;
}
