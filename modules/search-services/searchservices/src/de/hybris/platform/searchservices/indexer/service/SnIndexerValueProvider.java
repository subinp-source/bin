/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service;


import de.hybris.platform.searchservices.document.data.SnDocument;
import de.hybris.platform.searchservices.indexer.SnIndexerException;

import java.util.Collection;
import java.util.Set;


/**
 * Implementors for this interface should populate the document with the values to be indexed.
 */
public interface SnIndexerValueProvider<T>
{
	/**
	 * Returns the supported qualifier classes.
	 *
	 * @return the supported qualifier classes
	 */
	Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException;

	/**
	 * Populates the document with values to be indexed. The fields that use the same value resolver are grouped and this
	 * method is called once for each one of these groups.
	 *
	 * @param indexerContext
	 *           - the indexer context
	 * @param fieldWrappers
	 *           - the field wrappers
	 * @param source
	 *           - the source model
	 * @param target
	 *           - the target document
	 *
	 * @throws de.hybris.platform.searchservices.indexer.SnIndexerException
	 *            if an error occurs
	 */
	void provide(final SnIndexerContext indexerContext, final Collection<SnIndexerFieldWrapper> fieldWrappers, T source,
			final SnDocument target) throws SnIndexerException;
}
