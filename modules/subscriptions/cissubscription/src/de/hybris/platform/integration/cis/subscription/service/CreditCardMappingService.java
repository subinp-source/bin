/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.integration.cis.subscription.service;

import de.hybris.platform.commercefacades.order.data.CardTypeData;

import java.util.Collection;

import javax.annotation.Nonnull;


/**
 * This service should be implemented, if there is a difference between the credit card codes defined in hybris in
 * comparison to those of a billing provider.
 */
public interface CreditCardMappingService
{
	/**
	 * Converts the code of a credit card to the appropriate code of the provider
	 *
	 * @param creditCards
	 *           the credit cards
	 * @param billingProvider
	 *           the name of the billing provider
	 * @return true if conversion is done
	 */
	boolean convertCCToProviderSpecificName(@Nonnull Collection<CardTypeData> creditCards, String billingProvider);
}
