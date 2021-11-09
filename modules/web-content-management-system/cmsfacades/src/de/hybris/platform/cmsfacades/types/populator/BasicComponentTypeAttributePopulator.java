/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.populator;

import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 * This populator will convert the {@link AttributeDescriptorModel#getQualifier()} and
 * {@link AttributeDescriptorModel#getLocalized()} only.
 */
public class BasicComponentTypeAttributePopulator implements Populator<AttributeDescriptorModel, ComponentTypeAttributeData>
{

	@Override
	public void populate(final AttributeDescriptorModel source, final ComponentTypeAttributeData target) throws ConversionException
	{
		target.setEditable(true);
		target.setQualifier(source.getQualifier());
		target.setLocalized(source.getLocalized());
	}

}
