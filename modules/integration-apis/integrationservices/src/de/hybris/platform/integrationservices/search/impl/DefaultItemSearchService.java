/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integrationservices.search.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.integrationservices.search.FlexSearchQueryGenerator;
import de.hybris.platform.integrationservices.search.ItemNotFoundForKeyReferencedItemPropertyException;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;
import de.hybris.platform.integrationservices.search.ItemSearchResult;
import de.hybris.platform.integrationservices.search.ItemSearchService;
import de.hybris.platform.integrationservices.search.NonUniqueItemFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@code ItemSearchService}
 */
public class DefaultItemSearchService implements ItemSearchService
{
	private static final Logger LOG = Log.getLogger(DefaultItemSearchService.class);

	private FlexSearchQueryGenerator flexSearchQueryGenerator;
	private FlexibleSearchService flexibleSearchService;

	@Override
	public Optional<ItemModel> findUniqueItem(final ItemSearchRequest request)
	{
		try
		{
			final FlexibleSearchQuery searchQuery = getFlexSearchQueryGenerator().generate(request);
			final SearchResult<ItemModel> result = search(searchQuery);
			switch (result.getCount())
			{
				case 0:
					return Optional.empty();
				case 1:
					return Optional.of(result.getResult().get(0));
				default:
					throw new NonUniqueItemFoundException(request);
			}
		}
		catch (final ItemNotFoundForKeyReferencedItemPropertyException e)
		{
			LOG.debug("Nested key item not resolved", e);
			return Optional.empty();
		}
	}

	@Override
	public @NotNull ItemSearchResult<ItemModel> findItems(final ItemSearchRequest request)
	{
		final SearchResult<ItemModel> result = searchWithoutKey(request);
		return ItemSearchResult.createFrom(result);
	}

	@Override
	public int countItems(final ItemSearchRequest request)
	{
		return searchWithoutKey(request).getTotalCount();
	}

	private SearchResult<ItemModel> searchWithoutKey(final ItemSearchRequest request)
	{
		final FlexibleSearchQuery flexibleSearchQuery = getFlexSearchQueryGenerator().generate(request);
		return search(flexibleSearchQuery);
	}

	private SearchResult<ItemModel> search(final FlexibleSearchQuery query)
	{
		LOG.debug("Search: {}", query);
		return getFlexibleSearchService().search(query);
	}

	private FlexSearchQueryGenerator getFlexSearchQueryGenerator()
	{
		return flexSearchQueryGenerator;
	}

	private FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexSearchQueryGenerator(final FlexSearchQueryGenerator flexSearchQueryGenerator)
	{
		this.flexSearchQueryGenerator = flexSearchQueryGenerator;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
