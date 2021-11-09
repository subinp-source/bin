/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.attributeconverters;

import static java.util.Objects.isNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.hybris.platform.cms2.common.functions.Converter;
import org.apache.commons.configuration.ConversionException;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default Converter to parse a String to primitive types such as String, Boolean, Integer, Float, Long, Double
 */
public class DefaultDataToAttributeContentConverter implements Converter<Object, Object>
{

	private Class<?> primitiveType;
	private static final String VALUEOF = "valueOf";

	@Override
	public Object convert(final Object source)
	{

		if (!isNull(source))
		{
			if (source.getClass() != getPrimitiveType())
			{
				try
				{
					final Method method = getPrimitiveType().getMethod(VALUEOF, String.class);
					return method.invoke(null, source.toString());
				}
				catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e)
				{
					throw new ConversionException(e);
				}
			}
			else if (source.getClass().isAssignableFrom(String.class) && ((String)source).isEmpty())
			{
				return null;
			}
			else
			{
				return source;
			}
		}
		else
		{
			return null;
		}
	}


	protected Class<?> getPrimitiveType()
	{
		return primitiveType;
	}

	@Required
	public void setPrimitiveType(final Class<?> primitiveType)
	{
		this.primitiveType = primitiveType;
	}
}
