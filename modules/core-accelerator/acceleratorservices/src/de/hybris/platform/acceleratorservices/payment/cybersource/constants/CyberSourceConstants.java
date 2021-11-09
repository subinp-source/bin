/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.cybersource.constants;

import de.hybris.platform.acceleratorservices.payment.constants.PaymentConstants;


/**
 * 
 */
public interface CyberSourceConstants
{
	interface HopProperties extends PaymentConstants.PaymentProperties // NOSONAR
	{
		String HOP_TEST_CURRENCY = "hop.cybersource.testCurrency";
		String MERCHANT_ID = "hop.cybersource.merchantID";
		String SHARED_SECRET = "hop.cybersource.sharedSecret";
		String SERIAL_NUMBER = "hop.cybersource.serialNumber";
		String HOP_SETUP_FEE = "hop.cybersource.setupFee";
	}
}
