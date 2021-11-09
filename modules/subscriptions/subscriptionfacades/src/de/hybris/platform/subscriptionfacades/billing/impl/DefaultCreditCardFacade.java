/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.billing.impl;

import java.util.Collection;

import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.subscriptionfacades.billing.CreditCardFacade;

/**
 * Facade for converting credit card codes.
 */
public class DefaultCreditCardFacade implements CreditCardFacade
{
	@Override
	public boolean mappingStrategy(final Collection<CardTypeData> creditCards)
	{
		return false;
	}

}
