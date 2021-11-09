/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.core.model.order.QuoteModel;


/**
 * Strategy for re-quote an inactive quote
 */
public interface RequoteStrategy
{
	/**
	 * Re-quote an existing quote to get a new quote
	 * <p>
	 * fields in the new created quoteModel are cleared including: name, description, expire time, cart comments, line
	 * item comments, cartReference, assignee and generatedNotifications
	 * </p>
	 * <p>
	 * the new quote's version will be set to 1; and state is set to CREATE
	 * </p>
	 *
	 * @param quote
	 *           quoteModel which will be re-quoted
	 * @return new quoteModel
	 */
	QuoteModel requote(final QuoteModel quote);
}
