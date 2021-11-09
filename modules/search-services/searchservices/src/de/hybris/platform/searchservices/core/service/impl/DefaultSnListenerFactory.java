/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;

import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnListenerFactory;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation of {@link SnListenerFactory}.
 */
public class DefaultSnListenerFactory implements SnListenerFactory, ApplicationContextAware, InitializingBean
{
	private Collection<Class<?>> supportedTypes;

	private ApplicationContext applicationContext;
	private Map<Class<?>, List<Object>> globalListeners;

	@Override
	public void afterPropertiesSet()
	{
		loadGlobalListeners();
	}

	@Override
	public <T> List<T> getListeners(final SnContext context, final Class<T> listenerType)
	{
		final List<T> listeners = new ArrayList<>();

		final List<Object> globalListenersForType = getGlobalListeners().get(listenerType);
		if (globalListenersForType != null)
		{
			listeners.addAll((List<T>) globalListenersForType);
		}

		final SnIndexConfiguration indexConfiguration = context.getIndexConfiguration();
		if (indexConfiguration != null)
		{
			if (indexConfiguration.getSearchProviderConfiguration() != null)
			{
				loadListeners(listeners, indexConfiguration.getSearchProviderConfiguration().getListeners(), listenerType);
			}

			loadListeners(listeners, context.getIndexConfiguration().getListeners(), listenerType);
		}

		final SnIndexType indexType = context.getIndexType();
		if (indexType != null)
		{
			loadListeners(listeners, indexType.getListeners(), listenerType);
		}

		return Collections.unmodifiableList(listeners);
	}

	protected void loadGlobalListeners()
	{
		globalListeners = new HashMap<>();

		final Map<String, DefaultSnListenerDefinition> listenerDefinitionsMap = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(applicationContext, DefaultSnListenerDefinition.class);
		final List<DefaultSnListenerDefinition> listenerDefinitions = new ArrayList<>(listenerDefinitionsMap.values());

		Collections.sort(listenerDefinitions);

		for (final Class<?> type : supportedTypes)
		{
			final List<Object> listeners = new ArrayList<>();

			for (final DefaultSnListenerDefinition listenerDefinition : listenerDefinitions)
			{
				final Object listener = listenerDefinition.getListener();
				if (listener != null && type.isAssignableFrom(listener.getClass()))
				{
					listeners.add(listener);
				}
			}

			globalListeners.put(type, listeners);
		}
	}

	protected <T> void loadListeners(final List<T> listeners, final List<String> beanNames, final Class<T> listenerType)
	{
		if ((beanNames != null) && !beanNames.isEmpty())
		{
			for (final String beanName : beanNames)
			{
				if (applicationContext.isTypeMatch(beanName, listenerType))
				{
					final T listener = applicationContext.getBean(beanName, listenerType);
					listeners.add(listener);
				}
			}
		}
	}

	public Collection<Class<?>> getSupportedTypes()
	{
		return supportedTypes;
	}

	@Required
	public void setSupportedTypes(final Collection<Class<?>> supportedTypes)
	{
		this.supportedTypes = supportedTypes;
	}

	protected ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	protected Map<Class<?>, List<Object>> getGlobalListeners()
	{
		return globalListeners;
	}
}
