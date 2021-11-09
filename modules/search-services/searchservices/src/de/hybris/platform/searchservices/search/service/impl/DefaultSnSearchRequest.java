/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service.impl;

import de.hybris.platform.searchservices.core.service.impl.AbstractSnRequest;
import de.hybris.platform.searchservices.search.data.SnSearchQuery;
import de.hybris.platform.searchservices.search.service.SnSearchRequest;


/**
 * Default implementation for {@link SnSearchRequest}.
 */
public class DefaultSnSearchRequest extends AbstractSnRequest implements SnSearchRequest
{
	protected SnSearchQuery searchQuery;

	public DefaultSnSearchRequest(final String indexTypeId, final SnSearchQuery searchQuery)
	{
		super(indexTypeId);
		this.searchQuery = searchQuery;
	}

	@Override
	public SnSearchQuery getSearchQuery()
	{
		return searchQuery;
	}
}
