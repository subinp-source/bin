/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies;

import de.hybris.platform.acceleratorservices.payment.data.SubscriptionInfoData;


/**
 *  Checks the PSP public signature against a computed value based on a shared secret.
 *
 */
public interface SignatureValidationStrategy
{
	/**
	 * Computes a signature for
	 * {@link de.hybris.platform.acceleratorservices.payment.data.SubscriptionInfoData#getSubscriptionSignedValue()} and
	 * shared secret value stored as a property with key
	 * {@link de.hybris.platform.acceleratorservices.payment.cybersource.constants.CyberSourceConstants.HopProperties#SHARED_SECRET}
	 * via {@link de.hybris.platform.acceleratorservices.payment.utils.impl.DefaultAcceleratorDigestUtils} and validates
	 * that the
	 * {@link de.hybris.platform.acceleratorservices.payment.data.SubscriptionInfoData#getSubscriptionIDPublicSignature()}
	 * is equal to this computed value
	 */
	boolean validateSignature(SubscriptionInfoData subscriptionInfoData);
}
