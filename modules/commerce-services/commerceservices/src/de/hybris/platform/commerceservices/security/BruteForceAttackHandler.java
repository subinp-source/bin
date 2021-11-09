/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.security;

/**
 * Interface for checking brute force attack attempts.
 */
public interface BruteForceAttackHandler
{
	/**
	 * Method registers attempt's timestamp.
	 *
	 * @param key
	 *           that the attempt is registered for
	 * @return: whether it is brute force attack
	 */
	boolean registerAttempt(final String key);


	/**
	 * Method resets the counter for the given key
	 *
	 * @param key
	 *           key that attempts counter will be reset
	 */
	void resetAttemptCounter(final String key);
}
