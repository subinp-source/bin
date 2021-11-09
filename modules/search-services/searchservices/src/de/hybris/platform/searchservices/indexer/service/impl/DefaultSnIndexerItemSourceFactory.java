/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.indexer.service.impl;

import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.SnIndexerItemSourceMappingNotFoundException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSource;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceFactory;
import de.hybris.platform.searchservices.indexer.service.SnIndexerItemSourceMapping;
import de.hybris.platform.searchservices.model.AbstractSnIndexerItemSourceModel;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation for {@link SnIndexerItemSourceFactory}.
 */
public class DefaultSnIndexerItemSourceFactory implements SnIndexerItemSourceFactory, ApplicationContextAware, InitializingBean
{
	private ApplicationContext applicationContext;
	private Map<String, SnIndexerItemSourceMapping> itemTypeMapping;

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
		final Map<String, SnIndexerItemSourceMapping> beans = applicationContext.getBeansOfType(SnIndexerItemSourceMapping.class);

		itemTypeMapping = beans.values().stream()
				.collect(Collectors.toMap(SnIndexerItemSourceMapping::getItemType, mapping -> mapping));
	}

	@Override
	public <T extends AbstractSnIndexerItemSourceModel, R extends SnIndexerItemSource> R createItemSource(final T itemSourceModel,
			final Map<String, Object> parameters) throws SnIndexerException
	{
		Objects.requireNonNull(itemSourceModel, "configurationModel must not be null");

		final SnIndexerItemSourceMapping mapping = itemTypeMapping.get(itemSourceModel.getClass().getName());

		if (mapping == null)
		{
			throw new SnIndexerItemSourceMappingNotFoundException();
		}

		return (R) mapping.getLoadStrategy().load(itemSourceModel, parameters);
	}
}
