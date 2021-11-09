/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.filter;

import de.hybris.platform.core.model.ItemModel;

import java.util.Optional;

/**
 * A service for filtering items being sent to a webhook. Implementations can provide logic for which items should
 * be sent to a webhook and which should be excluded from that process.
 */
public interface WebhookFilterService
{
	/**
	 * Evaluates the item and filters out, if item does not match the filtering logic criteria.
	 *
	 * @param <T>       The type of the ItemModel
	 * @param item      an item being sent to a webhook.
	 * @param scriptUri a URI for the item filter logic (script) location. See <href a="https://help.sap.com/viewer/d0224eca81e249cb821f2cdf45a82ace/2005/en-US/8bec04a386691014938a9996a977d07f.html">Scripting Engine documentation</href>
	 * @return an {@code Optional} containing the item to send to the webhook, if the item passes the filter;
	 * or an {@code Optional.empty()}, if the item is filtered out from the webhook notification.
	 */
	<T extends ItemModel> Optional<T> filter(final T item, final String scriptUri);
}
