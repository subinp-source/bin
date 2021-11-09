/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.cmsitems.attributeconverters;

import de.hybris.platform.cms2.common.functions.Converter;
import de.hybris.platform.cmsfacades.constants.CmsfacadesConstants;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;


/**
 * Attribute Converter for {@link java.util.Date}.
 * Converts the Date to its proper {@link java.time.Instant} object and formats the date from {@link ZoneOffset#UTC} zone.
 */
public class DateAttributeToDataContentConverter implements Converter<Date, String>
{

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(CmsfacadesConstants.DATE_TIME_FORMAT);

	@Override
	public String convert(final Date source)
	{
		if (Objects.isNull(source))
		{
			return null;
		}
		return ZonedDateTime
				.ofInstant(source.toInstant(), ZoneOffset.UTC)
				.format(DATE_FORMATTER);
	}
}
