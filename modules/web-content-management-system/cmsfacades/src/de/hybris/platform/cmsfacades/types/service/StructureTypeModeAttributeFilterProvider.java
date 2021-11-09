/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service;

import de.hybris.platform.cmsfacades.data.StructureTypeMode;

import java.util.List;

/**
 * Structure Type Mode Provider Interface
 */
public interface StructureTypeModeAttributeFilterProvider
{

	/**
	 * Returns the structure type mode list given its type code and structure mode. 
	 * @param typeCode the type code
	 * @param mode the structure type mode
	 * @return a list of {@link StructureTypeModeAttributeFilter}; never null. 
	 */
	List<StructureTypeModeAttributeFilter> getStructureTypeModeAttributeFilters(final String typeCode, final StructureTypeMode mode);

	/**
	 * Adds a new Structure Type Mode to the existing list of mode definitions. 
	 */
	void addStructureTypeModeAttributeFilter(StructureTypeModeAttributeFilter mode);
}
