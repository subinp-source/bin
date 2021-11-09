/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.strategies.hooks;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

import java.util.List;


/**
 * Hook interface for CartValidation
 */
public interface CartValidationHook
{
	/**
	 * Executed before the cart validation
	 *
	 * @param parameter
	 *           object containing all the information for validation
	 * @param modifications
	 *           list containing the validation results
	 */
	void beforeValidateCart(CommerceCartParameter parameter, List<CommerceCartModification> modifications);

	/**
	 * Executed after the cart validation
	 *
	 * @param parameter
	 *           object containing all the information for validation
	 * @param modifications
	 *           list containing the validation results
	 */
	void afterValidateCart(CommerceCartParameter parameter, List<CommerceCartModification> modifications);
}
