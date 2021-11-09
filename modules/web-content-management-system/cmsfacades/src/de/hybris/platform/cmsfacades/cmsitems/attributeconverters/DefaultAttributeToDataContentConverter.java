/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.attributeconverters;

import de.hybris.platform.cms2.cmsitems.converter.AttributeContentConverter;
import de.hybris.platform.cms2.common.functions.Converter;

/**
 * Default Attribute Converter.
 * This converter should only be used when all of the {@link AttributeContentConverter} instances fail to convert a given object.
 * Returns the {@link Object#toString()} from the Object.
 */
public class DefaultAttributeToDataContentConverter implements Converter<Object, Object>
{
	@Override
	public Object convert(final Object source)
	{
		return source;
	}
}
