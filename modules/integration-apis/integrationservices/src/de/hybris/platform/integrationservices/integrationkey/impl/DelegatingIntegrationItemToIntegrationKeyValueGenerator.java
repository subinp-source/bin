/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.integrationkey.impl;

import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.integrationservices.search.IntegrationItemToMapConverter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

public final class DelegatingIntegrationItemToIntegrationKeyValueGenerator
		implements IntegrationKeyValueGenerator<TypeDescriptor, IntegrationItem>
{
	private IntegrationKeyValueGenerator<TypeDescriptor, Map<String, Object>> mapToIntegrationKeyGenerator;
	private IntegrationItemToMapConverter integrationItemToMapConverter;

	@Override
	public String generate(final TypeDescriptor typeDescriptor, final IntegrationItem itemData)
	{
		final Map<String, Object> itemAsMap = getIntegrationItemToMapConverter().convert(itemData);
		return getMapToIntegrationKeyGenerator().generate(typeDescriptor, itemAsMap);
	}

	protected IntegrationKeyValueGenerator<TypeDescriptor, Map<String, Object>> getMapToIntegrationKeyGenerator()
	{
		return mapToIntegrationKeyGenerator;
	}

	@Required
	public void setMapToIntegrationKeyGenerator(
			final IntegrationKeyValueGenerator<TypeDescriptor, Map<String, Object>> mapToIntegrationKeyGenerator)
	{
		this.mapToIntegrationKeyGenerator = mapToIntegrationKeyGenerator;
	}

	protected IntegrationItemToMapConverter getIntegrationItemToMapConverter()
	{
		return integrationItemToMapConverter;
	}

	@Required
	public void setIntegrationItemToMapConverter(
			final IntegrationItemToMapConverter integrationItemToMapConverter)
	{
		this.integrationItemToMapConverter = integrationItemToMapConverter;
	}
}
