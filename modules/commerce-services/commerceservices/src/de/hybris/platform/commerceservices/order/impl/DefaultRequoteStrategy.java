/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.commerceservices.order.RequoteStrategy;
import de.hybris.platform.commerceservices.order.strategies.QuoteUpdateStateStrategy;
import de.hybris.platform.commerceservices.util.CommerceQuoteUtils;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.QuoteEntryModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.strategies.impl.GenericAbstractOrderCloningStrategy;

import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link de.hybris.platform.commerceservices.order.RequoteStrategy}
 */
public class DefaultRequoteStrategy extends GenericAbstractOrderCloningStrategy<QuoteModel, QuoteEntryModel, QuoteModel>
		implements RequoteStrategy
{
	private CommerceQuoteUtils commerceQuoteUtils;
	private QuoteUpdateStateStrategy quoteUpdateStateStrategy;

	public DefaultRequoteStrategy()
	{
		super(QuoteModel.class, QuoteEntryModel.class, QuoteModel.class);
	}

	@Override
	public QuoteModel requote(final QuoteModel quote)
	{
		validateParameterNotNullStandardMessage("quote", quote);

		final QuoteModel newQuote = clone(quote, Optional.empty());
		postProcess(quote, newQuote);

		return newQuote;
	}

	@Override
	protected void postProcess(final QuoteModel original, final QuoteModel copy)
	{
		copy.setName(null);
		copy.setDescription(null);
		copy.setExpirationTime(null);
		copy.setComments(null);
		if (CollectionUtils.isNotEmpty(copy.getEntries()))
		{
			for (final AbstractOrderEntryModel orderEntry : copy.getEntries())
			{
				orderEntry.setComments(null);
			}
		}
		getCommerceQuoteUtils().removeExistingQuoteDiscount(copy);

		copy.setVersion(Integer.valueOf(1));
		copy.setCartReference(null);
		copy.setAssignee(null);
		copy.setGeneratedNotifications(null);
		copy.setPreviousEstimatedTotal(null);
		getQuoteUpdateStateStrategy().updateQuoteState(QuoteAction.CREATE, copy, copy.getUser());
	}

	protected CommerceQuoteUtils getCommerceQuoteUtils()
	{
		return commerceQuoteUtils;
	}

	@Required
	public void setCommerceQuoteUtils(final CommerceQuoteUtils commerceQuoteUtils)
	{
		this.commerceQuoteUtils = commerceQuoteUtils;
	}

	protected QuoteUpdateStateStrategy getQuoteUpdateStateStrategy()
	{
		return quoteUpdateStateStrategy;
	}

	@Required
	public void setQuoteUpdateStateStrategy(final QuoteUpdateStateStrategy quoteUpdateStateStrategy)
	{
		this.quoteUpdateStateStrategy = quoteUpdateStateStrategy;
	}
}
