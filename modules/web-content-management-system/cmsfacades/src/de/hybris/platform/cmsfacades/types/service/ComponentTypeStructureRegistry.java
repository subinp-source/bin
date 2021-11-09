/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service;

import java.util.Collection;
import java.util.Optional;


/**
 * Registry that stores a collection of <code>ComponentTypeStructure</code> elements.
 */
public interface ComponentTypeStructureRegistry
{

	/**
	 * Get a specific <code>ComponentTypeAttributeStructure</code> by typecode and qualifier.
	 *
	 * @param typecode
	 *           - the typecode of the element to retrieve from the registry.
	 * @param qualifier
	 *           - the qualifier of the element to retrieve from the registry.
	 * @return the element matching the typecode and qualifier
	 */
	Optional<ComponentTypeAttributeStructure> getComponentTypeAttributeStructure(String typecode, String qualifier);

	/**
	 * Get a specific <code>ComponentTypeStructure</code> by its typecode.
	 *
	 * @param typecode
	 *           - the typecode of the element to retrieve from the registry.
	 * @return the element matching the typecode
	 */
	ComponentTypeStructure getComponentTypeStructure(String typecode);

	/**
	 * Get all elements in the registry.
	 *
	 * @return all items in the registry or an empty collection if no elements are found.
	 */
	Collection<ComponentTypeStructure> getComponentTypeStructures();

	/**
	 * Get a specific <code>ComponentTypeStructure</code> of an Abstract type by its itemtype.
	 *
	 * @deprecated since 1905, not needed by the generic structure service
	 *
	 * @param itemtype
	 *           - the itemtype of the element to retrieve from the registry.
	 * @return the element matching the itemtype
	 */
	@Deprecated(since = "1905", forRemoval = true)
	ComponentTypeStructure getAbstractComponentTypeStructure(String itemtype);

}
