/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.index.service.impl.DefaultSnIndexContextFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContextFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerRequest;

import java.util.Objects;


/**
 * Default implementation for {@link SnIndexerContextFactory}.
 */
public class DefaultSnIndexerContextFactory extends DefaultSnIndexContextFactory implements SnIndexerContextFactory
{
	@Override
	public SnIndexerContext createIndexerContext(final SnIndexerRequest indexerRequest)
	{
		Objects.requireNonNull(indexerRequest, "indexerRequest must not be null");

		final DefaultSnIndexerContext context = new DefaultSnIndexerContext();
		populateContext(context, indexerRequest.getIndexTypeId());
		populateIndexContext(context, indexerRequest.getIndexTypeId());
		populateIndexerContext(context, indexerRequest);

		return context;
	}

	protected void populateIndexerContext(final DefaultSnIndexerContext context, final SnIndexerRequest indexerRequest)
	{
		context.setIndexerRequest(indexerRequest);
		context.setIndexerItemSourceOperations(indexerRequest.getIndexerItemSourceOperations());
	}
}
