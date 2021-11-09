/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.enums.SnDocumentOperationType;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceOperation;


/**
 * Default implementation for {@link SnIndexerItemSourceOperation}.
 */
public class DefaultSnIndexerItemSourceOperation implements SnIndexerItemSourceOperation
{
	private final SnDocumentOperationType documentOperationType;
	private final SnIndexerItemSource indexerItemSource;

	public DefaultSnIndexerItemSourceOperation(final SnDocumentOperationType documentOperationType,
			final SnIndexerItemSource indexerItemSource)
	{
		this.documentOperationType = documentOperationType;
		this.indexerItemSource = indexerItemSource;
	}

	@Override
	public SnDocumentOperationType getDocumentOperationType()
	{
		return documentOperationType;
	}

	@Override
	public SnIndexerItemSource getIndexerItemSource()
	{
		return indexerItemSource;
	}
}
