/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service.impl;

import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.StructureTypeMode;
import de.hybris.platform.cmsfacades.types.service.AttributeModePopulatorsProvider;
import de.hybris.platform.cmsfacades.types.service.StructureTypeModeAttributePopulators;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link AttributeModePopulatorsProvider}.  
 */
public class DefaultAttributeModePopulatorsProvider implements AttributeModePopulatorsProvider
{
	
	private List<StructureTypeModeAttributePopulators> attributeStructureTypeModes;
	
	@Override
	public List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> getAttributePopulators(
			final AttributeDescriptorModel attributeDescriptor, final StructureTypeMode structureTypeMode)
	{
		// return the populators in order in which they were defined in the configuration
		return getAttributeStructureTypeModes()
				.stream() //
				.filter(attributeMode -> attributeMode.getConstrainedBy().test(attributeDescriptor, structureTypeMode)) //
				.map(StructureTypeModeAttributePopulators::getAttributePopulators) //
				.flatMap(Collection::stream) //
				.collect(Collectors.toList());
	}

	@Override
	public void addStructureTypeModeAttributePopulators(
			final BiPredicate<AttributeDescriptorModel, StructureTypeMode> constrainedBy,
			final List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> attributePopulators)
	{
		getAttributeStructureTypeModes().add(new StructureTypeModeAttributePopulators()
		{
			@Override
			public BiPredicate<AttributeDescriptorModel, StructureTypeMode> getConstrainedBy()
			{
				return constrainedBy;
			}

			@Override
			public List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> getAttributePopulators()
			{
				return attributePopulators;
			}
		});
	}

	protected List<StructureTypeModeAttributePopulators> getAttributeStructureTypeModes()
	{
		return attributeStructureTypeModes;
	}

	@Required
	public void setAttributeStructureTypeModes(final List<StructureTypeModeAttributePopulators> attributeStructureTypeModes)
	{
		this.attributeStructureTypeModes = attributeStructureTypeModes;
	}
}
