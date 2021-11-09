/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;

import de.hybris.platform.searchservices.indexer.SnIndexerException;


/**
 * Implementors for this interface process the values to be indexed.
 */
public interface SnIndexerValueProcessor
{
	/**
	 * @param indexerContext
	 *           - the indexer context
	 * @param fieldWrapper
	 *           - the field wrapper
	 * @param value
	 *           - the value
	 *
	 * @return the processed value
	 */
	Object process(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, Object source) throws SnIndexerException;
}
