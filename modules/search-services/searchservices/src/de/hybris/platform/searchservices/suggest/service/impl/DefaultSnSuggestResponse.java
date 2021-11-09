/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service.impl;

import de.hybris.platform.searchservices.core.service.impl.AbstractSnResponse;
import de.hybris.platform.searchservices.suggest.data.SnSuggestResult;
import de.hybris.platform.searchservices.suggest.service.SnSuggestResponse;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;


/**
 * Default implementation for {@link SnSuggestResponse}.
 */
public class DefaultSnSuggestResponse extends AbstractSnResponse implements SnSuggestResponse
{
	protected SnSuggestResult suggestResult;

	public DefaultSnSuggestResponse(final SnIndexConfiguration indexConfiguration, final SnIndexType indexType)
	{
		super(indexConfiguration, indexType);
	}

	@Override
	public SnSuggestResult getSuggestResult()
	{
		return suggestResult;
	}

	public void setSuggestResult(final SnSuggestResult suggestResult)
	{
		this.suggestResult = suggestResult;
	}
}
