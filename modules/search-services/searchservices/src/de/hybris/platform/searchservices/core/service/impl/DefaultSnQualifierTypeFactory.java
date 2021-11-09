/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.core.service.impl;

import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.constants.SearchservicesConstants;
import de.hybris.platform.searchservices.core.service.SnContext;
import de.hybris.platform.searchservices.core.service.SnQualifierType;
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation of {@link SnQualifierTypeFactory}.
 */
public class DefaultSnQualifierTypeFactory implements SnQualifierTypeFactory, ApplicationContextAware, InitializingBean
{
	private ApplicationContext applicationContext;
	private Map<String, SnQualifierType> qualifierTypes;

	@Override
	public void afterPropertiesSet()
	{
		loadQualifierProviders();
	}

	protected void loadQualifierProviders()
	{
		final Map<String, SnQualifierType> qualifierTypesMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
				SnQualifierType.class);

		qualifierTypes = CollectionUtils.emptyIfNull(qualifierTypesMap.values()).stream()
				.collect(Collectors.toMap(SnQualifierType::getId, Function.identity()));
	}

	@Override
	public List<String> getAllQualifierTypeIds()
	{
		return List.copyOf(qualifierTypes.keySet());
	}

	@Override
	public List<SnQualifierType> getAllQualifierTypes()
	{
		return List.copyOf(qualifierTypes.values());
	}

	@Override
	public Optional<SnQualifierType> getQualifierType(final SnContext context, final SnField field)
	{
		if (BooleanUtils.isTrue(field.getLocalized()))
		{
			return Optional.of(qualifierTypes.get(SearchservicesConstants.LANGUAGE_QUALIFIER_TYPE));
		}
		else if (StringUtils.isNotBlank(field.getQualifierTypeId()))
		{
			return Optional.ofNullable(qualifierTypes.get(field.getQualifierTypeId()));
		}

		return Optional.empty();
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
