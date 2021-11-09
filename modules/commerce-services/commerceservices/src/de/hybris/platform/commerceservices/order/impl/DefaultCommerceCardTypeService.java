/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order.impl;

import de.hybris.platform.commerceservices.order.CommerceCardTypeService;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.payment.dto.CardType;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of service which returns defined card types
 */
public class DefaultCommerceCardTypeService implements CommerceCardTypeService
{
	private EnumerationService enumerationService;
	private TypeService typeService;

	private final Collection<CardType> cardTypes = Collections.synchronizedList(new ArrayList());

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(final TypeService typeService)
	{
		this.typeService = typeService;
	}

	protected void createCardTypeCollection()
	{
		synchronized (cardTypes)
		{
			if (cardTypes.isEmpty())
			{
				final List<CreditCardType> creditCardTypes = getEnumerationService().getEnumerationValues(CreditCardType._TYPECODE);
				for (final CreditCardType creditCardType : creditCardTypes)
				{
					final CardType cardType = new CardType(creditCardType.getCode(), creditCardType,
							getTypeService().getEnumerationValue(creditCardType).getName());
					cardTypes.add(cardType);
				}
			}
		}
	}

	@Override
	public Collection<CardType> getCardTypes()
	{
		createCardTypeCollection();

		return cardTypes;
	}

	@Override
	public CardType getCardTypeForCode(final String code)
	{
		createCardTypeCollection();

		for (final CardType cardType : cardTypes)
		{
			if (cardType.getCode().getCode().equals(code))
			{
				return cardType;
			}
		}
		return null;
	}

}
