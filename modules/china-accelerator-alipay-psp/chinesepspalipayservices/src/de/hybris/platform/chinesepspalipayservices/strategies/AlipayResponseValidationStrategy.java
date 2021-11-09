/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chinesepspalipayservices.strategies;

import java.util.Map;


/**
 * Validates alipay response after reception
 */
public interface AlipayResponseValidationStrategy
{
	/**
	 * Validates response map from alipay. Returns true if response is correct
	 *
	 * @param params
	 *           alipay request map
	 * @return true if NotifyId and Signature are valid, returns false otherwise
	 */
	boolean validateResponse(final Map<String, String> params);
}
