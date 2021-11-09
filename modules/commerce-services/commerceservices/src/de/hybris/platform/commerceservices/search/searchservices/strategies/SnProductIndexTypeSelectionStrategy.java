/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.search.searchservices.strategies;

import java.util.Optional;


/**
 * Resolves the index type for products.
 */
public interface SnProductIndexTypeSelectionStrategy
{
	/**
	 * Returns the index type for products based on the current session.
	 *
	 * @return index type for products
	 */
	Optional<String> getProductIndexTypeId();
}
