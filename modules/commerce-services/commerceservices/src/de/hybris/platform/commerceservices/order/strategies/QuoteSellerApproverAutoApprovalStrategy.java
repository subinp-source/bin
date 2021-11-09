/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies;


import de.hybris.platform.core.model.order.QuoteModel;


/**
 * Strategy that will be used by business process to check a quote can be auto approved or require seller approval
 * action
 */
public interface QuoteSellerApproverAutoApprovalStrategy
{
	/**
	 * Check whether the quote's sub total amount is within the configured quote auto approval threshold.
	 *
	 * @param quoteModel
	 *           the quote to inspect
	 * @return true if the quote is within the auto approval threshold, false otherwise
	 */
	boolean shouldAutoApproveQuote(QuoteModel quoteModel);
}
