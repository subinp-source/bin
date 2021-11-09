/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service;


import de.hybris.platform.cmsfacades.data.StructureTypeMode;

/**
 * Service that computes the <code>ComponentTypeStructure</code> from a given TYPECODE and mode
 */
public interface StructureTypeModeService
{

	/**
	 * Get a single component type structure for a given structure type mode.
	 * @param typeCode
	 *           - the _TYPECODE of the component type to retrieve from the base mode
	 * @param mode
	 *           - the mode of the structure type
	 * @return the component type structure or null when the code and mode provided do not match any existing types
	 */
	ComponentTypeStructure getComponentTypeByCodeAndMode(final String typeCode, final StructureTypeMode mode);
}
