/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.subscriptionfacades.billing;

import de.hybris.platform.commercefacades.order.data.CardTypeData;

import java.util.Collection;


/**
 * Facade for converting the hybris credit card codes to those of a billing provider.
 */
public interface CreditCardFacade
{
	/**
	 * mappingStrategy calls the appropriate service for converting the credit card codes.
	 * 
	 * @param creditCards
	 * 				credit card types to be mapped
	 * @return false, if no mapping strategy is implemented or no conversion has been processed
	 */
	boolean mappingStrategy(Collection<CardTypeData> creditCards);
}
