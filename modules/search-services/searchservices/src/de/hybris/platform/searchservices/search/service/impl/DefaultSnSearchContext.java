/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service.impl;

import de.hybris.platform.searchservices.index.service.impl.DefaultSnIndexContext;
import de.hybris.platform.searchservices.search.service.SnSearchContext;
import de.hybris.platform.searchservices.search.service.SnSearchRequest;
import de.hybris.platform.searchservices.search.service.SnSearchResponse;


/**
 * Default implementation for {@link SnSearchContext}.
 */
public class DefaultSnSearchContext extends DefaultSnIndexContext implements SnSearchContext
{
	private SnSearchRequest searchRequest;
	private SnSearchResponse searchResponse;

	@Override
	public SnSearchRequest getSearchRequest()
	{
		return searchRequest;
	}

	public void setSearchRequest(final SnSearchRequest searchRequest)
	{
		this.searchRequest = searchRequest;
	}

	@Override
	public SnSearchResponse getSearchResponse()
	{
		return searchResponse;
	}

	@Override
	public void setSearchResponse(final SnSearchResponse searchResponse)
	{
		this.searchResponse = searchResponse;
	}
}
