/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.populator;

import de.hybris.platform.cmsfacades.data.ComponentTypeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * This populator will convert the {@link ComposedTypeModel#getCode()} and {@link ComposedTypeModel#getName()} only.
 */
public class BasicComponentTypePopulator implements Populator<ComposedTypeModel, ComponentTypeData>
{

	@Override
	public void populate(final ComposedTypeModel source, final ComponentTypeData target) throws ConversionException
	{
		target.setCode(source.getCode());
		target.setName(source.getName());
	}

}
