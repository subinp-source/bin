/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.strategies;

/**
 * A strategy interface for validating specific postcode
 */
public interface PostcodeValidateStrategy
{

	/**
	 * validate the specific postcode
	 *
	 * @param postcode
	 *           the specific postcode
	 * @return validated result
	 */
	default boolean validate(String postcode)
	{
		return true;
	}
}
