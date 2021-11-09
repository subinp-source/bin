/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.consent.converters.populator;

import org.springframework.util.Assert;

import de.hybris.platform.commercefacades.consent.data.ConsentData;
import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.converters.Populator;


/**
 * Default populator that converts source {@link ConsentModel} to target {@link ConsentData}
 */
public class ConsentPopulator implements Populator<ConsentModel, ConsentData>
{
	@Override
	public void populate(final ConsentModel source, final ConsentData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setCode(source.getCode());
		target.setConsentWithdrawnDate(source.getConsentWithdrawnDate());
		target.setConsentGivenDate(source.getConsentGivenDate());
	}
}
