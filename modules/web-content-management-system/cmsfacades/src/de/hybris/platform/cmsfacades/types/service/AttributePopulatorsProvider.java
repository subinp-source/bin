/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service;

import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.List;

/**
 * Service to provide populators for AttributeDescriptorModels
 */
public interface AttributePopulatorsProvider
{

	/**
	 * Given an attribute descriptor model, return the list of Populators for this attribute type defined in the configuration map. 
	 * @param attributeDescriptor the attribute that will be tested to look for populators. 
	 * @return the list of populators. 
	 */
	List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> getAttributePopulators(final AttributeDescriptorModel attributeDescriptor);
}
