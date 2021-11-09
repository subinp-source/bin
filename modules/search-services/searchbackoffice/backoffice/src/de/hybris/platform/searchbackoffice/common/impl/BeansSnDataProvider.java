/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchbackoffice.common.impl;

import de.hybris.platform.searchbackoffice.common.SnDataProvider;
import de.hybris.platform.searchservices.core.SnRuntimeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Implementation of {@link SnDataProvider} that provides beans of specific types.
 */
public class BeansSnDataProvider implements SnDataProvider<String, String>, ApplicationContextAware
{
	protected static final String TYPES_KEY = "types";

	private ApplicationContext applicationContext;

	@Override
	public List<String> getData(final Map<String, Object> parameters)
	{
		final Object typesParameter = parameters.get(TYPES_KEY);

		if (!(typesParameter instanceof String))
		{
			return Collections.emptyList();
		}

		final String[] types = ((String) typesParameter).split(",");
		final List<String> data = new ArrayList<>();

		try
		{
			for (final String type : types)
			{
				final Class<?> typeClass = Class.forName(type);

				for (final String beanName : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, typeClass))
				{
					data.add(resolveBean(beanName));
				}
			}
		}
		catch (final ClassNotFoundException e)
		{
			throw new SnRuntimeException(e);
		}

		return data;
	}

	protected String resolveBean(final String beanName)
	{
		final String[] beanAliases = applicationContext.getAliases(beanName);

		if (beanAliases.length > 0)
		{
			return beanAliases[0];
		}
		else
		{
			return beanName;
		}
	}

	@Override
	public String getValue(final String data)
	{
		return data;
	}

	@Override
	public String getLabel(final String data)
	{
		return data;
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
}
