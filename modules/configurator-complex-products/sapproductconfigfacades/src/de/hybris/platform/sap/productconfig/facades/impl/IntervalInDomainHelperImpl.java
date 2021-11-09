/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.IntervalInDomainHelper;
import de.hybris.platform.sap.productconfig.facades.ValueFormatTranslator;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.util.localization.Localization;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Default implementation of the {@link IntervalInDomainHelper}.<br>
 */
public class IntervalInDomainHelperImpl implements IntervalInDomainHelper
{
	private static final Pattern INTERVAL_SPLIT_PATTERN = Pattern.compile("(-?[\\d,.]+)\\s?-\\s?(-?[\\d,.]*)");
	private static final Pattern INFINITY_PATTERN = Pattern.compile("([<>≤≥]=?\\s?)(-?[\\d,.]+)");

	public static final int INTERVAL_FROM_GROUP = 1;
	public static final int INTERVAL_TO_GROUP = 2;

	private ValueFormatTranslator valueFormatTranslator;

	@Override
	public String retrieveIntervalMask(final CsticModel cstic)
	{
		final StringBuilder intervalBuffer = new StringBuilder();
		if (cstic.getAssignableValues() != null)
		{
			for (final CsticValueModel valueModel : cstic.getAssignableValues())
			{
				if (valueModel.isDomainValue())
				{
					appendToIntervalMask(intervalBuffer, valueModel);
				}
			}
		}
		return intervalBuffer.toString().trim();

	}

	protected void appendToIntervalMask(final StringBuilder intervalBuffer, final CsticValueModel valueModel)
	{
		if (intervalBuffer.length() > 0)
		{
			intervalBuffer.append(" ; ");
		}
		intervalBuffer.append(formatNumericInterval(valueModel.getName()));
	}


	@Override
	public String formatNumericInterval(final String interval)
	{
		String formattedInterval = interval;
		Matcher match = INTERVAL_SPLIT_PATTERN.matcher(interval.trim());

		if (match.matches())
		{
			formattedInterval = getValueFormatTranslator().formatNumeric(match.group(INTERVAL_FROM_GROUP).trim());
			final String formattedValueMax = getValueFormatTranslator().formatNumeric(match.group(INTERVAL_TO_GROUP).trim());
			if (!formattedValueMax.isEmpty())
			{
				formattedInterval = formattedInterval + " - " + formattedValueMax;
			}
		}
		else
		{
			match = INFINITY_PATTERN.matcher(interval);
			if (match.matches())
			{
				formattedInterval = match.group(INTERVAL_FROM_GROUP)
						+ getValueFormatTranslator().formatNumeric(match.group(INTERVAL_TO_GROUP).trim());
			}
		}
		return formattedInterval;
	}

	@Override
	public String retrieveErrorMessage(final String value, final String interval)
	{
		return Localization.getLocalizedString("type.ProductConfiguration.IntervalValue.conflict", new Object[]
		{ value, interval });
	}

	protected ValueFormatTranslator getValueFormatTranslator()
	{
		return valueFormatTranslator;
	}

	/**
	 * @param valueFormatTranslator
	 */
	public void setValueFormatTranslator(final ValueFormatTranslator valueFormatTranslator)
	{
		this.valueFormatTranslator = valueFormatTranslator;
	}
}
