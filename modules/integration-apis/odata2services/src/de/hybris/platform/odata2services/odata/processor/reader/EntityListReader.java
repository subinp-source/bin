/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.reader;

import static de.hybris.platform.odata2services.odata.persistence.ConversionOptions.conversionOptionsBuilder;

import de.hybris.platform.odata2services.odata.persistence.ConversionOptions;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest;
import de.hybris.platform.odata2services.odata.persistence.lookup.ItemLookupResult;

import java.util.Collections;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.UriInfo;

import com.google.common.base.Preconditions;

/**
 * The EntityListReader reads from the commerce suite all the entities
 * that belong to an Integration Object
 */
public class EntityListReader extends AbstractEntityReader
{
	@Override
	public boolean isApplicable(final UriInfo uriInfo)
	{
		Preconditions.checkArgument(uriInfo.getKeyPredicates() != null);

		return uriInfo.getKeyPredicates().isEmpty();
	}

	@Override
	public ODataResponse read(final ItemLookupRequest itemLookupRequest) throws ODataException
	{
		final ItemLookupResult<ODataEntry> result = hasNoFilterResult(itemLookupRequest) ?
				ItemLookupResult.createFrom(Collections.emptyList(), 0) :
				getPersistenceService().getEntities(itemLookupRequest, buildConversionOptions(itemLookupRequest));
		return getResponseWriter().write(itemLookupRequest, itemLookupRequest.getEntitySet(), result);
	}

	private ConversionOptions buildConversionOptions(final ItemLookupRequest itemLookupRequest)
	{
		return conversionOptionsBuilder()
				.withNavigationSegments(itemLookupRequest.getNavigationSegments())
				.withExpand(itemLookupRequest.getExpand())
				.build();
	}

	private boolean hasNoFilterResult(final ItemLookupRequest itemLookupRequest)
	{
		return itemLookupRequest.isNoFilterResult();
	}
}
