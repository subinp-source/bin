/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.integrationservices.populator;

import static de.hybris.platform.integrationservices.constants.IntegrationservicesConstants.INTEGRATION_KEY_PROPERTY_NAME;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator;
import de.hybris.platform.integrationservices.model.TypeDescriptor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

/**
 * An attribute value populator, which injects integration key value into a {@code Map} representing a data item, based on the
 * values of key attributes in that map.
 */
public class DefaultIntegrationKeyPopulator implements Populator<ItemToMapConversionContext, Map<String, Object>>
{
	private IntegrationKeyValueGenerator<TypeDescriptor, Map<String, Object>> integrationKeyValueGenerator;

	@Override
	public void populate(final ItemToMapConversionContext context, final Map<String, Object> entry)
	{
		final String integrationKey = getIntegrationKeyValueGenerator().generate(context.getTypeDescriptor(), entry);
		entry.put(INTEGRATION_KEY_PROPERTY_NAME, integrationKey);
	}

	protected IntegrationKeyValueGenerator<TypeDescriptor, Map<String, Object>> getIntegrationKeyValueGenerator()
	{
		return integrationKeyValueGenerator;
	}

	@Required
	public void setIntegrationKeyValueGenerator(final IntegrationKeyValueGenerator<TypeDescriptor, Map<String, Object>> generator)
	{
		integrationKeyValueGenerator = generator;
	}
}

