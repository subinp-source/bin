/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service;

import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.StructureTypeMode;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.List;
import java.util.function.BiPredicate;

/**
 * Service to provide populators for {@link AttributeDescriptorModel} and {@link StructureTypeMode}. 
 */
public interface AttributeModePopulatorsProvider
{

	/**
	 * Given an attribute descriptor model and a StructureTypeMode, 
	 * return the list of Populators for this attribute type defined in the configuration. 
	 * @param attributeDescriptor the attribute that will be tested to look for populators.
	 * @param structureTypeMode the {@link StructureTypeMode} 
	 * @return the list of populators. 
	 */
	List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> getAttributePopulators(
			final AttributeDescriptorModel attributeDescriptor, final StructureTypeMode structureTypeMode);

	/**
	 * Adds new attribute structure type mode to the existing list of attribute structure per mode. 
	 * @param constrainedBy the biPredicate that contrains the usage of the following populators.
	 * @param attributePopulators the attribute populators that will be applied if the corresponding biPredicate is true. 
	 */
	void addStructureTypeModeAttributePopulators(final BiPredicate<AttributeDescriptorModel, StructureTypeMode> constrainedBy, 
			final List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> attributePopulators);
}
