/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.solrfacetsearch.provider.entity;

import java.math.BigDecimal;


/**
 * Entity to hold one entry in the Solr price range.
 */
public class SolrPriceRangeEntry
{
	private final BigDecimal value;
	private final String currencyIso;

	public SolrPriceRangeEntry(final String value, final String currencyIso)
	{
		this.value = new BigDecimal(value);
		this.currencyIso = currencyIso;
	}

	public BigDecimal getValue()
	{
		return value;
	}

	public String getCurrencyIso()
	{
		return currencyIso;
	}
}
