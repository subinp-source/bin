/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.integrationkey.impl;

import de.hybris.platform.integrationservices.integrationkey.IntegrationKeyValueGenerator;
import de.hybris.platform.integrationservices.model.TypeDescriptor;
import de.hybris.platform.odata2services.odata.integrationkey.ODataEntryToMapConverter;

import java.util.Map;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.springframework.beans.factory.annotation.Required;

/**
 * Generates integration key value for a given {@link TypeDescriptor} from the attribute values represented by a {@code ODataEntry}
 */
public final class DelegatingODataEntryToIntegrationKeyValueGenerator
		implements IntegrationKeyValueGenerator<TypeDescriptor, ODataEntry>
{
	private IntegrationKeyValueGenerator<TypeDescriptor, Map<String, Object>> mapToIntegrationKeyGenerator;
	private ODataEntryToMapConverter oDataEntryToMapConverter;

	@Override
	public String generate(final TypeDescriptor typeDescriptor, final ODataEntry entry)
	{
		final Map<String, Object> entryAsMap = getoDataEntryToMapConverter().convert(typeDescriptor, entry);
		return getMapToIntegrationKeyGenerator().generate(typeDescriptor, entryAsMap);
	}

	protected ODataEntryToMapConverter getoDataEntryToMapConverter()
	{
		return oDataEntryToMapConverter;
	}

	@Required
	public void setoDataEntryToMapConverter(final ODataEntryToMapConverter oDataEntryToMapConverter)
	{
		this.oDataEntryToMapConverter = oDataEntryToMapConverter;
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
}
