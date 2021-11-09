/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service.impl;

import de.hybris.platform.searchservices.index.service.impl.DefaultSnIndexContext;
import de.hybris.platform.searchservices.suggest.service.SnSuggestContext;
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;
import de.hybris.platform.searchservices.suggest.service.SnSuggestResponse;


/**
 * Default implementation for {@link SnSuggestContext}.
 */
public class DefaultSnSuggestContext extends DefaultSnIndexContext implements SnSuggestContext
{
	private SnSuggestRequest suggestRequest;
	private SnSuggestResponse suggestResponse;

	@Override
	public SnSuggestRequest getSuggestRequest()
	{
		return suggestRequest;
	}

	public void setSuggestRequest(final SnSuggestRequest suggestRequest)
	{
		this.suggestRequest = suggestRequest;
	}

	@Override
	public SnSuggestResponse getSuggestResponse()
	{
		return suggestResponse;
	}

	@Override
	public void setSuggestResponse(final SnSuggestResponse suggestResponse)
	{
		this.suggestResponse = suggestResponse;
	}
}
