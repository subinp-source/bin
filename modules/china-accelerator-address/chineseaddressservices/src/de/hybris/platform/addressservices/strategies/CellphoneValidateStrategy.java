/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addressservices.strategies;

/**
 * Verifies cell phone strategy
 */
public interface CellphoneValidateStrategy
{

	/**
	 * Validates the specific cell phone
	 *
	 * @param cellphone
	 *           the specific cell phone
	 * @return validated result
	 */
	boolean isInvalidCellphone(final String cellphone);
}
