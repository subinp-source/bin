/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.webhookservices.filter;

import de.hybris.platform.core.model.ItemModel;

import java.util.Optional;

/**
 * Users need to implement this interface in their scripts to filter the item being sent to the destination.
 * This is a Groovy example of how the script should look like
 * <pre>{@code
 *
 *  class MyFilter implements WebhookFilter {
 *
 *      public <T extends ItemModel> Optional<T> filter(T item) {
 *         // perform filtering
 *         // if item is to be sent, return Optional.of(item)
 *         // if item is not to be sent, return Optinal.empty()
 *         Optional.of(item)
 *      }
 *  }
 *  new MyFilter()
 *
 * }</pre>
 */
public interface WebhookFilter
{
	/**
	 * Filter the item before sending
	 *
	 * @param <T>  The ItemModel type
	 * @param item The item to filter
	 * @return Wrap the item in an {@link Optional} if it should be sent, e.g. {@code Optional.of(item)},
	 * if item is not to be sent, return {@link Optional#empty()}
	 */
	<T extends ItemModel> Optional<T> filter(T item);
}