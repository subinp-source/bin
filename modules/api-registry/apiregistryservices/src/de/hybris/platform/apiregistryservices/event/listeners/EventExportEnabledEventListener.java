/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.apiregistryservices.event.listeners;

import static de.hybris.platform.apiregistryservices.utils.EventExportUtils.EXPORTING_PROP;

import de.hybris.platform.apiregistryservices.event.EventExportEnabledEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.util.Config;

/**
 * Service-layer listener of @{@link EventExportEnabledEvent}
 * Switches event exporting parameter to true
 */
public class EventExportEnabledEventListener extends AbstractEventListener<EventExportEnabledEvent>
{
	@Override
	protected void onEvent(final EventExportEnabledEvent eventExportEvent)
	{
		Config.setParameter(EXPORTING_PROP, String.valueOf(true));
	}
}
