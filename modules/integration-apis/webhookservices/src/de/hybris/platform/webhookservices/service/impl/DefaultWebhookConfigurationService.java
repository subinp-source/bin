/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webhookservices.service.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.integrationservices.model.DescriptorFactory;
import de.hybris.platform.integrationservices.model.IntegrationObjectItemModel;
import de.hybris.platform.integrationservices.model.IntegrationObjectModel;
import de.hybris.platform.integrationservices.util.Log;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel;
import de.hybris.platform.webhookservices.service.WebhookConfigurationService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import com.google.common.base.Preconditions;

/**
 * Default implementation of {@link WebhookConfigurationService}
 */
public class DefaultWebhookConfigurationService implements WebhookConfigurationService
{
	private static final Logger LOG = Log.getLogger(DefaultWebhookConfigurationService.class);
	private static final String FIND_BY_EVENT_QUERY = " SELECT DISTINCT {" + ItemModel.PK + "}" +
			" FROM {" + WebhookConfigurationModel._TYPECODE + "}" +
			" WHERE {" + WebhookConfigurationModel.EVENTTYPE + "}=?eventClass";

	private final FlexibleSearchService flexibleSearchService;
	private final DescriptorFactory descriptorFactory;

	/**
	 * Instantiates the {@link DefaultWebhookConfigurationService}
	 *
	 * @param flexibleSearchService Service for searching {@link ItemModel}s
	 * @param descriptorFactory     DescriptorFactory to create descriptors
	 */
	public DefaultWebhookConfigurationService(@NotNull final FlexibleSearchService flexibleSearchService,
	                                          @NotNull final DescriptorFactory descriptorFactory)
	{
		Preconditions.checkArgument(flexibleSearchService != null, "FlexibleSearchService cannot be null");
		Preconditions.checkArgument(descriptorFactory != null, "DescriptorFactory cannot be null");

		this.flexibleSearchService = flexibleSearchService;
		this.descriptorFactory = descriptorFactory;
	}

	@Override
	public Collection<WebhookConfigurationModel> getWebhookConfigurationsByEventAndItemModel(
			final AbstractEvent event, final ItemModel item)
	{
		return event == null || item == null ?
				Collections.emptyList() :
				findByEventAndItemModel(event, item);
	}

	private Collection<WebhookConfigurationModel> findByEventAndItemModel(final AbstractEvent event, final ItemModel item)
	{
		final var configs = findWebhookConfigurations(
				Map.of("eventClass", event.getClass().getCanonicalName()));
		return filterWebhookConfigurationsByItemModel(configs, item);
	}

	private Collection<WebhookConfigurationModel> findWebhookConfigurations(final Map<String, Object> params)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_BY_EVENT_QUERY);
		query.addQueryParameters(params);
		final SearchResult<WebhookConfigurationModel> result = flexibleSearchService.search(query);
		final List<WebhookConfigurationModel> list = result.getResult();
		LOG.debug("Found {} WebhookConfigurations", list.size());
		return list;
	}

	private Collection<WebhookConfigurationModel> filterWebhookConfigurationsByItemModel(
			final Collection<WebhookConfigurationModel> webhookConfigurations, final ItemModel item)
	{
		return webhookConfigurations.stream()
		                            .filter(webhookConfiguration ->
				                            isMatchIntegrationObjectRootItemType(webhookConfiguration.getIntegrationObject(),
						                            item))
		                            .collect(Collectors.toList());
	}

	private boolean isMatchIntegrationObjectRootItemType(final IntegrationObjectModel integrationObject, final ItemModel item)
	{
		final IntegrationObjectItemModel rootItem = integrationObject.getRootItem();
		if (rootItem != null)
		{
			final var typeDescriptor = descriptorFactory.createItemTypeDescriptor(rootItem);
			return typeDescriptor.isInstance(item);
		}
		return false;
	}
}
