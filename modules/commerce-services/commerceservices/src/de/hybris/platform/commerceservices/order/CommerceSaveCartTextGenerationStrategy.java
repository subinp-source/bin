/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.order;

import de.hybris.platform.core.model.order.CartModel;


/**
 * Exposes methods to generate texts (such as a name or description) for a cart to be saved. These methods are only
 * called if these texts are not provided.
 */
public interface CommerceSaveCartTextGenerationStrategy
{
	/**
	 * Generates a name for the cart to be saved
	 *
	 * @param cartToBeSaved
	 *           {@link CartModel}
	 * @return the generated name
	 */
	String generateSaveCartName(CartModel cartToBeSaved);

	/**
	 * Generates a description for the cart to be saved
	 *
	 * @param cartToBeSaved
	 *           {@link CartModel}
	 * @return the generated description
	 */
	String generateSaveCartDescription(CartModel cartToBeSaved);

	/**
	 * Generates a name for the cloned saved cart to be saved
	 *
	 * @param savedCartToBeCloned
	 *           {@link CartModel}
	 * @param copyCountRegex
	 *           {@link String} parameter that contains the regex to generate the clone name.
	 * @return the generated name
	 */
	String generateCloneSaveCartName(CartModel savedCartToBeCloned, String copyCountRegex);
}
