/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.event;

import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.PublishEventContext;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class InvalidateCharonCacheEvent extends AbstractEvent implements ClusterAwareEvent
{
	private String cacheKey;

	public InvalidateCharonCacheEvent(final String cacheKey)
	{
		this.cacheKey = cacheKey;
	}

	@Override
	public boolean canPublish(final PublishEventContext publishEventContext)
	{
		return true;
	}

	public String getCacheKey()
	{
		return cacheKey;
	}

	protected void setCacheKey(final String cacheKey)
	{
		this.cacheKey = cacheKey;
	}
}
