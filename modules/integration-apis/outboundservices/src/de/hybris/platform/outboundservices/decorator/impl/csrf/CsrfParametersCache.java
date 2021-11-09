/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.outboundservices.decorator.impl.csrf;

import de.hybris.platform.integrationservices.cache.impl.BaseIntegrationCache;
import de.hybris.platform.regioncache.key.CacheKey;

/**
 * A cache for {@link CsrfParameters}.
 */
public class CsrfParametersCache extends BaseIntegrationCache<CsrfParametersCacheKey, CsrfParameters>
{
	@Override
	protected CacheKey toCacheKey(final CsrfParametersCacheKey key)
	{
		return key;
	}

	@Override
	protected Class<CsrfParameters> getValueType()
	{
		return CsrfParameters.class;
	}
}
