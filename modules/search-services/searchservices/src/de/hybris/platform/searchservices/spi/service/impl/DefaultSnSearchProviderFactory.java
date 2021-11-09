/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.spi.service.impl;

import de.hybris.platform.searchservices.admin.SnIndexConfigurationNotFoundException;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerStrategyFactory;
import de.hybris.platform.searchservices.model.AbstractSnSearchProviderConfigurationModel;
import de.hybris.platform.searchservices.spi.SnSearchProviderConfigurationNotFoundException;
import de.hybris.platform.searchservices.spi.SnSearchProviderMappingNotFoundException;
import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;
import de.hybris.platform.searchservices.spi.service.SnSearchProvider;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderFactory;
import de.hybris.platform.searchservices.spi.service.SnSearchProviderMapping;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation for {@link SnIndexerStrategyFactory}.
 */
public class DefaultSnSearchProviderFactory implements SnSearchProviderFactory, ApplicationContextAware, InitializingBean
{
	private ApplicationContext applicationContext;
	private Map<String, SnSearchProviderMapping> itemTypeMapping;
	private Map<String, SnSearchProviderMapping> typeMapping;

	public ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet()
	{
		initializeMappings();
	}

	protected void initializeMappings()
	{
		final Map<String, SnSearchProviderMapping> beans = applicationContext.getBeansOfType(SnSearchProviderMapping.class);

		itemTypeMapping = beans.values().stream()
				.collect(Collectors.toMap(SnSearchProviderMapping::getItemType, mapping -> mapping));

		typeMapping = beans.values().stream().collect(Collectors.toMap(SnSearchProviderMapping::getType, mapping -> mapping));
	}

	@Override
	public SnSearchProvider getSearchProviderForContext(final SnContext context)
	{
		Objects.requireNonNull(context, "context must not be null");

		final SnIndexConfiguration indexConfiguration = context.getIndexConfiguration();

		if (indexConfiguration == null)
		{
			throw new SnIndexConfigurationNotFoundException("Index configuration not found for context");
		}

		if (indexConfiguration.getSearchProviderConfiguration() == null)
		{
			throw new SnSearchProviderConfigurationNotFoundException(MessageFormat
					.format("Search provider configuration not found for index configuration ''{0}''", indexConfiguration.getId()));
		}

		return getSearchProviderMappingForConfiguration(indexConfiguration.getSearchProviderConfiguration()).getSearchProvider();
	}

	@Override
	public SnSearchProviderMapping getSearchProviderMappingForConfigurationModel(
			final AbstractSnSearchProviderConfigurationModel configurationModel)
	{
		Objects.requireNonNull(configurationModel, "configurationModel must not be null");

		final SnSearchProviderMapping mapping = itemTypeMapping.get(configurationModel.getClass().getName());

		if (mapping == null)
		{
			throw new SnSearchProviderMappingNotFoundException();
		}

		return mapping;
	}

	@Override
	public SnSearchProviderMapping getSearchProviderMappingForConfiguration(
			final AbstractSnSearchProviderConfiguration configuration)
	{
		Objects.requireNonNull(configuration, "configuration must not be null");

		final SnSearchProviderMapping mapping = typeMapping.get(configuration.getClass().getName());

		if (mapping == null)
		{
			throw new SnSearchProviderMappingNotFoundException();
		}

		return mapping;
	}
}
