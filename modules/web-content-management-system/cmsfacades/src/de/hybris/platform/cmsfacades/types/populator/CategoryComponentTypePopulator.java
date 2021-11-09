/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.populator;

import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.cmsfacades.data.StructureTypeCategory;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;


/**
 * This populator will populate {@link de.hybris.platform.cmsfacades.data.ComponentTypeData#setCategory(String)}
 */
public class CategoryComponentTypePopulator implements Populator<ComposedTypeModel, ComponentTypeData>
{
	private StructureTypeCategory category = StructureTypeCategory.COMPONENT;

	@Override
	public void populate(final ComposedTypeModel source, final ComponentTypeData target) throws ConversionException
	{
		target.setCategory(getCategory().name());
	}

	protected StructureTypeCategory getCategory()
	{
		return category;
	}

	@Required
	public void setCategory(final StructureTypeCategory category)
	{
		this.category = category;
	}

}
