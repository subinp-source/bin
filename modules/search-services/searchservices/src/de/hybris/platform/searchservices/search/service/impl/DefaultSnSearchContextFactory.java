/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service.impl;

import de.hybris.platform.searchservices.index.service.SnIndexContextFactory;
import de.hybris.platform.searchservices.index.service.impl.DefaultSnIndexContextFactory;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import de.hybris.platform.searchservices.search.service.SnSearchContextFactory;
import de.hybris.platform.searchservices.search.service.SnSearchRequest;

import java.util.Objects;


/**
 * Default implementation for {@link SnIndexContextFactory}.
 */
public class DefaultSnSearchContextFactory extends DefaultSnIndexContextFactory implements SnSearchContextFactory
{
	@Override
	public SnSearchContext createSearchContext(final SnSearchRequest searchRequest)
	{
		Objects.requireNonNull(searchRequest, "searchRequest must not be null");

		final DefaultSnSearchContext context = new DefaultSnSearchContext();
		populateContext(context, searchRequest.getIndexTypeId());
		populateIndexContext(context, searchRequest.getIndexTypeId());
		populateSearchContext(context, searchRequest);

		return context;
	}

	protected void populateSearchContext(final DefaultSnSearchContext context, final SnSearchRequest searchRequest)
	{
		context.setSearchRequest(searchRequest);
	}
}
