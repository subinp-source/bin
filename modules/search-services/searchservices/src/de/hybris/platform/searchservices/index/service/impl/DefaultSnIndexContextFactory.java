/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.index.service.impl;

import de.hybris.platform.searchservices.core.service.SnContextFactory;
import de.hybris.platform.searchservices.core.service.impl.DefaultSnContextFactory;
import de.hybris.platform.searchservices.index.service.SnIndexContext;
import de.hybris.platform.searchservices.index.service.SnIndexContextFactory;

import java.util.Objects;


/**
 * Default implementation for {@link SnContextFactory}.
 */
public class DefaultSnIndexContextFactory extends DefaultSnContextFactory implements SnIndexContextFactory
{
	@Override
	public SnIndexContext createIndexContext(final String indexTypeId)
	{
		Objects.requireNonNull(indexTypeId, "indexTypeId must not be null");

		final DefaultSnIndexContext context = new DefaultSnIndexContext();
		populateContext(context, indexTypeId);
		populateIndexContext(context, indexTypeId);

		return context;
	}

	protected void populateIndexContext(final DefaultSnIndexContext context, final String indexTypeId)
	{
		context.setIndexId(indexTypeId);
	}
}
