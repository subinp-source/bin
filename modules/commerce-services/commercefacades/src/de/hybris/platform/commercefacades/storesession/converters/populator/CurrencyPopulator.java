/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.storesession.converters.populator;

import de.hybris.platform.commercefacades.storesession.data.CurrencyData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import org.springframework.util.Assert;


public class CurrencyPopulator<SOURCE extends CurrencyModel, TARGET extends CurrencyData> implements Populator<SOURCE, TARGET>
{

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setIsocode(source.getIsocode());
		target.setName(source.getName());
		target.setActive(source.getActive().booleanValue());
		target.setSymbol(source.getSymbol());
	}
}
