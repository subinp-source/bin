/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies.impl;

import de.hybris.platform.acceleratorservices.payment.strategies.FraudCallbackStrategy;
import java.util.Map;
import org.apache.log4j.Logger;


public class DefaultFraudCallbackStrategy implements FraudCallbackStrategy
{
	private static final Logger LOG = Logger.getLogger(DefaultFraudCallbackStrategy.class);

	@Override
	public void handleFraudCallback(final Map<String, String> parameters)
	{
		LOG.warn("An empty implementation of fraudulent transaction handling called.");
	}
}
