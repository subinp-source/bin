/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.address.util;

import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.address.data.AddressVerificationResultData;

import org.apache.commons.collections.CollectionUtils;

/**
 * Utility class with methods to help handle an AddressVerificationResult.
 */
public final class AddressVerificationResultUtils
{
	/**
	 * Determine if the input AddressVerificationResult contains field errors.
	 * 
	 * @param resultData
	 *            the AddressVerificationResult to check for errors.
	 * @param <A>
	 *            the generic AddressVerificationResult type.
	 * @return true if the AddressVerificationResult has errors, false if not.
	 */
	public static <A extends AddressVerificationResultData> boolean requiresErrorHandling(final A resultData)
	{
		return resultData != null
				&& ((CollectionUtils.isNotEmpty(resultData.getFieldErrors()))
					|| AddressVerificationDecision.REJECT.equals(resultData.getDecision())
					|| AddressVerificationDecision.UNKNOWN.equals(resultData.getDecision()));
	}

	/**
	 * Determine if the input AddressVerificationResult contains suggested addresses.
	 * 
	 * @param resultData
	 *            the AddressVerificationResult to check for suggested addresses.
	 * @param <A>
	 *            the generic AddressVerificationResult type.
	 * @return true if the AddressVerificationResult has suggested addresses, false if not.
	 */
	public static <A extends AddressVerificationResultData> boolean hasSuggestedAddresses(final A resultData)
	{
		return resultData != null && CollectionUtils.isNotEmpty(resultData.getSuggestedAddresses());
	}
}
