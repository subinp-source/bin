/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.indexer.service.SnIndexerBatchResponse;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;


/**
 * Default implementation for {@link SnIndexerBatchResponse}.
 */
public class DefaultSnIndexerBatchResponse extends DefaultSnIndexerResponse implements SnIndexerBatchResponse
{
	public DefaultSnIndexerBatchResponse(final SnIndexConfiguration indexConfiguration, final SnIndexType indexType)
	{
		super(indexConfiguration, indexType);
	}
}
