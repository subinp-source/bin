/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.document;

import java.math.BigDecimal;

public class AmountRange implements Range
{

	private final BigDecimal minAmountRange;
	private final BigDecimal maxAmountRange;

	public AmountRange(final BigDecimal minAmountRange, final BigDecimal maxAmountRange)
	{
		this.minAmountRange = minAmountRange;
		this.maxAmountRange = maxAmountRange;
	}

	@Override
	public BigDecimal getMinBoundary()
	{
		return this.minAmountRange;
	}


	@Override
	public BigDecimal getMaxBoundary()
	{
		return maxAmountRange;
	}
}
