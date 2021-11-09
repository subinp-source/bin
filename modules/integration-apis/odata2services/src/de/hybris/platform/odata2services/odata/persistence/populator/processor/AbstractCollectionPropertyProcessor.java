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

package de.hybris.platform.odata2services.odata.persistence.populator.processor;

import static de.hybris.platform.odata2services.odata.persistence.ODataFeedBuilder.oDataFeedBuilder;

import de.hybris.platform.odata2services.odata.persistence.ItemConversionRequest;

import java.util.Collection;
import java.util.List;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;

/**
 * Base implementation for property processors dealing with properties of collection type.
 */
public abstract class AbstractCollectionPropertyProcessor extends AbstractPropertyProcessor
{
	@Override
	protected void processEntityInternal(
			final ODataEntry oDataEntry,
			final String propertyName,
			final Object value,
			final ItemConversionRequest request)
			throws EdmException
	{
		if (canHandleEntityValue(value))
		{
			final List<ODataEntry> entries = deriveDataFeedEntries(request, propertyName, value);
			final ODataFeed feed = oDataFeedBuilder().withEntries(entries).build();

			oDataEntry.getProperties().putIfAbsent(propertyName, feed);
		}
	}

	protected boolean canHandleEntityValue(final Object value)
	{
		return value instanceof Collection;
	}

	protected abstract List<ODataEntry> deriveDataFeedEntries(ItemConversionRequest request, String propertyName, Object value) throws EdmException;
}
