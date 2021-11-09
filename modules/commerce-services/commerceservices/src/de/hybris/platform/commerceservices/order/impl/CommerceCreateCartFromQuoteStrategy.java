/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.QuoteModel;
import de.hybris.platform.order.strategies.impl.DefaultCreateCartFromQuoteStrategy;


/**
 * Commerce specific extension of {@link DefaultCreateCartFromQuoteStrategy}
 */
public class CommerceCreateCartFromQuoteStrategy extends DefaultCreateCartFromQuoteStrategy
{

	@Override
	protected void postProcess(final QuoteModel original, final CartModel copy)
	{
		super.postProcess(original, copy);
		copy.setQuoteReference(original);
		original.setCartReference(copy);
	}


}
