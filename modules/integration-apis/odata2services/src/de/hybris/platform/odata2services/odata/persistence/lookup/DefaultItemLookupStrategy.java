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

package de.hybris.platform.odata2services.odata.persistence.lookup;

import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isKeyProperty;
import static de.hybris.platform.odata2services.odata.EdmAnnotationUtils.isNullable;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.search.FlexibleSearchQueryBuilder;
import de.hybris.platform.integrationservices.search.ItemNotFoundForKeyReferencedItemPropertyException;
import de.hybris.platform.integrationservices.search.NonUniqueItemFoundException;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequestFactory;
import de.hybris.platform.odata2services.odata.persistence.exception.MissingKeyNavigationPropertyException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.core.ep.entry.ODataEntryImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.collect.Maps;

public class DefaultItemLookupStrategy implements ItemLookupStrategy
{
	private static final Logger LOG = Log.getLogger(DefaultItemLookupStrategy.class);

	private FlexibleSearchService flexibleSearchService;
	private IntegrationObjectService integrationObjectService;
	private ItemLookupRequestFactory itemLookupRequestFactory;

	@Override
	public ItemModel lookup(final ItemLookupRequest lookupRequest) throws EdmException
	{
		try
		{
			return lookupInternal(lookupRequest);
		}
		catch (final ItemNotFoundForKeyReferencedItemPropertyException e)
		{
			LOG.trace("Cannot Find Item due to ItemNotFoundForKeyNavigationPropertyException", e);
			return null;
		}
	}

	@Override
	public int count(final ItemLookupRequest lookupRequest) throws EdmException
	{
		final FlexibleSearchQueryBuilder builder = queryBuilder(lookupRequest)
				.withTotalCount();
		return search(builder).getTotalCount();
	}

	@Override
	public ItemLookupResult<ItemModel> lookupItems(final ItemLookupRequest lookupRequest) throws EdmException
	{
		final FlexibleSearchQueryBuilder builder = queryBuilder(lookupRequest)
				.withStart(lookupRequest.getSkip())
				.withCount(lookupRequest.getTop())
				.withTotalCount()
				.orderedByPK();
		final SearchResult<ItemModel> searchResult = search(builder);
		return ItemLookupResult.createFrom(searchResult);
	}

	protected ItemModel lookupInternal(final ItemLookupRequest lookupRequest) throws EdmException
	{
		final FlexibleSearchQueryBuilder builder = queryBuilder(lookupRequest);
		final FlexibleSearchQuery searchQuery = buildQuery(builder);
		if (searchQuery.getQueryParameters().isEmpty())
		{
			throw new NonUniqueItemFoundException(String.format("No key properties defined for type [%s]",
					lookupRequest.getEntityType().getName()));
		}

		final SearchResult<ItemModel> result = search(searchQuery);
		if (result.getCount() == 0)
		{
			return null;
		}
		else if (result.getCount() == 1)
		{
			return result.getResult().get(0);
		}
		throw new NonUniqueItemFoundException(String.format("No unique item found for %s: %s",
				lookupRequest.getEntityType().getName(), searchQuery.getQueryParameters()));
	}

	protected SearchResult<ItemModel> search(final FlexibleSearchQueryBuilder builder)
	{
		return search(buildQuery(builder));
	}

	protected SearchResult<ItemModel> search(final FlexibleSearchQuery query)
	{
		LOG.debug("Search: {}", query);
		return flexibleSearchService.search(query);
	}

	protected FlexibleSearchQuery buildQuery(final FlexibleSearchQueryBuilder builder)
	{
		return builder.build();
	}

	protected FlexibleSearchQueryBuilder queryBuilder(final ItemLookupRequest lookupRequest) throws EdmException
	{
		final FlexibleSearchQueryBuilder queryBuilder = new FlexibleSearchQueryBuilder(getIntegrationObjectService())
				.withIntegrationObjectItem(lookupRequest.getIntegrationObjectCode(), lookupRequest.getEntityType().getName())
				.withKeyConditionFor(toMap(lookupRequest.getODataEntry()))
				.withFilter(lookupRequest.getFilter())
				.withOrderBy(lookupRequest.getOrderBy());

		if (isCollectionNavigationPropertyQuery(lookupRequest))
		{
			buildWhereForKeyNavProperties(queryBuilder, lookupRequest);
		}
		if (lookupRequest.getAttribute() != null)
		{
			final Pair<String, String> attribute = lookupRequest.getAttribute();
			queryBuilder.withParameter(attribute.getKey(), deriveAttributeValue(attribute));
		}
		return queryBuilder;
	}

