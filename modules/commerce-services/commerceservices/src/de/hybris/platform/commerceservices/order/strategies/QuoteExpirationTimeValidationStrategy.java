/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies;

import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.servicelayer.time.TimeService;


/**
 * Strategy To Validate Quote Expiration Time
 */
public interface QuoteExpirationTimeValidationStrategy
{
	/**
	 * This method determines whether the supplied QuoteModel has Expired or not. Criteria used to determine whether the
	 * Quote has expired is listed below, <br/>
	 * <br/>
	 * 1) QuoteModel.expirationTime >= current time (current SystemTime)
	 *
	 * <br/>
	 * <br/>
	 * Disclaimer : Uses {@link TimeService} to determine Current Time.
	 *
	 * @param quoteModel
	 *           the quote to inspect
	 *
	 * @return true if the quote is expired, false otherwise
	 * @throws IllegalArgumentException
	 *            if the quote model is null
	 */
	boolean hasQuoteExpired(QuoteModel quoteModel);
}
