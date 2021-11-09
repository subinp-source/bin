/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.model;

import de.hybris.platform.commerceservices.model.consent.ConsentModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;


public class ConsentActiveAttribute extends AbstractDynamicAttributeHandler<Boolean, ConsentModel>
{
	@Override
	public Boolean get(final ConsentModel model)
	{
		if (model == null)
		{
			throw new IllegalArgumentException("consent must not be null");
		}

		return Boolean.valueOf(model.getConsentWithdrawnDate() == null);
	}
}
