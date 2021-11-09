/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.pages.service;

import java.util.Collection;
import java.util.Optional;


/**
 * Registry that stores a collection of <code>PageVariationResolverType</code> elements.
 */
public interface PageVariationResolverTypeRegistry
{
	/**
	 * Get a specific <code>PageVariationResolverType</code> by type code.
	 *
	 * @param typecode
	 *           - the model type code of the element to retrieve from the registry
	 * @return the element matching the page type. If none is found matching the given page type, return the resolver
	 *         type for <code>AbstractPageModel</code>
	 */
	Optional<PageVariationResolverType> getPageVariationResolverType(String typecode);

	/**
	 * Get all elements in the registry.
	 *
	 * @return all items in the registry; never <tt>null</tt>
	 */
	Collection<PageVariationResolverType> getPageVariationResolverTypes();
}
