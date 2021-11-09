/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.populators.impl;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.TaxValue;

import java.util.Collection;

import org.cxml.Money;
import org.cxml.Tax;
import org.cxml.TaxDetail;


/**
 * Populates a collection of {@link TaxValue} based on an {@link Tax}.
 */
public class DefaultTaxValuePopulator implements Populator<Tax, Collection<TaxValue>>
{

	@Override
	public void populate(final Tax source, final Collection<TaxValue> target) throws ConversionException
	{
		if (source == null)
		{
			// no tax info has been provided
			return;
		}
		for (final TaxDetail taxDetail : source.getTaxDetail())
		{
			final Money taxAmount = taxDetail.getTaxAmount().getMoney();
			final TaxValue taxValue = new TaxValue(taxDetail.getCategory(), Double.parseDouble(taxAmount.getvalue()), true,
					taxAmount.getCurrency());
			target.add(taxValue);

		}
	}

}
