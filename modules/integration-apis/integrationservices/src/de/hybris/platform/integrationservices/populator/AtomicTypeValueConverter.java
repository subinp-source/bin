/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.integrationservices.populator;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class AtomicTypeValueConverter implements Converter<Object, Object>
{
	private static final String DATE_FORMAT = "/Date(%s)/";

	public Object convert(final Object value)
	{
		if (value instanceof Date)
		{
			return String.format(DATE_FORMAT, ((Date) value).getTime());
		}
		else if (value instanceof Long || value instanceof BigDecimal)
		{
			return (value.toString());
		}
		return value;
	}
}
