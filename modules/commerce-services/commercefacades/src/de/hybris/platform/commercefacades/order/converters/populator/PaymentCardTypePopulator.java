/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.payment.dto.CardType;
import de.hybris.platform.servicelayer.type.TypeService;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator implementation for {@link de.hybris.platform.payment.dto.CardType} as source and
 * {@link de.hybris.platform.commercefacades.order.data.CardTypeData} as target type.
 */
public class PaymentCardTypePopulator implements Populator<CardType, CardTypeData>
{
	private TypeService typeService;

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	@Override
	public void populate(final CardType source, final CardTypeData target)
	{
		final CreditCardType creditCardType = source.getCode();
		target.setCode(creditCardType.getCode());
		target.setName(getTypeService().getEnumerationValue(creditCardType).getName());
	}
}
