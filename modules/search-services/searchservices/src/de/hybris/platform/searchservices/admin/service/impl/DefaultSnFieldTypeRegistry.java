/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.searchservices.admin.service.impl;


import de.hybris.platform.searchservices.admin.service.SnFieldTypeRegistry;
import de.hybris.platform.searchservices.admin.data.SnFieldTypeInfo;
import de.hybris.platform.searchservices.enums.SnFieldType;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * Default implementation of {@link SnFieldTypeRegistry}.
 */
public class DefaultSnFieldTypeRegistry implements SnFieldTypeRegistry, ApplicationContextAware, InitializingBean
{
	private ApplicationContext applicationContext;
	private Map<SnFieldType, SnFieldTypeInfo> fieldTypeMapping;

	@Override
	public void afterPropertiesSet() throws Exception
	{
		fieldTypeMapping = loadFielTypeMapping();
	}

	@Override
	public SnFieldTypeInfo getFieldTypeInfo(final SnFieldType fieldType)
	{
		return fieldTypeMapping.get(fieldType);
	}

	protected Map<SnFieldType, SnFieldTypeInfo> loadFielTypeMapping()
	{
		final Map<SnFieldType, SnFieldTypeInfo> mapping = new EnumMap<>(SnFieldType.class);

		final Map<String, SnFieldTypeInfo> fieldTypeInfoBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
				SnFieldTypeInfo.class);

		for (final SnFieldTypeInfo fieldTypeInfo : fieldTypeInfoBeans.values())
		{
			mapping.put(fieldTypeInfo.getFieldType(), fieldTypeInfo);
		}

		return mapping;
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

	protected Map<SnFieldType, SnFieldTypeInfo> getFieldTypeMapping()
	{
		return fieldTypeMapping;
	}
}
