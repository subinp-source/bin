/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.formatters;

import java.math.BigDecimal;
import java.util.Locale;

import de.hybris.platform.core.model.c2l.CurrencyModel;

public interface AmountFormatter
{
	public String formatAmount( final BigDecimal value, final CurrencyModel currency, final Locale locale );
}
