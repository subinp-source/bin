/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.converter;

import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * Copyright ValueTranslator that format copyright year by using given date format.
 */
public class CopyrightTranslator extends AbstractValueTranslator
{
	private static final String DEFAULT_DATE_FORMAT = "yyyy";
	private static final String PARAM_DATE_FORMAT = "dateFormat";

	@Override
	public String exportValue(final Object value)
	{
		return value == null ? StringUtils.EMPTY : value.toString();
	}

	@Override
	public Object importValue(final String valueExpr, final Item toItem)
	{
		clearStatus();
		String dateFormat = getColumnDescriptor().getDescriptorData().getModifier(PARAM_DATE_FORMAT);
		if (StringUtils.isEmpty(dateFormat))
		{
			dateFormat = DEFAULT_DATE_FORMAT;
		}
		if (StringUtils.isNotEmpty(valueExpr))
		{
			return MessageFormat.format(valueExpr, DateTime.now().toString(dateFormat));
		}
		return StringUtils.EMPTY;
	}
}
