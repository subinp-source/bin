/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.search.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.config.ItemSearchConfiguration;
import de.hybris.platform.integrationservices.item.IntegrationItem;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;
import de.hybris.platform.integrationservices.search.FlexibleSearchQueryBuilder;
import de.hybris.platform.integrationservices.search.ImmutableItemSearchRequest;
import de.hybris.platform.integrationservices.search.IntegrationItemToMapConverter;
import de.hybris.platform.integrationservices.search.ItemNotFoundForKeyReferencedItemPropertyException;
import de.hybris.platform.integrationservices.search.ItemSearchRequest;
import de.hybris.platform.integrationservices.search.ItemSearchService;
import de.hybris.platform.integrationservices.search.ItemTypeMatch;
import de.hybris.platform.integrationservices.service.IntegrationObjectService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

public class DefaultFlexSearchQueryBuilderFactory implements FlexSearchQueryBuilderFactory
{
	private IntegrationObjectService integrationObjectService;
	private IntegrationItemToMapConverter integrationItemToMapConverter;
	private ItemSearchService itemSearchService;
	private ItemSearchConfiguration itemSearchConfiguration;


	/**
	 * {@inheritDoc}
	 *
	 * @throws ItemNotFoundForKeyReferencedItemPropertyException when unique item search contains a key condition with a nested item
	 *                                                           that cannot be resolved in the persistent storage.
	 */
	@Override
	public FlexibleSearchQueryBuilder createQueryBuilder(final ItemSearchRequest searchRequest)
	{
		final String integrationObjectCode = searchRequest.getTypeDescriptor().getIntegrationObjectCode();
		final String ioItemCode = searchRequest.getTypeDescriptor().getItemCode();

		final FlexibleSearchQueryBuilder queryBuilder = new FlexibleSearchQueryBuilder(integrationObjectService)
				.withIntegrationObjectItem(integrationObjectCode, ioItemCode)
				.withFilter(searchRequest.getFilter())
				.withOrderBy(searchRequest.getOrderBy())
				.withLocale(searchRequest.getAcceptLocale())
				.withTypeHierarchyRestriction(getItemTypeMatch());

		searchRequest.getPaginationParameters().ifPresent(queryBuilder::withPaginationParameters);
		return searchRequest.getRequestedItem()
		                    .map(item -> applyUniqueItemSearchConditions(queryBuilder, item))
		                    .orElseGet(() -> queryBuilder.withTotalCount().orderedByPK());
	}

	private FlexibleSearchQueryBuilder applyUniqueItemSearchConditions(final FlexibleSearchQueryBuilder queryBuilder,
	                                                                   final IntegrationItem item)
	{
		return queryBuilder.withKeyConditionFor(integrationItemToMapConverter.convert(item))
		                   .withParameters(createParametersMapForKeyNavigationProperties(item));
	}

	private Map<String, Object> createParametersMapForKeyNavigationProperties(final IntegrationItem item)
	{
		final Map<String, Object> parameters = new HashMap<>();
		item.getItemType().getAttributes().stream()
		    .filter(d -> !d.isPrimitive())
		    .filter(TypeAttributeDescriptor::isKeyAttribute)
		    .forEach(d -> parameters.put(d.getQualifier(), resolveNestedItemPk(item, d)));
		return parameters;
	}

	private PK resolveNestedItemPk(final IntegrationItem item, final TypeAttributeDescriptor refAttribute)
	{
		final IntegrationItem nestedItem = item.getReferencedItem(refAttribute);
		return nestedItem == null ? null : getPk(item, refAttribute, nestedItem);
	}

	private PK getPk(final IntegrationItem item, final TypeAttributeDescriptor refAttribute, final IntegrationItem nestedItem)
	{
		final ImmutableItemSearchRequest nestedItemPropertySearchRequest = new ImmutableItemSearchRequest.Builder()
				.withIntegrationItem(nestedItem)
				.build();

		return itemSearchService.findUniqueItem(nestedItemPropertySearchRequest)
		                        .map(ItemModel::getPk)
		                        .orElseThrow(() -> new ItemNotFoundForKeyReferencedItemPropertyException(
				                        item.getItemType(), refAttribute.getAttributeName()));
	}

	@Required
	public void setIntegrationObjectService(final IntegrationObjectService service)
	{
		integrationObjectService = service;
	}

	@Required
	public void setIntegrationItemToMapConverter(final IntegrationItemToMapConverter converter)
	{
		integrationItemToMapConverter = converter;
	}

	@Required
	public void setItemSearchService(final ItemSearchService service)
	{
		itemSearchService = service;
	}

	public void setItemSearchConfiguration(final ItemSearchConfiguration configuration)
	{
		itemSearchConfiguration = configuration;
	}

	private ItemTypeMatch getItemTypeMatch()
	{
		return itemSearchConfiguration == null ? ItemTypeMatch.DEFAULT : itemSearchConfiguration.getItemTypeMatch();
	}
}