/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.populator;

import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

/**
 * Populator that populates the editable field of the {@link ComponentTypeAttributeData} POJO.
 */
public class EditableComponentTypeAttributePopulator implements Populator<AttributeDescriptorModel, ComponentTypeAttributeData>
{
	private boolean isEditable;

	@Override
	public void populate(final AttributeDescriptorModel source, final ComponentTypeAttributeData target) throws ConversionException
	{
		target.setEditable(isEditable());
	}

	public void setEditable(final boolean isEditable)
	{
		this.isEditable = isEditable;
	}

	public boolean isEditable()
	{
		return isEditable;
	}
}
