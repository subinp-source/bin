/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.strategies;

import de.hybris.platform.core.model.order.AbstractOrderModel;


/**
 * @deprecated Since 6.3.
 */
@Deprecated(since = "6.3", forRemoval = true)
public interface QuoteEvaluationStrategy
{
	public boolean isQuoteAllowed(final AbstractOrderModel source);
}
