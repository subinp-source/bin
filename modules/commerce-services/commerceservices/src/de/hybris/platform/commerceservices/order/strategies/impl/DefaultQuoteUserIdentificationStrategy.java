/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies.impl;

import de.hybris.platform.commerceservices.order.strategies.QuoteUserIdentificationStrategy;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link QuoteUserIdentificationStrategy}.
 */
public class DefaultQuoteUserIdentificationStrategy implements QuoteUserIdentificationStrategy
{
	private UserService userService;

	@Override
	public UserModel getCurrentQuoteUser()
	{
		return getUserService().getCurrentUser();
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

}
