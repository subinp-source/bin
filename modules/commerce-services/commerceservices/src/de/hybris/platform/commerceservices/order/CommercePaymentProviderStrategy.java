/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

/**
 *  A strategy for getting a payment provider
 *
 */
public interface CommercePaymentProviderStrategy
{
	/**
	 * Get payment provider assigned to the {@link de.hybris.platform.store.BaseStoreModel}
	 * @return A payment provider name.
	 */
	String getPaymentProvider();
}
