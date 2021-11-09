/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies;

import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;


/**
 * Strategy to help validate quote metadata attributes, such as name
 */
public interface QuoteMetadataValidationStrategy
{
	/**
	 * Validates the metadata attributes (i.e. name) for a quote. When a seller approver Approves the quote,
	 * <tt>IllegalStateException</tt> will be thrown if the expiration time is not set.
	 *
	 * @param quoteAction
	 *           the action that is being performed
	 * @param quoteModel
	 *           the quote to be validated
	 * @param userModel
	 *           the user performing the action on the quote
	 * @throws IllegalArgumentException
	 *            if any attributes fail validation or if the quote user type is unknown
	 * @throws IllegalStateException
	 *            if a seller approver performs Approve action and the expiration time is not set
	 */
	void validate(QuoteAction quoteAction, QuoteModel quoteModel, UserModel userModel);
}
