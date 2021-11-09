/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;

import java.util.Collections;
import java.util.List;


/**
 * Implementation of {@link SnIndexerItemSource} that uses pks.
 */
public class PksSnIndexerItemSource implements SnIndexerItemSource
{
	private final List<PK> pks;

	public PksSnIndexerItemSource(final List<PK> pks)
	{
		this.pks = pks;
	}

	@Override
	public List<PK> getPks(final SnIndexerContext indexerContext) throws SnIndexerException
	{
		if (pks == null)
		{
			return Collections.emptyList();
		}

		return pks;
	}
}
