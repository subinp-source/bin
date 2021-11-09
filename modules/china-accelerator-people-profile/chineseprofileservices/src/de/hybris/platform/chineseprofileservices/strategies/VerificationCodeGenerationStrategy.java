/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.chineseprofileservices.strategies;


/**
 * Generate a verification code which will be sent to customer's mobile and verify customer's mobile number.
 * 
 * @deprecated Since 1905. Use {@link VerificationCodeStrategy} instead.
 */
@Deprecated(since = "1905", forRemoval= true )
public interface VerificationCodeGenerationStrategy
{

	/**
	 * Generate a verification code.
	 *
	 * @return verification code
	 */
	String generate();
}
