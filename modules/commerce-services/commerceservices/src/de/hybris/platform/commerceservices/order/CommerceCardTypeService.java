/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.payment.dto.CardType;

import java.util.Collection;


/**
 * Service that returns defined card types
 */
public interface CommerceCardTypeService
{
	/**
	 * Get all credit card types
	 * 
	 * @return the {@link Collection} of card types
	 */
	Collection<CardType> getCardTypes();

	/**
	 * Gets a card type by code
	 * 
	 * @param code
	 *           the card type code
	 * @return the card type
	 */
	CardType getCardTypeForCode(String code);
}
