/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionservices.model;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;


public class PriceRowsValidProductAttribute implements DynamicAttributeHandler<Boolean, ProductModel>, Serializable
{

	@Override
	public Boolean get(final ProductModel model)
	{
		if (model.getEurope1Prices() != null)
		{
			final Optional<Collection<String>> errorMessages = model.getEurope1Prices().stream()
					.filter(it -> it instanceof SubscriptionPricePlanModel)
					.map(it -> ((SubscriptionPricePlanModel) it).getValidationMessages())
					.filter(messages -> (messages.size() == 1 && !messages.iterator().next()
							.equals(Localization.getLocalizedString("subscriptionservices.customvalidation.priceplan.correct")))
							|| messages.size() > 1)
					.findAny();
			if (errorMessages.isPresent())
			{
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	@Override
	public void set(final ProductModel model, final Boolean value)
	{
		throw new UnsupportedOperationException();
	}
}
