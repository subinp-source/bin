/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.payment.strategies;

import java.util.List;

/**
 *   A mapping between Payment Service Providers error codes and Spring form ids.
 */
public interface ErroCodeToFormFieldMappingStrategy
{
	/**
	 * Gets a spring form field id that maps to an error code from a payment provider
	 * @param code An error code from PSP
	 * @return A form field id
	 */
	List<String> getFieldForErrorCode(Integer code);
}
