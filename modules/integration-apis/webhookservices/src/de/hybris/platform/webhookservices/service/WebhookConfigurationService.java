/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.webhookservices.service;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.webhookservices.model.WebhookConfigurationModel;

import java.util.Collection;

import javax.validation.constraints.NotNull;

/**
 * A service that provides convenience methods to interact with {@link WebhookConfigurationModel}s.
 */
public interface WebhookConfigurationService
{
	/**
	 * Find WebhookConfigurations that are associated to the given event, and have the Integration Object root item
	 * type the same as the given item type
	 *
	 * @param event Event to search for
	 * @param item Search Integration Object root items matching the item type
	 * @return A collection of WebhookConfigurationModels if found, otherwise an empty collection
	 */
	Collection<WebhookConfigurationModel> getWebhookConfigurationsByEventAndItemModel(@NotNull AbstractEvent event,
																					  @NotNull ItemModel item);
}
