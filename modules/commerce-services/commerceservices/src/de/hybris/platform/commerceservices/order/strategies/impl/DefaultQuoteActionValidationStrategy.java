/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.enums.QuoteAction;
import de.hybris.platform.commerceservices.order.exceptions.IllegalQuoteStateException;
import de.hybris.platform.commerceservices.order.strategies.QuoteActionValidationStrategy;
import de.hybris.platform.commerceservices.order.strategies.QuoteStateSelectionStrategy;
import de.hybris.platform.core.enums.QuoteState;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link QuoteActionValidationStrategy}.
 */
public class DefaultQuoteActionValidationStrategy implements QuoteActionValidationStrategy
{
	private QuoteStateSelectionStrategy quoteStateSelectionStrategy;

	@Override
	public void validate(final QuoteAction quoteAction, final QuoteModel quoteModel, final UserModel userModel)
	{
		if (!isValidAction(quoteAction, quoteModel, userModel))
		{
			throw new IllegalQuoteStateException(quoteAction, quoteModel.getCode(), quoteModel.getState(), quoteModel.getVersion());
		}
	}

	@Override
	public boolean isValidAction(final QuoteAction quoteAction, final QuoteModel quoteModel, final UserModel userModel)
	{
		validateParameterNotNullStandardMessage("Quote action", quoteAction);
		validateParameterNotNullStandardMessage("Quote", quoteModel);
		validateParameterNotNullStandardMessage("User", userModel);

		final Set<QuoteState> states = getQuoteStateSelectionStrategy().getAllowedStatesForAction(quoteAction, userModel);

		return CollectionUtils.isNotEmpty(states) && states.contains(quoteModel.getState());

	}

	protected QuoteStateSelectionStrategy getQuoteStateSelectionStrategy()
	{
		return quoteStateSelectionStrategy;
	}

	@Required
	public void setQuoteStateSelectionStrategy(final QuoteStateSelectionStrategy quoteStateSelectionStrategy)
	{
		this.quoteStateSelectionStrategy = quoteStateSelectionStrategy;
	}
}
