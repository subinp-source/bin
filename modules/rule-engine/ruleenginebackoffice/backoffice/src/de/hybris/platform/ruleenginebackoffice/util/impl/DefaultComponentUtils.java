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
package de.hybris.platform.ruleenginebackoffice.util.impl;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import de.hybris.platform.ruleenginebackoffice.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Default implementation of {@link ComponentUtils}
 */
public class DefaultComponentUtils implements ComponentUtils
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultComponentUtils.class);

	private static final String GLOBAL_EVENT_LISTENERS_MAP = "globalEventListenersMap";

	private CockpitEventQueue cockpitEventQueue;

	@Override
	public void setupGlobalEventListeners(final GenericForwardComposer<Component> composer, final Component component)
	{
		for (final Method method : composer.getClass().getMethods())
		{
			bindGlobalEventListenerIfPresent(method, composer, component);
		}
	}

	@Override
	public void removeGlobalEventListeners(final String widgetId)
	{
		LOG.debug("Unregister cockpit event listener for widget [{}]", widgetId);
		getCockpitEventQueue().removeListener(widgetId);
	}

	protected void bindGlobalEventListenerIfPresent(final Method method, final GenericForwardComposer<Component> composer,
			final Component component)
	{
		final GlobalCockpitEvent cockpitEventAnnotation = method.getAnnotation(GlobalCockpitEvent.class);

		if (cockpitEventAnnotation != null)
		{
			final Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes != null && parameterTypes.length == 1)
			{
				addGlobalEventListener(cockpitEventAnnotation.eventName(), component,
						event -> onEvent(method, composer, parameterTypes[0], event), cockpitEventAnnotation.scope());
			}
			else
			{
				LOG.error("Could not apply GlobalEventListener for '{}', method signature doesn't match requirements.", method);
			}
		}
	}

	protected void addGlobalEventListener(final String eventName, final Component component, final EventListener eventListener,
			final String scope)
	{
		final String uuid = component.getUuid();

		getCockpitEventQueue().registerListener(uuid, eventName, scope);

		addListenerToDesktop(component, eventName, eventListener);

		Executions.getCurrent().getDesktop()
				.addListener((DesktopCleanup) desktop -> removeGlobalEventListeners(component.getUuid()));
	}

	protected void addListenerToDesktop(final Component component, final String eventName, final EventListener eventListener)
	{
		if (eventListener != null && eventName != null)
		{
			final Map<String, Map<String, EventListener>> listeners = getListeners();

			final Map<String, EventListener> listenersForComponent = getListenersForComponent(listeners, component);

			listenersForComponent.put(eventName, eventListener);
		}
	}

	protected Map<String, Map<String, EventListener>> getListeners()
	{
		Map<String, Map<String, EventListener>> listeners = null;

		try
		{
			listeners = (Map<String, Map<String, EventListener>>) Executions.getCurrent().getDesktop()
					.getAttribute(GLOBAL_EVENT_LISTENERS_MAP);
		}
		catch (final ClassCastException exc)
		{
			LOG.error("Wrong type for attribute 'globalEventListenersMap'", exc);
		}

		if (listeners == null)
		{
			listeners = new ConcurrentHashMap<>();
			Executions.getCurrent().getDesktop().setAttribute(GLOBAL_EVENT_LISTENERS_MAP, listeners);
		}

		return listeners;
	}

	protected Map<String, EventListener> getListenersForComponent(final Map<String, Map<String, EventListener>> listeners,
			final Component component)
	{
		Map<String, EventListener> listenersForComponent = listeners.get(component.getUuid());
		if (listenersForComponent == null)
		{
			listenersForComponent = new ConcurrentHashMap();
			listeners.put(component.getUuid(), listenersForComponent);
		}

		return listenersForComponent;
	}

	protected void onEvent(final Method method, final GenericForwardComposer<Component> composer,
			final Class<?> parameterType, final Event event) throws IllegalAccessException
	{
		if (event.getData() != null && !parameterType.isAssignableFrom(event.getData().getClass()))
		{
			LOG.error("Global event listener method '{}' has wrong parameter type. Expected type assignable from '{}' but got '{}'",
					method, event.getData().getClass(), parameterType);
		}
		else
		{
			try
			{
				method.invoke(composer, new Object[]
				{ event.getData() });
			}
			catch (final InvocationTargetException exception)
			{
				LOG.error("Error when calling component callback", exception);
			}
		}
	}

	protected CockpitEventQueue getCockpitEventQueue()
	{
		return cockpitEventQueue;
	}

	@Required
	public void setCockpitEventQueue(final CockpitEventQueue cockpitEventQueue)
	{
		this.cockpitEventQueue = cockpitEventQueue;
	}
}
