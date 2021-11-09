/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.types.populator;

import de.hybris.platform.cmsfacades.data.ComponentTypeAttributeData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populator that populates the id attribute field of the {@link ComponentTypeAttributeData} POJO.
 */
public class IdComponentTypeAttributePopulator implements Populator<AttributeDescriptorModel, ComponentTypeAttributeData>
{
	private String idAttribute;

	@Override
	public void populate(final AttributeDescriptorModel source, final ComponentTypeAttributeData target) throws ConversionException
	{
		target.setIdAttribute(getIdAttribute());
	}

	protected String getIdAttribute()
	{
		return idAttribute;
	}

	@Required
	public void setIdAttribute(final String idAttribute)
	{
		this.idAttribute = idAttribute;
	}
}
