/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service.impl;

import de.hybris.platform.searchservices.index.service.impl.DefaultSnIndexContextFactory;
import de.hybris.platform.searchservices.suggest.service.SnSuggestContext;
import de.hybris.platform.searchservices.suggest.service.SnSuggestContextFactory;
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;

import java.util.Objects;


/**
 * Default implementation for {@link SnSuggestContextFactory}.
 */
public class DefaultSnSuggestContextFactory extends DefaultSnIndexContextFactory implements SnSuggestContextFactory
{
	@Override
	public SnSuggestContext createSuggestContext(final SnSuggestRequest suggestRequest)
	{
		Objects.requireNonNull(suggestRequest, "searchRequest must not be null");

		final DefaultSnSuggestContext context = new DefaultSnSuggestContext();
		populateContext(context, suggestRequest.getIndexTypeId());
		populateIndexContext(context, suggestRequest.getIndexTypeId());
		populateSuggestContext(context, suggestRequest);

		return context;
	}

	protected void populateSuggestContext(final DefaultSnSuggestContext context, final SnSuggestRequest suggestRequest)
	{
		context.setSuggestRequest(suggestRequest);
	}
}
