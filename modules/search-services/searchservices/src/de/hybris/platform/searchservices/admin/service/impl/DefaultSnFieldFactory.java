/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service.impl;

import de.hybris.platform.searchservices.admin.service.SnFieldFactory;
import de.hybris.platform.searchservices.admin.service.SnFieldProvider;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnIndexType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation of {@link SnFieldFactory}.
 */
public class DefaultSnFieldFactory implements SnFieldFactory, ApplicationContextAware, InitializingBean
{
	private ApplicationContext applicationContext;
	private List<SnFieldProvider> fieldProviders;

	@Override
	public void afterPropertiesSet()
	{
		loadFieldProviders();
	}

	protected void loadFieldProviders()
	{
		final Map<String, DefaultSnFieldProviderDefinition> providerDefinitionsMap = BeanFactoryUtils
				.beansOfTypeIncludingAncestors(applicationContext, DefaultSnFieldProviderDefinition.class);

		final List<DefaultSnFieldProviderDefinition> fieldProviderDefinitions = new ArrayList<>(providerDefinitionsMap.values());

		Collections.sort(fieldProviderDefinitions);

		fieldProviders = fieldProviderDefinitions.stream().map(DefaultSnFieldProviderDefinition::getFieldProvider)
				.collect(Collectors.toList());
	}

	@Override
	public List<SnField> getDefaultFields(final SnIndexType indexType)
	{
		final List<SnField> fields = new ArrayList<>();

		for (final SnFieldProvider provider : fieldProviders)
		{
			fields.addAll(provider.getDefaultFields(indexType));
		}

		return fields;
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
