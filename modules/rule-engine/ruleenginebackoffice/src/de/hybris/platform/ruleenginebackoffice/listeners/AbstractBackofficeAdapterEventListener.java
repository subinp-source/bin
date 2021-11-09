/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.ruleenginebackoffice.listeners;

import com.hybris.backoffice.events.DefaultBackofficeEventSender;
import de.hybris.platform.servicelayer.event.EventSender;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;


/**
 * Abstract event listener that provides common functionality to send respective backoffice notification
 */
public abstract class AbstractBackofficeAdapterEventListener<T extends AbstractEvent> extends AbstractEventListener<T>
{
	private static final Logger LOG = LoggerFactory.getLogger(AbstractBackofficeAdapterEventListener.class);

	@Override
	protected void onEvent(final T event)
	{
		if (isBackofficeEventsAdapterRegistered())
		{
			LOG.debug("Received event [{}]. Dispatching it to backoffice notification framework", event);
			getBackofficeEventSender().sendEvent(event);
		}
	}

	protected boolean isBackofficeEventsAdapterRegistered()
	{
		final EventSender backofficeEventSender = getBackofficeEventSender();
		if (backofficeEventSender instanceof DefaultBackofficeEventSender)
		{
			final DefaultBackofficeEventSender defaultBackofficeEventSender = (DefaultBackofficeEventSender) backofficeEventSender;
			final Field backofficeEventsAdapterField = findField(DefaultBackofficeEventSender.class, "backofficeEventsAdapter");
			backofficeEventsAdapterField.setAccessible(true);
			return Objects.nonNull(getField(backofficeEventsAdapterField, defaultBackofficeEventSender));
		}
		return true;
	}

	protected EventSender getBackofficeEventSender()
	{
		throw new UnsupportedOperationException(
				"Please define in the spring configuration a <lookup-method> for getBackofficeEventSender().");
	}
}
