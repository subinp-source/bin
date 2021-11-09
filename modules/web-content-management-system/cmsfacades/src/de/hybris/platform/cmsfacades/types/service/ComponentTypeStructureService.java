/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.Collection;

/**
 * Service that computes the <code>ComponentTypeStructure</code> from a given TYPECODE.
 */
public interface ComponentTypeStructureService
{
	/**
	 * Get a <code>ComponentTypeStructure</code> by its typecode.
	 *
	 * @param typeCode
	 *           - the typeCode of the element to retrieve.
	 * @return the element matching the typeCode
	 * @throws UnknownIdentifierException when the typeCode does not exist
	 * @throws ConversionException when the type requested does not extend CMSItem 
	 */
	ComponentTypeStructure getComponentTypeStructure(String typeCode);

	/**
	 * Get all elements in the registry.
	 *
	 * @return all items in the registry or an empty collection if no elements are found.
	 */
	Collection<ComponentTypeStructure> getComponentTypeStructures();
}
