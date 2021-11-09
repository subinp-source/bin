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
 * Defines attribute structure type mode data and its populators, constrained by of a BiPredicate with the corresponding attribute 
 * descriptor and a StructureTypeMode. 
 */
public interface StructureTypeModeAttributePopulators
{

	/**
	 * The BiPredicate that constrains this list of populators. 
	 * The first argument is the {@link AttributeDescriptorModel} and the second argument is the {@link StructureTypeMode}. 
	 * @return the biPredicate that constrains this attribute/mode's populator. 
	 */
	BiPredicate<AttributeDescriptorModel, StructureTypeMode> getConstrainedBy();

	/**
	 * The {@link Populator<AttributeDescriptorModel, ComponentTypeAttributeData>} list
	 * @return populators the populator list that should be applied for this attribute and mode. 
	 */
	List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> getAttributePopulators();

}
