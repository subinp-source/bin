/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;
import de.hybris.platform.integrationservices.search.ItemSearchResult;
import de.hybris.platform.integrationservices.search.ItemSearchService;
import de.hybris.platform.integrationservices.search.enricher.ItemSearchRequestEnricher;
import de.hybris.platform.integrationservices.search.validation.ItemSearchRequestValidator;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

/**
 * This implementation validates {@link ItemSearchRequest} before performing a search. If request is valid, then
 * processing is delegated to {@link DefaultItemSearchService}, otherwise the search is vetoed by throwing an exception.
 */
public class ValidatingItemSearchService implements ItemSearchService
{
	private final ItemSearchService itemSearchService;
	private List<ItemSearchRequestValidator> uniqueItemSearchValidators = Collections.emptyList();
	private List<ItemSearchRequestValidator> itemsSearchValidators = Collections.emptyList();
	private List<ItemSearchRequestValidator> countSearchValidators = Collections.emptyList();
	private List<ItemSearchRequestEnricher> uniqueItemSearchEnrichers = Collections.emptyList();
	private List<ItemSearchRequestEnricher> itemsSearchEnrichers = Collections.emptyList();
	private List<ItemSearchRequestEnricher> countSearchEnrichers = Collections.emptyList();
	
	/**
	 * Instantiates this service.
	 * @param service a service to delegate search to in case when no validation problems found.
	 */
	public ValidatingItemSearchService(@NotNull final ItemSearchService service)
	{
		Preconditions.checkArgument(service != null, "ItemSearchService to delegate search to is required");
		itemSearchService = service;
	}

	@Override
	public Optional<ItemModel> findUniqueItem(final ItemSearchRequest request)
	{
		final var enrichedRequest = runEnrichers(request, uniqueItemSearchEnrichers);
		runValidators(enrichedRequest, uniqueItemSearchValidators);
		return itemSearchService.findUniqueItem(enrichedRequest);
	}

	@Override
	public ItemSearchResult<ItemModel> findItems(final ItemSearchRequest request)
	{
		final var enrichedRequest = runEnrichers(request, itemsSearchEnrichers);
		runValidators(enrichedRequest, itemsSearchValidators);
		return itemSearchService.findItems(enrichedRequest);
	}

	@Override
	public int countItems(final ItemSearchRequest request)
	{
		final var enrichedRequest = runEnrichers(request, countSearchEnrichers);
		runValidators(enrichedRequest, countSearchValidators);
		return itemSearchService.countItems(enrichedRequest);
	}

	private ItemSearchRequest runValidators(final ItemSearchRequest request, final List<ItemSearchRequestValidator> validators)
	{
		for (final ItemSearchRequestValidator v : validators)
		{
			v.validate(request);
		}
		return request;
	}

	private ItemSearchRequest runEnrichers(final ItemSearchRequest request, final List<ItemSearchRequestEnricher> enrichers)
	{
		var updatedRequest = request;
		for(var enricher : enrichers)
		{
			updatedRequest = enricher.enrich(updatedRequest);
		}
		return updatedRequest;
	}

	/**
	 * Provides validators to be used for validating item search requests when {@link #findUniqueItem(ItemSearchRequest)} is called.
	 * The validators can be configured programmatically or by overriding {@code integrationServicesUniqueItemSearchValidators}
	 * list definition in the Spring configuration.
	 * @param validators validators to use. If empty or this method is not called, then {@link #findUniqueItem(ItemSearchRequest)}
	 *                   will be called unconditionally.
	 */
	public void setUniqueItemSearchValidators(@NotNull final List<ItemSearchRequestValidator> validators)
	{
		uniqueItemSearchValidators = validators;
	}

	/**
	 * Provides validators to be used for validating item search requests when {@link #findItems(ItemSearchRequest)} is called.
	 * The validators can be configured programmatically or by overriding {@code integrationServicesItemsSearchValidators}
	 * list definition in the Spring configuration.
	 * @param validators validators to use. If empty or this method is not called, then {@link #findItems(ItemSearchRequest)}
	 *                   will be called unconditionally.
	 */
	public void setItemsSearchValidators(@NotNull final List<ItemSearchRequestValidator> validators)
	{
		itemsSearchValidators = validators;
	}

	/**
	 * Provides validators to be used for validating item search requests when {@link #countItems(ItemSearchRequest)} is called.
	 * The validators can be configured programmatically or by overriding {@code integrationServicesCountSearchValidators}
	 * list definition in the Spring configuration.
	 * @param validators validators to use. If empty or this method is not called, then {@link #countItems(ItemSearchRequest)}
	 *                   will be called unconditionally.
	 */
	public void setCountSearchValidators(@NotNull final List<ItemSearchRequestValidator> validators)
	{
		countSearchValidators = validators;
	}

	/**
	 * Provides enrichers to be used for enriching item search requests when {@link #findUniqueItem(ItemSearchRequest)} is called.
	 * The enrichers can be configured programmatically or by overriding {@code integrationServicesUniqueItemSearchEnrichers}
	 * list definition in the Spring configuration.
	 * @param enrichers enrichers to use. If empty or this method is not called, then {@link #findUniqueItem(ItemSearchRequest)}
	 *                   will be called unconditionally.
	 */
	public void setUniqueItemSearchEnrichers(@NotNull final List<ItemSearchRequestEnricher> enrichers)
	{
		uniqueItemSearchEnrichers = enrichers;
	}

	/**
	 * Provides enrichers to be used for enriching item search requests when {@link #findItems(ItemSearchRequest)} is called.
	 * The enrichers can be configured programmatically or by overriding {@code integrationServicesItemsSearchEnrichers}
	 * list definition in the Spring configuration.
	 * @param enrichers enrichers to use. If empty or this method is not called, then {@link #findItems(ItemSearchRequest)}
	 *                   will be called unconditionally.
	 */
	public void setItemsSearchEnrichers(@NotNull final List<ItemSearchRequestEnricher> enrichers)
	{
		itemsSearchEnrichers = enrichers;
	}

	/**
	 * Provides enrichers to be used for enriching item search requests when {@link #countItems(ItemSearchRequest)} is called.
	 * The enrichers can be configured programmatically or by overriding {@code integrationServicesCountSearchEnrichers}
	 * list definition in the Spring configuration.
	 * @param enrichers enrichers to use. If empty or this method is not called, then {@link #countItems(ItemSearchRequest)}
	 *                   will be called unconditionally.
	 */
	public void setCountSearchEnrichers(@NotNull final List<ItemSearchRequestEnricher> enrichers)
	{
		countSearchEnrichers = enrichers;
	}
}
