/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.decorator.impl.csrf;

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.integrationservices.cache.IntegrationCacheKey;
import de.hybris.platform.regioncache.key.AbstractCacheKey;

import com.google.common.base.Preconditions;

/**
 * A cache key for retrieving/storing {@link CsrfParameters} in {@link de.hybris.platform.integrationservices.cache.IntegrationCache}
 */
public class CsrfParametersCacheKey extends AbstractCacheKey implements IntegrationCacheKey<PK>
{
	private final ConsumedDestinationModel destination;
	private final int hashCode;

	private CsrfParametersCacheKey(final ConsumedDestinationModel d)
	{
		super(d.getItemtype(), d.getTenantId());
		destination = d;
		hashCode = destination.hashCode();
	}

	public static CsrfParametersCacheKey from(final ConsumedDestinationModel dest)
	{
		Preconditions.checkArgument(dest != null, "ConsumedDestinationModel cannot be null");
		return new CsrfParametersCacheKey(dest);
	}

	@Override
	public PK getId()
	{
		return destination.getPk();
	}

	@Override
	public boolean equals(final Object o)
	{
		return this == o || (o != null
				&& getClass() == o.getClass()
				&& destination.equals(((CsrfParametersCacheKey) o).destination));
	}

	@Override
	public int hashCode()
	{
		return hashCode;
	}

	@Override
	public String toString()
	{
		return "CsrfParametersCacheKey{"
				+ "tenant=" + getTenantId() + ','
				+ "destination=" + destination.getUrl()
				+ '}';
	}
}
