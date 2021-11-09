/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies;

import java.util.Map;


/**
 * A strategy to handle callbacks about fraud from a payment service provider
 * 
 */
public interface FraudCallbackStrategy
{
	/**
	 * Handle fraudulent transactions
	 * 
	 * @param parameters
	 *           A map of key value pairs about fraudulent payment transactions.
	 */
	void handleFraudCallback(final Map<String, String> parameters);
}
