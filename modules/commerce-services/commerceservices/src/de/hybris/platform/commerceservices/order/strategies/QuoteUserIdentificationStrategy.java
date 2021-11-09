/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies;

import de.hybris.platform.core.model.user.UserModel;


/**
 * Strategy to help return the current user that is acting on a quote
 */
public interface QuoteUserIdentificationStrategy
{

	/**
	 * Gets the current quote user. By default this will be the current session user.
	 *
	 * @return the current quote user
	 */
	UserModel getCurrentQuoteUser();
}
