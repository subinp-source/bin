/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.addonsupport.valueprovider;

import java.util.Optional;


/**
 * Registry to access value providers by the name of the AddOn that defines them.
 */
public interface AddOnValueProviderRegistry
{
	/**
	 * Returns the value provider for the given AddOn name.
	 *
	 * @param addOnName
	 *           the name of the AddOn extension
	 * @return an {@link Optional} that either contains the value provider or is empty if no provider exists for the
	 *         given AddOn name
	 */
	Optional<AddOnValueProvider> get(String addOnName);
}