	private String deriveAttributeValue(final Pair<String, String> attribute)
	{
		return attribute.getValue();
	}

	protected boolean isCollectionNavigationPropertyQuery(final ItemLookupRequest lookupRequest)
	{
		return lookupRequest.getODataEntry() != null;
	}

	protected Map<String, Object> toMap(final ODataEntry oDataEntry)
	{
		if (oDataEntry == null)
		{
			return null;
		}

		final Map<String, Object> map = new HashMap<>(oDataEntry.getProperties());
		oDataEntry.getProperties().entrySet().stream()
				.filter(e -> e.getValue() instanceof ODataEntry)
				.forEach(e -> map.put(e.getKey(), toMap((ODataEntry) e.getValue())));
		return map;
	}

	/**
	 * @deprecated use {@link #buildWhereForKeyNavProperties(ItemLookupRequest)} instead. The parameters {@code Map} returned by this
	 * method can be applied to the query builder by calling {@code queryBuilder.withParameters(buildWhereForKeyNavProperties(lookupRequest))}}.
	 * This is what current implementation of this method does.
	 */
	@Deprecated(since = "1905.2002-CEP", forRemoval = true)
	protected FlexibleSearchQueryBuilder buildWhereForKeyNavProperties(final FlexibleSearchQueryBuilder queryBuilder, final ItemLookupRequest lookupRequest) throws EdmException
	{
		return queryBuilder.withParameters(buildWhereForKeyNavProperties(lookupRequest));
	}

	protected Map<String, Object> buildWhereForKeyNavProperties(final ItemLookupRequest lookupRequest) throws EdmException
	{
		final Map<String, Object> navigationPropertyQueryParams = new HashMap<>();
		for (final String navName : lookupRequest.getEntityType().getNavigationPropertyNames())
		{
			final EdmNavigationProperty navProperty = (EdmNavigationProperty) lookupRequest.getEntityType().getProperty(navName);

			if (isKeyProperty(navProperty))
			{

				navigationPropertyQueryParams.putAll(buildWhereForKeyNavigationProperty(lookupRequest, navName, navProperty));
			}
		}

		return navigationPropertyQueryParams;
	}

	private Map<String, Object> buildWhereForKeyNavigationProperty(final ItemLookupRequest lookupRequest, final String navName, final EdmNavigationProperty navProperty) throws EdmException
	{
		final EdmEntitySet navEntitySet = lookupRequest.getEntitySet().getRelatedEntitySet(navProperty);
		final Object navPropertyEntry = lookupRequest.getODataEntry().getProperties().get(navProperty.getName());
		if (navPropertyEntry != null)
		{
			final Map<String, Object> navProperties = ((ODataEntry) navPropertyEntry).getProperties();
			final Map<String, Object> navFlattenedProperties = Maps.newHashMap(navProperties);
			final ODataEntry navEntry = new ODataEntryImpl(navFlattenedProperties, null, null, null);

			final ItemLookupRequest navRequest = getItemLookupRequestFactory().createFrom(lookupRequest, navEntitySet, navEntry);

			final ItemModel navModel = lookupInternal(navRequest);
			if (navModel == null)
			{
				final String name = lookupRequest.getEntityType().getName();
				LOG.debug("{} was not found for {}", navName, name);
				throw new ItemNotFoundForKeyReferencedItemPropertyException(name, navName);
			}
			final String attributeName = getIntegrationObjectService()
					.findItemAttributeName(lookupRequest.getIntegrationObjectCode(), lookupRequest.getEntityType().getName(), navName);
			return Map.of(attributeName, navModel.getPk());
		}
		else
		{
			if (!isNullable(navProperty))
			{
				throw new MissingKeyNavigationPropertyException(lookupRequest.getEntityType().getName(), navProperty.getName());
			}
		}
		return Collections.emptyMap();
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	protected IntegrationObjectService getIntegrationObjectService()
	{
		return integrationObjectService;
	}

	@Required
	public void setIntegrationObjectService(final IntegrationObjectService integrationObjectService)
	{
		this.integrationObjectService = integrationObjectService;
	}

	protected ItemLookupRequestFactory getItemLookupRequestFactory()
	{
		return itemLookupRequestFactory;
	}

	@Required
	public void setItemLookupRequestFactory(final ItemLookupRequestFactory itemLookupRequestFactory)
	{
		this.itemLookupRequestFactory = itemLookupRequestFactory;
	}
}