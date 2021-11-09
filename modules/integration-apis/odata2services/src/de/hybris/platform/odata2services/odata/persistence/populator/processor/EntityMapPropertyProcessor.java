/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.persistence.populator.processor;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;
import de.hybris.platform.odata2services.odata.persistence.StorageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.core.ep.entry.EntryMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.MediaMetadataImpl;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.apache.olingo.odata2.core.uri.ExpandSelectTreeNodeImpl;

import com.google.common.collect.Maps;

public class EntityMapPropertyProcessor extends AbstractCollectionPropertyProcessor
{
	private static final String KEY_PROPERTY = "key";
	private static final String VALUE_PROPERTY = "value";

	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attribute)
	{
		return attribute.isMap() && !attribute.isLocalized();
	}

	@Override
	protected void processItemInternal(final ItemModel item, final String entryPropertyName, final Object value,
	                                   final StorageRequest request)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected List<ODataEntry> deriveDataFeedEntries(final ItemConversionRequest request, final String propertyName,
	                                                 final Object value)
	{
		return getListOfODataEntries((Map<?, ?>) value);
	}

	private List<ODataEntry> getListOfODataEntries(final Map<?, ?> values)
	{
		final List<ODataEntry> entries = new ArrayList<>(values.size());
		for (final Map.Entry value : values.entrySet())
		{
			final ODataEntry newEntry = new ODataEntryImpl(Maps.newHashMap(), new MediaMetadataImpl(),
					new EntryMetadataImpl(), new ExpandSelectTreeNodeImpl());
			newEntry.getProperties().put(KEY_PROPERTY, value.getKey());
			newEntry.getProperties().put(VALUE_PROPERTY, value.getValue());
			entries.add(newEntry);
		}
		return entries;
	}

	@Override
	protected boolean canHandleEntityValue(final Object value)
	{
		return value instanceof Map;
	}
}