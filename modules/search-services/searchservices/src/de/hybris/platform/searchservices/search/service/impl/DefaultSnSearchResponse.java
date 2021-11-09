/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.search.service.impl;

import de.hybris.platform.searchservices.core.service.impl.AbstractSnResponse;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.searchservices.search.service.SnSearchResponse;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;


/**
 * Default implementation for {@link SnSearchResponse}.
 */
public class DefaultSnSearchResponse extends AbstractSnResponse implements SnSearchResponse
{
	protected SnSearchResult searchResult;

	public DefaultSnSearchResponse(final SnIndexConfiguration indexConfiguration, final SnIndexType indexType)
	{
		super(indexConfiguration, indexType);
	}

	@Override
	public SnSearchResult getSearchResult()
	{
		return searchResult;
	}

	public void setSearchResult(final SnSearchResult searchResult)
	{
		this.searchResult = searchResult;
	}
}
