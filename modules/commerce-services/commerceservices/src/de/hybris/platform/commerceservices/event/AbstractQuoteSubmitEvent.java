/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.event;


import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.QuoteUserType;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;


/**
 * Abstract base class for Quote submit events which can be extended to create a new event.
 *
 * @param <T>
 */
public abstract class AbstractQuoteSubmitEvent<T extends BaseSiteModel> extends AbstractCommerceUserEvent<T>
{
	private final QuoteModel quote;
	private final UserModel userModel;
	private final QuoteUserType quoteUserType;

	/**
	 * Default Constructor
	 *
	 * @param quote
	 * @param userModel
	 * @param quoteUserType
	 */
	public AbstractQuoteSubmitEvent(final QuoteModel quote, final UserModel userModel, final QuoteUserType quoteUserType)
	{
		this.quote = quote;
		this.userModel = userModel;
		this.quoteUserType = quoteUserType;
	}

	/**
	 * @return the quote
	 */
	public QuoteModel getQuote()
	{
		return quote;
	}

	/**
	 * @return the quoteUserType
	 */
	public QuoteUserType getQuoteUserType()
	{
		return quoteUserType;
	}

	/**
	 * @return the userModel
	 */
	public UserModel getUserModel()
	{
		return userModel;
	}
}
