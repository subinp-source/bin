/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.suggest.service.impl;

import de.hybris.platform.searchservices.core.service.impl.AbstractSnRequest;
import de.hybris.platform.searchservices.suggest.data.SnSuggestQuery;
import de.hybris.platform.searchservices.suggest.service.SnSuggestRequest;


/**
 * Default implementation for {@link SnSuggestRequest}.
 */
public class DefaultSnSuggestRequest extends AbstractSnRequest implements SnSuggestRequest
{
	protected SnSuggestQuery suggestQuery;

	public DefaultSnSuggestRequest(final String indexTypeId, final SnSuggestQuery suggestQuery)
	{
		super(indexTypeId);
		this.suggestQuery = suggestQuery;
	}

	@Override
	public SnSuggestQuery getSuggestQuery()
	{
		return suggestQuery;
	}
}
