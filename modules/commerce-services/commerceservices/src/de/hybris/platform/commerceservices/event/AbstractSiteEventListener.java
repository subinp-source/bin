/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.event;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

public abstract class AbstractSiteEventListener<T extends AbstractEvent> extends AbstractEventListener<T>
{
	protected abstract void onSiteEvent(final T event);

	protected abstract boolean shouldHandleEvent(final T event);

	@Override
	protected void onEvent(final T event)
	{
		if (shouldHandleEvent(event))
		{
			onSiteEvent(event);
		}
	}
}
