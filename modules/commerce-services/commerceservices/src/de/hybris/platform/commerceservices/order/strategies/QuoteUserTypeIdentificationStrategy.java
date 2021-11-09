/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies;

import de.hybris.platform.commerceservices.enums.QuoteUserType;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Optional;


/**
 * Strategy to help return the current user type of user that is acting on a quote
 */
public interface QuoteUserTypeIdentificationStrategy
{
	/**
	 * Returns the {@link QuoteUserType} of the current user.
	 *
	 * @param userModel
	 *           user for which to get the quote user type
	 * @return the {@link QuoteUserType} of the given user
	 * @throws IllegalArgumentException
	 *            if the user model is null
	 */
	Optional<QuoteUserType> getCurrentQuoteUserType(UserModel userModel);
}