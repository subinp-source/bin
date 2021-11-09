/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.service.impl;

import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.cmsfacades.data.StructureTypeMode;
import de.hybris.platform.cmsfacades.types.service.StructureTypeModeAttributePopulators;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

import java.util.List;
import java.util.function.BiPredicate;

import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of {@link StructureTypeModeAttributePopulators}
 */
public class DefaultStructureTypeModeAttributePopulators implements StructureTypeModeAttributePopulators
{
	private BiPredicate<AttributeDescriptorModel, StructureTypeMode> constrainedBy;
	private List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> attributePopulators;

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

	@Required
	public void setConstrainedBy(final BiPredicate<AttributeDescriptorModel, StructureTypeMode> constrainedBy)
	{
		this.constrainedBy = constrainedBy;
	}

	@Required
	public void setAttributePopulators(
			final List<Populator<AttributeDescriptorModel, ComponentTypeAttributeData>> attributePopulators)
	{
		this.attributePopulators = attributePopulators;
	}
}
